package search_example;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

@Path("/test")
public class SearchServer {
	@POST
	@Path("/get")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String getSearch(SearchBean bean_search){
		System.out.println("called web service");		
		System.out.println(bean_search.getTable_name());
		System.out.println(bean_search.getColumns());
		System.out.println(((ConditionClass)bean_search.getCondition()).getAnd());
		return "{}";
	}	
		
}
