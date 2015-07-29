package authentication;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.security.SecureRandom;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import db.DbConnection;
import authentication.BeanUserCredentials;


@Path("/authentication/get-access-token")
public  class GetAccessToken  {
	
	String access_token;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
    public String getAccessToken(BeanUserCredentials bean_user_credentials) {
        String username = bean_user_credentials.getUsername();
        String password = bean_user_credentials.getPassword();
        boolean ps_result = this.veifyUserCredentials(username, password);
        if (ps_result){
			
			this.createUserSession();
			access_token = this.createAccessToken();
			System.out.println(access_token);
			
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
    	String condition_pass = "( "+query_user+" )";
    	String query_pass = "SELECT "+column_name_pass+" FROM "+table_name_pass+" WHERE "+condition_pass;
    	
    	String query = query_pass;
    	
    	try{
    		
        	Connection con = db.DbConnection.getConnection();
    		PreparedStatement ps = con.prepareStatement(query);
    		ps.setString(1, username);
    		ps_result = ps.execute();
    		
    	} 
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	
        return ps_result;
        
    }	

    public void createUserSession() {
        //create user session
        
    }	

    public String createAccessToken() {
    	
    	SecureRandom random = new SecureRandom();
    	String access_token = new BigInteger(130, random).toString(32);
    	
        return access_token;
        
    }	


 }