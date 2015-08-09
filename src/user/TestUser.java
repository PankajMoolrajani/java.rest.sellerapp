package user;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;

import db.DbConnection;
import db.DbUtils;

public class TestUser
{
	public static void main(String ar[])
	{
		BeanUser bean_user = new BeanUser();
		bean_user.setId(6);		
		bean_user.setNameFirst("chinmay");
		bean_user.setNameLast("messi");
		bean_user.setZip(20010);
		bean_user.setState("bihar");
		bean_user.setIdUserCategory(1);
						
		Map<String,Object> map_user_bean = new HashMap<String,Object>();
		BeanInfo info = null;
		try{
			info = Introspector.getBeanInfo(bean_user.getClass());
			for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
				Method reader = pd.getReadMethod();			
			    if (reader != null){
			    	map_user_bean.put(pd.getName(),reader.invoke(bean_user));
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
		
        Set<String> keys = map_user_bean.keySet();                
        Iterator<String> key_iterator = keys.iterator();
        while(key_iterator.hasNext())
        {
        	String key = key_iterator.next();
        	//	System.out.println(map_user_bean.get(key));
        	if(map_user_bean.get(key) == null || map_user_bean.get(key).toString().equals("0")){        		
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
        
         //...Step Two take three boolean variable         
        boolean is_user_update = false;
        boolean is_address_update = false;       
        
        if(map_user_bean.containsKey("addressLineOne") ||
        		map_user_bean.containsKey("addressLineTwo") || 
        		map_user_bean.containsKey("city") || 
        		map_user_bean.containsKey("state") || 
        		map_user_bean.containsKey("zip") ||
        		map_user_bean.containsKey("country") || 
        		map_user_bean.containsKey("landmark")){
        	is_address_update = true;	        
        }         
        if(map_user_bean.containsKey("nameUser") ||
        		map_user_bean.containsKey("idUserCategory")||
        		map_user_bean.containsKey("nameFirst") ||
        		map_user_bean.containsKey("nameLast") ||
        		map_user_bean.containsKey("emailid") ||
        		map_user_bean.containsKey("phone")){
        	is_user_update = true;
        }
                                    
        Map<String,String> map_address_db = new HashMap<String,String>();
        map_address_db.put("addressLineOne","address_line_one");
        map_address_db.put("addressLineTwo","address_line_two");
        map_address_db.put("city","city");
        map_address_db.put("state","state");
        map_address_db.put("country","country");
        map_address_db.put("zip","zip");
        map_address_db.put("landamrk","landmark");	
        
 		Map<String,String> map_user_db = new HashMap<String,String>();
 		map_user_db.put("nameUser", "name_user");
 		map_user_db.put("nameFirst", "name_first");
 		map_user_db.put("nameLast", "name_last");
 		map_user_db.put("emailid", "emailid");
 		map_user_db.put("phone", "phone");
 		map_user_db.put("idUserCategory","id_user_category");
 		
 		Connection con = DbConnection.getConnection(); 		
 		if(is_address_update == true)
 		{
 			String table = "address";
 			String parameters = "";
 			String condition = "id_user";
 			
 			for(String key : keys){
 				if(map_address_db.containsKey(key)){
 					parameters = parameters + map_address_db.get(key)+" = '"+map_user_bean.get(key)+"',";
        		}
 			}        
 			try{
 				con.setAutoCommit(false);
 				parameters = parameters.substring(0, parameters.length()-1); 				
 				String query = "UPDATE "+table+" SET "+parameters+" WHERE "+condition+"=?"; 				
 				PreparedStatement ps = con.prepareStatement(query);
 				ps.setInt(1, (Integer)map_user_bean.get("id"));
 				ps.executeUpdate();
 				con.commit(); 		
 			}catch(SQLException e){
 				e.printStackTrace();
        	}
 		}
 		if(is_user_update == true){
 			String table = "user";
 			String parameters = "";
 			String condition = "id";
 			
 			for(String key : keys){
 				if(map_user_db.containsKey(key)){
 					parameters = parameters + map_user_db.get(key)+" = '"+map_user_bean.get(key)+"',";
        		}
 			}        
 			try{
 				con.setAutoCommit(false);
 				parameters = parameters.substring(0, parameters.length()-1); 				
 				String query = "UPDATE "+table+" SET "+parameters+" WHERE "+condition+"=?"; 			
 				PreparedStatement ps = con.prepareStatement(query);
 				ps.setInt(1, (Integer)map_user_bean.get("id"));
 				ps.executeUpdate();
 				con.commit(); 		
 			}catch(SQLException e){
 				e.printStackTrace();
        	}
 		}        
	}
}
