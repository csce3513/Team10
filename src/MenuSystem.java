import java.awt.event.*;
import java.awt.*;
import java.applet.*;
import javax.swing.*;

import java.net.*;
import java.util.*;
import java.util.List;
import javax.jnlp.FileContents;
import javax.jnlp.FileOpenService;
import javax.jnlp.ServiceManager;


public class MenuSystem extends JApplet{
	DrawSurface drawSurface;
	Image[] images = new Image[10];
	
	public void init() {
		
		Container content_pane = getContentPane();
		setLayout(new BorderLayout());
		URL base = this.getDocumentBase();
		
		images[0] = getImage(base,"splash.png");
		images[1] = getImage(base,"background.png");
		images[2] = getImage(base,"start_button_s.png");
		images[3] = getImage(base,"start_button_m.png");
		images[4] = getImage(base,"option_button_g.png");
		images[5] = getImage(base,"option_button_green.png");		
		images[6] = getImage(base,"lefthand_n.png");
		images[7] = getImage(base,"righthand_n.png");
		images[8] = getImage(base,"lefthand_b.png");
		images[9] = getImage(base,"righthand_b.png");
		//images[7] = getImage(base,"left_hand.png");
		//images[8] = getImage(base,"right_hand.png");
		
		drawSurface = new DrawSurface(images);
		content_pane.add(drawSurface);
		setSize(380,600);
		
	}
	
	public void destroy() {
		
	}
}
class Splash extends JPanel
{
	Image img; 

	public Splash(Image image){
		this.img = image;
	}
	  public void paintComponent (Graphics g) { 
	   super.paintComponent (g); 

	   //Draw image centered in the middle of the panel    
	   g.drawImage (img, 0, 0, this); 
	  } // paintComponent
}
class Background extends JPanel
{
	Image img;
	public Background(Image image)
	{
		this.img = image;
	}
	public void paintComponent (Graphics g) 
	{ 
	  super.paintComponent (g); 	   
	  g.drawImage(img, 0, 0, this);
	} 
}
class Startbutton extends JPanel
{
	Image[] img;
	public Startbutton(Image[] image)
	{
		this.img = image;
	}
	  public void paintComponent (Graphics g) { 
	  super.paintComponent (g); 

	   //Draw image centered in the middle of the panel    
	   g.drawImage(img[2], 0, 0, this);
	   g.drawImage(img[3], 0, 0, this);
	  } // paintComponent
}
class DrawSurface extends JPanel implements MouseListener, MouseMotionListener, KeyListener, Runnable {
	
	int state = 0;
	int start_button_status = 2;
	int option_button_status = 4;
	int lefthand_button_status = 6;
	int righthand_button_status = 7;
	
	int imgX, imgY;
	int x,y;
	boolean button_activator = false;
	Thread stage = null;
	boolean hand = false;	// right hand = false, left hand = true;
	Image[] images;
	
	Splash splash;
	Background background;
	Startbutton startbutton;
	
	public DrawSurface(Image[] i){
		setFocusable(true);
		addMouseMotionListener(this);
		addKeyListener(this);
		addMouseListener(this);
		this.images = i;
		start();
	}
	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		if(state == 1)
		{
			g.drawImage(images[0],0,0,this);

		}
		else if(state == 2) 
		{	
			imgX = (this.getWidth() - images[start_button_status].getWidth(null)) / 2;
		    imgY = (this.getHeight() - images[start_button_status].getHeight(null)) / 2 - 70;
			g.drawImage(images[1],0,0,this);
			g.drawImage(images[start_button_status],imgX,imgY,this);
			imgX = (this.getWidth() - images[option_button_status].getWidth(null)) / 2;
		    imgY = (this.getHeight() - images[option_button_status].getHeight(null)) / 2 + 50;
			g.drawImage(images[option_button_status],imgX,imgY,this);
			//g.drawImage(images[start_button_status],20,140,this);
		}
		else if(state == 3)
		{
			g.drawImage(images[1],0,0,this);
			g.drawImage(images[lefthand_button_status],45,137,this);
			g.drawImage(images[righthand_button_status],212,137,this);
		}
		
		
	}
	public void start()
	{
		if(stage == null)
		stage = new Thread(this);
		stage.start();
	}
	
	public void stop()
	{
		if(stage != null && stage.isAlive())
            stage.stop();	
		stage = null;
	}
	public void run() 
	{		
		while(stage != null)
		{ 	System.out.println("state in run() : "+state);
			if(state < 2) state++;	
			else
				stage = null;
			try { Thread.sleep(3000); } 
			catch (InterruptedException e) {}
			repaint();
			
		}
	}
	@Override
	public void mouseClicked(MouseEvent m) {
		
		x = m.getX();
		y = m.getY();
		System.out.println("x :"+x+"y :"+y);
		System.out.println(state);
		
		if(state == 2 && button_activator)
		{
			System.out.println("s "+state+"b "+button_activator);
			if(x >= 154 && x <= 220) {
				if(y >= 190 && y <= 263) {
					state = 4;	// game start;
					System.out.println("up");
				}
			}
			if(x >= 150 && x <= 230) {
				if(y >= 340 && y <= 355) {
					state = 3;
					repaint();
					System.out.println("option");
				}
			}
		}
		else if(state == 3 && button_activator)
		{
			if(x >= 45 && x <= 167){ 
				if(y >= 137 && y <= 283) hand = true;
			}	
			else if(x >= 225 && x <= 326){ 
				if(y >= 140 && y <= 275) hand = false;
			}	
			state = 2;
			repaint();
		}

	}
	
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseMoved(MouseEvent m) {
		
		int x,y;
		x = m.getX();
		y = m.getY();
		System.out.println("x: "+x+"y: "+y);
		if(state == 2)
		{
			if(x >= 154 && x <= 220) {
				if(y >= 190 && y <= 263) {
					start_button_status = 3;
					button_activator = true;
					repaint();
				}
				else 
				{
					start_button_status = 2; 
					button_activator = false;
					repaint();
				}
			}
			if(x >= 150 && x <= 230) {
				if(y >= 340 && y <= 355) {
					option_button_status = 5;
					button_activator = true;
					repaint();
				}
				else 
				{
					option_button_status = 4; 
					button_activator = false;
					repaint();
				}
			}
			else
			{
				start_button_status = 2; 
				option_button_status = 4;
				button_activator = false;
				repaint();
			}
		}
		else if(state == 3)
		{
			if(x >= 45 && x <= 167) {
				if(y >= 137 && y <= 283) {
					lefthand_button_status = 8;
					button_activator = true;
					repaint();
				}
				else
				{
					lefthand_button_status = 6;
					button_activator = false;
					repaint();
				}
			}
			else if(x >= 225 && x <= 326) {
				if(y >= 140 && y <= 275) {
					righthand_button_status = 9;
					button_activator = true;
					repaint();
				}
				else
				{
					righthand_button_status = 7;
					button_activator = false;
					repaint();
				}
			}
			else
			{
				lefthand_button_status = 6;
				righthand_button_status = 7;
				button_activator = false;
				repaint();
			}
		}
	}
	
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}