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

import rest.sellerapp.bean.user.UserSearchParamBean;
import rest.sellerapp.bean.user.UserTableBean;
import rest.sellerapp.controller.db.DbConnection;
import rest.sellerapp.controller.db.DbUtils;

import com.google.gson.Gson;

@Path("/user")
public class User 
{
	@GET
	@Path("/table_data/all/{screen_size}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getUserTable(@PathParam("screen_size") String screenType)
	{
		Connection con = DbConnection.getConnection();
		PreparedStatement psUserTable = null;
		ResultSet rsUserTable = null;
		List<UserTableBean> userList = new ArrayList<UserTableBean>();
		Map<String,List<UserTableBean>> userMap = new HashMap<String,List<UserTableBean>>();
		try
		{
			String table_user = "user";
			String columns_user = "id,name_user,emailid,phone";
			//String condition_user = " where map_url=?";	
			psUserTable = con.prepareStatement("select "+columns_user+" from "+table_user);
			rsUserTable= psUserTable.executeQuery();			
			if(screenType.equals("large"))
			{								
				//System.out.println("2");
				while(rsUserTable.next())
				{												
					userList.add(new UserTableBean(rsUserTable.getInt(1),rsUserTable.getString(2),rsUserTable.getString(3),rsUserTable.getString(4)));																																												
				}
				userMap.put("userTable", userList);				
			}
			else if(screenType.equals("small"))
			{
				while(rsUserTable.next())
				{												
					userList.add(new UserTableBean(rsUserTable.getInt(1),rsUserTable.getString(2),rsUserTable.getString(3),rsUserTable.getString(4)));																																												
				}
				userMap.put("userTable", userList);				
			}								
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
	@Path("/table_data/search")
	@Produces(MediaType.TEXT_PLAIN)
	public String getUserTableSearch(/*UserSearchParamBean paramBean*/ @QueryParam("textChar") String textChar, @QueryParam("screenType") String screenType)
	{
		return textChar+" "+screenType;
//		String textChar = paramBean.getSearchString();
//		String screenType = paramBean.getSearchString();
//		
//		Connection con = DbConnection.getConnection();
//		PreparedStatement psUserTableSearch = null;
//		ResultSet rsUserTableSearch = null;
//		List<UserTableBean> userList = new ArrayList<UserTableBean>();
//		Map<String,List<UserTableBean>> userMap = new HashMap<String,List<UserTableBean>>();
//		
//		try
//		{
//			if(screenType.equals("large"))
//			{			
//				while(rsUserTableSearch.next())
//				{						
//					if(rsUserTableSearch.getString(2)!=null && rsUserTableSearch.getString(2).startsWith(textChar))
//					{							
//						userList.add(new UserTableBean(rsUserTableSearch.getInt(1),rsUserTableSearch.getString(2),rsUserTableSearch.getString(3),rsUserTableSearch.getString(4)));																			
//					}													
//				}						
//				userMap.put("userTable", userList);
//				new Gson().toJson(userMap);
//			}
//			else if(screenType.equals("small"))
//			{
//				while(rsUserTableSearch.next())
//				{							
//					if(rsUserTableSearch.getString(2).startsWith(textChar))
//					{
//					}
//				}
//			}	
//		}
//		catch(Exception e)
//		{				
//			e.printStackTrace();
//		}
//		finally
//		{
//			DbUtils.closeUtil(rsUserTable);
//			DbUtils.closeUtil(psUserTable);
//			DbUtils.closeUtil(con);						
//		}
//		return new Gson().toJson(userMap);		
	}
}
