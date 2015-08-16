

package inventory;

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
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

import db.DbConnection;


@Path("/inventory")
public  class Inventory  {

	
	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	
    public String create(BeanInventory bean_inventory) {
        
		Map <String,Object> map = new HashMap<String,Object>();
		
		int id = 0;
		
		if (checkExistenceInventory(bean_inventory) == false){
			
			id = this.createInventory(bean_inventory);
			
			if (id != 0){
	
				map.put("id", id);
				
				map.put("response_code", 2000);
				map.put("response_message", "success: create inventory");
				
				return new Gson().toJson(map);
				
			}
			else {
				
				map.put("id", 0);
				
				map.put("response_code", 4000);
				map.put("response_message", "failure: create inventory");
				
				return new Gson().toJson(map);
				
			}
			
		}
		else {
			
			map.put("response_code", 4000);
			map.put("response_message", "inventory already exists");
			
			return new Gson().toJson(map);
		}
        
    }	
	
	int createInventory(BeanInventory bean_inventory){
		
		int id = 0, id_stock = 0, id_price = 0, id_category = 0;
		
		id_stock = this.createStock(bean_inventory);

		id_price = this.createPrice(bean_inventory);
		
		Map<String, Object> map_check_existence_category = new HashMap<String, Object>();
		map_check_existence_category = this.checkExistenceCategory(bean_inventory.getIdCategory());
		
		if ((Boolean) map_check_existence_category.get("boolean")){
			
			id_category = (Integer) map_check_existence_category.get("id_category");
			
			String table_name = "inventory";
			String columns = "id_category, id_stock, id_price, sku, sku_replica, name, status_listing";
			String values = "?, ?, ?, ?, ?, ?, ?";
			String query = "INSERT INTO "+table_name+"("+columns+")"+" VALUES "+"("+values+")";
			
			Connection con = DbConnection.getConnection();
			
			try {
				
				PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, id_category);
				ps.setInt(2, id_stock);
				ps.setInt(3, id_price);
				ps.setString(4, bean_inventory.getSku());
				ps.setString(5, bean_inventory.getSkuReplica());
				ps.setString(6, bean_inventory.getName());
				ps.setString(7, bean_inventory.getStatusListing());
			
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
			
		}
		
		return id; 
	}
	
	
	Boolean checkExistenceInventory(BeanInventory bean_inventory){
		
		String table_name = "inventory";
		String column = "id";
		String condition = "name=? OR sku=?";
		String query = "SELECT "+column+" FROM "+table_name+" WHERE "+condition;
		
		Connection con = DbConnection.getConnection();
		
		try {
			
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, bean_inventory.getName());
			ps.setString(2, bean_inventory.getSku());
			
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
	
	
	int createStock(BeanInventory bean_inventory){
		
		int id_stock = 0;
		
		int available = bean_inventory.getAvailable();
		int outgoing = bean_inventory.getOutgoing();
		int incoming = bean_inventory.getIncoming();
		
		Map<String, Object> map_check_existence_stock = new HashMap<String, Object>();
		
		map_check_existence_stock = this.checkExistenceStock(available, outgoing, incoming);
		
		if ((Boolean) map_check_existence_stock.get("boolean")){
			
			id_stock = (Integer) map_check_existence_stock.get("id_stock");
			
		}
		else {
			
			String table_name = "stock";
			String columns = "available, outgoing, incoming";
			String values = "?, ?, ?";
			String query = "INSERT INTO "+table_name+"("+columns+")"+" VALUES "+"("+values+")";
			
			Connection con = DbConnection.getConnection();
			
			try {
				
				PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, available);
				ps.setInt(2, outgoing);
				ps.setInt(3, incoming);
				
				
				int rows_affected =  ps.executeUpdate();
				
				if (rows_affected != 0){
					
					ResultSet rs = ps.getGeneratedKeys();
					
					if (rs.next()){
					
						id_stock = rs.getInt(1);
						
					}    			
				}
					
			}
			catch (SQLException e){
				e.printStackTrace();
			}
		}
		
		return id_stock;
	}


