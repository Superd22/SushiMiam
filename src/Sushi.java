public class Sushi {
	public int type;
	public int[] Recipe;
	public String sprite;
	//	private int[][] sushiRecipe = new int[][]{{0,2,1,0,0,0},{0,1,1,1,0,0},{0,1,1,2,0,0}};

	public static final int ONIGIRI = 0, CALIFORNIA = 1, GUNKAN = 2, SALMON = 3, SHRIMP = 4, UNAGI = 5, DRAGON = 6, COMBO = 7;
	
	
	Sushi(int i) {
		this.type = i;
		switch(i) {
			case ONIGIRI: 
				this.Recipe = new int[]{0,2,1,0,0,0};
				this.sprite = "img/sushis/onigiri.png";
					break;
					
			case CALIFORNIA: 
				this.Recipe = new int[]{0,1,1,1,0,0};
				this.sprite = "img/sushis/california.png";
					break;
					
			case GUNKAN: 
				this.Recipe = new int[]{0,1,1,2,0,0};
				this.sprite = "img/sushis/gunkan.png";
					break;
					
			case SALMON: 
				this.Recipe = new int[]{0,1,1,0,2,0};
				this.sprite = "img/sushis/sroll.png";
					break;
					
			case SHRIMP: 
				this.Recipe = new int[]{2,1,1,0,0,0};
				this.sprite = "img/sushis/shrimp.png";
					break;
					
			case UNAGI:
				this.Recipe = new int[]{0,1,1,0,0,2};
				this.sprite = "img/sushis/unagi.png";
					break;
			
			case DRAGON:
				this.Recipe = new int[]{0,2,1,1,0,2};
				this.sprite = "img/sushis/dragon.png";
					break;
			
			case COMBO:
				this.Recipe = new int[]{1,2,1,1,1,1};
				this.sprite = "img/sushis/combo.png";
		}
	}
	
	
	private void MakeSushi() throws Exception {
		// Pour chaque ingrédient
		// (int) nbr  : contient le nombre 
		// (int) refIngredient : contient la référence de l'ingrédient en cours
		
		int refIngredient = 0;
		
		// On s'occupe du stock tkt.
		handleStock(type);
		
		// On verifie que l'on peut envoyer un sushi.
		if(overFlowSushiCheck()) {
			for(int nbr : this.sushis[type].Recipe) {
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
	
}
