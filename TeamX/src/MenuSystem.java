
import java.awt.event.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.applet.*;
import javax.swing.*;

import java.io.Serializable;
import java.net.*;
import java.util.*;
import java.util.List;
import java.applet.AudioClip;

public class MenuSystem extends JApplet{
	DrawSurface drawSurface;
	Image[] images = new Image[20];
	
	private AudioClip startingMusic, currentSound;
	
	public void init() {
		
		Container content_pane = getContentPane();
		setLayout(new BorderLayout());
		
		images[0] = getToolkit().getImage("images/splash.png");
		images[1] = getToolkit().getImage("images/background.png");
		images[2] = getToolkit().getImage("images/start_button_s.png");
		images[3] = getToolkit().getImage("images/start_button_m.png");
		images[4] = getToolkit().getImage("images/option_button_g.png");
		images[5] = getToolkit().getImage("images/option_button_green.png");		
		images[6] = getToolkit().getImage("images/lefthand_n.png");
		images[7] = getToolkit().getImage("images/righthand_n.png");
		images[8] = getToolkit().getImage("images/lefthand_b.png");
		images[9] = getToolkit().getImage("images/righthand_b.png");
		images[10] = getToolkit().getImage("images/bg.jpg");
		images[11] = getToolkit().getImage("images/bug.png");
		
		//startingMusic = getAudioClip(getDocumentBase(), "startingMusic.wav");
		
		drawSurface = new DrawSurface(images,startingMusic);
		content_pane.add(drawSurface);
		setSize(380,600);
		
	}
	
	public void destroy() {
		
	}

	public boolean imageloaded() {
	
		boolean imageLoad = true;
		
		for(int i = 0; i < 10; i++)
		if (images[i] == null)
		{
			imageLoad = false;
		}
		
		return	imageLoad;
			
	}
}

class DrawInfo implements Serializable{
	private int x,y,radius;
	private Color color;
	
	public DrawInfo(int x, int y, int radius, Color color)
	{
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.color = color;

	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	
}

class DrawSurface extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
	
	int state = 0;
	int start_button_status = 2;
	int option_button_status = 4;
	int lefthand_button_status = 6;
	int righthand_button_status = 7;
	
	int imgX, imgY;
	int bugX, bugY;
	int x,y;
	boolean button_activator = false;
	Thread startthread = new StartThread();
	boolean hand = false;	// right hand = false, left hand = true;
	Image[] images;
	AudioClip startingMusic;
	private Vector vc = new Vector();
	
	
	
	// <============================================== > //
	
	private int speed = 30; //speed of thread
    private int mousex;
    private int mousey;
    private int radius;
    private boolean mousemoved;
    Graphics G;
    
    private Shape shape1, shape2;
    private Area areaOne, areaTwo, test;
     
    static final int X = 380, Y = 250;
    static BufferedImage I;

        
    private Dimension size; //used to set the size of applet
    Random randomGenerator = new Random(); //generator for color, position, & speed
	Thread th = new GameThread(); // thread
	Thread bugthread = new BugThread();
    // < =============================================== > //
    
	
	public DrawSurface(Image[] i, AudioClip startingMusic){
		setFocusable(true);
		addMouseMotionListener(this);
		addKeyListener(this);
		addMouseListener(this);
		this.images = i;
		this.startingMusic = startingMusic;
		startthread.start();
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
		
		else if(state == 4)
		{
			 Graphics2D g2 = (Graphics2D) g;
	         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	         shape1 = new Ellipse2D.Double(25, 125, 350, 425);
	         shape2 = new Ellipse2D.Double(50, 150, 300, 375);
            
	         areaOne = new Area(shape1);
	         test = new Area(shape1);
	         areaTwo = new Area(shape2);
	         
	         if (mousemoved == false)
	         {
	        	 g.drawImage(images[10],0,0,this);
	        	 g.setColor(Color.blue);
	             g.fillOval(100, 200, 200, 275);
	         }
	         
	         g.drawImage(images[10],0,0,this);
	         
	         
	         for(int i = 0; i < vc.size(); i++)
	         {
	        	
	        	 DrawInfo peed = (DrawInfo) vc.elementAt(i);
	        	// System.out.println(peed.getRadius());
	        	 g.setColor(peed.getColor());
	        	 g.fillOval(peed.getX()-(peed.getRadius()/2),peed.getY()-(peed.getRadius()/2),peed.getRadius(),peed.getRadius());
	        	 
	         }
	         
	         
	         g.drawImage(images[11],bugX,bugY,this);
	         
	         g2.setColor(Color.YELLOW);
	         test.subtract(areaTwo);
	         
	         if(!test.contains(mousex, mousey))
	         {
	           if(radius %2 == 1)
	           g.fillOval(getMouseX()-radius/2, getMouseY()-radius/2, radius, radius);
	         }
	            
	         g2.draw(areaOne);
	         g2.draw(areaTwo);
	         g2.setColor(Color.white);
	         g2.fill(test);
	         
		}
		
	}
	
