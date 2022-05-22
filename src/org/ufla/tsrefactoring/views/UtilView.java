package org.ufla.tsrefactoring.views;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.ufla.tsrefactoring.dto.ResultTestSmellDTO;
import org.ufla.tsrefactoring.enums.TestSmell;
import org.ufla.tsrefactoring.refactoring.ConstructorInitializationRefactoring;
import org.ufla.tsrefactoring.refactoring.EmptyTestRefactoring;
import org.ufla.tsrefactoring.refactoring.IgnoredTestRefactoring;
import org.ufla.tsrefactoring.refactoring.RedundantPrintRefactoring;
import org.ufla.tsrefactoring.refactoring.ResourceOptimismRefactoring;

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

	public static void executeRefactor(IStructuredSelection selection, ResultTestSmellDTO rs, TableViewer viewer,
			TestSmell testSmell) {
		if (showQuestionMessage(rs.getMethodName(), viewer)) {
			try {
				switch (testSmell) {
				case EMPTY_TEST:
					if (EmptyTestRefactoring.executeRefactory(rs)) {
						// Open the file again
						UtilView.openFile(rs.getFilePath(), rs.getLineNumber());
						// Remove the item on the table list
						viewer.remove(rs);
						showMessage("Refactoring",
								"Successfully refactored. Open the file again to view the refactoring.", viewer);
					}
					break;
				case IGNORED_TEST:
					if (IgnoredTestRefactoring.executeRefactory(rs)) {
						UtilView.openFile(rs.getFilePath(), rs.getLineNumber());
						viewer.remove(rs);
						showMessage("Refactoring",
								"Successfully refactored. Open the file again to view the refactoring.", viewer);
					}
					break;
				case CONSTRUCTOR_INITIALIZATION:
					if (ConstructorInitializationRefactoring.executeRefactory(rs)) {
						UtilView.openFile(rs.getFilePath(), rs.getLineNumber());
						viewer.remove(rs);
						showMessage("Refactoring",
								"Successfully refactored. Open the file again to view the refactoring.", viewer);
					}
					break;
				case REDUNDANT_PRINT:
					if (RedundantPrintRefactoring.executeRefactory(rs)) {
						UtilView.openFile(rs.getFilePath(), rs.getLineNumber());
						viewer.remove(rs);
						showMessage("Refactoring",
								"Successfully refactored. Open the file again to view the refactoring.", viewer);
					}
					break;
				case RESOURCE_OPTIMISM:
					if (ResourceOptimismRefactoring.executeRefactory(rs)) {
						UtilView.openFile(rs.getFilePath(), rs.getLineNumber());
						viewer.remove(rs);
						showMessage("Refactoring",
								"Successfully refactored. Open the file again to view the refactoring.", viewer);
					}
					break;
				}
			} catch (FileNotFoundException | CoreException e) {
				e.printStackTrace();
			}
		}

	}

	private static boolean showQuestionMessage(String message, TableViewer viewer) {
		return MessageDialog.openQuestion(viewer.getControl().getShell(), "Question",
				"Do you really want to apply refactoring in this method: " + message + "?");
	}

	private static void showMessage(String title, String message, TableViewer viewer) {
		MessageDialog.openInformation(viewer.getControl().getShell(), title, message);
	}

}
