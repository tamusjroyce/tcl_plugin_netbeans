/*
 * Created during Google Summer Of Code 2011.
 * For Tcl/Tk Community by student Michał Poczwardowski
 * License: BSD
 */
package org.netbeans.modules.languages.tcl.optionpanel;

import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import org.openide.util.NbPreferences;
import org.openide.util.ImageUtilities;

/**
 * Tcl Plugin Preferences.
 * Displayed inside OptionPanel (Tools→Options).
 * @author dmp
 */
public final class TclPanel extends javax.swing.JPanel {

	//
	// Defaults:
	public static String tclshDefaultPath="/usr/bin/tclsh";
	private static String tclPartDefaultPortNumber="31337";
	private static String tclPartDefaultVerbose="0";
	//
	//
	private String tclshPath=getTclshDefaultPath();
	private String tclPartPortNumber=getTclPartDefaultPortNumber();
	private String tclPartVerbose=getTclPartDefaultVerbose();
	//
	// Preference names:
	public static String preferenceTclshPath="tclsh.path";
	public static String preferenceTclPartPortNumber="debugger.port";
	public static String preferenceTclPartVerbose="debugger.verbose";
	//
	//
	// Controller and validation request
	private boolean checkValidPath=false;
	private boolean checkValidPortNumber=false;
	private final TclOptionsPanelController controller;

	//
	//
	static public ImageIcon getImageIcon() {
		return new ImageIcon( ImageUtilities.loadImage( "org/netbeans/modules/languages/tcl/optionpanel/tcl.logo32p.png" ) );
	}

	public static String getTclshDefaultPath() {
		return TclPanel.tclshDefaultPath;
	}

	public static String getTclPartDefaultPortNumber() {
		return TclPanel.tclPartDefaultPortNumber;
	}

	public static String getTclPartDefaultVerbose() {
		return TclPanel.tclPartDefaultVerbose;
	}

	TclPanel( TclOptionsPanelController controller ) {
		this.controller=controller;
		initComponents();
	}

        // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                jLabelDescription = new javax.swing.JLabel();
                jTextTclshLocation = new javax.swing.JTextField();
                jButtonSetTclsh = new javax.swing.JButton();
                jButtonRestoreDefault = new javax.swing.JButton();
                jLabelDebuggerSettings = new javax.swing.JLabel();
                jTextFieldPortNumber = new javax.swing.JTextField();
                jLabelPortNumber = new javax.swing.JLabel();
                jCheckBoxVerboseMode = new javax.swing.JCheckBox();
                jButtonSetPortNumber = new javax.swing.JButton();

                org.openide.awt.Mnemonics.setLocalizedText(jLabelDescription, org.openide.util.NbBundle.getMessage(TclPanel.class, "TclPanel.jLabelDescription.text")); // NOI18N

                jTextTclshLocation.setText(org.openide.util.NbBundle.getMessage(TclPanel.class, "TclPanel.jTextTclshLocation.text")); // NOI18N

