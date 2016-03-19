package com.richard.game.rpssl;

public enum Choice {
	ROCK(1), PAPER(2), SCISSORS(3), SPOCK(4), LIZARD(5);
	private int index;
	private Choice(int _index) {
		index = _index;
	}
	public int getIndex() {return index;}
	
}
