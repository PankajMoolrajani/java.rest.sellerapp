package inventory_other;

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
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import order.BeanOrder;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
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
	@Path("/upload_images") 
	@Consumes(MediaType.MULTIPART_FORM_DATA)	
	@Produces(MediaType.TEXT_PLAIN)	
	public String testUpload(@Context HttpServletRequest request){		
		Map<String,String> map_result = new HashMap<String,String>();
		
		UPLOAD_DIRECTORY = (context.getRealPath("/")).toString()+"/inventory_images/";		
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);	   
		if (isMultipart) {
			// Create a factory for disk-based file items
            FileItemFactory factory = new DiskFileItemFactory();

            // Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload(factory);            
            // Parse the request
            List<FileItem> multiparts = null;
            try {
            	multiparts = upload.parseRequest(request);
			} catch (FileUploadException e) {
				map_result.put("response_message", "failed");
				map_result.put("response_code", "3000");
				return new Gson().toJson(map_result);
			} 
            	
			//make folder name
			String folder_name = multiparts.get(0).getName();                    
			int index_number = folder_name.indexOf("-");
			folder_name = folder_name.substring(0,index_number);
            	
			File finalDirectoryName = new File(UPLOAD_DIRECTORY+folder_name);
            	
			// if the directory does not exist, create it
				                        
			try{                        	
				finalDirectoryName.mkdir();                            
			} 
			catch(SecurityException se){
				map_result.put("response_message", "failed");
				map_result.put("response_code", "3001");
				return new Gson().toJson(map_result);            	
			}                               
			

			for (FileItem item : multiparts) {	                    	
				if (!item.isFormField()) {	                    					
					String name = new File(item.getName()).getName();
					try {
						item.write(new File(finalDirectoryName + File.separator + name));
					} catch (Exception e) {
						map_result.put("response_message", "failed");							
						map_result.put("response_code", "3002");
						return new Gson().toJson(map_result);
					}
            	}
            }		
			map_result.put("image_dir_path","/inventory_images/"+folder_name+"/");
		}		
		map_result.put("response_message", "success");
		map_result.put("response_code", "2000");     		
		return new Gson().toJson(map_result);
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
		Map <String,Object> map = new HashMap<String,Object>();
		
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
        if(is_stock_update == true){
        	System.out.println("is_stock_update is true");
 			String table = "stock";
 			String columns = "*";
 			String condition = "incoming=? AND available=? AND outgoing=?";
 			
 			try{ 				 				 			
 				con.setAutoCommit(false);
 				//condition = condition.substring(0, condition.length()-1);
 				
 				//check if stock pair with new parameters is already exist 				
 				String query_check_exist = "SELECT "+columns+" FROM "+table+" WHERE "+condition; 				
 				PreparedStatement ps_check_exist = con.prepareStatement(query_check_exist);
 				ps_check_exist.setInt(1, (Integer)map_inventory_bean.get("incoming"));
 				ps_check_exist.setInt(2, (Integer)map_inventory_bean.get("available"));
 				ps_check_exist.setInt(3, (Integer)map_inventory_bean.get("outgoing"));
 				
 				ResultSet rs_check_exist = ps_check_exist.executeQuery();
 				boolean is_stock_exist = rs_check_exist.next();
 				
 				if(!is_stock_exist){
 					int id = 0;
 		 			String parameters_new_stock = "?,?,?,?,?,?,?,?"; 		 			   
 		 			String query = "INSERT INTO "+table+" VALUES ("+parameters_new_stock+")"; 		 		 			
 					PreparedStatement ps_new_stock = con.prepareStatement(query , Statement.RETURN_GENERATED_KEYS);
 					ps_new_stock.setInt(1, 0);
 					ps_new_stock.setInt(2, (Integer)map_inventory_bean.get("available"));
 					ps_new_stock.setInt(3, (Integer)map_inventory_bean.get("incoming"));
 					ps_new_stock.setInt(4, (Integer)map_inventory_bean.get("outgoing")); 					
 					ps_new_stock.setString(5, null);
 					ps_new_stock.setString(6, null);
 					ps_new_stock.setString(7, null);
 					ps_new_stock.setString(8, null); 					
 					int rows_affected = ps_new_stock.executeUpdate();
 					
 					if (rows_affected != 0){ 						
 						ResultSet rs = ps_new_stock.getGeneratedKeys();
 						
 						if (rs.next()){
 						
 							id_new_stock = rs.getInt(1);
 							
 						}    			
 					}
 					con.commit();
 					map_inventory_ids.put("id_stock", id_new_stock); 					
 				}else{
 					id_existed_stock = rs_check_exist.getInt("id");
 					map_inventory_ids.put("id_stock", id_existed_stock);
 				} 				
 			}catch(SQLException e){ 				
 				e.printStackTrace();
 				map.put("response_code", 4000);
 				map.put("response_message", "failure: update inventory - table stock");
 				return new Gson().toJson(map);
        	}
 		}
     //flag-2   
        if(is_price_update == true){
        	String table = "price";
 			String columns = "*";
 			String condition = "price_sell=? AND price_mrp=?";
 			 			
 			try{ 				 				 			
 				con.setAutoCommit(false);
 				//condition = condition.substring(0, condition.length()-1);
 				
 				//check if stock pair with new parameters is already exist 				
 				String query_check_exist = "SELECT "+columns+" FROM "+table+" WHERE "+condition; 				
 				PreparedStatement ps_check_exist = con.prepareStatement(query_check_exist);
 				ps_check_exist.setInt(1, (Integer)map_inventory_bean.get("priceSell"));
 				ps_check_exist.setInt(2, (Integer)map_inventory_bean.get("priceMrp")); 				
 				
 				ResultSet rs_check_exist = ps_check_exist.executeQuery();
 				boolean is_price_exist = rs_check_exist.next();
 				
 				if(!is_price_exist){
 					int id = 0;
 		 			String parameters_new_stock = "?,?,?";
 		 			String query = "INSERT INTO "+table+" VALUES ("+parameters_new_stock+")"; 		
 		 			
 					PreparedStatement ps_new_stock = con.prepareStatement(query , Statement.RETURN_GENERATED_KEYS);
 					ps_new_stock.setInt(1, 0);
 					ps_new_stock.setInt(2, (Integer)map_inventory_bean.get("priceSell"));
 					ps_new_stock.setInt(3, (Integer)map_inventory_bean.get("priceMrp")); 								 					
 					
 					int rows_affected = ps_new_stock.executeUpdate();
 					
 					if (rows_affected != 0){ 						
 						ResultSet rs = ps_new_stock.getGeneratedKeys();
 						
 						if (rs.next()){
 						
 							id_new_price = rs.getInt(1);
 							
 						}    			
 					}
 					con.commit();
 					map_inventory_ids.put("id_price", id_new_stock); 					
 				}else{
 					id_existed_price = rs_check_exist.getInt("id");
 					map_inventory_ids.put("id_price", id_existed_price);
 				} 				
 			}catch(SQLException e){ 				
 				e.printStackTrace();
 				map.put("response_code", 4000);
 				map.put("response_message", "failure: update inventory - table price");
 				return new Gson().toJson(map);
        	}
 		}
        
        try{
        	String table_update_inventory = "inventory";
        	String parameter_update_inventory = "id_stock=?,id_price=?,";
        	String condition_update_inventory = "id=?";
        	
	 		for(String key : keys){	 		 			
				if(map_inventory_db.containsKey(key)){
					//id_category is coming from request, if it is zero then not concedered
					if(map_inventory_db.get(key).equals("id_category")){
						if(!map_inventory_bean.get(key).toString().trim().equals("0")){
							parameter_update_inventory = parameter_update_inventory + map_inventory_db.get(key)+" = '"+map_inventory_bean.get(key)+"',";
						}						
					}
					else{
						parameter_update_inventory = parameter_update_inventory + map_inventory_db.get(key)+" = '"+map_inventory_bean.get(key)+"',";
					}					
				}
			}
	 		parameter_update_inventory = parameter_update_inventory.substring(0, parameter_update_inventory.length()-1);	 		
	 		String query_update_inventory = "UPDATE "+table_update_inventory+" SET "+parameter_update_inventory+" WHERE "+condition_update_inventory;
	 		
	 		PreparedStatement ps_update_inventory = con.prepareStatement(query_update_inventory);
	 		ps_update_inventory.setInt(1, map_inventory_ids.get("id_stock"));
	 		ps_update_inventory.setInt(2, map_inventory_ids.get("id_price"));
	 		ps_update_inventory.setInt(3, (Integer)map_inventory_bean.get("id"));	 			 	
	 		ps_update_inventory.execute();
	 		con.commit();	 		
        }catch(Exception e){
        	e.printStackTrace();
        	map.put("response_code", 4000);
        	map.put("response_message", "failure: update inventory - table inventory");
        	return new Gson().toJson(map);
        }	
        map.put("response_code", 2000);
		map.put("response_message", "success: update inventory");
		return new Gson().toJson(map);
	}
	
	@GET
	@Path("/get/all")
	@Produces(MediaType.TEXT_PLAIN)
    
    public String getAll(){

    	Map <String,Object> map = new HashMap<String,Object>();
    	
    	ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    	
    	String table_name = "marketplace";
		String columns = "id,name,type,url";
		String query = "SELECT "+columns+" FROM "+table_name;
		
		
		Connection con = DbConnection.getConnection();
		
		try {
			
			PreparedStatement ps = con.prepareStatement(query);

			ResultSet rs =  ps.executeQuery();

			while (rs.next()){
						
				Map <String,Object> map_rs = new HashMap<String,Object>();
				
				map_rs.put("id", rs.getInt("id"));
				map_rs.put("name", rs.getString("name"));
				map_rs.put("type", rs.getString("type"));
				map_rs.put("url", rs.getString("url"));					
					
				list.add(map_rs);
				
				map.put("data", list);
				map.put("response_code", 2000);
				map.put("response_message", "success: get order - all");				
			} 			
//			else {
//				
//				map.put("response_code", 4000);
//				map.put("response_message", "failure: get order - all");
//				
//			}
				
		}
		catch (SQLException e){
			e.printStackTrace();
			map.put("response_code", 4000);
			map.put("response_message", "failure: get order - all");
		}
		
		return new Gson().toJson(map);
        
    }	
	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	
    public String create(BeanInventory bean_inventory) {        
		Map <String,Object> map = new HashMap<String,Object>();
		
		
		int id_inventory = 0;				
		if (checkExistenceInventory(bean_inventory) == false){
			
			id_inventory = this.createInventory(bean_inventory);
			
			if (id_inventory != 0){
	
				map.put("id_inventory", id_inventory);
				
				//Test Code Start
					List<String> list1 =null;
					boolean is_inventory_marketplace_created = this.createInventoryMarketplaces(bean_inventory, map);
					if(is_inventory_marketplace_created){
						
						map.put("response_code", 2000);
						map.put("response_message", "success: create inventory");
						
						return new Gson().toJson(map);
						
					}
					else{
						map.put("id_inventory", 0);
						
						map.put("response_code", 4000);
						map.put("response_message", "failure: create inventory");
						
						return new Gson().toJson(map);
					}
				//Test Code Close
			
			}
			else {
				
				map.put("id_inventory", 0);
				
				map.put("response_code", 4000);
				map.put("response_message", "failure: create inventory");
				
				return new Gson().toJson(map);
				
			}
			
		}
		else {
			
			map.put("response_code", 4000);
			map.put("response_message", "inventory already exists");
			
			return new Gson().toJson(map);
		}
        
    }	
	public boolean createInventoryMarketplaces(BeanInventory bean_inventory, Map<String,Object> map){
		
		List<BeanInventoryMarketplace> list =  bean_inventory.getList();
		//System.out.println(mplace.getId_marketplace()+" "+mplace.getSell_price_inventory_marketplace()+" "+mplace.getStatus_inventory_marketplace()+" "+mplace.getUrl_inventory_marketplace()+" "+mplace.status_inventory_marketplace);
		Connection con = DbConnection.getConnection();		
		
		String table_inventory_marketplace = "inventory_marketplace";
		String column_inventory_marketplace = "id_inventory, id_marketplace, id_price, id_stock, status_listing, url";
		String values_inventory_marketplace = "?, ?, ?, ?, ?, ?";
		String query_inventory_marketplace = "INSERT INTO "+table_inventory_marketplace+"("+column_inventory_marketplace+")"+" VALUES ("+values_inventory_marketplace+");";				
		
		try {
			con.setAutoCommit(false);
			PreparedStatement ps_inventory_marketplace = con.prepareStatement(query_inventory_marketplace);
			for(BeanInventoryMarketplace mplace : list){

				int id_inventory = (Integer)map.get("id_inventory");
				int id_marketplace = mplace.getId_marketplace();
				int id_price = this.createPrceInventoryMarketplace(mplace.getSell_price_inventory_marketplace(), bean_inventory.getPriceMrp());				
				int id_stock = this.createStockInventoryMarketplace(mplace.getStock_inventory_marketplace(), bean_inventory);
				String status = mplace.getStatus_inventory_marketplace();
				String url = mplace.getUrl_inventory_marketplace();
				
				System.out.println(id_inventory+" "+id_marketplace+" "+id_price+" "+id_stock+" "+status+" "+url);				
				ps_inventory_marketplace.setInt(1, id_inventory);
				ps_inventory_marketplace.setInt(2, id_marketplace);
				ps_inventory_marketplace.setInt(3, id_price);
				ps_inventory_marketplace.setInt(4, id_stock);
				ps_inventory_marketplace.setString(5, status);
				ps_inventory_marketplace.setString(6, url);
				
				ps_inventory_marketplace.execute();
				con.commit();
			}			
			return true;
		}
		catch(SQLException e){
			return false;
		}		
	}
	
	int createInventory(BeanInventory bean_inventory){
		
		int id = 0, id_stock = 0, id_price = 0, id_category = 0;
		
		id_stock = this.createStock(bean_inventory);

		id_price = this.createPrice(bean_inventory);
		
		Map<String, Object> map_check_existence_category = new HashMap<String, Object>();
		map_check_existence_category = this.checkExistenceCategory(bean_inventory.getIdCategory());
		
		if ((Boolean) map_check_existence_category.get("boolean")){
			
			id_category = (Integer) map_check_existence_category.get("id_category");
			
			String table_name = "inventory";
			String columns = "id_category, id_stock, id_price, sku, name, status_listing, image_dir";
			String values = "?, ?, ?, ?, ?, ?, ?";
			String query = "INSERT INTO "+table_name+"("+columns+")"+" VALUES "+"("+values+")";
			
			Connection con = DbConnection.getConnection();
			
			try {
				
				PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, id_category);
				ps.setInt(2, id_stock);
				ps.setInt(3, id_price);
				ps.setString(4, bean_inventory.getSku());
				ps.setString(5, bean_inventory.getName());
				ps.setString(6, bean_inventory.getStatusListing());
				ps.setString(7, bean_inventory.getImageDir());
			
				int rows_affected =  ps.executeUpdate();
				
				if (rows_affected != 0){
					
					ResultSet rs = ps.getGeneratedKeys();
					
					if (rs.next()){
					
						id = rs.getInt(1);
						
					}    			
				}
					
			}
			catch (SQLException e){
				e.printStackTrace();
			}
			
		}
		
		return id; 
	}
	
	
	Boolean checkExistenceInventory(BeanInventory bean_inventory){
		
		String table_name = "inventory";
		String column = "id";
		String condition = "name=? OR sku=?";
		String query = "SELECT "+column+" FROM "+table_name+" WHERE "+condition;
		
		Connection con = DbConnection.getConnection();
		
		try {
			
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, bean_inventory.getName());
			ps.setString(2, bean_inventory.getSku());
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()){
				return true;
			}
			
		} 
		catch (SQLException e){
			e.printStackTrace();
		}
		return false;
	}
	
	
	int createStock(BeanInventory bean_inventory){
		
		int id_stock = 0;
		
		int available = bean_inventory.getAvailable();
		int outgoing = bean_inventory.getOutgoing();
		int incoming = bean_inventory.getIncoming();
		
		String aisle = bean_inventory.getAisle();
		String rack = bean_inventory.getRack();
		String row = bean_inventory.getRow();
		String case_box = bean_inventory.getCaseBox();
		
		Map<String, Object> map_check_existence_stock = new HashMap<String, Object>();
		
		map_check_existence_stock = this.checkExistenceStock(available, outgoing, incoming);
		
		if ((Boolean) map_check_existence_stock.get("boolean")){
			
			id_stock = (Integer) map_check_existence_stock.get("id_stock");
			
		}
		else {
			
			String table_name = "stock";
			String columns = "available, outgoing, incoming, aisle, rack, row, case_box";
			String values = "?, ?, ?";
			String query = "INSERT INTO "+table_name+"("+columns+")"+" VALUES "+"("+values+")";
			
			Connection con = DbConnection.getConnection();
			
			try {
				
				PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, available);
				ps.setInt(2, outgoing);
				ps.setInt(3, incoming);
				ps.setString(4, aisle);
				ps.setString(5, rack);
				ps.setString(6, row);
				ps.setString(7, case_box);
				
				
				int rows_affected =  ps.executeUpdate();
				
				if (rows_affected != 0){
					
					ResultSet rs = ps.getGeneratedKeys();
					
					if (rs.next()){
					
						id_stock = rs.getInt(1);
						
					}    			
				}
					
			}
			catch (SQLException e){
				e.printStackTrace();
			}
		}
		
		return id_stock;
	}
	
	//create stock for inventory marktplace
	int createStockInventoryMarketplace(int available, BeanInventory bean_inventory){
		
		int id_stock = 0;
				
		int outgoing = bean_inventory.getOutgoing();
		int incoming = bean_inventory.getIncoming();
		
		String aisle = bean_inventory.getAisle();
		String rack = bean_inventory.getRack();
		String row = bean_inventory.getRow();
		String case_box = bean_inventory.getCaseBox();
		
		Map<String, Object> map_check_existence_stock = new HashMap<String, Object>();
		
		map_check_existence_stock = this.checkExistenceStock(available, outgoing, incoming);
		
		if ((Boolean) map_check_existence_stock.get("boolean")){
			
			id_stock = (Integer) map_check_existence_stock.get("id_stock");
			
		}
		else {
			
			String table_name = "stock";
			String columns = "available, outgoing, incoming, aisle, rack, row, case_box";
			String values = "?, ?, ?, ?, ?, ?, ?";
			String query = "INSERT INTO "+table_name+"("+columns+")"+" VALUES "+"("+values+")";
			
			Connection con = DbConnection.getConnection();
			
			try {
				
				PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, available);
				ps.setInt(2, outgoing);
				ps.setInt(3, incoming);
				ps.setString(4, aisle);
				ps.setString(5, rack);
				ps.setString(6, row);
				ps.setString(7, case_box);
				
				
				int rows_affected =  ps.executeUpdate();
				
				if (rows_affected != 0){
					
					ResultSet rs = ps.getGeneratedKeys();
					
					if (rs.next()){
					
						id_stock = rs.getInt(1);
						
					}    			
				}
					
			}
			catch (SQLException e){
				e.printStackTrace();
			}
		}
		
		return id_stock;
	}

	Map<String, Object> checkExistenceStock(int available, int outgoing, int incoming){
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		String table_name = "stock";
		String column = "id";
		String condition = "available=? and outgoing=? and incoming=?";
		String query = "SELECT "+column+" FROM "+table_name+" WHERE "+condition;
		
		Connection con = DbConnection.getConnection();
		
		try {
			
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, available);
			ps.setInt(2, outgoing);
			ps.setInt(3, incoming);

			ResultSet rs = ps.executeQuery();
			
			if (rs.next()){
				
				map.put("boolean", true);
				map.put("id_stock", rs.getInt("id"));
				
			}
			else {
				
				map.put("boolean", false);
				map.put("id_stock", 0);
				
			}
		} 
		catch (SQLException e){
			e.printStackTrace();
		}

		return map;
	
	}
	
	
	int createPrice(BeanInventory bean_inventory){
		
		int id_price = 0;
		
		int price_sell = bean_inventory.getPriceSell();
		int price_mrp = bean_inventory.getPriceMrp();
		
		Map<String, Object> map_check_existence_price = new HashMap<String, Object>();
		
		map_check_existence_price = this.checkExistencePrice(price_sell, price_mrp);
		
		if ((Boolean) map_check_existence_price.get("boolean")){
			
			id_price = (Integer) map_check_existence_price.get("id_price");
			
		}
		else {
			
			String table_name = "price";
			String columns = "price_sell, price_mrp";
			String values = "?, ?";
			String query = "INSERT INTO "+table_name+"("+columns+")"+" VALUES "+"("+values+")";
			
			Connection con = DbConnection.getConnection();
			
			try {
				
				PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, price_sell);
				ps.setInt(2, price_mrp);
				
				int rows_affected =  ps.executeUpdate();
				
				if (rows_affected != 0){
					
					ResultSet rs = ps.getGeneratedKeys();
					
					if (rs.next()){
					
						id_price = rs.getInt(1);
						
					}    			
				}
					
			}
			catch (SQLException e){
				e.printStackTrace();
			}
		}
		
		return id_price;
	}
	
	//for inventory marketplace 
	int createPrceInventoryMarketplace(int price_sell, int price_mrp){
		
		int id_price = 0;
						
		Map<String, Object> map_check_existence_price = new HashMap<String, Object>();
		
		map_check_existence_price = this.checkExistencePrice(price_sell, price_mrp);
		
		if ((Boolean) map_check_existence_price.get("boolean")){
			
			id_price = (Integer) map_check_existence_price.get("id_price");
			
		}
		else {
			
			String table_name = "price";
			String columns = "price_sell, price_mrp";
			String values = "?, ?";
			String query = "INSERT INTO "+table_name+"("+columns+")"+" VALUES "+"("+values+")";
			
			Connection con = DbConnection.getConnection();
			
			try {
				
				PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, price_sell);
				ps.setInt(2, price_mrp);
				
				int rows_affected =  ps.executeUpdate();
				
				if (rows_affected != 0){
					
					ResultSet rs = ps.getGeneratedKeys();
					
					if (rs.next()){
					
						id_price = rs.getInt(1);
						
					}    			
				}
					
			}
			catch (SQLException e){
				e.printStackTrace();
			}
		}
		
		return id_price;
	}

	Map<String, Object> checkExistencePrice(int price_sell, int price_mrp){
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		String table_name = "price";
		String column = "id";
		String condition = "price_sell=? and price_mrp=?";
		String query = "SELECT "+column+" FROM "+table_name+" WHERE "+condition;
		
		Connection con = DbConnection.getConnection();
		
		try {
			
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, price_sell);
			ps.setInt(2, price_mrp);

			ResultSet rs = ps.executeQuery();
			
			if (rs.next()){
				
				map.put("boolean", true);
				map.put("id_price", rs.getInt("id"));
				
			}
			else {
				
				map.put("boolean", false);
				map.put("id_price", 0);
				
			}
		} 
		catch (SQLException e){
			e.printStackTrace();
		}

		return map;
		
	}
	
	
	Map<String, Object> checkExistenceCategory(int id_category){
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		String table_name = "inventory_category";
		String column = "id";
		String condition = "id=?";
		String query = "SELECT "+column+" FROM "+table_name+" WHERE "+condition;
		
		Connection con = DbConnection.getConnection();
		
		try {
			
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, id_category);

			ResultSet rs = ps.executeQuery();
			
			if (rs.next()){
				
				map.put("boolean", true);
				map.put("id_category", rs.getInt("id"));
				
			}
			else {
				
				map.put("boolean", false);
				map.put("id_category", 0);
				
			}
		} 
		catch (SQLException e){
			e.printStackTrace();
		}

		return map;
		
	}

}
