package user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import db.DbConnection;
import db.DbUtils;

@Path("/user/get-user")
public class GetUser 
{
	@Context private HttpServletRequest request;
	
	@GET
	@Path("/all")
	@Produces(MediaType.TEXT_PLAIN)
	public String getAllUser()
	{
		HttpSession session = request.getSession(false);
		System.out.println(session.getId());
		
		Connection con = DbConnection.getConnection();
		PreparedStatement psUserTable = null;
		ResultSet rsUserTable = null;
		List<BeanUser> userList = new ArrayList<BeanUser>();
		Map<String,List<BeanUser>> userMap = new HashMap<String,List<BeanUser>>();		
		try
		{
			String tableUser = "user";
			String columnsUser = "id,name_user,emailid,phone";
			//String condition_user = " where map_url=?";	
			psUserTable = con.prepareStatement("select "+columnsUser+" from "+tableUser);
			rsUserTable= psUserTable.executeQuery();			
			
			while(rsUserTable.next())
			{												
				userList.add(new BeanUser(rsUserTable.getInt(1),rsUserTable.getString(2),rsUserTable.getString(3),rsUserTable.getString(4)));																																												
			}
			userMap.put("userTable", userList);														
		}
		catch(Exception e)
		{				
			e.printStackTrace();
		}
		finally
		{
			DbUtils.closeUtil(rsUserTable);
			DbUtils.closeUtil(psUserTable);
			DbUtils.closeUtil(con);						
		}
		return new Gson().toJson(userMap);
	}
	
	@GET
	@Path("/search-text/{textChars}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getSearchUser( @PathParam("textChars") String textChar)
	{				
		Connection con = DbConnection.getConnection();
		PreparedStatement psUserTableSearch = null;
		ResultSet rsUserTableSearch = null;
		List<BeanUser> userList = new ArrayList<BeanUser>();
		Map<String,List<BeanUser>> userMap = new HashMap<String,List<BeanUser>>();
		
		try
		{
			String tableUser = "user";
			String columnsUserSearch = "id,name_user,emailid,phone";
			String conditionUserSearch = "WHERE name_user LIKE ?";				
			psUserTableSearch = con.prepareStatement("select "+columnsUserSearch+" from "+tableUser+" "+conditionUserSearch);
			psUserTableSearch.setString(1, textChar+"%");
			rsUserTableSearch= psUserTableSearch.executeQuery();										
			while(rsUserTableSearch.next())
			{						
				if(rsUserTableSearch.getString(2)!=null && rsUserTableSearch.getString(2).startsWith(textChar))
				{							
					userList.add(new BeanUser(rsUserTableSearch.getInt(1),rsUserTableSearch.getString(2),rsUserTableSearch.getString(3),rsUserTableSearch.getString(4)));																			
				}													
			}						
			userMap.put("userTable", userList);
			new Gson().toJson(userMap);				
		}
		catch(Exception e)
		{				
			e.printStackTrace();
		}
		finally
		{
			DbUtils.closeUtil(rsUserTableSearch);
			DbUtils.closeUtil(psUserTableSearch);
			DbUtils.closeUtil(con);						
		}
		return new Gson().toJson(userMap);		
	}
	
	@GET
	@Path("/user-id/{userId}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getSpecificUser(@PathParam("userId") String userId)
	{
		Connection con = DbConnection.getConnection();
		
		Map<String,BeanUser> userFormDetailsMap = new HashMap <String,BeanUser>();
		BeanUser beanUser = new BeanUser();		
		
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
			userFormDetailsMap.put("userDetails", beanUser);
			
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