/*
 * Copyright (c) 2019 Ashley Thew
 */

package com.ashleythew.aut.paper.data;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class Paper {
	
	private String				code, name, url;
	protected List<Semester>	semesters;
	
	public Paper(String code, String name, String url){
		this.code = code;
		this.name = name;
		this.url = "https://arion.aut.ac.nz/ArionMain/CourseInfo/Information/Qualifications/" + url;
	}
	
	public String getName(){
		return name;
	}
	
	public String getCode(){
		return code;
	}
	
	public String getUrl(){
		return url;
	}
	
	public String toString(){
		return code + " - " + name;
	}
	
	public boolean hasSemester(String semester){
		return getSemester(semester) != null;
	}
	
	public Semester getSemester(String semester){
		for(Semester sem : semesters){
			if(sem.getSemester().equals(semester)){ return sem; }
		}
		return null;
	}
	
	public List<Semester> getSemesters(){
		if(semesters != null){ return semesters; }
		semesters = new ArrayList<>();
		// Gets all possible semesters and timetable element from url
		try{
			// Get data from url
			Document doc = Jsoup.connect(url).get();
			// Find Timetable element and go up a few elements
			Element tbody = doc.getElementsByAttributeValue("name", "#Timetable").first().parent().parent().parent();
			// Get the table for the timetables
			Elements tables = tbody.getElementsByTag("table");
			// Check size
			if(tables.size() > 0){
				// Get the table
				Element table = tables.get(0);
				// Get all table rows
				Elements trs = table.getElementsByTag("tr");
				// For each td element
				for(Element e1 : table.getElementsByTag("td")){
					// Get span elements
					Elements spans = e1.getElementsByTag("span");
					// If this element is a semester
					if(spans.size() == 2){
						// Get the parent element
						Element tr_p = e1.parent();
						// Gets semester from spans
						String name = spans.get(0).text() + ", " + spans.get(1).text();
						// Gets index of parent element
						int index = trs.indexOf(tr_p) + 1;
						// This is the classes element
						Element s_table = trs.get(index);
						// Add to map
						System.out.println("Found: " + name);
						Semester semester = new Semester(name);
						// Current Stream
						Stream s = null;
						// For each row in table
						for(Element tr : s_table.getElementsByClass("BackgroundLight")){
							// For each column
							Elements tds = tr.getElementsByAttribute("width");
							// Check size
							if(tds.size() > 0){
								// Get possible stream name
								String stream = tds.get(0).text();
								// If stream name not empty
								if(stream.length() > 1){
									// If current Stream isnt null
									if(s != null){
										// Add to list
										semester.getStreams().add(s);
										System.out.println(s);
									}
									// New Stream
									s = new Stream(stream);
								}
								// Day of class
								int day = Day.valueOf(tds.get(4).text().substring(0, 3)).ordinal() + 1;
								// Get time for class
								String time = tds.get(5).text();
								// Split to get start and end times
								// 4:00 p.m. - 6:00 p.m.
								String split[] = time.split(". - ");
								// Gets start time
								int start = Integer.parseInt(split[0].split(":")[0]);
								// If is pm
								if(split[0].contains("p") && start != 12){
									// Convert to 24 hour
									start += 12;
								}
								// Gets end time
								int end = Integer.parseInt(split[1].split(":")[0]);
								// If is pm
								if(split[1].contains("p") && end != 12){
									// Convert to 24 hour
									end += 12;
								}
								// Add class to Stream
								String room = tds.get(6).text();
								s.setCampus(Campus.getByRoom(room));
								s.getClasses().add(new Class(s.getCode(), tds.get(6).text(), day, start, end));
							}
						}
						// If Stream isnt null
						if(s != null){
							semester.getStreams().add(s);
							System.out.println(s);
						}
						semesters.add(semester);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return semesters;
	}
	
}