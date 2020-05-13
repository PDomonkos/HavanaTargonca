package world;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import app.App;


public class Package extends Thing {
	
	Position dest = null;
	Color myColor = Color.gray;
	
	public Package(Tile t) {
		super(t);
		t.Add(this);
	}
	
	public Package(Tile t, Position d) {
		super(t);
		t.Add(this);
		dest = d;
	}
	
    public void Draw(@SuppressWarnings("exports") Graphics2D g,int size)
    {
    	if (dest != null) {
    		g.setColor(myColor);
    		g.setStroke(new BasicStroke(0));
    		g.fillRect(GetX()*size, GetY()*size, size, size);
    	}
    	
    	String cellImage = "package";
    	
    	if (!cellImage.isEmpty()) {
            g.drawImage(App.getCachedImage(cellImage), GetX()*size+4, GetY()*size+4, size-8, size-8, null);
        }
    }

    public int Cost(){
        return 9999999;
    }
    
    public void setColor(@SuppressWarnings("exports") Color c) {
    	myColor = c;
    }
    
    public int GetDestX() {
    	if (dest != null)
    		return dest.GetX();
    	else
    		return -1;
    }
    
    public int GetDestY() {
    	if (dest != null)
    		return dest.GetY();
    	else
    		return -1;
    }
}
