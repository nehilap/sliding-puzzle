package sk.tuke.gamestudio.game.puzzle.nehila.core;

public class Fragment extends Tile {
	private final int index;
	
	public Fragment(int index) {
		this.index = index;
	}
	
	public int getIndex() {
		return index;
	}
	
	@Override
	public String toString() {
		return this.getIndex() + "";
	}
}
