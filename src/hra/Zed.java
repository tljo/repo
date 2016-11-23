package hra;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Zed {

	public static final int sirka = 45;
	
	//rychlost pohybu zdi
	public static final int rychlost = -6;
	
	//mezi horni a dolni casti zdi
	public static final int mezera = 200;

	private static int vzdalenostPosledniZdi = 0;
	
	//ruzne zdi ruzne obrazky => nelze pouzit static
	private static BufferedImage img = null;
	
	//x-ova souradnice zdi (meni se zprava doleva)
	private int x;
	
	//y-ova souradnice zdi (horni souradnice spodni casti zdi)
	private int y;
	
	private int vyska;
	
	private Random random;
	
	
	public Zed(int vzdalenost){
		this.x = vzdalenost;
		
		random = new Random();
		generujRandomValuesForWalls();
	}


	private void generujRandomValuesForWalls() {
		//y-ova souradnice horni casti spodni zdi
		y = random.nextInt(HraciPlocha.vyska-400) + mezera; //aby zbyla mezera mezi horní a dolní zdí
		
		//vyska spodni casti zdi
		vyska = HraciPlocha.vyska - y;
	}
	
	public void paint(Graphics g){
		//spodni cast zdi
		g.drawImage(img, x, y, null);
		
		//horni cast zdi
		g.drawImage(img, x, (- HraciPlocha.vyska) + (y - mezera), null); //obrazek max nahoru a pak ho posuneme trochu dolù
	}
	
	public void posun(){
		//posun zdi
		x = x + Zed.rychlost;
		
		//když se zeï posunem hrací plochy zprava doleva dostane za konec okna, tak nové umístìní
		if(x <= 0 - Zed.sirka){
			x = Zed.vzdalenostPosledniZdi;
		}
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public static void setVzdalenostPosledniZdi(int vzdalenostPosledniZdi){
		Zed.vzdalenostPosledniZdi = vzdalenostPosledniZdi;
	}
	
	public static int getVzdalenostPosledniZdi() {
		return vzdalenostPosledniZdi;
	}
	
	public static void setObrazek(BufferedImage img){
		Zed.img = img;
	}
	
	
	
	
	
	
}