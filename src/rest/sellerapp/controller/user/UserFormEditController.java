package rest.sellerapp.controller.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import rest.sellerapp.bean.user.UserCreateFormBean;
import rest.sellerapp.controller.db.DbConnection;

@Path("/user/table_row_form")
public class UserFormEditController {

	@GET
	@Path("/id/{userId}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getUserFormData(@PathParam("userId") String userId)
	{
		Connection con = DbConnection.getConnection();
		
		Map<String,UserCreateFormBean> userFormDetailsMap = new HashMap <String,UserCreateFormBean>();
		UserCreateFormBean beanUser = new UserCreateFormBean();		
		try{						
			String columnsUserTable = "*";
			String tableUserTable = "user";
			String conditionUserTable = "where id=?";
			PreparedStatement psUserFormData = con.prepareStatement("select "+columnsUserTable+" from "+tableUserTable+" "+conditionUserTable);
			psUserFormData.setInt(1, Integer.parseInt(userId));
			ResultSet rsUserFormData = psUserFormData.executeQuery();
			
			rsUserFormData.next();			
			beanUser.setFirstName(rsUserFormData.getString("name_first"));
			beanUser.setLastName(rsUserFormData.getString("name_last"));
			beanUser.setPhoneNumber(rsUserFormData.getString("phone"));
			beanUser.setEmailId(rsUserFormData.getString("emailid"));						
			int addressId = rsUserFormData.getInt("id_address");
			int userCatId = rsUserFormData.getInt("id_user_category");
			
			String columnsAddressTable = "*";
			String tableAddressTable = "address";
			String conditionAddressTable = "where id=?";
			PreparedStatement psAddress = con.prepareStatement("select "+columnsAddressTable+" from "+tableAddressTable+" "+conditionAddressTable);
			psAddress.setInt(1, addressId);
			ResultSet rsUserAddress = psAddress.executeQuery();
			
			rsUserAddress.next();
			beanUser.setAddLineOne(rsUserAddress.getString("address_line_one"));
			beanUser.setAddLineTwo(rsUserAddress.getString("address_line_two"));
			beanUser.setCity(rsUserAddress.getString("city"));
			beanUser.setState(rsUserAddress.getString("state"));
			beanUser.setZip(rsUserAddress.getString("zip"));
			
			String columnsUserCatTable = "id,name";
			String tableUserCatTable = "user_category";
			String conditionUserCatTable = "where id=?";
			PreparedStatement psUserCat = con.prepareStatement("select "+columnsUserCatTable+" from "+tableUserCatTable+" "+conditionUserCatTable);
			psUserCat.setInt(1, userCatId);
			ResultSet rsUserCat = psUserCat.executeQuery();
			
			rsUserCat.next();
			beanUser.setUserCatId(rsUserCat.getInt("id"));
			beanUser.setUserCategory(rsUserCat.getString("name"));
			userFormDetailsMap.put("rowUserDetails", beanUser);
			
		}
		catch(Exception e){
		e.printStackTrace();	
		}
		finally{
			
		}
		return new Gson().toJson(userFormDetailsMap);
	}
}
