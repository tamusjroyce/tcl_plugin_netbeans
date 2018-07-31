/*
 * Created during Google Summer Of Code 2011.
 * For Tcl/Tk Community by student Michał Poczwardowski
 * License: BSD
 */
package org.netbeans.modules.languages.tcl.project;

import java.awt.Dialog;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.modules.languages.tcl.debugger.TclDebuggerActionsProvider;
import org.netbeans.modules.languages.tcl.run.RunTclsh;
import org.netbeans.spi.project.ActionProvider;
import org.netbeans.spi.project.CopyOperationImplementation;
import org.netbeans.spi.project.DeleteOperationImplementation;
import org.netbeans.spi.project.ProjectState;
import org.netbeans.spi.project.ui.CustomizerProvider;
import org.netbeans.spi.project.ui.PrivilegedTemplates;
import org.netbeans.spi.project.ui.ProjectOpenedHook;
import org.netbeans.spi.project.ui.support.DefaultProjectOperations;
import org.netbeans.spi.project.ui.support.ProjectCustomizer;
import org.netbeans.spi.project.ui.support.ProjectCustomizer.Category;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 * Tcl Project Definition. 
 * @author dmp
 */
public class TclProject implements Project {


	/* 
	 * Default Project Structure:
	 * tclproject/ → identifiy by TclProjectFactory class
	 * tclproject/start.tcl 
	 * tclproject.propeties → handle by TclProjectProperties class
	 */
	final public FileObject projectDirectory;
	final public ProjectState projectState;
	private Lookup lookup;
	// properties // It's used by ProjectInformation and LogicalView
	static public String iconLocation="org/netbeans/modules/languages/tcl/filetype/tcl.filetype.notext.png";
	private TclProjectProperties projectProperties;

	public TclProject( FileObject fo, ProjectState ps ) {
		projectDirectory=fo;
		projectState=ps;
	}

	FileObject getTclFolder( boolean toCreate ) {
		FileObject result=projectDirectory.getFileObject( TclProjectFactory.projectDirectoryName );
		if( result == null && toCreate ) {
			try {
				result=projectDirectory.createFolder( TclProjectFactory.projectDirectoryName );
			} catch( IOException ioe ) {
				Exceptions.printStackTrace( ioe );
			}
		}
		return result;
	}

	static public ImageIcon getImageIcon() {
		return new ImageIcon( ImageUtilities.loadImage( iconLocation ) );
	}

	static public Image getImage() {
		return ImageUtilities.loadImage( iconLocation );
	}

	@Override
	public FileObject getProjectDirectory() {
		return projectDirectory;
	}

	public TclProjectInformation getTclProjectInformation() {
		return new TclProjectInformation();
	}

	/*
	 * Project Capabilities provided in Lookup
	 */
	@Override
	public Lookup getLookup() {
		if( lookup == null ) {
			lookup=Lookups.fixed( new Object[]{
				       projectState, // outside code could change this, for example that it needs to be saved
				       new TclProjectActionProviderImplementation(), // standard actions for example *RUN*, Build and Clean
				       new TclProjectDeleteOperation(),
				       new TclProjectCopyOperation( this ),
				       new TclProjectInformation(),
				       new TclProjectLogicalView( this ),
				       new PrivilegedTemplatesImplementation(),
				       new TclProjectCustomizerProvider(),
				       new TclProjectOpenedHook( this )
			       } );
		}
		return lookup;
	}

	public Properties getTclProjectProperties() {
		return this.projectProperties.getProperties();
	}

	public TclProjectProperties getTclProjectPropertiesObject() {
		return this.projectProperties;
	}

	public File getFileToRun() {

		String filePath=projectDirectory.getPath()
		       + System.getProperty( "file.separator" )
		       + TclProjectFactory.projectDirectoryName
		       + System.getProperty( "file.separator" )
		       + projectProperties.getProperties().getProperty( "run.file" );

		return new File( filePath );

	}

	public static String getDirectoryFromPath( String location ) {

		int to=location.lastIndexOf( System.getProperty( "file.separator" ) );
		return location.substring( 0, to );
	}

	public String getSrcDirectoryString() {
		return projectDirectory.getPath() + System.getProperty( "file.separator" ) + TclProjectFactory.projectDirectoryName;

	}

	private static final class PrivilegedTemplatesImplementation implements PrivilegedTemplates {

		private static final String[] privilegedNames=new String[]{
			"Templates/Tcl/TclTemplate.tcl",
			"Templates/Other/Folder"
		};

		@Override
		public String[] getPrivilegedTemplates() {
			return privilegedNames;
		}

	}

	private class TclProjectActionProviderImplementation implements ActionProvider {

		private String[] supportedAction=new String[]{
			ActionProvider.COMMAND_DELETE,
			ActionProvider.COMMAND_COPY,
			ActionProvider.COMMAND_RUN,
			ActionProvider.COMMAND_DEBUG
		};

		@Override
		public String[] getSupportedActions() {
			return supportedAction;
		}

		@Override
		public void invokeAction( String string, Lookup lookup ) throws IllegalArgumentException {
			if( string.equalsIgnoreCase( ActionProvider.COMMAND_DELETE ) ) {
				DefaultProjectOperations.performDefaultDeleteOperation( TclProject.this );
			}
			if( string.equalsIgnoreCase( ActionProvider.COMMAND_COPY ) ) {
				DefaultProjectOperations.performDefaultCopyOperation( TclProject.this );
			}
			if( string.equalsIgnoreCase( ActionProvider.COMMAND_DEBUG ) ) {
				TclDebuggerActionsProvider.startDebugger( TclProject.this );
			}
			if( string.equalsIgnoreCase( ActionProvider.COMMAND_RUN ) ) {
				/// RUN 
				File f=getFileToRun();
				new RunTclsh( f.getName(), f.getAbsolutePath() );
			}
		}

