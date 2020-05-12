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
	//csemp�k
    Tile[][] tiles;
    //targonc�k
    List<Robot> robots;
    //magass�g 
    int h;
    //sz�less�g
    int w;
    // eltelt id�
    int time;
    
    //ezek csak mert random csin�l csomagokat mag�ra
    // egy csomag telep m�retei
    int xLen;
    int yLen;
    Random r = new Random(42);

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
  
        for (int i=0; i<w+2; i+=1) 
        	for (int j=0; j<h+2; j+=1) 
            	tiles[i][j].Draw(g, size);

    }

    public int GetH() {
    	return h+2;
    }
    
    public int GetW() {
    	return w+2;
    }
    
    //p�lya gener�l�sa n db robottal
    private void Generate(int n){
        tiles = new Tile[w+2][h+2];
        robots = new ArrayList<Robot>();

        //csemp�k l�trehoz�sa +paddinggal
        //tiles[x koordin�ta balr�l n�][y koordin�ta lefel� n�]
        for (int i = 0; i < h + 2; i++){
            for (int j = 0; j < w + 2; j++){
                Tile t = new Tile(j, i);
                tiles[j][i]=t;
            }
        }

        //szomsz�dok be�ll�t�sa padding n�lk�l
        for (int i = 1; i < h + 1; i++)
            for (int j = 1; j < w + 1; j++)
                tiles[j][i].SetNeighbours(tiles[j][i - 1], tiles[j + 1][i], tiles[j][i + 1], tiles[j - 1][i]);

        //t�rgyak elhelyez�se:
        //falak a sz�l�re �s k�z�pre
        for (int i = 0; i < h + 2; i++){
            tiles[0][i].Add(new Obstacle(tiles[0][i]));
            tiles[w + 1][i].Add(new Obstacle(tiles[w+1][i]));
        }
        for (int j = 1; j < w + 1; j++){
            tiles[j][0].Add(new Obstacle(tiles[j][0]));
            tiles[j][h + 1].Add(new Obstacle(tiles[j][h+1]));
        }
        for (int i = 0; i < h + 1; i++){
            if (Math.abs(i-1-(int)h/2) > 1)
            	tiles[(int)w/2+1][i].Add(new Obstacle(tiles[(int)w/2+1][i]));
        }

        //csomagok 90% os kit�lt�tts�ggel
        for (int i = 0; i < (w - 1) / (yLen + 1); i++)
            for (int j = 0; j < (h - 1) / (xLen + 1); j++)
                if (r.nextInt(10) > 1)
                    for (int ii = 2; ii < 2 + yLen; ii++) {
                		int shift = 0;
                		if (i >= (w - 1)/2 / (yLen + 1))
                			shift = 2;
                		for (int jj = 2; jj < 2 + xLen; jj++)
                			tiles[i * (yLen + 1) + ii + shift][j * (xLen + 1) + jj].Add(new Package(tiles[i * (yLen + 1) + ii + shift][j * (xLen + 1) + jj]));
                    	}
        
        //n db targonca l�trehoz�sa
        for (int i = 0; i < Math.min((w - 1)/2,n); i++)
            robots.add(new Robot(tiles[i + 1][h], (float)(i+1)/n));
    }
    
    //targonc�k c�ljainak be�ll�t�sa
    //param�ter: .csv f�jl minden sor egy honnan hova p�r (x-y koordin�t�k)
    private void SetGoals(String goals){
        // sor sz�ml�l�
        int count = 0;
        String row;
        
        //csv f�jl soronk�nti olvas�sa
        try {
	        BufferedReader csvReader = new BufferedReader(new FileReader(goals));
	        row = csvReader.readLine();
	        while ((row = csvReader.readLine()) != null) {
	            String[] data = row.split(";");
	            
	            int fromXBlock, fromYBlock, fromXinBlock, fromYinBlock, toXBlock, toYBlock, toXinBlock, toYinBlock;
	
	            fromXBlock = Integer.parseInt(data[0]);
	            fromYBlock = Integer.parseInt(data[1]);
	            fromXinBlock = Integer.parseInt(data[2]);
	            fromYinBlock = Integer.parseInt(data[3]);
	            toXBlock = Integer.parseInt(data[4]);
	            toYBlock = Integer.parseInt(data[5]);
	            toXinBlock = Integer.parseInt(data[6]);
	            toYinBlock = Integer.parseInt(data[7]);
	            
	            int fromX, fromY, toX, toY;
	            fromX = fromXBlock * (yLen+1) + fromXinBlock+2;
	            fromY = fromYBlock * (xLen+1) + fromYinBlock+2;
	            toX = toXBlock * (yLen+1) + toXinBlock+2;
	            toY = toYBlock * (xLen+1) + toYinBlock+2;
	            if (fromX > (w - 1)/2)
	            	fromX += 2;
	            if (toX > (w - 1)/2)
	            	toX += 2;
	
	            // c�l be�ll�t�sa, ha lehet
	            if (fromX > 0 && fromY > 0 && toX > 0 && toY > 0 && fromX < w + 1 && fromY < h + 1 && toX < w + 1 && toY < h + 1)
	            {
	                //elhelyezz�k a mapen a c�lcsomagokat
	            	Package p =new Package(tiles[fromX][fromY]);
	            	p.setDest(new Position(toX,toY), robots.get(count % robots.size()).getColor());
	                tiles[fromX][fromY].Add(p);
	
	                //sorba a targonc�knak be�ll�tjuk a c�lokat, verembe el�sz�r a c�l, ut�na a kiindul�s, mert ford�tva szedi ki
	                //itt is lehetne optimaliz�lni
	                robots.get(count % robots.size()).addDestination(new Position(toX, toY));
	                robots.get(count % robots.size()).addDestination(new Position(fromX, fromY));
	                
	                count++;
	            }
	                        
	        }
	        csvReader.close();
        }catch(Exception e) {
        	
        }
    }   

    //minden targonc�t l�ptet, ha m�r nem l�pnek falseal t�r vissza
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