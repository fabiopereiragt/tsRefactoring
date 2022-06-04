package org.ufla.tsrefactoring.javaparser.visitor;

import java.util.ArrayList;
import java.util.List;

import org.ufla.tsrefactoring.dto.ResultTestSmellDTO;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ConstructorInitializationVisitor extends VoidVisitorAdapter<Void> {

	boolean constructorAllowed = false;
	//private String testFileName;
	private List<ResultTestSmellDTO> methods = new ArrayList<ResultTestSmellDTO>();

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
			this.methods.add(new ResultTestSmellDTO(
					n.getNameAsString(), n.getBegin().get().line));

		}
	}

	// GET
	public List<ResultTestSmellDTO> getMethods() {
		return methods;
	}
}
