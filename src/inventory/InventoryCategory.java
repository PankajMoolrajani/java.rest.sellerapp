

package inventory;

import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

import inventory.BeanInventoryCategory;
import db.DbConnection;

@Path("/inventory/category")
public  class InventoryCategory  {

	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	
    public String createInventoryCategory(BeanInventoryCategory bean_inventory_category) {
		
		Map <String,Object> map = new HashMap<String,Object>();
		
		int id = 0;
		
		if (checkExistence(bean_inventory_category) == false){
			
			id = this.create(bean_inventory_category);
			
			if (id != 0){
	
				map.put("id", id);
				
				map.put("response_code", 2000);
				map.put("response_message", "success: create category");
				
				return new Gson().toJson(map);
				
			}
			else {
				
				map.put("id", 0);
				
				map.put("response_code", 4000);
				map.put("response_message", "failure: create category");
				
				return new Gson().toJson(map);
				
			}
			
		}
		else {
			
			map.put("response_code", 4000);
			map.put("response_message", "category already exists");
			
			return new Gson().toJson(map);
		}
        
    }	
	
	int create(BeanInventoryCategory bean_inventory_category){
				
		int id = 0;
		
		String table_name = "inventory_category";
		String columns = "id_parent_category, name, name_table, id_tax";
		String values = "?, ?, ?, ?";
		String query = "INSERT INTO "+table_name+"("+columns+")"+" VALUES "+"("+values+")";
		
		Connection con = DbConnection.getConnection();
		
		try {
			
			PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, bean_inventory_category.getIdParentCategory());
			ps.setString(2, bean_inventory_category.getName());
			ps.setString(3, bean_inventory_category.getNameTable());
			ps.setInt(4, bean_inventory_category.getIdTax());
			
			int rows_affected =  ps.executeUpdate();
			
			if (rows_affected != 0){
				
				ResultSet rs = ps.getGeneratedKeys();
				
				if (rs.next()){
				
					id = rs.getInt(1);
					
				}    			
			}
				
		}
		catch (SQLException e){
			e.printStackTrace();
		}
		return id; 
	}
	
	Boolean checkExistence(BeanInventoryCategory bean_inventory_category){
		
		String table_name = "inventory_category";
		String column = "id";
		String condition = "id_parent_category=? and name=?";
		String query = "SELECT "+column+" FROM "+table_name+" WHERE "+condition;
		
		Connection con = DbConnection.getConnection();
		
		try {
			
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, bean_inventory_category.getIdParentCategory());
			ps.setString(2, bean_inventory_category.getName());
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()){
				return true;
			}
			
		} 
		catch (SQLException e){
			e.printStackTrace();
		}
		
		return false;
	}
	

    public String updateInventoryCategory(BeanInventoryCategory bean_inventory_category) {
        
        return null;
        
    }	
    
    
    @POST
	@Path("/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)

    public String deleteInventoryCategory(BeanInventoryCategory bean_inventory_category) {
        
    	Map <String,Object> map = new HashMap<String,Object>();
    	
    	String table_name = "inventory_category";
    	String condition = "id=?";
    	String query = "DELETE FROM "+table_name+" WHERE "+condition;
    	
    	Connection con = db.DbConnection.getConnection();
    	
    	try {
    		
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, bean_inventory_category.getId());
			
			if (ps.executeUpdate() == 1){
				map.put("response_code", 2000);
    			map.put("response_message", "success: inventory category delete");
			}
			else {
				map.put("response_code", 4000);
    			map.put("response_message", "failure: inventory category delete");
			}
			
		}
    	catch (Exception e) {
    		
    		if (e instanceof MySQLIntegrityConstraintViolationException){
    			map.put("response_code", 4000);
    			map.put("response_message", "failure: inventory category delete - constraint integrity exception");
    		}
    		
		}
   
    	return new Gson().toJson(map);
        
    }	

    
    @POST
	@Path("/get/all")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
    
    public String getAll() {
        
    	Map <String,Object> map = new HashMap<String,Object>();
    	
    	ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    	
    	String table_name = "inventory_category";
		String columns = "id, id_parent_category, name, name_table, id_tax";
		String query = "SELECT "+columns+" FROM "+table_name;
		
		
		Connection con = DbConnection.getConnection();
		
		try {
			
			PreparedStatement ps = con.prepareStatement(query);

			ResultSet rs =  ps.executeQuery();

			if (rs.first()){
				
				while (rs.next()){
					
					Map <String,Object> map_rs = new HashMap<String,Object>();
					
					map_rs.put("id", rs.getInt("id"));
					map_rs.put("id_parent_category", rs.getInt("id_parent_category"));
					map_rs.put("id_tax", rs.getInt("id_tax")); 
					map_rs.put("name", rs.getString("name"));
					map_rs.put("name_table", rs.getString("name_table"));
					
					list.add(map_rs);
					
				}
				
				map.put("data", list);
				map.put("response_code", 2000);
				map.put("response_message", "success: get inventory - id");
				
			} 			
			else {
				
				map.put("response_code", 4000);
				map.put("response_message", "failure: get inventory - all");
				
			}
				
		}
		catch (SQLException e){
			e.printStackTrace();
		}
		
		return new Gson().toJson(map);
        
    }	
    
    @POST
	@Path("/get/id")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
    
    public String getId(BeanInventoryCategory bean_inventory_category) {
    	
    	int identifier = bean_inventory_category.getId();
    	Map <String,Object> map = new HashMap<String,Object>();
    	    	
    	String table_name = "inventory_category";
		String columns = "id_parent_category, name, name_table, id_tax";
		String condition = "id = ?";
		String query = "SELECT "+columns+" FROM "+table_name+" WHERE "+condition;
		
		
		Connection con = DbConnection.getConnection();
		
		try {
			
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, identifier);

			ResultSet rs =  ps.executeQuery();
			
			if (rs.next()){
					
				Map <String,Object> map_rs = new HashMap<String,Object>();
				
				map_rs.put("id", identifier);
				map_rs.put("id_parent_category", rs.getInt("id_parent_category"));
				map_rs.put("id_tax", rs.getInt("id_tax")); 
				map_rs.put("name", rs.getString("name"));
				map_rs.put("name_table", rs.getString("name_table"));

				map.put("data", map_rs);
				map.put("response_code", 2000);
				map.put("response_message", "success: get inventory - id");
				
			}
			    			
			else {
				
				map.put("response_code", 4000);
				map.put("response_message", "failure: get inventory - id");
				
			}
		
				
		}
		catch (SQLException e){
			e.printStackTrace();
		}
		
		return new Gson().toJson(map);
        
    }	
    
    @POST
	@Path("/get/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
    
    public String getSearch(String identifier) {
        
        return null;
        
    }	


 }