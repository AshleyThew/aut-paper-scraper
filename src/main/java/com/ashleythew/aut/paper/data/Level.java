/*
 * Copyright (c) 2019 Ashley Thew
 */

package com.ashleythew.aut.paper.data;

import java.util.List;

import javax.swing.*;

public class Level{
	
	protected String		level;
	protected List<Paper>	papers;
	
	public Level(String level, List<Paper> papers){
		this.level = level;
		this.papers = papers;
	}
	
	public String getLevel(){
		return level;
	}
	
	public List<Paper> getPapers(){
		return papers;
	}
	
	public String toString(){
		return level;
	}
	
	public void populate(JComboBox box){
		box.removeAllItems();
		for(Paper p : papers){
			box.addItem(p);
		}
	}
}
