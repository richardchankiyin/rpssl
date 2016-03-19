package com.richard.game.rpssl.players;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.richard.game.rpssl.Choice;
import com.richard.game.rpssl.GameJudge;
import com.richard.game.rpssl.GamePlayer;
import com.richard.game.rpssl.Match;
import com.richard.game.rpssl.MatchResult;

public class TacticComputerPlayer implements GamePlayer {
	private static final Logger logger = LoggerFactory.getLogger(TacticComputerPlayer.class);
	Match participatingMatch = null;
	boolean isPlayerOne = false;
	private RandomComputerPlayer randomPlayer = new RandomComputerPlayer();
	
	private final int WIN_RETURN = 1;
	private final int LOSS_RETURN = -1;
	private final int DRAW_RETURN = 0;
	@Override
	public Choice choose() {
		logger.debug("starting choose method....");
		Choice result = null;
		logger.debug("participating match: {}", participatingMatch);
		if (participatingMatch == null) {
			logger.debug("no participating match, simply choose from random");
			result = randomPlayer.choose();
		}
		else {
			Map<Integer,MatchResult> matchResults = participatingMatch.getMatchResults();
			return optimize(matchResults, !isPlayerOne);
		}
		return result;
	}

	private Choice optimize(Map<Integer,MatchResult> map, boolean isCounterPartyPlayerOne) {
		
		
		if (map == null || map.keySet().size() == 0) {
			logger.debug("no previous round information");
			return randomPlayer.choose();
		} else {
			Collection<MatchResult> matchResults = map.values();
			
			Map<Choice,Long> countResult = matchResults.parallelStream().collect(Collectors.groupingBy(
					(MatchResult m)-> isCounterPartyPlayerOne ? m.getPlayer1Choice() : m.getPlayer2Choice()
							, Collectors.counting()));
			
			logger.debug("countresult: {}", countResult);
			
			GameJudge judge = GameJudge.getGameJudge();
			
			Map<Choice,Double> expectedReturns = new HashMap<Choice,Double>();
			// calculate the expected return of each Choice
			for (Choice c: Choice.values()) {
				double expectedReturn = 0;
				int factor = 0;
				for (Choice statChoice: countResult.keySet()) {
					Choice winner = judge.getWinner(c, statChoice);
					if (winner == null)
						factor = DRAW_RETURN;
					else if (c.equals(winner)) {
						factor = WIN_RETURN;
					} else {
						factor = LOSS_RETURN;
					}
					
					expectedReturn +=  (double)factor * ((double)(countResult.get(statChoice).longValue()) / (double)matchResults.size());
				}
				
				expectedReturns.put(c, expectedReturn);
			} //end for (Choice c: Choice.values())
			
			logger.debug("expected returns: {}", expectedReturns);
			
			Choice result = null;
			double minExpectedReturn = Double.MIN_VALUE;
			for (Choice c: expectedReturns.keySet()) {
				if (expectedReturns.get(c) > minExpectedReturn)
					result = c;
			}
			
			return result;
		}
		
	}
	
	
	@Override
	public String getDescription() {
		return "Tactic Computer Player";
	}
	
	@Override
	public void participate(Match match, boolean ip1) {
		participatingMatch= match;
		isPlayerOne = ip1;
	}

}
