package org.ufla.tsrefactoring.javaparser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.ufla.tsrefactoring.dto.ResultTestSmellDTO;

public class Analyzer {
	private static List<ResultTestSmellDTO> resultTestSmellDTO = new ArrayList<ResultTestSmellDTO>();
	private static final String[] FILE_PATH = { "/home/fabio/eclipse-workspace/junittesting/src" };

	private static List<ResultTestSmellDTO> getFiles() {
		for (String path : FILE_PATH) {

			Parser.listClasses(new File(path));

			Parser.getAllClassData().forEach(allClass -> {
				// iterate over a map using for
				for (var pair : allClass.getSourceMethod().entrySet()) {
					// System.out.println(pair.getKey()); System.out.println(pair.getValue());
					resultTestSmellDTO
							.add(new ResultTestSmellDTO(
									pair.getKey(), 
									pair.getValue(), 
									allClass.getFilePath()));
				}
			});
			Parser.getAllClassData().clear();
		}
		return resultTestSmellDTO;
	}

	public static List<ResultTestSmellDTO> getFilesAnalyzed() {
		return getFiles();
	}

}
