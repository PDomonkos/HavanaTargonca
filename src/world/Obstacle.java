package world;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;


public class Obstacle extends Thing {
	
	public Obstacle(Tile t) {
		super(t);
	}
	
    public void Draw(@SuppressWarnings("exports") Graphics2D g, int size)
    {
    	g.setColor(Color.black);
    	g.setStroke(new BasicStroke(0));
    	g.fillRect(GetX()*size+1, GetY()*size+1, size-1, size-1);
    }

    public int Cost()
    {
        return 9999999;
    }
}
