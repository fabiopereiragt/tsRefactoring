package org.ufla.tsrefactoring.refactoring;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.ufla.tsrefactoring.dto.ResultTestSmellDTO;
import org.ufla.tsrefactoring.util.Util;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class RedundantPrintRefactoring {

	public static boolean executeRefactory(ResultTestSmellDTO emptyTestSmell) throws FileNotFoundException {

		File file = new File(emptyTestSmell.getFilePath());
		CompilationUnit cu = StaticJavaParser.parse(file);
		
		cu.walk(MethodDeclaration.class, e -> {

			if (Util.isValidTestMethod(e)) {

				new VoidVisitorAdapter<Object>() {
					@Override
					public void visit(MethodCallExpr n, Object arg) {
						
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
									System.out.println(n);
								
								}
							}
						}
					}
				}.visit(e, null);

			}
		});

		try {
			// The second parameter says to append the file.
			// False, the file will be cleared before writing
			FileWriter fw = new FileWriter(file, false);
			fw.write(cu.toString());
			fw.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;

		// System.out.println(cu);
	}

	/*
	 * CompilationUnit cu = StaticJavaParser.parse(new
	 * File(emptyTestSmell.getFilePath())); cu.walk(MethodDeclaration.class, e -> {
	 * if (e.getBody().get().getStatements().size() == 0) { if
	 * (e.getBegin().get().line == emptyTestSmell.getLineNumber()) { e.remove();
	 * System.out.println("Retirei"); } } });
	 */

}
