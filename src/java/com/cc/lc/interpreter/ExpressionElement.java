package com.cc.lc.interpreter;

import java.util.HashSet;
import java.util.Set;



public class ExpressionElement extends MetaElement {
	private static Set<String> first = null;
	public static Set<String> firstSet() {
		if(null == first) {
			first = new HashSet<String>();
			first.addAll(AtomElement.firstSet());
			first.addAll(ListElement.firstSet());
			first.add("'");
		}
		return first;
	}
}
