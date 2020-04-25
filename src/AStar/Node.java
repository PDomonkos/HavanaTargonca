package AStar;

import world.Position;
import world.Direction;

public class Node implements Comparable<Node>{
	
    // oda vezetõ költség teljesen publikus
    public int G;
    //alulról becslõ heurisztika a célig teljesen publikus
    public int H;
    //kettõ összege, összehasonlítás alapja teljesen publikus
    public int F;
    //szülõ node, csak lekérdezhetõ
    private Node parent;
    //szülöbõl ide vezetõ irány, csak lekérdezhetõ
    private Direction dir; 
    //pozíció
    private Position myPosition;

    //1. konstruktor koordináták megadásával
    public Node(int fromX, int fromY)
    {
        myPosition = new Position(fromX, fromY);
    }

    //2. másik node-ból klónoz, irányt tárolja amin keresztül hozzá jutott a parent node
    public Node(Node P, Direction d)
    {
            int X = P.GetX();
            int Y = P.GetY();
            parent = P;
            dir = d;
            switch (dir)
            {
                case up:
                    Y--;
                    break;
                case down:
                    Y++;
                    break;
                case right:
                    X++;
                    break;
                case left:
                    X--;
                    break;
            }
            myPosition = new Position(X,Y);
    }

    public int GetX() {
    	return myPosition.GetX();
    }
    
    public int GetY() {
    	return myPosition.GetY();
    }
    
    public Node GetParent() {
    	return parent;
    }
    
    public Direction GetDirection() {
    	return dir;
    }

	@Override
	public int compareTo(Node o) {
		return this.F - o.F;
	}
}
