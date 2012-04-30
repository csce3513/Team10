import static org.junit.Assert.*;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class OjumTest extends TestCase {

	MenuSystem test;
	
	
	@Before
	public void setUp() throws Exception {
		test = new MenuSystem();
		test.init();
	}

	@After
	public void tearDown() throws Exception {
		 test = null;
	}
	
	public void testInit(){
		
		assertNotNull(test);
		
		for(int i = 0; i <= 41; i++){
			assertNotNull(test.images[i]);
		}
	}
	
	public void testPissometer(){
		
		double expected =  test.drawSurface.getM_INDEX();
		assertEquals(0.0, expected);
		
		test.drawSurface.setM_INDEX(10.1);
		expected =  test.drawSurface.getM_INDEX();
		assertEquals(10.1, expected);
		
	}
	
	public void testStateChange(){
		
		DrawSurface.STATE testState = test.drawSurface.getSTATE();
		
		assertEquals("Game STATE is in Splash State", DrawSurface.STATE.SPLASH, testState);
	
		test.drawSurface.setSTATE(DrawSurface.STATE.GAME);
		
		testState = test.drawSurface.getSTATE();
		assertEquals("Game STATE is in Game State", DrawSurface.STATE.GAME, testState);
		
		
	}
	
	public void testBugHit(){
		
		int testBugX = test.drawSurface.bugX;
		int testBugY = test.drawSurface.bugY;
		int testHP = test.drawSurface.getHp();
		boolean hpHit = false;
		
		assertEquals("Hp is 50", 50, testHP);
		assertEquals("Bug is in middle of screen", 190, testBugX);
		assertEquals("Bug is in middle of screen", 300, testBugY);
		
		test.drawSurface.setMouseX(190);
		test.drawSurface.setMouseY(300);
		
		testHP = test.drawSurface.getHp();
		
		if (testHP < 50)
			hpHit = true;
		else
			hpHit = false;
		
		
		assertTrue(hpHit);
		
		
		
		
		
		
		
		
		
	}
	
	
	

}