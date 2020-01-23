package com.ashleythew.aut.paper.scraper.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ashleythew.aut.paper.data.Qualification;
import com.ashleythew.aut.paper.database.mysql.listener.SQLListener;

public class QualificationDatabase extends SQLListener{
	
	private static QualificationDatabase db = new QualificationDatabase();
	
	public static QualificationDatabase getInstance(){
		return db;
	}
	
	private QualificationDatabase(){
		
	}
	
	private PreparedStatement addQualification, getQualification;
	
	@Override
	public void setup(Connection con){
		try{
			con.prepareStatement("CREATE TABLE IF NOT EXISTS `qualification` (`id` INT NOT NULL AUTO_INCREMENT, `code` VARCHAR(50), `name` TINYTEXT, PRIMARY KEY (`id`, `code`));").execute();
			addQualification = con.prepareStatement("INSERT  INTO `qualification` (`code`, `name`) VALUES (?,?);");
			getQualification = con.prepareStatement("SELECT * FROM `qualification` WHERE `code` = ?;");
		}catch(SQLException ex){
			ex.printStackTrace();
		}
	}
	
	public int addQualification(Qualification qualification){
		int id;
		if((id = getQualification(qualification)) != -1){ return id; }
		try{
			addQualification.setString(1, qualification.getCode());
			addQualification.setString(2, qualification.getName());
			addQualification.execute();
			id = getQualification(qualification);
		}catch(Exception e){
			e.printStackTrace();
		}
		return id;
	}
	
	private int getQualification(Qualification qualification){
		try{
			getQualification.setString(1, qualification.getCode());
			try(ResultSet rs = getQualification.executeQuery()){
				if(rs.next()){ return rs.getInt("id"); }
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		return -1;
	}
	
	@Override
	public void close(Connection con){
		closeStatements();
	}
}
