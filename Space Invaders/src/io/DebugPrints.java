package io;

import java.awt.Font;
import java.awt.Graphics2D;

import utils.GraphicsListener;
import utils.ObjectCollection;

public class DebugPrints implements GraphicsListener
{
	public boolean displayFPS = true;
	private short second = 0;
	private short currFPS = 0;
	private short fps = 0;
	
	public DebugPrints()
	{
		ObjectCollection.getRenderer().addGraphicsListener(this);
	}
	
	private void printGraphics(Graphics2D gfx, int elapsedTime)
	{
		if(displayFPS == true)
		{
			if(second/1000 >= 1)
			{
				second = 0;
				currFPS = fps;
				fps = 0;
				System.out.println("FPS: " + currFPS);
			}
			else
			{
				second += elapsedTime;
				fps++;
			}
		}
		
		
	}

	@Override
	public void graphicsCall(Graphics2D gfx, int elapsedTime)
	{
		Font oldFont = gfx.getFont();
		printGraphics(gfx, elapsedTime);
		gfx.setFont(oldFont);
	}
}
