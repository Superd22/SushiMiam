/**
 * SushiMiam
 * Ingredient.java
 * Enumères les différents Ingrédients disponibles.
 *
 * @author David Fain
 * @version 1.0 27/04/2015
 */



public class Ingredient {
	public int type;
	public int stock;
	public int replenishes;
	public int[] position_recette;
	public int[] position_commande;
	public String sprite;
	
	// 0 = Shrimp / 1 = Riz / 2 = Feuille Verte / 3 = fish egg / 4 = salmon / 5 = unagi 
	public static final int SHRIMP = 0, RIZ = 1, NORI = 2, FISHEGG = 3, SALMON = 4, UNAGI = 5, SAKE = 6;
	
	
	Ingredient(int i)  {
		this.type = i;
		
		switch(i) {
			case SHRIMP : 
				this.position_recette = new int[]{42,330}; 
				this.position_commande = new int[]{490,225}; 
				this.stock = this.replenishes = 5; 
				this.sprite = "img/order/shrimp.png";
					break;
				
			case RIZ : 
				this.position_recette = new int[]{92,330}; 
				this.position_commande = new int[]{550,275}; 
			 	this.stock = this.replenishes = 10; 
				this.sprite = "img/order/riz.png";
			 		break;
			 		
			case NORI : 
				this.position_recette = new int[]{42,388}; 
				this.position_commande = new int[]{490,275};
				this.stock = this.replenishes = 10; 
				this.sprite = "img/order/salmon.png";
					break;
					
			case FISHEGG : 
				this.position_recette = new int[]{92,388}; 
				this.position_commande = new int[]{575,275}; 
	 		 	this.stock = this.replenishes = 10; 
				this.sprite = "img/order/fishegg.png";
	 		 		break;
	 		 		
			case SALMON : 
				this.position_recette = new int[]{42,446}; 
				this.position_commande = new int[]{490,330};
				this.stock = this.replenishes = 5; 
				this.sprite = "img/order/salmon.png";
					break;
					
			case UNAGI : 
				this.position_recette = new int[]{92,446}; 
				this.position_commande = new int[]{570,230};
				this.stock = this.replenishes = 5;
				this.sprite = "img/order/unagi.png";
					break;
				
			case SAKE :
				this.position_commande = new int[]{550,275}; 
				this.stock = this.replenishes = 2;
				this.sprite = "img/order/sake.png";
		}
		
	}
	
	
	
}