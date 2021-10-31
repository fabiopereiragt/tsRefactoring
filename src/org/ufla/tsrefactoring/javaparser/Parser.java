package org.ufla.tsrefactoring.javaparser;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ufla.tsrefactoring.dto.ClassData;
import org.ufla.tsrefactoring.util.DirExplorer;

public class Parser {
	
	private static List<ClassData> allClassData = new ArrayList<ClassData>();
	
	public static void listClasses(File projectDir) {
		
	new DirExplorer(
			(level, path, file) -> path.endsWith(".java"), 
			(level, path, file) -> {
				try {
					CompilationUnit cu = StaticJavaParser.parse(file);
					Visitor v = new Visitor();
					v.visit(cu, null);
					ClassData classData = new ClassData(
							v.getNameClass(), 
							v.getPackageName(),
							v.getFields());
					insert(classData);
				} catch (IOException e) {
					new RuntimeException(e);
				}
			}).explore(projectDir);
		}
	
	private static void insert(ClassData cl) {
		allClassData.add(cl);
	}
	
	public static List<ClassData> getAllClassData() {
		return allClassData;
	}
	
}
