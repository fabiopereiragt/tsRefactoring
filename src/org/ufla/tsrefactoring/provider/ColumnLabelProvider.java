package org.ufla.tsrefactoring.provider;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.ufla.tsrefactoring.dto.ResultTestSmellDTO;

public class ColumnLabelProvider {
	public void createColumns(TableViewer viewer) {
		String[] titles = { "Source Method", "Line", "File path" };
		int[] bounds = { 100, 100, 100 };

		// Column 0: First Name
		int i = 0;
		TableViewerColumn colTestSmell = new TableViewerColumn(viewer, SWT.NONE);
		colTestSmell.getColumn().setWidth(bounds[i]);
		colTestSmell.getColumn().setText(titles[i]);
		colTestSmell.setLabelProvider(new org.eclipse.jface.viewers.ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				ResultTestSmellDTO rs = (ResultTestSmellDTO) element;
				return rs.getMethodName();
			}
		});

		i++;
		TableViewerColumn colSourceMethod = new TableViewerColumn(viewer, SWT.NONE);
		colSourceMethod.getColumn().setWidth(50);
		colSourceMethod.getColumn().setText("Line");
		colSourceMethod.setLabelProvider(new org.eclipse.jface.viewers.ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				ResultTestSmellDTO rs = (ResultTestSmellDTO) element;
				return Integer.toString(rs.getLineNumber());
			}
		});

		i++;
		TableViewerColumn colLine = new TableViewerColumn(viewer, SWT.NONE);
		colLine.getColumn().setWidth(300);
		colLine.getColumn().setText("File Path");
		colLine.setLabelProvider(new org.eclipse.jface.viewers.ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				ResultTestSmellDTO rs = (ResultTestSmellDTO) element;
				return rs.getFilePath();
			}
		});
	}
}
