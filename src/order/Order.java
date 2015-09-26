package order;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import db.DbConnection;
import order.BeanOrderLine;
import order.BeanOrder;
@Path ("/order")
public class Order {
	
	
	@POST
	@Path ("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	
	public String create(BeanOrder bean_order){
		
		Connection con = DbConnection.getConnection();
		try {
			con.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Map <String,Object> map = new HashMap<String,Object>();
		
		int id = 0;
		
		if (checkExistenceOrder(bean_order) == false){
			
			id = this.createOrder(con, bean_order);
			
			if (id != 0){
				
				ArrayList<Integer> list = this.createOrderLine(con, id, bean_order.getBeanOrderLine());
				if (list != null){
					try {
						con.commit();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					map.put("id", id);
					
					map.put("response_code", 2000);
					map.put("response_message", "success: create order");
				}
				else {
					
					map.put("id", 0);
					
					map.put("response_code", 4000);
					map.put("response_message", "failure: create order line");
					
					return new Gson().toJson(map);
				}
				
				
				return new Gson().toJson(map);
				
			}
			else {
				
				map.put("id", 0);
				
				map.put("response_code", 4000);
				map.put("response_message", "failure: create order - id is 0");
				
				return new Gson().toJson(map);
				
			}
			
		}
		else {
			
			map.put("response_code", 4000);
			map.put("response_message", "order already exists");
			
			return new Gson().toJson(map);
		}
	}
	
	int createOrder(Connection con, BeanOrder bean_order){
		
		int id = 0;

		if (this.checkExistenceUser(bean_order.getIdUser())
		 && this.checkExistenceMarketplace(bean_order.getIdMarketplace())) {

			String table_name = "orders";
			String columns = "id_user, id_marketplace, marketplace_orderid, amount_total_taxable, amount_total_untaxable, amount_total_tax, amount_total_shipping, amount_total";
			String values = "?, ?, ?, ?, ?, ?, ?, ?";
			String query = "INSERT INTO "+table_name+"("+columns+")"+" VALUES "+"("+values+")";
			
			
			try {
				
				PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, bean_order.getIdUser());
				ps.setInt(2, bean_order.getIdMarketplace());
				ps.setString(3, bean_order.getMarketplaceOrderid());
				ps.setDouble(4, bean_order.getAmountTotalTaxable());
				ps.setDouble(5, bean_order.getAmountTotalUntaxable());
				ps.setDouble(6, bean_order.getAmountTotalTax());
				ps.setDouble(7, bean_order.getAmountTotalShipping());
				ps.setDouble(8, bean_order.getAmountTotal());
				
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
	
	ArrayList<Integer> createOrderLine(Connection con, int id_order, ArrayList<BeanOrderLine> list_bean_order_line){
		
		ArrayList<Integer> list = new ArrayList<Integer>();
		
		for (BeanOrderLine ol: list_bean_order_line){
			if (this.checkExistenceInventoryMarketplace((ol.id_inventory_marketplace))){

				String table_name = "order_line";
				String columns = "id_order, id_inventory_marketplace, marketplace_suborderid, amount_taxable, amount_untaxable, amount_tax, amount_shipping, quantity";
				String values = "?, ?, ?, ?, ?, ?, ?, ?";
				String query = "INSERT INTO "+table_name+"("+columns+")"+" VALUES "+"("+values+")";
								
				try {
					
					PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
					ps.setInt(1, id_order);
					ps.setInt(2, ol.id_inventory_marketplace);
					ps.setString(3, ol.marketplace_suborderid);
					ps.setDouble(4, ol.amount_taxable);
					ps.setDouble(5, ol.amount_untaxable);
					ps.setDouble(6, ol.amount_tax);
					ps.setDouble(7, ol.amount_shipping);
					ps.setInt(8,  ol.quantity);
					
					int rows_affected =  ps.executeUpdate();
					
					if (rows_affected != 0){
						
						ResultSet rs = ps.getGeneratedKeys();
						
						if (rs.next()){
						
							list.add(rs.getInt(1));
							
						}   
												
					}
						
				}
				catch (SQLException e){
					e.printStackTrace();
				}
				
			}
			else {
				return null;
			}
		}
		
		return list;
		
	}
	
	
	Boolean checkExistenceOrder(BeanOrder bean_order){
		
		String table_name = "orders";
		String column = "id";
		String condition = "marketplace_orderid=?";
		String query = "SELECT "+column+" FROM "+table_name+" WHERE "+condition;
		
		Connection con = DbConnection.getConnection();
		
		try {
			
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, bean_order.getMarketplaceOrderid());
			
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
	
	
	Boolean checkExistenceUser(int id){
		
		String table_name = "user";
		String column = "id";
		String condition = "id=?";
		String query = "SELECT "+column+" FROM "+table_name+" WHERE "+condition;
		
		Connection con = DbConnection.getConnection();
		
		try {
			
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, id);
			
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
	
	
	Boolean checkExistenceMarketplace(int id){
		
		String table_name = "marketplace";
		String column = "id";
		String condition = "id=?";
		String query = "SELECT "+column+" FROM "+table_name+" WHERE "+condition;
		
		Connection con = DbConnection.getConnection();
		
		try {
			
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, id);
			
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


	Boolean checkExistenceInventoryMarketplace(int id){
	
		String table_name = "inventory_marketplace";
		String column = "id";
		String condition = "id=?";
		String query = "SELECT "+column+" FROM "+table_name+" WHERE "+condition;
		
		Connection con = DbConnection.getConnection();
		
		try {
			
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, id);
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
	
	
	@GET
	@Path("/get/all")
	@Produces(MediaType.TEXT_PLAIN)
    
    public String getAll() {

    	Map <String,Object> map = new HashMap<String,Object>();
    	
    	ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    	
    	String table_name = "orders";
		String columns = "id, id_marketplace, marketplace_orderid, amount_total_taxable, amount_total_untaxable, amount_total_tax, amount_total_shipping, amount_total, id_user";
		String query = "SELECT "+columns+" FROM "+table_name;
		
		
		Connection con = DbConnection.getConnection();
		
		try {
			
			PreparedStatement ps = con.prepareStatement(query);

			ResultSet rs =  ps.executeQuery();

			if (rs.first()){
				
				while (rs.next()){
					
					Map <String,Object> map_rs = new HashMap<String,Object>();
					
					map_rs.put("id", rs.getInt("id"));
					map_rs.put("id_marketplace", rs.getInt("id_marketplace"));
					map_rs.put("marketplace_orderid", rs.getString("marketplace_orderid"));
					map_rs.put("amount_total_taxable", rs.getDouble("amount_total_taxable"));
					map_rs.put("amount_total_untaxable", rs.getDouble("amount_total_untaxable"));
					map_rs.put("amount_total_tax", rs.getDouble("amount_total_tax"));
					map_rs.put("amount_total_shipping", rs.getDouble("amount_total_shipping"));
					map_rs.put("amount_total", rs.getDouble("amount_total"));
					map_rs.put("id_user", rs.getInt("id_user"));
					
					list.add(map_rs);
					
				}
				
				map.put("data", list);
				map.put("response_code", 2000);
				map.put("response_message", "success: get order - all");
				
			} 			
			else {
				
				map.put("response_code", 4000);
				map.put("response_message", "failure: get order - all");
				
			}
				
		}
		catch (SQLException e){
			e.printStackTrace();
		}
		
		return new Gson().toJson(map);
        
    }	
	
	@POST
	@Path("/get/id")
	@Produces(MediaType.TEXT_PLAIN)
    
    public String getAll(BeanOrder bean_order) {
    	
		Map <String,Object> map = new HashMap<String,Object>();
    	ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    	
    	String table_name = "orders";
		String columns = "id, id_marketplace, marketplace_orderid, amount_total_taxable, amount_total_untaxable, amount_total_tax, amount_total_shipping, amount_total, id_user";
		String condition = "id=?";
		String query = "SELECT "+columns+" FROM "+table_name + " WHERE " + condition;
		
		try {
			
			Connection con = DbConnection.getConnection();
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, bean_order.getId());

			ResultSet rs =  ps.executeQuery();
			if (rs.next()){

				Map <String,Object> map_rs = new HashMap<String,Object>();
				
				map_rs.put("id", rs.getInt("id"));
				map_rs.put("id_marketplace", rs.getInt("id_marketplace"));
				map_rs.put("marketplace_orderid", rs.getString("marketplace_orderid"));
				map_rs.put("amount_total_taxable", rs.getDouble("amount_total_taxable"));
				map_rs.put("amount_total_untaxable", rs.getDouble("amount_total_untaxable"));
				map_rs.put("amount_total_tax", rs.getDouble("amount_total_tax"));
				map_rs.put("amount_total_shipping", rs.getDouble("amount_total_shipping"));
				map_rs.put("amount_total", rs.getDouble("amount_total"));
				map_rs.put("id_user", rs.getInt("id_user"));
				
				list.add(map_rs);
				
				map.put("data", list);
				map.put("response_code", 2000);
				map.put("response_message", "success: get order - id");
				
			} 			
			else {
				
				map.put("response_code", 4000);
				map.put("response_message", "failure: get order - id");
				
			}
				
		}
		catch (SQLException e){
			e.printStackTrace();
		}
		
		return new Gson().toJson(map);
        
    }	
	
	
	@POST
	@Path ("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	
	public String update(BeanOrder bean_order){
		
		Map <String,Object> map = new HashMap<String,Object>();
		
		Connection con = DbConnection.getConnection();
		PreparedStatement ps;
		try {
			con.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//update - table order line
		String [] columns_order_line = {"id_order","id_inventory_marketplace","quantity","amount_taxable","amount_untaxable","amount_tax","amount_shipping","marketplace_suborderid"};		
		Map<Integer, Map<String,Object>> map_order_line = new HashMap<Integer, Map<String,Object>>();
		ArrayList<BeanOrderLine> list = bean_order.getBeanOrderLine();
		Iterator<BeanOrderLine> iterator = list.iterator();
		while(iterator.hasNext()){			
			BeanOrderLine bean_order_line = iterator.next();
			Map<String,Object> map_update = this.getUpdateMap(bean_order_line, columns_order_line);
			map_order_line.put(bean_order_line.getId(), map_update);
		}
		
		for (int key_id: map_order_line.keySet()){
						
			String table_name = "order_line";
			String column = "";
			String condition = "WHERE id=?";
			
			int count = 1;
			for (String key_column_name: map_order_line.get(key_id).keySet()){
				if (count == map_order_line.get(key_id).size()){
					column = column + key_column_name+"="+map_order_line.get(key_id).get(key_column_name)+" ";
				}
				else{
					column = column + key_column_name+"="+map_order_line.get(key_id).get(key_column_name)+", ";
				}
				count++;
			}
			
			String query = "UPDATE " + table_name + " SET " + column + condition;

			try {
				ps = con.prepareStatement(query);
				ps.setInt(1, key_id);
				ps.execute();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				map.put("response_code", 4000);
				map.put("response_message", "failure: update order - order_line");
				return new Gson().toJson(map);
			}
			
		}
				
		//update - table order
		String [] columns_order = { "id_user", "id_marketplace", "marketplace_orderid", "amount_total_taxable", "amount_total_untaxable", "amount_total_tax", "amount_total_shipping", "amount_total" };
		Map <String, Object> map_orders = this.getUpdateMap(bean_order, columns_order);
		
		String table_name = "orders";
		String column = "";
		String condition = "WHERE id=?";
		
		int count = 1;
		for (String key: map_orders.keySet()){
			if (count == map_orders.size()){				
				if (map_orders.get(key) instanceof String){
					column = column + key + "='" + map_orders.get(key) + "' ";
				}
				else {
					column = column + key + "=" + map_orders.get(key) + " ";
				}
			}
			else{
				if (map_orders.get(key) instanceof String){
					column = column + key + "='" + map_orders.get(key) + "', ";
				}
				else {
					column = column + key + "=" + map_orders.get(key) + ", ";
				}
			}
			count++;
		}
		
		String query = "UPDATE " + table_name + " SET " + column + condition;
		
		try {
			ps = con.prepareStatement(query);
			ps.setInt(1, bean_order.getId());
			ps.execute();
			con.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("response_code", 4000);
			map.put("response_message", "failure: update order - table orders");
			return new Gson().toJson(map);
		}
		
		map.put("response_code", 2000);
		map.put("response_message", "success: update order");
		return new Gson().toJson(map);
	}
	
	
	public Map<String, Object> getUpdateMap(Object bean, String [] columns){
		Map<String,Object> map = new HashMap<String,Object>();
		Map <String, Object> map_update = new HashMap <String, Object>();

		BeanInfo bean_info = null;
		try{
			bean_info = Introspector.getBeanInfo(bean.getClass());
			for (PropertyDescriptor pd : bean_info.getPropertyDescriptors()) {
				Method reader = pd.getReadMethod();			
			    if (reader != null){
			    	map.put(pd.getName(),reader.invoke(bean));
			    }
			}
		}catch(IllegalArgumentException e){
			e.printStackTrace();
		}catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IntrospectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (Map.Entry<String, Object> e : map.entrySet()){
			
			String key = e.getKey();
			
			for (int i=0; i < key.length(); i++){
				if (Character.isUpperCase(key.charAt(i))) {
					key = key.replace(Character.toString(key.charAt(i)), "_"+Character.toLowerCase(key.charAt(i)));
				}
			}
	
			if (Arrays.asList(columns).contains(key)){
				map_update.put(key, map.get(e.getKey()));
			}
			
		}
		return map_update;
	}
	
	
}
