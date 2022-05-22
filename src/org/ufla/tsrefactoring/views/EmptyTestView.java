package org.ufla.tsrefactoring.views;

import javax.inject.Inject;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.ufla.tsrefactoring.dto.ResultTestSmellDTO;
import org.ufla.tsrefactoring.enums.TestSmell;
import org.ufla.tsrefactoring.provider.EmptyTestProvider;



public class EmptyTestView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.ufla.tsrefactoring.views.EmptyTestView";

	@Inject
	IWorkbench workbench;

	private TableViewer viewer;
	private Action refactoring;
	private Action doubleClickAction;

	@Override
	public void createPartControl(Composite parent) {

		// define the TableViewer
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);

		viewer.setContentProvider(new EmptyTestProvider());
		
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
				EmptyTestView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(refactoring);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void makeActions() {

		refactoring = new Action() {
			public void run() {
				IStructuredSelection selection = viewer.getStructuredSelection();
				// Object obj = selection.getFirstElement();
				ResultTestSmellDTO rs = (ResultTestSmellDTO) selection.getFirstElement();
				
				UtilView.executeRefactor(selection, rs, viewer, TestSmell.EMPTY_TEST);
				
			/*	if (showQuestionMessage(rs.getMethodName())) {
					try {
						if (EmptyTestRefactoring.executeRefactory(rs)) {
							//Open the file again
							UtilView.openFile(rs.getFilePath(), rs.getLineNumber());
							// Remove the item on the table list
							viewer.remove(rs);
							showMessage("Refactoring",
									"Successfully refactored. Open the file again to view the refactoring.");
						}

					} catch (FileNotFoundException | CoreException e) {
						e.printStackTrace();
					}
				}*/
			}
		};
		refactoring.setText("Refactor");
		refactoring.setToolTipText("Refactor the test smell");
		refactoring.setImageDescriptor(
			PlatformUI.getWorkbench().getSharedImages()
			.getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK)
		);

		//Open the file in the test smell location
		doubleClickAction = new Action() {
			public void run() {
				/* 
				 * //Open the file in the eclipse editor
				 * 
				 * File file = new File(filePath); URI location = file.toURI(); IFile[] files =
				 * ResourcesPlugin.getWorkspace().getRoot().findFilesForLocationURI(location);
				 * IFile sourceFile = files[0]; IJavaElement sourceJavaElement =
				 * JavaCore.create(sourceFile); try { JavaUI.openInEditor(sourceJavaElement, );
				 * 
				 * } catch (PartInitException e1) { // TODO Auto-generated catch block
				 * e1.printStackTrace(); } catch (JavaModelException e1) { // TODO
				 * Auto-generated catch block e1.printStackTrace(); }
				 * 
				 * }
				 */
				IStructuredSelection selection = viewer.getStructuredSelection();
				ResultTestSmellDTO rs = (ResultTestSmellDTO) selection.getFirstElement();
				try {
					UtilView.openFile(rs.getFilePath(), rs.getLineNumber());
				} catch (CoreException e1) {
					e1.printStackTrace();
				}			
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
	
	/*
	private boolean showQuestionMessage(String message) {
		return MessageDialog.openQuestion(viewer.getControl().getShell(), "Question",
				"ATTENTION: this operation cannot be undone."
						+ "\nDo you really want to apply refactoring in this method: " + message + "?");
	}

	private void showMessage(String title, String message) {
		MessageDialog.openInformation(viewer.getControl().getShell(), title, message);
	}*/

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	// When closing a tab
	@Override
	public void dispose() {
	}

}
