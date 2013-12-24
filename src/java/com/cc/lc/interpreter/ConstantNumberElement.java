package com.cc.lc.interpreter;

public class ConstantNumberElement extends Element implements INumberElement {
	private int value;
	
	public ConstantNumberElement(int value) {
		this.value = value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public String toString() {
		return Integer.toString(value);
	}
}
