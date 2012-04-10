
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.util.Random;
import java.util.Vector;

import javax.swing.JPanel;

 @SuppressWarnings("serial") 
 class DrawSurface extends JPanel implements MouseListener, MouseMotionListener, KeyListener 
{
	 
	 public enum STATE {
		 	SPLASH(1), START_OPTION(2), LEFT_RIGHT(3), GAME(4), OPTION_MENU(5), CHOOSE_LEVEL(6), CHOOSE_HAND(7), CHOOSE_DIF(8);
		 	private int VALUE;
		 	
		 	private STATE (int value) {
		 		this.VALUE = value;
		 		
		 	}
	 };
	
	 private STATE currentState = STATE.SPLASH;
	 int state = 0;
	int start_button_status = 2;
	int option_button_status = 4;
	int lefthand_button_status = 6;
	int righthand_button_status = 7;
	
	int toilets_button_status = 13;
	int hand_preference_button_status = 15;
	int difficulty_button_status = 17;
	
	int easy_button_status = 19;
	int medium_button_status = 21;
	int hard_button_status = 23;
	
	int bg_image_status = 25;
	
	int arrow_button_left_status = 30;
	int arrow_button_right_status = 31;
	
	int bg_image_sel_status = 34;
	
	int imgX, imgY;
	int bugX = 190, bugY = 300;
	int x,y;
	boolean button_activator = false;
	Thread startthread = new StartThread();
	boolean hand = false;	// right hand = false, left hand = true;
	Image[] images;
	Image testIMG;
	AudioClip startingMusic;
	private Vector vc = new Vector();
	
	// <============================================== > //
	
	private int speed = 30; //speed of thread
	private int bugspeed = 5;
    private int mousex;
    private int mousey;
    private int radius;
    private int hp = 100;
    Graphics G;
    
    private Shape shape1, shape2;
    private Area areaTwo, test;
     
    static final int X = 380, Y = 250;
    static BufferedImage I;

        
    //private Dimension size; //used to set the size of applet
    Random randomGenerator = new Random(); //generator for color, position, & speed
	Thread th = new GameThread(); // thread
	Thread bugthread = new BugThread();
	
	// <=============== Ripple Water ================> //
    int width,height,hwidth,hheight;
    MemoryImageSource source;
    Image image, offImage;
    Graphics offGraphics;
    int i,a,b;
    int MouseX,MouseY;
    int fps,delay,pic_size;

    short ripplemap[];
    int texture[];
    int ripple[];
    int oldind,newind,mapind;
    int riprad;
    Thread ripwater = new RippleWater();
    
    public void ripplewaterinit(Image bg_image)
    {
    	fps = 50;
        delay = (fps > 0) ? (1000 / fps) : 100;

        width = 380;
        height = 600;
        hwidth = width>>1;
        hheight = height>>1;
        riprad=3;

        pic_size = width * (height+2) * 2;
        ripplemap = new short[pic_size];
        ripple = new int[width*height];
        texture = new int[width*height];
        oldind = width;
        newind = width * (height+3);
        
        System.out.println(bg_image);
        PixelGrabber pg = new PixelGrabber(bg_image,0,0,width,height,texture,0,width);
        try {
          pg.grabPixels();
          } catch (InterruptedException e) {}

        source = new MemoryImageSource(width, height, ripple, 0, width);
        source.setAnimated(true);
        source.setFullBufferUpdates(true);

        image = createImage(source);
        
        offImage = createImage(width, height);
        offGraphics = offImage.getGraphics(); 
    }
    
