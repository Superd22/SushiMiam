import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Player {
	

	private boolean is_game_launched = false;
	private Rectangle gameRegion;
	private int[] matches;
	private static Robot r;
	private eye cv = new eye();
	static {
	    try {
	        r = new Robot();
	    } catch(AWTException e){e.printStackTrace();}
	}
	
	Player() throws Exception {
	
		while(!is_game_launched) {

			BufferedImage screenshot = cv.takescreen();
			BufferedImage main_logo = ImageIO.read(new File("img/main_title.png"));
			

			 matches = cv.findsubImage(main_logo,screenshot);
			if(matches[0] != -1) {		System.out.println("vu"); is_game_launched=true;}
			else {System.out.println("still looking"); Thread.sleep(4000);}
           
		}
		this.gameRegion = cv.getPlayRegion(matches);
		this.pressStartButton();
		
	}
	
	private void pressStartButton() throws InterruptedException {
		// Hard coded positions
		int getX = this.gameRegion.x;
		int getY =  this.gameRegion.y;
		
		// Start Button
		cv.mouseLeftClick(getX+320,getY+200);
		Thread.sleep(100);
		
		// Continue
		cv.mouseLeftClick(getX+320,getY+390);
		Thread.sleep(100);
		
		// Skip
		cv.mouseLeftClick(getX+590,getY+460);
		Thread.sleep(100);
		
		// Another Continue
		cv.mouseLeftClick(getX+320,getY+390);
		
	}
	
	
}