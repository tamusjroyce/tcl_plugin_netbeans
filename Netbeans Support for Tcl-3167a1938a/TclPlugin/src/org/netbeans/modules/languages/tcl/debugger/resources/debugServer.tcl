#!/usr/bin/env tclsh

# Created during Google Summer Of Code 2011.
# For Tcl/Tk Community by student Michał Poczwardowski
# License: BSD

# Debug Server for Netbeanstcl

namespace eval ::debug {

  set doNotWait 0
  set stepOverLevel -1

# breakpoints:
  set breakpointsStatus 0
  variable breakpoint

# execution args:
  variable portNumber
  variable tclScript
  variable currentDirectory
  variable isVerbose

# setData variables
  variable fileName
  variable lineNumber
  variable infoFrame 
  variable infoLevel 

# other globals
  variable firstRunAfterWait
  variable socketFd

# callStack variables
  variable callStack
  variable callStackIndex
  variable startLevel
  variable previousCmdStr
  variable previousInfoLevel 
  variable previousFileName
  variable previousLineNumber


proc listVars {type level} {

#	set level [expr [info level] - 2]
#	puts "listVars: info level: [info level]"
	set listVars [uplevel $level info $type]
	set formatted ""
	set count 0
	foreach var $listVars {
			set isArray [uplevel $level array exists $var]
			if { $isArray } {
				set names [uplevel $level array names $var]

				#puts "$var = ARRAY"
				set toAppend "$var = <ARRAY>\n"
				#if{ [string compare $type "locals"] == 0 } {
				#	set toAppend "$var.$level = <ARRAY>\n"
				#}
				append formatted $toAppend
				incr count

				#puts "[set var]KEYS = $names"
				set toAppend "[set var]_KEYS = $names\n"
				#if{ [string compare $type "locals"] == 0 } {
				#	set toAppend "$var.$level_KEYS = <ARRAY>\n"
				#}

				append formatted $toAppend
				incr count

				foreach n $names {
					set varname [set var]($n)
					set value [uplevel $level set $varname]
					#puts "$varname = $value"
					set toAppend "$varname = $value\n"

					append formatted $toAppend
					incr count
				}
			} else {	
				set value [uplevel $level set $var]
				set valueFiltered [string map {"\n" "\\n"} $value]
				set toAppend "$var = $valueFiltered\n"
				#if { [string compare $type "locals"] == 0 } {
				#	set toAppend "$var.$level = $valueFiltered\n"
				#}

				append formatted $toAppend
				incr count
			}
		}
	append count "\n$formatted"
	return $count
}


proc setData args {
	set debug::infoLevel [info level]
	set debug::infoFrame [info frame]
	set infoDict [info frame [expr $debug::infoFrame - 3]]
	#puts "start:$debug::startLevel current:$debug::infoFrame dif:[expr $debug::infoFrame - $debug::startLevel]" 

	if { [dict exists $infoDict file] } {
		set debug::fileName [dict get $infoDict file]
		if { [dict exists $infoDict line] } {
			set debug::lineNumber [dict get $infoDict line]
		} else {
			#puts "NOLINE"
		}
	} else {
		#puts "NOFILE"
	}

}

proc sendData args {
 	puts $debug::socketFd $debug::fileName
	puts $debug::socketFd $debug::lineNumber

	flush $debug::socketFd
 	putsv ">>> sent ($debug::fileName:$debug::lineNumber)"

}

proc sendVariables args {

# levels:
# 	#0 - global level
# 	#1 - debugger internals, socket
# 	#2 - debug::main
# 	... (different procs inside script)
#	+1 debug::step
# 	+1 debug::sendVariable
# 	+1 debug::listVars (uplevel 3 from listVars gives top-level proc of debugged script)
# 

#	puts "sendVariables: info level: [info level]"
	# 3 jumps to base level for script in debugger
	set flist [listVars locals 3]
	puts -nonewline $debug::socketFd $flist

	set flist [listVars globals #0]
	puts -nonewline $debug::socketFd $flist

	flush $debug::socketFd
 	putsv ">>> sent variables"
}

proc serverProc {channel clientaddr clientport} {
	set debug::socketFd $channel
	puts stderr ">>> connection from $clientaddr:$clientport"

	set debug::serverProcStarted 1
	set debug::doNotWait 1
}

# used for CallStack
proc setPreviousDebugData {cmdStr} {
	set debug::previousCmdStr $cmdStr
	set debug::previousInfoLevel $debug::infoLevel
	set debug::previousFileName $debug::fileName
	set debug::previousLineNumber $debug::lineNumber
}

proc step {cmdStr op} {

	if { $debug::doNotWait == 0 } {
		puts stderr ">>> debugServer.tcl is waiting for connection... (localhost:$::debug::portNumber)"
		set debug::firstRunAfterWait 0
		vwait debug::serverProcStarted
		
		# return because first step is located inside debugServer.tcl = ommited
		return 
	}

	setData
   	putsv ">>> \[ $cmdStr \]"

	#puts "namespace: [namespace current]"

	### section for CallStack
	if { $debug::firstRunAfterWait == 0 } {

		set debug::startLevel $debug::infoLevel
		set debug::callStackIndex 0
		setPreviousDebugData $cmdStr
	
		set debug::firstRunAfterWait 1
	} else {
		if { $debug::previousInfoLevel < $debug::infoLevel } {
			# set new callStack element
			set debug::callStack($debug::callStackIndex,cmdStr) [string map {"\n" "\\n"} $debug::previousCmdStr]
			set debug::callStack($debug::callStackIndex,infoLevel) [expr $debug::previousInfoLevel - $debug::startLevel]
			set debug::callStack($debug::callStackIndex,fileName) $debug::previousFileName
			set debug::callStack($debug::callStackIndex,lineNumber) $debug::previousLineNumber
			incr debug::callStackIndex
		}
		# for callStack
		setPreviousDebugData $cmdStr
	}
	###


	if { $debug::breakpointsStatus != 0 } {
		set breakpointReached 0
		
		# Step everything, till next breakpoint
		if { [info exists debug::breakpoints($debug::fileName)] } {
			#puts "breakpoint In THIS LAP"
			set lines $debug::breakpoints($debug::fileName)
			foreach line $lines {
				if { $line == $debug::lineNumber } {
					set breakpointReached 1
					putsv ">>> reached breakpoint in file $debug::fileName line $line" 
				}
			}
		}
		if { $breakpointReached == 0 } {
			#puts "BOX NOT this lap"
			return
		}
	} 


	# Perform single stepping…
	if { $debug::stepOverLevel != -1 } {
		# Now step everything, till next equal or lower stepOverLevel
		if { $debug::stepOverLevel < $debug::infoFrame } {
			return
		} else {
			# Make stepover inactive
			set debug::stepOverLevel -1
		}
	}	

	while { true } {
	
		set operation [gets $debug::socketFd]
   		putsv ">>> operation: $operation"

		switch $operation {
		   status {
			sendData
		   }
		   variables {
			sendVariables
		   }
		   callstack {
			# get latest CallStack for Java Part of the debugger
			set clientCount [gets $debug::socketFd]
			
			# send back information how many callstacks will be send:
			set sendBack [expr $debug::callStackIndex - $clientCount]
			puts $debug::socketFd $sendBack 
			
			# foreach callStack 
			for { set iter $clientCount} {$iter < $debug::callStackIndex} {incr iter} {
				puts $debug::socketFd $debug::callStack($iter,cmdStr)
				puts $debug::socketFd $debug::callStack($iter,infoLevel)
				puts $debug::socketFd $debug::callStack($iter,fileName)
				puts $debug::socketFd $debug::callStack($iter,lineNumber)
			}
			flush $debug::socketFd
		   }
		   step {
			# Activate single stepping mode.
			set debug::breakpointsStatus 0
			# Break here allows to do next step.
			break
		   }
		   stepover {
			# Activate single stepping mode.
			set debug::breakpointsStatus 0
			# If there is no stepover active:
			if { $debug::stepOverLevel == -1} {
				set debug::stepOverLevel $debug::infoFrame
				break;
			}
		   }
		   breakpoints {
			set debug::breakpointsStatus [gets $debug::socketFd]
			unset -nocomplain ::debug::breakpoints

			set count 1
			while { $count <= $debug::breakpointsStatus } {
				set breakpointFileName [gets $debug::socketFd]
				set breakpointFileLines [gets $debug::socketFd]
				set ::debug::breakpoints($breakpointFileName) [lsort -integer $breakpointFileLines]
				incr count
			}
		   }
		   continue {
			# Run till next breakpoint
			set debug::breakpointsStatus 1
			# Do next step
			break
		   }
		   exit {
			close $debug::socketFd
			exit
		   }
		   default {
			puts $debug::socketFd "Invalid command."
			flush $debug::socketFd
		   }
   		}

	}

  }

  proc putsv args {
	if { $debug::isVerbose } {
   		puts stderr [lindex $args 0]
	}
  }

  proc main {script} {
	  source $script
  }

if { $argc <= 2 } {
	puts ">>> Please use following syntax:"
	puts ">>> ./debugServer.tcl tclFile currentDirectory portNumber optionalVerbose(0or1)"
	exit
}

set tclScript [lindex $::argv 0]
set currentDirectory [lindex $::argv 1]
set portNumber [lindex $::argv 2]

if { $argc == 4 } {
	set isVerbose [lindex $::argv 3]
} else {
	set isVerbose 0
}

cd $currentDirectory
socket -server serverProc $portNumber
trace add execution ::debug::main [list enterstep] ::debug::step

::debug::main $tclScript

}




