#!/usr/bin/env tclsh

namespace eval debug {

proc serverProc {channel clientaddr clientport} {

	setData
	puts ">>> connection from $clientaddr:$clientport"

	set operation "init"
	while { ![string equal $operation "exit"] } {
	
		set operation [gets $channel]
   		puts ">>> $operation operation"
  	 
		switch $operation {
		   run {
		   }
		   step {
			puts $channel "$::debug::fileName $::debug::lineNumber"
			flush $channel
			set ::debug::nextstep 1
		   }
		   default {
			puts $channel "Invalid command"
			flush $channel
		   }
   		}
	}
	puts ">>> connection closed"
	close $channel
}

proc setData args {
	set infoDict [info frame [expr [info frame] - [info level]]]
	set level [info frame]
	set ::debug::lineNumber [dict get $infoDict line]
	set ::debug::fileName [info script]
	puts ">>> setData $::debug::fileName:$::debug::lineNumber"
}


proc step args {
	puts  ">>> step"
	setData
	puts ">>> variable wait"
	vwait ::debug::nextstep
	puts ">>> vwait ended"

	# I'm not sure that unset will work in this case
	unset ::debug::nextstep
}

proc main args {
	puts ">>> source long.tcl"
	source long.tcl
}


socket -server serverProc 31337

trace add execution main {enterstep} step
debug::main
vwait ::forever

}
