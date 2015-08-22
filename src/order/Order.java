package order;

import inventory.BeanInventory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
		
		int id = 0, id_user = 0, id_marketplace, id_inventory_marketplace, id_inventory;

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
				ps.setInt(4, bean_order.getAmountTotalTaxable());
				ps.setInt(5, bean_order.getAmountTotalUntaxable());
				ps.setInt(6, bean_order.getAmountTotalTax());
				ps.setInt(7, bean_order.getAmountTotalShipping());
				ps.setInt(8, bean_order.getAmountTotal());
				
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
		System.out.println("Print array list");
		
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
					ps.setInt(8,  ol.getQuantity());
					
					
					int rows_affected =  ps.executeUpdate();
					
					if (rows_affected != 0){
						
						ResultSet rs = ps.getGeneratedKeys();
						
						if (rs.next()){
						
							list.add(rs.getInt(1));
							
						}   
						con.commit();
					}
						
				}
				catch (SQLException e){
					e.printStackTrace();
				}
				
			}
			else {
				System.out.println("else part of checkExistenceInventoryMarketplace");
				return null;
			}
			System.out.println("id_order: "+ol.id_order);
			System.out.println("id_inventory_marketplace: "+ol.id_inventory_marketplace);
			System.out.println("id_inventory_");
			System.out.println("quantity: "+ol.quantity);
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
}
