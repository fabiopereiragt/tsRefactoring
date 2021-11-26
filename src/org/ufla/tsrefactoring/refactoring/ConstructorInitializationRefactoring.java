package org.ufla.tsrefactoring.refactoring;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.ufla.tsrefactoring.dto.ResultTestSmellDTO;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;

public class ConstructorInitializationRefactoring {
	static boolean constructorAllowed = false;

	public static boolean executeRefactory(ResultTestSmellDTO constructorInitializationSmell)
			throws FileNotFoundException {
		File file = new File(constructorInitializationSmell.getFilePath());
		CompilationUnit cu = StaticJavaParser.parse(file);

		cu.accept(new ModifierVisitor<Void>() {
			@Override
			public Visitable visit(ClassOrInterfaceDeclaration n, Void arg) {
				for (int i = 0; i < n.getExtendedTypes().size(); i++) {
					ClassOrInterfaceType node = n.getExtendedTypes().get(i);
					constructorAllowed = node.getNameAsString().equals("ActivityInstrumentationTestCase2");
				}
				return super.visit(n, arg);
			}
		}, null);

		cu.accept(new ModifierVisitor<Void>() {

			@Override
			public Visitable visit(ConstructorDeclaration n, Void arg) {
				if (n.getBegin().get().line == constructorInitializationSmell.getLineNumber()) {
					n.setBlockComment("Method created by tsRefactoring");
					n.setName("void initialize");
					n.addMarkerAnnotation("BeforeAll");

					// Add import
					if (cu.getImports().stream()
							.noneMatch(x -> x.toString().equals("org.junit.jupiter.api.BeforeAll"))) {
						cu.addImport(new ImportDeclaration("org.junit.jupiter.api.BeforeAll", false, false));
					}
				}
				return super.visit(n, arg);
			}
		}, null);

		/*
		 * Create a new method
		 *  cu.accept(new ModifierVisitor<Void>() {
		 * 
		 * @Override public Visitable visit(ClassOrInterfaceDeclaration n, Void arg) {
		 * MethodDeclaration method = n.addMethod("initialize", Keyword.PUBLIC);
		 * method.setBody(constructorBody); method.addMarkerAnnotation("BeforeAll");
		 * method.setBlockComment("Method created by tsRefactoring");
		 * n.addMember(method); return super.visit(n, arg); } }, null);
		 */
		
		
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