		@Override
		public boolean isActionEnabled( String string, Lookup lookup ) throws IllegalArgumentException {

			if( ( string.equals( ActionProvider.COMMAND_DELETE ) ) ) {
				return true;
			} else if( ( string.equals( ActionProvider.COMMAND_COPY ) ) ) {
				return true;
			} else if( ( string.equals( ActionProvider.COMMAND_DEBUG ) ) ) {
				return true;
			} else if( ( string.equals( ActionProvider.COMMAND_RUN ) ) ) {
				return true;
			} else {
				throw new IllegalArgumentException( string );
			}
		}

	}

	private class TclProjectCustomizerProvider implements CustomizerProvider {

		private ProjectCustomizer.Category[] categories;
		private ProjectCustomizer.CategoryComponentProvider panelProvider;
		private JPanelTclProjectPropertiesRunCategory panelRunCategory;

		private void initCustomizer() {
			ProjectCustomizer.Category categoryGeneral=ProjectCustomizer.Category.create(
			       "General",
			       "General",
			       null, ( Category[] )null );

			categories=new ProjectCustomizer.Category[]{
				categoryGeneral, };

			HashMap<ProjectCustomizer.Category, JPanelTclProjectPropertiesRunCategory> panelsMap=
			       new HashMap<ProjectCustomizer.Category, JPanelTclProjectPropertiesRunCategory>();
			this.panelRunCategory=new JPanelTclProjectPropertiesRunCategory( TclProject.this );
			panelsMap.put( categoryGeneral, panelRunCategory );
			panelProvider=new PanelProvider( panelsMap );
		}

		@Override
		public void showCustomizer() {

			initCustomizer();

			OptionListener listener=new OptionListener( TclProject.this, panelRunCategory );
			Dialog dialog=ProjectCustomizer.createCustomizerDialog( categories, panelProvider, null, listener, null );
			dialog.addWindowListener( listener );

			dialog.setTitle( new TclProjectInformation().getDisplayName() );

			dialog.setVisible( true );
		}

	}

	private class OptionListener extends WindowAdapter implements ActionListener {

		private TclProject project;
		private final JPanelTclProjectPropertiesRunCategory panelRunCategory;

		private OptionListener( TclProject p, JPanelTclProjectPropertiesRunCategory panelRunCategory ) {
			this.panelRunCategory=panelRunCategory;
			this.project=p;
		}

		@Override
		public void actionPerformed( ActionEvent e ) {
			// OK Action → Store properties
			Properties properties=project.getTclProjectProperties();
			TclProjectProperties tclProjectProperties=project.getTclProjectPropertiesObject();
			properties.setProperty( "run.file", panelRunCategory.getTextFromjLabelProjectRun() );
			tclProjectProperties.storeProperties();
		}

		@Override
		public void windowClosed( WindowEvent e ) {
		}

		@Override
		public void windowClosing( WindowEvent e ) {
			// Close and dispose the dialog
		}

	}

	private static class PanelProvider implements ProjectCustomizer.CategoryComponentProvider {

		private Map panelsMap;

		public PanelProvider( Map panelsMap ) {
			this.panelsMap=panelsMap;
		}

		@Override
		public JComponent create( ProjectCustomizer.Category category ) {
			JComponent panel=( JComponent )panelsMap.get( category );
			if( panel == null ) {
				return new JPanel();

			} else {
				return panel;
			}
		}

	}

	public class TclProjectInformation implements ProjectInformation {

		@Override
		public String getName() {
			return getProjectDirectory().getName();
		}

		@Override
		public String getDisplayName() {
			return getName();
		}

		@Override
		public Icon getIcon() {
			return TclProject.getImageIcon();
		}

		@Override
		public Project getProject() {
			return TclProject.this;
		}

		@Override
		public void addPropertyChangeListener( PropertyChangeListener pl ) {
			return;
		}

		@Override
		public void removePropertyChangeListener( PropertyChangeListener pl ) {
			return;
		}

	}

// Actions for Project Opened/Closed by Netbeans GUI.
	private class TclProjectOpenedHook extends ProjectOpenedHook {

		private final TclProject project;

		public TclProjectOpenedHook( TclProject project ) {
			this.project=project;
		}

		@Override
		protected void projectOpened() {
			this.project.projectProperties=new TclProjectProperties( project );
		}

		@Override
		protected void projectClosed() {
			return;
		}

	}

/// Implementation of standard operations below >
	private class TclProjectDeleteOperation implements DeleteOperationImplementation {

		@Override
		public void notifyDeleting() throws IOException {
			return;
		}

		@Override
		public void notifyDeleted() throws IOException {
			return;
		}

		@Override
		public List<FileObject> getMetadataFiles() {
			List<FileObject> arrayListFiles=new ArrayList<FileObject>();
			return arrayListFiles;
		}

		@Override
		public List<FileObject> getDataFiles() {
			List<FileObject> arrayListFiles=new ArrayList<FileObject>();
			return arrayListFiles;
		}

	}

	private class TclProjectCopyOperation implements CopyOperationImplementation {

		private final TclProject project;
		private final FileObject projectDirectory;

		public TclProjectCopyOperation( TclProject project ) {
			this.project=project;
			this.projectDirectory=project.getProjectDirectory();
		}

		@Override
		public void notifyCopying() throws IOException {
			return;
		}

		@Override
		public void notifyCopied( Project prjct, File file, String string ) throws IOException {
			return;
		}

		@Override
		public List<FileObject> getMetadataFiles() {
			// returns empty collection
			return new ArrayList<FileObject>();
		}

		@Override
		public List<FileObject> getDataFiles() {
			// returns empty collection
			return new ArrayList<FileObject>();
		}

	}

}