    public void disturb(int dx, int dy) {
        for (int j=dy-riprad;j<dy+riprad;j++) {
          for (int k=dx-riprad;k<dx+riprad;k++) {
            if (j>=0 && j<height && k>=0 && k<width) {
  	    ripplemap[oldind+(j*width)+k] += 512;            
            } 
          }
        }
      }
    public void newframe() {
        //Toggle maps each frame
        i=oldind;
        oldind=newind;
        newind=i;

        i=0;
        mapind=oldind;
        for (int y=0;y<height;y++) {
          for (int x=0;x<width;x++) {
  	  short data = (short)((ripplemap[mapind-width]+ripplemap[mapind+width]+ripplemap[mapind-1]+ripplemap[mapind+1])>>1);
            data -= ripplemap[newind+i];
            data -= data >> 5;
            ripplemap[newind+i]=data;

  	  //where data=0 then still, where data>0 then wave
  	  data = (short)(1024-data);

            //offsets
    	  a=((x-hwidth)*data/1024)+hwidth;
            b=((y-hheight)*data/1024)+hheight;

   	  //bounds check
            if (a>=width) a=width-1;
            if (a<0) a=0;
            if (b>=height) b=height-1;
            if (b<0) b=0;

            ripple[i]=texture[a+(b*width)];
            mapind++;
            i++;
          }
        }
      }
    
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
		if(currentState == STATE.SPLASH)	// Draw splash image
		{
			g.drawImage(images[0],0,0,this);

		}
		else if(currentState == STATE.START_OPTION) // Start and option selection 
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
		else if(currentState == STATE.OPTION_MENU)	// Option Menu
		{
			g.drawImage(images[1],0,0,this);
			g.drawImage(images[toilets_button_status],85,120,this);
			g.drawImage(images[hand_preference_button_status],85,220,this);
			g.drawImage(images[difficulty_button_status],85,320,this);
		}
		
