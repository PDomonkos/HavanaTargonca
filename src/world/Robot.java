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
	//c�lok koordin�t�i
    Stack<Position> dest;
    //�tvonal
    List<Direction> path = new ArrayList<Direction>();
    //sz�ll�tott csomag
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

    //�tk�z�s heurisztik�ja, v�gtelen (de id�belis�get nem figyel)
    public int Cost(){
        return 999;
    }

    public void addDestination(Position p){
        dest.push(p);
    }

    //nem oo hogy �tveszi, de ha l�tn� az egsz p�ly�t se lenne az, nem itt a helye
    //megadott p�ly�n a megadott c�lhoz megy
    public void goTo(Tile[][] tiles, Position destination){

        boolean finish = false;

        //init + kezd� pont hozz�ad�sa
        List<Node> open = new ArrayList<Node>();
        List<Node> close = new ArrayList<Node>();
        Node n0 = new Node(myTile.GetX(), myTile.GetY());
        n0.G = 0;
        n0.H = Math.abs(n0.GetX() - destination.GetX()) + Math.abs(n0.GetY() - destination.GetY());
        n0.F = n0.H + n0.G;
        open.add(n0);

        //�tkeres�s
        while (open.size() != 0 && !finish)
        {
            //legkisebb elem kiv�tele
            Node q = Collections.min(open);
            open.remove(q);

            // gyerekeinek legener�l�sa, ha van
            List<Node> childs = new ArrayList<Node>();
            if (tiles[q.GetX()][q.GetY()].GetNeighbour(Direction.up)!=null)
                childs.add(new Node(q, Direction.up));
            if (tiles[q.GetX()][q.GetY()].GetNeighbour(Direction.down) != null)
                childs.add(new Node(q, Direction.down));
            if (tiles[q.GetX()][q.GetY()].GetNeighbour(Direction.right) != null)
                childs.add(new Node(q, Direction.right));
            if (tiles[q.GetX()][q.GetY()].GetNeighbour(Direction.left) != null)
                childs.add(new Node(q, Direction.left));

            //tov�bb l�p�s
            for (Node c : childs){
                boolean throwOut = false;

                // ha megtal�ltuk visszat�r�nk az �tvonallal
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

                // k�l�nben �rt�kek sz�mol�sa
                c.G = q.G + 1 + tiles[c.GetX()][c.GetY()].Cost();
                c.H = Math.abs(c.GetX() - destination.GetX()) + Math.abs(c.GetY() - destination.GetY());
                c.F = c.H + c.G;

                // ha benne van m�r kisebb �rt�kkel az openba kidobjuk
                for (Node n : open){
                    if (c.GetX() == n.GetX() && c.GetY() == n.GetY() && n.F <= c.F)
                        throwOut = true;
                }
                // ha benne van m�r kisebb �rt�kkel a closeba kidobjuk
                for (Node n : close){
                    if (c.GetX() == n.GetX() && c.GetY() == n.GetY() && n.F <= c.F)
                        throwOut = true;
                }
                // ha meg kell tartani akkor az openba tessz�k
                if (!throwOut) { open.add(c);}
            }

            //q closeba rak�sa
            close.add(q);
        }
    }

    //nem v�r ha �tk�z�s lenne, hanem m�smerre megy 
    //Egy l�p�st tesz a soron k�vetkez� c�lja fel�
    public boolean step(Tile[][] tiles){
        //ha nincs m�r c�lja akkor nem csin�l semmit, false-al t�r vissza
        if (dest.size() == 0) return false;

        //c�l koorin�t�k
        Position destination = dest.peek();

        //�tvonaltervez�s
        goTo(tiles, destination);

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
}