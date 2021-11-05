package org.ufla.tsrefactoring.javaparser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.ufla.tsrefactoring.dto.ClassData;

public class Analyzer {
	private static List<ClassData> classData = new ArrayList<ClassData>();
	private static final String[] FILE_PATH = { "D:\\dev\\java\\java-junit-sample-master" };

	private static List<ClassData> getFiles(){
		for(String path : FILE_PATH) {
			
			Parser.listClasses(new File(path));
			
			Parser.getAllClassData().forEach(allClass -> {
				classData.add(new ClassData(
						allClass.getClassName(),
						allClass.getPackageName(),
						allClass.getTypes()));
			});				
			Parser.getAllClassData().clear();
		}			
		return classData;
	}
	
	public static List<ClassData> getFilesAnalyzed() {
		return getFiles();
	}
	
	
}

