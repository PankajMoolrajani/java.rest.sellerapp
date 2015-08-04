package user_old;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import db.DbConnection;
import db.DbUtils;


@Path("/user/create-user")
public class CreateUser
{
	Set<Integer> addressIdSet=new HashSet<Integer>();
	String TrimedPhNum ;	
	
	@POST
	@Path("/single")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String createSingleUser(BeanUser userFormBean){		
		Map<String, String> map = new HashMap<String,String>();
		map.put("error_code", "");
		
		map.put("form_user_fname_text",userFormBean.getFirstName().trim());
		map.put("form_user_lname_text",userFormBean.getLastName().trim());
		map.put("form_user_category_select",userFormBean.getUserCategory().trim());
		map.put("form_user_phone_text",userFormBean.getPhoneNumber().trim());
		map.put("form_user_email_text",userFormBean.getEmailId().trim());
		map.put("form_user_add1_text",userFormBean.getAddLineOne().trim());
		map.put("form_user_add2_text",userFormBean.getAddLineTwo().trim());
		map.put("form_user_city_text",userFormBean.getCity().trim());
		map.put("form_user_state_text",userFormBean.getState().trim());
		map.put("form_user_zip_text",userFormBean.getZip().trim());
		
		if(validateOrder(map)){			
			//System.out.println("In validate order true");
			goForOrder(map);
		}
		System.out.println("validate order not done");
		map.remove("form_user_fname_text");
		map.remove("form_user_lname_text");
		map.remove("form_user_category_select");
		map.remove("form_user_phone_text");
		map.remove("form_user_phone_text");
		map.remove("form_user_email_text");
		map.remove("form_user_add1_text");
		map.remove("form_user_add2_text");
		map.remove("form_user_city_text");
		map.remove("form_user_state_text");
		map.remove("form_user_zip_text");
		
		return new Gson().toJson(map);		
	}
	
	public boolean validateOrder(Map<String,String> map)
	{
//		System.out.println("validateZip "+validateZip(map));
//		System.out.println("validateEmail "+validateEmail(map));
//		System.out.println("validatePhone "+validatePhone(map));
//		System.out.println("validateNameCityState "+validateNameCityState(map));
				
		if(validateEmptyFields(map) && validateHtmlInjection(map) && validateZip(map) && validateEmail(map) && validatePhone(map) && validateCategory(map) && validateNameCityState(map))		
		{
			return true;		
		}
		else
		{
			map.put("error_code", "505");
			return false;
		}		
	}
	
	public boolean validateEmptyFields(Map<String,String> map)
	{
		if(map.get("form_user_fname_text").isEmpty() || map.get("form_user_lname_text").isEmpty() || map.get("form_user_category_select").isEmpty() ||  map.get("form_user_phone_text").isEmpty() || map.get("form_user_email_text").isEmpty() || map.get("form_user_add1_text").isEmpty() || map.get("form_user_add2_text").isEmpty() || map.get("form_user_city_text").isEmpty() || map.get("form_user_state_text").isEmpty() ||map.get("form_user_zip_text").isEmpty())
			return false;		
		else
			return true;		
	}
	
