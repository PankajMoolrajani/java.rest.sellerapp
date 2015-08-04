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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;

import db.DbConnection;
import db.DbUtils;
import user.BeanUser;

public  class User  {

	private Set<Integer> addressIdSet=new HashSet<Integer>();
	private String TrimedPhNum ;
	
    public String createUser(BeanUser bean_user) {
        
        
    	String name_user = bean_user.getPassword();
    	String password = bean_user.getPassword();
    	
    	Boolean verification_credentials = this.verifyPasswordComplexity(name_user, password);
    	if (verification_credentials == true){
    		//insert password
        	//insert address
        	//insert user
    		Connection con = DbConnection.getConnection();
    		
    		int id_pass = 0;
    		String table_name = "pass";
    		String column_names = "password, question, ans";
    		String values = "?, ?, ?";
    		String query = "INSERT INTO "+table_name+"("+column_names+")"+" VALUES "+"("+values+")";
    		
    		
    		try {
    			
    			PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    			
    			ps.setString(1, bean_user.getPassword());
    			ps.setString(2, bean_user.getQuestion());
    			ps.setString(3, bean_user.getAns());
    			
    			int rows_affected =  ps.executeUpdate();
    			
    			if (rows_affected != 0){
    				
    				ResultSet rs = ps.getGeneratedKeys();
    				
    				if (rs.next()){
    				
    					id_pass = rs.getInt(1);
    					System.out.println(id_pass);
    					
    					goForUserCreate(bean_user);
    				}
    			
    			}
    		
    		} catch (SQLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}
    	
    	return null;
        
        
    }
    
    public Map<String,String> goForUserCreate(BeanUser bean_user)
    {
    	Map<String, String> map_result = new HashMap<String,String>();
    	map_result.put("error_code", "");
		
		
		Map<String,Object> objectAsMap = new HashMap<String,Object>();
		BeanInfo info = null;
		try{
			info = Introspector.getBeanInfo(bean_user.getClass());
			for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
				Method reader = pd.getReadMethod();			
			    if (reader != null){
			    	objectAsMap.put(pd.getName(),reader.invoke(bean_user));
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
		
		if(validateOrder(objectAsMap , map_result)){			
			//System.out.println("In validate order true");
			goForOrder(objectAsMap , map_result);
		}
		return null;
    }
	public boolean validateOrder(Map<String,Object> objectAsMap , Map<String,String> map_result)
	{
//		System.out.println("validateZip "+validateZip(objectAsMap));
//		System.out.println("validateEmail "+validateEmail(objectAsMap));
//		System.out.println("validatePhone "+validatePhone(objectAsMap));
//		System.out.println("validateNameCityState "+validateNameCityState(objectAsMap));
				
		if(validateEmptyFields(objectAsMap, map_result) && validateHtmlInjection(objectAsMap, map_result) && validateZip(objectAsMap,map_result) && validateEmail(objectAsMap,map_result) && validatePhone(objectAsMap,map_result) && validateCategory(objectAsMap,map_result) && validateNameCityState(objectAsMap,map_result))		
		{
			return true;		
		}
		else
		{
			map_result.put("error_code", "505");
			return false;
		}		
	}
	
	public boolean validateEmptyFields(Map<String,Object> objectAsMap, Map<String,String> map_result)
	{
		if(objectAsMap.get("form_user_fname_text").toString().isEmpty() || objectAsMap.get("form_user_lname_text").toString().isEmpty() || objectAsMap.get("form_user_category_select").toString().isEmpty() ||  objectAsMap.get("form_user_phone_text").toString().isEmpty() || objectAsMap.get("form_user_email_text").toString().isEmpty() || objectAsMap.get("form_user_add1_text").toString().isEmpty() || objectAsMap.get("form_user_add2_text").toString().isEmpty() || objectAsMap.get("form_user_city_text").toString().isEmpty() || objectAsMap.get("form_user_state_text").toString().isEmpty() ||objectAsMap.get("form_user_zip_text").toString().isEmpty())
			return false;		
		else
			return true;		
	}
	
	private boolean validateHtmlInjection(Map<String,Object> objectAsMap, Map<String,String> map_result)
	{
		Pattern pattern = Pattern.compile("<(\"[^\"]*\"|'[^']*'|[^'\">])*>");
		
	    Matcher matcherFName = pattern.matcher(objectAsMap.get("form_user_fname_text").toString());
	    Matcher matcherLname = pattern.matcher(objectAsMap.get("form_user_lname_text").toString());	    
		Matcher matcherPhone = pattern.matcher(objectAsMap.get("form_user_phone_text").toString());
	    Matcher matcherEmail = pattern.matcher(objectAsMap.get("form_user_email_text").toString());
		Matcher matcherAddOne = pattern.matcher(objectAsMap.get("form_user_add1_text").toString());
	    Matcher matcherAddTwo = pattern.matcher(objectAsMap.get("form_user_add2_text").toString());
	    Matcher matcherCity = pattern.matcher(objectAsMap.get("form_user_city_text").toString());
	    Matcher matcherState = pattern.matcher(objectAsMap.get("form_user_state_text").toString());
	    Matcher matcherZip = pattern.matcher(objectAsMap.get("form_user_zip_text").toString());
	    	    
	    if(matcherFName.matches() || 
	    		matcherLname.matches() || 
	    		matcherPhone.matches() || 
	    		matcherEmail.matches() ||
	    		matcherAddOne.matches() || 
	    		matcherAddTwo.matches() ||
	    		matcherCity.matches() || 
	    		matcherState.matches() ||
	    		matcherZip.matches()){
	    	
	    	//System.out.println("regex true section");
	    	return false;
	    }
	    else 
	    {
	    	map_result.put("userStatus", "HTML Injection");
	    	return true;
	    }		
	}
	
	public boolean validateZip(Map<String,Object>objectAsMap , Map<String,String> map_result)
	{
		Pattern patternPhZip = Pattern.compile("[0-9]+");	     
	    Matcher matcherZip = patternPhZip.matcher(objectAsMap.get("form_user_zip_text").toString());
	     if(matcherZip.matches())
	     {
	    	 return true;
	     }
	     else 
	     {
	    	 map_result.put("userStatus", "InvalidZip");
	    	 return false;
	     }
	}
	public boolean validateEmail(Map<String,Object>objectAsMap, Map<String,String> map_result)
	{
		 Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
	     Matcher matcher = pattern.matcher(objectAsMap.get("form_user_email_text").toString());
	     if(matcher.matches())
	     {
	    	return true; 
	     }
	     else
	     {
	    	 map_result.put("userStatus", "InvalidEmail");
	    	 return false;
	     }
	}
	public boolean validatePhone(Map<String,Object>objectAsMap, Map<String,String> map_result)
	{
		 Pattern pattern = Pattern.compile("^(\\+?\\d{1,4}[\\s-])?(?!0+\\s+,?$)\\d{10}\\s*,?$");
	     Matcher matcher = pattern.matcher(objectAsMap.get("form_user_phone_text").toString());
	     if(matcher.matches())
	     {
	    	return true; 
	     }
	     else
	     {
	    	 map_result.put("userStatus", "InvalidPhone");
	    	 return false;
	     }
	}
	public boolean validateCategory(Map<String,Object>objectAsMap, Map<String,String> map_result)
	{
		if(objectAsMap.get("form_user_category_select").equals("Select Category"))
		{
			map_result.put("userStatus", "Select Category");
			return false;
		}
		else
		{
			return true;
		}
	}
	public boolean validateNameCityState(Map<String,Object>objectAsMap, Map<String,String> map_result)
	{
		 Pattern pattern = Pattern.compile("^[a-zA-Z]+(?:(?:\\s+|-)[a-zA-Z]+)*$");
	     Matcher matcherAddOne = pattern.matcher(objectAsMap.get("form_user_fname_text").toString());
	     Matcher matcherAddTwo = pattern.matcher(objectAsMap.get("form_user_lname_text").toString());
	     Matcher matcherCity = pattern.matcher(objectAsMap.get("form_user_city_text").toString());
	     Matcher matcherState = pattern.matcher(objectAsMap.get("form_user_state_text").toString());
	     if(matcherAddOne.matches() && matcherAddTwo.matches() && matcherCity.matches() && matcherState.matches())
	     {
	    	 return true;
	     }
	     else
	     {
	    	 map_result.put("userStatus", "Invalid Name Or City Or State");
	    	 return false;
	     }
	} 
	
	public void goForOrder(Map<String,Object> objectAsMap, Map<String,String> map_result)
	{
		/*this is here because we don't need to trim the ph no. came from request, each time!! */						
		TrimedPhNum = trimLastTenDigits(objectAsMap.get("form_user_phone_text").toString());
		PreparedStatement psAddress = null;
		ResultSet rsAddress = null;
		Connection con = null;
		try
		{
			con = DbConnection.getConnection();						
			psAddress = con.prepareStatement("select * from address;");
			rsAddress = psAddress.executeQuery();
			//System.out.println("rs_address is "+rs_address);
			
			createOrder(rsAddress, objectAsMap, map_result);				
		}	
		catch(Exception e)
		{
			map_result.put("error_code", "502");
		}
		finally
		{
			DbUtils.closeUtil(rsAddress);
			DbUtils.closeUtil(psAddress);
			DbUtils.closeUtil(con);		
		}
	}	
	public void createOrder(ResultSet rsAddress, Map<String,Object> objectAsMap, Map<String,String> map_result)throws Exception
	{
		////System.out.println("create Order called");
		if(checkZipCode(rsAddress, objectAsMap))
		{
			////System.out.println("ckeckZipCode ture");
			rsAddress.beforeFirst();
			if(checkAddress(rsAddress, objectAsMap))
			{
				////System.out.println("ckeckAddress ture");
				if(checkPhoneNum(objectAsMap,map_result))
				{
					//System.out.println("ckeckPhonNum ture");
					//System.out.println("user already Exist");
					map_result.put("userStatus", "userAlreadyExist");					
				}
				else
				{
					//System.out.println("ckeckphoneNum false");
					//System.out.println("user not Exist1");
					map_result.put("userStatus", "userNotExist");
					createUser(objectAsMap, map_result);							
				}				
				//createUser(addressId);							
			}
			else
			{
				//System.out.println("user not Exist2");
				////System.out.println("ckeckAddress false");
				map_result.put("userStatus", "userNotExist");
				createUser(objectAsMap, map_result);								
			}
		}
		else
		{
			////System.out.println("ckeckZipCode false");
			//System.out.println("user not Exist3");
			map_result.put("userStatus", "userNotExist");
			createUser(objectAsMap, map_result);
		}
		
	}
	
	public boolean checkZipCode(ResultSet rs_address, Map<String,Object> objectAsMap) throws SQLException
	{
		while(rs_address.next())
		{			
			if(objectAsMap.get("form_user_zip_text").equals(rs_address.getString("zip")))
			{				
				return true;	
			}			
		}
		return false;
	}
	public boolean checkAddress(ResultSet rsAddress, Map<String,Object> objectAsMap)throws SQLException
	{
		while(rsAddress.next())
		{
			////System.out.println("hello");
			int lineOne = addressProbability(objectAsMap.get("form_user_add1_text").toString(), rsAddress.getString("address_line_one"));
			int lineTwo = addressProbability(objectAsMap.get("form_user_add2_text").toString(), rsAddress.getString("address_line_two"));
			////System.out.println(lineOne+" "+lineTwo);
			if(lineOne<=4 && lineTwo<=4)
			{						
				addressIdSet.add(rsAddress.getInt("id"));				
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
	
	public boolean checkPhoneNum(Map<String,Object> objectAsMap, Map<String,String> map_result){		
		PreparedStatement psUser = null;
		ResultSet rsUser = null;
		Connection con = DbConnection.getConnection();
		Iterator<Integer> addIdIterator = addressIdSet.iterator();	
		try{
			while(addIdIterator.hasNext()){
				int addId = addIdIterator.next();
				//System.out.println("addId Data came here 1 "+addId);			
					//System.out.println("came to try 1");
					psUser = con.prepareStatement("select phone from user where id_address="+addId);
					rsUser = psUser.executeQuery();
					//System.out.println("came to under try");
					rsUser.next();	
					//System.out.println("The trimed ph. no. is "+TrimedPhNum);
					if(rsUser.getString("phone").endsWith(TrimedPhNum)){				
						//System.out.println("in phone no. true section");
						return true;
					}
			}
		}
		catch(Exception e){
			map_result.put("error_code", "502");
		}
		finally{
			DbUtils.closeUtil(rsUser);
			DbUtils.closeUtil(psUser);
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
	public int addressProbability(String formString, String tableString)
	{
		if (formString == null || tableString == null) {
	          throw new IllegalArgumentException("Strings must not be null");
	      }	     

	      int n = formString.length(); // length of s
	      int m = tableString.length(); // length of t

	      if (n == 0) {
	          return m;
	      } else if (m == 0) {
	          return n;
	      }

	      if (n > m) {
	          // swap the input strings to consume less memory
	          String tmp = formString;
	          formString = tableString;
	          tableString = tmp;
	          n = m;
	          m = tableString.length();
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
	          t_j = tableString.charAt(j-1);
	          d[0] = j;

	          for (i=1; i<=n; i++) {
	              cost = formString.charAt(i-1)==t_j ? 0 : 1;
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
	public void createUser(Map<String,Object> objectAsMap, Map<String,String> map_result)
	{		
//		Connection con = DbConnection.getConnection();
//		
//		System.out.println("in create user function");
//		PreparedStatement psUser = null;
//		try
//		{
//			con.setAutoCommit(false);
//			String columnsUser="id,id_user_category,name_first,name_last,emailid,phone";
//			String tableUser="user";
//			String parametersUser="NULL,?,?,?,?,?";
//			
//			psUser = con.prepareStatement("insert into "+tableUser+"("+columnsUser+") values("+parametersUser+");");			
//			psUser.setString(1,objectAsMap.get("form_user_category_select"));
//			psUser.setString(2,objectAsMap.get("form_user_fname_text"));
//			psUser.setString(3,objectAsMap.get("form_user_lname_text"));
//			psUser.setString(4,objectAsMap.get("form_user_email_text"));
//			psUser.setString(5,objectAsMap.get("form_user_phone_text"));
//			
//			psUser.execute();
//			System.out.println("first checkpoint is clear");
//			
//			PreparedStatement psUserId = con.prepareStatement("select LAST_INSERT_ID()");
//			ResultSet rsUserId = psUserId.executeQuery();
//			rsUserId.next();
//			int lastInsertedUserId =rsUserId.getInt(1);
//			System.out.println("second check point clear and user_id= "+lastInsertedUserId);
//			
//			String columnsAddress="id,id_user,address_line_one,address_line_two,city,state,zip";
//			String tableAddress="address";
//			String parametersAddress="NULL,?,?,?,?,?,?";
//			
//			PreparedStatement psAddress = con.prepareStatement("insert into "+tableAddress+" ("+columnsAddress+") values("+parametersAddress+");");
//			psAddress.setInt(1, lastInsertedUserId);
//			psAddress.setString(2, objectAsMap.get("form_user_add1_text"));
//			psAddress.setString(3, objectAsMap.get("form_user_add2_text"));
//			psAddress.setString(4, objectAsMap.get("form_user_city_text"));
//			psAddress.setString(5, objectAsMap.get("form_user_state_text"));
//			psAddress.setString(6, objectAsMap.get("form_user_zip_text"));
//			
//			psAddress.execute();
//			
//			System.out.println("Third checkpoint clear ");
//			
//			//assign foreign key to user table of primary key of last inserted address
//			String columnsAddressId="id_address";
//			String tableAddressId="user";
//			String parametersAddressId="LAST_INSERT_ID()";
//			String conditionAddressId="where id=?";
//			
//			PreparedStatement psUserAddress = con.prepareStatement("update "+tableAddressId+" set "+columnsAddressId+"="+parametersAddressId+""+conditionAddressId+"");
//			psUserAddress.setInt(1, lastInsertedUserId);
//			psUserAddress.execute();
//			
//			System.out.println("go from here 1");
//			
//			//assign last inserted user id to the table according to the drop-down user category table.				
//			PreparedStatement psUserCatName = con.prepareStatement("select * from user_category where id=?");
//			
//			
//			psUserCatName.setInt(1, Integer.parseInt(objectAsMap.get("form_user_category_select")));
//			ResultSet rsUserCatName = psUserCatName.executeQuery();
//			System.out.println("go from here 2");
//			//get name of the table
//			rsUserCatName.next();		
//			
//			System.out.println(".............insert into "+rsUserCatName.getString("name")+"(id_user) values("+lastInsertedUserId+".........);/");
//			String columnsUserCat = "id_user";
//			String tableUserCat = rsUserCatName.getString("name");
//			String parametersUserCat= "?";		
//			
//			PreparedStatement psUserCat = con.prepareStatement("insert into "+tableUserCat+"("+columnsUserCat+") values("+parametersUserCat+");");		//
//			psUserCat.setInt(1, lastInsertedUserId);
//			psUserCat.execute();
//			con.commit();
//			System.out.println("Forth checkpoint clear");	
//		}
//		catch(Exception e){
//			e.printStackTrace();
//			//map_result.put("error_code", "502");
//		}
//		finally{
//			DbUtils.closeUtil(psUser);			
//		}
	}
    
    
    public Boolean verifyPasswordComplexity(String name_user, String password) {
        
        return true;
        
    }	

    public String updateUser(BeanUser bean_user) {
        
        return null;
        
    }	

    public String deleteUser(BeanUser bean_user) {
        
        return null;
        
    }	

    public String getUser(Object identifier) {
        
        return null;
        
    }	


 }