	private class StartThread extends Thread{
		
		StartThread()
		{
			super("StartThread");
		}
		
		public void run()
		{
			//startingMusic.play();
			
			while(true)
			{
				if(state < 2) state++;	
				else
					break;
				try { Thread.sleep(1900); } 
				catch (InterruptedException e) {}
				repaint();
			}
		}
	}
	
	class GameThread extends Thread{
		
		GameThread()
		{
			super("GameThread");
		}
		
		public void run()
		{
			while(true)
			{
				try { Thread.sleep(speed);  } 
				catch (InterruptedException e) {}
				updateColor();
			}
		}
	}
	
	class BugThread extends Thread{
		
		int ranX = 0;
		int ranY = 0;
		int curX = 0;
		int curY = 0;
		
		BugThread()
		{
			super("BugThread");
		}
		
		public void run()
		{
			while(true)
			{

				if(ranX == curX && ranY == curY) {
				
					ranX = randomGenerator.nextInt(380);
					ranY = randomGenerator.nextInt(600);
					
					System.out.println("ranX"+ranX+"ranY"+ranY);
					System.out.println("curX"+curX+"curY"+curY);
				}
				else
				{
					if(curX < ranX)
						curX++;
					else if(curX == ranX)
						;
					else
						curX--;
					
					if(curY < ranY)
						curY++;
					else if(curY == ranY)
						;
					else 
						curY--;
					
					bugX = curX;
					bugY = curY;
				}
				try { Thread.sleep(5);  } 
				catch (InterruptedException e) {}
				repaint();
			}
		}
	}
	@Override
	public void mouseClicked(MouseEvent m) {
		
		x = m.getX();
		y = m.getY();

		if(state == 2 && button_activator)
		{
			if(x >= 154 && x <= 220 && y >= 190 && y <= 263) {
					state = 4;	// game start;
					repaint();
					th.start();
					bugthread.start();
				}
			
			else if(x >= 150 && x <= 230 && y >= 340 && y <= 355) {
					state = 3;
					repaint();
				}
		}
		
		else if(state == 3 && button_activator)
		{
			if(x >= 45 && x <= 167 && y >= 137 && y <= 283)	{ 
				hand = true;
			}	
			else if(x >= 225 && x <= 326 && y >= 140 && y <= 275)	{ 
				hand = false;
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
	
	public void setMouseX(int x){
        mousex = x;
    }
    
    public void setMouseY(int y){
        mousey = y;
    }
    
    public int getMouseX(){
        return mousex;
    }
    
    public int getMouseY(){
        return mousey;
    }
    
	@Override
	public void mouseMoved(MouseEvent m) {
		
		int x,y;
		x = m.getX();
		y = m.getY();
		
		if(state == 2)
		{
			if(x >= 154 && x <= 220 && y >= 190 && y <= 263 )	{ 
					start_button_status = 3;
					button_activator = true;
					repaint();
					
				}

			else if(x >= 150 && x <= 230 && y >= 340 && y <= 355) {
				if(y >= 340 && y <= 355) {
					option_button_status = 5;
					button_activator = true;
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
			if(x >= 45 && x <= 167 && y >= 137 && y <= 283) {
		
				lefthand_button_status = 8;
				button_activator = true;
				repaint();
			}
			
			else if(x >= 225 && x <= 326 && y >= 140 && y <= 275) {
				
				righthand_button_status = 9;
				button_activator = true;
				repaint();
			}
			else
			{
				lefthand_button_status = 6;
				righthand_button_status = 7;
				button_activator = false;
				repaint();
			}
		}
		else if(state == 4)
		{
	        setMouseX(m.getX());
	        setMouseY(m.getY()); 
			mousemoved = true;
	        if (radius > 10)
	            radius--;
	        
	        DrawInfo di = new DrawInfo(x,y,radius,Color.YELLOW);
	        vc.add(di);
			
		}
	}
	
    public void updateColor() {
        int x = getMouseX(); //get x if clicked
        int y = getMouseY(); //get y if clicked

    if (mousemoved)
        {
        if (radius < 150)
            radius++;
        else
            radius = 0;
        
        repaint();
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
	public void startthread() {
		// TODO Auto-generated method stub
		
	}


}
