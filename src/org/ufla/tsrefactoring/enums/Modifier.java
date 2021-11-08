package org.ufla.tsrefactoring.enums;

import java.util.EnumSet;

import com.github.javaparser.ast.AccessSpecifier;

public enum Modifier {
	   PUBLIC,
	    PROTECTED,
	    PRIVATE,
	    ABSTRACT,
	    STATIC,
	    FINAL,
	    TRANSIENT,
	    VOLATILE,
	    SYNCHRONIZED,
	    NATIVE,
	    STRICTFP,
	    TRANSITIVE,
	    DEFAULT;

	    final String codeRepresentation;

	    Modifier() {
	        this.codeRepresentation = name().toLowerCase();
	    }

	    /**
	     * @return the keyword represented by this modifier.
	     */
	    public String asString() {
	        return codeRepresentation;
	    }

	    public EnumSet<Modifier> toEnumSet() {
	        return EnumSet.of(this);
	    }

	    public static AccessSpecifier getAccessSpecifier(EnumSet<Modifier> modifiers) {
	        if (modifiers.contains(Modifier.PUBLIC)) {
	            return AccessSpecifier.PUBLIC;
	        } else if (modifiers.contains(Modifier.PROTECTED)) {
	            return AccessSpecifier.PROTECTED;
	        } else if (modifiers.contains(Modifier.PRIVATE)) {
	            return AccessSpecifier.PRIVATE;
	        } else {
	            return AccessSpecifier.PACKAGE_PRIVATE;
	        }
	    }
}
