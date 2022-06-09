package org.ufla.tsrefactoring.refactoring;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.ufla.tsrefactoring.dto.ResultTestSmellDTO;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;

public class MagicNumberRefactoring {

	public static boolean executeRefactory(ResultTestSmellDTO magicNumberTestSmell) throws FileNotFoundException {

		File file = new File(magicNumberTestSmell.getFilePath());
		CompilationUnit cu = StaticJavaParser.parse(file);

		cu.accept(new ModifierVisitor<Void>() {

			@Override
			public Visitable visit(MethodCallExpr n, Void arg) {
				if (n.getBegin().get().line == magicNumberTestSmell.getLineNumber()) {
					if (n.getNameAsString().startsWith(("assertArrayEquals"))
							|| n.getNameAsString().startsWith(("assertEquals"))
							|| n.getNameAsString().startsWith(("assertNotSame"))
							|| n.getNameAsString().startsWith(("assertSame"))
							|| n.getNameAsString().startsWith(("assertThat"))
							|| n.getNameAsString().equals("assertNotNull")
							|| n.getNameAsString().equals("assertNull")) {

						// Get the variable type
						String typeVariable = n.getArgument(0).getClass().getSimpleName().toString();

						Statement staticStatement = null;

						String variableName = "var" + n.getBegin().get().line;

						// check variable type and create the statement
						if (typeVariable.startsWith("Integer")) {
							staticStatement = StaticJavaParser
									.parseStatement("int " + variableName + " = " + n.getArgument(0) + ";");
						} else {
							staticStatement = StaticJavaParser
									.parseStatement("double " + variableName + " = " + n.getArgument(0) + ";");
						}

						Statement staticStatementb = staticStatement;

						// add the new local variable
						n.findAncestor(BlockStmt.class).ifPresent(x -> {
							x.addStatement(0, staticStatementb);
						});

						// add comment
						n.setBlockComment("Rename " + variableName + " according to your context");

						// Change the argument for the variable
						Expression newVariable = StaticJavaParser.parseExpression(variableName);
						n.setArgument(0, newVariable);
					}
				}
				return super.visit(n, arg);
			}

		}, null);
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
	}
}
