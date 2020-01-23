package com.ashleythew.aut.paper.scraper.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ashleythew.aut.paper.data.Semester;
import com.ashleythew.aut.paper.database.mysql.listener.SQLListener;

public class SemesterDatabase extends SQLListener{
	
	private static SemesterDatabase db = new SemesterDatabase();
	
	public static SemesterDatabase getInstance(){
		return db;
	}
	
	private SemesterDatabase(){
		
	}
	
	private PreparedStatement addSemester, getSemester;
	
	@Override
	public void setup(Connection con){
		try{
			con.prepareStatement("CREATE TABLE IF NOT EXISTS `semester` (`id` INT NOT NULL AUTO_INCREMENT, `semester` VARCHAR(20), PRIMARY KEY (`id`, `semester`));").execute();
			addSemester = con.prepareStatement("INSERT  INTO `semester` (`semester`) VALUES (?);");
			getSemester = con.prepareStatement("SELECT * FROM `semester` WHERE `semester` = ?;");
		}catch(SQLException ex){
			ex.printStackTrace();
		}
	}
	
	public int addSemester(Semester semester){
		int id;
		if((id = getSemester(semester)) != -1){ return id; }
		try{
			addSemester.setString(1, semester.getSemester());
			addSemester.execute();
			id = getSemester(semester);
		}catch(Exception e){
			System.out.println(semester.getSemester());
			e.printStackTrace();
			try{
				Thread.sleep(5000);
			}catch(InterruptedException e1){
				e1.printStackTrace();
			}
		}
		return id;
	}
	
	private int getSemester(Semester semester){
		try{
			getSemester.setString(1, semester.getSemester());
			try(ResultSet rs = getSemester.executeQuery()){
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
