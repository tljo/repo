package hra;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Hrac {
	public static final int sirka = 40;
	public static final int vyska = 33;
	
	private static final int koef_zrychleni = 1; //velikost skoku hrace
	private static final int koef_rychlost = 2;  //rychlost padu hrace
	
	private BufferedImage img = null;
	
	private int x; //pocatecni x-ova pozice hrace, nemeni se (skoky jen nahoru)
	private int y; //pocatecni y-ova pozice hrace, meni se
	private int rychlost; //bude ovlivnena koeficienty
	
	
	public Hrac(BufferedImage img){
		this.img = img;
		y = HraciPlocha.vyska / 2; //vyska, ve ktere bude hrac - v pulce
		x = (HraciPlocha.sirka / 2) - (img.getWidth() / 2); //stred obrazku je ve stredu hraci plochy
		
		rychlost = koef_rychlost; //to bude pocatecni rychlost
	}
	
	/**
	 * vola se po narazu do zdi nebo do okraje okna
	 */
	public void reset(){
		y = HraciPlocha.vyska / 2; //hrac do puvodni pozice
		rychlost = koef_rychlost;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public void skok(){
		rychlost = -17;  //pri skoku se snizi rychlost
	}
	
	/**
	 * zajistuje pohyb hrace
	 */
	public void posun(){
		rychlost = rychlost + koef_zrychleni;
		y = y + rychlost; //posune se o rychlost.. napr je na 100 a skoci tak se posune na 83
	}
	
	public void paint(Graphics g){
		g.drawImage(img, x, y, null);
		
		if(HraciPlocha.DEBUG){
			g.setColor(Color.WHITE);
			g.drawString("[x =" + x + ", y =" + y + ". rychlost =" + rychlost +"]", x, y-5);
		}
	}
	
	public int getVyskaHrace(){
		return img.getHeight(); //vracime rozmer obrazku
	}
	
	/**
	 * vraci pomyslny ctverec/obdelnik, ktery opisuje hrace
	 */
	public Rectangle getMez(){
		return new Rectangle(x, y, img.getWidth(), img.getHeight());
	}
	
}
