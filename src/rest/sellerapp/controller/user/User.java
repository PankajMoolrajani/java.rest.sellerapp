package rest.sellerapp.controller.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import rest.sellerapp.bean.user.UserTableBean;
import rest.sellerapp.controller.db.DbConnection;
import rest.sellerapp.controller.db.DbUtils;

import com.google.gson.Gson;

@Path("/user/table_data")
public class User 
{
	@GET
	@Path("/all")
	@Produces(MediaType.TEXT_PLAIN)
	public String getUserTable()
	{
		Connection con = DbConnection.getConnection();
		PreparedStatement psUserTable = null;
		ResultSet rsUserTable = null;
		List<UserTableBean> userList = new ArrayList<UserTableBean>();
		Map<String,List<UserTableBean>> userMap = new HashMap<String,List<UserTableBean>>();
		try
		{
			String tableUser = "user";
			String columnsUser = "id,name_user,emailid,phone";
			//String condition_user = " where map_url=?";	
			psUserTable = con.prepareStatement("select "+columnsUser+" from "+tableUser);
			rsUserTable= psUserTable.executeQuery();			
			
			while(rsUserTable.next())
			{												
				userList.add(new UserTableBean(rsUserTable.getInt(1),rsUserTable.getString(2),rsUserTable.getString(3),rsUserTable.getString(4)));																																												
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
	@Path("/search")
	@Produces(MediaType.TEXT_PLAIN)
	public String getUserTableSearch( @QueryParam("textChar") String textChar)
	{				
		Connection con = DbConnection.getConnection();
		PreparedStatement psUserTableSearch = null;
		ResultSet rsUserTableSearch = null;
		List<UserTableBean> userList = new ArrayList<UserTableBean>();
		Map<String,List<UserTableBean>> userMap = new HashMap<String,List<UserTableBean>>();
		
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
					userList.add(new UserTableBean(rsUserTableSearch.getInt(1),rsUserTableSearch.getString(2),rsUserTableSearch.getString(3),rsUserTableSearch.getString(4)));																			
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
	
}