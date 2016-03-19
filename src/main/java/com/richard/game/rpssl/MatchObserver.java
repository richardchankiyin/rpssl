package com.richard.game.rpssl;

@FunctionalInterface
public interface MatchObserver {
	public void onReceive(String message);

}
