package com.cc.lc.interpreter;

import java.util.List;


public class ArithmeticFunction extends Function {
	ArithmeticFunction(String name, int arity, boolean special, boolean builtIn) {
		super(name, arity, special, builtIn);
	}
	
	protected Element apply(List<Element> params) {
		//System.out.println("applytoparams:" + params);
		boolean numberOnly = true;
		if(null == params) {
			//System.out.println("params is null");
			return super.apply(params);
		} else {
			for(Element param : params) {
				if(!(param instanceof INumberElement)) {
					//System.out.println("param: " + param);
					numberOnly = false;
					break;
				} else {
					//System.out.println("param2: " + param);
				}
			}
			if(numberOnly) {
				//System.out.println("numberOnly");
				int[] numbers = new int[params.size()];
				int i = 0;
				for(Element param : params) {
					numbers[i++] = ((INumberElement) param).getValue();
				}
				return new ConstantNumberElement(apply(numbers));
			} else {
				System.out.println("builtin arithmetic rel op given non-number");
				return ListElement.nil;
			}
		}
	}
	
	int apply(int[] params) {
		return 0;
	}
}
