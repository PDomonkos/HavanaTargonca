package world;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import app.App;
import app.Drawable;

public class Warehouse implements Drawable {
	//csempï¿½k
    Tile[][] tiles;
    //targoncï¿½k
    List<Robot> robots;
    //mï¿½g el nem vitt csomagok
    List<Package> packagePool;
    int packageNum = 0;
    //magassï¿½g 
    int h;
    //szï¿½lessï¿½g
    int w;
    // eltelt idï¿½
    int time;
    
    //ezek csak mert random csinï¿½l csomagokat magï¿½ra
    // egy csomag telep mï¿½retei
    int xLen;
    int yLen;
    Random r = new Random(42);

    public Warehouse() {
    	time = 0;
    }
    
    public int Set(int H, int W, int BH, int BW, int N) {
        h = H;
        w = W;
        xLen = BH;
        yLen = BW;
        
        time = 0;
       
        int ret = Generate(N);
        SetGoals("goals.csv");
        
        App.refresh();
        
        return ret;
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
    
    //pï¿½lya generï¿½lï¿½sa n db robottal
    private int Generate(int n){
        tiles = new Tile[w+2][h+2];
        robots = new ArrayList<Robot>();
        packagePool = new ArrayList<Package>();

        //csempï¿½k lï¿½trehozï¿½sa +paddinggal
        //tiles[x koordinï¿½ta balrï¿½l nï¿½][y koordinï¿½ta lefelï¿½ nï¿½]
        for (int i = 0; i < h + 2; i++){
            for (int j = 0; j < w + 2; j++){
                Tile t = new Tile(j, i);
                tiles[j][i]=t;
            }
        }

        //szomszï¿½dok beï¿½llï¿½tï¿½sa padding nï¿½lkï¿½l
        for (int i = 1; i < h + 1; i++)
            for (int j = 1; j < w + 1; j++)
                tiles[j][i].SetNeighbours(tiles[j][i - 1], tiles[j + 1][i], tiles[j][i + 1], tiles[j - 1][i]);

        //tï¿½rgyak elhelyezï¿½se:
        //falak a szï¿½lï¿½re ï¿½s kï¿½zï¿½pre
        for (int i = 0; i < h + 2; i++){
        	new Obstacle(tiles[0][i]);
            new Obstacle(tiles[w+1][i]);
        }
        for (int j = 1; j < w + 1; j++){
        	new Obstacle(tiles[j][0]);
            new Obstacle(tiles[j][h+1]);
        }
        for (int i = 0; i < h + 1; i++){
            if (Math.abs(i-1-(int)h/2) > 1)
            	new Obstacle(tiles[(int)w/2+1][i]);
        }

        r.setSeed(40);
        
        //csomagok 90% os kitï¿½ltï¿½ttsï¿½ggel
        for (int i = 0; i < (w - 1) / (yLen + 1); i++)
            for (int j = 0; j < (h - 1) / (xLen + 1); j++)
                if (r.nextInt(10) > 1)
                    for (int ii = 2; ii < 2 + yLen; ii++) {
                		int shift = 0;
                		if (i >= (w - 1)/2 / (yLen + 1))
                			shift = 2;
                		for (int jj = 2; jj < 2 + xLen; jj++)
                			new Package(tiles[i * (yLen + 1) + ii + shift][j * (xLen + 1) + jj]);
                    }
        
        //n db targonca lï¿½trehozï¿½sa
        boolean flip = false;
        for (int i = 0; i < Math.min((w - 1)/2,n); i++) {
        	int xpos = i + 1;
        	if (flip)
        		xpos = w-xpos+1;
        	robots.add(new Robot(tiles[xpos][h], (float)(i+1)/n));
        	flip = !flip;
        }
        
        return Math.min((w - 1)/2,n);
    }
    
    //targoncï¿½k cï¿½ljainak beï¿½llï¿½tï¿½sa
    //paramï¿½ter: .csv fï¿½jl minden sor egy honnan hova pï¿½r (x-y koordinï¿½tï¿½k)
    private void SetGoals(String goals){
        // sor szï¿½mlï¿½lï¿½
        int count = 0;
        String row;
        
        Random r = new Random();
        r.setSeed(50);
        List<Position> possiblePos = new ArrayList<Position>();
        for (int XBlock = 0; XBlock < 8; XBlock+=1) 
        	for (int YBlock = 0; YBlock < 8; YBlock+=1) 
        		for (int XinBlock = 0; XinBlock < 5; XinBlock+=1) 
        			for (int YinBlock = 0; YinBlock < 2; YinBlock+=1) 
        				if (r.nextInt(100) > 80)
        					possiblePos.add(new Position(YBlock * (yLen+1) + YinBlock+2,XBlock * (xLen+1) + XinBlock+2));
        Collections.shuffle(possiblePos, new Random(70)); 
        
        System.err.println("asd "+possiblePos.size());
    	System.err.flush();
        
        boolean picked = false;
        for (int i = 0; i < possiblePos.size()-1; i+=1) {
        	if (!picked) {
        		int fromX = possiblePos.get(i).GetX();
        		int fromY = possiblePos.get(i).GetY();
        		int toX = possiblePos.get(i+1).GetX();
        		int toY = possiblePos.get(i+1).GetY();

        		System.err.println(" fx "+fromX+" fy "+fromY+" tx "+toX+" ty "+toY);
            	System.err.flush();
        		
        		if (fromX > (w - 1)/2)
	            	fromX += 2;
	            if (toX > (w - 1)/2)
	            	toX += 2;
        		
        		if (fromX > 0 && fromY > 0 && toX > 0 && toY > 0 && fromX < w + 1 && fromY < h + 1 && toX < w + 1 && toY < h + 1) {
	            	packagePool.add(new Package(tiles[fromX][fromY],new Position(toX,toY)));
	            	picked = true;
	            }
	
        	} else {picked = false;}
        }
        packageNum = packagePool.size();
        
        //beolvasásnál
        
        //csv fï¿½jl soronkï¿½nti olvasï¿½sa
        /*try {
	        BufferedReader csvReader = new BufferedReader(new FileReader(goals));
	        row = csvReader.readLine();
	        while ((row = csvReader.readLine()) != null) {
	            String[] data = row.split(";");
	            
	            int fromXBlock, fromYBlock, fromXinBlock, fromYinBlock, toXBlock, toYBlock, toXinBlock, toYinBlock;
	
	            fromYBlock = Integer.parseInt(data[0]);
	            fromXBlock = Integer.parseInt(data[1]);
	            fromXinBlock = Integer.parseInt(data[2]);
	            fromYinBlock = Integer.parseInt(data[3]);
	            toYBlock = Integer.parseInt(data[4]);
	            toXBlock = Integer.parseInt(data[5]);
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
	
	            // cï¿½llal rendelkezï¿½ csomagok
	            if (fromX > 0 && fromY > 0 && toX > 0 && toY > 0 && fromX < w + 1 && fromY < h + 1 && toX < w + 1 && toY < h + 1) {
	            	packagePool.add(new Package(tiles[fromX][fromY],new Position(toX,toY)));
	            }
          
	        }
	        csvReader.close();
	        packageNum = packagePool.size();
        	}catch(Exception e) {}*/
    }   

    //minden targoncï¿½t lï¿½ptet, ha mï¿½r nem lï¿½pnek falseal tï¿½r vissza
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
    
///////////////////////////////////////////////////////
    
    public int bid(int id) {
    	int value = robots.get(id).bid();
    	return value;
    }
    
    public void win(int id) {
    	robots.get(id).win();
    }
    
    public void auctionStart() {
    	for (Robot r : robots){
    		r.getReady();
    	}
    }
    
    public void removePackage(Package p) {
    	packagePool.remove(p);
    	if (packagePool.size()==0)
    		App.env.updateAuctPercept(true);
    }
    
    public int getPNum() {
    	return packagePool.size();
    }
    
    public boolean isPInPackages(Package p) {
    	return packagePool.contains(p);
    }
    
    public List<Package> getPackages(){
    	return packagePool;
    }
    
    public Tile[][] getTiles(){
    	return tiles;
    }
    
    public int GetMax() {
    	int max = 0;
    	for (Robot r : robots) 
    		if (max < r.getSumPath())
    			max = r.getSumPath();
    	return max;
    	// time-al egyenlõ??
    }
    
    public int GetSum() {
    	int sum = 0;
    	for (Robot r : robots) 
    		sum += r.getSumPath();
    	return sum;
    }
    
    public int GetAve() {
    	if (packageNum == 0)
    		return 0;
    	int ave = 0;
    	int cnt = 0;
    	for (Robot r : robots) {
    		ave += r.getMinAve();
    		cnt += 1;
    	}
    	return (int)ave/packageNum;
    }
    
}
