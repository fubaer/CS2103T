package textbuddy;

import static org.junit.Assert.*;

import org.junit.Test;

public class textbuddytest {

	@Test
	public void test() {
		
		//preparing 
		TextBuddy.clearAll();
		
		// test adding
		int initalSize = TextBuddy.getBufferSize();
		TextBuddy.addLine("hello");
		assertEquals(initalSize+1, TextBuddy.getBufferSize());
		
		//testing clearall
		TextBuddy.clearAll();
		assertEquals(0,TextBuddy.getBufferSize());
		
		// test deleting
		TextBuddy.addLine("What");
		TextBuddy.addLine("does");
		TextBuddy.addLine("the");
		TextBuddy.addLine("fox");
		TextBuddy.addLine("says");
		initalSize = TextBuddy.getBufferSize();
		TextBuddy.deleteLine("delete 2");
		assertEquals(initalSize-1, TextBuddy.getBufferSize());
		
	}

}
