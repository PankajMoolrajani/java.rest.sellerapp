package inventory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

@Path("test_upload")
public class TestClass 
{
	@Context ServletContext context;
	public TestClass(){
		
	}
	
	@POST
	@Path("/upload") 
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_PLAIN)	
	public String testUpload(@FormDataParam("file") InputStream is, 
     	   @FormDataParam("file") FormDataContentDisposition formData,
		   @FormDataParam("name") String name){
		System.out.println(name);
		 String fileLocation = (context.getRealPath("/")).toString()+"/inventory_images/" + formData.getFileName();
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
}
