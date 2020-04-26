package world;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import AStar.Node;
import app.App;

public class Robot extends Thing{
	//célok koordinátái
    Stack<Position> dest;
    //útvonal
    List<Direction> path = new ArrayList<Direction>();
    //szállított csomag
    private Package myPackage;


    public Robot(Tile t){
    	super(t);
        myPackage = null;
        t.Add(this);
        t.Inc();
        dest = new Stack<Position>();
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
        /*g.setColor(Color.blue);
        if (myPackage != null)
        	g.setColor(Color.cyan);
    	g.setStroke(new BasicStroke(0));
    	g.fillRect(GetX()*size+1, GetY()*size+1, size-1, size-1);
    	*/
    	
    	String cellImage = "robot";
    	
    	if (!cellImage.isEmpty()) {
            g.drawImage(App.getCachedImage(cellImage), GetX()*size, GetY()*size, size, size, null);
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

    //nem oo hogy átveszi, de ha látná az egsz pályát se lenne az, nem itt a helye
    //megadott pályán a megadott célhoz megy
    public void goTo(Tile[][] tiles, Position destination){

        boolean finish = false;

        //init + kezdõ pont hozzáadása
        List<Node> open = new ArrayList<Node>();
        List<Node> close = new ArrayList<Node>();
        Node n0 = new Node(myTile.GetX(), myTile.GetY());
        n0.G = 0;
        n0.H = Math.abs(n0.GetX() - destination.GetX()) + Math.abs(n0.GetY() - destination.GetY());
        n0.F = n0.H + n0.G;
        open.add(n0);

        //útkeresés
        while (open.size() != 0 && !finish)
        {
            //legkisebb elem kivétele
            Node q = Collections.min(open);
            open.remove(q);

            // gyerekeinek legenerálása, ha van
            List<Node> childs = new ArrayList<Node>();
            if (tiles[q.GetX()][q.GetY()].GetNeighbour(Direction.up)!=null)
                childs.add(new Node(q, Direction.up));
            if (tiles[q.GetX()][q.GetY()].GetNeighbour(Direction.down) != null)
                childs.add(new Node(q, Direction.down));
            if (tiles[q.GetX()][q.GetY()].GetNeighbour(Direction.right) != null)
                childs.add(new Node(q, Direction.right));
            if (tiles[q.GetX()][q.GetY()].GetNeighbour(Direction.left) != null)
                childs.add(new Node(q, Direction.left));

            //tovább lépés
            for (Node c : childs){
                boolean throwOut = false;

                // ha megtaláltuk visszatérünk az útvonallal
                if (c.GetX() == destination.GetX() && c.GetY() == destination.GetY())
                {
                    Node n = c;
                    while (!(n.GetX() == GetX() && n.GetY() == GetY()))
                    {
                        path.add(n.GetDirection());
                        n = n.GetParent();
                    }
                    Collections.reverse(path);
                    finish = true;
                }

                // különben értékek számolása
                c.G = q.G + 1 + tiles[c.GetX()][c.GetY()].Cost();
                c.H = Math.abs(c.GetX() - destination.GetX()) + Math.abs(c.GetY() - destination.GetY());
                c.F = c.H + c.G;

                // ha benne van már kisebb értékkel az openba kidobjuk
                for (Node n : open){
                    if (c.GetX() == n.GetX() && c.GetY() == n.GetY() && n.F <= c.F)
                        throwOut = true;
                }
                // ha benne van már kisebb értékkel a closeba kidobjuk
                for (Node n : close){
                    if (c.GetX() == n.GetX() && c.GetY() == n.GetY() && n.F <= c.F)
                        throwOut = true;
                }
                // ha meg kell tartani akkor az openba tesszük
                if (!throwOut) { open.add(c);}
            }

            //q closeba rakása
            close.add(q);
        }
    }

    //nem vár ha ütközés lenne, hanem másmerre megy 
    //Egy lépést tesz a soron következõ célja felé
    public boolean step(Tile[][] tiles){
        //ha nincs már célja akkor nem csinál semmit, false-al tér vissza
        if (dest.size() == 0) return false;

        //cél koorináták
        Position destination = dest.peek();

        //útvonaltervezés
        goTo(tiles, destination);

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
}