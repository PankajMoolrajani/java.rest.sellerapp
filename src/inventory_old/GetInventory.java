package inventory_old;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import db.DbConnection;

@Path("/inventory/get-inventory")
public class GetInventory
{
	@GET
	@Path("/all")
	@Produces(MediaType.TEXT_PLAIN)
	public String getAllInventory()
	{
		Connection con = DbConnection.getConnection();
		try
		{
			String columns_ivnentory = "";
			PreparedStatement ps = con.prepareStatement("");
		}
		catch(SQLException e){
			
		}
		return "";
	}
}
