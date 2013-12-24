package com.cc.lc.interpreter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

//obviously, a class that represents an executable list
public class Function {
	private int arity; // number of arguments required; -1 means any number
	private boolean special; // true means args are evaluated prior to execution of function, false means the literal lists are passed to the function
	private boolean builtIn; // true means the function is metatyped in the java code, false means it was designed by the user
	List<Element> parameters; // the parameters the function will execute on. ex: func(arg1, arg2, arg3,...).
	String name; // the handle or name of the function. if the function is executed like func(arg1, arg2, arg3...), then func is the name.
	
	Function() {
		arity = 0;
		parameters = new ArrayList<Element>();
		special = false;
	}
	
	Function(String name) {
		this.name = name;
	}
	
	Function(int arity) {
		this.arity = arity;
	}
	
	Function(boolean special) {
		this.special = special;
		this.arity = 0;
	}
	
	Function(String name, int arity, boolean special, boolean builtIn) {
		this.arity = arity;
		this.special = special;
		this.builtIn = builtIn;
		this.name = name;
	}
	
	Function(boolean special, boolean builtIn) {
		this.special = special;
		this.builtIn = builtIn;
	}

	protected Element apply(List<Element> params) {
		return null;
	}
	
	public static boolean exists(String functionName) {
		return map.containsKey(functionName);
	}
	
	public static Function get(String functionName) {
		return map.get(functionName);
	}
	
	private static String wrapShow(String s, int whitespace) {
    	StringBuilder builder = new StringBuilder();
    	for(int i = 0; i < whitespace - s.length(); i++) {
    		builder.append(' ');
    	}
    	builder.append(s);
    	return builder.toString();
    }
	
	//method to execute the function on the list of params. If the param list's size is not equal to the arity, an error 
	//will be printed and empty list returned.
	protected Element call(List<Element> params) {
		//if the arity and param list correspond in size
		if((null == params && arity == 0) || (null != params && params.size() == arity) || arity == -1) {
			if(null == params) {
				return apply(new ArrayList<Element>());
			} else if(!special) {
				List<Element> evaluatedParams = new ArrayList<Element>();
				for(Element param : params) {
					evaluatedParams.add(Interpreter.execute(param));
				}
				return apply(evaluatedParams);
			} else return apply(params);
		} else {
			//System.out.println(params);
			System.out.println(name + " given " + (null == params ? 0 : params.size()) + " args, but needs " + arity + " args");
			return ListElement.nil;
		}
	}
	
	public boolean isBuiltIn() {
		return builtIn;
	}
	
	public boolean isSpecial() {
		return special;
	}
	
	public int getArity() {
		return arity;
	}
	
	
	private static Map<String, Function> map = new LinkedHashMap<String, Function>();
	
