package user_old;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import db.DbConnection;
import db.DbUtils;

@Path("/user/update")
public class UpdateUser 
{
	@POST
	@Path("/form-data")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String updateUser(BeanUser BeanUser)
	{	
		Map<String,String> map = new HashMap<String,String>();
		System.out.println(BeanUser.getUserId()+" "+BeanUser.getUserCatId()+" "+BeanUser.getFirstName());
		createUser(map,BeanUser);
		
		return "updation process";
	}
	
	public void createUser(Map<String,String> map, BeanUser beanUser)
	{
		Connection con = DbConnection.getConnection();
		
		System.out.println("in create user function");
		PreparedStatement psUser = null;
		try
		{
			String columnsUser="id_user_category=?,name_first=?,name_last=?,emailid=?,phone=?";
			String tableUser="user";			
			String conditionUser = "where id=?";
			psUser = con.prepareStatement("UPDATE "+tableUser+" SET "+columnsUser+" "+conditionUser);	
			System.out.println("UPDATE "+tableUser+" SET "+columnsUser+" "+conditionUser);
			psUser.setInt(1,beanUser.getUserCatId());
			psUser.setString(2,beanUser.getFirstName());
			psUser.setString(3,beanUser.getLastName());
			psUser.setString(4,beanUser.getEmailId());
			psUser.setString(5,beanUser.getPhoneNumber());
			psUser.setInt(6,beanUser.getUserId());			
			psUser.execute();
			System.out.println("first checkpoint is clear");
			
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
//			System.out.println("Forth checkpoint clear");	
		}
		catch(Exception e){
			e.printStackTrace();
			//map.put("error_code", "502");
		}
		finally{
			DbUtils.closeUtil(psUser);			
		}
	}
}
