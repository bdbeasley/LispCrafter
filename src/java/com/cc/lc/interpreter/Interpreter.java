package com.cc.lc.interpreter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* *** This file is given as part of the programming assignment. *** */

public class Interpreter {
    private Scan scanner = null; // a scanner...

    // tok is global to all these parsing methods;
    // scan just calls the scanner's scan method and saves the result in tok.
    private Token tok; // the current token
    private void scan() {
        tok = scanner.scan();
        Function.initMap();
    }

    // note on the first sets:
    // we cheat -- there is only a single token in the set,
    // so we just compare tkrep with the first token.

    // level at which parsing;
    // used to handle the problem of scan-ahead, given interactive system.
    private int level;

    // for error handling
    // to make this a bit friendlier in interactive environment;
    // handle parse errors by jumping back to main loop.

    class ParsingExpressionException extends Exception {
        public ParsingExpressionException(String msg) {
			//super(msg); // call constructor in superclass (i.e., base class);
            // it outputs message and a bit more.
        }
    }

    public Interpreter(String args[]) {
        scanner = new Scan(args);
        //start();
    }

    private void start() {
        while( true ) {
            System.out.print( "> ");
            System.out.flush();
            // always reset since might previous might have failed
            level = 0;
            scan();
            if ( is(TK.EOF) ) break;
            try {
                // read and parse expression.
                //rootElement.addInnerElement(expr());
                Element element = expr();
                print(element);
                System.out.println(execute(element));
                //rootElement.
                // in later parts:
                //   print out expression
                //   evaluate expression
                //   print out value of evaluated expression
                // note that an error in evaluating (at any level) will
                // return nil and evaluation will continue.
            }
            catch (ParsingExpressionException e) {
                System.out.println( "trying to recover from parse error");
                gobble();
            }
        }
    }

    public String evaluate() throws ParsingExpressionException {

        while( true ) {
                System.out.print( "> ");
                System.out.flush();
            // always reset since might previous might have failed
            level = 0;
            scan();
            if ( is(TK.EOF) ) break;
            try {
                // read and parse expression.
                //rootElement.addInnerElement(expr());
                Element element = expr();
                print(element);
                return execute(element).toString();
                //rootElement.
                // in later parts:
                //   print out expression
                //   evaluate expression
                //   print out value of evaluated expression
                // note that an error in evaluating (at any level) will
                // return nil and evaluation will continue.
            }
            catch (ParsingExpressionException e) {
                System.out.println( "trying to recover from parse error");
                gobble();
            }
        }
        return null;
    }

    public Interpreter(String input) {
        scanner = new Scan(input);
        //start();
    }

    void print(Element element) {
        System.out.println(element);
    }
    
    
    //general function to take a literal such as (+ 6 7) and turn it into its evaluated element such as 13
    public static Element execute(Element element) {
    	//decipher what type of element this is, then 
        //call evaluate on it if it's supported
    	Element evaluated = null;
    	if(element instanceof IdElement) {
    		evaluated = evaluate((IdElement) element);
    	} else if(element instanceof INumberElement) {
    		evaluated = evaluate((INumberElement) element);
    	} else if(element instanceof ListElement) {
    		evaluated = evaluate((ListElement) element);
    	} 
		return evaluated;
    }
    
    static Map<String, Integer> idMap = new HashMap<String, Integer>(); // stores a map of all id's bound in the program

    public static Element evaluate(IdElement idElement) {
    	if(idMap.containsKey(idElement.getToken().string)) {
    		return new ConstantNumberElement(idMap.get(idElement.getToken().string));
    	} else {
    		System.out.println("\"" + idElement.getToken().string + "\" is not bound as a parameter");
    		return new ListElement();
    	}
    }
    
    //this function takes a com.cc.lc.interpreter.NumberElement, evaluates it.
    //funnyly, it just degenerates to a com.cc.lc.interpreter.ConstantNumberElement. Why?
    //because ConstantNumberElements typically don't get evaluated. 
    //Using ContantNumberElement signals to our interpreter that we've 
    //looked at this number and decided it's just a number so we won't
    //be looking to evaluate it again... it's already degenerated.
    public static Element evaluate(INumberElement numberElement) {
    	return new ConstantNumberElement(numberElement.getValue());
    }
    
