import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Image.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.imageio.ImageIO;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class eye {
	
	private static Robot r;
	static {
	    try {
	        r = new Robot();
	    } catch(AWTException e){e.printStackTrace();}
	}
	
	static void main(String[] args) {
	}
	
	private Color[] convertImgToArray(BufferedImage img) throws IOException {
		
		  int width = img.getWidth();
	      int height = img.getHeight();
	      Color[] result = new Color[height*width];

	      for (int i = 0; i < height*width; i++) {
	    	  	int[] cords = this.getCoordFromInd(i,width);
	            result[i] = new Color( (int) img.getRGB(cords[0],cords[1]) , false);
	      }
	      
	    return result;
	}
	
	
	private int[] getCoordFromInd(int index, int width) {
		int x = index % width;
		int y = (index - x) / width;
		
		int[] cords = {x,y};
		return cords;
	}
	
	private boolean isPixelClose(Color one, Color two) {
		if( Math.abs(one.getBlue() - two.getBlue()) < 20 &&
			Math.abs(one.getGreen() - two.getGreen()) < 20 &&
			Math.abs(one.getRed() - two.getRed()) < 20) return true;
		else return false;
	}
	
	public int[] findsubImage(BufferedImage sub_img, BufferedImage big_img) throws IOException {
		
		Color[] array_big = this.convertImgToArray(big_img);
		Color[] array_sub = this.convertImgToArray(sub_img);
		
		int y_in_sub = 0;
		int offset = 0;
		int pos_in_big = 0;
		boolean is_a_match = false;
		boolean will_overflow = false;
		for(pos_in_big=0;pos_in_big < array_big.length-1;pos_in_big++) {
			if(will_overflow) break;
			//System.out.println("big");
			//System.out.println(pos_in_big);
			for(int pos_in_sub=0; pos_in_sub < (array_sub.length);pos_in_sub++) {
				
				// Changement de ligne dans big si besoin
				if ( (pos_in_sub > 0) && ( (pos_in_sub % sub_img.getWidth()) == 0) ) {
					// On est à la fin d'une ligne
					y_in_sub++;
				}
				is_a_match = true;

				
				// On compare chaque pixel un à un
			
				offset = (y_in_sub * (big_img.getWidth() - sub_img.getWidth()) ) + pos_in_sub ;
				
				if((offset+pos_in_big) > array_big.length-1) { 					
					pos_in_sub = 0;
					y_in_sub = 0;
					offset = 0;
					is_a_match = false;
					will_overflow = true;
					break;

					
				}
				
				// Si les pixels ne sont pas les mêmes, on s'arrête là et on passe au big pixel suivant
				if (!this.isPixelClose(array_big[pos_in_big+offset],array_sub[pos_in_sub])) {
					// On sort de la boucle
					pos_in_sub = 0;
					y_in_sub = 0;
					offset = 0;
					is_a_match = false;
					break;
				}
				
			}
			
			if (is_a_match) {
				System.out.println("break match");
				break;
			}
			
		}
		
		if(is_a_match) {
			return this.getCoordFromInd(pos_in_big, big_img.getWidth());
		}
		else return new int[]{-1 , -1};
		
	}
	

	
	public BufferedImage takescreen() throws Exception{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle screenRectangle = new Rectangle(screenSize);
		BufferedImage shot = this.takeRegion(screenRectangle);
		
		
		return shot;
	}
	
	public BufferedImage takeRegion(Rectangle region) {
		BufferedImage shot = r.createScreenCapture(region);
		return shot;
	}
	
	public Rectangle getPlayRegion(int[] cords) {
		// cords contient les coordonées du main_logo
		
		// Aprés mesure on a :
		int left_x_decal = -121;
		int left_y_decal = -55;
		
		// d'où :
		Rectangle gameRegion = new Rectangle( cords[0]+left_x_decal,cords[1]+left_y_decal, 640, 480);
		
		return gameRegion;
	}
	
	public void mouseLeftClick(int x,int y) {
		r.mouseMove(x, y);
		int button = InputEvent.getMaskForButton(1);
		r.mousePress(button);
		r.mouseRelease(button);
	}
	
}
