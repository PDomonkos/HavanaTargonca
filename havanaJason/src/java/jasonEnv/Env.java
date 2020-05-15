// Environment code for project havanaJason
package jasonEnv;

import jason.asSyntax.*;
import jason.asSyntax.parser.ParseException;
import jason.environment.*;
import world.Warehouse;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.logging.*;

import AStar.AStar;
import app.App;
import app.MainFrame;

public class Env extends Environment {
  
	private App myApp;
	int     sleep    = 0;
    private Logger logger = Logger.getLogger("havanaJason."+Env.class.getName());

    Term                    bid      = Literal.parseLiteral("do(bid)");
    Term                    win      = Literal.parseLiteral("do(win)");
    //Term					query	 = Literal.parseLiteral("do(query)");
    //Term                    start    = Literal.parseLiteral("do(startEnv)");
    
    @Override
    public void init(String[] args) {
        super.init(args);
        myApp = new App(this);
        sleep = 50;
    }

    @Override
    public boolean executeAction(String agName, Structure action) {

         informAgsEnvironmentChanged();
         
         try {
             if (sleep > 0) {
                 Thread.sleep(sleep);
             }

             int agId = getAgIdBasedOnName(agName);

             if (action.equals(bid)) {
                 int bidValue = myApp.map.bid(agId);
            	 updateAgPercept(agId, bidValue);
            	 return true;
             } else if (action.equals(win)) {
                 myApp.map.win(agId);
            	 return true;
             } else if (action.equals(query)) {
                 int n = myApp.map.getPNum();
                 updateAuctPercept(n == 0);
            	 return true;
             } else if (action.equals(start)) {
                 myApp.enableStart();
            	 myApp.map.auctionStart();
            	 return true;
             }  else {
                 logger.info("executing: " + action + ", but not implemented!");
             }
         } catch (InterruptedException e) {
         } catch (Exception e) {
             logger.log(Level.SEVERE, "error executing " + action + " for " + agName, e);
         }
         return false;
     }

    @Override
    public void stop() {
        super.stop();
    }
    
    private int getAgIdBasedOnName(String agName) {
        return (Integer.parseInt(agName.substring(5))) - 1;
    }
    
    private void updateAgPercept(int agId, int value) {
    	String agName = "forklift" + (agId + 1);
        clearPercepts(agName);
        addPercept(agName, Literal.parseLiteral("bid(" + value +")"));
    }
    
    public void updateAuctPercept(boolean end) {
    	if (end)
    		addPercept("auctioner", Literal.parseLiteral("end"));
    }
    
    public void startAuction(int agentCount) {
		agentCount=10;
		addPercept("auctioner", Literal.parseLiteral("restart"));
		addPercept("auctioner", Literal.parseLiteral("agentCount("+String.valueOf(agentCount)+")"));
		addPercept("auctioner", Literal.parseLiteral("createAgents"));
    }
}