    //this is a loaded function, but basically it's task is to take a listElement,
    //identify the function and its arguments, then evaluate this list using the function, and 
    //return the result. Yup, that's it.
    public static Element evaluate(ListElement listElement) {
    	//first of all, the com.cc.lc.interpreter.ListElement MUST have a ListExpressionElement, otherwise it has no arguments or function
    	if(listElement.getInnerElements().size() > 0) {
    		//get the com.cc.lc.interpreter.ExpressionListElement from the com.cc.lc.interpreter.ListElement
    		ExpressionListElement expressionListElement = (ExpressionListElement) listElement.getInnerElements().get(0);
    		//hey, if we got it (aka it's not null)
    		if(null != expressionListElement) {
    			//and if it's not empty
	    		if(expressionListElement.getInnerElements().size() > 0) {
	    			//get the first element
		        	Element firstElement = expressionListElement.getInnerElements().get(0);
		        	//if the first element of the com.cc.lc.interpreter.ExpressionListElement is an com.cc.lc.interpreter.IdElement
		        	if(firstElement instanceof IdElement) {
		        		//then it's a function name. Good, so get the value of the com.cc.lc.interpreter.IdElement, and call it the function name
		    			String functionName = ((IdElement) firstElement).getToken().string;
		    			if(Function.exists(functionName)) {
		    				//alright, now all we have to do is call the function identified by that function name,
		    				//and pass it the rest of the arguments
		    				if(expressionListElement.getInnerElements().size() > 1) {
		    					return Function.get(functionName).call(expressionListElement.getInnerElements().subList(1,expressionListElement.getInnerElements().size()));
		    				} else {
		    					return Function.get(functionName).call(null);
		    				}
		    			} else {
		    				System.out.println("\"" + functionName + "\" is not bound as a function");
		    				return ListElement.nil();
		    			}
		    		} 
		        	//but if the first element is com.cc.lc.interpreter.ListElement, it's a totally different story.
		        	else if(firstElement instanceof ListElement) {
		        		//Now honestly, a list starting with a list doesn't make much sense to com.cc.lc.interpreter.LL, so we'll just return NIL here
		    			ListElement lElement = (ListElement) firstElement;
		    			if(lElement.isNil()) {
		    				System.out.println("null car in eval");
		    				return ListElement.nil();
		    			} else if(1 == expressionListElement.getInnerElements().size()) {
		    				System.out.println("bad cons'ed object as function/lambda");
	    					return ListElement.nil();
		    			} else {
		    				return ListElement.nil();
		    			}
		    		} else if(firstElement instanceof NumberElement) {
		    			//likewise, with com.cc.lc.interpreter.NumberElement, this just doesn't make sense. solution? return NIL
		    			System.out.println("can't use number as function name");
		    			return ListElement.nil();
		    		} else {
		    			//once again, we're just confused at this point. solution? return NIL
		    			System.out.println("first element of wrong type");
		    			return ListElement.nil();
		    		}
	    		} else return listElement;
    		} else return listElement;
    	} else return listElement;
    }
    
    //builds up an com.cc.lc.interpreter.ExpressionElement
    public Element expr() throws ParsingExpressionException {
    	//well, if the current otken is a '('
        if(is(TK.LPAREN))
        	//then we know for sure that we're dealing with a list
        	//so build up a list
            return list();
        //otherwise, if the current token is an ID or a NUM
        else if(is(TK.ID) || is(TK.NUM))
        	// build up an atom.
            return atom();
        //but if it's a quoted expression
        else if(is(TK.QUOTE)) {
        	//we'll have to create a list in the form of (quote expr), where expr is 
        	//the expression following the quotation symbol
        	ExpressionListElement expressionListElement = new ExpressionListElement();
        	expressionListElement.addInnerElement(new IdElement(new Token(TK.QUOTE, "quote", tok.lineNumber)));
        	scan();
        	expressionListElement.addInnerElement(expr());
        	return new ListElement(expressionListElement);
        } else {
            parse_error("bad start of expression");
            return null; // silence, compiler!
        }
    }
    
