package search_example_two;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.gson.Gson;

@Path("/test-two")
public class SearchServerTwo {
	@POST
	@Path("/get")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getSearch(Map<String,Object> mapObject)throws JSONException{		
		Gson gson = new Gson();
		String jsonString = gson.toJson(mapObject);
		JSONObject jsonObject = new JSONObject(jsonString);
		JSONObject jsonCondition = (JSONObject)jsonObject.get("condition");
		JSONArray jsonArrayOr = jsonCondition.getJSONArray("or");
		System.out.println(jsonArrayOr.get(0));
		return "{}";
	}	
}
