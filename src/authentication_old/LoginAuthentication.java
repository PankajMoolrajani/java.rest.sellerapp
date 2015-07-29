package authentication_old;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import db.DbConnection;


//@Path("/authentication")
public class LoginAuthentication
{	
	@GET
	@Path("/access-token-url")	
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String,String> getAccessToken()throws Exception{						
		Map<String,String> map = new HashMap<String,String>();
		
		Connection con = DbConnection.getConnection();
		
		String table_rest_mapping = "rest_mapping";
		String columns_rest_mapping = "name_url";
		String condition_rest_mapping = " where map_url=?";	
		PreparedStatement ps_rest_mapping = con.prepareStatement("select "+columns_rest_mapping+" from "+table_rest_mapping+" "+condition_rest_mapping);
		ps_rest_mapping.setString(1, "access_token_url");
		ResultSet rs_rest_mapping = ps_rest_mapping.executeQuery();
		rs_rest_mapping.next();		
		map.put("access_token_url", rs_rest_mapping.getString("name_url"));
		return map;
	}		
	
	@GET
	@Path("/access-token-check-url")	
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String,String> getAccessTokenURL()throws Exception{						
		Map<String,String> map = new HashMap<String,String>();		
		Connection con = DbConnection.getConnection();
		
		String table_rest_mapping = "rest_mapping";
		String columns_rest_mapping = "name_url";
		String condition_rest_mapping = " where map_url=?";	
		PreparedStatement ps_rest_mapping = con.prepareStatement("select "+columns_rest_mapping+" from "+table_rest_mapping+" "+condition_rest_mapping);
		ps_rest_mapping.setString(1, "access_token_check_url");
		ResultSet rs_rest_mapping = ps_rest_mapping.executeQuery();		
		rs_rest_mapping.next();		
		map.put("access_token_check_url", rs_rest_mapping.getString("name_url"));
		return map;
	}		
	
	@Path("/get-token")
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public String getAuthenticationtoken(String jsonData)
	{		
		Gson gson  = new GsonBuilder().setPrettyPrinting().create();
		LoginBean loginBean = gson.fromJson(jsonData,LoginBean.class);
		Map<String,String> map = new HashMap<String,String>();		
		map.put("access_token","3c754d63c6a911e4f6d68b67c62c4e56");
		String authToken = gson.toJson(map);
		return authToken;
	}
	
	@Path("/check-token")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String checkAuthenticationtoken()
	{		
		Gson gson  = new GsonBuilder().setPrettyPrinting().create();
		Map<String,String> map = new HashMap<String,String>();		
		map.put("check_access_token","True");
		String checkToken = gson.toJson(map);
		return checkToken;		
	}
	

	
}
