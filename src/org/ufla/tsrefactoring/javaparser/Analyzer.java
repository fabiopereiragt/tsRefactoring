package org.ufla.tsrefactoring.javaparser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.ufla.tsrefactoring.dto.ResultTestSmellDTO;
import org.ufla.tsrefactoring.enums.TestSmell;

public class Analyzer {
	private static List<ResultTestSmellDTO> resultEmptyTest = new ArrayList<ResultTestSmellDTO>();
	
	//private static final String[] FILE_PATH = { "/home/fabio/eclipse-workspace/junittesting/src/com/javatpoint/testcase" };	
	private static final String[] FILE_PATH = { "D:\\eclipse-workspace\\Gadgetbride\\app\\src\\test" };
	
	private static List<ResultTestSmellDTO> getFiles(TestSmell testSmell) {
		for (String path : FILE_PATH) {
			
			resultEmptyTest.clear();

			Parser.listClasses(new File(path), testSmell);

			Parser.getAllClassData().forEach(allClass -> {
				// iterate over a map using for
				for (var pair : allClass.getSourceMethod().entrySet()) {
					// System.out.println(pair.getKey()); System.out.println(pair.getValue());
					resultEmptyTest
							.add(new ResultTestSmellDTO(
									pair.getKey(), 
									pair.getValue(), 
									allClass.getFilePath()));
				}
			});
			Parser.getAllClassData().clear();
		}
		return resultEmptyTest;
	}

	public static List<ResultTestSmellDTO> getFilesAnalyzed(TestSmell testSmell) {
		return getFiles(testSmell);
	}

}
