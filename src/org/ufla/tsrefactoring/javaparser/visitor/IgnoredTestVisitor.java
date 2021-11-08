package org.ufla.tsrefactoring.javaparser.visitor;

import java.util.HashMap;
import java.util.Map;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;


public class IgnoredTestVisitor extends VoidVisitorAdapter<Void> {

	private Map<String, Integer> methods = new HashMap<String, Integer>();

	@Override
	public void visit(MethodDeclaration n, Void arg) {
		if (n.getAnnotationByName("Ignore").isPresent()) {
			this.methods.put(n.getNameAsString(), n.getBegin().get().line);
		}

	}

	// GETS E SETS
	public Map<String, Integer> getMethods() {
		return methods;
	}

	public void setMethods(Map<String, Integer> methods) {
		this.methods = methods;
	}
}
