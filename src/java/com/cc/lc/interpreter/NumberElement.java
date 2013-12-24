package com.cc.lc.interpreter;

import java.util.HashSet;
import java.util.Set;

public class NumberElement extends TokenElement implements INumberElement{
	private static Set<String> first = null;
	public static Set<String> firstSet() {
		if(first == null) { 
			first = new HashSet<String>();
			for(int i = 0; i < 10; i++) {
				first.add(Integer.toString(i));
			}
		}
		return first;
	}
	
	public NumberElement(Token token) {
		this.token = token;
	}

	@Override
	public void setValue(int value) {
		this.token.string = ""+value;
	}

	@Override
	public int getValue() {
		return Integer.parseInt(this.token.string);
	}
}
