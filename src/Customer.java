import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * SushiMiam
 * Sushi.java
 * Enumères les différents Customers à servir.
 *
 * @author David Fain
 * @version 1.0 27/04/2015
 */



public class Customer {
	public int type;
	public int previous_state = 0;
	public long hasbeen_waiting = 0;
	public int[] position;
	
		// new int[][]{{41,151},{141,151},{241,151},{341,151},{441,151},{541,151}};
	

	Customer(int i)  {
		this.type = i;
		
		switch(i) {
			case 0 : this.position = new int[]{41,151};break;
			case 1 : this.position = new int[]{141,151};break;
			case 2 : this.position = new int[]{241,151};break;
			case 3 : this.position = new int[]{341,151};break;
			case 4 : this.position = new int[]{441,151};break;
			case 5 : this.position = new int[]{541,151};break;
		}
		
	}

}
