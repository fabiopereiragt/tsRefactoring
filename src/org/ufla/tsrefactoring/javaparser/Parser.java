package org.ufla.tsrefactoring.javaparser;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ufla.tsrefactoring.dto.ResultTestSmellDTO;
import org.ufla.tsrefactoring.enums.TestSmell;
import org.ufla.tsrefactoring.javaparser.visitor.ConstructorInitializationVisitor;
import org.ufla.tsrefactoring.javaparser.visitor.EmptyTestVisitor;
import org.ufla.tsrefactoring.javaparser.visitor.IgnoredTestVisitor;
import org.ufla.tsrefactoring.javaparser.visitor.MagicNumberVisitor;
import org.ufla.tsrefactoring.javaparser.visitor.RedundantPrintVisitor;
import org.ufla.tsrefactoring.javaparser.visitor.ResourceOptimismVisitor;
import org.ufla.tsrefactoring.util.DirExplorer;

public class Parser {

	private static List<ResultTestSmellDTO> allClassData = new ArrayList<ResultTestSmellDTO>();

	public static void listClasses(File projectDir, TestSmell testSmell) {

		new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
			try {
				CompilationUnit cu = StaticJavaParser.parse(file);

				if (testSmell == TestSmell.EMPTY_TEST) {
					EmptyTestVisitor v = new EmptyTestVisitor();
					v.visit(cu, null);
					insert(v.getMethods(), projectDir.toString().concat(path));
				} else if (testSmell == TestSmell.IGNORED_TEST) {
					IgnoredTestVisitor v = new IgnoredTestVisitor();
					v.visit(cu, null);
					insert(v.getMethods(), projectDir.toString().concat(path));
				} else if (testSmell == TestSmell.REDUNDANT_PRINT) {
					RedundantPrintVisitor v = new RedundantPrintVisitor();
					v.visit(cu, null);
					insert(v.getMethods(), projectDir.toString().concat(path));
				} else if (testSmell == TestSmell.RESOURCE_OPTIMISM) {
					ResourceOptimismVisitor v = new ResourceOptimismVisitor();
					v.visit(cu, null);
					insert(v.getMethods(), projectDir.toString().concat(path));
				} else if (testSmell == TestSmell.MAGIC_NUMBER) {
					MagicNumberVisitor v = new MagicNumberVisitor();
					v.visit(cu, null);
					insert(v.getMethods(), projectDir.toString().concat(path));
				} else {
					ConstructorInitializationVisitor v = new ConstructorInitializationVisitor();
					v.visit(cu, null);
					insert(v.getMethods(), projectDir.toString().concat(path));
				}
			} catch (IOException e) {
				new RuntimeException(e);
			}
		}).explore(projectDir);
	}

	private static void insert(List<ResultTestSmellDTO> listResult, String path) {
		listResult.forEach(item -> {			
			allClassData.add(
					new ResultTestSmellDTO(
							item.getMethodName(), 
							item.getLineNumber(),
							path));
		});
	}

	public static List<ResultTestSmellDTO> getAllClassData() {
		return allClassData;
	}

}
