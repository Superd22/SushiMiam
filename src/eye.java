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
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
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
	
	private String convertImgToString(BufferedImage img, final String formatName) throws IOException {
		  final ByteArrayOutputStream os = new ByteArrayOutputStream();
		    try {
		        ImageIO.write(img, formatName, Base64.getEncoder().wrap(os));
		        return os.toString(StandardCharsets.ISO_8859_1.name());
		    } catch (final IOException ioe) {
		        throw new UncheckedIOException(ioe);
		    }
	}
	
	public List<Integer> findsubImage(BufferedImage sub_img, BufferedImage big_img) throws IOException {
		// Donc attention, ça va être la fête.
		
		//String sub = this.convertImgToString(sub_img,"png");
		//String big = this.convertImgToString(big_img,"png");
		
		
		String big = "Ma bite est super longue comme dirait paul c'est cool de discuter avec lui car il a une putain de grosse teub sa mére la pute c'est génial.";
		String sub = "bite";
		List<Integer> matches = new Vector<Integer>();
		
			// Tailles
			int m = big.length();
			int n = sub.length();	
						
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
