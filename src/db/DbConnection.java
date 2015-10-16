package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbConnection 
{
	private static Connection con = null;
	private DbConnection()
	{}
	public static Connection getConnection()
	{
		try
		{
			String db_url="";
			String db_user = "";
			String db_pass = "";
			Class.forName("com.mysql.jdbc.Driver");
			Connection con =  DriverManager.getConnection(db_url,db_user,db_pass);
			return con;
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			return con;
		}
	}
}
