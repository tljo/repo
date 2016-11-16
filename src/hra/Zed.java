package hra;

import java.awt.image.BufferedImage;

public class Zed {

	public static final int sirka = 45;
	
	//rychlost pohybu zdi
	public static final int rychlost = -6;
	
	//mezi horni a dolni casti zdi
	public static final int mezera = 200;
	
	//ruzne zdi ruzne obrazky => nelze pouzit static
	private static BufferedImage img = null;
	
	//x-ova souradnice zdi (meni se zprava doleva)
	private int x;
	
	//y-ova souradnice zdi (horni souradnice spodni casti zdi)
	private int y;
	
	private int vyska;
	
	
	public Zed(int vzdalenost){
		this.x = vzdalenost;
	}
	
}