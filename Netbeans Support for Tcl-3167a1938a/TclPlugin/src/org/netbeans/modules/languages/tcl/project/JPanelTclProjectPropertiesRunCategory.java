/*
 * Created during Google Summer Of Code 2011.
 * For Tcl/Tk Community by student Michał Poczwardowski
 * License: BSD
 */

/*
 * JPanelTclProjectPropertiesRunCategory.java
 *
 * Created on Jun 30, 2011, 1:09:59 AM
 */
package org.netbeans.modules.languages.tcl.project;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author dmp
 */
public class JPanelTclProjectPropertiesRunCategory extends javax.swing.JPanel {

	private Properties properties;
	private final TclProject project;
	private final TclProject.TclProjectInformation info;

	/** Creates new form JPanelTclProjectPropertiesRunCategory */
	public JPanelTclProjectPropertiesRunCategory( TclProject project ) {
		this.project=project;
		info=this.project.getTclProjectInformation();
		this.properties=project.getTclProjectProperties();

		initComponents();

		// set default values
		jLabelProjectName.setText( info.getDisplayName() );
		jLabelProjectPath.setText( project.getProjectDirectory().getPath() );
		jLabelProjectRun.setText( properties.getProperty( "run.file" ) );

	}

	String getTextFromjLabelProjectRun() {
		return jLabelProjectRun.getText();
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings( "unchecked" )
        // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                jLabelDescription = new javax.swing.JLabel();
                jLabelDescriptionName = new javax.swing.JLabel();
                jLabelProjectName = new javax.swing.JLabel();
                jLabelDescriptionPath = new javax.swing.JLabel();
                jLabelProjectPath = new javax.swing.JLabel();
                jLabelDescriptionRun = new javax.swing.JLabel();
                jLabelProjectRun = new javax.swing.JLabel();
                jButtonSelectProjectRun = new javax.swing.JButton();

                jLabelDescription.setText(org.openide.util.NbBundle.getMessage(JPanelTclProjectPropertiesRunCategory.class, "JPanelTclProjectPropertiesRunCategory.jLabelDescription.text")); // NOI18N

                jLabelDescriptionName.setText(org.openide.util.NbBundle.getMessage(JPanelTclProjectPropertiesRunCategory.class, "JPanelTclProjectPropertiesRunCategory.jLabelDescriptionName.text")); // NOI18N

                jLabelProjectName.setText(org.openide.util.NbBundle.getMessage(JPanelTclProjectPropertiesRunCategory.class, "JPanelTclProjectPropertiesRunCategory.jLabelProjectName.text")); // NOI18N

                jLabelDescriptionPath.setText(org.openide.util.NbBundle.getMessage(JPanelTclProjectPropertiesRunCategory.class, "JPanelTclProjectPropertiesRunCategory.jLabelDescriptionPath.text")); // NOI18N

                jLabelProjectPath.setText(org.openide.util.NbBundle.getMessage(JPanelTclProjectPropertiesRunCategory.class, "JPanelTclProjectPropertiesRunCategory.jLabelProjectPath.text")); // NOI18N

                jLabelDescriptionRun.setText(org.openide.util.NbBundle.getMessage(JPanelTclProjectPropertiesRunCategory.class, "JPanelTclProjectPropertiesRunCategory.jLabelDescriptionRun.text")); // NOI18N

                jLabelProjectRun.setText(org.openide.util.NbBundle.getMessage(JPanelTclProjectPropertiesRunCategory.class, "JPanelTclProjectPropertiesRunCategory.jLabelProjectRun.text")); // NOI18N

                jButtonSelectProjectRun.setText(org.openide.util.NbBundle.getMessage(JPanelTclProjectPropertiesRunCategory.class, "JPanelTclProjectPropertiesRunCategory.jButtonSelectProjectRun.text")); // NOI18N
                jButtonSelectProjectRun.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jButtonSelectProjectRunActionPerformed(evt);
                        }
                });

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
                this.setLayout(layout);
                layout.setHorizontalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabelDescription)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabelDescriptionName)
                                                        .addComponent(jLabelDescriptionPath)
                                                        .addComponent(jLabelDescriptionRun))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jLabelProjectRun)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(jButtonSelectProjectRun))
                                                        .addComponent(jLabelProjectPath)
                                                        .addComponent(jLabelProjectName))))
                                .addContainerGap(178, Short.MAX_VALUE))
                );
                layout.setVerticalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabelDescription)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabelDescriptionName)
                                        .addComponent(jLabelProjectName))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabelDescriptionPath)
                                        .addComponent(jLabelProjectPath))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabelDescriptionRun)
                                        .addComponent(jLabelProjectRun)
                                        .addComponent(jButtonSelectProjectRun, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(209, Short.MAX_VALUE))
                );
        }// </editor-fold>//GEN-END:initComponents

	private void jButtonSelectProjectRunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectProjectRunActionPerformed

		//String sourcesPath=project.projectDirectory.getPath()
		//       + System.getProperty( "file.separator" )
		//      + TclProjectFactory.projectDirectoryName
		//      + System.getProperty( "file.separator" );
		String sourcesPath = project.getSrcDirectoryString() + System.getProperty("file.separator");
		
		FileSystemView fsv=new ProjectDirectoryFileSystemView( sourcesPath );
		FileFilter filter=new FileNameExtensionFilter( "Tcl File", "tcl" );
		JFileChooser fileChooser=new JFileChooser( fsv );
		fileChooser.addChoosableFileFilter( filter );
		int returnVal=fileChooser.showOpenDialog( this );


		if( returnVal == JFileChooser.APPROVE_OPTION ) {
			String absoluteFilePath=fileChooser.getSelectedFile().getAbsolutePath();
			if( absoluteFilePath.contains( sourcesPath ) ) {
				String filePath=absoluteFilePath.replace( sourcesPath, "" );
				jLabelProjectRun.setText( filePath );
			} else {
			JOptionPane.showMessageDialog( this,
			       "Wrong file! Please select file within project directory.",
			       "Information",
			       JOptionPane.INFORMATION_MESSAGE, TclProject.getImageIcon() );
			}
		}
	}//GEN-LAST:event_jButtonSelectProjectRunActionPerformed

