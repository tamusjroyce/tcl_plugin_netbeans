/*
 * Created during Google Summer Of Code 2011.
 * For Tcl/Tk Community by student Michał Poczwardowski
 * License: BSD
 */
package org.netbeans.modules.languages.tcl.project;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.swing.JOptionPane;
import org.openide.util.Exceptions;

/**
 *
 * @author dmp
 */
public class TclProjectProperties {


	
	private Properties properties;
	private String propertiesFileName="tclproject.properties";
	private String propertiesFilePath;
	private HashMap<String, String> defaultPreferences;
	private final TclProject project;
	private final TclProject.TclProjectInformation info;

	public TclProjectProperties( TclProject project ) {

		this.project=project;
		info=project.getTclProjectInformation();
		// file.separator property musi be used because of / under windows
		this.propertiesFilePath=this.project.projectDirectory.getPath() + System.getProperty( "file.separator" ) + propertiesFileName;

		loadProperties();
	}

	private void loadProperties() {
		this.properties=new Properties();
		try {
			FileInputStream fis=new FileInputStream( propertiesFilePath );
			try {
				properties.load( fis );
			} catch( IOException ex ) {
				Exceptions.printStackTrace( ex );
			}
		} catch( FileNotFoundException ex ) {
			JOptionPane.showMessageDialog( null,
			       "Properties file not found. Creating default for current project.",
			       "Information",
			       JOptionPane.INFORMATION_MESSAGE, TclProject.getImageIcon() );

			generateDefaultPropertiesFile();

			FileInputStream fis;
			try {
				fis=new FileInputStream( propertiesFilePath );
				try {
					properties.load( fis );
				} catch( IOException ex1 ) {
					Exceptions.printStackTrace( ex1 );
				}
			} catch( FileNotFoundException ex1 ) {
				JOptionPane.showMessageDialog( null,
				       "Couldn't create/read default preferences file",
				       "Error",
				       JOptionPane.ERROR_MESSAGE );
			}
		}
	}
	public void storeProperties() {
		FileOutputStream fos;
		try {
			fos=new FileOutputStream( propertiesFilePath );
			try {
				this.properties.store( fos, "# Tcl Project Properties (generated for " + info.getDisplayName() + " project)\n" );
			} catch( IOException ex ) {
				Exceptions.printStackTrace( ex );
			}
			try {
				fos.close();
			} catch( IOException ex ) {
				Exceptions.printStackTrace( ex );
			}
		} catch( FileNotFoundException ex ) {
			Exceptions.printStackTrace( ex );
		}
	}

	private void setDefaultProperties() {
		this.defaultPreferences=new HashMap<String, String>();
		this.defaultPreferences.put( "run.file", "start.tcl" );
	}

	private void generateDefaultPropertiesFile() {

		setDefaultProperties();

		BufferedWriter newFile=null;
		try {
			newFile=new BufferedWriter( new FileWriter( propertiesFilePath ) );
			newFile.write( "# Tcl Project Properties (generated for " + info.getDisplayName() + " project)\n" );
			for( Map.Entry<String, String> entry:this.defaultPreferences.entrySet() ) {
				newFile.write( entry.getKey() + "=" + entry.getValue() + "\n" );
			}
			newFile.close();
		} catch( IOException ex ) {
			Exceptions.printStackTrace( ex );
		}
	}

	Properties getProperties() {
		return this.properties;
	}


}
