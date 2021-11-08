package org.ufla.tsrefactoring.provider;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.ufla.tsrefactoring.dto.ResultTestSmellDTO;
import org.ufla.tsrefactoring.enums.TestSmell;
import org.ufla.tsrefactoring.javaparser.Analyzer;
import org.eclipse.jface.viewers.Viewer;

public class ConstructorInitializationProvider implements IStructuredContentProvider {

	private static List<ResultTestSmellDTO> resultTestSmellDTO = new ArrayList<ResultTestSmellDTO>();
	
	public ConstructorInitializationProvider() {
		resultTestSmellDTO.clear();
		resultTestSmellDTO = Analyzer.getFilesAnalyzed(TestSmell.CONSTRUCTOR_INITIALIZATION);
	}

	@Override
	public Object[] getElements(Object arg0) {
		return resultTestSmellDTO.toArray();
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

}
