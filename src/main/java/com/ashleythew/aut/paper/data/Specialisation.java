/*
 * Copyright (c) 2019 Ashley Thew
 */

package com.ashleythew.aut.paper.data;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Specialisation {
	
	protected String		name, url;
	protected List<Course>	courses;
	
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
		if(courses != null){
			populate(box);
		}else{
			loadCourses();
		}
	}
	
	public void loadCourses(){
		courses = new ArrayList<>();
		try{
			Document doc = Jsoup.connect(url).get();
			Elements trs = doc.getElementsByClass("BackgroundLight");
			for(Element tr : trs){
				Element tbody = tr.getElementsByTag("tbody").get(1);
				// Element trCode = tbody.getElementsByTag("tr").get(0);
				// String code = trCode.text();
				Element a = tbody.getElementsByTag("a").get(0);
				String name = a.text();
				String url = "https://arion.aut.ac.nz/ArionMain/CourseInfo/Information/Qualifications/" + a.attr("href");
				String code = a.parent().text().trim().substring(name.length() + 1);
				Course course = new Course(code, name, url);
				courses.add(course);
				System.out.println("Found: " + course.toString());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	protected void populate(JComboBox box){
		box.removeAllItems();
		for(Course c : courses){
			box.addItem(c);
		}
	}
}
