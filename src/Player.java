import java.awt.AWTException;
import java.util.ArrayList;
import java.util.List;
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
	
	private int[][] customerPosition = new int[][]{{41,151},{141,151},{241,151},{341,151},{441,151},{541,151}};
	
	
	// 0 = Shrimp / 1 = Riz / 2 = Feuille Verte / 3 = fish egg / 4 = salmon / 5 = unagi 
	private int[] stocks = new int[]{5,10,10,10,5,5};
	private int[][] sushiPosition = new int[][]{{42,330},{92,330},{42,388},{92,388},{42,446},{92,446}};
	private int[][] sushiRecipe = new int[][]{{0,2,1,0,0,0},{0,1,1,1,0,0},{0,1,1,2,0,0}};
	private boolean[] beingDone = new boolean[]{false,false,false,false,false,false};
	
	
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
		
		while(true) {
			checkAllCustomers();
			Thread.sleep(5000);
			System.out.println("looking for customers");
		}
		
	}
	
	private void pressStartButton() throws Exception {
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
		Thread.sleep(100);
	}
	
	private void checkAllCustomers() throws IOException, InterruptedException {
		for(int i=0;i<6;i++) {


			// Dans le cas où on a pas déjà envoyé le sushi
			if(this.beingDone[i] == false) {
				
				// Si on a un client
				if(checkACustomer(i)) {
						int d = checkSushi(i);
						
						// Si on a un sushi
						if(d > -1) {
							int refIngredient = 0;
							
							// Pour chaque ingrédient
							// (int) nbr  : contient le nombre 
							// (int) refIngredient : contient la référence de l'ingrédient en cours
							for(int nbr : this.sushiRecipe[d]) {
								
								
								int s = 0;
								while(s < nbr) {
									cv.mouseLeftClick(this.sushiPosition[refIngredient][0] + this.gameRegion.x, this.sushiPosition[refIngredient][1] + this.gameRegion.y);
									Thread.sleep(500);
									
									s++;
								}
								
								
								refIngredient++;
							}
							
							// Puis Validation
							Thread.sleep(1000);
							cv.mouseLeftClick(this.gameRegion.x+210, 384 + this.gameRegion.y);
							this.beingDone[i] = true;
							Thread.sleep(2000);
						}
				}
			}
			else {
				// On a déjà envoyé un sushi à ce type
				if(!this.checkACustomer(i)) {
					// Si le type s'est tiré on ramasse son assiete
					cv.mouseLeftClick(this.customerPosition[i][0] + this.gameRegion.x + 41, this.customerPosition[i][1] + this.gameRegion.y + 47);
					this.beingDone[i] = false;
					
					Thread.sleep(1000);
				}
			}
		}
	}
	
	private int checkSushi(int nbC) throws IOException {
		int sushiType = -1;
		
			int x = this.gameRegion.x + this.customerPosition[nbC][0];
			int y = this.gameRegion.y + this.customerPosition[nbC][1] - 105;
			
			BufferedImage screenshot = cv.takeRegion(new Rectangle(x,y,35,45));
			
			BufferedImage sushi_one = ImageIO.read(new File("img/sushis/onigiri.png"));
			BufferedImage sushi_two = ImageIO.read(new File("img/sushis/california.png"));
			BufferedImage sushi_three = ImageIO.read(new File("img/sushis/gunkan.png"));
			

			int[] matches = cv.findsubImage(sushi_one,screenshot);
			if(matches[0] != -1) {sushiType = 0;}
			else {
				matches = cv.findsubImage(sushi_two,screenshot);
				if(matches[0] != -1) {sushiType = 1;}
				else {
					matches = cv.findsubImage(sushi_three,screenshot);
					if(matches[0] != -1) {sushiType = 2;}
				}
			}
			
		return sushiType;
	}
	
	private boolean checkACustomer(int nbC) throws IOException {
		boolean isThere = true;
		
		int x =  this.gameRegion.x + this.customerPosition[nbC][0];
		int y =  this.gameRegion.y + this.customerPosition[nbC][1];		
		
		BufferedImage screenshot = cv.takeRegion(new Rectangle(x,y,5,40));
		BufferedImage empty_seat = ImageIO.read(new File("img/empty_seat.png"));
			
		matches = cv.findsubImage(empty_seat,screenshot);
		if(matches[0] != -1) isThere = false;	
		
		return isThere;
	}
	
}