package world;


import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import app.App;
import app.Drawable;

public class Warehouse implements Drawable {
	//csempék
    Tile[][] tiles;
    //targoncák
    List<Robot> robots;
    //magasság 
    int h;
    //szélesség
    int w;
    // eltelt idõ
    int time;
    
    //ezek csak mert random csinál csomagokat magára
    // egy csomag telep méretei
    int xLen;
    int yLen;
    Random r = new Random();

    public Warehouse() {
    	time = 0;
    }
    
    public void Set(int H, int W, int BH, int BW, int N) {
        h = H;
        w = W;
        xLen = BH;
        yLen = BW;
        
        time = 0;
       
        Generate(N);
        SetGoals("goals.csv");
        
        App.refresh();
    }

    public void Draw(@SuppressWarnings("exports") Graphics2D g, int size) {
  
        for (int i=0; i<h+2; i+=1) 
        	for (int j=0; j<w+2; j+=1) 
            	tiles[i][j].Draw(g, size);

    }

    public int GetH() {
    	return h+2;
    }
    
    public int GetW() {
    	return w+2;
    }
    
    //pálya generálása n db robottal
    private void Generate(int n){
        tiles = new Tile[w+2][h+2];
        robots = new ArrayList<Robot>();

        //csempék létrehozása +paddinggal
        //tiles[x koordináta balról nõ][y koordináta lefelé nõ]
        for (int i = 0; i < h + 2; i++){
            for (int j = 0; j < w + 2; j++){
                Tile t = new Tile(j, i);
                tiles[j][i]=t;
            }
        }

        //szomszédok beállítása padding nélkül
        for (int i = 1; i < h + 1; i++)
            for (int j = 1; j < w + 1; j++)
                tiles[j][i].SetNeighbours(tiles[j][i - 1], tiles[j + 1][i], tiles[j][i + 1], tiles[j - 1][i]);

        //tárgyak elhelyezése:
        //falak a szélére
        for (int i = 0; i < h + 2; i++){
            tiles[0][i].Add(new Obstacle(tiles[0][i]));
            tiles[w + 1][i].Add(new Obstacle(tiles[w+1][i]));
        }
        for (int j = 1; j < w + 1; j++){
            tiles[j][0].Add(new Obstacle(tiles[j][0]));
            tiles[j][h + 1].Add(new Obstacle(tiles[j][h+1]));
        }
        //csomagok 80% os kitöltöttséggel
        for (int i = 0; i < (w - 1) / (yLen + 1); i++)
            for (int j = 0; j < (h - 1) / (xLen + 1); j++)
                if (r.nextInt(10) > 2)
                    for (int ii = 2; ii < 2 + yLen; ii++)
                        for (int jj = 2; jj < 2 + xLen; jj++)
                            tiles[j * (xLen + 1) + jj][i * (yLen + 1) + ii].Add(new Package(tiles[j * (xLen + 1) + jj][i * (yLen + 1) + ii]));

        //n db targonca létrehozása
        for (int i = 0; i < n; i++)
            robots.add(new Robot(tiles[i + 1][h], (float)(i+1)/n));
    }
    
    //targoncák céljainak beállítása
    //paraméter: .csv fájl minden sor egy honnan hova pár (x-y koordináták)
    private void SetGoals(String goals){
        // sor számláló
        int count = 0;
        String row;
        
        //csv fájl soronkénti olvasása
        try {
	        BufferedReader csvReader = new BufferedReader(new FileReader(goals));
	        row = csvReader.readLine();
	        while ((row = csvReader.readLine()) != null) {
	            String[] data = row.split(";");
	            
	            int fromX, fromY, toX, toY;
	
	            fromX = Integer.parseInt(data[0]);
	            fromY = Integer.parseInt(data[1]);
	            toX = Integer.parseInt(data[2]);
	            toY = Integer.parseInt(data[3]);
	
	            // cél beállítása, ha lehet
	            if (fromX > 0 && fromY > 0 && toX > 0 && toY > 0 && fromX < h + 1 && fromY < w + 1 && toX < h + 1 && toY < w + 1)
	            {
	                //elhelyezzük a mapen a célcsomagokat
	            	Package p =new Package(tiles[fromX][fromY]);
	            	p.setDest(new Position(toX,toY), robots.get(count % robots.size()).getColor());
	                tiles[fromX][fromY].Add(p);
	
	                //sorba a targoncáknak beállítjuk a célokat, verembe elõszõr a cél, utána a kiindulás, mert fordítva szedi ki
	                //itt is lehetne optimalizálni
	                robots.get(count % robots.size()).addDestination(new Position(toX, toY));
	                robots.get(count % robots.size()).addDestination(new Position(fromX, fromY));
	                
	                count++;
	            }
	                        
	        }
	        csvReader.close();
        }catch(Exception e) {
        	
        }
    }
    

    //minden targoncát léptet, ha már nem lépnek falseal tér vissza
    public boolean Step(){
        time += 1;
    	boolean ret = false;
        for (Robot r : robots){
            boolean tmp = r.step(tiles);
            if (tmp) ret = true;
        }
        
        App.refresh();
        
        return ret;
    }
    
    public int GetTime() {
    	return time;
    }
}
