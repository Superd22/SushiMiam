import java.util.ArrayList;

/**
 * SushiMiam
 * Sushi.java
 * Enumères les différents Sushi disponibles.
 *
 * @author David Fain
 * @version 1.0 27/04/2015
 */


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

}
