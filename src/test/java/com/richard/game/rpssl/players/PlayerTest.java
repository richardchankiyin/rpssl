package com.richard.game.rpssl.players;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.richard.game.rpssl.Choice;
import com.richard.game.rpssl.GamePlayer;
import com.richard.game.rpssl.Match;
import com.richard.game.rpssl.MatchControl;



public class PlayerTest {
	private static final Logger logger = LoggerFactory.getLogger(PlayerTest.class);
	
	@Test
	public void testChooseByRandomComputerPlayer() {
		int rounds = 1000 * Choice.values().length;
		List<Choice> results = new ArrayList<Choice>();
		for (int i=0; i<rounds; ++i) {
			RandomComputerPlayer player = new RandomComputerPlayer();
			Choice choice = player.choose();
			logger.debug("RandomComputerPlayer Choice: {}", choice);
			assertNotNull(choice);
			assertTrue(Arrays.asList(Choice.values()).contains(choice));
			results.add(choice);
		}
		
		Map<Choice,Long> stat = results.stream().collect(Collectors.groupingBy(Function.identity(),Collectors.counting()));
		logger.debug("{}",stat);
		assertEquals(Choice.values().length,stat.keySet().size());
	}

	@Test
	public void testMatchPlayedByTacticComputerPlayer() {
		Match match = new Match(
				new TacticComputerPlayer(), new GamePlayer() {
					public Choice choose() {
						return Choice.PAPER;
					}
					
					public String getDescription() {
						return "Paper Chooser";
					}
				}
		);
		match.setController(new MatchControl() {
        	private int count = 0;
        	public boolean isContinue() {
        		++count;
        		return count < 3;
        	}
        });
		match.startMatch();
		assertTrue(match.getNoOfPlayer1Win() >= 1);
	}
}
