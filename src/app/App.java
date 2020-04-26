package app;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import javax.imageio.ImageIO;

import world.Warehouse;

public class App {
    // The GUI frame
    static MainFrame mainFrame = null;
    // The map the simulator uses
    static Warehouse map = null;
    // Image cache for loading every image only once
    private static HashMap<String,BufferedImage> imageCache = new HashMap<>();
    static boolean finished = false;
    
    /**
     * Main method of the application
     * @param args      The command line arguments
     */
    public static void main(String[] args) {
        Settings.load();
        map = new Warehouse();
        mainFrame = new MainFrame();
        mainFrame.setVisible(true);
        
    }
    
    /**
     * Refresh the GUI
     */
    public static void refresh() {
        if (mainFrame != null)  mainFrame.refresh();
    }
    
    /**
     * Pause the autstep thread
     */
    public static void pause() {
        if (mainFrame != null)  mainFrame.pause();
    }
    
    /**
     * Resume the autstep thread
     */
    public static void resume() {
        if (mainFrame != null)  mainFrame.resume();
    }
    
    /**
     * Log a message with timestamp to the console
     * @param message       The message to log to the console
     */
    public static void log(String message) {
        String timeStamp = new SimpleDateFormat("HH:mm:ss.SSS").format(Calendar.getInstance().getTime());
        System.out.println("["+timeStamp+"] "+message);
    }
    
    /**
     * Get cached image identified by the string definition
     * @param image         String image definition
     * @return              Cached image
     */
    @SuppressWarnings("exports")
	public static BufferedImage getCachedImage(String image) {
        if (!imageCache.containsKey(image)) {
            // Image not yet cached
            try {
                BufferedImage img = ImageIO.read(new File("images/"+image+".png"));
                imageCache.put(image, img);
                return img;
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }  
        } else {
            // Image found in the cache
            return imageCache.get(image);
        }
    }
}
