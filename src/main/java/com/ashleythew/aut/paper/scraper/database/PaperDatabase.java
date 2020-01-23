package com.ashleythew.aut.paper.scraper.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ashleythew.aut.paper.data.Paper;
import com.ashleythew.aut.paper.database.mysql.listener.SQLListener;

public class PaperDatabase extends SQLListener{
	
	private static PaperDatabase db = new PaperDatabase();
	
	public static PaperDatabase getInstance(){
		return db;
	}
	
	private PaperDatabase(){
		
	}
	
	private PreparedStatement addPaper, getPaper;
	
	@Override
	public void setup(Connection con){
		try{
			con.prepareStatement("CREATE TABLE IF NOT EXISTS `paper` (`id` INT NOT NULL AUTO_INCREMENT, `code` VARCHAR(50), `name` TINYTEXT, `qualification_id` INT NOT NULL, PRIMARY KEY (`id`, `code`));").execute();
			addPaper = con.prepareStatement("INSERT  INTO `paper` (`code`, `name`, `qualification_id`) VALUES (?,?,?);");
			getPaper = con.prepareStatement("SELECT * FROM `paper` WHERE `code` = ?;");
		}catch(SQLException ex){
			ex.printStackTrace();
		}
	}
	
	public int addPaper(int qualification, Paper paper){
		int id;
		if((id = getPaper(paper)) != -1){ return id; }
		try{
			addPaper.setString(1, paper.getCode());
			addPaper.setString(2, paper.getName());
			addPaper.setInt(3, qualification);
			addPaper.execute();
			id = getPaper(paper);
		}catch(Exception e){
			e.printStackTrace();
		}
		return id;
	}
	
	private int getPaper(Paper paper){
		try{
			getPaper.setString(1, paper.getCode());
			try(ResultSet rs = getPaper.executeQuery()){
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
