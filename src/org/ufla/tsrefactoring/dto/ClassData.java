package org.ufla.tsrefactoring.dto;

import java.util.List;

public class ClassData {
	private String className;
	private String packageName;
	private List<String> types;

	public ClassData(String className, String packageName, List<String> types) {
		super();
		this.className = className;
		this.packageName = packageName;
		this.types = types;
	}

	public ClassData() {
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public List<String> getTypes() {
		return types;
	}

	public void setTypes(List<String> types) {
		this.types = types;
	}

}
