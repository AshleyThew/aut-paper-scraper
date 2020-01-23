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

import com.ashleythew.aut.paper.data.*;
import com.ashleythew.aut.paper.data.Class;
import com.ashleythew.aut.paper.database.mysql.MySQLDatabase;
import com.ashleythew.aut.paper.database.mysql.MySQLProperties;
import com.ashleythew.aut.paper.scraper.database.*;

public class PaperScraper{
	
	public static void main(String[] args){
		PaperScraper scraper = new PaperScraper();
		scraper.saveResource(new File("."), "mysql.properties", false);
		scraper.loadSpecialisations();
		
		/// TODO
		
		scraper.getSpecialisations().forEach(scraper::work);
		
		scraper.closeDatabase();
	}
	
	private void work(Specialisation specialisation){
		specialisation.loadQualifications().forEach(this::work);
	}
	
	private void work(Qualification qualification){
		int qualificationID = QualificationDatabase.getInstance().addQualification(qualification);
		if(qualificationID == -1){ return; }
		System.out.println("Qualification " + qualificationID + ": " + qualification);
		try{
			Thread.sleep(0);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		qualification.loadLevels().forEach(level -> work(qualificationID, level));
	}
	
	private void work(int qualification, Level level){
		level.getPapers().forEach(p -> work(qualification, p));
	}
	
	private void work(int qualification, Paper paper){
		try{
			Thread.sleep(0);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		int paperID = PaperDatabase.getInstance().addPaper(qualification, paper);
		paper.getSemesters().forEach(s -> work(paperID, s));
		System.out.println("Paper " + paperID + ": " + paper);
	}
	
	private void work(int paper, Semester semester){
		int semesterID = SemesterDatabase.getInstance().addSemester(semester);
		semester.getStreams().forEach(s -> work(paper, semesterID, s));
	}
	
	private void work(int paperID, int semesterID, Stream stream){
		int streamID = StreamDatabase.getInstance().addStream(paperID, semesterID, stream);
		ClassDatabase.getInstance().addClasses(streamID, stream);
		stream.getClasses().forEach(c -> work(streamID, c));
	}
	
	private void work(int streamID, Class clazz){
		
	}
	
	private MySQLDatabase database;
	
	private PaperScraper(){
		MySQLProperties properties = new MySQLProperties(new File(".", "mysql.properties"));
		database = new MySQLDatabase(properties, true);
		database.addListener(PaperDatabase.getInstance());
		database.addListener(QualificationDatabase.getInstance());
		database.addListener(SemesterDatabase.getInstance());
		database.addListener(StreamDatabase.getInstance());
		database.addListener(ClassDatabase.getInstance());
	}
	
	private void closeDatabase(){
		database.closeConnection();
	}
	
	protected List<Specialisation> specialisations = new ArrayList<Specialisation>();
	
	public List<Specialisation> getSpecialisations(){
		return specialisations;
	}
	
	protected void loadSpecialisations(){
		try{
			// Get data from url
			Document doc = Jsoup.connect("https://arion.aut.ac.nz/ArionMain/CourseInfo/Information/Qualifications/Subjects.aspx").execute().bufferUp().parse();
			Elements navigations = doc.getElementsByClass("Navigation");
			for(Element navigation : navigations){
				String Qualification = navigation.text();
				String url = "https://arion.aut.ac.nz/ArionMain/CourseInfo/Information/Qualifications/" + navigation.attr("href");
				System.out.println("Found: " + Qualification);
				specialisations.add(new Specialisation(Qualification, url));
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
