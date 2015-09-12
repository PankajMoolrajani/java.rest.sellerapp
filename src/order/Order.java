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
import java.util.Map;

import javax.ws.rs.Consumes;
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
	
	
	@POST
	@Path ("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	
	public String update(BeanOrder bean_order){
		System.out.println("Order Update !");
		String [] columns = { "id_user", "id_marketplace", "marketplace_orderid", "amount_total_taxable", "amount_total_untaxable", "amount_total_tax", "amount_total_shipping", "amount_total" };
		Map <String, Object> map = this.getUpdateMap(bean_order, columns);
		
		for (Map.Entry<String, Object> e : map.entrySet()){
			System.out.println(e.getKey()+" - "+e.getValue());
		}

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
			//System.out.println(key + ": " + map.get(e.getKey()));
			
			//String [] columns_order_line = { "id_order", "id_inventory_marketplace", "marketplace_suborderid", "amount_taxable", "amount_untaxable", "amount_tax", "amount_shipping", "quantity" };
			if (Arrays.asList(columns).contains(key)){
				

				map_update.put(key, map.get(e.getKey()));
				//System.out.println(key+": "+map.get(e.getKey()));
			}
			
			//change uppercase into lowercase with prefix of _
		}
		
//		for (Map.Entry<String, Object> e : map.entrySet()){
//			System.out.println(e.getKey()+" - "+e.getValue());
//		}
		
		return map_update;

	}
	
	
}
