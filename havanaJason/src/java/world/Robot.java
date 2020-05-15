package world;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import app.App;

public class Robot extends Thing{
	//célok koordinátái
    Stack<Position> dest;
    //útvonal
    List<Direction> path = new ArrayList<Direction>();
    //szállított csomag
    private Package myPackage;
    // egyéni szín
    private Color myColor = Color.blue;

    //szavazás miatt
    private List<Package> myGoalPackages;
    private Package myLastBidPackage = null;
    private int myLastBidValue = 0;
    private int lastIndex = -1;
    private int sumPath = 0;
    private int myLastSumPath = 0;
    
    public Robot(Tile t, float c){
    	super(t);
        myPackage = null;
        t.Add(this);
        t.Inc();
        dest = new Stack<Position>();
        myGoalPackages = new ArrayList<Package>();
        myColor = new Color(150,255-Math.round(255*c),Math.round(255*c));
    }

    public void move(Direction d){
        myTile.Remove();
        Tile t = myTile.GetNeighbour(d);
        t.Add(this);
        t.Inc();
        if (myPackage != null)
        	myPackage.setTile(myTile);
    }

    public void Draw(@SuppressWarnings("exports") Graphics2D g, int size){ 
        g.setColor(myColor);
    	g.setStroke(new BasicStroke(0));
    	g.fillRect(GetX()*size, GetY()*size, size, size);

    	
    	String cellImage = "robot";
    	
    	if (!cellImage.isEmpty()) {
            g.drawImage(App.getCachedImage(cellImage), GetX()*size+4, GetY()*size+4, size-8, size-8, null);
        }
    	
    	if (myPackage != null) {
    		myPackage.Draw(g, size);
    	}

    }

    //ütközés heurisztikája, végtelen (de idõbeliséget nem figyel)
    public int Cost(){
        return 999;
    }

    public void addDestination(Position p){
        dest.push(p);
    }

    //nem vár ha ütközés lenne, hanem másmerre megy 
    //Egy lépést tesz a soron következõ célja felé
    public boolean step(Tile[][] tiles){
        //ha nincs már célja akkor nem csinál semmit, false-al tér vissza
        if (dest.size() == 0) return false;

        //cél koorináták
        Position destination = dest.peek();

        //útvonaltervezés
        path = App.aStar.goTo(tiles, new Position(GetX(),GetY()),destination);

        //egy lépés a cél felé
        move(path.get(0));

        //ha a cél mellé érünk, akkor kivesszük az adott célt a listából, és lerakjuk/felvesszük a szállítmányt
        if ((destination.GetX() == GetX() && Math.abs(destination.GetY()-GetY())==1) || (destination.GetY() == GetY() && Math.abs(destination.GetX() - GetX()) == 1))
        {
            dest.pop();
            //ha nem viszünk csomagot felvesszük azt, különben lerakjuk
            if (myPackage == null) {
                myPackage = (Package)tiles[destination.GetX()][destination.GetY()].Remove();
            	myPackage.setTile(myTile);
            }
            else{
                tiles[destination.GetX()][destination.GetY()].Add(myPackage);
                myPackage = null;
            }

            if (dest.size() == 0) return false;
        }

//lehetne itt falseal visszatérni és legközebb be se lépne
        // igazzal tér vissza
        return true;
    }
    
    @SuppressWarnings("exports")
	public Color getColor() {
    	return myColor;
    }
    
/////////////////////////////////////////////////////////////////////////////////
    public int bid() {
    	if (myLastBidPackage != null && App.map.isPInPackages(myLastBidPackage))
    		return myLastBidValue;
    	
    	myLastBidPackage = null;
        myLastBidValue = 999999;
        lastIndex = -1;
        myLastSumPath = 0;
        
        for (Package p : App.map.getPackages()) {
        	int bestBid = 999999;
        	List<Package> tmp;
        	if (myGoalPackages == null) 
        		tmp = new ArrayList();
        	else
        		tmp = new ArrayList(myGoalPackages);
        	int attempts = tmp.size();
        	for (int i = 0; i< attmepts)
        	
        }
        
    	return myLastBidValue;
    }
    
    public void win() {
    	Package p = myLastBidPackage;
    	myLastBidPackage = null;
    	myLastBidValue = 0;
    	p.setColor(myColor);
    	App.map.removePackage(p);
    	myGoalPackages.add(lastIndex, p);
    	lastIndex = -1;
    	sumPath = myLastSumPath;
    }
    
    public void getReady() {
    	if (myGoalPackages != null) {
    		// ezt lehet fordítva kell bejárni
    		for (Package p : myGoalPackages) {
    			addDestination(new Position(p.GetDestX(), p.GetDestY()));
    	    	addDestination(new Position(p.GetX(), p.GetY()));
    		}
    	}
    }
    
}