import static org.junit.Assert.*;

import org.junit.Test;


public class MenuSystemTest {

	MenuSystem test1 = new MenuSystem();
	
	@Test
	public void testInit() {
		

		test1.init();
		assertNotNull(test1);
		
		for(int i = 0; i <= 11; i++){
			assertNotNull(test1.images[i]);

		}
	}
}
