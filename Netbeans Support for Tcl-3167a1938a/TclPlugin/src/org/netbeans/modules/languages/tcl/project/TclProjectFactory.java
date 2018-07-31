/*
 * Created during Google Summer Of Code 2011.
 * For Tcl/Tk Community by student Michał Poczwardowski
 * License: BSD
 */
package org.netbeans.modules.languages.tcl.project;

import java.io.IOException;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectFactory;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;

/**
 * Tcl Project Factory.
 * @author dmp
 */
@org.openide.util.lookup.ServiceProvider( service=ProjectFactory.class )
public class TclProjectFactory implements ProjectFactory {

	public static final String projectDirectoryName="tclproject";

	@Override
	public boolean isProject( FileObject fo ) {
		return fo.getFileObject( projectDirectoryName )!=null;
	}

	@Override
	public Project loadProject( FileObject fo, ProjectState ps ) throws IOException {
		if( isProject( fo ) ) {
			return new TclProject( fo, ps );
		} else {
			return null;
		}
	}

	@Override
	public void saveProject( Project prjct ) throws IOException, ClassCastException {
		FileObject projectRoot=prjct.getProjectDirectory();
		if( projectRoot.getFileObject( projectDirectoryName )==null ) {
			throw new IOException( "ProjectDirectory  ("+projectRoot.getPath()+") doesn't exist");
		}
		( ( TclProject )prjct ).getTclFolder( true );
	}

}
