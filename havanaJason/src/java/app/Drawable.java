package app;

import java.awt.Graphics2D;

public interface Drawable {
	//ki tudják magukat rajzolni
    public void Draw(@SuppressWarnings("exports") Graphics2D g, int size);
}
