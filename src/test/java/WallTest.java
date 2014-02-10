import java.text.NumberFormat;

import junit.framework.Assert;

import org.antinori.lumber.Wall;
import org.junit.Test;


public class WallTest {
	
	//@Test
	public void testWall() throws Exception {
		
		Wall w1 = new Wall(14, 12, false, false);
		Wall w2 = new Wall(14, 12, false, false);
		Wall w3 = new Wall(10, 12, true, false);
		Wall w4 = new Wall(10, 12, true, false);

		
		System.out.println(w1.toString());
		System.out.println(w2.toString());
		System.out.println(w3.toString());
		System.out.println(w4.toString());
		
		float c = w1.getCost()+w2.getCost()+w3.getCost()+w4.getCost();
		
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		String moneyString = formatter.format(c);
		System.out.println("total cost = " + moneyString);

		
	}
	
	@Test
	public void testVerticals() throws Exception {
		
		Wall w1 = new Wall(14, 12, false, false);
		System.out.println(w1.toString());
		Assert.assertEquals(w1.getVerticalPieces().size(), 12);
		
		Wall w2 = new Wall(14, 12, true, false);
		System.out.println(w2.toString());
		Assert.assertEquals(w2.getVerticalPieces().size(),11);
		
		Wall w3 = new Wall(8, 12, false, false);
		System.out.println(w3.toString());
		Assert.assertEquals(w3.getVerticalPieces().size(),7);
		
		Wall w4 = new Wall(8, 12, true, false);
		System.out.println(w4.toString());
		Assert.assertEquals(w4.getVerticalPieces().size() , 7);
		
		Wall w5 = new Wall(23, 12, false, false);
		System.out.println(w5.toString());
		Assert.assertEquals(w5.getVerticalPieces().size() , 19);
		
		Wall w6 = new Wall(10, 12, false, false);
		System.out.println(w6.toString());
		Assert.assertEquals(w6.getVerticalPieces().size(),9);
	}

}
