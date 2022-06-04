package org.ufla.tsrefactoring.javaparser.visitor;

import java.util.ArrayList;
import java.util.List;

import org.ufla.tsrefactoring.dto.ResultTestSmellDTO;
import org.ufla.tsrefactoring.util.Util;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class EmptyTestVisitor extends VoidVisitorAdapter<Void> {

	private List<ResultTestSmellDTO> methods = new ArrayList<ResultTestSmellDTO>();

	@Override
	public void visit(MethodDeclaration n, Void arg) {
	    if (Util.isValidTestMethod(n)) { 			
			if (!n.isAbstract()) {
				if (n.getBody().isPresent()) {
					// get the total number of statements contained in the method
					if (n.getBody().get().getStatements().size() == 0) {
						//add(n.getNameAsString(), n.getBegin().get().line);
						this.methods.add(new ResultTestSmellDTO(
								n.getNameAsString(), n.getBegin().get().line));
					}
				}
			}
		}
	}

	// GET
	public List<ResultTestSmellDTO> getMethods() {
		return methods;
	}
}
