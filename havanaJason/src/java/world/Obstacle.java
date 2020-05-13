package world;

import java.awt.Graphics2D;

import app.App;


public class Obstacle extends Thing {
	
	public Obstacle(Tile t) {
		super(t);
		t.Add(this);
	}
	
    public void Draw(@SuppressWarnings("exports") Graphics2D g, int size)
    {
   	
    	String cellImage = "brick";
    	
    	if (!cellImage.isEmpty()) {
            g.drawImage(App.getCachedImage(cellImage), GetX()*size, GetY()*size, size, size, null);
        }
    }

    public int Cost()
    {
        return 9999999;
    }
}