    //builds up an com.cc.lc.interpreter.ExpressionListElement
    ExpressionListElement expression_list() throws ParsingExpressionException {
    	//if the next token is in first set of com.cc.lc.interpreter.ExpressionElement
    	if(ExpressionElement.firstSet().contains(tok.string.substring(0,1))) {
    		//then we can assume we're dealing with an com.cc.lc.interpreter.ExpressionElement
    		//so, we create an com.cc.lc.interpreter.ExpressionListElement
    		ExpressionListElement expressionListElement = new ExpressionListElement();
    		// and for every next expression
			while(ExpressionElement.firstSet().contains(tok.string.substring(0,1))) {
				//we add the built com.cc.lc.interpreter.ExpressionElement to the com.cc.lc.interpreter.ExpressionListElement
	    		expressionListElement.addInnerElement(expr());
			}
			return expressionListElement;
    	} else return null;
    }
    
    //builds up a com.cc.lc.interpreter.ListElement
    ListElement list() throws ParsingExpressionException {
        //require a '(' to start a list and signal that we are entering a list by increasing the level
        level++;
        mustbe(TK.LPAREN);
        //if we got here, then we can assume we're dealing with a list, so create a com.cc.lc.interpreter.ListElement
        ListElement listElement = new ListElement();
        //every com.cc.lc.interpreter.ListElement contains 1 and only 1 com.cc.lc.interpreter.ExpressionListElement, and no other Elements, so
        //add the build and add an com.cc.lc.interpreter.ExpressionListElement
        if(ExpressionListElement.firstSet().contains(tok.string.substring(0,1))) {
        	listElement.addInnerElement(expression_list());
        }
        //require a ')' to end a list and signal that we are exiting a list by decreasing the level
        level--;
        mustbe(TK.RPAREN);
        return listElement;
    }
    
    //builds up an com.cc.lc.interpreter.AtomElement, which is either an com.cc.lc.interpreter.IdElement or a com.cc.lc.interpreter.NumberElement
    Element atom() throws ParsingExpressionException {
    	Element element = null;
    	//if the current token is an com.cc.lc.interpreter.IdElement
        if( is(TK.ID) ) {
        	//create the com.cc.lc.interpreter.IdElement and verify that current token is an com.cc.lc.interpreter.IdElement
        	element = new IdElement(tok);
            mustbe(TK.ID);
        }
        //otherwise, if the current otken is a com.cc.lc.interpreter.NumberElement
        else if( is(TK.NUM) ) {
        	//create the com.cc.lc.interpreter.NumberElement and verify that current token is a com.cc.lc.interpreter.NumberElement
        	element = new NumberElement(tok);
            mustbe(TK.NUM);
        }
        //otherwise, this thing ain't an atom. damn
        else {
            parse_error("oops -- bad atom");
        }
        return element;
    }
    
    // is current token what we want?
    private boolean is(TK tk) {
        return tk == tok.kind;
    }

    // ensure current token is tk and skip over it.
    void mustbe(TK tk) throws ParsingExpressionException {
        if( !is(tk) ) {
            System.err.println( "mustbe: want " + tk + ", got " + tok );
            parse_error( "missing token (mustbe)" );
        }
        // read ahead to next token only if not at top level.
        // this enables returning to main loop after parse entire expression;
        // otherwise would need to wait for user to type first
        // part of next expression before evaluating current expression,
        // which wouldn't be so good in interactive environment.
        // (so main loop always calls scan before calling expr)
        if (level > 0) scan();
    }
    
    List<String> msgs = new ArrayList<String>();
    void semantic_error(String msg) {
    	msgs.add(msg);
    }
    
    void parse_error(String msg) throws ParsingExpressionException {
        System.err.println( "can't parse: " + msg );
        throw new ParsingExpressionException("problem parsing");
    }
    
    // used in recovering from errors.
    // gobble up all tokens up until something that could start an expression.
    // obviously, not entirely effective...
    // another possibility would be to gobble up to matching ) or ]
    // but that's not 100% effective either.
    void gobble() {
        while( level > 0 &&
               !is(TK.LPAREN) &&
               !is(TK.ID) &&
               !is(TK.NUM) &&
               !is(TK.EOF) ) {
            scan();
        }
    }
    
    boolean captureScan = false;
    StringBuilder capture = new StringBuilder();
    void startCapture() {
    	captureScan = true;
    }
    
    String stopCapture() {
    	captureScan = false;
    	return capture.toString();
    }
}
