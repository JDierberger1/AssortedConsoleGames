package lib;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Board {

	private Tile[][] board;

	private int width;
	private int height;

	public Board() {
		this(16, 16);
	}

	public Board(int w, int h) {
		if (w <= 0) w = 16;
		if (h <= 0) h = 16;
		width = w;
		height = h;
		board = new Tile[h][w];
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				if (Math.random() > Math.max((2 * Math.sqrt(h * w)) / 100, 0.4)) {
					board[i][j] = new Tile(j, i,
							(i > 0 ? board[i - 1][j] : null),
							(i < h - 1 ? board[i + 1][j] : null),
							(j > 0 ? board[i][j - 1] : null),
							(j < w - 1 ? board[i][j + 1] : null));
				}
			}
		}
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				if (board[i][j] != null) {
					board[i][j].setNorth((i > 0 ? board[i - 1][j] : null));
					board[i][j].setSouth((i < h - 1 ? board[i + 1][j] : null));
					board[i][j].setEast((j > 0 ? board[i][j - 1] : null));
					board[i][j].setWest((j < w - 1 ? board[i][j + 1] : null));
				}
			}
		}
	}

	public void renderBoard(PrintStream out) {
		int hindent = Integer.toString(board.length).length() + 1;
		int windent = Math.max(Integer.toString(board[0].length).length() + 1, 2);
		
		for (int i = 0; i < hindent; i++) {
			out.print(" ");
		}
		for (int i = 0; i < board[0].length; i++) {
			System.out.printf("%-" + windent + "d", i);
		}
		out.println();
		
		int h = 0;
		for (Tile[] a : board) {
			System.out.printf("%-" + hindent + "d", h);
			for (Tile t : a) {
				System.out.printf("%s", t != null ? t.toString() : "   ");
			}
			System.out.println();
			h++;
		}
	}

	public int getWidth () {
		return width;
	}

	public int getHeight () {
		return height;
	}

	public Tile getTile(int x, int y) {
		return board[y][x];
	}

	public void setTile(int x, int y, Tile t) {
		board[y][x] = t;
	}

	public void update() {
		for (Tile[] a : board) {
			for (Tile t : a) {
				if (t != null) {
					t.update();
				}
			}
		}
	}

	public Entity[] getEntities() {
		LinkedList<Entity> list = new LinkedList<Entity>();
		for (Tile[] a : board) {
			for (Tile t : a) {
				for (Entity e : t.getOccupants()) {
					list.add(e);
				}
			}
		}
		return list.toArray(new Entity[list.size()]);
	}

	public void spawn(Entity e) {
		ArrayList<Tile> list = new ArrayList<Tile>();
		for (Tile[] a : board) {
			for (Tile t : a) {
				if (t != null && t.connenctedness() >= 3) {
					list.add(t);
				}
			}
		}
		Random r = new Random();
		if (list.size() > 0) {
			list.get(r.nextInt(list.size())).addOccupant(e);
		} else {
			Tile t;
			do {
				t = board[r.nextInt(width)][r.nextInt(height)];
			} while (t == null);
			t.addOccupant(e);
		}
	}

}