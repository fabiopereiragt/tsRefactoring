package org.ufla.tsrefactoring.javaparser.visitor;

import java.util.HashMap;
import java.util.Map;

import org.ufla.tsrefactoring.util.Util;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class MagicNumberVisitor extends VoidVisitorAdapter<Void> {

	private MethodDeclaration currentMethod = null;
	private int magicCount = 0;
	private int numberLine = 0;
	private Map<String, Integer> methods = new HashMap<String, Integer>();

	@Override
	public void visit(MethodDeclaration n, Void arg) {
		if (Util.isValidTestMethod(n) || Util.isValidSetupMethod(n)) {
			this.currentMethod = n;

			super.visit(n, arg);

			if (magicCount >= 1) {
				this.methods.put(n.getNameAsString(), this.numberLine);				
			}

			//reset values for next method
            currentMethod = null;
            magicCount = 0;
		}

	}

	 // examine the methods being called within the test method
    @Override
    public void visit(MethodCallExpr n, Void arg) {
        super.visit(n, arg);
        if (currentMethod != null) {
            // if the name of a method being called start with 'assert'
            if (n.getNameAsString().startsWith(("assertArrayEquals")) ||
                    n.getNameAsString().startsWith(("assertEquals")) ||
                    n.getNameAsString().startsWith(("assertNotSame")) ||
                    n.getNameAsString().startsWith(("assertSame")) ||
                    n.getNameAsString().startsWith(("assertThat")) ||
                    n.getNameAsString().equals("assertNotNull") ||
                    n.getNameAsString().equals("assertNull")) {
                // checks all arguments of the assert method
                for (Expression argument : n.getArguments()) {
                    // if the argument is a number
                    if (Util.isNumber(argument.toString())) {
                        magicCount++;
                        numberLine = n.getBegin().get().line;
                    }
                    // if the argument contains an ObjectCreationExpr (e.g. assertEquals(new Integer(2),...)
                    else if (argument instanceof ObjectCreationExpr) {
                        for (Expression objectArguments : ((ObjectCreationExpr) argument).getArguments()) {
                            if (Util.isNumber(objectArguments.toString())) {
                                magicCount++;
                                numberLine = n.getBegin().get().line;
                            }
                        }
                    }
                    // if the argument contains an MethodCallExpr (e.g. assertEquals(someMethod(2),...)
                    else if (argument instanceof MethodCallExpr) {
                        for (Expression objectArguments : ((MethodCallExpr) argument).getArguments()) {
                            if (Util.isNumber(objectArguments.toString())) {
                                magicCount++;
                                numberLine = n.getBegin().get().line;
                               
                            }
                        }
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
