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
	
	private Customer[] customers = new Customer[]{new Customer(0),new Customer(1),new Customer(2),new Customer(3),new Customer(4),new Customer(5)};
	private Ingredient[] ingredients = new Ingredient[]{new Ingredient(0),new Ingredient(1),new Ingredient(2),new Ingredient(3),new Ingredient(4),new Ingredient(5)};
	private Sushi[] sushis = new Sushi[]{new Sushi(0),new Sushi(1),new Sushi(2),new Sushi(3)};
	
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
		this.checkAllCustomers();
		
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
	
	
	private void removeAssiete(int i) throws InterruptedException {
		cv.mouseLeftClick(this.customers[i].position[0] + this.gameRegion.x + 41, this.customers[i].position[1]  + this.gameRegion.y + 47);
	}
	
	private void MakeSushi(int sushiType) throws InterruptedException {
		// Pour chaque ingrédient
		// (int) nbr  : contient le nombre 
		// (int) refIngredient : contient la référence de l'ingrédient en cours
		
		int refIngredient = 0;
		
		// On s'occupe du stock tkt.
		handleStock(sushiType);
		
		for(int nbr : this.sushis[sushiType].Recipe) {
			int s = 0;
			while(s < nbr) {
				this.ingredients[refIngredient].stock--;
				cv.mouseLeftClick(this.ingredients[refIngredient].position_recette[0] + this.gameRegion.x, this.ingredients[refIngredient].position_recette[1]  + this.gameRegion.y);
				Thread.sleep(200);
				
				s++;
			}
			
			
			refIngredient++;
		}
		
		// Puis Validation
		cv.mouseLeftClick(this.gameRegion.x+210, 384 + this.gameRegion.y);
		Thread.sleep(1000);
	}
	
	private ArrayList<Integer> checkStock(int recipe) {
		
		ArrayList<Integer> need = new ArrayList<Integer>();
		
		for(int i = 0;i<6;i++) {
			if(this.ingredients[i].stock < this.sushis[recipe].Recipe[i]) {
				need.add(i);
			}
		}
		
		return need;
	}
	
	private void handleStock(int i) throws InterruptedException {
		ArrayList<Integer>need = this.checkStock(i);
			if(need.size() > 0 ) {
				for (int j = 0; j < need.size(); j++) {
					int ingredient = need.get(j);
					this.replenish(ingredient);
					this.ingredients[ingredient].stock += this.ingredients[ingredient].replenishes;
				}
				
				Thread.sleep(7000);
			}
		}
	
	private void replenish(int i) throws InterruptedException {
		// Telephone : 590, 355;
		// Click sur le téléphone
		cv.mouseLeftClick(this.gameRegion.x+590, this.gameRegion.y+355);
		
		// Selection Rice/Topping
			if(i == 1) cv.mouseLeftClick(this.gameRegion.x+500, this.gameRegion.y+290);
			else cv.mouseLeftClick(this.gameRegion.x+500, this.gameRegion.y+270);
		
		// Click sur le gérondif
		cv.mouseLeftClick(this.gameRegion.x + this.ingredients[i].position_commande[0], this.gameRegion.y+ this.ingredients[i].position_commande[1]);
		
		// Commande normale svpliz.
		cv.mouseLeftClick(this.gameRegion.x+495, this.gameRegion.y+295);
 
		
	}
	
	private void checkAllCustomers() throws IOException, InterruptedException {
		
		for(int i=0;i<6;i++) {
			if(this.customers[i].previous_state < 2) {
				if(checkACustomer(i)) {
					// On regarde si le client i a un sushi
					int sushiType = checkSushi(i); 
					
					if(sushiType > -1) {
						// Monsieur a un sushi
						if(this.customers[i].previous_state == 0) {
							
							// Monsieur n'avait pas de commande, on va donc lui faire son petit sushi
							this.customers[i].hasbeen_waiting = 0;
							this.customers[i].previous_state = 1;
							this.MakeSushi(sushiType);
						}
						else {
							// Monsieur avait déjà une commande, on wait pour voir si elle arrive oupa.
							this.customers[i].hasbeen_waiting++;
							
							// Si ça fait longtemps qu'il attend, on le remet sur la liste.
							if(this.customers[i].hasbeen_waiting > 20) {
								this.customers[i].previous_state = 0;
							}
						}
					}
					else {
						// Monsieur n'a pas demandé de sushi
						this.customers[i].previous_state = 2;
					}
				}
			}
				// Le type a passé commande et l'a reçu, on check si on peut virer l'assiète
				if(!checkACustomer(i)) {
					this.removeAssiete(i);
					this.customers[i].previous_state = 0;
				}

		}

	     System.out.println("looking for customers");
		
	     

		BufferedImage screenshot = cv.takeRegion(new Rectangle(this.gameRegion.x +190,this.gameRegion.y+ 110,300,30));
		BufferedImage win = ImageIO.read(new File("img/win.png"));
		

		int[] matches = cv.findsubImage(win,screenshot);
		if(matches[0] != -1){
			cv.mouseLeftClick(this.gameRegion.x+320, this.gameRegion.y+370);
			cv.mouseLeftClick(this.gameRegion.x+320, this.gameRegion.y+370);
		}
			
	     
	     
	     
	     this.checkAllCustomers();
	}
	
	private int checkSushi(int nbC) throws IOException {
		int sushiType = -1;
		
			int x = this.gameRegion.x + this.customers[nbC].position[0];
			int y = this.gameRegion.y + this.customers[nbC].position[1] - 105;
			
			BufferedImage screenshot = cv.takeRegion(new Rectangle(x,y,35,45));
			
			BufferedImage sushi_one = ImageIO.read(new File("img/sushis/onigiri.png"));
			BufferedImage sushi_two = ImageIO.read(new File("img/sushis/california.png"));
			BufferedImage sushi_three = ImageIO.read(new File("img/sushis/gunkan.png"));
			BufferedImage sushi_four = ImageIO.read(new File("img/sushis/sroll.png"));
			

			int[] matches = cv.findsubImage(sushi_one,screenshot);
			if(matches[0] != -1) {sushiType = 0;}
			else {
				matches = cv.findsubImage(sushi_two,screenshot);
				if(matches[0] != -1) {sushiType = 1;}
				else {
					matches = cv.findsubImage(sushi_three,screenshot);
					if(matches[0] != -1) {sushiType = 2;}
					else {
						matches = cv.findsubImage(sushi_four,screenshot);
						if(matches[0] != 1) sushiType = 3;
					}
				}
			}
			
		return sushiType;
	}
	
	private boolean checkACustomer(int nbC) throws IOException {
		boolean isThere = true;
		
		int x =  this.gameRegion.x + this.customers[nbC].position[0];
		int y =  this.gameRegion.y + this.customers[nbC].position[1];		
		
		BufferedImage screenshot = cv.takeRegion(new Rectangle(x,y,5,40));
		BufferedImage empty_seat = ImageIO.read(new File("img/empty_seat.png"));
			
		matches = cv.findsubImage(empty_seat,screenshot);
		if(matches[0] != -1) isThere = false;	
		
		return isThere;
	}
	
}