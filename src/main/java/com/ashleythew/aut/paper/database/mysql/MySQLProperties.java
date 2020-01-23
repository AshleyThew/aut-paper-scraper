package com.ashleythew.aut.paper.database.mysql;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class MySQLProperties{
	
	private MySQL dbInfo;
	
	public MySQLProperties(File file){
		dbInfo = loadDBInfo(file);
	}
	
	private MySQL loadDBInfo(File file){
		Properties properties = readProperties(file);
		
		String url = properties.getProperty("jdbc.url");
		String port = properties.getProperty("jdbc.port");
		String database = properties.getProperty("jdbc.database");
		String username = properties.getProperty("jdbc.username");
		String password = properties.getProperty("jdbc.password");
		String extra = properties.getProperty("jdbc.extra");
		
		return new MySQL(url, port, database, username, password, extra);
	}
	
	private Properties readProperties(File file){
		Properties properties = new Properties();
		readProperties(file);
		return properties;
	}
	
	private void readProperties(Properties properties, File file){
		try(FileInputStream inputStream = new FileInputStream(file)){
			properties.load(inputStream);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
}
