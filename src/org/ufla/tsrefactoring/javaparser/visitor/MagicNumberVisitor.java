package org.ufla.tsrefactoring.javaparser.visitor;

import java.util.ArrayList;
import java.util.List;

import org.ufla.tsrefactoring.dto.ResultTestSmellDTO;
import org.ufla.tsrefactoring.util.Util;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class MagicNumberVisitor extends VoidVisitorAdapter<Void> {

	private MethodDeclaration currentMethod = null;
	private List<ResultTestSmellDTO> methods = new ArrayList<ResultTestSmellDTO>();

	@Override
	public void visit(MethodDeclaration n, Void arg) {
		if (Util.isValidTestMethod(n) || Util.isValidSetupMethod(n)) {
			this.currentMethod = n;

			super.visit(n, arg);

		/*	if (magicCount >= 1) {
				this.methods.put(n.getNameAsString(), this.numberLine);				
			}
*/
			//reset values for next method
            currentMethod = null;
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
                    	 this.methods.add(
                         		new ResultTestSmellDTO(
                         		currentMethod.getNameAsString(), 
                         		argument.getBegin().get().line));	
                    }
                    // if the argument contains an ObjectCreationExpr (e.g. assertEquals(new Integer(2),...)
                    else if (argument instanceof ObjectCreationExpr) {
                        for (Expression objectArguments : ((ObjectCreationExpr) argument).getArguments()) {
                            if (Util.isNumber(objectArguments.toString())) {
                            	 this.methods.add(
                                 		new ResultTestSmellDTO(
                                 		currentMethod.getNameAsString(), 
                                 		argument.getBegin().get().line));	
                            }
                        }
                    }
                    // if the argument contains an MethodCallExpr (e.g. assertEquals(someMethod(2),...)
                    else if (argument instanceof MethodCallExpr) {
                        for (Expression objectArguments : ((MethodCallExpr) argument).getArguments()) {
                            if (Util.isNumber(objectArguments.toString())) {                                                               
                                this.methods.add(
                                		new ResultTestSmellDTO(
                                		currentMethod.getNameAsString(), 
                                		argument.getBegin().get().line));                       
                            }
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