	Map<String, Object> checkExistenceStock(int available, int outgoing, int incoming){
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		String table_name = "stock";
		String column = "id";
		String condition = "available=? and outgoing=? and incoming=?";
		String query = "SELECT "+column+" FROM "+table_name+" WHERE "+condition;
		
		Connection con = DbConnection.getConnection();
		
		try {
			
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, available);
			ps.setInt(2, outgoing);
			ps.setInt(3, incoming);

			ResultSet rs = ps.executeQuery();
			
			if (rs.next()){
				
				map.put("boolean", true);
				map.put("id_stock", rs.getInt("id"));
				
			}
			else {
				
				map.put("boolean", false);
				map.put("id_stock", 0);
				
			}
		} 
		catch (SQLException e){
			e.printStackTrace();
		}

		return map;
	
	}
	
	
	int createPrice(BeanInventory bean_inventory){
		
		int id_price = 0;
		
		int price_sell = bean_inventory.getPriceSell();
		int price_mrp = bean_inventory.getPriceMrp();
		
		Map<String, Object> map_check_existence_price = new HashMap<String, Object>();
		
		map_check_existence_price = this.checkExistencePrice(price_sell, price_mrp);
		
		if ((Boolean) map_check_existence_price.get("boolean")){
			
			id_price = (Integer) map_check_existence_price.get("id_price");
			
		}
		else {
			
			String table_name = "price";
			String columns = "price_sell, price_mrp";
			String values = "?, ?";
			String query = "INSERT INTO "+table_name+"("+columns+")"+" VALUES "+"("+values+")";
			
			Connection con = DbConnection.getConnection();
			
			try {
				
				PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, price_sell);
				ps.setInt(2, price_mrp);
				
				
				int rows_affected =  ps.executeUpdate();
				
				if (rows_affected != 0){
					
					ResultSet rs = ps.getGeneratedKeys();
					
					if (rs.next()){
					
						id_price = rs.getInt(1);
						
					}    			
				}
					
			}
			catch (SQLException e){
				e.printStackTrace();
			}
		}
		
		return id_price;
	}


	Map<String, Object> checkExistencePrice(int price_sell, int price_mrp){
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		String table_name = "price";
		String column = "id";
		String condition = "price_sell=? and price_mrp=?";
		String query = "SELECT "+column+" FROM "+table_name+" WHERE "+condition;
		
		Connection con = DbConnection.getConnection();
		
		try {
			
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, price_sell);
			ps.setInt(2, price_mrp);

			ResultSet rs = ps.executeQuery();
			
			if (rs.next()){
				
				map.put("boolean", true);
				map.put("id_price", rs.getInt("id"));
				
			}
			else {
				
				map.put("boolean", false);
				map.put("id_price", 0);
				
			}
		} 
		catch (SQLException e){
			e.printStackTrace();
		}

		return map;
		
	}
	
	
	Map<String, Object> checkExistenceCategory(int id_category){
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		String table_name = "inventory_category";
		String column = "id";
		String condition = "id=?";
		String query = "SELECT "+column+" FROM "+table_name+" WHERE "+condition;
		
		Connection con = DbConnection.getConnection();
		
		try {
			
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, id_category);

			ResultSet rs = ps.executeQuery();
			
			if (rs.next()){
				
				map.put("boolean", true);
				map.put("id_category", rs.getInt("id"));
				
			}
			else {
				
				map.put("boolean", false);
				map.put("id_category", 0);
				
			}
		} 
		catch (SQLException e){
			e.printStackTrace();
		}

		return map;
		
	}
	
	
    public String updateInventory(BeanInventory bean_inventory) {
        
        return null;
        
    }	

    
    @POST
	@Path("/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
    
    public String deleteInventory(BeanInventory bean_inventory) {
        
    	Map <String,Object> map = new HashMap<String,Object>();
    	
    	String table_name = "inventory";
    	String condition = "id=?";
    	String query = "DELETE FROM "+table_name+" WHERE "+condition;
    	
    	Connection con = db.DbConnection.getConnection();
    	
    	try {
    		
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, bean_inventory.getId());
			
			if (ps.executeUpdate() == 1){
				map.put("response_code", 2000);
    			map.put("response_message", "success: inventory delete");
			}
			else {
				map.put("response_code", 4000);
    			map.put("response_message", "failure: inventory delete");
			}
			
		}
    	catch (Exception e) {
    		
    		if (e instanceof MySQLIntegrityConstraintViolationException){
    			map.put("response_code", 4000);
    			map.put("response_message", "failure: inventory delete - constraint integrity exception");
    		}
    		
		}
   
    	return new Gson().toJson(map);
        
    }	

    public String getInventory(Object identifier) {
        
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