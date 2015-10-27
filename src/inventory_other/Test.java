package inventory_other;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class Test {
	public static void main(String ar[]){
		BufferedImage image = null;
        try {
            URL url = new URL("https://www.googleapis.com/plus/v1/people/1234567890?key=AIzaSyBdGv9o0zTkVPQjAW_twEtjn7plN0461aE");
            image = ImageIO.read(url);
            ImageIO.write(image, "jpg",new File("D:\\test/mypic.jpg"));
        } catch (IOException e) {
        	e.printStackTrace();
        }        
        System.out.println("done");
	}
}
