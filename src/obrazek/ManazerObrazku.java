package obrazek;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ManazerObrazku {
	private Map<String, BufferedImage> mapaObr;
	private ZdrojObrazku zo;
	
	public ManazerObrazku(ZdrojObrazku zo){
		mapaObr = new HashMap<>();
		this.zo = zo;
		this.zo.naplnMapu();
	}
	
	private void pripravObrazek(Obrazek o){
		zo.setZdroj(o.getKlic());
		mapaObr.put(o.getKlic(), nactiObrazek(o));
	}

	public void pripravObrazky(){		//po dokonèení bude hashmapa naplnìná obrázky
		pripravObrazek(Obrazek.HRAC);
		pripravObrazek(Obrazek.POZADI);
		pripravObrazek(Obrazek.ZED);
	}
	
	private BufferedImage nactiObrazek(Obrazek o){
		BufferedImage img;
		
		try{ //když se obrázek nepodaøí naèíst
			
			img = zo.getObrazek();
			
			if(img != null){ //ovìøení, že obrázek existuje - server mùže dát prázdný obr
				if(! obrazekMaSpravneRozmery(img, o.getSirka(), o.getVyska())){
					img = upravObrazek(img, o.getSirka(), o.getVyska());
				}
			}else{
				img = vyrobObrazek(o.getSirka(), o.getVyska(), o.getBarva());
			}
			
			
		}catch(IOException e){
			img = vyrobObrazek(o.getSirka(), o.getVyska(), o.getBarva());
		}
		
		return img;
	}
	
	private BufferedImage upravObrazek(BufferedImage img, int sirka, int vyska) {
		BufferedImage zmenenyImage = new BufferedImage(sirka, vyska, img.getType());
		Graphics2D g = zmenenyImage.createGraphics();
		g.drawImage(img, 0, 0, sirka, vyska, null);
		g.dispose();   //likvidace plátna a vrátí se zmìnìný obrázek
		return zmenenyImage;
	}

	private boolean obrazekMaSpravneRozmery(BufferedImage img, int sirka, int vyska) {
		return (img.getWidth() == sirka) && (img.getHeight() == vyska);
	}

	private BufferedImage vyrobObrazek(int sirka, int vyska, Color barva) {
		BufferedImage img = new BufferedImage(sirka, vyska, BufferedImage.TYPE_3BYTE_BGR);  //vyrobení prázdného obrázku
		Graphics2D g = img.createGraphics();
		g.setColor(barva);
		g.fillRect(0, 0, sirka, vyska);  //urèení levého horního rohu a pak rozmìry
		g.dispose();
		return img;
	}

	public BufferedImage getObrazek(Obrazek o){
		return mapaObr.get(o.getKlic());
	}
	
}
