/*
 * Created during Google Summer Of Code 2011.
 * For Tcl/Tk Community by student Michał Poczwardowski
 * License: BSD
 */
package org.netbeans.modules.languages.tcl.syntaxhilight.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.netbeans.modules.languages.tcl.syntaxhilight.TclTokenId;

/**
 * Handle categories for different styles.
 * Inspired by version from Netbeans Wiki. 
 */
public class TokenFileReaderAntlr {

	private HashMap<String, String> tokenCategories=new HashMap<String, String>();
	private ArrayList<TclTokenId> tokensArray=new ArrayList<TclTokenId>();

	public TokenFileReaderAntlr() {
		categoriesMapping();
	}

	/**
	 * MAP → include any keywords…
	 */
	private void categoriesMapping() {

		// Token categories… corresponding with Tcl.tokensArray

		tokenCategories.put( "VARIABLE", "variable" );
		tokenCategories.put( "ID", "identifier" );
		tokenCategories.put( "COMMENT", "comment" );
		tokenCategories.put( "WS", "whitespace" );
		tokenCategories.put( "BRACE", "brace" );
		tokenCategories.put( "STRING", "string" );
		tokenCategories.put( "ESC_SEQ", "escape-sequence" );
		tokenCategories.put( "NAMESPACE", "namespace" );

		tokenCategories.put( "OPERATOR", "operator" );
		tokenCategories.put( "SEMICOLON", "operator" );

		tokenCategories.put( "EXPONENT", "number" );
		tokenCategories.put( "FLOAT", "number" );
		tokenCategories.put( "HEX_NUMBER", "number" );
		tokenCategories.put( "INT", "number" );


		tokenCategories.put( "TCL_COMMAND", "tcl-command" );
		tokenCategories.put( "TK_COMMAND", "tk-command" );
		tokenCategories.put( "ITCL_COMMAND", "itcl-command" );

	}

	/**
	 * Reads in the token file.
	 */
	private void readTokenFile( BufferedReader buff ) {
		String line=null;
		try {
			TclTokenId id;
			while( ( line=buff.readLine() ) != null ) {
				String[] splLine=line.split( "=" );
				String name=splLine[0];
				int tok=Integer.parseInt( splLine[1].trim() );
				String category=tokenCategories.get( name );
				if( category != null ) {
					id=new TclTokenId( name, category, tok );
				} else { // else mark this as a whitespace
					id=new TclTokenId( name, "whitespace", tok );
				}
				tokensArray.add( id );
			}
		} catch( Exception ex ) {
			return;
		}
	}
	/**
	 * Reads the Tcl.token file from the ANTLR parser and generates
	 * appropriate tokensArray.
	 */
	public List<TclTokenId> readTokenFile() {
		ClassLoader classLoader=Thread.currentThread().getContextClassLoader();
		InputStream inputStream=classLoader.getResourceAsStream( "org/netbeans/modules/languages/tcl/syntaxhilight/Tcl.tokens" );
		BufferedReader reader=new BufferedReader( new InputStreamReader( inputStream ) );
		readTokenFile( reader );
		return tokensArray;
	}


}
