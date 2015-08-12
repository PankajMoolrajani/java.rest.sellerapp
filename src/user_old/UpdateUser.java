package user_old;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import db.DbConnection;
import db.DbUtils;


public class UpdateUser 
{
	
	public String updateUser(BeanUser BeanUser)
	{				
		Map<String,String> map = new HashMap<String,String>();		
		createUser(map,BeanUser);		
		return "updation process done";
	}
	
	public void createUser(Map<String,String> map, BeanUser beanUser)
	{
		Connection con = DbConnection.getConnection();			
		PreparedStatement ps = null;		
		try
		{
			con.setAutoCommit(false);
			String columnsUser="id_user_category=?,name_first=?,name_last=?,emailid=?,phone=?";
			String tableUser="user";			
			String conditionUser = "id=?";
			ps = con.prepareStatement("UPDATE "+tableUser+" SET "+columnsUser+" WHERE "+conditionUser);	
			
			ps.setInt(1,beanUser.getUserCatId());
			ps.setString(2,beanUser.getFirstName());
			ps.setString(3,beanUser.getLastName());
			ps.setString(4,beanUser.getEmailId());
			ps.setString(5,beanUser.getPhoneNumber());
			ps.setInt(6,beanUser.getUserId());			
			ps.execute();			
			
			//get address id to update address
			String columns_address="id_address";
			String table_address="user";			
			String condition_address = "id=?";
			ps = con.prepareStatement("SELECT "+columns_address+" FROM "+table_address+" WHERE "+condition_address);
			ps.setInt(1, beanUser.getUserId());
			ResultSet rs = ps.executeQuery();
			rs.next();
			int address_id = rs.getInt("id_address");
			
			//update address table			
			String table_address_update = "address";
			String columns_address_update = "address_line_one=?,address_line_two=?,city=?,state=?,zip=?";
			String condition_address_update = "id=?";
			String query_address_update = "UPDATE "+table_address_update+" SET "+columns_address_update+" WHERE "+condition_address_update;
			ps = con.prepareStatement(query_address_update);			
			ps.setString(1, beanUser.getAddLineOne());
			ps.setString(2, beanUser.getAddLineTwo());
			ps.setString(3, beanUser.getCity());
			ps.setString(4, beanUser.getState());
			ps.setString(5, beanUser.getZip());
			ps.setInt(6, address_id);
			ps.executeUpdate();
			con.commit();
		}
		catch(Exception e){			
			e.printStackTrace();
			//map.put("error_code", "502");
		}
		finally{
			DbUtils.closeUtil(ps);			
		}
	}
}
