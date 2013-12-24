package com.cc.lc.interpreter;

import java.util.HashSet;
import java.util.Set;


public class AtomElement extends TokenElement {
	public AtomElement(Token token) { setToken(token); }
	private static Set<String> first = null;
	public static Set<String> firstSet() {
		if(null == first) {
			first = new HashSet<String>();
			first.addAll(NumberElement.firstSet());
			first.addAll(IdElement.firstSet());
		}
		return first;
	}
}