		else if(currentState == STATE.CHOOSE_LEVEL) // Level screen currentState = STATE.CHOOSE_LEVEL CHOOSE_LEVEL(31)
		{
			g.drawImage(images[1],0,0,this);
			g.drawRect(0, 0, 360, 600);
			g.drawImage(images[bg_image_sel_status],65,50,this);
			g.drawImage(images[arrow_button_left_status],90,473,this); 
			g.drawImage(images[arrow_button_right_status],195,470,this);			
		}
		else if(currentState == STATE.CHOOSE_HAND) // Choose lefthand or right hand currentState = STATE.CHOOSE_HAND CHOOSE_HAND(32)
		{
			g.drawImage(images[1],0,0,this);
			g.drawImage(images[lefthand_button_status],45,137,this);
			g.drawImage(images[righthand_button_status],212,137,this);
		}
		else if(currentState == STATE.CHOOSE_DIF) // EASY MEDIUM HARD currentState = STATE.CHOOSE_DIF CHOOSE_DIF(33)
		{
			g.drawImage(images[1],0,0,this);
			g.drawImage(images[easy_button_status],85,120,this);
			g.drawImage(images[medium_button_status],85,220,this);
			g.drawImage(images[hard_button_status],85,320,this);
		}

		
		else if(currentState == STATE.GAME)
		{
			 Graphics2D g2 = (Graphics2D) g;
	         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	         shape1 = new Ellipse2D.Double(90, 275, 200, 325);
	         shape2 = new Ellipse2D.Double(98, 290, 185, 300);
            
	         //areaOne = new Area(shape1);
	         test = new Area(shape1);
	         areaTwo = new Area(shape2);
	         
	        
	         
	         g.drawImage(offImage,0,0,this);
	         g.drawImage(images[39],0,0,this);

	         if (areaTwo.contains(mousex, mousey)) {
	         if(radius %2 == 1)
	         g2.setColor(Color.yellow);
	         //g.fillOval(getMouseX()-radius/2, getMouseY()-radius/2, radius, radius);
	         }
	         else {

	         }
	         
	         g.drawImage(images[11],bugX,bugY,this);
	         g.setColor(Color.red);
	         g.setFont(new Font("MV Boli", Font.BOLD+Font.ITALIC, 30));
	         g.drawString("HP:"+hp,250, 40);
	         
	         g2.setColor(Color.lightGray);
	         test.subtract(areaTwo);

	         //g2.draw(areaOne);
	         //g2.draw(areaTwo);
	         g2.setColor(Color.white);
	         g2.fill(test);
	         
		}
		
	}
	
	class RippleWater extends Thread
	{
		RippleWater()
		{
			super("RippleWater");			
		}
		public void run()
		{
		  while (true) 
		  {
		     newframe();
		     source.newPixels();
		     offGraphics.drawImage(image,0,0,width,height,null);
		     repaint();
		  }  
		}
	}
	
	class StartThread extends Thread{
		
		StartThread()
		{
			super("StartThread");
		}
		
		public void run()
		{
			System.out.println("playmusic");
			startingMusic.play();
			
			while(true)
			{
				if(currentState != STATE.START_OPTION){
				try { Thread.sleep(1900); } 
				catch (InterruptedException e) {}
				currentState = STATE.START_OPTION;
				repaint();
				}
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
				//updateColor();
			}
		}
	}
	
	class BugThread extends Thread{
		
		int ranX = 190;
		int ranY = 300;
		
		BugThread()
		{
			super("BugThread");
		}
		

		public void run()
		{
			while(true)
			{

				if(ranX == bugX && ranY == bugY) {
				
					ranX = 100 + randomGenerator.nextInt(175);
					ranY = 275 + randomGenerator.nextInt(250);
					
					
				}
				else
				{
					if(bugX < ranX)
						bugX++;
					else if(bugX == ranX)
						;
					else
						bugX--;
					
					if(bugY < ranY)
						bugY++;
					else if(bugY == ranY)
						;
					else 
						bugY--;
					
				}
				disturb(bugX, bugY);
				if(hp == 0)
				{
					currentState = STATE.START_OPTION;
					repaint();
					break;
				}
				try { Thread.sleep(bugspeed);  } 
				catch (InterruptedException e) {}
				repaint();
			}
		}

	}
	
	
	@Override
	public void mouseClicked(MouseEvent m) {
		
		x = m.getX();
		y = m.getY();

		if(currentState == STATE.START_OPTION && button_activator)
		{
			if(x >= 154 && x <= 220 && y >= 190 && y <= 263) {
					currentState = STATE.GAME;	// game start;
					repaint();
					ripplewaterinit(images[bg_image_status]);
					th.start();
					bugthread.start();
					ripwater.start();
					
					
				}
			
			else if(x >= 150 && x <= 230 && y >= 340 && y <= 355) {
					currentState = STATE.OPTION_MENU;
					repaint();
				}
		}
		
		else if(currentState == STATE.OPTION_MENU && button_activator)
		{
			
			if(x >= 126 && x <= 257 && y >= 127 && y <= 151) 
			{
				currentState = STATE.CHOOSE_LEVEL;
				repaint();
			}
			
			else if(x >= 89 && x <= 295 && y >= 228 && y <= 248) {
				
				currentState = STATE.CHOOSE_HAND;
				repaint();
			}
			
			else if(x >= 135 && x <= 257 && y >= 325 && y <= 348) {
				
				currentState = STATE.CHOOSE_DIF;
				repaint();
			}	
		}
		else if(currentState == STATE.CHOOSE_DIF && button_activator)
		{
			
			if(x >= 135 && x <= 240 && y >= 131 && y <= 158) {
				
				bugspeed = 5;
				currentState = STATE.START_OPTION;
				repaint();
			}
			
			else if(x >= 101 && x <= 291 && y >= 231 && y <= 258) {
				
				bugspeed = 4;
				currentState = STATE.START_OPTION;
				repaint();
			}
			
			else if(x >= 127 && x <= 240 && y >= 327 && y <= 357) {
				
				bugspeed = 2;
				currentState = STATE.START_OPTION;
				repaint();
			}
		}
		else if(currentState == STATE.CHOOSE_HAND && button_activator)
		{
			if(x >= 45 && x <= 167 && y >= 137 && y <= 283)	{ 
				hand = true;
			}	
			else if(x >= 225 && x <= 326 && y >= 140 && y <= 275)	{ 
				hand = false;
			}	
			currentState = STATE.START_OPTION;
			repaint();
		}
		
		else if(currentState == STATE.CHOOSE_LEVEL && button_activator)
		{
			if(x >= 115 && x <= 169 && y >= 486 && y <= 545) {
				
				arrow_button_left_status = 32;
				button_activator = true;
				
				bg_image_sel_status--;
				if(bg_image_sel_status  == 33)
				{
					bg_image_sel_status = 38;
				}
				bg_image_status = bg_image_sel_status - 9;
				
				repaint();
			}
			
			else if(x >= 219 && x <= 494 && y >= 273 && y <= 546) {
				
				arrow_button_right_status= 33;
				button_activator = true;
				bg_image_sel_status++;
				if(bg_image_sel_status  == 39)
				{
					bg_image_sel_status = 34;
				}
				bg_image_status = bg_image_sel_status - 9;
				repaint();
			}
			else if(x >= 70 && x <= 311 && y >= 51 && y <= 441) 
			{
				currentState = STATE.START_OPTION;
				repaint();
			}
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
		
	
		if(currentState == STATE.START_OPTION)
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
		else if(currentState == STATE.OPTION_MENU)
		{
			
			if(x >= 126 && x <= 257 && y >= 127 && y <= 151) {
				
				toilets_button_status = 14;
				button_activator = true;
				repaint();
			}
			
			else if(x >= 89 && x <= 295 && y >= 228 && y <= 248) {
				
				hand_preference_button_status = 16;
				button_activator = true;
				repaint();
			}
			
			else if(x >= 135 && x <= 257 && y >= 325 && y <= 348) {
				
				difficulty_button_status = 18;
				button_activator = true;
				repaint();
			}
			
			else
			{
				toilets_button_status = 13;
				hand_preference_button_status = 15;
				difficulty_button_status = 17;
				button_activator = false;
				repaint();
			}
		}
		else if(currentState == STATE.CHOOSE_DIF)		// difficulty button_activate changes
		{
				
				if(x >= 135 && x <= 240 && y >= 131 && y <= 158) {
					
					easy_button_status = 20;
					button_activator = true;
					repaint();
					
				}
				
				else if(x >= 101 && x <= 291 && y >= 231 && y <= 258) {
					
					medium_button_status = 22;
					button_activator = true;
					repaint();
				}
				
				else if(x >= 127 && x <= 240 && y >= 327 && y <= 357) {
					
					hard_button_status = 24;
					button_activator = true;
					repaint();
				}
				
				else
				{
					easy_button_status = 19;
					medium_button_status = 21;
					hard_button_status = 23;
					button_activator = false;
					repaint();
				}
		}
		else if(currentState == STATE.CHOOSE_HAND)
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
		
		else if(currentState == STATE.CHOOSE_LEVEL)
		{
			if(x >= 115 && x <= 169 && y >= 486 && y <= 545) {
		
				arrow_button_left_status = 32;
				button_activator = true;
				repaint();
			}
			
			else if(x >= 219 && x <= 280 && y >= 495 && y <= 546) {
				
				arrow_button_right_status= 33;
				button_activator = true;
				repaint();
			}
			else if(x >= 70 && x <= 311 && y >= 51 && y <= 441) {
				
				button_activator = true;
				repaint();
			}
			else
			{
				arrow_button_left_status = 30;
				arrow_button_right_status= 31;
				button_activator = false;
				repaint();
			}
			
		}
			
		
		else if(currentState == STATE.GAME)
		{
			disturb(m.getX(),m.getY());
	        setMouseX(m.getX());
	        setMouseY(m.getY()); 
	        if (radius > 10)
	            radius--;
	        
	        if(m.getX() >= bugX && m.getX() <= bugX+24 && m.getY() >= bugY && m.getY() <= bugY+24 )
	        {
	        	hp--;
	        	//System.out.println(hp);
	        }
	        DrawInfo di = new DrawInfo(x,y,radius,Color.YELLOW);
	        vc.add(di);
			
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