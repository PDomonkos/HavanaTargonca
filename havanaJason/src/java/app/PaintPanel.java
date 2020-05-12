package app;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class PaintPanel extends JPanel{  
	private static final long serialVersionUID = 1L;
	private int cellSize = 0;
    //private Warehouse map;
    public static boolean paintingInProgress = false;
    
    public PaintPanel() {
        super();
        setDoubleBuffered(true);
        setBackground(new Color(219, 219, 219));

    }
    
    
    @Override
    public void paintComponent(@SuppressWarnings("exports") Graphics g) {
    	super.paintComponent(g);
        
        if (App.map == null) {
            return;
        }
    	
        int cellWidth = (int)Math.floor(getWidth()/App.map.GetW());
        int cellHeight = (int)Math.floor(getHeight()/App.map.GetH());
        cellSize = Math.min(cellWidth, cellHeight);
        
        // Convert Graphics to Graphics2D
        Graphics2D g2 = (Graphics2D) g;
                
        App.map.Draw(g2,cellSize);
        
        paintingInProgress = false;
    }
    

}
