package com.richard.game.rpssl;

public class MatchResult {
	private String player1Desc;
	private String player2Desc;
	private Choice player1Choice;
	private Choice player2Choice;
	private String winnerDesc;
	
	public MatchResult(String player1Desc, String player2Desc, Choice player1Choice, Choice player2Choice, String winnerDesc) {
		this.player1Desc = player1Desc;
		this.player2Desc = player2Desc;
		this.player1Choice = player1Choice;
		this.player2Choice = player2Choice;
		this.winnerDesc = winnerDesc;
	}
	
	
	
	public String toString() {
		return new StringBuilder().append(String.format("Player1:[%s delivers %s] Player2:[%s delivers %s] Winner:%s", player1Desc, player1Choice, player2Desc, player2Choice, winnerDesc)).toString();
	}

	public String getPlayer1Desc() {
		return player1Desc;
	}

	public String getPlayer2Desc() {
		return player2Desc;
	}

	public Choice getPlayer1Choice() {
		return player1Choice;
	}

	public Choice getPlayer2Choice() {
		return player2Choice;
	}

	public String getWinnerDesc() {
		return winnerDesc;
	}
	
	
}
