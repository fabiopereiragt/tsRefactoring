package org.ufla.tsrefactoring.dto;

import java.util.Map;

public class ClassDataDTO {
	private Map<String, Integer> sourceMethod;
	private String filePath;

	public ClassDataDTO(Map<String, Integer> sourceMethod, String filePath) {
		super();
		this.sourceMethod = sourceMethod;
		this.filePath = filePath;
	}

	public ClassDataDTO() {

	}

	public Map<String, Integer> getSourceMethod() {
		return sourceMethod;
	}

	public void setSourceMethod(Map<String, Integer> sourceMethod) {
		this.sourceMethod = sourceMethod;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
