package rest.sellerapp.controller.user;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import rest.sellerapp.bean.user.UserCreateFormBean;

@Path("/user/create")
public class UserCreateFormController 
{
	@POST
	@Path("/x")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String submitUserCreateFormData(UserCreateFormBean userFormBean)
	{			
		Map<String, String> map = new HashMap<String,String>();
		
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
		
		if(validateOrder(map))
		{
			//System.out.println("In validate order true");
			goForOrder(map);
		}
			return new Gson().toJson(map);		
	}
	public boolean validateOrder(Map<String,String> map)
	{
		/*System.out.println("validateZip "+validateZip(map));
		System.out.println("validateEmail "+validateEmail(map));
		System.out.println("validatePhone "+validatePhone(map));
		System.out.println("validateNameCityState "+validateNameCityState(map)); */
				
		if(validateEmptyFields(map) && validateHtmlInjection(map) && validateZip(map) && validateEmail(map) && validatePhone(map) && validateCategory(map) && validateNameCityState(map))		
			return true;		
		else 
			return false;
	}
	
	public boolean validateEmptyFields(Map<String,String> map)
	{
		if(map.get("form_user_fname_text").isEmpty() || map.get("form_user_lname_text").isEmpty() || map.get("form_user_category_select").isEmpty() ||  map.get("form_user_phone_text").isEmpty() || map.get("form_user_email_text").isEmpty() || map.get("form_user_add1_text").isEmpty() || map.get("form_user_add2_text").isEmpty() || map.get("form_user_city_text").isEmpty() || map.get("form_user_state_text").isEmpty() ||map.get("form_user_zip_text").isEmpty())
		{
			return false;
		}
		else
		{
			return true;
		}
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
	    	
	    	System.out.println("regex true section");
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
		TrimedPhNum = trimLastTenDigits(map.get("form_user_phone_text"));
		try
		{
			con = pkg_db.DbConnection.getConnection();				
			PreparedStatement ps_address = con.prepareStatement("select * from address;");
			ResultSet rs_address = ps_address.executeQuery();
			//System.out.println("rs_address is "+rs_address);
			
			createOrder(rs_address, map);				
		}	
		catch(Exception e)
		{
			System.out.println(e);
		}
	}	
	public void createOrder(ResultSet rs_address, Map<String,String> map)throws Exception
	{
		//System.out.println("create Order called");
		if(checkZipCode(rs_address, map))
		{
			//System.out.println("ckeckZipCode ture");
			rs_address.beforeFirst();
			if(checkAddress(rs_address, map))
			{
				//System.out.println("ckeckAddress ture");
				if(checkPhoneNum())
				{
					//System.out.println("ckeckPhonNum ture");
					System.out.println("user not Exist");
					map.put("userStatus", "userExist");
					out.write(gs.toJson(map));
				}
				else
				{
					//System.out.println("ckeckphoneNum false");
					System.out.println("user not Exist1");
					map.put("userStatus", "userNotExist");
					createUser(map);
					out.write(gs.toJson(map));					
				}				
				//createUser(addressId);							
			}
			else
			{
				System.out.println("user not Exist2");
				//System.out.println("ckeckAddress false");
				map.put("userStatus", "userNotExist");
				createUser(map);
				out.write(gs.toJson(map));
				
			}
		}
		else
		{
			//System.out.println("ckeckZipCode false");
			System.out.println("user not Exist3");
			map.put("userStatus", "userNotExist");
			createUser(map);
			out.write(gs.toJson(map));
			
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
	public boolean checkAddress(ResultSet rs_address, Map<String,String> map)throws SQLException
	{
		while(rs_address.next())
		{
			//System.out.println("hello");
			int lineOne = addressProbability(map.get("form_user_add1_text"), rs_address.getString("address_line_one"));
			int lineTwo = addressProbability(map.get("form_user_add2_text"), rs_address.getString("address_line_two"));
			//System.out.println(lineOne+" "+lineTwo);
			if(lineOne<=4 && lineTwo<=4)
			{				
				
				addressIdSet.add(rs_address.getInt("id"));				
			}
			
		}
		//System.out.println("size of list is "+addressIdSet.size());
		if(addressIdSet.size()>0)
		{
			return true;
		}
		else
			return false;
	}
	public boolean checkPhoneNum()throws SQLException
	{		
		Iterator<Integer> addIdIterator = addressIdSet.iterator();		
		while(addIdIterator.hasNext())
		{
			int addId = addIdIterator.next();
			//System.out.println("addId Data "+addId);
			PreparedStatement ps_user = con.prepareStatement("select phone from user where id_address="+addId);
			ResultSet rs_user = ps_user.executeQuery();
			rs_user.next();	
			//System.out.println("The trimed ph. no. is "+TrimedPhNum);
			if(rs_user.getString("phone").endsWith(TrimedPhNum))
			{				
				//System.out.println("in phone no. true section");
				return true;
			}
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
}
