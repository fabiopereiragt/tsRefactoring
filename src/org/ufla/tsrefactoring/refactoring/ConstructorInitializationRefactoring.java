package org.ufla.tsrefactoring.refactoring;

import java.io.File;
import java.io.FileNotFoundException;

import org.ufla.tsrefactoring.dto.ResultTestSmellDTO;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;

public class ConstructorInitializationRefactoring {
	static boolean constructorAllowed = false;
	
	public static boolean executeRefactory(ResultTestSmellDTO constructorInitializationSmell) throws FileNotFoundException {
		File file = new File(constructorInitializationSmell.getFilePath());
		CompilationUnit cu = StaticJavaParser.parse(file);
		
		
		//Remove the method
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
				if (!constructorAllowed && n.getBegin().get().line == constructorInitializationSmell.getLineNumber()) {
					System.out.println("Method name: " + n.getNameAsString() + n.getBody());
					//(n.getNameAsString(), n.getBegin().get().line);

				}
				return super.visit(n, arg);
			}
		}, null);
		
		
		return false;
	}

}
