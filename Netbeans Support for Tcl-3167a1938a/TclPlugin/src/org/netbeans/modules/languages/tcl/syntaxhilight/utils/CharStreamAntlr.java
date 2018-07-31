/*
 * Created during Google Summer Of Code 2011.
 * For Tcl/Tk Community by student Michał Poczwardowski
 * License: BSD
 */
package org.netbeans.modules.languages.tcl.syntaxhilight.utils;

import java.util.ArrayList;
import org.antlr.runtime.CharStream;
import org.netbeans.spi.lexer.LexerInput;

/**
 * Char Stream implementation to handle ANTLR. 
 * Implements org.antlr.runtime.CharStream;
 */
public class CharStreamAntlr implements CharStream {

	//
	private int lineNumber=1;
	private int charInLinePosition=0;
	//
	private LexerInput lexerInput;
	//
	private int index=0;
	private int markDepth=0;
	private int lastMarker;
	//
	private boolean isIgnoreCase=false;

	//
	//
	private class CharStreamStateElement {

		int index;
		int line;
		int inlinePosition;
	}

	private ArrayList<CharStreamStateElement> states;
	//

	public CharStreamAntlr( LexerInput input, boolean ignoreCase ) {
		this.lexerInput=input;
		this.isIgnoreCase=ignoreCase;
	}

	private void doBackup( int count ) {
		lexerInput.backup( count );
	}

	private int readFromLexerInput() {
		int data=lexerInput.read();
		if( data == LexerInput.EOF ) {
			data=CharStream.EOF;
		}
		return data;
	}

	// Overrides
	@Override
	public String substring( int start, int stop ) {
		return lexerInput.readText( start, stop ).toString();
	}

	@Override
	public int LT( int i ) {
		return LA( i );
	}

	@Override
	public void consume() {
		int data=lexerInput.read();
		index++;
		charInLinePosition++;

		if( data == '\n' ) {
			charInLinePosition=0;
			lineNumber++;
		}
	}

	@Override
	public int LA( int i ) {

		if( i == 0 ) {
			return 0;
		}

		int c=0;
		for( int j=0; j < i; j++ ) {
			c=readFromLexerInput();
		}
		doBackup( i );

		if( isIgnoreCase && ( c != -1 ) ) {
			return Character.toLowerCase( ( char )c );
		} else {
			return c;
		}
	}

	@Override
	public int mark() {
		if( states == null ) {
			states=new ArrayList<CharStreamStateElement>();
			states.add( null );
		}
		markDepth++;
		CharStreamStateElement state=null;
		if( markDepth >= states.size() ) {
			state=new CharStreamStateElement();
			states.add( state );
		} else {
			state=states.get( markDepth );
		}

		// fullfill state:
		state.inlinePosition=charInLinePosition;
		state.line=lineNumber;
		state.index=index;

		lastMarker=markDepth;
		return markDepth;
	}

	@Override
	public void rewind() {
		rewind( lastMarker );
	}

	@Override
	public void rewind( int marker ) {
		CharStreamStateElement state=states.get( marker );
		seek( state.index );
		lineNumber=state.line;
		charInLinePosition=state.inlinePosition;
		release( marker );
	}

	@Override
	public void release( int marker ) {
		markDepth=marker;
		markDepth--;
	}

	@Override
	public void seek( int index ) {
		if( index < this.index ) {
			doBackup( this.index - index );
			this.index=index;
			return;
		}

		while( this.index < index ) {
			consume();
		}
	}

	@Override
	public int index() {
		return index;
	}

	@Override
	public int size() {
		return -1;
	}

	@Override
	public String getSourceName() {
		return "TclEditor";
	}

	//
	// getters/setters
	//
	@Override
	public int getLine() {
		return lineNumber;
	}

	@Override
	public void setLine( int line ) {
		this.lineNumber=line;
	}

	@Override
	public void setCharPositionInLine( int pos ) {
		this.charInLinePosition=pos;
	}

	@Override
	public int getCharPositionInLine() {
		return charInLinePosition;
	}

}
