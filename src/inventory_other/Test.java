package inventory_other;
import com.dropbox.core.*;
import java.io.*;
import java.util.Locale;
import java.util.Scanner;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;

import javax.imageio.ImageIO;

public class Test {
	public static void main(String ar[]){
		// Get your app key and secret from the Dropbox developers website.
		try{
	        final String APP_KEY = "28wx7i5lfr5hqbb";
	        final String APP_SECRET = "9gimqhf116er7eb";

	        DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);

	        DbxRequestConfig config = new DbxRequestConfig("Monoxor2", Locale.getDefault().toString());
	        DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(config, appInfo);
	        
	        Desktop.getDesktop().browse(new URI(webAuth.start()));
	        Scanner scanner = new Scanner(System.in);
	        String code = scanner.next();
	        System.out.println(webAuth.finish(code));
	        scanner.close();
	        
		}catch(Exception e){
			e.printStackTrace();
		}
       
	}
}
