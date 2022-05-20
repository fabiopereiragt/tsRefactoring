package org.ufla.tsrefactoring.javaparser;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ufla.tsrefactoring.dto.ClassDataDTO;
import org.ufla.tsrefactoring.enums.TestSmell;
import org.ufla.tsrefactoring.javaparser.visitor.ConstructorInitializationVisitor;
import org.ufla.tsrefactoring.javaparser.visitor.EmptyTestVisitor;
import org.ufla.tsrefactoring.javaparser.visitor.IgnoredTestVisitor;
import org.ufla.tsrefactoring.javaparser.visitor.RedundantPrintVisitor;
import org.ufla.tsrefactoring.javaparser.visitor.ResourceOptimismVisitor;
import org.ufla.tsrefactoring.util.DirExplorer;

public class Parser {

	private static List<ClassDataDTO> allClassData = new ArrayList<ClassDataDTO>();

	public static void listClasses(File projectDir, TestSmell testSmell) {

		new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
			try {
				CompilationUnit cu = StaticJavaParser.parse(file);

				if (testSmell == TestSmell.EMPTY_TEST) {
					EmptyTestVisitor v = new EmptyTestVisitor();
					v.visit(cu, null);
					ClassDataDTO classData = new ClassDataDTO(v.getMethods(), 
							projectDir.toString().concat(path));
					insert(classData);
				} else if (testSmell == TestSmell.IGNORED_TEST) {
					IgnoredTestVisitor v = new IgnoredTestVisitor();
					v.visit(cu, null);
					ClassDataDTO classData = new ClassDataDTO(v.getMethods(),
							projectDir.toString().concat(path));
					insert(classData);
				} else if (testSmell == TestSmell.REDUNDANT_PRINT) {
					RedundantPrintVisitor v = new RedundantPrintVisitor();
					v.visit(cu, null);
					ClassDataDTO classData = new ClassDataDTO(v.getMethods(),
							projectDir.toString().concat(path));
					insert(classData);
				} else if (testSmell == TestSmell.RESOURCE_OPTIMISM) {
					ResourceOptimismVisitor v = new ResourceOptimismVisitor();
					v.visit(cu, null);
					ClassDataDTO classData = new ClassDataDTO(v.getMethods(),
							projectDir.toString().concat(path));
					insert(classData);
				} else {
					ConstructorInitializationVisitor v = new ConstructorInitializationVisitor();
					v.visit(cu, null);
					ClassDataDTO classData = new ClassDataDTO(v.getMethods(),
							projectDir.toString().concat(path));
					insert(classData);
				}
			} catch (IOException e) {
				new RuntimeException(e);
			}
		}).explore(projectDir);
	}

	private static void insert(ClassDataDTO cl) {
		allClassData.add(cl);
	}

	public static List<ClassDataDTO> getAllClassData() {
		return allClassData;
	}

}
