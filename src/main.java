import java.util.List;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class main {
	
	public static void main(String[] args) throws Exception{
		eye coucou = new eye();
		
		
		BufferedImage first_screen = coucou.takescreen();
		BufferedImage smaller_screen = coucou.takeRegion(new Rectangle(1,1,1,1));
		
		List<Integer> matches = coucou.findsubImage(smaller_screen,first_screen);
		for (Integer integer : matches) System.out.println("Match at: " + integer);
		System.out.println((matches.equals(Arrays.asList(1, 3)) ? "OK" : "Failed"));
		
	}

}
