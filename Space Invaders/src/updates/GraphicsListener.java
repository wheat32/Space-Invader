package updates;

import java.awt.Graphics2D;

public interface GraphicsListener
{
	/**
	 * <b>This method gets called every frame and passes the graphics context.</b>
	 * <p>
	 * This should only be used for graphic elements that must be updated or drawn every frame.
	 * 
	 * @param gfx - The Graphic2D element used during runtime
	 */
	void graphicsCall(Graphics2D gfx);
	
	/***
	 * <b>This method gets called every frame the window resizes or resolution changes.</b>
	 * @param oldWidth - The resolution width before the change
	 * @param oldHeight - The resolution height before the change
	 */
	void resize(int oldWidth, int oldHeight);
}
