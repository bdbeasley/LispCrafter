package com.cc.lc.interpreter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;



public class IdElement extends TokenElement {
	private static Set<String> first = null;
	public static Set<String> firstSet() {
		if(first == null) { 
			first = new HashSet<String>();
			Collections.addAll(first, new String[] {
					"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z",
					"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z",
					"+","*","-","/","<",">","="
			});
		}
		return first;
	}
	
	public IdElement(Token token) {
		this.token = token;
	}
}
