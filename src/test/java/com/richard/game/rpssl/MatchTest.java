package com.richard.game.rpssl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MatchTest {
	private static final Logger logger = LoggerFactory.getLogger(MatchTest.class);
	
	@Test(expected=IllegalArgumentException.class)
	public void testMissingPlayer1AndPlayer2() {
		new Match(null,null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testMissingPlayer1() {
		new Match(null, new GamePlayer() {
			public Choice choose() {
				return null;
			}
			
			public String getDescription() {
				return null;
			}
		});
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testMissingPlayer2() {
		new Match(new GamePlayer() {
			public Choice choose() {
				return null;
			}
			
			public String getDescription() {
				return null;
			}
		}, null);
	}
	
	@Test
	public void testStartMatchNoController() {
		Match match = new Match(new GamePlayer() {
			public Choice choose() {
				return Choice.ROCK;
			}
			
			public String getDescription() {
				return "Player A";
			}
		},
				new GamePlayer() {
			public Choice choose() {
				return Choice.PAPER;
			}
			
			public String getDescription() {
				return "Player B";
			}
		});
		
		match.startMatch();
		
		assertEquals(0,match.getNoOfDraw());
		assertEquals(0,match.getNoOfPlayer1Win());
		assertEquals(1,match.getNoOfPlayer2Win());
		assertEquals(1,match.getNoOfRoundPlayed());
		
	}
	
	@Test
	public void testStartMatchControllerWith1Round() {
		Match match = new Match(new GamePlayer() {
			public Choice choose() {
				return Choice.ROCK;
			}
			
			public String getDescription() {
				return "Player A";
			}
		},
				new GamePlayer() {
			public Choice choose() {
				return Choice.PAPER;
			}
			
			public String getDescription() {
				return "Player B";
			}
		});
		
		
		match.setController(() -> {return false;});
		
		match.startMatch();
		
		assertEquals(0,match.getNoOfDraw());
		assertEquals(0,match.getNoOfPlayer1Win());
		assertEquals(1,match.getNoOfPlayer2Win());
		assertEquals(1,match.getNoOfRoundPlayed());
		
		
		match = new Match(new GamePlayer() {
			public Choice choose() {
				return Choice.PAPER;
			}
			
			public String getDescription() {
				return "Player A";
			}
		},
				new GamePlayer() {
			public Choice choose() {
				return Choice.PAPER;
			}
			
			public String getDescription() {
				return "Player B";
			}
		});
		
		match.setController(() -> {return false;});
		
		match.startMatch();
		
		assertEquals(1,match.getNoOfDraw());
		assertEquals(0,match.getNoOfPlayer1Win());
		assertEquals(0,match.getNoOfPlayer2Win());
		assertEquals(1,match.getNoOfRoundPlayed());
		
		match = new Match(new GamePlayer() {
			public Choice choose() {
				return Choice.PAPER;
			}
			
			public String getDescription() {
				return "Player A";
			}
		},
				new GamePlayer() {
			public Choice choose() {
				return Choice.ROCK;
			}
			
			public String getDescription() {
				return "Player B";
			}
		});
		
		match.setController(() -> {return false;});
		
		match.startMatch();
		
		assertEquals(0,match.getNoOfDraw());
		assertEquals(1,match.getNoOfPlayer1Win());
		assertEquals(0,match.getNoOfPlayer2Win());
		assertEquals(1,match.getNoOfRoundPlayed());

	}
	
	@Test
	public void testStartMatchControllerWith2Rounds() {
		Match match = new Match(new GamePlayer() {
			public Choice choose() {
				return Choice.ROCK;
			}
			
			public String getDescription() {
				return "Player A";
			}
		},
				new GamePlayer() {
			public Choice choose() {
				return Choice.PAPER;
			}
			
			public String getDescription() {
				return "Player B";
			}
		});
		
		match.setController(new MatchControl() {
			private int round = 0;
			public boolean isContinue() {
				round++;
				if (round < 2) {
					return true;
				} else {
					return false;
				}
			}
		});
		
		match.startMatch();
		
		assertEquals(0,match.getNoOfDraw());
		assertEquals(0,match.getNoOfPlayer1Win());
		assertEquals(2,match.getNoOfPlayer2Win());
		assertEquals(2,match.getNoOfRoundPlayed());
		
		match = new Match(new GamePlayer() {
			public Choice choose() {
				return Choice.PAPER;
			}
			
			public String getDescription() {
				return "Player A";
			}
		},
				new GamePlayer() {
			public Choice choose() {
				return Choice.PAPER;
			}
			
			public String getDescription() {
				return "Player B";
			}
		});
		
		match.setController(new MatchControl() {
			private int round = 0;
			public boolean isContinue() {
				round++;
				if (round < 2) {
					return true;
				} else {
					return false;
				}
			}
		});
		
		match.startMatch();
		
		assertEquals(2,match.getNoOfDraw());
		assertEquals(0,match.getNoOfPlayer1Win());
		assertEquals(0,match.getNoOfPlayer2Win());
		assertEquals(2,match.getNoOfRoundPlayed());

		match = new Match(new GamePlayer() {
			public Choice choose() {
				return Choice.PAPER;
			}
			
			public String getDescription() {
				return "Player A";
			}
		},
				new GamePlayer() {
			public Choice choose() {
				return Choice.ROCK;
			}
			
			public String getDescription() {
				return "Player B";
			}
		});
		
		match.setController(new MatchControl() {
			private int round = 0;
			public boolean isContinue() {
				round++;
				if (round < 2) {
					return true;
				} else {
					return false;
				}
			}
		});
		
		match.startMatch();
		
		assertEquals(0,match.getNoOfDraw());
		assertEquals(2,match.getNoOfPlayer1Win());
		assertEquals(0,match.getNoOfPlayer2Win());
		assertEquals(2,match.getNoOfRoundPlayed());
	}
	
	@Test
	public void testGamePlayerChooseTimeout() {
		Match match = new Match(new GamePlayer() {
			public Choice choose() {
				try {
					Thread.sleep(5000);
				}
				catch (Exception e) {
					
				}
				return Choice.PAPER;
			}
			
			public String getDescription() {
				return "Player A";
			}
		},
				new GamePlayer() {
			public Choice choose() {
				return Choice.ROCK;
			}
			
			public String getDescription() {
				return "Player B";
			}
		});
		
		match.setNoOfSecondWaitingForPlayer(1);
		match.startMatch();
		
		assertEquals(0,match.getNoOfDraw());
		assertEquals(0,match.getNoOfPlayer1Win());
		assertEquals(0,match.getNoOfPlayer2Win());
		assertEquals(0,match.getNoOfRoundPlayed());
	
	}
	
	@Test
	public void testWaitForContinueTimeout() {
		Match match = new Match(new GamePlayer() {
			public Choice choose() {
				return Choice.PAPER;
			}
			
			public String getDescription() {
				return "Player A";
			}
		},
				new GamePlayer() {
			public Choice choose() {
				return Choice.ROCK;
			}
			
			public String getDescription() {
				return "Player B";
			}
		});
		
		match.setNoOfSecondWaitingForContinue(1);
		match.setController(()->{try {Thread.sleep(10000);} catch(Exception e) {};return false;});
		match.startMatch();
		
		assertEquals(0,match.getNoOfDraw());
		assertEquals(1,match.getNoOfPlayer1Win());
		assertEquals(0,match.getNoOfPlayer2Win());
		assertEquals(1,match.getNoOfRoundPlayed());
	}
	
	@Test(expected=IllegalStateException.class)
	public void testMatchStartedTwice() {
		Match match = new Match(new GamePlayer() {
			public Choice choose() {
				return Choice.PAPER;
			}
			
			public String getDescription() {
				return "Player A";
			}
		},
				new GamePlayer() {
			public Choice choose() {
				return Choice.ROCK;
			}
			
			public String getDescription() {
				return "Player B";
			}
		});
		
		match.startMatch();
		match.startMatch();
		
	}
	
	@Test
	public void testMatchStartedConcurrently() throws ExecutionException, TimeoutException, InterruptedException{
		Match match = new Match(new GamePlayer() {
			public Choice choose() {
				return Choice.PAPER;
			}
			
			public String getDescription() {
				return "Player A";
			}
		},
				new GamePlayer() {
			public Choice choose() {
				return Choice.ROCK;
			}
			
			public String getDescription() {
				return "Player B";
			}
		});
		
		ExecutorService executor = Executors.newFixedThreadPool(2);
		Callable<Boolean> callable = new Callable<Boolean>() {
			public Boolean call() {
				boolean exceptionFound = false;
				try {
					match.startMatch();
				}
				catch (Exception e) {
					logger.debug("exception found: {}", e);
					exceptionFound = true;
				}
				
				return exceptionFound;
			}
		};
		
		Future<Boolean> future1 = executor.submit(callable);
		Future<Boolean> future2 = executor.submit(callable);
		
		assertEquals(false,future1.get() && future2.get());
	}
	
	@Test
	public void testMatchNotifyObservers() {
		List<String> messageList = new ArrayList<String>();
		
		Match match = new Match(new GamePlayer() {
			public Choice choose() {
				return Choice.ROCK;	
			}
			public String getDescription() {
				return "player a";
			}
			public void onReceive(String message) {
				logger.debug("player a received message: {}", message);
			}
		}, new GamePlayer() {
			public Choice choose() {
				return Choice.ROCK;	
			}
			public String getDescription() {
				return "player b";
			}
			public void onReceive(String message) {
				logger.debug("player b received message: {}", message);
			}
		});
		
		match.setController(new MatchControl() {
        	private int count = 0;
        	public boolean isContinue() {
        		++count;
        		return count < 2;
        	}
        });
		
		match.addObserver(m->messageList.add(m));
		
		match.startMatch();
		logger.debug("message list: {}", messageList);
		assertEquals(6,messageList.size());
	}
}
