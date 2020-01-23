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

public class Specialisation{
	
	protected String				code, name, url;
	protected List<Qualification>	qualifications;
	
	public Specialisation(String name, String url){
		this.name = name;
		this.url = url;
	}
	
	public String getName(){
		return name;
	}
	
	public String getUrl(){
		return url;
	}
	
	public String toString(){
		return name;
	}
	
	public void populateQualifications(JComboBox box){
		if(qualifications != null){
			populate(box);
		}else{
			loadQualifications();
		}
	}
	
	public List<Qualification> loadQualifications(){
		if(qualifications != null){ return qualifications; }
		qualifications = new ArrayList<>();
		try{
			Document doc = Jsoup.connect(url).execute().bufferUp().parse();
			Elements trs = doc.getElementsByClass("BackgroundLight");
			for(Element tr : trs){
				Element tbody = tr.getElementsByTag("tbody").get(1);
				// Element trCode = tbody.getElementsByTag("tr").get(0);
				// String code = trCode.text();
				Element a = tbody.getElementsByTag("a").get(0);
				String name = a.text();
				String url = "https://arion.aut.ac.nz/ArionMain/CourseInfo/Information/Qualifications/" + a.attr("href");
				String code = a.parent().text().trim().substring(name.length() + 1);
				Qualification course = new Qualification(code, name, url);
				qualifications.add(course);
				System.out.println("Found: " + course.toString());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return qualifications;
	}
	
	protected void populate(JComboBox box){
		box.removeAllItems();
		for(Qualification c : qualifications){
			box.addItem(c);
		}
	}
}
