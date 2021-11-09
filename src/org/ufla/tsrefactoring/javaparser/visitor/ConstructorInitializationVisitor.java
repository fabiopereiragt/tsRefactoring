package org.ufla.tsrefactoring.javaparser.visitor;

import java.util.HashMap;
import java.util.Map;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ConstructorInitializationVisitor extends VoidVisitorAdapter<Void> {

	boolean constructorAllowed = false;
	//private String testFileName;
	private Map<String, Integer> methods = new HashMap<String, Integer>();

	@Override
	public void visit(ClassOrInterfaceDeclaration n, Void arg) {
		for (int i = 0; i < n.getExtendedTypes().size(); i++) {
			ClassOrInterfaceType node = n.getExtendedTypes().get(i);
			constructorAllowed = node.getNameAsString().equals("ActivityInstrumentationTestCase2");
		}
		super.visit(n, arg);
	}

	@Override
	public void visit(ConstructorDeclaration n, Void arg) {
		// This check is needed to handle java files that have multiple classes
		// if (n.getNameAsString().equals(testFileName)) {}
		if (!constructorAllowed) {
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
