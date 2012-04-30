import java.awt.*;
import javax.swing.*;
import java.io.Serializable;

import java.applet.AudioClip;

@SuppressWarnings("serial")
public class MenuSystem extends JApplet{
	
	DrawSurface drawSurface;
	Image[] images = new Image[50];
	
	AudioClip startingMusic;
	
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
		images[10] = getToolkit().getImage("images/bg1.jpg");
		images[11] = getToolkit().getImage("images/bug.png");
		images[12] = getToolkit().getImage("images/toilets_n.png");
		images[13] = getToolkit().getImage("images/toilets_b.png");
		images[14] = getToolkit().getImage("images/hand_preference_n.png");
		images[15] = getToolkit().getImage("images/hand_preference_b.png");
		images[16] = getToolkit().getImage("images/difficulty_n.png");
		images[17] = getToolkit().getImage("images/difficulty_b.png");
		
		images[18] = getToolkit().getImage("images/easy_n.png");
		images[19] = getToolkit().getImage("images/easy_b.png");
		images[20] = getToolkit().getImage("images/medium_n.png");
		images[21] = getToolkit().getImage("images/medium_b.png");
		images[22] = getToolkit().getImage("images/hard_n.png");
		images[23] = getToolkit().getImage("images/hard_b.png");
		
		images[24] = getToolkit().getImage("images/bg1.jpg");
		images[25] = getToolkit().getImage("images/bg2.png");
		images[26] = getToolkit().getImage("images/bg3.jpg");
		images[27] = getToolkit().getImage("images/bg4.jpg");
		images[28] = getToolkit().getImage("images/bg5.jpg");
		
		images[29] = getToolkit().getImage("images/double_arrow_button_l.png");
		images[30] = getToolkit().getImage("images/double_arrow_button_r.png");
		images[31] = getToolkit().getImage("images/double_arrow_button_l_b.png");
		images[32] = getToolkit().getImage("images/double_arrow_button_r_b.png");
		
		images[33] = getToolkit().getImage("images/bg1_s.jpg");
		images[34] = getToolkit().getImage("images/bg2_s.png");
		images[35] = getToolkit().getImage("images/bg3_s.jpg");
		images[36] = getToolkit().getImage("images/bg4_s.jpg");
		images[37] = getToolkit().getImage("images/bg5_s.jpg");
		
		images[38] = getToolkit().getImage("images/bg1.png");
		images[39] = getToolkit().getImage("images/fg2.png");
		
		images[40] = getToolkit().getImage("images/youwin.png");
		images[41] = getToolkit().getImage("images/youlose.png");
		
		
		//startingMusic = getAudioClip(getDocumentBase(), "themesong.au");
		
		drawSurface = new DrawSurface(images,startingMusic);
		content_pane.add(drawSurface);
		setSize(380,600);
	}
	

}

 @SuppressWarnings("serial")
class DrawInfo implements Serializable {

	private int x,y;
	
	public DrawInfo(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}


	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}


}	

