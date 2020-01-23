package com.ashleythew.aut.paper.scraper.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ashleythew.aut.paper.data.Stream;
import com.ashleythew.aut.paper.database.mysql.listener.SQLListener;

public class StreamDatabase extends SQLListener{
	
	private static StreamDatabase db = new StreamDatabase();
	
	public static StreamDatabase getInstance(){
		return db;
	}
	
	private StreamDatabase(){
		
	}
	
	private PreparedStatement addStream, getStream;
	
	@Override
	public void setup(Connection con){
		try{
			con.prepareStatement("CREATE TABLE IF NOT EXISTS `stream` (`id` INT NOT NULL AUTO_INCREMENT, `code` VARCHAR(16), `campus` TINYTEXT, `paper_id` INT NOT NULL, `stream_id` INT NOT NULL, PRIMARY KEY (`id`, `code`));").execute();
			addStream = con.prepareStatement("INSERT  INTO `stream` (`code`, `campus`, `paper_id`, `stream_id`) VALUES (?,?,?,?);");
			getStream = con.prepareStatement("SELECT * FROM `stream` WHERE `code` = ?;");
		}catch(SQLException ex){
			ex.printStackTrace();
		}
	}
	
	public int addStream(int paperID, int streamID, Stream stream){
		int id;
		if((id = getStream(stream)) != -1){ return id; }
		try{
			addStream.setString(1, stream.getCode());
			addStream.setString(2, stream.getCampus().name());
			addStream.setInt(3, paperID);
			addStream.setInt(4, streamID);
			addStream.execute();
			id = getStream(stream);
		}catch(Exception e){
			e.printStackTrace();
		}
		return id;
	}
	
	private int getStream(Stream stream){
		try{
			getStream.setString(1, stream.getCode());
			try(ResultSet rs = getStream.executeQuery()){
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
