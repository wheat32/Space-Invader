package core;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import system.Options;

public class Renderer extends JFrame
{
	private static final long serialVersionUID = 1L;

	private MainLoop mainLoop;
	
	private Canvas canvas;
	
	public Renderer(MainLoop mainLoop)
	{
		super("Space Invader");
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		canvas = new Canvas();
		super.setMinimumSize(new Dimension(640, 480));
		canvas.setPreferredSize(new Dimension(Options.SCREEN_WIDTH, Options.SCREEN_HEIGHT));
		super.add(canvas);
		super.setIgnoreRepaint(true);
		super.pack();
		canvas.createBufferStrategy(2);
		super.setVisible(true);
		
		this.mainLoop = mainLoop;
	}
	
	public void render()
	{
		//[START] ----- Required Code ----- \\
		BufferStrategy strategy = canvas.getBufferStrategy();
		Graphics2D gfx = (Graphics2D) strategy.getDrawGraphics();
		
		gfx.clearRect(0, 0, Options.SCREEN_WIDTH, Options.SCREEN_HEIGHT);
		//[END] ----- Required Code ----- \\
		
		for(int i = 0; i < mainLoop.getGraphicsListeners().size(); i++)
		{
			mainLoop.getGraphicsListeners().get(i).getValue().graphicsCall(gfx);
		}
		
		gfx.dispose();
		strategy.show();
	}
	
	public Canvas getCanvas()
	{
		return canvas;
	}
}
