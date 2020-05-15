package AStar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import app.App;
import world.Direction;
import world.Position;
import world.Tile;

public class AStar {
	
	public List<Direction> goTo(Tile[][] tiles, Position start, Position destination){
		List<Direction> path = new ArrayList<Direction>();
        boolean finish = false;

        //init + kezdõ pont hozzáadása
        List<Node> open = new ArrayList<Node>();
        List<Node> close = new ArrayList<Node>();
        Node n0 = new Node(start.GetX(), start.GetY());
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
                    while (!(n.GetX() == start.GetX() && n.GetY() == start.GetY()))
                    {
                        path.add(n.GetDirection());
                        n = n.GetParent();
                    }
                    Collections.reverse(path);
                    finish = true;
                }

                // különben értékek számolása
                c.G = q.G + 1 + tiles[c.GetX()][c.GetY()].Cost(false);
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
        
        return path;
    }
	
//////////////////////////////////////////////////////////////////////////////////////////////
	public int getDist(Position start, Position destination){
		Tile[][] tiles = App.map.getTiles();
		int ret = 0;
		boolean finish = false;

        //init + kezdõ pont hozzáadása
        List<Node> open = new ArrayList<Node>();
        List<Node> close = new ArrayList<Node>();
        Node n0 = new Node(start.GetX(), start.GetY());
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
                    while (!(n.GetX() == start.GetX() && n.GetY() == start.GetY()))
                    {
                    	ret += 1;
                        n = n.GetParent();
                    }
  
                    finish = true;
                }

                // különben értékek számolása
                c.G = q.G + 1 + tiles[c.GetX()][c.GetY()].Cost(true);
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
        
        return ret;
    }
	
}
