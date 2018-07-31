#!/usr/bin/env tclsh

# Created during Google Summer Of Code 2011.
# For Tcl/Tk Community by student Michał Poczwardowski
# License: BSD

# Debug Server for Netbeanstcl

namespace eval ::debug {

  set doNotWait 0
  set portNumber 31337

  variable fileName
  variable lineNumber

  variable socketFd

proc setData args {
	set infoDict [info frame [expr [info frame] - [info level]]]
#	set level [info frame]
	set debug::lineNumber [dict get $infoDict line]
	set debug::fileName [dict get $infoDict file]
#	puts ">>> setData $::debug::fileName:$::debug::lineNumber"
}

  proc serverProc {channel clientaddr clientport} {
      set debug::socketFd $channel
      puts ">>> connection from $clientaddr:$clientport"

      set debug::serverProcStarted 1
      set debug::doNotWait 1
  }

  proc step {cmd_str op} {

      if { $debug::doNotWait == 0 } {
         puts ">>> Waiting for connection…"
         vwait debug::serverProcStarted
      }

	while { true } {
	
		set operation [gets $debug::socketFd]
   		puts ">>> Operation: $operation."
  	 	setData

		switch $operation {
		   status {
			puts $debug::socketFd $debug::fileName
			puts $debug::socketFd $debug::lineNumber
			flush $debug::socketFd
		   }
		   step {
			puts $debug::socketFd $debug::fileName
			puts $debug::socketFd $debug::lineNumber
			flush $debug::socketFd
			break;
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

      return
  }

  proc main {script} {
	  source $script
  }


socket -server serverProc $debug::portNumber

trace add execution ::debug::main [list enterstep] ::debug::step

if { $argc != 1 } {
	puts ">>> First argument for debugServer.tcl should be a tcl file."
	exit
}

::debug::main [lindex $::argv 0]

}




