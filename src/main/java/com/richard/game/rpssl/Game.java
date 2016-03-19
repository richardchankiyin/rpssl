package com.richard.game.rpssl;

import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class Game {
	private static final Logger logger = LoggerFactory.getLogger(Game.class);
	
	private ReentrantLock lock = new ReentrantLock();
	private Choice host;
	private Choice guest;
	private Choice winner;
	private boolean isPlayed = false;
	private Date playTime = null;
	public Game(Choice _host, Choice _guest) {
		if (_host == null || _guest == null)
			throw new IllegalArgumentException("host and guest must not be null");
		host = _host;
		guest = _guest;
		logger.debug("host:{} and guest:{} created",host,guest);
	}
	
	public Choice getHost() {return host;}
	public Choice getGuest() {return guest;}
	
	public Choice play() {
		boolean canGetLock = lock.tryLock();
		if (canGetLock == false) {
			throw new IllegalStateException("You failed to acquire lock");
		} else {
			if (isPlayed == true) {
				lock.unlock();
				throw new IllegalStateException("The game is over");
			} else {
				lock.unlock();
				isPlayed = true;
				playTime = new Date();
				winner = GameJudge.getGameJudge().getWinner(host, guest);
				logger.info("{}", this);
				return winner;
			}
		}
	}
	
	
	public String toString() {
		StringBuilder strBuilder = new StringBuilder();
		return strBuilder
				.append(String.format
						("Game -- Host:[%s] Guest:[%s] isPlayed:[%s] PlayTime:[%s] Winner:[%s]"
								, host,guest,isPlayed,playTime,winner)).toString();
	}
	
}
