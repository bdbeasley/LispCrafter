package com.cc.lc.interpreter;

public class StringElement extends Element {
	String value;
	public StringElement(String value) {
		this.value = value;
	}
	
	public String toString() {
		return value;
	}
}
