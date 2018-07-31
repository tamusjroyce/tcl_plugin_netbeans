/*
 * Created during Google Summer Of Code 2011.
 * For Tcl/Tk Community by student Michał Poczwardowski
 * License: BSD
 */
package org.netbeans.modules.languages.tcl.optionpanel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.JComponent;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;

@OptionsPanelController.TopLevelRegistration( categoryName="#OptionsCategory_Name_Tcl",
iconBase="org/netbeans/modules/languages/tcl/optionpanel/tcl.logo32p.png",
keywords="#OptionsCategory_Keywords_Tcl",
keywordsCategory="Tcl" )
/**
 * Class implements OptionsPanelController.
 * TclPanel.java registration and operation here.
 * @author dmp
 */
public final class TclOptionsPanelController extends OptionsPanelController {

	private TclPanel panel;
	private final PropertyChangeSupport propertyChangeSupport=new PropertyChangeSupport( this );
	private boolean isChanged;


	@Override
	public void update() {
		getPanel().load();
		isChanged=false;
	}

	@Override
	public void applyChanges() {
		getPanel().store();
		isChanged=false;
	}

	@Override
	public void cancel() {
		// close panel
	}

	@Override
	public boolean isValid() {
		return getPanel().valid();
	}

	@Override
	public boolean isChanged() {
		return isChanged;
	}

	@Override
	public HelpCtx getHelpCtx() {
		return null; 
	}

	@Override
	public JComponent getComponent( Lookup masterLookup ) {
		return getPanel();
	}

	@Override
	public void addPropertyChangeListener( PropertyChangeListener l ) {
		propertyChangeSupport.addPropertyChangeListener( l );
	}

	@Override
	public void removePropertyChangeListener( PropertyChangeListener l ) {
		propertyChangeSupport.removePropertyChangeListener( l );
	}

	private TclPanel getPanel() {
		if( panel==null ) {
			panel=new TclPanel( this );
		}
		return panel;
	}

	void changed() {
		if( !isChanged ) {
			isChanged=true;
			propertyChangeSupport.firePropertyChange( OptionsPanelController.PROP_CHANGED, false, true );
		}
		propertyChangeSupport.firePropertyChange( OptionsPanelController.PROP_VALID, null, null );
	}

}
