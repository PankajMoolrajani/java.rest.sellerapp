package manage_exceptions;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


import com.google.gson.Gson;

import db.DbConnection;

@Path("/exception")
public class ManageException 
{
	@GET	
	@Path("/{errorCode}")
	@Produces(MediaType.TEXT_PLAIN)
	public String manageException( @PathParam("errorCode") String errorCode)
	{										
		Map<String,String> map = new HashMap<String,String>();
		try
		{
			Connection con = DbConnection.getConnection();
			PreparedStatement ps = con.prepareStatement("select error_message from error_codes where code=?");
			ps.setString(1, errorCode);
			ResultSet rs = ps.executeQuery();
			rs.next();
			String error_message = rs.getString("error_message");
			map.put("error_message", error_message);
			con.close();								
		}
		catch(Exception e)
		{
			System.out.println(e);			
		}
		return new Gson().toJson(map);				
	}
}
