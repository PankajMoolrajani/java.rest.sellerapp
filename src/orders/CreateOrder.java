package orders;

import java.util.HashMap;
import java.util.Map;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;


@Path ("/order")
public class CreateOrder {

	@GET
	@Path ("/create")
	//@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	
	public String create_order_test(){
		System.out.println("order create");
		Map<String, String> map = new HashMap<String,String>();
		//map.put("user_id", order_bean.getUser_id());
		//map.put("user_name", order_bean.getUser_name());
		//map.put("product_id", order_bean.getProduct_id());
		
		map.put("user_id", "123");
		map.put("user_name", "pankaj");
		map.put("product_id", "2w13");
		
		return new Gson().toJson(map);	
		
		
	}
}
