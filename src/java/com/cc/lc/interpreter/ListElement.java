package com.cc.lc.interpreter;

import java.util.HashSet;
import java.util.Set;



public class ListElement extends MetaElement {
	
	public ListElement() {
		
	}
	
	public ListElement(ExpressionListElement element) {
		addInnerElement(element);
	}
	
	private static Set<String> first = null;
	public static Set<String> firstSet() {
		if(first == null) {
			first = new HashSet<String>();
			first.add("(");
		}
		return first;
	}
	
	protected String prefix() { return "("; }
	protected String postfix() { return ")";}
	
	protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
	
	public boolean isNil() {
		return 0 == innerElements.size();
	}
	
	static ListElement nil() {
		return new ListElement();
	}
	
	public static ListElement nil = nil();
	
	public boolean equals(ListElement element) {
		return true;
		
	}
}
