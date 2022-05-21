package org.ufla.tsrefactoring.views;

import java.io.File;
import java.net.URI;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

public class UtilView {
	public static void openFile(String filePath, int lineNumber) throws CoreException {
		File file = new File(filePath);
		URI location = file.toURI();
		IFile[] files = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocationURI(location);
		IFile sourceFile = files[0];
		System.out.println(filePath);
		IMarker marker = sourceFile.createMarker(IMarker.TEXT);
		marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IDE.openEditor(page, marker);
		marker.delete();
	}
}
