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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import db.DbConnection;
import db.DbUtils;

import user.BeanUser;
import user.BeanUserCategory;

@Path("/user")
public  class User  {

	private Set<Integer> addressIdSet=new HashSet<Integer>();
	private String trimed_phone_num ;
	
	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)	
    public String createUser(BeanUser bean_user) {
        		
		Map<String,String> result_map = goForUserCreate(bean_user);
		return new Gson().toJson(result_map);
		
//    	String name_user = bean_user.getPassword();
//    	String password = bean_user.getPassword();
//    	
//    	Boolean verification_credentials = this.verifyPasswordComplexity(name_user, password);
//    	if (verification_credentials == true){
//    		//insert password
//        	//insert address
//        	//insert user
//    		Connection con = DbConnection.getConnection();
//    		
//    		int id_pass = 0;
//    		String table_name = "pass";
//    		String column_names = "password, question, ans";
//    		String values = "?, ?, ?";
//    		String query = "INSERT INTO "+table_name+"("+column_names+")"+" VALUES "+"("+values+")";
//    		
//    		
//    		try {
//    			
//    			PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
//    			
//    			ps.setString(1, bean_user.getPassword());
//    			ps.setString(2, bean_user.getQuestion());
//    			ps.setString(3, bean_user.getAns());
//    			
//    			int rows_affected =  ps.executeUpdate();
//    			
//    			if (rows_affected != 0){
//    				
//    				ResultSet rs = ps.getGeneratedKeys();
//    				
//    				if (rs.next()){
//    				
//    					id_pass = rs.getInt(1);
//    					System.out.println(id_pass);
//    					
//    					Map<String,String> result_map = goForUserCreate(bean_user);
//    				}    			
//    			}
//    		
//    		} catch (SQLException e) {
//    			// TODO Auto-generated catch block
//    			e.printStackTrace();
//    		}
//    	}
    	
		//return new Gson().toJson(result_map);
    }
    
    public Map<String,String> goForUserCreate(user.BeanUser bean_user)
    {
    	Map<String, String> result_map = new HashMap<String,String>();
    	result_map.put("error_code", "");
		
		
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
		//System.out.println(map_user_bean);
		if(validateUser(map_user_bean , result_map)){			
			//System.out.println("In validate order true");
			goForUserCreate(map_user_bean , result_map);
		}
				
		return result_map;
    }
	public boolean validateUser(Map<String,Object> map_user_bean , Map<String,String> result_map)
	{
//		System.out.println("validateZip "+validateZip(map_user_bean, result_map));
//		System.out.println("validateEmail "+validateEmail(map_user_bean, result_map));
//		System.out.println("validatePhone "+validatePhone(map_user_bean, result_map));
//		System.out.println("validateNameCityState "+validateNameCityState(map_user_bean, result_map));
//		System.out.println("validate empty fields "+validateEmptyFields(map_user_bean, result_map));
//		System.out.println("validate injuction "+validateHtmlInjection(map_user_bean, result_map));
//		System.out.println("validate category "+validateCategory(map_user_bean,result_map));
		
		if(validateEmptyFields(map_user_bean, result_map) && validateHtmlInjection(map_user_bean, result_map) && validateZip(map_user_bean,result_map) && validateEmail(map_user_bean,result_map) && validatePhone(map_user_bean,result_map) && validateCategory(map_user_bean,result_map) && validateNameCityState(map_user_bean,result_map))		
		{
			return true;		
		}
		else
		{
			result_map.put("error_code", "505");
			return false;
		}			
	}
	
	public boolean validateEmptyFields(Map<String,Object> map_user_bean, Map<String,String> result_map)
	{
		if(map_user_bean.get("nameFirst").toString().isEmpty() || map_user_bean.get("nameLast").toString().isEmpty() || map_user_bean.get("idUserCategory").toString().isEmpty() ||  map_user_bean.get("phone").toString().isEmpty() || map_user_bean.get("emailid").toString().isEmpty() || map_user_bean.get("addressLineOne").toString().isEmpty() || map_user_bean.get("addressLineTwo").toString().isEmpty() || map_user_bean.get("city").toString().isEmpty() || map_user_bean.get("state").toString().isEmpty() ||map_user_bean.get("zip").toString().isEmpty())
			return false;		
		else
			return true;		
	}
	
	private boolean validateHtmlInjection(Map<String,Object> map_user_bean, Map<String,String> result_map)
	{
		Pattern pattern = Pattern.compile("<(\"[^\"]*\"|'[^']*'|[^'\">])*>");
		
	    Matcher matcher_fname = pattern.matcher(map_user_bean.get("nameFirst").toString());
	    Matcher matcher_lname = pattern.matcher(map_user_bean.get("nameLast").toString());	    
		Matcher matcher_phone = pattern.matcher(map_user_bean.get("phone").toString());
	    Matcher matcher_email = pattern.matcher(map_user_bean.get("emailid").toString());
		Matcher matcher_address_one = pattern.matcher(map_user_bean.get("addressLineOne").toString());
	    Matcher matcher_address_two = pattern.matcher(map_user_bean.get("addressLineTwo").toString());
	    Matcher matcher_city = pattern.matcher(map_user_bean.get("city").toString());
	    Matcher matcher_state = pattern.matcher(map_user_bean.get("state").toString());
	    Matcher matcher_zip = pattern.matcher(map_user_bean.get("zip").toString());
	    	    
	    if(matcher_fname.matches() || 
	    		matcher_lname.matches() || 
	    		matcher_phone.matches() || 
	    		matcher_email.matches() ||
	    		matcher_address_one.matches() || 
	    		matcher_address_two.matches() ||
	    		matcher_city.matches() || 
	    		matcher_state.matches() ||
	    		matcher_zip.matches()){
	    	
	    	result_map.put("userStatus", "HTML Injection");
	    	return false;
	    }
	    else 
	    {	    		    
	    	return true;
	    }		
	}
	
	public boolean validateZip(Map<String,Object> map_user_bean , Map<String,String> result_map)
	{
		Pattern pattern_zip = Pattern.compile("[0-9]+");	     
	    Matcher matcher_zip = pattern_zip.matcher(map_user_bean.get("zip").toString());
	     if(matcher_zip.matches())
	     {
	    	 return true;
	     }
	     else 
	     {
	    	 result_map.put("userStatus", "InvalidZip");
	    	 return false;
	     }
	}
	public boolean validateEmail(Map<String,Object>map_user_bean, Map<String,String> result_map)
	{
		 Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
	     Matcher matcher = pattern.matcher(map_user_bean.get("emailid").toString());
	     if(matcher.matches())
	     {
	    	return true; 
	     }
	     else
	     {
	    	 result_map.put("userStatus", "InvalidEmail");
	    	 return false;
	     }
	}
	public boolean validatePhone(Map<String,Object>map_user_bean, Map<String,String> result_map)
	{
		 Pattern pattern = Pattern.compile("^(\\+?\\d{1,4}[\\s-])?(?!0+\\s+,?$)\\d{10}\\s*,?$");
	     Matcher matcher = pattern.matcher(map_user_bean.get("phone").toString());
	     if(matcher.matches())
	     {
	    	return true; 
	     }
	     else
	     {
	    	 result_map.put("userStatus", "InvalidPhone");
	    	 return false;
	     }
	}
	public boolean validateCategory(Map<String,Object>map_user_bean, Map<String,String> result_map)
	{
		if(map_user_bean.get("idUserCategory").equals("Select Category"))
		{
			result_map.put("userStatus", "Select Category");
			return false;
		}
		else
		{
			return true;
		}
	}
	public boolean validateNameCityState(Map<String,Object> map_user_bean, Map<String,String> result_map)
	{
		 Pattern pattern = Pattern.compile("^[a-zA-Z]+(?:(?:\\s+|-)[a-zA-Z]+)*$");
	     Matcher matcher_address_one = pattern.matcher(map_user_bean.get("nameFirst").toString());
	     Matcher matcher_address_two = pattern.matcher(map_user_bean.get("nameLast").toString());
	     Matcher matcher_city = pattern.matcher(map_user_bean.get("city").toString());
	     Matcher matcher_state = pattern.matcher(map_user_bean.get("state").toString());
	     if(matcher_address_one.matches() && matcher_address_two.matches() && matcher_city.matches() && matcher_state.matches())
	     {
	    	 return true;
	     }
	     else
	     {
	    	 result_map.put("userStatus", "Invalid Name Or City Or State");
	    	 return false;
	     }
	} 
	
	public void goForUserCreate(Map<String,Object> map_user_bean, Map<String,String> result_map)
	{
		/*this is here because we don't need to trim the ph no. came from request, each time!! */						
		trimed_phone_num = trimLastTenDigits(map_user_bean.get("phone").toString());
		PreparedStatement ps_address = null;
		ResultSet rs_address = null;
		Connection con = null;
		try
		{
			con = DbConnection.getConnection();						
			ps_address = con.prepareStatement("select * from address;");
			rs_address = ps_address.executeQuery();			
			
			createUser(rs_address, map_user_bean, result_map);				
		}	
		catch(Exception e)
		{						
			result_map.put("error_code", "502");			
		}
		finally
		{
			DbUtils.closeUtil(rs_address);
			DbUtils.closeUtil(ps_address);
			DbUtils.closeUtil(con);		
		}
	}	
	public void createUser(ResultSet rs_address, Map<String,Object> map_user_bean, Map<String,String> result_map)throws Exception
	{
		//System.out.println("create Order called "+checkZipCode(rs_address, map_user_bean));
		if(checkZipCode(rs_address, map_user_bean))
		{
			//System.out.println("ckeckZipCode ture");
			rs_address.beforeFirst();
			if(checkAddress(rs_address, map_user_bean))
			{
				System.out.println("ckeckAddress ture");
				if(checkPhoneNum(map_user_bean,result_map))
				{
					//System.out.println("ckeckPhonNum ture");
					//System.out.println("user already Exist");
					result_map.put("userStatus", "userAlreadyExist");					
				}
				else
				{
					//System.out.println("ckeckphoneNum false");
					//System.out.println("user not Exist1");
					result_map.put("userStatus", "userNotExist");
					createUser(map_user_bean, result_map);							
				}				
				//createUser(addressId);							
			}
			else
			{
				System.out.println("user not Exist2");
				System.out.println("ckeckAddress false");
				result_map.put("userStatus", "userNotExist");
				createUser(map_user_bean, result_map);								
			}
		}
		else
		{
			//System.out.println("ckeckZipCode false");
			//System.out.println("user not Exist3");
			result_map.put("userStatus", "userNotExist");
			createUser(map_user_bean, result_map);
		}
		
	}
	
	public boolean checkZipCode(ResultSet rs_address, Map<String,Object> map_user_bean) throws SQLException
	{		
		System.out.println(rs_address);
		while(rs_address.next())
		{			
			if(map_user_bean.get("zip").toString().equals(rs_address.getString("zip")))
			{								
				return true;					
			}			
		}
		return false;
	}
	public boolean checkAddress(ResultSet rs_address, Map<String,Object> map_user_bean)throws SQLException
	{
		while(rs_address.next())
		{
			//System.out.println("hello");
			int line_one = addressProbability(map_user_bean.get("addressLineOne").toString(), rs_address.getString("address_line_one"));
			int line_two = addressProbability(map_user_bean.get("addressLineTwo").toString(), rs_address.getString("address_line_two"));
			////System.out.println(lineOne+" "+lineTwo);
			if(line_one<=4 && line_two<=4)
			{						
				addressIdSet.add(rs_address.getInt("id"));				
			}
			
		}
		////System.out.println("size of list is "+addressIdSet.size());
		if(addressIdSet.size()>0)
		{
			
			return true;
		}
		else
			return false;
	}
	
	public boolean checkPhoneNum(Map<String,Object> map_user_bean, Map<String,String> result_map){		
		PreparedStatement ps_user = null;
		ResultSet rs_user = null;
		Connection con = DbConnection.getConnection();
		Iterator<Integer> addIdIterator = addressIdSet.iterator();	
		try{
			while(addIdIterator.hasNext()){
				int addId = addIdIterator.next();
				//System.out.println("addId Data came here 1 "+addId);								
				ps_user = con.prepareStatement("select phone from user where id_address="+addId);
				rs_user = ps_user.executeQuery();					
				rs_user.next();	
					//System.out.println("The trimed ph. no. is "+trimed_phone_num);
					if(rs_user.getString("phone").endsWith(trimed_phone_num)){				
						//System.out.println("in phone no. true section");
						return true;
					}
			}
		}
		catch(Exception e){
			System.out.println("b");
			result_map.put("error_code", "502");
		}
		finally{
			DbUtils.closeUtil(rs_user);
			DbUtils.closeUtil(ps_user);
			DbUtils.closeUtil(con);		
		}
		return false;
	}
	public String trimLastTenDigits(String ph)
	{
		if(ph.length()>=10)
		{
			return ph.substring(ph.length()-10);
		}
		else
			return ph;
	}
	public int addressProbability(String form_string, String table_string)
	{
		if (form_string == null || table_string == null) {
	          throw new IllegalArgumentException("Strings must not be null");
	      }	     

	      int n = form_string.length(); // length of s
	      int m = table_string.length(); // length of t

	      if (n == 0) {
	          return m;
	      } else if (m == 0) {
	          return n;
	      }

	      if (n > m) {
	          // swap the input strings to consume less memory
	          String tmp = form_string;
	          form_string = table_string;
	          table_string = tmp;
	          n = m;
	          m = table_string.length();
	      }

	      int p[] = new int[n+1]; //'previous' cost array, horizontally
	      int d[] = new int[n+1]; // cost array, horizontally
	      int _d[]; //placeholder to assist in swapping p and d

	      // indexes into strings s and t
	      int i; // iterates through s
	      int j; // iterates through t

	      char t_j; // jth character of t

	      int cost; // cost

	      for (i = 0; i<=n; i++) {
	          p[i] = i;
	      }

	      for (j = 1; j<=m; j++) {
	          t_j = table_string.charAt(j-1);
	          d[0] = j;

	          for (i=1; i<=n; i++) {
	              cost = form_string.charAt(i-1)==t_j ? 0 : 1;
	              // minimum of cell to the left+1, to the top+1, diagonally left and up +cost
	              d[i] = Math.min(Math.min(d[i-1]+1, p[i]+1),  p[i-1]+cost);
	          }

	          // copy current distance counts to 'previous row' distance counts
	          _d = p;
	          p = d;
	          d = _d;
	      }

	      // our last action in the above loop was to switch d and p, so p now 
	      // actually has the most recent cost counts
	      return p[n];	 

	}
	public void createUser(Map<String,Object> map_user_bean, Map<String,String> result_map)
	{		
		Connection con = DbConnection.getConnection();
		
		//System.out.println("in create user function");		
		try
		{
			con.setAutoCommit(false);
			String column_user="id,id_user_category,name_first,name_last,emailid,phone";
			String table_user="user";
			String parameters_user="NULL,?,?,?,?,?";
			String query_user = "insert into "+table_user+"("+column_user+") values("+parameters_user+");";
			
			PreparedStatement ps_user = con.prepareStatement( query_user , Statement.RETURN_GENERATED_KEYS);			
			ps_user.setString(1,map_user_bean.get("idUserCategory").toString());
			ps_user.setString(2,map_user_bean.get("nameFirst").toString());
			ps_user.setString(3,map_user_bean.get("nameLast").toString());
			ps_user.setString(4,map_user_bean.get("emailid").toString());
			ps_user.setString(5,map_user_bean.get("phone").toString());
			
			int row_affected_user = ps_user.executeUpdate();			
			int generated_user_id = 0;
			if(row_affected_user !=0)
			{
				ResultSet rs_generated_key = ps_user.getGeneratedKeys();
				rs_generated_key.next();
				generated_user_id = rs_generated_key.getInt(1);				
			}
			////System.out.println("first checkpoint is clear");			
			
			String columnsAddress="id,id_user,address_line_one,address_line_two,city,state,zip";
			String tableAddress="address";
			String parametersAddress="NULL,?,?,?,?,?,?";
			String query_address = "insert into "+tableAddress+" ("+columnsAddress+") values("+parametersAddress+");";

			PreparedStatement ps_address = con.prepareStatement(query_address, Statement.RETURN_GENERATED_KEYS);
			ps_address.setInt(1, generated_user_id);
			ps_address.setString(2, map_user_bean.get("addressLineOne").toString());
			ps_address.setString(3, map_user_bean.get("addressLineTwo").toString());
			ps_address.setString(4, map_user_bean.get("city").toString());
			ps_address.setString(5, map_user_bean.get("state").toString());
			ps_address.setString(6, map_user_bean.get("zip").toString());
			
			int row_affected_address = ps_address.executeUpdate();
			int generated_address_id = 0;
			if(row_affected_address !=0)
			{
				ResultSet rs_generated_key = ps_address.getGeneratedKeys();
				rs_generated_key.next();
				generated_address_id = rs_generated_key.getInt(1);				
			}
			//System.out.println("auto generated address key is "+generated_address_id);						
			
			//assign foreign key to user table of primary key of last inserted address
			String parameter_user_update="id_address=?";
			String table_user_update="user";			
			String condition_user_update="id=?";
			String query_user_update = "UPDATE "+table_user_update+" SET "+parameter_user_update+" WHERE "+condition_user_update+"";
			
			PreparedStatement ps_user_update = con.prepareStatement(query_user_update);
			ps_user_update.setInt(1, generated_address_id);
			ps_user_update.setInt(2, generated_user_id);
			ps_user_update.execute();
									
			//assign last inserted user id to the table according to the drop-down user category table.	
			String table_user_category = "user_category";
			String column_user_category = "*";
			String condition_user_category = "id=?";
			String query_user_category = "SELECT "+column_user_category+" FROM "+table_user_category+" WHERE "+condition_user_category+"";
			PreparedStatement ps_user_category = con.prepareStatement(query_user_category);			
			
			ps_user_category.setInt(1, Integer.parseInt(map_user_bean.get("idUserCategory").toString()));
			ResultSet rs_user_category = ps_user_category.executeQuery();
			//System.out.println("go from here 2");
			//get name of the table
			rs_user_category.next();		
			
			//System.out.println(".............insert into "+rs_user_category.getString("name")+"(id_user) values("+generated_user_id+".........);/");
			String column_user_category_type = "id_user";
			String table_user_category_type = rs_user_category.getString("name");
			String parameter_user_category_type= "?";		
			String query_user_category_type = "insert into "+table_user_category_type+"("+column_user_category_type+") values("+parameter_user_category_type+")";

			PreparedStatement ps_user_category_type = con.prepareStatement(query_user_category_type);		
			ps_user_category_type.setInt(1, generated_user_id);
			ps_user_category_type.execute();			
			con.commit();
			//System.out.println("Forth checkpoint clear");	
		}
		catch(Exception e){			
			e.printStackTrace();
			//result_map.put("error_code", "502");
		}
		finally{
			//DbUtils.closeUtil(psUser);			
		}
	}
    
    
    public Boolean verifyPasswordComplexity(String name_user, String password) {
        
        return true;
        
    }	
    
    @POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)	
    public String updateUser(BeanUser bean_user) {
    	
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
        while(key_iterator.hasNext()){
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
        return "{}";
    }	
    
    public String deleteUser(BeanUser bean_user) {
        
        return null;
        
    }	
    
    @GET
	@Path("/get/all")
	@Produces(MediaType.TEXT_PLAIN)
    public String getUserAll() {
        
    	Connection con = DbConnection.getConnection();
		PreparedStatement ps_user_all = null;
		ResultSet rs_user_all = null;
		List<BeanUser> list_user_all = new ArrayList<BeanUser>();
		Map<String,List<BeanUser>> map_user_all = new HashMap<String,List<BeanUser>>();		
		try
		{
			String table_user_all = "user";
			String column_user_all = "id,name_user,emailid,phone";
			//String condition_user = " where map_url=?";	
			ps_user_all = con.prepareStatement("SELECT "+column_user_all+" FROM "+table_user_all);
			rs_user_all= ps_user_all.executeQuery();			
			
			while(rs_user_all.next())
			{												
				list_user_all.add(new BeanUser(rs_user_all.getInt("id"),rs_user_all.getString("name_user"),rs_user_all.getString("emailid"),rs_user_all.getString("phone")));																																												
			}
			map_user_all.put("userTable", list_user_all);														
		}
		catch(Exception e)
		{				
			e.printStackTrace();
		}
		finally
		{
			DbUtils.closeUtil(rs_user_all);
			DbUtils.closeUtil(ps_user_all);
			DbUtils.closeUtil(con);						
		}		
		return new Gson().toJson(map_user_all);
        
    }	
    
    @POST
	@Path("/get/search")	
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)	
    public String getUserSearch(BeanUser bean_user){    	    	
    	Connection con = DbConnection.getConnection();
		PreparedStatement ps_user_search = null;
		ResultSet rs_user_search = null;
		List<BeanUser> list_user_search = new ArrayList<BeanUser>();
		Map<String,List<BeanUser>> map_user_search = new HashMap<String,List<BeanUser>>();
		
		try
		{
			String table_user_search = "user";
			String columns_user_search = "id,name_user,emailid,phone";
			String condition_user_search = "name_user LIKE ?";
			String query_user_search = "SELECT "+columns_user_search+" FROM "+table_user_search+" WHERE "+condition_user_search;
			
			ps_user_search = con.prepareStatement(query_user_search);
			ps_user_search.setString(1, bean_user.getNameUser()+"%");
			rs_user_search= ps_user_search.executeQuery();										
			
			while(rs_user_search.next())
			{						
				if(rs_user_search.getString(2)!=null && rs_user_search.getString(2).startsWith(bean_user.getNameUser()))
				{							
					list_user_search.add(new BeanUser(rs_user_search.getInt("id"),rs_user_search.getString("name_user"),rs_user_search.getString("emailid"),rs_user_search.getString("phone")));																			
				}													
			}						
			map_user_search.put("userTable", list_user_search);
			new Gson().toJson(map_user_search);				
		}
		catch(Exception e)
		{				
			e.printStackTrace();
		}
		finally
		{
			DbUtils.closeUtil(rs_user_search);
			DbUtils.closeUtil(ps_user_search);
			DbUtils.closeUtil(con);						
		}
		return new Gson().toJson(map_user_search);		
    }
    
    @POST
	@Path("/get/id")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
    public String getUserId(BeanUser bean_user_get){
    	System.out.println("webservice called");
    	Connection con = DbConnection.getConnection();
		
		Map<String,Object> map_user_details = new HashMap <String,Object>();
		
		BeanUser bean_user = new BeanUser();		
		BeanUserCategory bean_user_category = new BeanUserCategory();
		
		PreparedStatement ps_user_data = null;
		ResultSet rs_user_data = null;
		
		PreparedStatement ps_user_address_data = null;
		ResultSet rs_user_address_data = null;
		
		PreparedStatement ps_user_category_data = null;
		ResultSet rs_user_category_data = null;
		try{						
			String columns_user_table = "*";
			String table_user_table = "user";
			String condition_user_table = "id=?";
			ps_user_data = con.prepareStatement("SELECT "+columns_user_table+" FROM "+table_user_table+" WHERE "+condition_user_table);
			ps_user_data.setInt(1, bean_user_get.getId());

			rs_user_data = ps_user_data.executeQuery();
			
			rs_user_data.next();			
			bean_user.setNameFirst(rs_user_data.getString("name_first"));
			bean_user.setNameLast(rs_user_data.getString("name_last"));
			bean_user.setPhone(rs_user_data.getString("phone"));
			bean_user.setEmailid(rs_user_data.getString("emailid"));						
			int addressId = rs_user_data.getInt("id_address");
			int userCatId = rs_user_data.getInt("id_user_category");
			
			
			String columns_address_table = "*";
			String table_address_table = "address";
			String condition_address_table = "id=?";
			String query_address_table = "SELECT "+columns_address_table+" FROM "+table_address_table+" WHERE "+condition_address_table;
			
			ps_user_address_data = con.prepareStatement(query_address_table);
			ps_user_address_data.setInt(1, addressId);
			rs_user_address_data = ps_user_address_data.executeQuery();
			
			rs_user_address_data.next();
			bean_user.setAddressLineOne(rs_user_address_data.getString("address_line_one"));
			bean_user.setAddressLineTwo(rs_user_address_data.getString("address_line_two"));
			bean_user.setCity(rs_user_address_data.getString("city"));
			bean_user.setState(rs_user_address_data.getString("state"));
			bean_user.setZip(rs_user_address_data.getInt("zip"));
			
			
			String columns_user_category = "id,name";
			String table_user_category = "user_category";
			String condition_user_category = "id=?";
			String query_user_category = "SELECT "+columns_user_category+" FROM "+table_user_category+" WHERE "+condition_user_category;

			ps_user_category_data = con.prepareStatement(query_user_category);
			ps_user_category_data.setInt(1, userCatId);
			rs_user_category_data = ps_user_category_data.executeQuery();
			
			rs_user_category_data.next();
			bean_user_category.setId(rs_user_category_data.getInt("id"));
			bean_user_category.setName(rs_user_category_data.getString("name"));
			map_user_details.put("userDetails", bean_user);
			map_user_details.put("userCategoryDetails", bean_user_category);
		}
		catch(Exception e){
		e.printStackTrace();	
		}
		finally{
			DbUtils.closeUtil(rs_user_category_data);
			DbUtils.closeUtil(ps_user_category_data);			
			DbUtils.closeUtil(rs_user_address_data);
			DbUtils.closeUtil(ps_user_address_data);
			DbUtils.closeUtil(rs_user_data);
			DbUtils.closeUtil(ps_user_data);
			DbUtils.closeUtil(con);
		}
		return new Gson().toJson(map_user_details);
    }
 }