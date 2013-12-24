package com.cc.lc.interpreter;

import java.util.List;


public class ComparisonFunction extends Function{
	ComparisonFunction(String name, int arity, boolean special, boolean builtIn) {
		super(name, arity, special, builtIn);
	}
	
	protected Element apply(List<Element> params) {
		boolean numberOnly = true;
		if(null == params) {
			//System.out.println("params is null");
			return super.apply(params);
		} else {
			for(Element param : params) {
				if(!(param instanceof INumberElement)) {
					//System.out.println(param.getClass() + ", " + param);
					numberOnly = false;
					break;
				}
			}
			if(numberOnly) {
				//System.out.println("numberOnly");
				int[] numbers = new int[params.size()];
				int i = 0;
				for(Element param : params) {
					numbers[i++] = ((INumberElement) param).getValue();
				}
				return new BooleanElement(apply(numbers));
			} else {
				//System.out.println("not numberOnly");
				return ListElement.nil;
			}
		}
	}
	
	boolean apply(int[] params) {
		return false;
	}
}
