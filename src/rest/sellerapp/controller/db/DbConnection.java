package rest.sellerapp.controller.db;

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
			String db_url="jdbc:mysql://dev.monoxor.com/db_pkagencies";
			String db_user = "dev";
			String db_pass = "ecom";
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
