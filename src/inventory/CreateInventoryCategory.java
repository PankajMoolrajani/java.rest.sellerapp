package inventory;


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

@Path("/inventory")
public  class CreateInventoryCategory  {

	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	
    public String createInventoryCategory(BeanInventoryCategory bean_inventory_category) {
        
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("id", bean_inventory_category.getId());
		map.put("id_parent_category", bean_inventory_category.getIdParentCategory());
		map.put("id_tax", bean_inventory_category.getIdTax());
		map.put("name", bean_inventory_category.getName());
		map.put("name_table", bean_inventory_category.getNameTable());
		
		Connection con = DbConnection.getConnection();
		String table_name = "inventory_category";
		String column_names = "id_parent_category, id_tax, name, name_table";
		String column_values = "?, ?, ?, ?";
		String query = "INSERT INTO "+table_name+"("+column_names+")"+" VALUES"+"("+column_values+")";
		
		try{
			
			PreparedStatement ps = con.prepareStatement(query);
			
			ps.setInt(1, bean_inventory_category.getIdParentCategory());
			ps.setInt(2, bean_inventory_category.getIdTax());
			ps.setString(3, bean_inventory_category.getName());
			ps.setString(4, bean_inventory_category.getNameTable());
			
			Boolean ps_result = ps.execute();
			System.out.println(ps_result);
			
			map.put("result", ps_result);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return new Gson().toJson(map);	
        
    }	


 }