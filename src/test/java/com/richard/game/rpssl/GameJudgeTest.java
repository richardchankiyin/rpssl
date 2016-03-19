package com.richard.game.rpssl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameJudgeTest {
	private static final Logger logger = LoggerFactory.getLogger(GameJudgeTest.class);
	
	@Test
	public void testSingletonBySingleThreadCreatingTwice() {
		GameJudge judge1 = GameJudge.getGameJudge();
		GameJudge judge2 = GameJudge.getGameJudge();
		logger.debug("judge1: [{}] judge2:[{}]", judge1, judge2);
		assertEquals(judge1,judge2);
	}

	@Test
	public void testSingletonByMultiThreadCreatingTwice() {
		ExecutorService executor = Executors.newFixedThreadPool(2);
		Future<GameJudge> judgeFuture1 = executor.submit(()->{return GameJudge.getGameJudge();});
		Future<GameJudge> judgeFuture2 = executor.submit(()->{return GameJudge.getGameJudge();});
		
		try {
			GameJudge judge1 = judgeFuture1.get(10, TimeUnit.SECONDS);
			GameJudge judge2 = judgeFuture2.get(10, TimeUnit.SECONDS);
			assertEquals(judge1,judge2);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Test
	public void testPlayWithROCKAsHost() {
		GameJudge judge = GameJudge.getGameJudge();
		
		assertEquals(Choice.PAPER,judge.getWinner(Choice.ROCK, Choice.PAPER));
		assertEquals(Choice.ROCK,judge.getWinner(Choice.ROCK, Choice.SCISSORS));
		assertEquals(Choice.SPOCK,judge.getWinner(Choice.ROCK, Choice.SPOCK));
		assertEquals(Choice.ROCK,judge.getWinner(Choice.ROCK,Choice.LIZARD));
		
		// draw game, winner is null
		assertNull(judge.getWinner(Choice.ROCK, Choice.ROCK));
	}
	
	@Test
	public void testPlayWithPAPERAsHost() {
		GameJudge judge = GameJudge.getGameJudge();
		
		assertEquals(Choice.PAPER,judge.getWinner(Choice.PAPER, Choice.ROCK));
		assertEquals(Choice.LIZARD,judge.getWinner(Choice.PAPER, Choice.LIZARD));
		assertEquals(Choice.PAPER,judge.getWinner(Choice.PAPER, Choice.SPOCK));
		assertEquals(Choice.SCISSORS,judge.getWinner(Choice.PAPER, Choice.SCISSORS));
		
		//draw game, winner is null
		assertNull(judge.getWinner(Choice.PAPER, Choice.PAPER));
	}
	
	@Test
	public void testPlayWithSCISSORSAsHost() {
		GameJudge judge = GameJudge.getGameJudge();
		
		assertEquals(Choice.SCISSORS,judge.getWinner(Choice.SCISSORS, Choice.PAPER));
		assertEquals(Choice.ROCK,judge.getWinner(Choice.SCISSORS, Choice.ROCK));
		assertEquals(Choice.SCISSORS,judge.getWinner(Choice.SCISSORS, Choice.LIZARD));
		assertEquals(Choice.SPOCK,judge.getWinner(Choice.SCISSORS,Choice.SPOCK));
		
		//draw game, winner is null
		assertNull(judge.getWinner(Choice.SCISSORS, Choice.SCISSORS));
	}
	
	@Test
	public void testPlayWithSPOCKAsHost() {
		GameJudge judge = GameJudge.getGameJudge();
		
		assertEquals(Choice.SPOCK,judge.getWinner(Choice.SPOCK, Choice.SCISSORS));
		assertEquals(Choice.PAPER,judge.getWinner(Choice.SPOCK, Choice.PAPER));
		assertEquals(Choice.SPOCK,judge.getWinner(Choice.SPOCK, Choice.ROCK));
		assertEquals(Choice.LIZARD,judge.getWinner(Choice.SPOCK, Choice.LIZARD));
		
		//draw game, winner is null
		assertNull(judge.getWinner(Choice.SPOCK, Choice.SPOCK));
		
	}
	
	@Test
	public void testPlayWithLIZARDAsHost() {
		GameJudge judge = GameJudge.getGameJudge();
		
		assertEquals(Choice.LIZARD,judge.getWinner(Choice.LIZARD, Choice.SPOCK));
		assertEquals(Choice.SCISSORS,judge.getWinner(Choice.LIZARD, Choice.SCISSORS));
		assertEquals(Choice.LIZARD,judge.getWinner(Choice.LIZARD, Choice.PAPER));
		assertEquals(Choice.ROCK,judge.getWinner(Choice.LIZARD,Choice.ROCK));
		
		//draw game, winner is null
		assertNull(judge.getWinner(Choice.LIZARD, Choice.LIZARD));
	}
}
