package com.expando.database
import java.sql.Connection


interface ServerConnection {

	public Connection getConnection()
	public String getConnectionURL()
	public String getConnectionStatus()
}
