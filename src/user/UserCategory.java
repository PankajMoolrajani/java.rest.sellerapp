

package user;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import db.DbConnection;
import db.DbUtils;


@Path("/user/category")
public  class UserCategory  {

	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	
    public String createUserCategory(BeanUserCategory bean_user_category) {
        int id_user_category = 0;
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("id",bean_user_category.getId());
		map.put("name", bean_user_category.getName());
		map.put("description", bean_user_category.getDescription());
        
        Connection con = DbConnection.getConnection();
		String table_name = "user_category";
		String column_names = "name, description";
		String values = "?, ?";
		String query = "INSERT INTO "+table_name+"("+column_names+")"+" VALUES "+"("+values+")";
		
		
		try {
			
			PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			
			ps.setString(1, bean_user_category.getName());
			ps.setString(2, bean_user_category.getDescription());
			
			int rows_affected =  ps.executeUpdate();
			
			if (rows_affected != 0){
				
				ResultSet rs = ps.getGeneratedKeys();
				
				if (rs.next()){
				
					id_user_category = rs.getInt(1);
					map.put("id_user_category", id_user_category);
				
				}
			
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new Gson().toJson(map);	
    }	

	@POST
	@Path("/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	
    public String deleteUserCategory(BeanUserCategory bean_user_category) {
		
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("id",bean_user_category.getId());
		map.put("name", bean_user_category.getName());
		map.put("description", bean_user_category.getDescription());
        
        Connection con = DbConnection.getConnection();
		String table_name = "user_category";
		String query = "DELETE from "+table_name+" WHERE id=?";
		
		
		try {
			
			PreparedStatement ps = con.prepareStatement(query);
			
			ps.setInt(1, bean_user_category.getId());
			
			int rows_affected =  ps.executeUpdate();
			
			if (rows_affected != 0){
				
				System.out.println("user_category : delete : "+bean_user_category.getId()+" : success");
				map.put("result", "success");
			
			}
			else {
				System.out.println("user_category : delete:"+bean_user_category.getId()+" : fail");
				map.put("result", "fail");
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new Gson().toJson(map);	
    }	

	
    @POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)	
    public String updateUserCategory(BeanUserCategory bean_user_category) {
    	
    	Map<String,Object> map_user_category_bean = new HashMap<String,Object>();
		BeanInfo info = null;
		try{
			info = Introspector.getBeanInfo(bean_user_category.getClass());
			for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
				Method reader = pd.getReadMethod();			
			    if (reader != null){
			    	map_user_category_bean.put(pd.getName(),reader.invoke(bean_user_category));
			    }
			}
		}catch(IllegalArgumentException e){
			e.printStackTrace();
		}catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IntrospectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        Set<String> keys = map_user_category_bean.keySet();                
        Iterator<String> key_iterator = keys.iterator();
        while(key_iterator.hasNext())
        {
        	String key = key_iterator.next();
        	//	System.out.println(map_user_bean.get(key));
        	if(map_user_category_bean.get(key) == null || map_user_category_bean.get(key).toString().equals("0")){        		
        		key_iterator.remove();        		
        	}        	
        }         
                  
                
//        for(String key : keys){        
//        	if(map_user_bean.get(key) instanceof String){
//        		System.out.println("string "+key+"  "+map_user_bean.get(key));
//        	}
//        	else if(map_user_bean.get(key) instanceof Integer){
//        		System.out.println("Int "+key+"   "+map_user_bean.get(key));
//        	}        	
//        }
        
         //for mapping bean variable name from db name    
 		Map<String,String> map_user_category_db = new HashMap<String,String>();
 		map_user_category_db.put("name", "name");
 		map_user_category_db.put("description", "description");
 		map_user_category_db.put("id","id");
 		
 		Connection con = DbConnection.getConnection(); 		 		 	
 		String table = "user_category";
 		String parameters = "";
 		String condition = "id";
 			
 		for(String key : keys){
 			if(map_user_category_db.containsKey(key)){
 				parameters = parameters + map_user_category_db.get(key)+" = '"+map_user_category_bean.get(key)+"',";
        	}
 		}        
 		try{
 			con.setAutoCommit(false);
 			parameters = parameters.substring(0, parameters.length()-1); 				
 			String query = "UPDATE "+table+" SET "+parameters+" WHERE "+condition+"=?"; 			
 			PreparedStatement ps = con.prepareStatement(query);
 			ps.setInt(1, (Integer)map_user_category_bean.get("id"));
 			ps.executeUpdate();
 			con.commit(); 		
 		}catch(SQLException e){
 			e.printStackTrace();
 		}
 		    
        return "{}";
    }	

	
	@GET
	@Path("/get/all")	
	@Produces(MediaType.TEXT_PLAIN)
	public String getUserCategoryAll(){
		
		Connection con = DbConnection.getConnection();
		PreparedStatement ps_user_category_all = null;
		ResultSet rs_user_category_all = null;
		List<BeanUserCategory> list_user_category_all = new ArrayList<BeanUserCategory>();
		Map<String,List<BeanUserCategory>> map_user_category_all = new HashMap<String,List<BeanUserCategory>>();		
		try{
			String table_user_category_all = "user_category";
			String column_user_category_all = "id,name";
			//String condition_user = " where map_url=?";	
			ps_user_category_all = con.prepareStatement("SELECT "+column_user_category_all+" FROM "+table_user_category_all);
			rs_user_category_all= ps_user_category_all.executeQuery();			
			
			while(rs_user_category_all.next())
			{												
				list_user_category_all.add(new BeanUserCategory(rs_user_category_all.getInt("id"),rs_user_category_all.getString("name")));																																												
			}
			map_user_category_all.put("user_category_table", list_user_category_all);														
		}
		catch(SQLException e)
		{				
			e.printStackTrace();
		}
		finally{
			DbUtils.closeUtil(rs_user_category_all);
			DbUtils.closeUtil(ps_user_category_all);
			DbUtils.closeUtil(con);						
		}		
		return new Gson().toJson(map_user_category_all);				
	}
	
    @POST
	@Path("/get/search")	
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)	
    public String getUserCategorySearch(BeanUserCategory bean_user_category){    	        	
    	Connection con = DbConnection.getConnection();
		PreparedStatement ps_user_category_search = null;
		ResultSet rs_user_category_search = null;
		List<BeanUserCategory> list_user_category_search = new ArrayList<BeanUserCategory>();
		Map<String,List<BeanUserCategory>> map_user_category_search = new HashMap<String,List<BeanUserCategory>>();
		
		try{
			String table_user_category_search = "user_category";
			String columns_user_category_search = "id,name";
			String condition_user_category_search = "name LIKE ?";
			String query_user_category_search = "SELECT "+columns_user_category_search+" FROM "+table_user_category_search+" WHERE "+condition_user_category_search;
			
			ps_user_category_search = con.prepareStatement(query_user_category_search);
			ps_user_category_search.setString(1, bean_user_category.getName()+"%");
			rs_user_category_search= ps_user_category_search.executeQuery();										
			
			while(rs_user_category_search.next())
			{						
				if(rs_user_category_search.getString(2)!=null && rs_user_category_search.getString(2).startsWith(bean_user_category.getName()))
				{							
					list_user_category_search.add(new BeanUserCategory(rs_user_category_search.getInt("id"),rs_user_category_search.getString("name")));																			
				}													
			}						
			map_user_category_search.put("user_category_search_row", list_user_category_search);
			new Gson().toJson(map_user_category_search);				
		}
		catch(Exception e){				
			e.printStackTrace();
		}
		finally{
			DbUtils.closeUtil(rs_user_category_search);
			DbUtils.closeUtil(ps_user_category_search);
			DbUtils.closeUtil(con);						
		}
		return new Gson().toJson(map_user_category_search);		
    }
    
    @POST
	@Path("/get/id")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
    public String getUserCategoryId(BeanUserCategory bean_user_get){    	
    	Connection con = DbConnection.getConnection();		
		Map<String,Object> map_user_category_details = new HashMap <String,Object>();
					
		BeanUserCategory bean_user_category = new BeanUserCategory();
		
		PreparedStatement ps_user_category_data = null;
		ResultSet rs_user_category_data = null;
		
		try{		
			con.setAutoCommit(false);
			String columns_user_category = "id,name,description";
			String table_user_category = "user_category";
			String condition_user_category = "id=?";
			String query_user_category = "SELECT "+columns_user_category+" FROM "+table_user_category+" WHERE "+condition_user_category;

			ps_user_category_data = con.prepareStatement(query_user_category);
			ps_user_category_data.setInt(1, bean_user_get.getId());
			rs_user_category_data = ps_user_category_data.executeQuery();
			
			rs_user_category_data.next();
			bean_user_category.setId(rs_user_category_data.getInt("id"));
			bean_user_category.setName(rs_user_category_data.getString("name"));
			bean_user_category.setDescription(rs_user_category_data.getString("description"));
			
			map_user_category_details.put("user_category_form_detail", bean_user_category);
			con.commit();
		}
		catch(Exception e){
			e.printStackTrace();	
		}
		finally{
			DbUtils.closeUtil(rs_user_category_data);
			DbUtils.closeUtil(ps_user_category_data);						
			DbUtils.closeUtil(con);
		}
		return new Gson().toJson(map_user_category_details);
    }
 }