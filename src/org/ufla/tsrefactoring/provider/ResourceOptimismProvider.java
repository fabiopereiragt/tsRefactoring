package org.ufla.tsrefactoring.provider;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.ufla.tsrefactoring.dto.ResultTestSmellDTO;
import org.ufla.tsrefactoring.enums.TestSmell;
import org.ufla.tsrefactoring.javaparser.Analyzer;

public class ResourceOptimismProvider implements IStructuredContentProvider {

	private static List<ResultTestSmellDTO> resultTestSmellDTO = new ArrayList<ResultTestSmellDTO>();

	public ResourceOptimismProvider() {
		resultTestSmellDTO = Analyzer.getFilesAnalyzed(TestSmell.RESOURCE_OPTIMISM);
	}

	@Override
	public Object[] getElements(Object arg0) {
		return resultTestSmellDTO.toArray();
	}

	@Override
	public void dispose() {
		resultTestSmellDTO.clear();
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

}