                org.openide.awt.Mnemonics.setLocalizedText(jButtonSetTclsh, org.openide.util.NbBundle.getMessage(TclPanel.class, "TclPanel.jButtonSetTclsh.text")); // NOI18N
                jButtonSetTclsh.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jButtonSetTclshActionPerformed(evt);
                        }
                });

                org.openide.awt.Mnemonics.setLocalizedText(jButtonRestoreDefault, org.openide.util.NbBundle.getMessage(TclPanel.class, "TclPanel.jButtonRestoreDefault.text")); // NOI18N
                jButtonRestoreDefault.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jButtonRestoreDefaultActionPerformed(evt);
                        }
                });

                org.openide.awt.Mnemonics.setLocalizedText(jLabelDebuggerSettings, org.openide.util.NbBundle.getMessage(TclPanel.class, "TclPanel.jLabelDebuggerSettings.text")); // NOI18N

                jTextFieldPortNumber.setText(org.openide.util.NbBundle.getMessage(TclPanel.class, "TclPanel.jTextFieldPortNumber.text")); // NOI18N

                org.openide.awt.Mnemonics.setLocalizedText(jLabelPortNumber, org.openide.util.NbBundle.getMessage(TclPanel.class, "TclPanel.jLabelPortNumber.text")); // NOI18N

                org.openide.awt.Mnemonics.setLocalizedText(jCheckBoxVerboseMode, org.openide.util.NbBundle.getMessage(TclPanel.class, "TclPanel.jCheckBoxVerboseMode.text")); // NOI18N
                jCheckBoxVerboseMode.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jCheckBoxVerboseModeActionPerformed(evt);
                        }
                });

                org.openide.awt.Mnemonics.setLocalizedText(jButtonSetPortNumber, org.openide.util.NbBundle.getMessage(TclPanel.class, "TclPanel.jButtonSetPortNumber.text")); // NOI18N
                jButtonSetPortNumber.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jButtonSetPortNumberActionPerformed(evt);
                        }
                });

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
                this.setLayout(layout);
                layout.setHorizontalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jButtonRestoreDefault)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                                        .addGap(118, 118, 118)
                                                                        .addComponent(jLabelPortNumber)
                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                        .addComponent(jTextFieldPortNumber))
                                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                                        .addComponent(jLabelDescription)
                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addComponent(jTextTclshLocation, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addComponent(jCheckBoxVerboseMode))
                                                        .addComponent(jLabelDebuggerSettings))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jButtonSetPortNumber, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jButtonSetTclsh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addContainerGap(47, Short.MAX_VALUE))
                );
                layout.setVerticalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(42, 42, 42)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabelDescription)
                                        .addComponent(jTextTclshLocation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButtonSetTclsh))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonRestoreDefault)
                                .addGap(28, 28, 28)
                                .addComponent(jLabelDebuggerSettings)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabelPortNumber)
                                        .addComponent(jTextFieldPortNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButtonSetPortNumber))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jCheckBoxVerboseMode)
                                .addContainerGap(19, Short.MAX_VALUE))
                );
        }// </editor-fold>//GEN-END:initComponents

	private void jButtonSetTclshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSetTclshActionPerformed
		tclshPath=jTextTclshLocation.getText();
		this.checkValidPath=true;
		controller.changed();
	}//GEN-LAST:event_jButtonSetTclshActionPerformed

	private void jButtonRestoreDefaultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRestoreDefaultActionPerformed
		jTextTclshLocation.setText( getTclshDefaultPath() );
		JOptionPane.showMessageDialog( this, "Default tclsh path restored", "Information", JOptionPane.INFORMATION_MESSAGE, getImageIcon() );
	}//GEN-LAST:event_jButtonRestoreDefaultActionPerformed

	private void jCheckBoxVerboseModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxVerboseModeActionPerformed
		if( jCheckBoxVerboseMode.isSelected() ) {
			this.tclPartVerbose="1";
		} else {
			this.tclPartVerbose="0";
		}
		controller.changed();
	}//GEN-LAST:event_jCheckBoxVerboseModeActionPerformed

	private void jButtonSetPortNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSetPortNumberActionPerformed
		tclPartPortNumber=jTextFieldPortNumber.getText();
		this.checkValidPortNumber=true;
		controller.changed();
	}//GEN-LAST:event_jButtonSetPortNumberActionPerformed

	void load() {
		tclshPath=NbPreferences.forModule( TclPanel.class ).get( preferenceTclshPath, getTclshDefaultPath() );
		tclPartPortNumber=NbPreferences.forModule( TclPanel.class ).get( preferenceTclPartPortNumber, getTclPartDefaultPortNumber() );
		tclPartVerbose=NbPreferences.forModule( TclPanel.class ).get( preferenceTclPartVerbose, getTclPartDefaultVerbose() );
		jTextTclshLocation.setText( tclshPath );
		jTextFieldPortNumber.setText( tclPartPortNumber );
		if( tclPartVerbose.equals( "1" ) ) {
			jCheckBoxVerboseMode.setSelected( true );
		} else {
			jCheckBoxVerboseMode.setSelected( false );
		}
	}

	void store() {
		NbPreferences.forModule( TclPanel.class ).put( preferenceTclshPath, tclshPath );
		NbPreferences.forModule( TclPanel.class ).put( preferenceTclPartPortNumber, tclPartPortNumber );
		NbPreferences.forModule( TclPanel.class ).put( preferenceTclPartVerbose, tclPartVerbose );
	}

	boolean valid() {
		if( validTclshFile() && validPortNumber() ) {
			return true;
		} else {
			return false;
		}
	}

	boolean validTclshFile() {
		if( checkValidPath == true ) {
			// Check existence of tclsh file
			File tclshFile=new File( jTextTclshLocation.getText() );
			if( tclshFile.exists() ) {
				JOptionPane.showMessageDialog( this, "Tclsh path set to: " + tclshPath + "\nClick OK twice to store changes.", "Information", JOptionPane.INFORMATION_MESSAGE, getImageIcon() );
				checkValidPath=false;
				return true;
			} else {
				JOptionPane.showMessageDialog( this, "Wrong tclsh location!", "Error", JOptionPane.ERROR_MESSAGE );
				return false;
			}
		} else {
			return true;
		}
	}

	boolean validPortNumber() {
		if( checkValidPortNumber == true ) {
			String portString=jTextFieldPortNumber.getText();
			try {
				System.out.println( "portString" + portString );
				int portNumber=Integer.parseInt( portString );
				System.out.println( "portNumber" + portString );
				if( portNumber > 65535 || portNumber < 0 ) {
					JOptionPane.showMessageDialog( this, "Invalid port number!", "Error", JOptionPane.ERROR_MESSAGE );
					return false;
				}
				this.tclPartPortNumber=portString;
				checkValidPortNumber=false;
				JOptionPane.showMessageDialog( this, "Port number set to: " + portString + "\nClick OK twice to store changes.", "Information", JOptionPane.INFORMATION_MESSAGE, getImageIcon() );
				return true;
			} catch( Exception e ) {
				JOptionPane.showMessageDialog( this, "Invalid port number!", "Error", JOptionPane.ERROR_MESSAGE );
				return false;
			}
		} else {
			return true;
		}
	}

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JButton jButtonRestoreDefault;
        private javax.swing.JButton jButtonSetPortNumber;
        private javax.swing.JButton jButtonSetTclsh;
        private javax.swing.JCheckBox jCheckBoxVerboseMode;
        private javax.swing.JLabel jLabelDebuggerSettings;
        private javax.swing.JLabel jLabelDescription;
        private javax.swing.JLabel jLabelPortNumber;
        private javax.swing.JTextField jTextFieldPortNumber;
        private javax.swing.JTextField jTextTclshLocation;
        // End of variables declaration//GEN-END:variables
}
