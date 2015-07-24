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
import rest.sellerapp.controller.db.DbUtils;

@Path("/user/get_form_data")
public class UserFormEditController {

	@GET
	@Path("/user_id/{userId}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getUserFormData(@PathParam("userId") String userId)
	{
		Connection con = DbConnection.getConnection();
		
		Map<String,UserCreateFormBean> userFormDetailsMap = new HashMap <String,UserCreateFormBean>();
		UserCreateFormBean beanUser = new UserCreateFormBean();		
		
		PreparedStatement psUserFormData = null;
		ResultSet rsUserFormData = null;
		
		PreparedStatement psUserAddress = null;
		ResultSet rsUserAddress = null;
		
		PreparedStatement psUserCat = null;
		ResultSet rsUserCat = null;
		try{						
			String columnsUserTable = "*";
			String tableUserTable = "user";
			String conditionUserTable = "where id=?";
			psUserFormData = con.prepareStatement("select "+columnsUserTable+" from "+tableUserTable+" "+conditionUserTable);
			psUserFormData.setInt(1, Integer.parseInt(userId));

			rsUserFormData = psUserFormData.executeQuery();
			
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
			psUserAddress = con.prepareStatement("select "+columnsAddressTable+" from "+tableAddressTable+" "+conditionAddressTable);
			psUserAddress.setInt(1, addressId);
			rsUserAddress = psUserAddress.executeQuery();
			
			rsUserAddress.next();
			beanUser.setAddLineOne(rsUserAddress.getString("address_line_one"));
			beanUser.setAddLineTwo(rsUserAddress.getString("address_line_two"));
			beanUser.setCity(rsUserAddress.getString("city"));
			beanUser.setState(rsUserAddress.getString("state"));
			beanUser.setZip(rsUserAddress.getString("zip"));
			
			String columnsUserCatTable = "id,name";
			String tableUserCatTable = "user_category";
			String conditionUserCatTable = "where id=?";
			psUserCat = con.prepareStatement("select "+columnsUserCatTable+" from "+tableUserCatTable+" "+conditionUserCatTable);
			psUserCat.setInt(1, userCatId);
			rsUserCat = psUserCat.executeQuery();
			
			rsUserCat.next();
			beanUser.setUserCatId(rsUserCat.getInt("id"));
			beanUser.setUserCategory(rsUserCat.getString("name"));
			userFormDetailsMap.put("rowUserDetails", beanUser);
			
		}
		catch(Exception e){
		e.printStackTrace();	
		}
		finally{
			DbUtils.closeUtil(rsUserCat);
			DbUtils.closeUtil(psUserCat);			
			DbUtils.closeUtil(rsUserAddress);
			DbUtils.closeUtil(psUserAddress);
			DbUtils.closeUtil(rsUserFormData);
			DbUtils.closeUtil(psUserFormData);
			DbUtils.closeUtil(con);
		}
		return new Gson().toJson(userFormDetailsMap);
	}
}
