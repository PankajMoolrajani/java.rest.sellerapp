package order_uniware;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import order.BeanOrder;
import order.BeanOrderLine;

import au.com.bytecode.opencsv.CSVReader;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import db.DbConnection;
import db.DbUtils;

@Path("/uniware")
public class UniwareUpload {
		
	@Context ServletContext context;
	public UniwareUpload(){
		
	}
	
	@POST
	@Path("/upload") 
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_PLAIN)	
	public String uploadFile(@FormDataParam("file") InputStream is, 
	                    	   @FormDataParam("file") FormDataContentDisposition formData) {
		
	    String fileLocation = (context.getRealPath("/")).toString()+"/order_file/" + formData.getFileName();
	    try {
			saveFile(is, fileLocation);
			//String result = "Successfully File Uploaded on the path "+fileLocation;	
			
			//paseUniwareFileData(fileLocation);
			
			return "{}"; //{'id':14,'response_message':'success : create category','response_code':2000}
		} catch (IOException e) {
			e.printStackTrace();
			return "{'error':'yes_error'}";			
		}		
		
		
	}
	
	private void saveFile(InputStream is, String fileLocation) throws IOException {
    	OutputStream os = new FileOutputStream(new File(fileLocation));
		byte[] buffer = new byte[256];
	    int bytes = 0;
	    while ((bytes = is.read(buffer)) != -1) {
	        os.write(buffer, 0, bytes);
	    }
	}
	
	private void paseUniwareFileData(String fileLocation){

        CSVReader reader;					    	    
	    Map<String,BeanOrder> uniware_file_data_map = new LinkedHashMap<String,BeanOrder>(200,0.50f);	    	   
	    Set<String> order_id_set = new LinkedHashSet<String>();
		//Map<String,String> output_map = new HashMap<String,String>();
        try 
        {
	        reader = new CSVReader(new FileReader(fileLocation));
	        String[] row;
	        int order_ID_Column_No =1;
	        int sub_Order_Id_Column_No = 1;
	        int sku_Column_No = 1;
	        int marketplace_Column_No = 1;	
	        int total_Price_Column_No = 1;
	        int selling_Price_Column_No = 1;
	        int discount_Column_No = 1;	   
	        int shipping_Charges_Column_No = 1;
	        int tax_Percent_Column_No = 1;
	        
			Map<String,Integer> map = new HashMap<String,Integer>();
        	
        	row = reader.readNext();
        	
        	//to get column number of particular field
            for (int i = 0; i < row.length; i++) 
            {
            	if(row[i].toString().equals("Sale Order Item Code"))
            	{
            		sub_Order_Id_Column_No = i;
            		continue;
                }            	
            	if(row[i].toString().equals("Display Order Code"))
            	{
            		order_ID_Column_No = i;      
            		continue;
                }
            	else if(row[i].toString().equals("Item SKU Code"))
            	{
            		sku_Column_No=i;
            		continue;
                }
            	else if(row[i].toString().equals("Channel Name"))
            	{
            		marketplace_Column_No = i;            		
            		continue;
            	}
            	else if(row[i].toString().equals("Total Price"))
            	{
            		total_Price_Column_No = i;
            		continue;
            	}
            	else if(row[i].toString().equals("Selling Price"))
            	{
            		selling_Price_Column_No = i;
            		continue;
            	}
            	else if(row[i].toString().equals("Discount"))
            	{
            		discount_Column_No = i;
            		continue;
            	}
            	else if(row[i].toString().equals("Shipping Charges"))
            	{
            		shipping_Charges_Column_No = i;
            		continue;
            	}
//            	else if(row[i].toString().equals("Tax %"))
//            	{
//            		tax_Percent_Column_No = i;
//            		continue;
//            	}            	
            }                           
        	int i=1;
        	
        	BeanOrder order_bean = null;
        	BeanOrderLine order_line_bean = null;
        	
        	//all marketplaceIds
        	Map<String,Integer> marketplace_ids_map = fetchMarketplaceIds();
        	        
            while ((row = reader.readNext()) != null) {	    		             	
            	
	        	String order_id = row[order_ID_Column_No].toString().trim();
	        	String sub_order_id = row[sub_Order_Id_Column_No].toString().trim();
	        	String sku_value = row[sku_Column_No].toString().trim();
	        	String marketplace_name = row[marketplace_Column_No].toString().trim();
	        	//System.out.println("error "+ row[total_Price_Column_No].toString().trim());
	        	float total_price =  Float.parseFloat(row[total_Price_Column_No].toString().trim());
	        	float selling_price =  Float.parseFloat(row[selling_Price_Column_No].toString().trim()); 
	        	float discount_amount = Float.parseFloat(row[discount_Column_No].toString().trim());	        	
	        	float shipping_charges = Float.parseFloat(row[shipping_Charges_Column_No].toString().trim());
	        	
	        	double tax_percent = getInventoryTaxPercent(sku_value);
	        	//System.out.println(row[tax_Percent_Column_No].toString());
	        	//String taxPercent = row[tax_Percent_Column_No].toString().trim(); 	        
	        	
	        	Integer marketplace_id = marketplace_ids_map.get(marketplace_name.toLowerCase());
	        	
	        	if(order_id_set.add(order_id)){ //if new order	
	        		//create a new order
	        		order_bean = new BeanOrder();
	        		order_bean.setIdUser(1); //temprory fill
	        		
	        		order_bean.setMarketplaceOrderid(order_id);	        			        		        			        
					order_bean.setIdMarketplace(marketplace_id.intValue());
					
					order_line_bean = new BeanOrderLine();
					order_line_bean.setMarektplaceSuborderid(sub_order_id);
					
										
					float amount_taxable = 0;
					float amount_tax = 0;
					float amount_untaxable = 0;
					if(tax_percent == 0.0){
						amount_taxable = 0;
						amount_tax = 0;
						amount_untaxable = selling_price;
					}
					else{
						amount_tax = (float)((selling_price * tax_percent)/100);
						amount_untaxable = 0;
						amount_taxable = selling_price;
					}	
					
				//	order_bean.setAmountTotalTaxable(amount_taxable);
					
					order_line_bean.setAmountTaxable(amount_taxable);
					order_line_bean.setAmountUntaxable(amount_untaxable);
					order_line_bean.setAmountTax(amount_tax);
					
					int id_inventory = getInventoryId(sku_value);				
					int id_inventory_marketplace = getInventoryMarketplaceId(id_inventory, marketplace_id.intValue());
					order_line_bean.setIdInventoryMarketplace(id_inventory_marketplace);
					
					order_line_bean.setAmountShipping(shipping_charges);
					
					ArrayList<BeanOrderLine> order_line_list = new ArrayList<BeanOrderLine>();
					order_line_list.add(order_line_bean);
					order_bean.setBeanOrderLine(order_line_list);	
										
					uniware_file_data_map.put(order_id, order_bean);
	        	}	   
	        	else{ // existing order, so add new order line to the order
	        		
	        		BeanOrder fetched_order_bean= uniware_file_data_map.get(order_id);
	        		ArrayList<BeanOrderLine> fetched_order_line_list = fetched_order_bean.getBeanOrderLine();
	        		
	        		order_line_bean = new BeanOrderLine();
					order_line_bean.setMarektplaceSuborderid(sub_order_id);
														
					float amount_taxable = 0;
					float amount_tax = 0;
					float amount_untaxable = 0;
					if(tax_percent == 0.0){
						amount_taxable = 0;
						amount_tax = 0;
						amount_untaxable = selling_price;
					}
					else{
						amount_tax = (float)((selling_price * tax_percent)/100);
						amount_untaxable = 0;
						amount_taxable = selling_price;
					}	
					
					order_line_bean.setAmountTaxable(amount_taxable);
					order_line_bean.setAmountUntaxable(amount_untaxable);
					order_line_bean.setAmountTax(amount_tax);
					
					int id_inventory = getInventoryId(sku_value);				
					int id_inventory_marketplace = getInventoryMarketplaceId(id_inventory, marketplace_id.intValue());
					order_line_bean.setIdInventoryMarketplace(id_inventory_marketplace);
					
					order_line_bean.setAmountShipping(shipping_charges);
					
					//update order line list
					fetched_order_line_list.add(order_line_bean);
					//update order bean
					fetched_order_bean.setBeanOrderLine(fetched_order_line_list);
					//update uniware map
					uniware_file_data_map.put(order_id , fetched_order_bean);
	        	}
	        }	                       
	     }	        
        catch (FileNotFoundException e) 
        {        	        	
                System.err.println(e);
        }
        catch (IOException e) 
        {
        	//outputMap.put("error_code", "506");        	
            System.err.println(e);
        }		   		
	}
	
	public int getInventoryMarketplaceId(int id_product, int id_mplace) 
	{			
		int id_inventory_mplace = 0;
		Connection con = null;
		PreparedStatement ps_mplace_id = null;
		ResultSet rs_mplace_id = null;
		
		PreparedStatement ps_inventory_mplace_id = null;
		ResultSet rs_inv_mplace_id = null;
		try
		{
			con = DbConnection.getConnection();
			
			ps_inventory_mplace_id = con.prepareStatement("select id from inventory_marketplace where id_product=? and id_marketplace=?");
			ps_inventory_mplace_id.setInt(1, id_product);
			ps_inventory_mplace_id.setInt(2, id_mplace);			
			
			rs_inv_mplace_id = ps_inventory_mplace_id.executeQuery();					
			
			while(rs_inv_mplace_id.next())
			{
				id_inventory_mplace = rs_inv_mplace_id.getInt("id");			
			}			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return 0;			
		}
		finally
		{
			DbUtils.closeUtil(rs_inv_mplace_id);
			DbUtils.closeUtil(ps_inventory_mplace_id);
			DbUtils.closeUtil(rs_mplace_id);
			DbUtils.closeUtil(ps_mplace_id);
			DbUtils.closeUtil(con);
		}
		return id_inventory_mplace;
	}
	
	private int getInventoryId(String productSkuNumber) 
	{
		int id_product = 0;		
		Connection con_product_id = null;
		PreparedStatement ps_product_id = null;
		ResultSet rs_product_id = null;
		try
		{
			con_product_id = DbConnection.getConnection();
			ps_product_id = con_product_id.prepareStatement("select id from product where sku=?");
			ps_product_id.setString(1, productSkuNumber);
			rs_product_id = ps_product_id.executeQuery();			
			while(rs_product_id.next())
			{
				id_product = rs_product_id.getInt("id");				
			}
			return id_product;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return id_product;			
		}
		finally
		{
			DbUtils.closeUtil(rs_product_id);
			DbUtils.closeUtil(ps_product_id);
			DbUtils.closeUtil(con_product_id);
		}
	}
	
	private double getInventoryTaxPercent(String sku)
	{
		Connection con = null;
		PreparedStatement ps_inventory_id = null;			
		ResultSet rs_inventory_id = null;
		
		PreparedStatement ps_tax_percent = null;
		ResultSet rs_tax_percent = null;		
		try
		{
			con = DbConnection.getConnection();
			ps_inventory_id = con.prepareStatement("select id_category from product where sku=?");
			ps_inventory_id.setString(1, sku);
			rs_inventory_id = ps_inventory_id.executeQuery();
			rs_inventory_id.next();
			int inventory_cat_id = rs_inventory_id.getInt("id_category");
			
			//System.out.println("inventory_cat_id is "+inventory_cat_id);
			ps_tax_percent = con.prepareStatement("select tax_percent from inventory_category where id=?");
			ps_tax_percent.setInt(1, inventory_cat_id);
			rs_tax_percent = ps_tax_percent.executeQuery();
			rs_tax_percent.next();			
			double taxPercent = rs_tax_percent.getDouble("tax_percent");
			return taxPercent;
		}
		catch(Exception e)
		{			
			e.printStackTrace();
			return 0;
		}
		finally
		{
			DbUtils.closeUtil(rs_tax_percent);
			DbUtils.closeUtil(ps_tax_percent);
			
			DbUtils.closeUtil(rs_inventory_id);
			DbUtils.closeUtil(ps_inventory_id);
			
			DbUtils.closeUtil(con);						
		}
	}

	public Map<String,Integer> fetchMarketplaceIds()
	{
		Connection con = DbConnection.getConnection();
		PreparedStatement ps_marketplace = null;
		ResultSet rs_marketplace = null;
		
		Map<String,Integer> marketplaceBeanMap = new HashMap<String,Integer>();
		try
		{
			ps_marketplace = con.prepareStatement("select * from marketplace");
			rs_marketplace = ps_marketplace.executeQuery();			
			while(rs_marketplace.next())
			{
				Integer id = rs_marketplace.getInt("id");
				String name = rs_marketplace.getString("name").toLowerCase();
				marketplaceBeanMap.put(name, id);
			}
			return marketplaceBeanMap;
		}	
		catch(Exception e)
		{			
			e.printStackTrace();
			return null;
		}
		finally
		{
			DbUtils.closeUtil(rs_marketplace);
			DbUtils.closeUtil(ps_marketplace);
			DbUtils.closeUtil(con);						
		}
	}
}
