package com.cc.lc.interpreter;


public class TokenElement extends Element {
	protected Token token = null;
	
	public void setToken(Token token) {
		this.token = token;
	}
	
	public Token getToken() {
		return token;
	}
	
	public String toString() {
		return new StringBuilder(prefix()).append(token.string).append(postfix()).toString();
	}
}
