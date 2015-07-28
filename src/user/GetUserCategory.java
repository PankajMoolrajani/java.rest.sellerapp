package user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import db.DbConnection;
import db.DbUtils;


@Path("/user-category/get-user-category")
public class GetUserCategory 
{
	@GET
	@Path("/all")	
	@Produces(MediaType.TEXT_PLAIN)
	public String getUserCategoriesForSelectList()
	{		
		Connection con = DbConnection.getConnection();
		PreparedStatement psUserCatList = null;
		ResultSet rsUserCatList = null;
		Map<String,List<BeanUserCategory>> userCategoryMap = new HashMap<String,List<BeanUserCategory>>();
		List<BeanUserCategory> userCategoryList = new ArrayList<BeanUserCategory>();
		
		try{
			String columnsUserCat = "id,name";
			String tableUserCat = "user_category";
			
			psUserCatList = con.prepareStatement("select "+columnsUserCat+" from "+tableUserCat);
			rsUserCatList = psUserCatList.executeQuery();	
			while(rsUserCatList.next())
			{
				userCategoryList.add(new BeanUserCategory(rsUserCatList.getInt(1),rsUserCatList.getString(2)));
			}
			userCategoryMap.put("userCategories", userCategoryList);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			DbUtils.closeUtil(rsUserCatList);
			DbUtils.closeUtil(psUserCatList);
			DbUtils.closeUtil(con);
		}
		return new Gson().toJson(userCategoryMap);
	}
}
