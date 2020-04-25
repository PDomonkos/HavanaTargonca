package world;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

//TODO target package osztály, vagy egy flag
//TODO esetleg tudja a célját

public class Package extends Thing {
	
	Position dest;
	Color myColor = Color.gray;
	
	
	public Package(Tile t) {
		super(t);
	}
	
    public void Draw(@SuppressWarnings("exports") Graphics2D g,int size)
    {
    	g.setColor(myColor);
    	g.setStroke(new BasicStroke(0));
    	g.fillRect(GetX()*size+1, GetY()*size+1, size-1, size-1);
    }

    public int Cost(){
        return 9999999;
    }
    
    // ha van célja
    public void setDest(Position d) {
    	dest = d;
    	myColor = Color.red;
    }
}
