public class Sushi {
	public int type;
	public int[] Recipe;
	//	private int[][] sushiRecipe = new int[][]{{0,2,1,0,0,0},{0,1,1,1,0,0},{0,1,1,2,0,0}};
	
	Sushi(int i) {
		this.type = i;
		switch(i) {
			case 0: this.Recipe = new int[]{0,2,1,0,0,0};break;
			case 1: this.Recipe = new int[]{0,1,1,1,0,0};break;
			case 2: this.Recipe = new int[]{0,1,1,2,0,0};break;
			case 3: this.Recipe = new int[]{0,1,1,0,2,0};break;
		}
	}
}
