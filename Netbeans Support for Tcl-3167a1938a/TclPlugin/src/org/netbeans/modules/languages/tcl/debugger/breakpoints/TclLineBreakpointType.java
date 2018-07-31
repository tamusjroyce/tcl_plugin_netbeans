/*
 * Created during Google Summer Of Code 2011.
 * For Tcl/Tk Community by student Michał Poczwardowski
 * License: BSD
 */
package org.netbeans.modules.languages.tcl.debugger.breakpoints;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import org.netbeans.modules.languages.tcl.project.TclProject;
import org.netbeans.spi.debugger.ui.BreakpointType;

/**
 * Class stores informations about TclBreakpoint Type.
 * "Line" word might cause misunderstandings. It's not a breakpoint but just type description.
 * @author dmp
 */
public class TclLineBreakpointType extends BreakpointType {

	@Override
	public String getCategoryDisplayName() {
		return "Tcl";
	}

	@Override
	public JComponent getCustomizer() {
		
			JOptionPane.showMessageDialog( null,
			       "Add line annotatoin to add new breakpoint",
			       "Information",
			       JOptionPane.INFORMATION_MESSAGE, TclProject.getImageIcon() );
		return null;
	}

	@Override
	public String getTypeDisplayName() {
		return "Tcl Line Breakpoint";
	}

	@Override
	public boolean isDefault() {
		return true;
	}

}
