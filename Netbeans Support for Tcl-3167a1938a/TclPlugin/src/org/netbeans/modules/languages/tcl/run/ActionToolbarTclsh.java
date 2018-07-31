/*
 * Created during Google Summer Of Code 2011.
 * For Tcl/Tk Community by student Michał Poczwardowski
 * License: BSD
 */
package org.netbeans.modules.languages.tcl.run;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;

@ActionID( category="Run",
id="org.netbeans.modules.languages.tcl.run.ActionToolbarTclsh" )
@ActionRegistration( iconBase="org/netbeans/modules/languages/tcl/optionpanel/tcl.logo32p.png",
displayName="#CTL_ActionToolbarTclsh" )
@ActionReferences( {
	@ActionReference( path="Toolbars/Tcl", position=444 )
} )
@Messages( "CTL_ActionToolbarTclsh=Show me tclsh" )
/**
 * Run Tclsh at Netbeans Toolbar.
 * @author dmp
 */
public final class ActionToolbarTclsh implements ActionListener {

	@Override
	public void actionPerformed( ActionEvent e ) {
		new RunTclsh();
	}

}
