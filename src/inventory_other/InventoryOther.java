package inventory_other;

import inventory.BeanInventory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import order.BeanOrder;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import au.com.bytecode.opencsv.CSVReader;

import com.google.gson.Gson;

import db.DbConnection;
@Path("/inventory_other")
public class InventoryOther {

	private static final long serialVersionUID = 1L;
	private String UPLOAD_DIRECTORY = null;
	
	@Context ServletContext context;
	public InventoryOther(){	
		
	}
	
	@POST
	@Path("/upload_file")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_PLAIN)
	public String upload(@Context HttpServletRequest request){		
		Map<String,String> map_result = new HashMap<String,String>();
		
		UPLOAD_DIRECTORY = (context.getRealPath("/")).toString()+"/inventory_files/";		
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);	   
		if (isMultipart) {	    	
            // Create a factory for disk-based file items
			FileItemFactory factory = new DiskFileItemFactory();

            // Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload(factory);
            try {
            	// Parse the request
            	List<FileItem> multiparts = upload.parseRequest(request);	                    
            	String fileName = multiparts.get(0).getName();	                    	                    	                  
            	
            	for (FileItem item : multiparts) {	                    	
            		if (!item.isFormField()) {	                                		
            			String name = new File(item.getName()).getName();
            			item.write(new File(UPLOAD_DIRECTORY + File.separator + name));
            		}
            	}
            	uploadInventory(UPLOAD_DIRECTORY+fileName);
            } 
            catch (Exception e) 
            {
            	map_result.put("response_message", "failed");							
				map_result.put("response_code", "3002");
				return new Gson().toJson(map_result);            	
            }           
		}
		map_result.put("response_message", "success");
		map_result.put("response_code", "2000");     		
		return new Gson().toJson(map_result);
	}
	
	private void uploadInventory(String file_path){
		System.out.println(file_path);
		CSVReader reader;					    	    
	    Map<String,BeanOrder> uniware_file_data_map = new LinkedHashMap<String,BeanOrder>(200,0.50f);	    	   
	    Set<String> order_id_set = new LinkedHashSet<String>();
	    try{
	    	 reader = new CSVReader(new FileReader(file_path));
	    	 String[] row;
	    	 int sku_Column_No =1;
	    	 int name_Column_No = 1;
	    	 int price_Column_No = 1;
	    	 int max_price_Column_No = 1;	
	    	 int stock_in_hand_Column_No = 1;		        
	    	 int type_of_inventory_Column_No = 1;		        
	    	 
	    	 Map<String,Integer> map = new HashMap<String,Integer>();	        	
	    	 row = reader.readNext();
	    	 for (int i = 0; i < row.length; i++) {
	    		 System.out.println(row[i].toString());
	    		 if(row[i].toString().equals("SKU"))
	    		 {
	    			 sku_Column_No = i;
	    			 continue;
	    		 }            	
	    		 else if(row[i].toString().equals("NAME"))
	    		 {
	    			 name_Column_No = i;      
	    			 continue;
	    		 }
	    		 else if(row[i].toString().equals("PRICE"))
	    		 {
	    			 price_Column_No=i;
	    			 continue;
	    		 }	            
	    		 else if(row[i].toString().equals("MAX PRICE"))
	    		 {
	    			 max_price_Column_No = i;
	    			 continue;
	    		 }
	    		 else if(row[i].toString().equals("STOCK IN HAND"))
	    		 {	    			 
	    			 stock_in_hand_Column_No = i;
	    			 continue;
	    		 }	
	    		 else if(row[i].toString().equals("TYPE OF INVENTORY"))
	    		 {
	    			 type_of_inventory_Column_No = i;
	    			 continue;
	    		 }	            	     
	    	 }	    		    	
	    	 List<BeanInventory> list_inventory_upload = new ArrayList<BeanInventory>();
	    	 while ((row = reader.readNext()) != null) {		    		 
	    		 String name = row[name_Column_No].toString().trim();	    		 
	    		 String sku_value = row[sku_Column_No].toString().trim();	    		 
	    		 String type_of_inventory = row[type_of_inventory_Column_No].toString().trim();
	    		 int stock_in_hand = Integer.parseInt(row[stock_in_hand_Column_No].toString().trim());	    		 
	    		 int price =  Integer.parseInt(row[price_Column_No].toString().trim());	    		 
	    		 int max_price =  Integer.parseInt(row[max_price_Column_No].toString().trim());	
	    		 
	    		 BeanInventory beanInventory =new BeanInventory();
	    		 beanInventory.setSku(sku_value);
	    		 beanInventory.setName(name);
	    		 beanInventory.setPriceMrp(max_price);
	    		 beanInventory.setPriceSell(price);
	    		
	    	 }
	    }
	    catch(Exception e){
	    	
	    }
	}
	
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String updateInventory(BeanInventory bean_inventory){
		Map<String,Object> map_inventory_bean = new HashMap<String,Object>();
		BeanInfo info = null;
		try{
			info = Introspector.getBeanInfo(bean_inventory.getClass());
			for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
				Method reader = pd.getReadMethod();			
			    if (reader != null){
			    	map_inventory_bean.put(pd.getName(),reader.invoke(bean_inventory));
			    }
			}
		}catch(IllegalArgumentException e){
			e.printStackTrace();
		}catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IntrospectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Set<String> keys = map_inventory_bean.keySet();                
		Iterator<String> key_iterator = keys.iterator();
		System.out.println(map_inventory_bean);
		while(key_iterator.hasNext()){
			String key = key_iterator.next();
			//	System.out.println(map_user_bean.get(key));
			if(map_inventory_bean.get(key) == null || map_inventory_bean.get(key).toString().equals("0")){        		
				key_iterator.remove();        		
			}        	
		} 
		boolean is_stock_update = false;
		boolean is_price_update = false;	
		boolean is_category_update = false;
		if(map_inventory_bean.containsKey("available") ||
        		map_inventory_bean.containsKey("incoming") || 
        		map_inventory_bean.containsKey("outgoing") || 
        		map_inventory_bean.containsKey("aisle") || 
        		map_inventory_bean.containsKey("row") ||
        		map_inventory_bean.containsKey("rack") || 
        		map_inventory_bean.containsKey("caseBox")){
        	is_stock_update = true;	        
        }         
        if(map_inventory_bean.containsKey("priceSell") ||
        		map_inventory_bean.containsKey("priceMrp")){
        	is_price_update = true;
        }
        if(map_inventory_bean.containsKey("idCategory")){
        	is_category_update = true;
        }
       
        Map<String,String> map_stock_db = new HashMap<String,String>();
        map_stock_db.put("available","available");
        map_stock_db.put("incoming","incoming");
        map_stock_db.put("outgoing","outgoing");
        map_stock_db.put("aisle","aisle");
        map_stock_db.put("row","row");
        map_stock_db.put("rack","rack");
        map_stock_db.put("caseBox","case_box");	
        
 		Map<String,String> map_price_db = new HashMap<String,String>();
 		map_price_db.put("nameUser", "name_user");
 		map_price_db.put("nameFirst", "name_first");
 		map_price_db.put("nameLast", "name_last");
 		map_price_db.put("emailid", "emailid");
 		map_price_db.put("phone", "phone");
 		map_price_db.put("idUserCategory","id_user_category");
 		
 		Map<String,String> map_category_db = new HashMap<String,String>();
 		map_price_db.put("idCategory", "id_category");
 		
 		//get id's of foreign keys in inventory
        Connection con = DbConnection.getConnection();
        String table_inventory = "inventory";
        String columns_inventory = "id_category,id_stock,id_price,tag_ids";
        String condition_inventory = "id=?";
        
        if(is_stock_update == true){
 			String table = "stock";
 			String parameters = "";
 			String condition = "id"; 			
 			for(String key : keys){
 				if(map_inventory_bean.containsKey(key)){
 					parameters = parameters + map_stock_db.get(key)+" = '"+map_inventory_bean.get(key)+"',";
        		}
 			}        
 			try{
 				con.setAutoCommit(false);
 				parameters = parameters.substring(0, parameters.length()-1); 				
 				String query = "UPDATE "+table+" SET "+parameters+" WHERE "+condition+"=?"; 			
 				PreparedStatement ps = con.prepareStatement(query);
 				ps.setInt(1, (Integer)map_inventory_bean.get("id"));
 				
 				ps.executeUpdate();
 				con.commit(); 		
 			}catch(SQLException e){
 				e.printStackTrace();
        	}
 		}
		return "";
	}
}
