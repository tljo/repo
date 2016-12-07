package app;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import hra.HraciPlocha;
import obrazek.ManazerObrazku;
import obrazek.ZdrojObrazkuSoubor;

public class FlappyFimHlavniApp extends JFrame{
	private ManazerObrazku mo;

	public FlappyFimHlavniApp(){
		mo = new ManazerObrazku(new ZdrojObrazkuSoubor());
	}
	
	public void spust(){
		class Vlakno extends SwingWorker<Object, Object>{
			private JFrame vlastnik;
			private JLabel lb;
			private HraciPlocha hraciPlocha;
			
			public void setVlastnik(JFrame vlastnik) {
				this.vlastnik = vlastnik;
			}
			
			public void doBeforeExecute(){
				lb = new JLabel("Loading, Please wait...");
				lb.setFont(new Font("Arial", Font.BOLD, 42));
				lb.setForeground(Color.RED);
				lb.setHorizontalAlignment(SwingConstants.CENTER);
				
				vlastnik.add(lb);
				lb.setVisible(true);
				vlastnik.revalidate(); //revalidace komponent
				vlastnik.repaint();
			}
			
			@Override
			protected Object doInBackground() throws Exception {  //na pozad� se na�tou obr�zky a p�iprav� se plocha, kdy� bude u�ivatel koukat na hl�ku Loading...
				mo.pripravObrazky();
				hraciPlocha = new HraciPlocha(mo);
				hraciPlocha.pripravHraciPlochu();
				return null;
			}

			@Override
			protected void done() {
				super.done();
				
				vlastnik.remove(lb);
				vlastnik.revalidate();
				vlastnik.add(hraciPlocha);
				hraciPlocha.setVisible(true);
				vlastnik.revalidate();
				vlastnik.repaint();
			}
		}
		
		Vlakno v = new Vlakno(); //vytvo�en� a� po definici t��dy!
		v.setVlastnik(this);
		v.doBeforeExecute();
		v.execute();           //vl�kno se nastartuje a metody se provedou
	}
	
	public void initGUI(){
		setSize(HraciPlocha.sirka, HraciPlocha.vyska);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("FlappyFIM");
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				FlappyFimHlavniApp app = new FlappyFimHlavniApp();
				app.initGUI();
				app.spust();
			}
		});

	}

}
