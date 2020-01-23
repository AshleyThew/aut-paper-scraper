package com.ashleythew.aut.paper.database;

import java.sql.Connection;

import com.ashleythew.aut.paper.database.mysql.listener.SQLListener;

public abstract class Database{
	
	public abstract Connection openConnection();
	
	public abstract Connection getConnection();
	
	public abstract boolean isConnected();
	
	public abstract void closeConnection();
	
	public abstract void addListener(SQLListener listener);
}
