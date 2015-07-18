package rest.sellerapp.controller.user;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/user")
public class User 
{
	@GET
	@Path("/user_table")
	@Produces(MediaType.TEXT_PLAIN)
	public String getUserTable()
	{
		return "Hello";
	}
}
