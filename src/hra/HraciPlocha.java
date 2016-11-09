package hra;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.Timer;

import obrazek.Obrazek;
import obrazek.ZdrojObrazkuSoubor;

public class HraciPlocha extends JPanel{
	public static final int vyska = 800;
	public static final int sirka = 600;
	
	//rychlost behu pozadi - doleva
	public static final int rychlost = -2;
	
	private BufferedImage imgPozadi;
	private Timer casovacAnimace;
	private boolean pauza = false;
	private boolean hraBezi = false;
	private int posunPozadiX = 0;
	
	
	public HraciPlocha(){
		ZdrojObrazkuSoubor z = new ZdrojObrazkuSoubor();
		z.naplnMapu();
		z.setZdroj(Obrazek.POZADI.getKlic());
		
		try {
			imgPozadi = z.getObrazek();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//z JPanelu - pøekreslení panelu
	public void paint(Graphics g){
		super.paint(g); 				//základní vykreslení
		
		g.drawImage(imgPozadi, posunPozadiX, 0, null); //nalepeni prvniho obrazku
		g.drawImage(imgPozadi, posunPozadiX+imgPozadi.getWidth(), 0, null); //druhy obrazek za nej
		
		
		
	}
	
	private void posun(){
		if(!pauza && hraBezi){		//hra pobezi a nebude pauza
			
			
			
			//posun pozice pozadi hraci prochy (scroll)
			posunPozadiX = posunPozadiX + HraciPlocha.rychlost;
			
			//kdyz se pozadi doposouva, zacne se od zacatku
			if(posunPozadiX == -imgPozadi.getWidth()){
				posunPozadiX = 0;
			}
		}
	}
	
	private void spustHru(){
		casovacAnimace = new Timer(20, new ActionListener() { //po 20ms se refreshne
			@Override
			public void actionPerformed(ActionEvent e) {
				repaint();
				posun();
			}
		});
		
		hraBezi = true;
		casovacAnimace.start(); //spousteci metoda
	}
	
	public void pripravHraciPlochu(){
		this.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e){
				if(e.getButton() == MouseEvent.BUTTON1){ //stisk leveho tlacitka
					//TODO skok hrace
				}
				
				if(e.getButton() == MouseEvent.BUTTON3){
					if(hraBezi){
						if(pauza){
							pauza = false;			//klikáním se pauzuje a odpauzuje
						}else{
							pauza = true;
						}
					}else{
						pripravNovouHru();
						spustHru();
					}
				}
			}
			
		});
		
		setSize(sirka, vyska);
	}
	
	protected void pripravNovouHru(){
		
	}
	
}
