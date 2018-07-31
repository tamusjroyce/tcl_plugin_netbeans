/*
 * Created during Google Summer Of Code 2011.
 * For Tcl/Tk Community by student Michał Poczwardowski
 * License: BSD
 */
package org.netbeans.modules.languages.tcl.syntaxhilight;

import org.netbeans.api.lexer.Token;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;

import org.netbeans.modules.languages.tcl.syntaxhilight.utils.CharStreamAntlr;

/**
 * Implementation of org.netbeans.spi.lexer.Lexer.
 * @author dmp
 */
public class TclEditorLexer implements Lexer<TclTokenId> {

	private LexerRestartInfo<TclTokenId> lexerInfo;
	private Tcl lexer;

	public TclEditorLexer( LexerRestartInfo<TclTokenId> info ) {
		this.lexerInfo=info;
		CharStreamAntlr charStream=new CharStreamAntlr( info.input(), false );
		lexer=new Tcl( charStream );
	}

	@Override
	public org.netbeans.api.lexer.Token<TclTokenId> nextToken() {
		org.antlr.runtime.Token token=lexer.nextToken();

		Token<TclTokenId> createdToken=null;

		if( token.getType() != -1 ) {
			TclTokenId tokenId=TclLanguageHierarchy.getToken( token.getType() );
			createdToken=lexerInfo.tokenFactory().createToken( tokenId );
		} else if( lexerInfo.input().readLength() > 0 ) {
			TclTokenId tokenId=TclLanguageHierarchy.getToken( Tcl.WS );
			createdToken=lexerInfo.tokenFactory().createToken( tokenId );
		}

		return createdToken;
	}

	@Override
	public Object state() {
		return null;
	}

	@Override
	public void release() {
		return;
	}

}
