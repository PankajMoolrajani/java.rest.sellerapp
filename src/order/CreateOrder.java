package order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import db.DbConnection;

@Path ("/order")
public class CreateOrder {

	@POST
	@Path ("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	
	public String create_order_test(BeanCreateOrder orderBean){
		System.out.println("order create");
		Map<String, Object> map = new HashMap<String,Object>();
		
		map.put("id_marketplace", orderBean.getMarketplaceId());
		map.put("marketplace_order_id", orderBean.getMarketplaceOrderId());
		map.put("amount_taxable", orderBean.getAmountTaxable());
		map.put("amount_untaxable", orderBean.getAmountUntaxable());
		map.put("amount_tax", orderBean.getAmountTax());
		map.put("amount_total", orderBean.getAmountTotal());
		map.put("amount_shipping", orderBean.getAmountShipping());
		
		//connect to db
		Connection con = DbConnection.getConnection();
		String table_name = "main_order";
		String column_names = "id_marketplace, marketplace_order_id, amount_taxable, amount_untaxable, amount_tax, amount_total, amount_shipping";
		String query = "INSERT INTO "+table_name+"("+column_names+") VALUES (?, ?, ?, ?, ?, ?, ?)";
		System.out.println(query);
		
		try {
			PreparedStatement ps = con.prepareStatement(query);
			
			ps.setInt(1,orderBean.getMarketplaceId());
			ps.setString(2, orderBean.getMarketplaceOrderId());
			ps.setDouble(3, orderBean.getAmountTaxable());
			ps.setDouble(4, orderBean.getAmountUntaxable());
			ps.setDouble(5, orderBean.getAmountTax());
			ps.setDouble(6, orderBean.getAmountTotal());
			ps.setDouble(7, orderBean.getAmountShipping());
			
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
