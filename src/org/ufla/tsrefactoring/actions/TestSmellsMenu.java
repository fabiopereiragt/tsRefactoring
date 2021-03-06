package org.ufla.tsrefactoring.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import org.eclipse.ui.PartInitException;

/**
 * Our sample action implements workbench action delegate. The action proxy will
 * be created by the workbench and shown in the UI. When the user tries to use
 * the action, this delegate will be created and execution will be delegated to
 * it.
 * 
 * @see IWorkbenchWindowActionDelegate
 */
public class TestSmellsMenu implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;

	/**
	 * The constructor.
	 */
	public TestSmellsMenu() {
	}

	/**
	 * The action has been activated. The argument of the method represents the
	 * 'real' action sitting in the workbench UI.
	 * 
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {
		IWorkbenchPage page = window.getActivePage();
		try {
			if (action.getId().equals("ConstructorInitialization")) {
				page.showView("org.ufla.tsrefactoring.views.ConstructorInitializationView");
			} else if (action.getId().equals("IgnoredTest")) {
				page.showView("org.ufla.tsrefactoring.views.IgnoredTestView");
			} else if (action.getId().equals("EmptyTest")) {
				page.showView("org.ufla.tsrefactoring.views.EmptyTestView");
			} else if (action.getId().equals("ResourceOptimism")) {
				page.showView("org.ufla.tsrefactoring.views.ResourceOptimismView");
			}else if (action.getId().equals("MagicNumber")) {
				page.showView("org.ufla.tsrefactoring.views.MagicNumberView");
			}else {
				page.showView("org.ufla.tsrefactoring.views.RedundantPrintView");
			}
			
		} catch (PartInitException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Selection in the workbench has been changed. We can change the state of the
	 * 'real' action here if we want, but this can only happen after the delegate
	 * has been created.
	 * 
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

	/**
	 * We can use this method to dispose of any system resources we previously
	 * allocated.
	 * 
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {
	}

	/**
	 * We will cache window object in order to be able to provide parent shell for
	 * the message dialog.
	 * 
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}
}