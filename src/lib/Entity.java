package lib;
/**
 * Game entity.
 * @author J-Dierberger
 */
public abstract class Entity {

	private static int IDctr = 0;

	public final int id;
	protected final String typename;

	public Entity(String type) {
		typename = type;
		id = IDctr++;
	}

	@Override
	public final boolean equals(Object o) {
		return o instanceof Entity && ((Entity)o).typename.equals(typename)
				&& ((Entity)o).id == id;
	}

	@Override
	public final int hashCode() {
		return typename.hashCode() + id * 31;
	}

	public abstract void update();

	public abstract double visibility();

	public abstract double getHealth();

	public abstract void setHealth(double d);

	public abstract void onChangeTile(Tile t);

	public abstract void onEntityEnterTile(Entity e);

	public abstract void interact(Entity other);

	public abstract void onEntityLeaveTile(Entity e);

	@Override
	public abstract String toString();

}