	private boolean validateHtmlInjection(Map<String,String>map)
	{
		Pattern pattern = Pattern.compile("<(\"[^\"]*\"|'[^']*'|[^'\">])*>");
		
	    Matcher matcherFName = pattern.matcher(map.get("form_user_fname_text"));
	    Matcher matcherLname = pattern.matcher(map.get("form_user_lname_text"));	    
		Matcher matcherPhone = pattern.matcher(map.get("form_user_phone_text"));
	    Matcher matcherEmail = pattern.matcher(map.get("form_user_email_text"));
		Matcher matcherAddOne = pattern.matcher(map.get("form_user_add1_text"));
	    Matcher matcherAddTwo = pattern.matcher(map.get("form_user_add2_text"));
	    Matcher matcherCity = pattern.matcher(map.get("form_user_city_text"));
	    Matcher matcherState = pattern.matcher(map.get("form_user_state_text"));
	    Matcher matcherZip = pattern.matcher(map.get("form_user_zip_text"));
	    	    
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
	    	map.put("userStatus", "HTML Injection");
	    	return true;
	    }		
	}
	
	public boolean validateZip(Map<String,String>map)
	{
		Pattern patternPhZip = Pattern.compile("[0-9]+");	     
	    Matcher matcherZip = patternPhZip.matcher(map.get("form_user_zip_text"));
	     if(matcherZip.matches())
	     {
	    	 return true;
	     }
	     else 
	     {
	    	 map.put("userStatus", "InvalidZip");
	    	 return false;
	     }
	}
	public boolean validateEmail(Map<String,String>map)
	{
		 Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
	     Matcher matcher = pattern.matcher(map.get("form_user_email_text"));
	     if(matcher.matches())
	     {
	    	return true; 
	     }
	     else
	     {
	    	 map.put("userStatus", "InvalidEmail");
	    	 return false;
	     }
	}
	public boolean validatePhone(Map<String,String>map)
	{
		 Pattern pattern = Pattern.compile("^(\\+?\\d{1,4}[\\s-])?(?!0+\\s+,?$)\\d{10}\\s*,?$");
	     Matcher matcher = pattern.matcher(map.get("form_user_phone_text"));
	     if(matcher.matches())
	     {
	    	return true; 
	     }
	     else
	     {
	    	 map.put("userStatus", "InvalidPhone");
	    	 return false;
	     }
	}
	public boolean validateCategory(Map<String,String>map)
	{
		if(map.get("form_user_category_select").equals("Select Category"))
		{
			map.put("userStatus", "Select Category");
			return false;
		}
		else
		{
			return true;
		}
	}
	public boolean validateNameCityState(Map<String,String>map)
	{
		 Pattern pattern = Pattern.compile("^[a-zA-Z]+(?:(?:\\s+|-)[a-zA-Z]+)*$");
	     Matcher matcherAddOne = pattern.matcher(map.get("form_user_fname_text"));
	     Matcher matcherAddTwo = pattern.matcher(map.get("form_user_lname_text"));
	     Matcher matcherCity = pattern.matcher(map.get("form_user_city_text"));
	     Matcher matcherState = pattern.matcher(map.get("form_user_state_text"));
	     if(matcherAddOne.matches() && matcherAddTwo.matches() && matcherCity.matches() && matcherState.matches())
	     {
	    	 return true;
	     }
	     else
	     {
	    	 map.put("userStatus", "Invalid Name Or City Or State");
	    	 return false;
	     }
	} 
	
	public void goForOrder(Map<String,String> map)
	{
		/*this is here because we don't need to trim the ph no. came from request, each time!! */						
		TrimedPhNum = trimLastTenDigits(map.get("form_user_phone_text"));
		PreparedStatement psAddress = null;
		ResultSet rsAddress = null;
		Connection con = null;
		try
		{
			con = DbConnection.getConnection();						
			psAddress = con.prepareStatement("select * from address;");
			rsAddress = psAddress.executeQuery();
			//System.out.println("rs_address is "+rs_address);
			
			createOrder(rsAddress, map);				
		}	
		catch(Exception e)
		{
			map.put("error_code", "502");
		}
		finally
		{
			DbUtils.closeUtil(rsAddress);
			DbUtils.closeUtil(psAddress);
			DbUtils.closeUtil(con);		
		}
	}	
	public void createOrder(ResultSet rsAddress, Map<String,String> map)throws Exception
	{
		////System.out.println("create Order called");
		if(checkZipCode(rsAddress, map))
		{
			////System.out.println("ckeckZipCode ture");
			rsAddress.beforeFirst();
			if(checkAddress(rsAddress, map))
			{
				////System.out.println("ckeckAddress ture");
				if(checkPhoneNum(map))
				{
					//System.out.println("ckeckPhonNum ture");
					//System.out.println("user already Exist");
					map.put("userStatus", "userAlreadyExist");					
				}
				else
				{
					//System.out.println("ckeckphoneNum false");
					//System.out.println("user not Exist1");
					map.put("userStatus", "userNotExist");
					createUser(map);							
				}				
				//createUser(addressId);							
			}
			else
			{
				//System.out.println("user not Exist2");
				////System.out.println("ckeckAddress false");
				map.put("userStatus", "userNotExist");
				createUser(map);								
			}
		}
		else
		{
			////System.out.println("ckeckZipCode false");
			//System.out.println("user not Exist3");
			map.put("userStatus", "userNotExist");
			createUser(map);
		}
		
	}
	
	public boolean checkZipCode(ResultSet rs_address, Map<String,String> map) throws SQLException
	{
		while(rs_address.next())
		{			
			if(map.get("form_user_zip_text").equals(rs_address.getString("zip")))
			{				
				return true;	
			}			
		}
		return false;
	}
	public boolean checkAddress(ResultSet rsAddress, Map<String,String> map)throws SQLException
	{
		while(rsAddress.next())
		{
			////System.out.println("hello");
			int lineOne = addressProbability(map.get("form_user_add1_text"), rsAddress.getString("address_line_one"));
			int lineTwo = addressProbability(map.get("form_user_add2_text"), rsAddress.getString("address_line_two"));
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
	
	public boolean checkPhoneNum(Map<String,String> map){		
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
			map.put("error_code", "502");
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
	public void createUser(Map<String,String> map)
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
//			psUser.setString(1,map.get("form_user_category_select"));
//			psUser.setString(2,map.get("form_user_fname_text"));
//			psUser.setString(3,map.get("form_user_lname_text"));
//			psUser.setString(4,map.get("form_user_email_text"));
//			psUser.setString(5,map.get("form_user_phone_text"));
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
//			psAddress.setString(2, map.get("form_user_add1_text"));
//			psAddress.setString(3, map.get("form_user_add2_text"));
//			psAddress.setString(4, map.get("form_user_city_text"));
//			psAddress.setString(5, map.get("form_user_state_text"));
//			psAddress.setString(6, map.get("form_user_zip_text"));
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
//			psUserCatName.setInt(1, Integer.parseInt(map.get("form_user_category_select")));
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
//			//map.put("error_code", "502");
//		}
//		finally{
//			DbUtils.closeUtil(psUser);			
//		}
	}

}
