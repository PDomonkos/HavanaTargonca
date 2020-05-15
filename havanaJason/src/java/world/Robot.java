package world;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import app.App;

public class Robot extends Thing{
	//cï¿½lok koordinï¿½tï¿½i
    Stack<Position> dest;
    //ï¿½tvonal
    List<Direction> path = new ArrayList<Direction>();
    //szï¿½llï¿½tott csomag
    private Package myPackage;
    // egyï¿½ni szï¿½n
    private Color myColor = Color.blue;

    //szavazï¿½s miatt
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

    //ï¿½tkï¿½zï¿½s heurisztikï¿½ja, vï¿½gtelen (de idï¿½belisï¿½get nem figyel)
    public int Cost(){
        return 999;
    }

    public void addDestination(Position p){
        dest.push(p);
    }

    //nem vï¿½r ha ï¿½tkï¿½zï¿½s lenne, hanem mï¿½smerre megy 
    //Egy lï¿½pï¿½st tesz a soron kï¿½vetkezï¿½ cï¿½lja felï¿½
    public boolean step(Tile[][] tiles){
        //ha nincs mï¿½r cï¿½lja akkor nem csinï¿½l semmit, false-al tï¿½r vissza
        if (dest.size() == 0) return false;

        //cï¿½l koorinï¿½tï¿½k
        Position destination = dest.peek();

        //ï¿½tvonaltervezï¿½s
        path = App.aStar.goTo(tiles, new Position(GetX(),GetY()),destination);

        //egy lï¿½pï¿½s a cï¿½l felï¿½
        move(path.get(0));

        //ha a cï¿½l mellï¿½ ï¿½rï¿½nk, akkor kivesszï¿½k az adott cï¿½lt a listï¿½bï¿½l, ï¿½s lerakjuk/felvesszï¿½k a szï¿½llï¿½tmï¿½nyt
        if ((destination.GetX() == GetX() && Math.abs(destination.GetY()-GetY())==1) || (destination.GetY() == GetY() && Math.abs(destination.GetX() - GetX()) == 1))
        {
            dest.pop();
            //ha nem viszï¿½nk csomagot felvesszï¿½k azt, kï¿½lï¿½nben lerakjuk
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

//lehetne itt falseal visszatï¿½rni ï¿½s legkï¿½zebb be se lï¿½pne
        // igazzal tï¿½r vissza
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
    	
        int bestSumPath = 999999;
    	int bestIndex = -1;
    	Package bestPackage = null;
    	
        for (Package p : App.map.getPackages()) {
        	List<Package> tmp;
        	if (myGoalPackages == null) 
        		tmp = new ArrayList();
        	else
        		tmp = new ArrayList(myGoalPackages);
        	
        	int attempts = tmp.size();
        	for (int i = 0; i< attempts+1; i++) {
        		List<Package> tmp2 = new ArrayList(tmp);
        		tmp2.add(i,p);
        		
        		//számolni
        		int newSumPath = 0;
        		List<Position> newPath = new ArrayList<Position>();
        		newPath.add(new Position(GetX(),GetY()));
        		for (Package pp : tmp2) {
        			newPath.add(new Position(pp.GetX(),pp.GetY()));
        			newPath.add(new Position(pp.GetDestX(),pp.GetDestY()));
        		}
        		
        		Position start = new Position(GetX(),GetY());
        		for (Position pos : newPath) {
        			newSumPath += App.aStar.getDist(start, pos);
        			start = pos;
        		}
        		
        		if (newSumPath < bestSumPath) {
        			bestIndex = i;
        			bestSumPath = newSumPath;
        			bestPackage = p;	
        		}
        	}
        	
        }
        
        myLastBidPackage = bestPackage;
        myLastBidValue = bestSumPath-sumPath;
        lastIndex = bestIndex;
        myLastSumPath = bestSumPath;
        
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
    		// ezt lehet fordï¿½tva kell bejï¿½rni
    		for (Package p : myGoalPackages) {
    			addDestination(new Position(p.GetDestX(), p.GetDestY()));
    	    	addDestination(new Position(p.GetX(), p.GetY()));
    		}
    	}
    }
    
}