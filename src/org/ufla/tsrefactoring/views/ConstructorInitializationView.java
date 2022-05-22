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
import org.ufla.tsrefactoring.provider.ConstructorInitializationProvider;

public class ConstructorInitializationView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.ufla.tsrefactoring.views.ConstructorInitializationView";

	@Inject
	IWorkbench workbench;

	private TableViewer viewer;
	private Action refactoring;
	private Action doubleClickAction;

	@Override
	public void createPartControl(Composite parent) {

		// define the TableViewer
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);

		// Provider the data
		viewer.setContentProvider(new ConstructorInitializationProvider());

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
				ConstructorInitializationView.this.fillContextMenu(manager);
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
				ResultTestSmellDTO rs = (ResultTestSmellDTO) selection.getFirstElement();
				UtilView.executeRefactor(selection, rs, viewer, TestSmell.CONSTRUCTOR_INITIALIZATION);
			}
		};
		refactoring.setText("Refactor");
		refactoring.setToolTipText("Refactor the test smell");
		refactoring.setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));

		// Open the file in the test smell location
		doubleClickAction = new Action() {
			public void run() {
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

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	// When closing a tab
	@Override
	public void dispose() {
	}

}
