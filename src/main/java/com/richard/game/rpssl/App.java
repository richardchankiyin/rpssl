package com.richard.game.rpssl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.richard.game.rpssl.players.RandomComputerPlayer;
import com.richard.game.rpssl.players.TacticComputerPlayer;

/**
 * Hello world!
 *
 */
public class App 
{
	private static final Logger logger = LoggerFactory.getLogger(App.class);
    public static void main( String[] args )
    {
        Match match = new Match(
        	new GamePlayer() {
        		public Choice choose() {
        			return Choice.ROCK;
        		}
        		
        		public String getDescription() {
        			return "player a";
        		}
        	},
        	new GamePlayer() {
        		public Choice choose() {
        			return Choice.PAPER;
        		}
        		
        		public String getDescription() {
        			return "player b";
        		}
        	});
        
        match.startMatch();
        
        match = new Match(
            	new GamePlayer() {
            		public Choice choose() {
            			return Choice.PAPER;
            		}
            		
            		public String getDescription() {
            			return "player a";
            		}
            	},
            	new GamePlayer() {
            		public Choice choose() {
            			return Choice.PAPER;
            		}
            		
            		public String getDescription() {
            			return "player b";
            		}
            	});
            
            match.startMatch();
            
        match = new Match(new RandomComputerPlayer(), new RandomComputerPlayer());
        match.startMatch();
        
        logger.info("=====================================================");
        
        match = new Match(new RandomComputerPlayer(), new TacticComputerPlayer());
        match.setController(new MatchControl() {
        	private int count = 0;
        	public boolean isContinue() {
        		++count;
        		return count < 10000;
        	}
        });
        match.startMatch();
        logger.info("Random vs Tactics --[No of Draw: {} No of Random Win: {} No of Tactic Win:{}]", match.getNoOfDraw(), match.getNoOfPlayer1Win(), match.getNoOfPlayer2Win());
    }
}
