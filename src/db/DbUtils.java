package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DbUtils
{	
	public static void closeUtil(ResultSet rs)
	{
		try
		{
			if(rs!=null)
			rs.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}	
	public static void closeUtil(PreparedStatement stmt)
	{
		try
		{
			if(stmt!=null)
			stmt.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
	}	
	public static void closeUtil(Connection con)
	{
		try
		{
			if(con!=null)
			con.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
	}
}
