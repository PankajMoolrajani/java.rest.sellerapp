

package user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import db.DbConnection;


@Path("/user/category")
public  class UserCategory  {

	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	
    public String createUserCategory(BeanUserCategory bean_user_category) {
        int id_user_category = 0;
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("id",bean_user_category.getId());
		map.put("name", bean_user_category.getName());
		map.put("description", bean_user_category.getDescription());
        
        Connection con = DbConnection.getConnection();
		String table_name = "user_category";
		String column_names = "name, description";
		String values = "?, ?";
		String query = "INSERT INTO "+table_name+"("+column_names+")"+" VALUES "+"("+values+")";
		
		
		try {
			
			PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			
			ps.setString(1, bean_user_category.getName());
			ps.setString(2, bean_user_category.getDescription());
			
			int rows_affected =  ps.executeUpdate();
			
			if (rows_affected != 0){
				
				ResultSet rs = ps.getGeneratedKeys();
				
				if (rs.next()){
				
					id_user_category = rs.getInt(1);
					map.put("id_user_category", id_user_category);
				
				}
			
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new Gson().toJson(map);	
    }	

	
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	
    public String updateUserCategory(BeanUserCategory bean_user_category) {
        int id_user_category = 0;
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("id",bean_user_category.getId());
		map.put("name", bean_user_category.getName());
		map.put("description", bean_user_category.getDescription());
        
        Connection con = DbConnection.getConnection();
		String table_name = "user_category";
		String column_name = "name";
		String column_description = "description";
		String query = "UPDATE "+table_name+" SET "+column_name+"=?"+", "+column_description+"=?"+" WHERE id=?";
		
		
		try {
			
			PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			
			ps.setString(1, bean_user_category.getName());
			ps.setString(2, bean_user_category.getDescription());
			ps.setInt(3, bean_user_category.getId());
			
			int rows_affected =  ps.executeUpdate();
			System.out.println(rows_affected);
			if (rows_affected != 0){
				
				ResultSet rs = ps.getGeneratedKeys();
				
				if (rs.next()){
				
					id_user_category = rs.getInt(1);
					map.put("id_user_category", id_user_category);
				
				}
			
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new Gson().toJson(map);	
    }	


	@POST
	@Path("/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	
    public String deleteUserCategory(BeanUserCategory bean_user_category) {
        int id_user_category = 0;
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("id",bean_user_category.getId());
		map.put("name", bean_user_category.getName());
		map.put("description", bean_user_category.getDescription());
        
        Connection con = DbConnection.getConnection();
		String table_name = "user_category";
		String query = "DELETE from "+table_name+" WHERE id=?";
		
		
		try {
			
			PreparedStatement ps = con.prepareStatement(query);
			
			ps.setInt(1, bean_user_category.getId());
			
			int rows_affected =  ps.executeUpdate();
			
			if (rows_affected != 0){
				
				System.out.println("user_category:delete : "+bean_user_category.getId()+" : success");
				map.put("result", "success");
			
			}
			else {
				System.out.println("user_category:delete:"+bean_user_category.getId()+":fail");
				map.put("result", "fail");
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new Gson().toJson(map);	
    }	

	
	@POST
	@Path("/get")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	
    public String getUserCategory(BeanUserCategory bean_user_category) {
        int id_user_category = 0;
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("id",bean_user_category.getId());
		map.put("name", bean_user_category.getName());
		map.put("description", bean_user_category.getDescription());
        
        Connection con = DbConnection.getConnection();
		String table_name = "user_category";
		String column_names = "name, description";
		String values = "?, ?";
		String query = "INSERT INTO "+table_name+"("+column_names+")"+" VALUES "+"("+values+")";
		
		
		try {
			
			PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			
			ps.setString(1, bean_user_category.getName());
			ps.setString(2, bean_user_category.getDescription());
			
			int rows_affected =  ps.executeUpdate();
			
			if (rows_affected != 0){
				
				ResultSet rs = ps.getGeneratedKeys();
				
				if (rs.next()){
				
					id_user_category = rs.getInt(1);
					map.put("id_user_category", id_user_category);
				
				}
			
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new Gson().toJson(map);	
    }	

 }