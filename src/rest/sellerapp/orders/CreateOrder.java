package rest.sellerapp.orders;

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

import rest.sellerapp.controller.db.DbConnection;

import com.google.gson.Gson;


@Path ("/order")
public class CreateOrder {

	@POST
	@Path ("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	
	public String create_order_test(BeanCreateOrder bean_create){
		System.out.println("order create");
		Map<String, Object> map = new HashMap<String,Object>();
		
		map.put("id_marketplace", bean_create.getIdMarketplace());
		map.put("marketplace_order_id", bean_create.getMarketplaceOrderId());
		map.put("amount_taxable", bean_create.getAmountTaxable());
		map.put("amount_untaxable", bean_create.getAmountUntaxable());
		map.put("amount_tax", bean_create.getAmountTax());
		map.put("amount_total", bean_create.getAmountTotal());
		map.put("amount_shipping", bean_create.getAmountShipping());
		
		//connect to db
		Connection con = DbConnection.getConnection();
		String table_name = "main_order";
		String column_names = "id_marketplace, marketplace_order_id, amount_taxable, amount_untaxable, amount_tax, amount_total, amount_shipping";
		String query = "INSERT INTO "+table_name+"("+column_names+") VALUES (?, ?, ?, ?, ?, ?, ?)";
		System.out.println(query);
		
		try {
			PreparedStatement ps = con.prepareStatement(query);
			
			ps.setInt(1,bean_create.getIdMarketplace());
			ps.setString(2, bean_create.getMarketplaceOrderId());
			ps.setDouble(3, bean_create.getAmountTaxable());
			ps.setDouble(4, bean_create.getAmountUntaxable());
			ps.setDouble(5, bean_create.getAmountTax());
			ps.setDouble(6, bean_create.getAmountTotal());
			ps.setDouble(7, bean_create.getAmountShipping());
			
			Boolean ps_result = ps.execute();
			System.out.println(ps_result);
			
			map.put("result", ps_result);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//insert data to db
		//get id of ineserted row
		//return the new id in a map object json
		
		
		return new Gson().toJson(map);	
		
		
	}
}
