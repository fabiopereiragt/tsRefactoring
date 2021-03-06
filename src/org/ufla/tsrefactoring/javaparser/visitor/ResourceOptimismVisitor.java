package org.ufla.tsrefactoring.javaparser.visitor;

import java.util.ArrayList;
import java.util.List;

import org.ufla.tsrefactoring.dto.ResultTestSmellDTO;
import org.ufla.tsrefactoring.util.Util;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ResourceOptimismVisitor extends VoidVisitorAdapter<Void> {

	private MethodDeclaration currentMethod = null;
	private boolean hasSmell = false;
	private int numberLine = 0;
	private List<String> methodVariables = new ArrayList<>();
	private List<String> classVariables = new ArrayList<>();
	private List<ResultTestSmellDTO> methods = new ArrayList<ResultTestSmellDTO>();

	@Override
	public void visit(MethodDeclaration n, Void arg) {
		if (Util.isValidTestMethod(n) || Util.isValidSetupMethod(n)) {
			this.currentMethod = n;

			super.visit(n, arg);

			if (hasSmell && methodVariables.size() > 0) {
				//this.methods.put(n.getNameAsString(), n.getBegin().get().line);
				this.methods.add(new ResultTestSmellDTO(
						n.getNameAsString(), numberLine));
			}

			// reset values for next method
			currentMethod = null;
			methodVariables = new ArrayList<>();
		}

	}

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public void visit(VariableDeclarationExpr n, Void arg) {
		if (currentMethod != null) {
			for (VariableDeclarator variableDeclarator : n.getVariables()) {
				if (variableDeclarator.getType().equals("File")) {
					methodVariables.add(variableDeclarator.getNameAsString());					
				}
			}
		}
		super.visit(n, arg);
	}

	@Override
	public void visit(ObjectCreationExpr n, Void arg) {
		if (currentMethod != null) {
			if (n.getParentNode().isPresent()) {
				if (!(n.getParentNode().get() instanceof VariableDeclarator)) { // VariableDeclarator is handled in the
																				// override method
					if (n.getType().asString().equals("File")) {
						hasSmell = true;
					}
				}
			}
		} else {
			System.out.println(n.getType());
		}
		super.visit(n, arg);
	}

	@Override
	public void visit(VariableDeclarator n, Void arg) {
		if (currentMethod != null) {
			if (n.getType().asString().equals("File")) {
				methodVariables.add(n.getNameAsString());
				numberLine = n.getBegin().get().line;
			}
		} else {
			if (n.getType().asString().equals("File")) {
				classVariables.add(n.getNameAsString());
				numberLine = n.getBegin().get().line;
			}
		}
		super.visit(n, arg);
	}

	@Override
	public void visit(FieldDeclaration n, Void arg) {
		for (VariableDeclarator variableDeclarator : n.getVariables()) {
			if (variableDeclarator.getType().equals("File")) {
				classVariables.add(variableDeclarator.getNameAsString());
			}
		}
		super.visit(n, arg);
	}

	@Override
	public void visit(MethodCallExpr n, Void arg) {
		super.visit(n, arg);
		if (currentMethod != null) {
			if (n.getNameAsString().equals("exists") || n.getNameAsString().equals("isFile")
					|| n.getNameAsString().equals("notExists")) {
				if (n.getScope().isPresent()) {
					if (n.getScope().get() instanceof NameExpr) {
						if (methodVariables.contains(((NameExpr) n.getScope().get()).getNameAsString())) {
							methodVariables.remove(((NameExpr) n.getScope().get()).getNameAsString());
						}
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
