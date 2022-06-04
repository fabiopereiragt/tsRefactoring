package org.ufla.tsrefactoring.dto;

public class ResultTestSmellDTO {
	private String methodName;
	private int lineNumber;
	private String filePath;

	public ResultTestSmellDTO(String methodName, int lineNumber) {
		super();
		this.methodName = methodName;
		this.lineNumber = lineNumber;
	}

	public ResultTestSmellDTO(String methodName, int lineNumber, String filePath) {
		super();
		this.methodName = methodName;
		this.lineNumber = lineNumber;
		this.filePath = filePath;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
