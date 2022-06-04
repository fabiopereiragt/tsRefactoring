package org.ufla.tsrefactoring.javaparser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.ufla.tsrefactoring.dto.ResultTestSmellDTO;
import org.ufla.tsrefactoring.enums.TestSmell;

public class Analyzer {
	private static List<ResultTestSmellDTO> resultEmptyTest = new ArrayList<ResultTestSmellDTO>();

	private static final String[] FILE_PATH = { "D:\\eclipse-workspace\\Gadgetbride\\app\\src\\test" };

	private static List<ResultTestSmellDTO> getFiles(TestSmell testSmell) {
		for (String path : FILE_PATH) {

			resultEmptyTest.clear();

			Parser.listClasses(new File(path), testSmell);

			resultEmptyTest = Parser.getAllClassData();
		}
		return resultEmptyTest;
	}

	public static List<ResultTestSmellDTO> getFilesAnalyzed(TestSmell testSmell) {
		return getFiles(testSmell);
	}

}
