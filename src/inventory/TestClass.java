package inventory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.FormDataParam;

@Path("test_upload")
public class TestClass 
{
	private static final long serialVersionUID = 1L;
	private String UPLOAD_DIRECTORY = null;
	
	@Context ServletContext context;
	public TestClass(){	
		
	}
	
	//not working yet -- look below this function
//	@POST
//	@Path("/upload") 
//	@Consumes(MediaType.MULTIPART_FORM_DATA)	
//	@Produces(MediaType.TEXT_PLAIN)	
//	public String testUpload(FormDataMultiPart multiPart){		
//		UPLOAD_DIRECTORY = (context.getRealPath("/")).toString()+"/inventory_images/";
//		 List<FormDataBodyPart> fields = multiPart.getFields("file");        
//		    for(FormDataBodyPart field : fields){
//		    	
////		    	try{
////		    		handleInputStream(field.getValueAs(InputStream.class),UPLOAD_DIRECTORY);	
////		    	}
////		    	catch(Exception e){
////		    		System.out.println(e);
////		    	}
//		        
//		    }
//		return "{}";
//	}	
//	private void handleInputStream(InputStream is,String fileName)throws IOException{
//		OutputStream os = new FileOutputStream(new File(fileName));
//		byte[] buffer = new byte[256];
//	    int bytes = 0;
//	    while ((bytes = is.read(buffer)) != -1) {
//	        os.write(buffer, 0, bytes);
//	    }
//	    //read the stream any way you want
//	}
	
	
	//working function
	@POST
	@Path("/upload") 
	@Consumes(MediaType.MULTIPART_FORM_DATA)	
	@Produces(MediaType.TEXT_PLAIN)	
	public String testUpload(@Context HttpServletRequest request){				
		UPLOAD_DIRECTORY = (context.getRealPath("/")).toString()+"/inventory_images/";		
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
                    int index_number = fileName.indexOf("-");
                    fileName = fileName.substring(0,index_number);
                    
                    File finalDirectoryName = new File(UPLOAD_DIRECTORY+fileName);
                    
                    // if the directory does not exist, create it
                    if (!finalDirectoryName.exists()) {	                        
                        boolean result = false;

                        try{
                        	finalDirectoryName.mkdir();
                            result = true;
                        } 
                        catch(SecurityException se){
                            //handle it
                        }        
                        if(result) {    
                            System.out.println("DIR created");  
                        }
                    }

                    for (FileItem item : multiparts) {	                    	
                      if (!item.isFormField()) {	                    
                    	  
                         String name = new File(item.getName()).getName();
                         item.write(new File(finalDirectoryName + File.separator + name));
                      }
                 }
            } 
            catch (Exception e) 
            {
              System.out.println("File upload failed");
            }
		}
		return "{}";
	}	
	
	//working function
//	@POST
//	@Path("/upload") 
//	@Consumes(MediaType.MULTIPART_FORM_DATA)
//	@Produces(MediaType.TEXT_PLAIN)	
//	public String testUpload(@Context HttpServletRequest request){				
//		UPLOAD_DIRECTORY = (context.getRealPath("/")).toString()+"/inventory_images/";		
//		boolean isMultipart = ServletFileUpload.isMultipartContent(request);	   
//		if (isMultipart) {
//            // Create a factory for disk-based file items
//            FileItemFactory factory = new DiskFileItemFactory();
//
//            // Create a new file upload handler
//            ServletFileUpload upload = new ServletFileUpload(factory);
//            try {
//                    // Parse the request
//                    List<FileItem> multiparts = upload.parseRequest(request);                    
//                    String fileName = multiparts.get(0).getName();                    
//                    int index_number = fileName.indexOf("-");
//                    fileName = fileName.substring(0,index_number);
//                    
//                    File finalDirectoryName = new File(UPLOAD_DIRECTORY+fileName);
//                    
//                    // if the directory does not exist, create it
//                    if (!finalDirectoryName.exists()) {	                        
//                        boolean result = false;
//
//                        try{
//                        	finalDirectoryName.mkdir();
//                            result = true;
//                        } 
//                        catch(SecurityException se){
//                            //handle it
//                        }        
//                        if(result) {    
//                            System.out.println("DIR created");  
//                        }
//                    }
//
//                    for (FileItem item : multiparts) {	                    	
//                      if (!item.isFormField()) {	                    
//                    	  
//                         String name = new File(item.getName()).getName();
//                         item.write(new File(finalDirectoryName + File.separator + name));
//                      }
//                 }
//            } 
//            catch (Exception e) 
//            {
//              System.out.println("File upload failed");
//            }
//		}
//		return "{}";
//	}	
}
