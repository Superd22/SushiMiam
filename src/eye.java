import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Image.*;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.imageio.ImageIO;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class eye {
	
	private static Robot r;
	static {
	    try {
	        r = new Robot();
	    } catch(AWTException e){e.printStackTrace();}
	}
	
	static void main(String[] args) {
	}
	
	private String convertImgToString(BufferedImage img) throws IOException {
		
		int width = img.getWidth();
		int height = img.getHeight();
		
		int size = width*height;
		
		List<Integer> ints = new ArrayList<Integer>();
		
		for(int i=0;i<size;i++) {
			int[] cords = this.getCoordFromInd(i, width);
				ints.add(img.getRGB(cords[0], cords[1]));
		}
		
		return ints.toString();
	}
	
	
	private int[] getCoordFromInd(int index, int width) {
		int x = index % width;
		int y = (index - x) / width;
		
		int[] cords = {x,y};
		return cords;
	}
	
	
	public List<Integer> findsubImage(BufferedImage sub_img, BufferedImage big_img) throws IOException {
		// Donc attention, ça va être la fête.
		
		
		File outputfile = new File("testoutput.png");
		ImageIO.write(sub_img, "png", outputfile);
		
		String sub = this.convertImgToString(sub_img);
		String big = this.convertImgToString(big_img);

		
		List<Integer> matches = new Vector<Integer>();
		
			// Tailles
			int big_w = big_img.getWidth();
			int sub_w = sub_img.getWidth();
			
			int m = big_img.getHeight() * big_w;
			int n = sub_img.getHeight() * sub_w;
			
			// Prépération tableau
			Map<Character, Integer> rightMostIndexes = this.preprocessForBadCharacterShift(sub);
			
			// Alignement
			int alignedAt = 0;
				while(alignedAt + (n - 1) < m ) {
					// A chaque fois que l'on est aligné, on scan pour le pattern de droite à gauche
					// a la position x du big et y du sub 
						for(int indexInPattern = n - 1; indexInPattern >= 0; indexInPattern--) {
							// On se décale;
							

							int indexInText = alignedAt + indexInPattern;
							
							char x = big.charAt(indexInText);
							char y = sub.charAt(indexInPattern);
							
							// Si sub est plus grand que big, on n'a pas de match
							if(indexInText >= m) break;
							
							
							// Si on a un mismatch, on shift
							if (x != y) {
								// On récupère l'index du x 
								Integer r = rightMostIndexes.get(x);
								
								// Si r n'est pas dans sub, on peut sauter toute la chaine
								if (r == null) { alignedAt = indexInText + 1;}
								else {
									// Sinon, c'est on shift vers la droite jusqu'a l'occurence de r;
									int shift = indexInText - (alignedAt + r);
									alignedAt += shift > 0 ? shift : alignedAt + 1;
								}
								break;
							}
							// Si au contraire on match
							else if (indexInPattern == 0) {
								matches.add(alignedAt);
								alignedAt++;
							}
						}
					}
		
		return matches;	
	}
	
	private Map<Character, Integer> preprocessForBadCharacterShift(String pattern) {
		Map<Character, Integer> map = new HashMap<Character, Integer>();
		for (int i = pattern.length() - 1; i >= 0; i--) {
			char c = pattern.charAt(i);
			if (!map.containsKey(c)) map.put(c, i);
		}
		return map;
	}
	
	public BufferedImage takescreen() throws Exception{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle screenRectangle = new Rectangle(screenSize);
		BufferedImage shot = this.takeRegion(screenRectangle);
		
		
		return shot;
	}
	
	public BufferedImage takeRegion(Rectangle region) {
		BufferedImage shot = r.createScreenCapture(region);
		return shot;
	}
	
	
}
