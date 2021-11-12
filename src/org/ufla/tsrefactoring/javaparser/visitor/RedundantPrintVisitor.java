package org.ufla.tsrefactoring.javaparser.visitor;

import java.util.HashMap;
import java.util.Map;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import org.ufla.tsrefactoring.util.Util;

public class RedundantPrintVisitor extends VoidVisitorAdapter<Void> {

	private MethodDeclaration currentMethod = null;
	private int printCount = 0;
	private Map<String, Integer> methods = new HashMap<String, Integer>();

	@Override
	public void visit(MethodDeclaration n, Void arg) {
		if (Util.isValidTestMethod(n)) {
			currentMethod = n;

			super.visit(n, arg);

			if (printCount >= 1) {
				this.methods.put(n.getNameAsString(), n.getBegin().get().line);
			}

			// reset values for next method
			currentMethod = null;
			printCount = 0;
		}
	}

	// examine the methods being called within the test method
	@Override
	public void visit(MethodCallExpr n, Void arg) {
		super.visit(n, arg);
		if (currentMethod != null) {
			// if the name of a method being called is 'print' or 'println' or 'printf' or
			// 'write'
			if (n.getNameAsString().equals("print") || n.getNameAsString().equals("println")
					|| n.getNameAsString().equals("printf") || n.getNameAsString().equals("write")) {

				// check the scope of the method & proceed only if the scope is "out"
				if ((n.getScope().isPresent() && n.getScope().get() instanceof FieldAccessExpr
						&& (((FieldAccessExpr) n.getScope().get())).getNameAsString().equals("out"))) {

					FieldAccessExpr f1 = (((FieldAccessExpr) n.getScope().get()));

					// check the scope of the field & proceed only if the scope is "System"
					if ((f1.getScope() != null && f1.getScope() instanceof NameExpr
							&& ((NameExpr) f1.getScope()).getNameAsString().equals("System"))) {
						// System.out.println("Third if: " + f1.getNameAsString() + "line" +
						// f1.getBegin().get().line);
						// this.methods.put(currentMethod.getNameAsString(), f1.getBegin().get().line);
						printCount++;
					}
				}

			}
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
