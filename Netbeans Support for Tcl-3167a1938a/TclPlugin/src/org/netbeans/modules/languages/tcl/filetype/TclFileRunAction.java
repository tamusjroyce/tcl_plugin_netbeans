/*
 * Created during Google Summer Of Code 2011.
 * For Tcl/Tk Community by student Michał Poczwardowski
 * License: BSD
 */
package org.netbeans.modules.languages.tcl.filetype;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.NbBundle.Messages;
import org.netbeans.modules.languages.tcl.run.RunTclsh;


@ActionID( category="Run",
id="org.netbeans.modules.languages.tcl.filetype.TclFileRunAction" )
@ActionRegistration( displayName="#CTL_TclFileRunAction" )
@ActionReferences( {
	@ActionReference( path="Loaders/text/x-tcl/Actions", position=120 )
} )
@Messages( "CTL_TclFileRunAction=Run Script" )
/**
 * "Run Script" action on every Tcl file.
 */
public final class TclFileRunAction implements ActionListener {

	private final TclDataObject context;

	public TclFileRunAction( TclDataObject context ) {
		this.context=context;
	}

	@Override
	public void actionPerformed( ActionEvent ev ) {
		FileObject f = context.getPrimaryFile();
   		String filePath = FileUtil.getFileDisplayName(f);
   		//String msg = "source: " + filePath + " Tcl script executed by " +
		//      NbPreferences.forModule(TclPanel.class).get(TclPanel.preferenceTclshPath, TclPanel.tclshDefaultPath);
		//System.out.println(msg);
		new RunTclsh( f.getNameExt(), filePath );
	}

}
