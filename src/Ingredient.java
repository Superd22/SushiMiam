public class Ingredient {
	public int type;
	public int stock;
	public int replenishes;
	public int[] position_recette;
	public int[] position_commande;
	
	// 0 = Shrimp / 1 = Riz / 2 = Feuille Verte / 3 = fish egg / 4 = salmon / 5 = unagi 
	
	Ingredient(int i)  {
		this.type = i;
		
		switch(i) {
			case 0 : this.position_recette = new int[]{42,330}; this.position_commande = new int[]{480,225}; 
					 this.stock = this.replenishes = 5; break;
			case 1 : this.position_recette = new int[]{92,330}; this.position_commande = new int[]{550,275}; 
			 		 this.stock = this.replenishes = 10; break;
			case 2 : this.position_recette = new int[]{42,388}; this.position_commande = new int[]{570,225};
					 this.stock = this.replenishes = 10; break;
			case 3 : this.position_recette = new int[]{92,388}; this.position_commande = new int[]{480,280}; 
	 		 		 this.stock = this.replenishes = 10; break;
			case 4 : this.position_recette = new int[]{42,446}; this.position_commande = new int[]{570,280};
					 this.stock = this.replenishes = 5; break;
			case 5 : this.position_recette = new int[]{92,446}; this.position_commande = new int[]{480,325};
					 this.stock = this.replenishes = 5; break;
		}
		
	}
}
