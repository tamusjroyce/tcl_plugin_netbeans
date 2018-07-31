/*
 * Created during Google Summer Of Code 2011.
 * For Tcl/Tk Community by student Michał Poczwardowski
 * License: BSD
 */
package org.netbeans.modules.languages.tcl.syntaxhilight;

import org.netbeans.api.lexer.TokenId;

/**
 * Tcl Token ID.
 * @author dmp
 */
public class TclTokenId implements TokenId {

	private final String name;
	private final String primaryCategory;
	private final int id;

	public TclTokenId( String name, String primaryCategory, int id ) {
		this.name=name;
		this.primaryCategory=primaryCategory;
		this.id=id;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public int ordinal() {
		return id;
	}

	@Override
	public String primaryCategory() {
		return primaryCategory;
	}

}
