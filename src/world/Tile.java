package world;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;
import AStar.Searchable;
import app.Drawable;

public class Tile implements Drawable, Searchable {
	//rajta lévõ dolog amit lehet rajzolni
    private Thing myThing;
    // 4 szomszéd
    private Map<Direction, Tile> neighbours;
    //Számolja hányszor volt rajta targonca a heatmaphez
    public int count;
    // pozíció
    public Position myPosition;

    public Tile(int X, int Y){
    	myPosition = new Position(X,Y);
        count = 0;
        myThing = null;
        neighbours = null;
    }

    public void SetNeighbours(Tile u, Tile r, Tile d, Tile l){
        neighbours = new HashMap<Direction, Tile>();
        neighbours.put(Direction.up, u);
        neighbours.put(Direction.right, r);
        neighbours.put(Direction.down, d);
        neighbours.put(Direction.left, l);
    }
    
    public Tile GetNeighbour(Direction d){
        if (neighbours == null)
            return null;
        return neighbours.get(d);
    }
    
    public int GetCount() {
    	return count;
    }

    public int GetX() {
    	return myPosition.GetX();
    }
    
    public int GetY() {
    	return myPosition.GetY();
    }
    
    //rárak egy dolgot 
    public void Add(Thing t){
        this.myThing = t;
        myThing.setTile(this);
    }

    //eltávolítja a rajra lévõ dolgot, és visszatér vele
    public Thing Remove(){
        Thing ret = myThing;
        this.myThing = null;
        return ret;
    }

    public void Inc() { count++; }

    public void Draw(@SuppressWarnings("exports") Graphics2D g, int size){   	
    	if (count == 0) { g.setColor(Color.white); }
    	else{
            int red = 8 * count;
            if (red > 255) red = 255;
            g.setColor(new Color(255, 255-red, 255-red));
        }
    	g.setStroke(new BasicStroke(0));
    	g.fillRect(GetX()*size+1, GetY()*size+1, size-1, size-1);
    	
        if (myThing != null)
        {
            myThing.Draw(g, size);
        }

    }

    public int Cost()
    {
        if (myThing != null)
        {
            return myThing.Cost();
        }
        else
        {
        	//Count lehetne de az nem jobb
            return 1;
        }
    }
}
