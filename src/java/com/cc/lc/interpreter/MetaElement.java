package com.cc.lc.interpreter;

import java.util.ArrayList;
import java.util.List;


public class MetaElement extends Element {
	protected List<Element> innerElements;
	
	public MetaElement() {
		innerElements = new ArrayList<Element>();
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(prefix());
		
		boolean first = true;
		for(Element element: innerElements) {
			if(null != element) {
				if(!first) {
					builder.append(' ');
				} else first = false;
			
				builder.append(element.toString());
			}
		}
		
		builder.append(postfix());
		return builder.toString();
	}
	
	public void addInnerElement(Element element) {
		innerElements.add(element);
	}
	
	public List<Element> getInnerElements() {
		return innerElements;
	}
}
