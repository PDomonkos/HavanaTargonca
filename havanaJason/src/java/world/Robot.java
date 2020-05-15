package world;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
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

    //�tk�z�s heurisztik�ja, v�gtelen (de id�belis�get nem figyel)
    public int Cost(){
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
    	//adott robot megn�zi hogy a szabad csomagok k�z�l melyiket sz�rja be a saj�tj�ba, elmenti azt mint lehet�s�get (�s a bid �rt�k�t is)
    	//ha legk�zelebb nem vitt�k el az elmentettet akkor megint azzal bidel
    	return 1;
    }
    
    public void win() {
    	//adott robot megkapja a legutolj�ra szavazott csomagj�t
    	//azt kivessz�k a csomagok k�z�l �s sz�nezz�k
    }
    
    public void getReady() {
    	//c�lcsomagokat berakni a dest t�mbbe
    }
    
}