package world;

public class Position {
	private int x;
	private int y;
	
	public Position(int newX, int newY) {
		x = newX;
		y = newY;
	}
	
	public int GetX() {
		return x;
	}
	public int GetY() {
		return y;
	}
	public void SetX(int newX) {
		x = newX;
	}
	public void SetY(int newY) {
		y = newY;
	}
}
