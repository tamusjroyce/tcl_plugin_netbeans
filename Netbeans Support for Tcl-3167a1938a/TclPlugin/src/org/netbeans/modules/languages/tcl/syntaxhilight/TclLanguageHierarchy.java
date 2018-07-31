/*
 * Created during Google Summer Of Code 2011.
 * For Tcl/Tk Community by student Michał Poczwardowski
 * License: BSD
 */
package org.netbeans.modules.languages.tcl.syntaxhilight;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.netbeans.api.lexer.Language;
import org.netbeans.spi.lexer.LanguageHierarchy;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;

import org.netbeans.modules.languages.tcl.syntaxhilight.utils.TokenFileReaderAntlr;

/**
 * Tcl Language Hierarchy.
 * @author dmp
 */
public class TclLanguageHierarchy extends LanguageHierarchy {

	private static List<TclTokenId> tokens;
	private static Map<Integer, TclTokenId> idToToken;
	@SuppressWarnings( "unchecked" )
	private static final Language<TclTokenId> language=new TclLanguageHierarchy().language();

	public static Language<TclTokenId> getLanguage() {
		return language;
	}

	private static void init() {
		TokenFileReaderAntlr reader=new TokenFileReaderAntlr();
		tokens=reader.readTokenFile();
		idToToken=new HashMap<Integer, TclTokenId>();
		for( TclTokenId token:tokens ) {
			idToToken.put( token.ordinal(), token );
		}
	}

	static synchronized TclTokenId getToken( int id ) {
		if( idToToken == null ) {
			init();
		}
		return idToToken.get( id );
	}

	@Override
	protected synchronized Collection<TclTokenId> createTokenIds() {
		if( tokens == null ) {
			init();
		}
		return tokens;
	}

	@SuppressWarnings( "unchecked" )
	@Override
	protected Lexer<TclTokenId> createLexer( LexerRestartInfo info ) {
		return new TclEditorLexer( info );
	}

	@Override
	protected String mimeType() {
		return "text/x-tcl";
	}

}
