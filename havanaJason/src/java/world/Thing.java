package world;

import AStar.Searchable;
import app.Drawable;

public abstract class Thing implements Drawable, Searchable{
	protected Tile myTile;
	
	public Thing(Tile t) {
		myTile = t;
	}
	
	public void setTile(Tile t) {
		myTile = t;
	}
	
	public int GetX() {
		return myTile.GetX();
	}
	
	public int GetY() {
		return myTile.GetY();
	}
}
