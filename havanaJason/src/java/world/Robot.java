package world;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import app.App;

public class Robot extends Thing{
	//c�lok koordin�t�i
    Stack<Position> dest;
    //�tvonal
    List<Direction> path = new ArrayList<Direction>();
    //sz�ll�tott csomag
    private Package myPackage;
    // egy�ni sz�n
    private Color myColor = Color.blue;

    //szavaz�s miatt
    private List<Package> myGoalPackages;
    private Package myLastBidPackage = null;
    private int myLastBidValue = 0;
    private int lastIndex = -1;
    
    private int sumPath = 0;
    private int minAve = 0;
    
    private int myLastMax = 0;
    private int myLastSum = 0;
    private int myLastAve = 0;
    
    public Robot(Tile t, float c){
    	super(t);
        myPackage = null;
        t.Add(this);
        t.Inc();
        dest = new Stack<Position>();
        myGoalPackages = new ArrayList<Package>();
        Random r = new Random();
        myColor = new Color(Math.round(20*c),255-Math.round(255*c),r.nextInt(255));
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

    //�tk�z�s heurisztik�ja, v�gtelen (de id�belis�get nem figyel)
    public int Cost(boolean test){
        if (test)
        	return 0;
    	return 999;
    }

    public void addDestination(Position p){
        dest.push(p);
    }

    //nem v�r ha �tk�z�s lenne, hanem m�smerre megy 
    //Egy l�p�st tesz a soron k�vetkez� c�lja fel�
    public boolean step(Tile[][] tiles){
        //ha nincs m�r c�lja akkor nem csin�l semmit, false-al t�r vissza
        if (dest.size() == 0) return false;

        //c�l koorin�t�k
        Position destination = dest.peek();

        //�tvonaltervez�s
        path = App.aStar.goTo(tiles, new Position(GetX(),GetY()),destination);

        //egy l�p�s a c�l fel�
        move(path.get(0));

        //ha a c�l mell� �r�nk, akkor kivessz�k az adott c�lt a list�b�l, �s lerakjuk/felvessz�k a sz�ll�tm�nyt
        if ((destination.GetX() == GetX() && Math.abs(destination.GetY()-GetY())==1) || (destination.GetY() == GetY() && Math.abs(destination.GetX() - GetX()) == 1))
        {
            dest.pop();
            //ha nem visz�nk csomagot felvessz�k azt, k�l�nben lerakjuk
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

//lehetne itt falseal visszat�rni �s legk�zebb be se l�pne
        // igazzal t�r vissza
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
	
        int bestAve = 999999;
        int bestSum = 999999;
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
        		
        		int newBestAve = 0;
        		int newBestSum = 0;
        		/*List<Position> newPath = new ArrayList<Position>();
        		for (Package pp : tmp2) {
        			newPath.add(new Position(pp.GetX(),pp.GetY()));
        			newPath.add(new Position(pp.GetDestX(),pp.GetDestY()));
        		}*/
        					    	        	
	        	Position start = new Position(GetX(),GetY());
	        	int pathTill = 0;
        		for (Package pac : tmp2) {
        			Position sp = new Position(pac.GetX(),pac.GetY());
        			Position dp = new Position(pac.GetDestX(),pac.GetDestY());
        			int temp = App.aStar.getDist(start, sp) + App.aStar.getDist(sp, dp);
        			newBestAve += temp + pathTill;
        			pathTill += temp;
        			newBestSum += temp;
        			start = dp;
        		}
        		
        		
        		/*start = new Position(GetX(),GetY());
        		for (Position pos : newPath) {
        			best += App.aStar.getDist(start, pos);
        			start = pos;
        		}*/
        		
        		
        		if ((App.getMode() == 3 && newBestAve < bestAve ) || (newBestSum < bestSum) ) {
        			bestIndex = i;
        			bestAve = newBestAve;
        			bestSum = newBestSum;
        			bestPackage = p;	
        		}
        	}
        	
        }
        
        myLastBidPackage = bestPackage;
        lastIndex = bestIndex;
        
        myLastMax = bestSum;
        myLastSum = bestSum;
        myLastAve = bestAve;

        switch (App.getMode()) {
	        case 1 :
	        	myLastBidValue = myLastMax;
	      	  break;
	        case 2 :
	        	myLastBidValue = myLastSum - sumPath;
	      	  break;
	        default:
	        	myLastBidValue = myLastAve - minAve;
        }
        
    	return myLastBidValue;
    }
    
    public void win() {
    	Package p = myLastBidPackage;
    	p.setColor(myColor);   	
    	App.map.removePackage(p);
    	myGoalPackages.add(lastIndex, p);
    	
    	sumPath = myLastSum;
    	minAve = myLastAve;
    	
    	App.refresh();
    	
    	myLastBidPackage = null;
    	myLastBidValue = 0;
    	lastIndex = -1;
    }
    
    public void getReady() {
    	if (myGoalPackages != null) {
    		// ezt lehet ford�tva kell bej�rni
    		for (int counter = myGoalPackages.size() - 1; counter >= 0; counter--) {
    			Package p = myGoalPackages.get(counter);
    			addDestination(new Position(p.GetDestX(), p.GetDestY()));
    	    	addDestination(new Position(p.GetX(), p.GetY()));
    		}
    	}
    }
    
    public int getSumPath() {
    	return sumPath;
    }
    
    public int getMinAve() {
    	return minAve;
    }
    
}