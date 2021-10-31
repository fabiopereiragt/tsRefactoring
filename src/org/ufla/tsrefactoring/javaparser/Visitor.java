package org.ufla.tsrefactoring.javaparser;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class Visitor extends VoidVisitorAdapter<Void> {

	private String nameClass;
	private String packageName;
	List<String> fields = new ArrayList<String>();

	@Override
	public void visit(ClassOrInterfaceDeclaration n, Void arg) {
		super.visit(n, arg);
		// Get class or interface name
		this.nameClass = n.getNameAsString();

		// Get extends
		if (n.getExtendedTypes().size() > 0) {
			this.fields.add(n.getExtendedTypes().get(0).getNameAsString());
		}
	}

	@Override
	public void visit(MethodDeclaration n, Void arg) {
		super.visit(n, arg);

		// Desconsidera métodos GET e SET
		if (!(n.getNameAsString().contains("get") || n.getNameAsString().contains("set"))) {

			if (n.getParameters().size() > 0) {
				String[] extendsVariables = new String[n.getParameters().size()];

				for (int i = 0; i < n.getParameters().size(); i++) {
					extendsVariables[i] = n.getParameters().get(i).getTypeAsString();
				}

				for (String v : extendsVariables) {
					this.fields.add(v);
					// System.out.println("Variáveis parâmetro: " + v);
				}
			}

			new VoidVisitorAdapter<Object>() {
				@Override
				public void visit(VariableDeclarator n, Object arg) {
					super.visit(n, arg);
					insert(n.getTypeAsString());
					// System.out.println("Local variable: " + n.getTypeAsString());
				}
			}.visit(n, null);
		}

	}

	private void insert(String n) {
		this.fields.add(n);
	}

	public void visit(FieldDeclaration n, Void arg) {
		super.visit(n, arg);
		this.fields.add(n.getElementType().asString());
	}

	@Override
	public void visit(PackageDeclaration n, Void arg) {
		super.visit(n, arg);
		this.packageName = n.getNameAsString();
	}

	// GETS E SETS

	public String getNameClass() {
		return nameClass;
	}

	public void setNameClass(String nameClass) {
		this.nameClass = nameClass;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public List<String> getFields() {
		return fields;
	}

	public void setFields(List<String> fields) {
		this.fields = fields;
	}

}
