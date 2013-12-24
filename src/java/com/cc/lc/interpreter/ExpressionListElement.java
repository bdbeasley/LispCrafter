package com.cc.lc.interpreter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ExpressionListElement extends MetaElement {
	private static Set<String> first = null;
	
	public static Set<String> firstSet() {
		if(null == first) {
			first = new HashSet<String>();
			first.addAll(ExpressionElement.firstSet());
		}
		return first;
	}
	
	public ExpressionListElement(List<Element> innerElements) {
		this.innerElements = innerElements;
	}
	
	public ExpressionListElement() {
		super();
	}
}
