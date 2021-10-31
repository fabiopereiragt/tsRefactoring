package org.ufla.tsrefactoring.dto;



public class MethodComposition {
	private String methodName;
	private String methodBody;
	public MethodComposition(String methodName, String methodBody) {
		super();
		this.methodName = methodName;
		this.methodBody = methodBody;
	}
	
	public MethodComposition() {}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getMethodBody() {
		return methodBody;
	}

	public void setMethodBody(String methodBody) {
		this.methodBody = methodBody;
	}
}
