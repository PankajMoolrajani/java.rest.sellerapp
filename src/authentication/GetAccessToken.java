package authentication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

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

	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
    public String getAccessToken(BeanUserCredentials bean_user_credentials) {
        String username = bean_user_credentials.getUsername();
        String password = bean_user_credentials.getPassword();
        this.veifyUserCredentials(username, password);
		String access_token = "pankaj - access - token"+username+" "+password;
        return access_token;
        
    }	

    public boolean veifyUserCredentials(String username, String password) {
        
    	Connection con = db.DbConnection.getConnection();
    	String table_name = "user";
    	String column_name = "id_pass";
    	String condition = "name_user";
    	
    	String query = "SELECT "+column_name+" FROM "+table_name+" WHERE "+condition+" = ?";
    	
    	try{
    		PreparedStatement ps = con.prepareStatement(query);
    		ps.setString(1, username);
    		Boolean ps_result = ps.execute();
    		System.out.println(ps_result);
    		
    		if (true){
    			this.createUserSession();
    			this.createAccessToken();
    		}
    	} 
    	catch(SQLException e){
    		e.printStackTrace();
    	}
        return false;
        
    }	

    public void createUserSession() {
        //crate user session
        
    }	

    public String createAccessToken() {
        //create with SecureRandomClass
        return null;
        
    }	


 }