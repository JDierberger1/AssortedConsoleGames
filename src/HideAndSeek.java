import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

import lib.Board;
import lib.Entity;
import lib.Tile;

public class HideAndSeek {

	public static final String[] scareMessages = {
		"You hear something stir...",
		"Something stirs in the dark...",
		"What was that?",
		"Shadows stir out of the corner of your eye...",
		"The hair on the back of your neck stands...",
		"You feel something in the room with you...",
		"You felt something exiting the room..."
	};

	public static final String[] nearMessages = {
		"You see a light in the distance...",
		"A breeze blows from the distance...",
		"A sound echos from the distance...",
		"You hear a noise from the distance",
		"Something steps on a stick in the distance...",
		"Something shuffles from beyond the darkness..."
	};

	public static final Scanner scanner = new Scanner(System.in);

	public static final Random rand = new Random();

	public static Board board;

	public static final LinkedList<CoordinatePair> MOVES = new LinkedList<CoordinatePair>();
	private static final class CoordinatePair {
		int x;
		int y;
		Player p;
		CoordinatePair(int x, int y, Player p) { this.x = x; this.y = y; this.p = p; }
	}

	public static final Player P1 = new Player();

	public static final Player P2 = new Player();

	private static Player turn = rand.nextInt(2) == 0 ? P1 : P2;

	public static final class Player extends Entity {

		private Tile tile;

		public Player() {
			super("Player");
		}

		@Override
		public void update() {
			if (this != turn) return;
			boolean go = true;
			String player = (this == P1) ? "P1" : "P2";
			Player other = (this == P1) ? P2 : P1;
			clearConsole();
			tile.setTileChar(this == P1 ? '1' : '2');
			board.renderBoard(System.out);
			if (Math.random() > 0.1 && (other.tile.getX() == this.tile.getX()
					|| other.tile.getY() == this.tile.getY())) {
				System.out.println(nearMessages[rand.nextInt(nearMessages.length)]);
			} else if (Math.random() > 0.95) {
				System.out.println(scareMessages[rand.nextInt(scareMessages.length)]);
			}
			System.out.printf("(%d, %d)%n", tile.getY(), tile.getX());
			System.out.println("Enter an option (" + player + "):");
			System.out.println("    w. Go North");
			System.out.println("    s. Go South");
			System.out.println("    a. Go East");
			System.out.println("    d. Go West");
			System.out.println("    x. Attack");
			while (go) {
				switch (scanner.nextLine().toLowerCase()) {
				case "w":
					if (tile.getNorth() != null) {
						tile.setTileChar(' ');
						tile.removeOccupant(this);
						tile.getNorth().addOccupant(this);
						go = false;
					} else System.out.println("That way is blocked. (go again)");
					break;
				case "s":
					if (tile.getSouth() != null) {
						tile.setTileChar(' ');
						tile.removeOccupant(this);
						tile.getSouth().addOccupant(this);
						go = false;
					} else System.out.println("That way is blocked. (go again)");
					break;
				case "a":
					if (tile.getEast() != null) {
						tile.setTileChar(' ');
						tile.removeOccupant(this);
						tile.getEast().addOccupant(this);
						go = false;
					} else System.out.println("That way is blocked. (go again)");
					break;
				case "d":
					if (tile.getWest() != null) {
						tile.setTileChar(' ');
						tile.removeOccupant(this);
						tile.getWest().addOccupant(this);
						go = false;
					} else System.out.println("That way is blocked. (go again)");
					break;
				case "x":
					if (tile.getOccupants().length > 1) {
						if (this == P1) end(P1);
						if (this == P2) end(P2);
					}
					tile.setTileChar(' ');
					go = false;
					break;
				default:
					System.out.println("That is not a valid option (go again)");
					break;
				}
			}
			MOVES.add(new CoordinatePair(tile.getX(), tile.getY(), this));
			turn = turn == P1 ? P2 : P1;
			clearConsole();
			System.out.print("Press enter to continue to next turn...");
			scanner.nextLine();
		}

		@Override
		public double visibility() {
			return 0;
		}

		@Override
		public double getHealth() {
			return 0;
		}

		@Override
		public void onChangeTile(Tile t) {
			tile = t;
		}

		@Override
		public void onEntityEnterTile(Entity e) {
			if (Math.random() >= 0.5) {
				System.out.println("You hear definite footsteps behind you...");
			} else if (Math.random() > 0.15) {
				System.out.println(scareMessages[rand.nextInt(scareMessages.length)]);
			}
		}

		@Override
		public void interact(Entity other) {
		}

		@Override
		public void onEntityLeaveTile(Entity e) {
			if (Math.random() > 0.1 && e != this) {
				System.out.println(scareMessages[rand.nextInt(scareMessages.length)]);
			}
		}

		@Override
		public String toString() {
			return "Player";
		}

		@Override
		public void setHealth(double d) {
		}
	};

	public static void main(String[] args) {
		clearConsole();
		int w = (args.length > 0) ? Integer.valueOf(args[0]) : 16;
		int h = (args.length > 1) ? Integer.valueOf(args[1]) : 16;
		board = new Board(w, h);
		board.spawn(P1);
		board.spawn(P2);
		MOVES.add(new CoordinatePair(P1.tile.getX(), P1.tile.getY(), P1));
		MOVES.add(new CoordinatePair(P2.tile.getX(), P2.tile.getY(), P2));
		while (true) {
			System.out.println("Start");
			board.update();
		}
	}

	public static void replayGame(Board b) {
		CoordinatePair move = MOVES.get(0);
		b.getTile(move.x, move.y).setTileChar(move.p == P1 ? '1' : '2');
		clearConsole();
		board.renderBoard(System.out);
		stop(500);
		for (CoordinatePair c : MOVES) {
			clearConsole();
			b.getTile(c.x, c.y).setTileChar(c.p == P1 ? '1' : '2');

			b.getTile(move.x, move.y).setTileChar(' ');
			move = c;
			board.renderBoard(System.out);
			stop(500);
		}
	}

	public static void stop(long l) {
		long start = System.currentTimeMillis();
		while (System.currentTimeMillis() - start < l) {
			Runtime.getRuntime().freeMemory();
		}
	}

	public static void end(Entity e) {
		if (e == P1) {
			System.out.println("Player 1 wins!");
		} else {
			System.out.println("Player 2 wins!");
		}
		while (true) {
			System.out.println("Watch replay?");
			if (scanner.nextLine().toLowerCase().charAt(0) == 'y') {
				replayGame(board);
			} else {
				System.exit(0);
			}
		}
	}

	public static void clearConsole() {
		try {
			String osName = System.getProperty("os.name");
			if (osName.contains("Windows")) {
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			} else {
				Runtime.getRuntime().exec("clear");
			}
		} catch (Exception e) {
		}
	}

}