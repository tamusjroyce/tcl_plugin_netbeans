/*
 * Created during Google Summer Of Code 2011.
 * For Tcl/Tk Community by student Michał Poczwardowski
 * License: BSD
 */
package org.netbeans.modules.languages.tcl.debugger;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Scanner;
import org.openide.util.Exceptions;

/**
 * Connection to debugServer.tcl.
 * @author dmp
 */
public class ClientToTclPart {

	Socket socket;
	PrintWriter bufferedWriter=null;
	Scanner bufferedReader=null;

	public ClientToTclPart( int portNumber ) {

		try {
			InetAddress inetAddress;
			inetAddress=InetAddress.getByName( "localhost" );
			SocketAddress socketAddress=new InetSocketAddress( inetAddress, portNumber );
			socket=new Socket();
			socket.connect( socketAddress );
			//bufferedWriter=new BufferedWriter( new OutputStreamWriter( socket.getOutputStream() ) );
			bufferedWriter=new PrintWriter( socket.getOutputStream(), true );
			bufferedReader=new Scanner( socket.getInputStream() );// new BufferedReader( new InputStreamReader( socket.getInputStream() ) );

		} catch( IOException ex ) {
		}
	}

	public void printlnToSocket( String writeString ) {

		bufferedWriter.println( writeString );
		bufferedWriter.flush();
	}

	public String nextLineFromFromSocket() {
		return bufferedReader.nextLine();
	}

	public boolean isClosed() {
		if( bufferedReader == null ) {
			return true;
		} else {
			return false;
		}
	}

	public void close() {
		bufferedReader.close();
		bufferedReader=null;
		bufferedWriter.close();
		try {
			socket.close();
		} catch( IOException ex ) {
			Exceptions.printStackTrace( ex );
		}
	}

}
