package search;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import search.BeanSearch;

@Path("/search")

public class Search {
	
	@POST
	@Path("/get")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	
	public String getSearch(BeanSearch bean_search){
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("search","ok");
		System.out.println("search");
		System.out.println(bean_search.getColumns());
		System.out.println(bean_search.getTableName());
		System.out.println(bean_search.getMapCondition());
		return new Gson().toJson(map);
	}
	
}
