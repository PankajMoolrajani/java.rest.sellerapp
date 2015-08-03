

package user;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.Gson;

import db.DbConnection;
import user.BeanUser;



public  class User  {

	

   

    public String createUser(BeanUser bean_user) {
        
        
    	String name_user = bean_user.getPassword();
    	String password = bean_user.getPassword();
    	
    	Boolean verification_credentials = this.verifyPasswordComplexity(name_user, password);
    	if (verification_credentials == true){
    		//insert password
        	//insert address
        	//insert user
    		Connection con = DbConnection.getConnection();
    		
    		int id_pass = 0;
    		String table_name = "pass";
    		String column_names = "password, question, ans";
    		String values = "?, ?, ?";
    		String query = "INSERT INTO "+table_name+"("+column_names+")"+" VALUES "+"("+values+")";
    		
    		
    		try {
    			
    			PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    			
    			ps.setString(1, bean_user.getPassword());
    			ps.setString(2, bean_user.getQuestion());
    			ps.setString(3, bean_user.getAns());
    			
    			int rows_affected =  ps.executeUpdate();
    			
    			if (rows_affected != 0){
    				
    				ResultSet rs = ps.getGeneratedKeys();
    				
    				if (rs.next()){
    				
    					id_pass = rs.getInt(1);
    					System.out.println(id_pass);
    				
    				}
    			
    			}
    		
    		} catch (SQLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}


    	}
    	
    	return null;
        
        
    }	
    
    public Boolean verifyPasswordComplexity(String name_user, String password) {
        
        return true;
        
    }	

    public String updateUser(BeanUser bean_user) {
        
        return null;
        
    }	

    public String deleteUser(BeanUser bean_user) {
        
        return null;
        
    }	

    public String getUser(Object identifier) {
        
        return null;
        
    }	


 }