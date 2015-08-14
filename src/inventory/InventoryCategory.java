

package inventory;

import java.io.StringWriter;
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

    public String deleteInventoryCategory(BeanInventoryCategory bean_inventory_category) {
        
        return null;
        
    }	

    
    @POST
	@Path("/get")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
    
    public String getInventory(Object identifier) {
    	
    	//convert Object identifier to map - amit
    	
    	System.out.println(identifier);
    	System.out.println(identifier.getClass().getName());
        return null;
        
    }	

    public Object recognizeIdentifier(Object identifier) {
        
        return null;
        
    }	

    public String all(String identifier) {
        
        return null;
        
    }	

    public String id(int identifier) {
        
        return null;
        
    }	

    public String search(String identifier) {
        
        return null;
        
    }	


 }