package org.ufla.tsrefactoring.views;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;

import javax.inject.Inject;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.ViewPart;
import org.ufla.tsrefactoring.dto.ResultTestSmellDTO;
import org.ufla.tsrefactoring.provider.ResourceOptimismProvider;
import org.ufla.tsrefactoring.refactoring.ConstructorInitializationRefactoring;

/**
 * This sample class demonstrates how to plug-in a new workbench view. The view
 * shows data obtained from the model. The sample creates a dummy model on the
 * fly, but a real implementation would connect to the model available either in
 * this or another plug-in (e.g. the workspace). The view is connected to the
 * model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be
 * presented in the view. Each view can present the same model objects using
 * different labels and icons, if needed. Alternatively, a single label provider
 * can be shared between views in order to ensure that objects of the same type
 * are presented in the same way everywhere.
 * <p>
 */

public class ResourceOptimismView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.ufla.tsrefactoring.views.ResourceOptimismView";

	@Inject
	IWorkbench workbench;

	private TableViewer viewer;
	private Action doubleClickAction;

	@Override
	public void createPartControl(Composite parent) {

		// define the TableViewer
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);

		// Provider the data
		viewer.setContentProvider(new ResourceOptimismProvider());

		/*
		 * ColumnLabelProvider columnsLabels = new ColumnLabelProvider();
		 * columnsLabels.createColumns(viewer);
		 */

		TableViewerColumn colTestSmell = new TableViewerColumn(viewer, SWT.NONE);
		colTestSmell.getColumn().setWidth(200);
		colTestSmell.getColumn().setText("Source Method");
		colTestSmell.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				ResultTestSmellDTO rs = (ResultTestSmellDTO) element;
				return rs.getMethodName();
			}
		});

		TableViewerColumn colSourceMethod = new TableViewerColumn(viewer, SWT.NONE);
		colSourceMethod.getColumn().setWidth(50);
		colSourceMethod.getColumn().setText("Line");
		colSourceMethod.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				ResultTestSmellDTO rs = (ResultTestSmellDTO) element;
				return Integer.toString(rs.getLineNumber());
			}
		});

		TableViewerColumn colLine = new TableViewerColumn(viewer, SWT.NONE);
		colLine.getColumn().setWidth(300);
		colLine.getColumn().setText("File Path");
		colLine.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				ResultTestSmellDTO rs = (ResultTestSmellDTO) element;
				return rs.getFilePath();
			}
		});

		viewer.getTable().setLinesVisible(true);
		viewer.getTable().setHeaderVisible(true);
		viewer.setInput(getViewSite());

		getSite().setSelectionProvider(viewer);
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				ResourceOptimismView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void fillContextMenu(IMenuManager manager) {
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void makeActions() {

		doubleClickAction = new Action() {
			public void run() {
				IStructuredSelection selection = viewer.getStructuredSelection();
				// Object obj = selection.getFirstElement();
				ResultTestSmellDTO rs = (ResultTestSmellDTO) selection.getFirstElement();
				try {
					openFile(rs.getFilePath(), rs.getLineNumber());
				} catch (CoreException e1) {
					e1.printStackTrace();
				}

				if (showQuestionMessage(rs.getMethodName())) {
					try {
						if (ConstructorInitializationRefactoring.executeRefactory(rs)) {
							showMessage("Refactoring",
									"Successfully refactored. Open the file again to view the refactoring.");
							// Remove the item on the table list
							viewer.remove(rs);
						}

					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}

				}
			}

			private void openFile(String filePath, int lineNumber) throws CoreException {
				File file = new File(filePath);
				URI location = file.toURI();
				IFile[] files = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocationURI(location);
				IFile sourceFile = files[0];
				System.out.println(filePath);
				//IPath path = new Path(filePath);
				//IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
				IMarker marker = sourceFile.createMarker(IMarker.TEXT);
				marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				IDE.openEditor(page, marker);
				marker.delete();
				/*
				 * File file = new File(filePath); URI location = file.toURI(); IFile[] files =
				 * ResourcesPlugin.getWorkspace().getRoot().findFilesForLocationURI(location);
				 * IFile sourceFile = files[0]; IJavaElement sourceJavaElement =
				 * JavaCore.create(sourceFile); try { JavaUI.openInEditor(sourceJavaElement, );
				 * 
				 * } catch (PartInitException e1) { // TODO Auto-generated catch block
				 * e1.printStackTrace(); } catch (JavaModelException e1) { // TODO
				 * Auto-generated catch block e1.printStackTrace(); }
				 */

			}
		};
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}

	private boolean showQuestionMessage(String message) {
		return MessageDialog.openQuestion(viewer.getControl().getShell(), "Question",
				"Do you really want to apply refactoring in this method: " + message + "?");
	}

	private void showMessage(String title, String message) {
		MessageDialog.openInformation(viewer.getControl().getShell(), title, message);
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	// When closing a tab
	@Override
	public void dispose() {
	}

}
