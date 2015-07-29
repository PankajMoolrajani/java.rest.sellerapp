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
        System.out.println("got request");
        String username = bean_user_credentials.getUsername();
        String password = bean_user_credentials.getPassword();
		String access_token = "pankaj - access - token"+username+" "+password;
        return access_token;
        
    }	

    public boolean veifyUserCredentials(String username, boolean password) {
        
        return false;
        
    }	

    public void createUserSession() {
        
        
    }	

    public String createAccessToken() {
        //create with SecureRandomClass
        return null;
        
    }	


 }