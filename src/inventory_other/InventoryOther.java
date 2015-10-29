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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
		
		int id_existed_stock = 0;
		int id_new_stock = 0;
		
		int id_existed_price = 0;
		int id_new_price = 0;
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
		//System.out.println(map_inventory_bean);
		while(key_iterator.hasNext()){
			String key = key_iterator.next();
				//System.out.println(key+" "+map_inventory_bean.get(key));
			if(map_inventory_bean.get(key) == null){        		
				key_iterator.remove();        		
			}        	
		} 		
		Iterator<String> key_iterator2 = keys.iterator();
		while(key_iterator2.hasNext()){
			String key = key_iterator2.next();
			//System.out.println(key+" "+map_inventory_bean.get(key));			
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
       System.out.println(is_stock_update+" "+is_category_update);
       Map<String,String> map_inventory_db = new HashMap<String,String>();
       map_inventory_db.put("name", "name");
       map_inventory_db.put("sku", "sku");
       map_inventory_db.put("statusListing", "status_listing");
       map_inventory_db.put("iamgeDir", "image_dir");
       map_inventory_db.put("idCategory","id_category");
       
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
 		 		
 		map_price_db.put("idCategory", "id_category");
 		
 		//get id's of foreign keys in inventory
 		Map<String,Integer> map_inventory_ids = new HashMap<String,Integer>();
        Connection con = DbConnection.getConnection();
        String table_inventory_ids = "inventory";
        String columns_inventory_ids = "id_category,id_stock,id_price,tag_ids";
        String condition_inventory_ids = "id=?";
        String query_inventory_ids = "SELECT "+columns_inventory_ids+" FROM "+table_inventory_ids+" WHERE "+condition_inventory_ids; 			
        PreparedStatement ps_inventory_ids;
		try {			
			ps_inventory_ids = con.prepareStatement(query_inventory_ids);
			ps_inventory_ids.setInt(1, (Integer)map_inventory_bean.get("id"));
			ResultSet rs_inventory_ids = ps_inventory_ids.executeQuery();
			rs_inventory_ids.next();
			
			map_inventory_ids.put("id_category", rs_inventory_ids.getInt("id_category"));
			map_inventory_ids.put("id_stock", rs_inventory_ids.getInt("id_stock"));
			map_inventory_ids.put("id_price", rs_inventory_ids.getInt("id_price"));
			map_inventory_ids.put("tag_ids", rs_inventory_ids.getInt("tag_ids"));			
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}        
	//flag -1
//        if(is_stock_update == true){
//        	System.out.println("is_stock_update is true");
// 			String table = "stock";
// 			String columns = "*";
// 			String condition = "incoming=? AND available=? AND outgoing=?";
// 			
//			 	
// 			
// 			try{ 				 				 			
// 				con.setAutoCommit(false);
// 				//condition = condition.substring(0, condition.length()-1);
// 				
// 				//check if stock pair with new parameters is already exist 				
// 				String query_check_exist = "SELECT "+columns+" FROM "+table+" WHERE "+condition;
// 				System.out.println(query_check_exist);
// 				PreparedStatement ps_check_exist = con.prepareStatement(query_check_exist);
// 				ps_check_exist.setInt(1, (Integer)map_inventory_bean.get("incoming"));
// 				ps_check_exist.setInt(2, (Integer)map_inventory_bean.get("available"));
// 				ps_check_exist.setInt(3, (Integer)map_inventory_bean.get("outgoing"));
// 				
// 				ResultSet rs_check_exist = ps_check_exist.executeQuery();
// 				boolean is_stock_exist = rs_check_exist.next();
// 				
// 				if(!is_stock_exist){
// 					int id = 0;
// 		 			String parameters_new_stock = "?,?,?,?,?,?,?,?";
// 		 			
//// 		 			//String condition = "id"; 			
//// 		 			for(String key : keys){
//// 		 				if(map_stock_db.containsKey(key)){
//// 		 					parameters_new_stock = parameters_new_stock + map_stock_db.get(key)+" = "+map_inventory_bean.get(key)+",";
//// 		        		}
//// 		 			}     
// 		 			String query = "INSERT INTO "+table+" VALUES ("+parameters_new_stock+")"; 		
// 		 			System.out.println(query);
// 					PreparedStatement ps_new_stock = con.prepareStatement(query , Statement.RETURN_GENERATED_KEYS);
// 					ps_new_stock.setInt(1, 0);
// 					ps_new_stock.setInt(2, (Integer)map_inventory_bean.get("available"));
// 					ps_new_stock.setInt(3, (Integer)map_inventory_bean.get("incoming"));
// 					ps_new_stock.setInt(4, (Integer)map_inventory_bean.get("outgoing")); 					
// 					ps_new_stock.setString(5, null);
// 					ps_new_stock.setString(6, null);
// 					ps_new_stock.setString(7, null);
// 					ps_new_stock.setString(8, null); 					
// 					int rows_affected = ps_new_stock.executeUpdate();
// 					
// 					if (rows_affected != 0){ 						
// 						ResultSet rs = ps_new_stock.getGeneratedKeys();
// 						
// 						if (rs.next()){
// 						
// 							id_new_stock = rs.getInt(1);
// 							
// 						}    			
// 					}
// 					con.commit();
// 					System.out.println("this is id: "+id_new_stock);
// 				}else{
// 					id_existed_stock = rs_check_exist.getInt("id");
// 					
// 				} 				
// 			}catch(SQLException e){
// 				System.out.println("error in stock update");
// 				e.printStackTrace();
//        	}
// 		}
//     //flag-2   
//        if(is_price_update == true){
//        	String table = "price";
// 			String columns = "*";
// 			String condition = "price_sell=? AND price_mrp=?";
// 			 			
// 			try{ 				 				 			
// 				con.setAutoCommit(false);
// 				//condition = condition.substring(0, condition.length()-1);
// 				
// 				//check if stock pair with new parameters is already exist 				
// 				String query_check_exist = "SELECT "+columns+" FROM "+table+" WHERE "+condition;
// 				System.out.println(query_check_exist);
// 				PreparedStatement ps_check_exist = con.prepareStatement(query_check_exist);
// 				ps_check_exist.setInt(1, (Integer)map_inventory_bean.get("priceSell"));
// 				ps_check_exist.setInt(2, (Integer)map_inventory_bean.get("priceMrp")); 				
// 				
// 				ResultSet rs_check_exist = ps_check_exist.executeQuery();
// 				boolean is_price_exist = rs_check_exist.next();
// 				
// 				if(!is_price_exist){
// 					int id = 0;
// 		 			String parameters_new_stock = "?,?,?";
// 		 			
//// 		 			//String condition = "id"; 			
//// 		 			for(String key : keys){
//// 		 				if(map_stock_db.containsKey(key)){
//// 		 					parameters_new_stock = parameters_new_stock + map_stock_db.get(key)+" = "+map_inventory_bean.get(key)+",";
//// 		        		}
//// 		 			}     
// 		 			String query = "INSERT INTO "+table+" VALUES ("+parameters_new_stock+")"; 		
// 		 			System.out.println(query);
// 					PreparedStatement ps_new_stock = con.prepareStatement(query , Statement.RETURN_GENERATED_KEYS);
// 					ps_new_stock.setInt(1, 0);
// 					ps_new_stock.setInt(2, (Integer)map_inventory_bean.get("priceSell"));
// 					ps_new_stock.setInt(3, (Integer)map_inventory_bean.get("priceMrp")); 								 					
// 					
// 					int rows_affected = ps_new_stock.executeUpdate();
// 					
// 					if (rows_affected != 0){ 						
// 						ResultSet rs = ps_new_stock.getGeneratedKeys();
// 						
// 						if (rs.next()){
// 						
// 							id_new_price = rs.getInt(1);
// 							
// 						}    			
// 					}
// 					con.commit();
// 					System.out.println("this is id: "+id_new_price);
// 				}else{
// 					id_existed_price = rs_check_exist.getInt("id");
// 					
// 				} 				
// 			}catch(SQLException e){
// 				System.out.println("error in stock update");
// 				e.printStackTrace();
//        	}
// 		}
        
        try{
        	String table_update_inventory = "";
        	String parameter_update_inventory = "id_stock=? AND id_price=? AND ";
        	String condition_update_inventory = "";
        	
	 		for(String key : keys){	 	
	 			
				if(map_inventory_db.containsKey(key)){
					//id_category is coming from request, if it is zero then not concedered
					if(map_inventory_db.get(key).equals("id_category")){
						//System.out.println("here it is "+map_inventory_bean.get(key).toString().trim());
						if(!map_inventory_bean.get(key).toString().trim().equals("0")){
							//System.out.println(" * "+parameter_update_inventory);
							parameter_update_inventory = parameter_update_inventory + map_inventory_db.get(key)+" = "+map_inventory_bean.get(key)+",";
						}						
					}
					else{
						//System.out.println("i m here "+map_inventory_db.get(key)+" "+map_inventory_bean.get(key));
						parameter_update_inventory = parameter_update_inventory + map_inventory_db.get(key)+" = "+map_inventory_bean.get(key)+",";
					}					
				}
			}
	 		System.out.println(parameter_update_inventory);
	 		//query_update_inventory = "UPDATE FROM "+table_update_inventory+" SET "+
        }catch(Exception e){
        	
        }
		return "";
	}
}