// Restrictions for JFileChooser:
	class ProjectDirectoryFileSystemView extends FileSystemView {

		private final File[] rootLocation;

		ProjectDirectoryFileSystemView( String rootDirectoryPath ) {
			this.rootLocation=new File[]{ new File( rootDirectoryPath ) };
		}

		@Override
		public File getDefaultDirectory() {
			return rootLocation[0];
		}

		@Override
		public File getHomeDirectory() {
			return rootLocation[0];
		}

		@Override
		public File[] getRoots() {
			return rootLocation;
		}

		@Override
		public boolean isRoot( File file ) {
			for( File root:rootLocation ) {
				if( root.equals( file ) ) {
					return true;
				}
			}
			return false;
		}

		@Override
		public File createNewFolder( File containingDir ) throws IOException {
			JOptionPane.showMessageDialog( null,
			       "Select .tcl file to run. Do not create directories.",
			       "Information",
			       JOptionPane.INFORMATION_MESSAGE, TclProject.getImageIcon() );
			return new File( rootLocation[0].getAbsolutePath() + System.getProperty( "file.separator" ) + TclProjectFactory.projectDirectoryName );
		}

	}

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JButton jButtonSelectProjectRun;
        private javax.swing.JLabel jLabelDescription;
        private javax.swing.JLabel jLabelDescriptionName;
        private javax.swing.JLabel jLabelDescriptionPath;
        private javax.swing.JLabel jLabelDescriptionRun;
        private javax.swing.JLabel jLabelProjectName;
        private javax.swing.JLabel jLabelProjectPath;
        private javax.swing.JLabel jLabelProjectRun;
        // End of variables declaration//GEN-END:variables
}
