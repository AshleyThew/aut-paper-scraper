/*
 * Copyright (c) 2019 Ashley Thew
 */

package com.ashleythew.aut.paper.data;

import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Qualification{
	
	protected String		code, name, url;
	protected List<Level>	levels;
	
	public Qualification(String code, String name, String url){
		this.code = code;
		this.name = name;
		this.url = url;
	}
	
	public String getCode(){
		return code;
	}
	
	public String getName(){
		return name;
	}
	
	public String getUrl(){
		return url;
	}
	
	public String toString(){
		return code + " - " + name;
	}
	
	public List<Level> loadLevels(){
		if(levels != null){ return levels;
		}
		levels = new ArrayList<>();
		try{
			Document doc = Jsoup.connect(url).execute().bufferUp().parse();
			Elements trs = doc.getElementsByClass("BackgroundLight");
			String url = "https://arion.aut.ac.nz/ArionMain/CourseInfo/Information/Qualifications/" + trs.get(2).getElementsByTag("a").get(0).attr("href").substring(3);
			doc = Jsoup.connect(url).execute().bufferUp().parse();
			Elements e = doc.getElementsByClass("TextHeading");
			// For each heading
			for(Element e1 : e){
				// If is our level
				if(e1.text().startsWith("Level ")){
					String level_number = e1.text();
					// Go up one element
					Element tr = e1.parent();
					// Go up another element
					Element tbody = tr.parent();
					// All table rows
					trs = tbody.getElementsByTag("tr");
					// This is the paper codes index
					int in = trs.indexOf(tr) + 2;
					// The paper codes table
					Element e2 = trs.get(in);
					
					List<Paper> papers = new ArrayList<>();
					// For each paper code
					for(Element e3 : e2.getElementsByClass("BackgroundLight")){
						// Paper code elements
						Elements e4 = e3.getElementsByTag("a");
						// Paper code
						Element code = e4.first();
						// Paper name
						Element name = e4.get(1);
						// Paper url
						Paper paper = new Paper(code.text(), name.text(), code.attr("href"));
						// Add to list
						papers.add(paper);
						System.out.println("Found: " + level_number + " - " + paper);
					}
					Level level = new Level(level_number, papers);
					levels.add(level);
				}
			}
		}catch(Exception e){
			System.out.println("Error with " + url);
			e.printStackTrace();
		}
		return levels;
	}
	
	protected void populate(JComboBox box){
		box.removeAllItems();
		for(Level l : levels){
			box.addItem(l);
		}
	}
}
