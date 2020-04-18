import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.AbstractList;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

public class CountryMap {
	
	private int samplesPerAxisPerBox;

	private BufferedImage imageBuffer;
	int maxColorDistance;
	int boxWidth = 0;
	int boxHeight = 0;
	AbstractMap<Color, Integer> colorMapping = new HashMap<Color, Integer>();
	
	
	public CountryMap(String path, int maxColorDistance, int samplesPerAxisPerBox) throws Exception {
		InputStream inStream = new FileInputStream(path);
		imageBuffer = ImageIO.read(inStream);
		inStream.close();
		this.maxColorDistance = maxColorDistance;
		this.samplesPerAxisPerBox = samplesPerAxisPerBox;
	}

	public int getWidth() {
		return imageBuffer.getWidth();
	}
	
	public int getHeight() {
		return imageBuffer.getHeight();
	}
	
	public void setBoxSize(int width, int height) {
		boxWidth = width;
		boxHeight = height;
	}
	
	public void addColorDensity(Color color, int density) {
		colorMapping.put(color, density);
	}
	
	public int getPopulationDensity(int gridX, int gridY) {
		AbstractMap<Color, Integer> colorCounts = new HashMap<Color, Integer>();
		for (int pixelX = gridX * boxWidth + boxWidth / 2 / samplesPerAxisPerBox; pixelX < Math.min(getWidth(), (gridX+1) * boxWidth); pixelX += boxWidth / samplesPerAxisPerBox) {
			for (int pixelY = gridY * boxHeight + boxHeight / 2 / samplesPerAxisPerBox; pixelY < Math.min(getHeight(), (gridY+1) * boxHeight); pixelY += boxWidth / samplesPerAxisPerBox) {
				int argb = imageBuffer.getRGB(pixelX, pixelY);
				int r = (argb & 0xff0000) >> 16;
				int g = (argb & 0x00ff00) >>  8;
				int b = (argb & 0x0000ff);
				for (Iterator<Color> it =  colorMapping.keySet().iterator(); it.hasNext(); ) {
					Color color = it.next();
					double colorDistance = Math.sqrt( (r - color.getRed()) * ( r - color.getRed())
							+ (g - color.getGreen()) * (g - color.getGreen())
							+ (b - color.getBlue()) * (b - color.getBlue()) );
					if (colorDistance <= maxColorDistance) {
						Integer lastCount = colorCounts.get(color);
						if (lastCount != null) {
							colorCounts.put(color,  lastCount.intValue()+1);
						} else {
							colorCounts.put(color, 1);
						}
					}
				}
			}
		}
		Color topColor = null;
		int topCount = -1;
		for (Iterator<Entry<Color, Integer>> it = colorCounts.entrySet().iterator(); it.hasNext(); ) {
			Entry<Color, Integer> ent = it.next();
			if (ent.getValue().intValue() > topCount) {
				topColor = ent.getKey();
				topCount = ent.getValue().intValue();
			}
		}
		
		int density = 0;
		if (topColor != null) {
			Integer densityObj = colorMapping.get(topColor);
			if (densityObj != null) {
				density = densityObj.intValue();
			}
		}
		return density;
	}
	
	public static void main(String[] args) {
		try {
			CountryMap countryMap = new CountryMap(args[0], 55, 3);
			System.out.println("width="+countryMap.getWidth()+", height="+countryMap.getHeight());
			int numXBoxes = countryMap.getWidth()/10;
			int numYBoxes = countryMap.getHeight()/10;
			countryMap.addColorDensity(new Color(237,  88, 146), 2000);
			countryMap.addColorDensity(new Color(244, 116,  82),  750);
			countryMap.addColorDensity(new Color(252, 197,  97),  300);
			countryMap.addColorDensity(new Color(249, 229,   9),   75);
			countryMap.addColorDensity(new Color(186, 210,  44),   30);
			countryMap.addColorDensity(new Color(105, 180,  95),    5);
			countryMap.addColorDensity(new Color(  0, 159, 139),    1);
			countryMap.setBoxSize(countryMap.getWidth() / numXBoxes, countryMap.getHeight() / numYBoxes);
			for (int j=0; j < numYBoxes; ++j) {
				for (int i=0; i < numXBoxes; ++i) {
					System.out.print(String.format(" %6d", countryMap.getPopulationDensity(i,  j)));
				}
				System.out.println();
			}
			System.out.println();
			for (int j=0; j < numYBoxes; ++j) {
				for (int i=0; i < numXBoxes; ++i) {
					System.out.print(countryMap.getPopulationDensity(i,  j) > 0 ? "XX" : "  ");
				}
				System.out.println();
			}
		} catch (Exception ex) {
			System.out.println("Error: "+ex);
			System.exit(1);
		}
	}

}
