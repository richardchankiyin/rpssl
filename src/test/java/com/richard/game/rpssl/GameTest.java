package com.richard.game.rpssl;

import static org.junit.Assert.*;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameTest {
	private static final Logger logger = LoggerFactory.getLogger(GameTest.class); 
	@Test(expected=IllegalArgumentException.class)
	public void testConstructorArgMissingBoth() {
		new Game(null,null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testConstructorArgMissingGuest() {
		new Game(Choice.PAPER,null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testConstructorArgMissingHost() {
		new Game(null,Choice.PAPER);
	}
	
	@Test
	public void testConstructorOK() {
		new Game(Choice.PAPER, Choice.ROCK);
		
	}
	
	@Test
	public void testGettingHostAndGuest() {
		Game game = new Game(Choice.PAPER, Choice.ROCK);
		assertEquals(Choice.PAPER,game.getHost());
		assertEquals(Choice.ROCK,game.getGuest());
		logger.debug("{}", game);
	}
	
	@Test(expected=IllegalStateException.class)
	public void testPlayTwiceFailed() {
		Game game = new Game(Choice.PAPER, Choice.ROCK);
		game.play();
		game.play();
	}
	
	@Test
	public void testPlayContentionFailed() {
		Game game = new Game(Choice.PAPER, Choice.ROCK);
		
		Thread t1 = new Thread(()->
		{ 
			try {
				game.play();
			} catch(Exception e) {
				logger.debug("got exception: {}",e);
				assertEquals(IllegalStateException.class,e.getClass());
			}
		});
		Thread t2 = new Thread(()->
			{ 
				try {
					game.play();
				} catch(Exception e) {
					logger.debug("got exception: {}",e);
					assertEquals(IllegalStateException.class,e.getClass());
				}
			});
		
		t1.start();
		t2.start();
	}
}