	//all the built-in functions are defined here
	//yeah okay its a big block of code but it's just better imo to keep it in a central place
	public static void initMap() {
		map.put("show", new Function("show", 0, true, true) {
        	public Element apply(List<Element> params) {
        		//iterate through the function map's values and print out their attributes
        		StringBuilder builder = new StringBuilder();
        		for(Entry<String, Function> e : map.entrySet()) {
        			builder.append(wrapShow(e.getKey(), 15	));
					builder.append(wrapShow(e.getValue().isSpecial() ? "special" : "non-special", 13));
					builder.append(wrapShow("" + e.getValue().getArity(), 5));
					builder.append(wrapShow(e.getValue().isBuiltIn() ? "builtin" : "non-buildin", 13)); 
					builder.append('\n');
        		}
        		System.out.print(builder.toString());
        		return ListElement.nil;
        	}
        });
		map.put("cons", new Function("cons", 2, false, true) {
			public Element apply(List<Element> params) {
				Element param1 = params.get(0);
				if(params.get(1) instanceof ListElement) {
					ListElement param2 = (ListElement) params.get(1);
					ExpressionListElement param2ExpressionListElement = param2.getInnerElements().size() > 0 ? (ExpressionListElement) param2.getInnerElements().get(0) : new ExpressionListElement();
					ExpressionListElement expressionListElement = new ExpressionListElement();
					expressionListElement.addInnerElement(param1);
					for(Element param : param2ExpressionListElement.getInnerElements()) {
						expressionListElement.addInnerElement(param);
					}
					return new ListElement(expressionListElement);
				} else {
					System.out.println("cons's 2nd argument is non-list");
					return ListElement.nil();
				}
			}
		});
		map.put("car", new Function("car", 1, false, true) {
			public Element apply(List<Element> params) {
				if(params.get(0) instanceof ListElement) {
					ListElement listElement = (ListElement) params.get(0);
					if(listElement.isNil()) { 
						return new ListElement();
					} else {
						return(((ExpressionListElement) (listElement).getInnerElements().get(0)).getInnerElements().get(0));
					}
				} else return null;
			}
		});
		map.put("cdr", new Function("cdr", 1,false,true) {
			public Element apply(List<Element> params) {
				if(params.get(0) instanceof ListElement) {
					ListElement listElement = (ListElement) params.get(0);
					if(listElement.isNil()) { 
						return new ListElement();
					} else {
						List<Element> innerElements =  ((ListElement) params.get(0)).getInnerElements();
						ExpressionListElement expressionListElement = ((ExpressionListElement) ((ListElement) params.get(0)).getInnerElements().get(0)),
								newList = new ExpressionListElement();
						List<Element> expressionElements = expressionListElement.getInnerElements().subList(1, expressionListElement.getInnerElements().size());
						for(Element element : expressionElements) {
							newList.addInnerElement(element);
						}
						return new ListElement(newList);
					}
				} else return null;
			}
		});
		map.put("quote", new Function("quote", 1,true,true) {
			public Element apply(List<Element> params) {
				return params.get(0);
			}
		});
		
		map.put("list", new Function("list", -1,false,true) {
			public Element apply(List<Element> params) {
				ListElement listElement = new ListElement();
				ExpressionListElement expressionListElement = new ExpressionListElement();
				if(params != null) {
					for(Element element : params) {
						expressionListElement.addInnerElement(element);
					}
				}
				listElement.addInnerElement(expressionListElement);
				return listElement;
			}
		});
		
		map.put("append", new Function("append", -1,false,true) {
			public Element apply(List<Element> params) {
				ExpressionListElement expressionListElement = new ExpressionListElement();
				for(Element param : params) {
					if(param instanceof ListElement) {
						ListElement listParam = (ListElement) param;
						ExpressionListElement paramExpressionListElement = listParam.getInnerElements().size() > 0 ? (ExpressionListElement) listParam.getInnerElements().get(0) : new ExpressionListElement();
						for(Element element : paramExpressionListElement.getInnerElements()) {
							expressionListElement.addInnerElement(element);
						}
					} else {
						System.out.println(this.name + " given non-list");
						return ListElement.nil();
					}
				}
				return new ListElement(expressionListElement);
			}
		});
		
		map.put("length", new Function("length", 1,false,true) {
			public Element apply(List<Element> params) {
				if(params.get(0) instanceof ListElement) {
					ListElement listElement = (ListElement) params.get(0);
					ExpressionListElement expressionListElement = listElement.getInnerElements().size() > 0 ? (ExpressionListElement) listElement.getInnerElements().get(0) : new ExpressionListElement();
					return new ConstantNumberElement(expressionListElement.getInnerElements().size());
				} else {
					System.out.println("length given a non-list or an impure list (dotted pair at end of list)");
					return ListElement.nil;
				}
			}
		});
		
		map.put("+", new ArithmeticFunction("+", 2,false,true) {
			int apply(int[] params) {
				int value = 0;
				for(int i : params) {	
					value += i;
				}
				return value;
			}
		});
		
		map.put("-", new ArithmeticFunction("-", 2,false,true) {
			int apply(int[] params) {
				return params[0] - params[1];
			}
		});
		
		map.put("*", new ArithmeticFunction("*", 2,false,true) {
			int apply(int[] params) {
				int value = params[0];
				for(int i = 1; i < params.length; i++) {
					value *= params[i];
				}
				return value;
			}
		});
		
		map.put("/", new ArithmeticFunction("/", 2,false,true) {
			int apply(int[] params) {
				if(params[1] != 0) {
					return params[0] / params[1];
				} else {
					System.out.println("divide by zero");
					return -9999999;
				}
			}
		});
		map.put("=", new Function("=", 2,false,true) {
			public Element apply(List<Element> params) {
				if(params.get(0) instanceof INumberElement && params.get(1) instanceof INumberElement) {
					return new BooleanElement(((INumberElement) params.get(0)).getValue() == ((INumberElement) params.get(1)).getValue());
				} else {
					System.out.println("builtin arithmetic rel op given non-number");
					return new BooleanElement(false);
				}
			}
		});
		map.put("/=", new ArithmeticFunction("/=", 2,false,true) {
			public Element apply(List<Element> params) {
				if(params.get(0) instanceof INumberElement && params.get(1) instanceof INumberElement) {
					return new BooleanElement(((INumberElement) params.get(0)).getValue() != ((INumberElement) params.get(1)).getValue());
				} else {
					System.out.println("builtin arithmetic rel op given non-number");
					return new BooleanElement(false);
				}
			}
		});
		map.put("<", new ComparisonFunction("<", 2,false,true) {
			boolean apply(int[] params) {
				return params[0] < params[1];
			}
		});
		map.put(">", new ComparisonFunction(">", 2,false,true) {
			boolean apply(int[] params) {
				return params[0] > params[1];
			}
		});
		map.put("<=", new ComparisonFunction("<=", 2,false,true) {
			boolean apply(int[] params) {
				return params[0] <= params[1];
			}
		});
		map.put(">=", new ComparisonFunction(">=", 2,false,true) {
			boolean apply(int[] params) {
				return params[0] >= params[1];
			}
		});
		map.put("null", new Function("null", 1,false,true) {
			public Element apply(List<Element> params) {
				return new BooleanElement(params.get(0) instanceof ListElement && ((ListElement) params.get(0)).isNil());
			}
		});
		map.put("atom", new Function("atom", 1,false,true) {
			public Element apply(List<Element> params) {
				return new BooleanElement(params.get(0) instanceof IdElement || 
						params.get(0) instanceof NumberElement ||
						params.get(0) instanceof ConstantNumberElement ||
						params.get(0) instanceof BooleanElement || 
						(params.get(0) instanceof ListElement && ((ListElement) params.get(0)).isNil()));
			}
		});
		map.put("listp", new Function("listp", 1,false,true) {
			public Element apply(List<Element> params) {
				return new BooleanElement(params.get(0) instanceof ListElement);
			}
		});
		map.put("integerp", new Function("integerp", 1,false,true) {
			public Element apply(List<Element> params) {
				return new BooleanElement(params.get(0) instanceof NumberElement || 
						params.get(0) instanceof ConstantNumberElement);
			}
		});
		map.put("cond", new Function("cond", -1,true,true) {
			public Element apply(List<Element> params) {
				if(params.size() > 0) {
					for(Element param : params) {
						if(param instanceof ListElement) {
							ListElement listElementParam = (ListElement) param;
							ExpressionListElement expressionListElement = listElementParam.getInnerElements().size() > 0 ? (ExpressionListElement) listElementParam.getInnerElements().get(0) : new ExpressionListElement();
							boolean shouldEval = false;
							if(expressionListElement.getInnerElements().size() > 0) {
								Element condition = expressionListElement.getInnerElements().get(0);
								if(condition instanceof ListElement) {
									ListElement conditionListElement = (ListElement) condition;
									Element conditionResult = Interpreter.evaluate(conditionListElement);
									if(conditionResult instanceof BooleanElement && ((BooleanElement) conditionResult).getValue()) {
										shouldEval = true;
									} else {
										//oh well, it ain't true, buddy
										//TODO: handle this stufferz
									}
								} else if(condition instanceof INumberElement){
									//TODO: make sure this is right...
									shouldEval = true;
								}
							} 
							if(shouldEval) {
								Element toEval = expressionListElement.getInnerElements().get(expressionListElement.getInnerElements().size() - 1);
								return Interpreter.execute(toEval);
							}
						} else {
							//uh oh a problem: bad input
						}
					}
					return ListElement.nil;
				} else return ListElement.nil;
			}
		});
		
		//PART 9
		//create a built-in function bound to "thread", which is a special (non evaluating) function.
		map.put("thread", new Function("thread", 3, false, true) {
			public Element apply(List<Element> params) {
				Element threadKey = params.get(0); // get the element that identifies the threaded operation by id
				if(threadKey instanceof IdElement) {
					if(params.get(2) instanceof INumberElement) {
						int maxThreads = ((INumberElement) params.get(2)).getValue(); // the max number of threads allowed to execute
						if(params.get(1) instanceof ListElement) {
							//make a threaded executor pool
							ThreadedElementExecutor executor = new ThreadedElementExecutor();
							//get the param that holds all the to-be-executed code
							ListElement listElement = (ListElement) params.get(1);
							//get the list stored in the to-be-executed code
							ExpressionListElement expressionListElement = (ExpressionListElement) listElement.getInnerElements().get(0);
							//for each element in the list of to-be-executed lists
							for(Element expressionElement: expressionListElement.getInnerElements().subList(1, expressionListElement.getInnerElements().size())) {
								if(expressionElement instanceof ListElement) {
									//execute the list (aka function) using our threaded executor pool
									executor.execute(expressionElement);
								}
							}
							
						 	try {
						 		//self-explanatory
								executor.awaitTermination(1, TimeUnit.SECONDS);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						 	
						 	//now that we have evaluated/executed each item of the list
						 	//we want to take the returned values and use them as the respective values in the
						 	//function, so build up a list of these parameters,
						 	//with the function name as the first parameter, and then
						 	//execute it, thus returning the fully evaluated expression
						 	List<Element> executed = new ArrayList<Element>();
						 	executed.add(expressionListElement.getInnerElements().get(0));
						 	executed.addAll(executor.getExecuted());
						 	Element answer = new ListElement(new ExpressionListElement(executed));
							answer = Interpreter.execute(answer);
							return answer;
						} else return params.get(1);
					} else {
						System.out.println("max number threads not a number");
					}
				} else {
					System.out.println("thread key not an id");
				}
				return params.get(1);
			}
		});
		
		// a function that adds just like the "+", but allows any number of arguments
		// in other words, in math it's a sigma symbol
		map.put("+m", new ArithmeticFunction("+m", -1, false, true) {
			int apply(int[] params) {
				int value = 0;
				for(int i : params) {	
					value += i;
				}
				return value;
			}
		});
		
		//when first called, it starts a timer
		//when called a second time, it prints the number of milliseconds since the first call
		//then resets the timer 
		map.put("time", new Function("time", 0, false, true) {
			public Element apply(List<Element> params) {
				if(time == 0) {
					time = (int) System.currentTimeMillis();
					return new ConstantNumberElement(0);
				} else {
					int temp = time;
					time = 0;
					return new ConstantNumberElement((int) (System.currentTimeMillis() - temp));
				}
			}
		});
	}

	private static int time = 0;
}
