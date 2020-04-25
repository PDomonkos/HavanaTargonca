package AStar;

import world.Position;
import world.Direction;

public class Node implements Comparable<Node>{
	
    // oda vezet� k�lts�g teljesen publikus
    public int G;
    //alulr�l becsl� heurisztika a c�lig teljesen publikus
    public int H;
    //kett� �sszege, �sszehasonl�t�s alapja teljesen publikus
    public int F;
    //sz�l� node, csak lek�rdezhet�
    private Node parent;
    //sz�l�b�l ide vezet� ir�ny, csak lek�rdezhet�
    private Direction dir; 
    //poz�ci�
    private Position myPosition;

    //1. konstruktor koordin�t�k megad�s�val
    public Node(int fromX, int fromY)
    {
        myPosition = new Position(fromX, fromY);
    }

    //2. m�sik node-b�l kl�noz, ir�nyt t�rolja amin kereszt�l hozz� jutott a parent node
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
