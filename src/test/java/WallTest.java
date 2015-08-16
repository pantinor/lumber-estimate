import java.text.NumberFormat;

import org.antinori.lumber.Wall;
import org.testng.Assert;
import org.testng.annotations.Test;

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
	

	@Test
	public void isPalindrome() {
		String wordOrPhrase = "pauly";
	    char[] reversed = wordOrPhrase.toCharArray();

	    char tmp;
	    for(int i=reversed.length/2; i >= 0; i--) {
	        tmp = reversed[i];
	        reversed[i] = reversed[reversed.length-1-i];
	        reversed[reversed.length-1-i] = tmp;
	    }
	    
	    String res = new String(reversed);
		System.out.println(res);

	}
	
	@Test
	public void testReverseWords() throws Exception {
		char[] sentence = "My name is Paul".toCharArray();
		
		reverseWords(sentence);
		
	}
	
	
	private void reverseWords(char[] sentence) {
		if (sentence == null)
			return;
		
		// reverse the chars first pass
		int pos1 = 0;
		int pos2 = sentence.length - 1;
		reverseArray(sentence, pos1, pos2);
		
		System.out.println(sentence);


		// second pass - get each word and reverse chars
		int startOfWordPos = 0;
		int endOfWordPos = getPositionOfFirstSpace(sentence, 0) - 1;
		
		while (startOfWordPos < sentence.length) {
			reverseArray(sentence, startOfWordPos, endOfWordPos);
			System.out.println(sentence);

			startOfWordPos = endOfWordPos + 2;
			endOfWordPos = getPositionOfFirstSpace(sentence, startOfWordPos) - 1;
		}
		
		System.out.println(sentence);


	}
	
	private void reverseArray(char[] c, int pos1, int pos2) {
		while (pos1 < pos2) {
			swapChars(c, pos1, pos2);
			pos1++;
			pos2--;
		}
	}

	private void swapChars(char[] c, int pos1, int pos2) {
		char c1 = c[pos1];
		char c2 = c[pos2];
		c[pos1] = c2;
		c[pos2] = c1;
	}

	private int getPositionOfFirstSpace(char[] c, int start) {
		for (int x = start; x < c.length; x++) {
			if (c[x] == ' ') {
				return x;
			}
		}
		return c.length;
	}


}
