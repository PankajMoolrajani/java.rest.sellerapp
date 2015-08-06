package user;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;

public class TestUser
{
	public static void main(String ar[])
	{
		Map<String,Object> ob = new HashMap<String,Object>();
		
		BeanUser b1 = new BeanUser();
		b1.setCity("alwar");
		b1.setNameFirst("amit");
		b1.setNameLast("sharma");
		
		BeanUserCategory b2 = new BeanUserCategory();
		b2.setId(2);
		b2.setName("supplier");
		b2.setDescription("desc");
		
		ob.put("userData", b1);
		ob.put("userCategoryData", b2);
		
		Gson gson = new Gson();
		System.out.println(gson.toJson(ob));
	}
}
