package world;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import app.App;

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
    	//g.setColor(myColor);
    	//g.setStroke(new BasicStroke(0));
    	//g.fillRect(GetX()*size+1, GetY()*size+1, size-1, size-1);
    	
    	String cellImage = "package";
    	
    	if (!cellImage.isEmpty()) {
            g.drawImage(App.getCachedImage(cellImage), GetX()*size+5, GetY()*size+5, size-7, size-7, null);
        }
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
