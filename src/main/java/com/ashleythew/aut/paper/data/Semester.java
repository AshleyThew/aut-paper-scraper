/*
 * Copyright (c) 2019 Ashley Thew
 */

package com.ashleythew.aut.paper.data;

import java.util.ArrayList;
import java.util.List;

public class Semester {
	
	protected String		semester;
	protected List<Stream>	streams	= new ArrayList<>();
	
	public Semester(String semester){
		this.semester = semester;
	}
	
	public String getSemester(){
		return semester;
	}
	
	public List<Stream> getStreams(){
		return streams;
	}
	
	public boolean hasCampus(Campus campus){
		for(Stream s : streams){
			if(s.isValidCampus(campus)){ return true; }
		}
		return false;
	}
	
	public List<Stream> getStreams(Campus campus){
		List<Stream> streams = new ArrayList<>();
		for(Stream stream : this.streams){
			if(stream.isValidCampus(campus)){
				streams.add(stream);
			}
		}
		return streams;
	}
}
