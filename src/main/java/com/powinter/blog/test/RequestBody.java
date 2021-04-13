package com.powinter.blog.test;

import java.util.HashMap;

public class RequestBody extends HashMap<String,Object> implements Cloneable{
	private static final long serialVersionUID = -3667743373914393933L;
	@Override
    public RequestBody clone() {
		RequestBody body = new RequestBody();
		for(Entry<String,Object> entry : super.entrySet()) {
			this.put(entry.getKey(),entry.getValue());
		}
		return body;
    }
}
