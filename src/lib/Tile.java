package lib;
import java.util.HashSet;

/**
 * Game tile.
 * @author J-Dierberger
 */
public class Tile {

	private final int[] coordinates = new int[2];

	private Tile north;
	private Tile south;
	private Tile east;
	private Tile west;

	private char tileChar = ' ';

	private final HashSet<Entity> occupants = new HashSet<Entity>();

	public Tile(int x, int y, Tile n, Tile s, Tile e, Tile w) {
		coordinates[0] = x;
		coordinates[1] = y;
		north = n;
		south = s;
		east = e;
		west = w;
	}

	public Tile getNorth() {
		return north;
	}

	public Tile getSouth() {
		return south;
	}

	public Tile getEast() {
		return east;
	}

	public Tile getWest() {
		return west;
	}

	void setNorth(Tile t) {
		north = t;
	}

	void setSouth(Tile t) {
		south = t;
	}

	void setEast(Tile t) {
		east = t;
	}

	void setWest(Tile t) {
		west = t;
	}

	public int getX() {
		return coordinates[0];
	}

	public int getY() {
		return coordinates[1];
	}

	public void setTileChar(char c) {
		tileChar = c;
	}

	public boolean addOccupant(Entity e) {
		if (occupants.add(e)) {
			e.onChangeTile(this);
			for (Entity e2 : occupants) {
				if (!e2.equals(e)) {
					e2.onEntityEnterTile(e);
				}
			}
			return true;
		}
		return false;
	}

	public boolean removeOccupant(Entity e) {
		if (occupants.remove(e)) {
			for (Entity e2 : occupants) {
				if (!e2.equals(e)) {
					e2.onEntityLeaveTile(e);
				}
			}
			return true;
		}
		return false;
	}

	public Entity[] getOccupants() {
		return occupants.toArray(new Entity[occupants.size()]);
	}

	public void update() {
		for (Entity e : occupants) {
			e.update();
		}
	}

	@Override
	public String toString() {
		return String.format("[%c]", tileChar);
	}

	public int connenctedness() {
		int r = 0;
		r += (north != null) ? 1 : 0;
		r += (south != null) ? 1 : 0;
		r += (east != null) ? 1 : 0;
		r += (west != null) ? 1 : 0;
		return r;
	}

}
