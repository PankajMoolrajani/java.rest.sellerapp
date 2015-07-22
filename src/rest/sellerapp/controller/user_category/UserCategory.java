package rest.sellerapp.controller.user_category;

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

import rest.sellerapp.bean.user_category.UserCategoryBean;
import rest.sellerapp.controller.db.DbConnection;
import rest.sellerapp.controller.db.DbUtils;

@Path("/user_category")
public class UserCategory 
{
	@GET
	@Path("/category_list")	
	@Produces(MediaType.TEXT_PLAIN)
	public String getUserCategoriesForList()
	{		
		Connection con = DbConnection.getConnection();
		PreparedStatement psUserCatList = null;
		ResultSet rsUserCatList = null;
		Map<String,List<UserCategoryBean>> userCategoryMap = new HashMap<String,List<UserCategoryBean>>();
		List<UserCategoryBean> userCategoryList = new ArrayList<UserCategoryBean>();
		
		try{
			String columnsUserCat = "id,name";
			String tableUserCat = "user_category";
			
			psUserCatList = con.prepareStatement("select "+columnsUserCat+" from "+tableUserCat);
			rsUserCatList = psUserCatList.executeQuery();	
			while(rsUserCatList.next())
			{
				userCategoryList.add(new UserCategoryBean(rsUserCatList.getInt(1),rsUserCatList.getString(2)));
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
