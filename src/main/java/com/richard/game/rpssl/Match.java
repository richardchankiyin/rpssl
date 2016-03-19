package com.richard.game.rpssl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Match {
	private static final Logger logger = LoggerFactory.getLogger(Match.class);
	
	private final GamePlayer player1;
	private final GamePlayer player2;
	private final ReentrantLock lock = new ReentrantLock();
	private boolean isMatchEnd;
	private int noOfPlayer1Win = 0;
	private int noOfPlayer2Win = 0;
	private int noOfDraw = 0;
	private int noOfRoundPlayed = 0;
	private int noOfSecondWaitingForPlayer = 10;
	private int noOfSecondWaitingForContinue = 1;
	private final String DRAW_GAME_DESC = "Draw";
	private Map<Integer,MatchResult> matchResults = new HashMap<Integer,MatchResult>();
	private MatchControl controller;
	private List<MatchObserver> matchObservers = new ArrayList<MatchObserver>();
	
	public Match(GamePlayer player1, GamePlayer player2) {
		if (player1 == null || player2 == null)
			throw new IllegalArgumentException("player1 and player2 must exist!");
		
		//set player
		this.player1 = player1;
		this.player2 = player2;
		
		//inform players player positions
		this.player1.participate(this,true);
		this.player2.participate(this,false);
		
		// add players as observers
		this.addObserver(player1);
		this.addObserver(player2);
		
		logger.debug("Player1:[{}] Player2:[{}]", player1, player2);
	}
	
	public void addObserver(MatchObserver observer) {
		matchObservers.add(observer);
	}
	
	public void notifyObservers(String message) {
		
		for (MatchObserver observer: matchObservers) {
			observer.onReceive(message);
		}
	}
	
	public void setController(MatchControl controller) {
		this.controller = controller;
	}
	
	public void setNoOfSecondWaitingForPlayer(int noOfSecondWaitingForPlayer) {
		this.noOfSecondWaitingForPlayer = noOfSecondWaitingForPlayer;
	}
	
	public void setNoOfSecondWaitingForContinue(int noOfSecondWaitingForContinue) {
		this.noOfSecondWaitingForContinue = noOfSecondWaitingForContinue;
	}
	
	public int getNoOfPlayer1Win() {return noOfPlayer1Win;}
	public int getNoOfPlayer2Win() {return noOfPlayer2Win;}
	public int getNoOfDraw() {return noOfDraw;}
	public int getNoOfRoundPlayed() {return noOfRoundPlayed;}
	public Map<Integer,MatchResult> getMatchResults() {return matchResults;}
	
	public void startMatch() {
		boolean lockGot = lock.tryLock();
		
		if (lockGot == false) {
			throw new IllegalStateException("You failed to obtain lock to start the match!");
		}
		
		if (isMatchEnd == true) {
			throw new IllegalStateException("This match is end");
		}
		
		boolean isContinue = true;
		ExecutorService executor = Executors.newFixedThreadPool(2);
		GameJudge judge = GameJudge.getGameJudge();
		
		while (isContinue) {
			isContinue = false;
			
			this.notifyObservers(String.format("Round %s starts", noOfRoundPlayed + 1));
			
			Future<Choice> player1ChoiceFuture = executor.submit(()->{return player1.choose();});
			Future<Choice> player2ChoiceFuture = executor.submit(()->{return player2.choose();});
			Choice player1Choice = null;
			Choice player2Choice = null;
			String winnerDesc = null;
			
			try {
				player1Choice = player1ChoiceFuture.get(noOfSecondWaitingForPlayer, TimeUnit.SECONDS);
				player2Choice = player2ChoiceFuture.get(noOfSecondWaitingForPlayer, TimeUnit.SECONDS);
				
				Choice winnerChoice = judge.getWinner(player1Choice, player2Choice);
				
				if (winnerChoice == null) {
					++noOfDraw;
					winnerDesc = DRAW_GAME_DESC;
					logger.debug("[Player1: {}] vs [Player2: {}] -- draw game", player1Choice, player2Choice);
				} else {
					if (player1Choice.equals(winnerChoice)) {
						++noOfPlayer1Win;
						winnerDesc = player1.getDescription();
						logger.debug("[Player1: {}] vs [Player2: {}] -- player1 win", player1Choice, player2Choice);
					}	
					else {
						++noOfPlayer2Win;
						winnerDesc = player2.getDescription();
						logger.debug("[Player1: {}] vs [Player2: {}] -- player2 win", player1Choice, player2Choice);
					}
				}
				MatchResult currentMatchResult = new MatchResult(player1.getDescription(),player2.getDescription(),player1Choice,player2Choice,winnerDesc);
				logger.debug("Match {} Result: {}", (noOfRoundPlayed + 1), currentMatchResult);
				this.notifyObservers(String.format("Match %s Result: %s", noOfRoundPlayed + 1, currentMatchResult));
								
				matchResults.put(noOfRoundPlayed + 1, currentMatchResult);
				
				++noOfRoundPlayed;
			}
			catch (Exception e) {
				logger.debug("issue found: {}", e);
			}
			
			logger.debug("check whether to continue or not");
			Future<Boolean> isContinueFuture = executor.submit(()->{return controller != null ? controller.isContinue() : false ;});
			try {
				isContinue = isContinueFuture.get(noOfSecondWaitingForContinue, TimeUnit.SECONDS);
				logger.debug("isContinue: {}", isContinue);
			}
			catch (Exception e) {
				logger.debug("problem found when waiting for continue or not: {}", e);
				isContinue = false;
			}
			
			if (isContinue) {
				this.notifyObservers("Continue");
			}
		} //end while
		
		// setting match end
		isMatchEnd = true;
		
		this.notifyObservers("End game");
		//shutting down thread pool
		executor.shutdownNow();
	}
}
