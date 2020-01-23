/*
 * Copyright (c) 2019 Ashley Thew
 */

package com.ashleythew.aut.paper.data;

import java.util.ArrayList;
import java.util.List;

public class Timetable{
	
	private List<Class> classes = new ArrayList<Class>();
	
	public Timetable(){
		
	}
	
	public List<Class> getClasses(){
		return classes;
	}
	
	public boolean tryAdd(Stream s){
		for(Class c : classes){
			for(Class c1 : s.getClasses()){
				if(c.collides(c1)){ return false; }
			}
		}
		classes.addAll(s.getClasses());
		return true;
	}
	
	public List<String> getUnique(){
		List<String> list = new ArrayList<String>();
		for(Class c : classes){
			if(!list.contains(c.getCode())){
				list.add(c.getCode());
			}
		}
		return list;
	}
	
	public Timetable clone(){
		Timetable tt = new Timetable();
		tt.classes.addAll(classes);
		return tt;
	}
	
	public boolean hasClass(String code){
		for(Class clas : classes){
			if(clas.getCode().equals(code)){ return true; }
		}
		return false;
	}
	
	public String[][] generateData(){
		int low = getLowestHour();
		int high = getMaxHour();
		int size = high - low;
		String[][] data = new String[size][6];
		String poss[][] = new String[5][24];
		// For each class
		for(Class c : classes){
			// For each hour in each class
			for(int hour = c.getStart(); hour < c.getEnd(); hour++){
				// Put code into time slot
				poss[c.getDay() - 1][hour] = c.getCode();
			}
		}
		for(int hour = low; hour < high; hour++){
			// Calculate hour to print
			List<String> row = new ArrayList<>();
			int print = hour > 12 ? hour - 12 : hour;
			row.add("" + print);
			int position = hour - low;
			// For each day
			for(int day = 0; day < 5; day++){
				// Print seperator
				// Get timeslot for this day and hour
				String b = poss[day][hour];
				// If isnt null
				if(b != null){
					row.add(b);
				}else{
					row.add("");
				}
			}
			data[position] = row.toArray(new String[0]);
		}
		return data;
	}
	
	// Method to get the lowest hour from a timetable
	public int getLowestHour(){
		// Init low to highest number
		int low = 24;
		// For each class
		for(Class c : classes){
			// Reassign low to the lowest
			low = Math.min(low, c.getStart());
		}
		return low;
	}
	
	// Method to get the highest hour from a timetable
	public int getMaxHour(){
		// Init max to lowest number
		int max = 0;
		// For each class
		for(Class c : classes){
			// Reassign max to the highest
			max = Math.max(max, c.getEnd());
		}
		return max;
	}
	
}
