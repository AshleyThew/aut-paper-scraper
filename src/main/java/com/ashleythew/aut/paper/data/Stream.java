/*
 * Copyright (c) 2019 Ashley Thew
 */

package com.ashleythew.aut.paper.data;

import java.util.ArrayList;
import java.util.List;

public class Stream{
	
	private String		code;
	private Campus		campus;
	private List<Class>	classes	= new ArrayList<Class>();
	
	public Stream(String code){
		this.code = code;
	}
	
	public String getCode(){
		return code;
	}
	
	public List<Class> getClasses(){
		return classes;
	}
	
	public Campus getCampus(){
		return campus;
	}
	
	public void setCampus(Campus campus){
		this.campus = campus;
	}
	
	public boolean isValidCampus(Campus campus){
		return this.campus.getCode().startsWith(campus.getCode());
	}
	
	public String toString(){
		String to = campus.name() + " " + code;
		for(Class c : classes){
			to += "\n" + c.toString();
		}
		return to;
	}
	
}