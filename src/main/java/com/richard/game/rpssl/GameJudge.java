package com.richard.game.rpssl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameJudge implements Judge {
	private static final Logger logger = LoggerFactory.getLogger(GameJudge.class);
	private volatile static GameJudge judge = null;
	private Map<Integer,Choice> mapOfCombinations = new HashMap<Integer,Choice>();
	
	public static GameJudge getGameJudge() {
		if (judge == null) {
			logger.debug("no singleton found.....");
			synchronized(GameJudge.class) {
				if (judge == null) {
					logger.debug("going to create singleton....");
					judge = new GameJudge();
					logger.debug("singleton created....");
					
					return judge;
				} else {
					logger.debug("singleton created by another thread....");
					return judge;
				}	
					
			}
		} else {
			logger.debug("singleton created already....");
			return judge;
		}
	}
	
	private static int hashingHostAndGuest(Choice host,Choice guest) {
		return host.getIndex() * 10 + guest.getIndex();
	}
	
	private GameJudge() {
		mapOfCombinations.put(hashingHostAndGuest(Choice.ROCK,Choice.PAPER), Choice.PAPER);
		mapOfCombinations.put(hashingHostAndGuest(Choice.ROCK,Choice.SCISSORS), Choice.ROCK);
		mapOfCombinations.put(hashingHostAndGuest(Choice.ROCK,Choice.SPOCK), Choice.SPOCK);
		mapOfCombinations.put(hashingHostAndGuest(Choice.ROCK,Choice.LIZARD), Choice.ROCK);
		
		mapOfCombinations.put(hashingHostAndGuest(Choice.PAPER,Choice.ROCK), Choice.PAPER);
		mapOfCombinations.put(hashingHostAndGuest(Choice.PAPER,Choice.LIZARD), Choice.LIZARD);
		mapOfCombinations.put(hashingHostAndGuest(Choice.PAPER,Choice.SPOCK), Choice.PAPER);
		mapOfCombinations.put(hashingHostAndGuest(Choice.PAPER,Choice.SCISSORS), Choice.SCISSORS);
		
		mapOfCombinations.put(hashingHostAndGuest(Choice.SCISSORS,Choice.PAPER), Choice.SCISSORS);
		mapOfCombinations.put(hashingHostAndGuest(Choice.SCISSORS,Choice.ROCK), Choice.ROCK);
		mapOfCombinations.put(hashingHostAndGuest(Choice.SCISSORS,Choice.LIZARD), Choice.SCISSORS);
		mapOfCombinations.put(hashingHostAndGuest(Choice.SCISSORS,Choice.SPOCK), Choice.SPOCK);
				
		mapOfCombinations.put(hashingHostAndGuest(Choice.SPOCK,Choice.SCISSORS), Choice.SPOCK);
		mapOfCombinations.put(hashingHostAndGuest(Choice.SPOCK,Choice.PAPER), Choice.PAPER);
		mapOfCombinations.put(hashingHostAndGuest(Choice.SPOCK,Choice.ROCK), Choice.SPOCK);
		mapOfCombinations.put(hashingHostAndGuest(Choice.SPOCK,Choice.LIZARD), Choice.LIZARD);
		
		mapOfCombinations.put(hashingHostAndGuest(Choice.LIZARD,Choice.SPOCK), Choice.LIZARD);
		mapOfCombinations.put(hashingHostAndGuest(Choice.LIZARD,Choice.SCISSORS), Choice.SCISSORS);
		mapOfCombinations.put(hashingHostAndGuest(Choice.LIZARD,Choice.PAPER), Choice.LIZARD);
		mapOfCombinations.put(hashingHostAndGuest(Choice.LIZARD,Choice.ROCK), Choice.ROCK);
	
		logger.debug("mapOfCombinations: {}", mapOfCombinations);
	}

	public Choice getWinner(Choice host, Choice guest) {
		return mapOfCombinations.get(hashingHostAndGuest(host,guest));
	}

}
