package com.ashleythew.aut.paper.scraper.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.ashleythew.aut.paper.data.Class;
import com.ashleythew.aut.paper.data.Stream;
import com.ashleythew.aut.paper.database.mysql.listener.SQLListener;

public class ClassDatabase extends SQLListener{
	
	private static ClassDatabase db = new ClassDatabase();
	
	public static ClassDatabase getInstance(){
		return db;
	}
	
	private ClassDatabase(){
		
	}
	
	private PreparedStatement addClass;
	
	@Override
	public void setup(Connection con){
		try{
			con.prepareStatement("DROP TABLE `class`;").execute();
			con.prepareStatement("CREATE TABLE IF NOT EXISTS `class` (`code` VARCHAR(50), `day` INT NOT NULL, `start` INT NOT NULL, `end` INT NOT NULL, `stream_id` INT NOT NULL);").execute();
			addClass = con.prepareStatement("INSERT  INTO `class` (`code`, `day`, `start`, `end`, `stream_id`) VALUES (?,?,?,?,?);");
		}catch(SQLException ex){
			ex.printStackTrace();
		}
	}
	
	public void addClasses(int streamID, Stream stream){
		try{
			for(Class clazz : stream.getClasses()){
				addClass.setString(1, clazz.getCode());
				addClass.setInt(2, clazz.getDay());
				addClass.setInt(3, clazz.getStart());
				addClass.setInt(4, clazz.getEnd());
				addClass.setInt(5, streamID);
				addClass.addBatch();
			}
			addClass.execute();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void close(Connection con){
		closeStatements();
	}
}
