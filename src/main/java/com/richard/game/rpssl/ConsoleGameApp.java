package com.richard.game.rpssl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.richard.game.rpssl.players.RandomComputerPlayer;
import com.richard.game.rpssl.players.TacticComputerPlayer;

public class ConsoleGameApp {

	private static final Logger logger = LoggerFactory.getLogger(ConsoleGameApp.class);
	public static void main(String[] args) {
		logger.debug("program started");
		String name = greet();
		int choice = chooseCounterParty(name);
		GamePlayer player = createPlayer(choice);
		runMatch(name,player);
		sayGoodBye();
	}
	
	private static String greet() {
		System.out.println("========Welcome to Rock Paper Scissors Spock Lizard==========");
		String name = askName();
		System.out.printf("Hi %s! Nice to meet you!\n", name);
		return name;
	}
	
	private static String askName() {
		System.out.println("What is your name?");
		return System.console().readLine();
	}
	
	private static void sayGoodBye() {
		System.out.println("Good Bye!");
	}

	private static void showMatchResult(Match match,String name,GamePlayer player) {
		System.out.printf("No of Rounds: %s \n", match.getNoOfRoundPlayed());
		System.out.printf("%s win: %s\n", name, match.getNoOfPlayer1Win());
		System.out.printf("%s win: %s\n", player.getDescription(), match.getNoOfPlayer2Win());
		System.out.printf("No of Draw: %s\n", match.getNoOfDraw());
	}
	
	private static int chooseCounterParty(String name) {
		boolean correctChoice = false;
		int result = -1;
		do {
			System.out.println("Please select player:");
			System.out.println("1) Random Player");
			System.out.println("2) Tactic Player");
			try {
				String choice =System.console().readLine();
				int choiceInInt = Integer.parseInt(choice);
				logger.debug("choiceInInt: {}", choiceInInt);
				if (choiceInInt == 1 || choiceInInt == 2) {
					result = choiceInInt;
					correctChoice = true;
				}
			}
			catch (Exception e) {
				System.out.println("I am sorry I don't quite get what you input! Please try again!");
				logger.debug("exception found: {}", e);
			}
		} while(!correctChoice);
		
		return result;
	}
	
	private static GamePlayer createPlayer(int choice) {
		return (choice == 1) ? new RandomComputerPlayer() : new TacticComputerPlayer();
	}
	
	private static void runMatch(String name, GamePlayer player) {
		System.out.printf("%s, Let's start now!\n", name);
		GamePlayer humanPlayer = new GamePlayer() {
			public Choice choose() {
				boolean correctChoice = false;
				Choice result = null;
				do {
					System.out.println("Please choose the following....");
					System.out.println("1) Rock");
					System.out.println("2) Paper");
					System.out.println("3) Scissors");
					System.out.println("4) Spock");
					System.out.println("5) Lizard");
					
					try {
						String choice =System.console().readLine();
						int choiceInInt = Integer.parseInt(choice);
						
						
						
						if (choiceInInt == 1 || choiceInInt == 2 || choiceInInt == 3 || choiceInInt == 4 || choiceInInt == 5) {
							result = Choice.values()[choiceInInt - 1];
							correctChoice = true;
						}
					
					}
					catch (Exception e) {
						System.out.println("I am sorry I don't quite get what you input! Please try again!");
						logger.debug("exception found: {}", e);
					}
				} while(!correctChoice);
				
				System.out.printf("You have choosen %s\n", result);
				
				return result;
			}
			
			public String getDescription() {
				return name;
			}
		};
		
		Match match = new Match(humanPlayer,player);
		match.addObserver(m->System.out.println(m));
		match.setController(()->{
			System.out.printf("========================================\n");
			System.out.printf("No of Rounds Played: %s\n", match.getNoOfRoundPlayed());
			System.out.printf("%s win: %s\n", name, match.getNoOfPlayer1Win());
			System.out.printf("%s win: %s\n", player.getDescription(), match.getNoOfPlayer2Win());
			System.out.printf("No of Draws: %s\n", match.getNoOfDraw());
			System.out.printf("========================================\n");
			System.out.printf("%s, do you want to continue? (Press Y)\n", name);
			String choice = System.console().readLine();
			return ("Y".equalsIgnoreCase(choice.trim()));
		});
		match.setNoOfSecondWaitingForPlayer(30);
		match.setNoOfSecondWaitingForContinue(30);
		match.startMatch();
		
		showMatchResult(match,name,player);
	}
}
