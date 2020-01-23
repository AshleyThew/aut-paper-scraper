package com.ashleythew.aut.paper.scraper;

import com.ashleythew.aut.paper.data.Specialisation;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class PaperScraper {

    public static void main(String[] args) {
        PaperScraper scraper = new PaperScraper();
        scraper.loadSpecialistions();
    }

    protected List<Specialisation> specialisations = new ArrayList<Specialisation>();

    public List<Specialisation> getSpecialisations(){
        return specialisations;
    }

    protected void loadSpecialistions(){
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
}
