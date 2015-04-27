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
	
	private int level = 0;
	
	private Customer[] customers = new Customer[]{new Customer(0),new Customer(1),new Customer(2),new Customer(3),new Customer(4),new Customer(5)};
	private Ingredient[] ingredients = new Ingredient[]{new Ingredient(0),new Ingredient(1),new Ingredient(2),new Ingredient(3),new Ingredient(4),new Ingredient(5), new Ingredient(6)};
	private Sushi[] sushis = new Sushi[]{new Sushi(0),new Sushi(1),new Sushi(2),new Sushi(3),new Sushi(4),new Sushi(5),new Sushi(6),new Sushi(7)};
	private ArrayList<Commande> commandes = new ArrayList<Commande>();
	
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
	
	
	private void checkLevelUp() throws IOException, InterruptedException {
			BufferedImage screenshot = cv.takeRegion(new Rectangle(this.gameRegion.x +190,this.gameRegion.y+100,300,100));
			BufferedImage win = ImageIO.read(new File("img/win.png"));
			

			int[] matches = cv.findsubImage(win,screenshot);
			if(matches[0] != -1){
				doLevelUp();
			}
				
	}
	
	private void doLevelUp() throws InterruptedException {
		
		// Click sur win
		cv.mouseLeftClick(this.gameRegion.x+320, this.gameRegion.y+370);
		cv.mouseLeftClick(this.gameRegion.x+320, this.gameRegion.y+370);
		
		// Suivi niveau
		this.level++;
		
		// Remise à zéro des ingrédients
		for(int i=0;i<7;i++) {
			ingredients[i].stock = ingredients[i].replenishes;
		}
	}
	
	private void removeAssiete(int i) throws InterruptedException {
		cv.mouseLeftClick(this.customers[i].position[0] + this.gameRegion.x + 41, this.customers[i].position[1]  + this.gameRegion.y + 47);
	}
	
	private void MakeSushi(int sushiType) throws Exception {
		// Pour chaque ingrédient
		// (int) nbr  : contient le nombre 
		// (int) refIngredient : contient la référence de l'ingrédient en cours
		
		int refIngredient = 0;
		
		// On s'occupe du stock tkt.
		handleStock(sushiType);
		
		// On verifie que l'on peut envoyer un sushi.
		if(overFlowSushiCheck()) {
			for(int nbr : this.sushis[sushiType].Recipe) {
				int s = 0;
				while(s < nbr) {
					this.ingredients[refIngredient].stock--;
					Thread.sleep(50);
					cv.mouseLeftClick(this.ingredients[refIngredient].position_recette[0] + this.gameRegion.x, this.ingredients[refIngredient].position_recette[1]  + this.gameRegion.y);
					Thread.sleep(100);
					
					s++;
				}
				
				
				refIngredient++;
			}
		}
		// Puis Validation
		cv.mouseLeftClick(this.gameRegion.x+210, 384 + this.gameRegion.y);
		Thread.sleep(800);
	}
	
	private ArrayList<Integer> checkStock(int recipe) {
		
		ArrayList<Integer> need = new ArrayList<Integer>();
		
		// Renvoie tous les ingrédients dont on aura besoin pour effectuer recipe.
		for(int i = 0;i<6;i++) {
			if(this.ingredients[i].stock < this.sushis[recipe].Recipe[i]) {
				need.add(i);
			}
		}
		
		// PLUS, on ajoute tout ceux qui sont = 0
		for(int d = 0;d<6;d++) {
			if(ingredients[d].stock == 0 && !(need.contains(d))) need.add(d);
		}
		
		return need;
	}
	
	private void handleStock(int i) throws Exception {
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
	
	
	
	private void replenish(int i) throws Exception {
		// Telephone : 590, 355;
		// Click sur le téléphone
		cv.mouseLeftClick(this.gameRegion.x+590, this.gameRegion.y+355);
		
		// Selection Rice/Topping/Sake
			if(i == 1) cv.mouseLeftClick(this.gameRegion.x+500, this.gameRegion.y+290);
			else if (i == 6) cv.mouseLeftClick(this.gameRegion.x+500, this.gameRegion.y+313);
			else cv.mouseLeftClick(this.gameRegion.x+500, this.gameRegion.y+270);
			recCheckStock(i);
	}
	
	
	private void recCheckStock(int i) throws Exception {
		Thread.sleep(200);
		if(canActuallyPurchase(i)) {
			// Click sur le gérondif
			cv.mouseLeftClick(this.gameRegion.x + this.ingredients[i].position_commande[0], this.gameRegion.y+ this.ingredients[i].position_commande[1]);
			// Commande normale svpliz.
			Thread.sleep(200);
			cv.mouseLeftClick(this.gameRegion.x+495, this.gameRegion.y+295);
			Thread.sleep(100);
		}
		else {
			// Du coup on est pauvre on peut que check les 4 premières assiètes
			// (le menu block les 2 dernières)
			for(int d=0;d<4;d++) {removeAssiete(d);}
			recCheckStock(i);
		}
	}
	private boolean canActuallyPurchase(int i) throws IOException {		
		BufferedImage screenshot = cv.takeRegion(new Rectangle(this.gameRegion.x,this.gameRegion.y,640,480));
		BufferedImage sprite = ImageIO.read(new File(this.ingredients[i].sprite));		
		
		int[] matches = cv.findsubImage(sprite,screenshot);
		if(matches[0] > -1) {
			return true;
		}
		else return false;
		
	}
	
	private void checkAllCustomers() throws Exception {
		
		for(int i=0;i<6;i++) {
			if(this.customers[i].previous_state < 2) {
				if(checkACustomer(i)) {
					// On regarde si le client i a un sushi
					int sushiType = checkSushi(i); 
					
					if(sushiType > -1) {
						// Monsieur a un sushi
						if(this.customers[i].previous_state == 0) {
							
							// Monsieur n'avait pas de commande, on va donc lui faire son petit sushi
							this.customers[i].hasbeen_waiting = System.currentTimeMillis()/1000;
							this.customers[i].previous_state = 1;
							this.MakeSushi(sushiType);
						}
						else {							
							// Si ça fait longtemps qu'il attend, on le remet sur la liste.
							if( (System.currentTimeMillis()/1000)-25 >= this.customers[i].hasbeen_waiting) {
								this.customers[i].previous_state = 0;
							}
						}
					}
					else {
						// Monsieur n'a pas demandé de sushi
						this.customers[i].previous_state = 2;
					}

					// Si on a un type pas content.
					if(checkHappiness(i)) {
						
						
						// Et qu'on a du saké.
						if(ingredients[6].stock > 0) {
							giveSake(i);
						}
						
						
					}
					
				}
			}
				// Le type a passé commande et l'a reçu, on check si on peut virer l'assiète
				if(!checkACustomer(i)) {
					this.removeAssiete(i);
					this.customers[i].previous_state = 0;
				}

		}
		
		checkSake();
		checkLevelUp();
	    checkAllCustomers();
	}
	
	private void giveSake(int i) throws Exception {
		// Donc on veut donner du saké à mamie.
		// First on doit trouver la bouteille.
		
		BufferedImage screenshot = cv.takeRegion(new Rectangle(gameRegion.x+395,gameRegion.y+286,55,200));
		BufferedImage sake = ImageIO.read(new File("img/sake.png"));
		
		int[] matches = cv.findsubImage(sake,screenshot);
		// Normalement si on est là c'est qu'on a forcément du saké, m'enfin failsafe.
			if(matches[0] > -1) {

				cv.dragNdrop(gameRegion.x+matches[0]+400, gameRegion.y+matches[1]+291, gameRegion.x+customers[i].position[0]+15, gameRegion.y+customers[i].position[1]);
				ingredients[6].stock--;
				
				if(ingredients[6].stock == 0) {
					// Recheck
					BufferedImage screenshot2 = cv.takeRegion(new Rectangle(gameRegion.x+395,gameRegion.y+286,55,200));
					int[] bad = cv.findsubImage(sake,screenshot2);
					
					// Si effectivement y'en a plus sur la table on recommande.
					if(bad[0] == -1) replenish(6);
					else ingredients[6].stock = 1;
					}
				}
			}
	
	private void checkSake() throws Exception {
			// Si on avait plus de Saké
		if(ingredients[6].stock == 0) {
			BufferedImage screenshot = cv.takeRegion(new Rectangle(gameRegion.x+395,gameRegion.y+286,55,200));
			BufferedImage sake = ImageIO.read(new File("img/sake.png"));
			
			// Mais qu'en fait on en a.
			int[] matches = cv.findsubImage(sake,screenshot);
				// C'est qu'on en a deux.
				if(matches[0] > -1) ingredients[6].stock = 2;
		}
	}
	
	private boolean overFlowSushiCheck() throws Exception {
		BufferedImage screenshot = cv.takeRegion(new Rectangle(this.gameRegion.x,this.gameRegion.y,640,480));
		BufferedImage empty = ImageIO.read(new File("img/empty_sushi.png"));
		
			int[] matches = cv.findsubImage(empty,screenshot);
		
				// Le "truc" est vide.
			if(matches[0] > -1) {
				// Donc tu peux clicker
				return true;
			}
			else return overFlowSushiCheck();
	}
	
	private int checkSushi(int nbC) throws IOException {
		int sushiType = -1;
		
			int x = this.gameRegion.x + this.customers[nbC].position[0];
			int y = this.gameRegion.y + this.customers[nbC].position[1] - 105;
			
			BufferedImage screenshot = cv.takeRegion(new Rectangle(x,y,70,70));
			 // Pour chaque sushi concerné par le level
			for(int d=0;d<3+level;d++) {
				BufferedImage sushi = ImageIO.read(new File(sushis[d].sprite));
				int[] matches = cv.findsubImage(sushi,screenshot);
				
				if(matches[0] > -1) {
					sushiType = d;
					break;
				}
				
			}
			
		return sushiType;
	}
	
	private boolean checkHappiness(int i) throws Exception {

		
		int x =  this.gameRegion.x + this.customers[i].position[0]-10;
		int y =  this.gameRegion.y + this.customers[i].position[1]-51;		
		
		BufferedImage screenshot = cv.takeRegion(new Rectangle(x,y,65,15));
		BufferedImage bubbles = ImageIO.read(new File("img/bubbles.png"));
		
			int[] check = cv.findsubImage(bubbles,screenshot);

			if(check[0] > -1) return true;
			else return false;
	}
	
	private boolean checkACustomer(int nbC) throws IOException {
		boolean isThere = true;
		
		int x =  this.gameRegion.x + this.customers[nbC].position[0];
		int y =  this.gameRegion.y + this.customers[nbC].position[1];		
		
		BufferedImage screenshot = cv.takeRegion(new Rectangle(x,y,15,40));
		BufferedImage empty_seat = ImageIO.read(new File("img/empty_seat.png"));
			
		matches = cv.findsubImage(empty_seat,screenshot);
		if(matches[0] != -1) isThere = false;	
		
		return isThere;
	}
	
}