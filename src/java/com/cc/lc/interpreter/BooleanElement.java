package com.cc.lc.interpreter;

public class BooleanElement extends Element {
	boolean value;
	
	BooleanElement(boolean value) {
		this.value = value;
	}
	
	public String toString() {
		return value ? "-999" : ListElement.nil.toString();
	}
	
	public boolean getValue() {
		return value;
	}
}
