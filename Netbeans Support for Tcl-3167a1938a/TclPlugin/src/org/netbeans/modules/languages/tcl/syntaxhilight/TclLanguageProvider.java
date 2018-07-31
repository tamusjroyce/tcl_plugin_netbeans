/*
 * Created during Google Summer Of Code 2011.
 * For Tcl/Tk Community by student Michał Poczwardowski
 * License: BSD
 */
package org.netbeans.modules.languages.tcl.syntaxhilight;

import org.netbeans.api.lexer.InputAttributes;
import org.netbeans.api.lexer.Language;
import org.netbeans.api.lexer.LanguagePath;
import org.netbeans.api.lexer.Token;
import org.netbeans.spi.lexer.LanguageEmbedding;
import org.netbeans.spi.lexer.LanguageProvider;

/**
 * Tcl Language Provider.
 * @author dmp
 */
@org.openide.util.lookup.ServiceProvider( service=org.netbeans.spi.lexer.LanguageProvider.class )
public class TclLanguageProvider extends LanguageProvider {

	@Override
	public Language<?> findLanguage( String mimeType ) {
		if( "text/x-tcl".equals( mimeType ) ) {
			return new TclLanguageHierarchy().language();
		}

		return null;
	}

	@Override
	public LanguageEmbedding<?> findLanguageEmbedding( Token<?> token, LanguagePath lp, InputAttributes ia ) {
		return null;
	}

}
