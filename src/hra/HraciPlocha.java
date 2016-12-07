package hra;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import hra.Zed;
import obrazek.ManazerObrazku;
import obrazek.Obrazek;
import obrazek.ZdrojObrazkuSoubor;

public class HraciPlocha extends JPanel{
	private static final long serialVersionUID = 1L;
	public static final int vyska = 800;
	public static final int sirka = 600;

	//aspon 3 zdi, aby se prvni stihla posunout za okraj
	public static final int pocet_zdi = 4;
	
	public static final boolean DEBUG = true;
	
	//rychlost behu pozadi - doleva
	public static final int rychlost = -5;
	
	private Hrac hrac;
	
	private BufferedImage imgPozadi;
	private Timer casovacAnimace;
	private boolean pauza = false;
	private boolean hraBezi = false;
	private int posunPozadiX = 0;
	
	private SeznamZdi seznamZdi;
	private Zed aktualniZed;
	private Zed predchoziZed;
	
	private int skore = 0;
	private JLabel lbSkore;
	private JLabel lbZprava;
	private Font font;
	private Font zpravaFont;
	
	
	public HraciPlocha(ManazerObrazku mo){
		imgPozadi = mo.getObrazek(Obrazek.POZADI);
		hrac = new Hrac(mo.getObrazek(Obrazek.HRAC));
		Zed.setObrazek(mo.getObrazek(Obrazek.ZED));
		
		seznamZdi = new SeznamZdi();
		vyrobLabely();
	}
	
	private void vyrobLabely() {
		font = new Font("Courier New", Font.BOLD, 40);
		zpravaFont = new Font("Courier New", Font.BOLD, 20);
		this.setLayout(new BorderLayout());
		
		lbZprava = new JLabel();
		lbZprava.setFont(zpravaFont);
		lbZprava.setForeground(Color.RED); //barva zpravy na konci a na zaèátku
		lbZprava.setHorizontalAlignment(SwingConstants.CENTER);
		
		lbSkore = new JLabel("0");
		lbSkore.setFont(font);
		lbSkore.setForeground(Color.WHITE); //barva skore nahoøe
		lbSkore.setHorizontalAlignment(SwingConstants.CENTER);
		
		this.add(lbSkore, BorderLayout.NORTH);
		this.add(lbZprava, BorderLayout.CENTER);
	}
	
	private void vyrobZdi(){
		Zed zed;
		int vzdalenost = HraciPlocha.sirka;
		
		for(int i = 0; i < pocet_zdi; i++){
			zed = new Zed(vzdalenost);
			seznamZdi.add(zed);
			vzdalenost = vzdalenost + (HraciPlocha.sirka/2);
		}
		
		vzdalenost = vzdalenost - HraciPlocha.sirka - Zed.sirka;
		Zed.setVzdalenostPosledniZdi(vzdalenost);
	}
	
	//z JPanelu - pøekreslení panelu
	public void paint(Graphics g){
		super.paint(g); 				//základní vykreslení
		
		g.drawImage(imgPozadi, posunPozadiX, 0, null); //nalepeni prvniho obrazku
		g.drawImage(imgPozadi, posunPozadiX+imgPozadi.getWidth(), 0, null); //druhy obrazek za nej
		
		if(HraciPlocha.DEBUG){
			g.setColor(Color.WHITE);
			g.drawString("posunPozadiX = " + posunPozadiX, 0, 10);
		}
		
		for(Zed zed: seznamZdi){
			zed.paint(g);
		}
		
		hrac.paint(g);
		
		lbSkore.paint(g);
		lbZprava.paint(g);
	}
	
	private void posun(){
		if(!pauza && hraBezi){		//hra pobezi a nebude pauza
			aktualniZed = seznamZdi.getAktualniZed();
			predchoziZed = seznamZdi.getPredchoziZed();
			
			if(jeZedKolize(predchoziZed, hrac) ||
				jeZedKolize(aktualniZed, hrac) ||
				jeHraciPlochaKolize(hrac)){
					konecHryReset();
			}else{
				for(Zed zed: seznamZdi){
					zed.posun();
				}
			}

			hrac.posun();
			
			if(hrac.getX() >= aktualniZed.getX()){
				seznamZdi.nastavDalsiZedNaAktualni();
				incrementSkoreZed();
				lbSkore.setText(skore + "");
			}
			
			//posun pozice pozadi hraci prochy (scroll)
			posunPozadiX = posunPozadiX + HraciPlocha.rychlost;
			
			//kdyz se pozadi doposouva, zacne se od zacatku
			if(posunPozadiX == -imgPozadi.getWidth()){
				posunPozadiX = 0;
			}
		}
	}
	
	private void konecHryReset(){
		hraBezi = false;
		casovacAnimace.stop();
		casovacAnimace = null;
		resetHra();
		setZpravaZedKolize();
	}
	
	private boolean jeZedKolize(Zed zed, Hrac hrac){
		return zed.getLowerRect().intersects(hrac.getMez()) || zed.getUpperRect().intersects(hrac.getMez());
	}
	
	private boolean jeHraciPlochaKolize(Hrac hrac){
		return hrac.getY() < 0 || hrac.getY() >= HraciPlocha.vyska;
	}
	
	private void spustHru(){
		casovacAnimace = new Timer(20, new ActionListener() { //po 20ms se refreshne
			@Override
			public void actionPerformed(ActionEvent e) {
				repaint();
				posun();
			}
		});
		setMessageEmpty();
		hraBezi = true;
		casovacAnimace.start(); //spousteci metoda
	}
	
	public void pripravHraciPlochu(){
		setMessageControls();
		this.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e){
				if(e.getButton() == MouseEvent.BUTTON1){ //stisk leveho tlacitka
					hrac.skok();
				}
				
				if(e.getButton() == MouseEvent.BUTTON3){
					if(hraBezi){
						if(pauza){
							setMessageEmpty();
							pauza = false;			//klikáním se pauzuje a odpauzuje
						}else{
							setMessagePaused();
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
		resetHra();
	}
	
	private void resetHra() {
		resetZdi();
		hrac.reset();
		lbSkore.setText(skore + "");
		resetSkore();
	}

	private void resetSkore() {
		skore = 0;		
	}
	
	private void incrementSkoreZed() {
		skore += Zed.body_pro_zed;
	}

	private void resetZdi() {
		seznamZdi.clear();
		vyrobZdi();
	}
	
	private void setZpravaZedKolize() {
		lbZprava.setFont(font);
		lbZprava.setText("Chyba!");
	}
	
	private void setMessagePaused() {
		lbZprava.setFont(font);
		lbZprava.setText("PAUZA");
	}
	
	private void setMessageControls() {
		lbZprava.setFont(zpravaFont);
		lbZprava.setText("levé tl. myši = skok, právé tl. myši = pauza");
	}
	
	private void setMessageEmpty() {
		lbZprava.setText("");
	}
	
}
