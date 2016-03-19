package com.richard.game.rpssl;

public interface GamePlayer extends MatchObserver{
	public Choice choose();
	
	public String getDescription();
	
	public default void participate(Match match, boolean isPlayerOne) {
		
	}
	
	public default void onReceive(String message) {
		
	}
}
