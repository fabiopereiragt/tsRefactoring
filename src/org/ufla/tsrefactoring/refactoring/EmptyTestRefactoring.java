package org.ufla.tsrefactoring.refactoring;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.ufla.tsrefactoring.dto.ResultTestSmellDTO;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;

public class EmptyTestRefactoring {

	public static boolean executeRefactory(ResultTestSmellDTO emptyTestSmell) throws FileNotFoundException {
		File file = new File(emptyTestSmell.getFilePath());
		CompilationUnit cu = StaticJavaParser.parse(file);

		cu.accept(new ModifierVisitor<Void>() {

			@Override
			public Visitable visit(MethodDeclaration n, Void arg) {
				if (n.getBody().get().getStatements().size() == 0) {
					if (n.getBegin().get().line == emptyTestSmell.getLineNumber()) {
						return null;
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
