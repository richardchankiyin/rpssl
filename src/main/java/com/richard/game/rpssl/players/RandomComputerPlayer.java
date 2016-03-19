package com.richard.game.rpssl.players;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.richard.game.rpssl.Choice;
import com.richard.game.rpssl.GamePlayer;


public class RandomComputerPlayer implements GamePlayer {
	private static final Logger logger = LoggerFactory.getLogger(RandomComputerPlayer.class);
	//Random random = new Random(System.currentTimeMillis());
	Random random = new Random();
	Choice[] values = Choice.values();
	@Override
	public Choice choose() {
		int noPicked = Math.abs(random.nextInt() % 5);
		Choice picked = values[noPicked];
		logger.debug("noPicked: {} choice:{}", noPicked, picked);
		
		return picked;
	}

	@Override
	public String getDescription() {
		return "Random Computer Player";
	}

}
