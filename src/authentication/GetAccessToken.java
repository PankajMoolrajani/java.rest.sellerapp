package authentication;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.security.SecureRandom;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import db.DbConnection;
import db.DbUtils;
import authentication.BeanUserCredentials;


@Path("/authentication")
public  class GetAccessToken  {
	
	@Context private HttpServletRequest request;
	private String access_token;	
	private HttpSession session;
	
	@POST
	@Path("/get-access-token")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
    public String getAccessToken(BeanUserCredentials bean_user_credentials) {

        String username = bean_user_credentials.getUsername();
        String password = bean_user_credentials.getPassword();
        
        PreparedStatement ps = null;
    	ResultSet rs = null;
    	Connection con = null;
        boolean ps_result = this.veifyUserCredentials(username, password);
        if (ps_result){
			
			this.createUserSession();
			access_token = this.createAccessToken();
			session.setAttribute("access_token", createAccessToken());  
			
		//save access_token and session_id into db	(we should create a function for this also)		
			String table_name_session = "session";	    
	    	String query_session = "INSERT INTO "+table_name_session+" values (?,?)";
	    	String query = query_session;
	    	
	    	try{
		    	con = DbConnection.getConnection();
		    	ps = con.prepareStatement(query);
		    	ps.setString(1, session.getId());
		    	ps.setString(2, access_token);
		    	ps.execute();		    	
		    	
	    	}
	    	catch(SQLException e){	    		
	    		e.printStackTrace();
	    	}
	    	finally{
	    		DbUtils.closeUtil(ps);
	    		DbUtils.closeUtil(con);
	    	}
		}
        
        return access_token;
        
    }	

    public boolean veifyUserCredentials(String username, String password) {
        
    	boolean ps_result = false;
    	
    	String table_name_user = "user";
    	String column_name_user = "id_pass";
    	String condition_user = "name_user";
    	String query_user = "SELECT "+column_name_user+" FROM "+table_name_user+" WHERE "+condition_user+" = ?";
    	
    	String table_name_pass = "pass";
    	String column_name_pass = "password";
    	String condition_pass = "id_user IN ( "+query_user+" )";
    	String query_pass = "SELECT "+column_name_pass+" FROM "+table_name_pass+" WHERE "+condition_pass;
    	
    	String query = query_pass;
    	
    	Connection con = null;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	
    	try{
    		
        	con = db.DbConnection.getConnection();
    		//PreparedStatement ps = con.prepareStatement(query);
        	ps = con.prepareStatement(query);
    		ps.setString(1, username);
    		
    		//rs_result = ps.execute();
    		rs = ps.executeQuery();
    		rs.next();
    		String result_password = rs.getString("password");
    		
    		if(password.equals(result_password))
    		{    			
    			ps_result = true;    			
    		}
    	} 
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	finally
    	{
    		DbUtils.closeUtil(rs);
    		DbUtils.closeUtil(ps);
    		DbUtils.closeUtil(con);
    	}
        return ps_result;
        
    }	

    public void createUserSession() {    	
    	session = request.getSession(true);		     
    }	

    public String createAccessToken() {
    	
    	SecureRandom random = new SecureRandom();
    	String access_token = new BigInteger(130, random).toString(32);
    	
        return access_token;
        
    }	


 }