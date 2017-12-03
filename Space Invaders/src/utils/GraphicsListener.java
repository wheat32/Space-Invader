package utils;

import java.awt.Graphics2D;

public interface GraphicsListener
{
	/**
	 * <b>This method gets called every frame and passes the graphics context and elapsed time since last call.</b>
	 * <p>
	 * This should only be used for graphic elements that must be updated or drawn every frame.
	 * 
	 * @param gfx - The Graphic2D element used during runtime
	 * @param elapsedTime - The time elapsed since the last call
	 */
	void graphicsCall(Graphics2D gfx, int elapsedTime);
}
