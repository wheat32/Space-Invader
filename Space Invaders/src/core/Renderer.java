package core;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import javax.swing.JFrame;
import javax.swing.Timer;

import bosses.BossAlien;
import enemies.MarchingAlien;
import miscEntities.Wall;
import players.SpaceShip;
import system.Options;
import utils.ConstantValues;
import utils.FrameListener;
import utils.GraphicsListener;
import utils.KeyInputManagement;
import utils.ObjectCollection;
import utils.Stats;

public class Renderer extends JFrame implements ComponentListener, ConstantValues, KeyListener, MouseListener
{
	private static final long serialVersionUID = 1L;
	
	private Timer timer;
	private long timeStamp = System.currentTimeMillis();
	private int elapsedTime = 0;
	private ArrayList<FrameListener> frameListeners = new ArrayList<FrameListener>();
	private ArrayList<GraphicsListener> graphicsListeners = new ArrayList<GraphicsListener>();
	
	private Canvas canvas;
	
	public Renderer()
	{
		super("Space Invader");
		super.setMinimumSize(new Dimension(640, 480));
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.setIgnoreRepaint(true);
		canvas = new Canvas();
		
		System.out.println(Options.SCREEN_WIDTH + "x" + Options.SCREEN_HEIGHT);
		//canvas.setSize(new Dimension(Options.SCREEN_WIDTH, Options.SCREEN_HEIGHT));
		canvas.setSize(new Dimension(980, 780));
		super.add(canvas);
		super.pack();
		canvas.createBufferStrategy(2);
		
		super.setVisible(true);
		super.addComponentListener(this);
		super.addKeyListener(this);
		super.addMouseListener(this);
	}
	
	public void start()
	{
		ObjectCollection.getAudio().returnFocus();
		timeStamp = System.currentTimeMillis();
		
		timer = new Timer(1000/30, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)//TODO can make optimizations from here
			{
				render();
				makeFrameCalls();
			}
		});
		timer.start();
	}
	
	public void stop()
	{
		ObjectCollection.getAudio().outOfFocus();
		timer.stop();
	}
	
	private void render()
	{
		//[START] ----- Required Code ----- \\
		BufferStrategy strategy = canvas.getBufferStrategy();
		Graphics2D gfx = (Graphics2D) strategy.getDrawGraphics();
		
		requestFocus();
		
		elapsedTime = (int) (System.currentTimeMillis() - timeStamp);
		
		gfx.setColor(Color.BLACK);
		gfx.fillRect(0, 0, Stats.SCREEN_WIDTH, Stats.SCREEN_HEIGHT);
		
		ObjectCollection.getBackground().displayBackground(gfx, elapsedTime);		
		//[END] ----- Required Code ----- \\
		
		for(int i = 0; i < ObjectCollection.getEntityManagement().getEntitiesSize(); i++) 
		{
			Entity e = ObjectCollection.getEntityManagement().getEntity(i);
			
			//Skip the Wall
			if(e instanceof Wall == true)
			{
				continue;
			}
			
			e.draw(gfx, elapsedTime);

			if(ObjectCollection.getGameManagement().getInGame() == true) 
			{
				//Specific type checks
				if(e instanceof SpaceShip == true)
				{
					((SpaceShip) e).processKeys();
				}
				
				if(e instanceof MarchingAlien == false)
				{
					e.move(elapsedTime);
				}
			}
			else
			{
				if(e instanceof BossAlien)
				{
					e.move(0);
				}
				else
				{
					e.deactivate();
					continue;
				}
			}
			
			//Collision detection
			for(int j = i+1; j < ObjectCollection.getEntityManagement().getEntitiesSize(); j++)
			{
				Entity e2 = ObjectCollection.getEntityManagement().getEntity(j);
				
				//If there is no collision check for whatever e2 is inside of e
				if(e.inCollision(e2) == false)
				{
					e2.inCollision(e);
					//System.out.println(e.toString() + " is in collision with " + e2.toString());
				}
			}
		}
		
		makeGraphicsCalls(gfx, elapsedTime);
		
		renderCleanUp(gfx, strategy);
	}
	
	private void renderCleanUp(Graphics2D gfx, BufferStrategy strategy)
	{
		gfx.dispose();
		strategy.show();
		timeStamp = System.currentTimeMillis();
	}
	
	public void addFrameListener(FrameListener listener)
	{
		frameListeners.add(listener);
	}
	
	public void removeFrameListener(FrameListener listener)
	{
		if(frameListeners.remove(listener) == false)
		{
			System.err.println("Error removing object " + listener.toString() + " as a frame listener.");
		}
	}
	
	public void makeFrameCalls()
	{
		try
		{
			for(FrameListener listener : frameListeners)
			{
				listener.frameCall(elapsedTime);
			}
		}
		catch(ConcurrentModificationException e)
		{
			//Do nothing. Really a trivial exception that occurs when adding an element to an ArrayList while it is being iterated through
		}
	}
	
	public void addGraphicsListener(GraphicsListener listener)
	{
		graphicsListeners.add(listener);
	}
	
	public void removeGraphicsListener(GraphicsListener listener)
	{
		if(graphicsListeners.remove(listener) == false)
		{
			System.err.println("Error removing object " + listener.toString() + " as a graphics listener.");
		}
	}
	
	public void makeGraphicsCalls(Graphics2D gfx, int elapsedTime)
	{
		for(GraphicsListener listener : graphicsListeners)
		{
			listener.graphicsCall(gfx, elapsedTime);
		}
	}
	
	@Override
	public void componentHidden(ComponentEvent e)
	{
		System.out.println("Component Hidden");
		timer.stop();
		ObjectCollection.getAudio().outOfFocus();
	}
	
	@Override
	public void componentShown(ComponentEvent e)
	{
		timer.start();	
		timeStamp = System.currentTimeMillis();
	}

	@Override
	public void componentMoved(ComponentEvent e)
	{
		//NOTHING
	}

	@Override
	public void componentResized(ComponentEvent e)
	{
		if(frameListeners.size() == 0)
		{
			return;//We know this method is being called before everything is initialized
		}
		
		int oldScreenWidth = Stats.SCREEN_WIDTH;
		int oldScreenHeight = Stats.SCREEN_HEIGHT;
		Stats.SCREEN_WIDTH = canvas.getWidth();
		Stats.SCREEN_HEIGHT = canvas.getHeight();
		
		for(int i = 0; i < ObjectCollection.getEntityManagement().getEntitiesSize(); i++)
		{
			ObjectCollection.getEntityManagement().getEntity(i).resize(oldScreenWidth, oldScreenHeight);
			ObjectCollection.getEntityManagement().getEntity(i).move(0);
		}
		
		ObjectCollection.getFontManagement().resetFontSizes();
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		KeyInputManagement.keyPressed(e);
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		KeyInputManagement.keyReleased(e);
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		KeyInputManagement.keyTyped(e);
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		System.out.println("Mouse clicked");
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		System.out.println("Mouse entered");
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		System.out.println("Mouse exited");
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		System.out.println("Mouse pressed");
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}
}