package com.ashleythew.aut.paper.scraper;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ashleythew.aut.paper.data.Specialisation;

public class PaperScraper{
	
	public static void main(String[] args){
		PaperScraper scraper = new PaperScraper();
		scraper.saveResource(new File("."), "mysql.properties", false);
		scraper.loadSpecialisations();
	}
	
	protected List<Specialisation> specialisations = new ArrayList<Specialisation>();
	
	public List<Specialisation> getSpecialisations(){
		return specialisations;
	}
	
	protected void loadSpecialisations(){
		try{
			// Get data from url
			Document doc = Jsoup.connect("https://arion.aut.ac.nz/ArionMain/CourseInfo/Information/Qualifications/Subjects.aspx").get();
			Elements navigations = doc.getElementsByClass("Navigation");
			for(Element navigation : navigations){
				String specialisation = navigation.text();
				String url = "https://arion.aut.ac.nz/ArionMain/CourseInfo/Information/Qualifications/" + navigation.attr("href");
				System.out.println("Found: " + specialisation);
				specialisations.add(new Specialisation(specialisation, url));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public InputStream getResource(String filename){
		if(filename == null){ throw new IllegalArgumentException("Filename cannot be null"); }
		
		try{
			URL url = this.getClass().getClassLoader().getResource(filename);
			
			if(url == null){ return null; }
			
			URLConnection connection = url.openConnection();
			connection.setUseCaches(false);
			return connection.getInputStream();
		}catch(IOException ex){
			return null;
		}
	}
	
	public void saveResource(File folder, String resourcePath, boolean replace){
		if((resourcePath == null) || (resourcePath.equals(""))){ throw new IllegalArgumentException("ResourcePath cannot be null or empty"); }
		resourcePath = resourcePath.replace('\\', '/');
		InputStream in = getResource(resourcePath);
		if(in == null){
			System.out.println("The embedded resource '" + resourcePath + "' cannot be found");
			return;
		}
		File outFile = new File(folder, resourcePath);
		int lastIndex = resourcePath.lastIndexOf('/');
		File outDir = new File(folder, resourcePath.substring(0, lastIndex >= 0 ? lastIndex : 0));
		if(!outDir.exists()){
			outDir.mkdirs();
		}
		try{
			if(!outFile.exists() || replace){
				OutputStream out = new FileOutputStream(outFile);
				byte[] buf = new byte[1024];
				int len;
				while((len = in.read(buf)) > 0){
					out.write(buf, 0, len);
				}
				out.close();
				in.close();
			}else{
				System.out.print("Could not save " + outFile.getName() + " to " + outFile + " because " + outFile.getName() + " already exists.");
			}
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}
}
