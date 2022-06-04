package org.ufla.tsrefactoring.javaparser.visitor;

import java.util.ArrayList;
import java.util.List;

import org.ufla.tsrefactoring.dto.ResultTestSmellDTO;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;


public class IgnoredTestVisitor extends VoidVisitorAdapter<Void> {

	private List<ResultTestSmellDTO> methods = new ArrayList<ResultTestSmellDTO>();

	@Override
	public void visit(MethodDeclaration n, Void arg) {
		if (n.getAnnotationByName("Ignore").isPresent() || n.getAnnotationByName("Disabled").isPresent()) {
			this.methods.add(new ResultTestSmellDTO(
					n.getNameAsString(), n.getBegin().get().line));
		}

	}

	// GET
		public List<ResultTestSmellDTO> getMethods() {
			return methods;
		}
}
