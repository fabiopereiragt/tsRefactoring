package org.ufla.tsrefactoring.javaparser.visitor;

import java.util.HashMap;
import java.util.Map;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class EmptyTestVisitor extends VoidVisitorAdapter<Void> {

	private Map<String, Integer> methods = new HashMap<String, Integer>();

	@Override
	public void visit(MethodDeclaration n, Void arg) {
		super.visit(n, arg);

		// Desconsidera métodos GET e SET
		this.methods.put(n.getNameAsString(), n.getBegin().get().line);
		
	//	System.out.println(n.getNameAsString());
	//	System.out.println(n.getBegin().get().line);
	}

	// GETS E SETS
	public Map<String, Integer> getMethods() {
		return methods;
	}

	public void setMethods(Map<String, Integer> methods) {
		this.methods = methods;
	}
}
