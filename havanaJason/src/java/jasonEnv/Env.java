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
    private Logger logger = Logger.getLogger("havanaJason."+Env.class.getName());

    /** Called before the MAS execution with the args informed in .mas2j */
    @Override
    public void init(String[] args) {
        super.init(args);
        try {
			addPercept(ASSyntax.parseLiteral("percept(demo)"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        myApp = new App();
    }

    @Override
    public boolean executeAction(String agName, Structure action) {
        logger.info("executing: "+action+", but not implemented!");
        if (true) { // you may improve this condition
             informAgsEnvironmentChanged();
        }
        return true; // the action was executed with success
    }

    /** Called before the end of MAS execution */
    @Override
    public void stop() {
        super.stop();
    }
}
