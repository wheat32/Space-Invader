package core;

import java.awt.Canvas;
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
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import javax.swing.JFrame;
import javax.swing.Timer;

import input.KeyInputManagement;
import io.FontManagement;
import system.Audio;
import system.Options;
import system.Time;
import updates.EarlyUpdateListener;
import updates.GraphicsListener;
import updates.LateUpdateListener;
import updates.UpdateListener;
import utils.ConstantValues.RenderLayer;

public class Renderer extends JFrame implements ComponentListener, KeyListener, MouseListener
{
	private static final long serialVersionUID = 1L;
	
	private Timer timer;
	
	private ArrayList<EarlyUpdateListener> earlyUpdateListeners = new ArrayList<EarlyUpdateListener>();
	private ArrayList<UpdateListener> updateListeners = new ArrayList<UpdateListener>();
	/***
	 * <i>The first value in the Entry is the render layer.</i>
	 */
	private ArrayList<AbstractMap.SimpleEntry<Byte, GraphicsListener>> graphicsListeners = new ArrayList<>();
	private ArrayList<LateUpdateListener> lateUpdateListeners = new ArrayList<LateUpdateListener>();
	
	private Canvas canvas;
	
	public Renderer()
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
		super.addComponentListener(this);
		super.addKeyListener(this);
		super.addMouseListener(this);
	}
	
	public void start()
	{
		Audio.returnFocus();
		
		timer = new Timer(1000/30, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				makeEarlyUpdateCalls();
				makeUpdateCalls();
				render();
				makeLateUpdateCalls();
			}
		});
		timer.start();
	}
	
	public void stop()
	{
		Audio.outOfFocus();
		timer.stop();
	}
	
	private void render()
	{
		//[START] ----- Required Code ----- \\
		BufferStrategy strategy = canvas.getBufferStrategy();
		Graphics2D gfx = (Graphics2D) strategy.getDrawGraphics();
		
		gfx.clearRect(0, 0, Options.SCREEN_WIDTH, Options.SCREEN_HEIGHT);
		//[END] ----- Required Code ----- \\
		
		for(int i = 0; i < graphicsListeners.size(); i++)
		{
			graphicsListeners.get(i).getValue().graphicsCall(gfx);
		}
		
		gfx.dispose();
		strategy.show();
	}
	
	public void addEarlyUpdateListener(EarlyUpdateListener listener)
	{
		if(earlyUpdateListeners.contains(listener) == false)
		{
			earlyUpdateListeners.add(listener);
		}
	}
	
	public void removeEarlyUpdateListener(EarlyUpdateListener listener)
	{
		if(earlyUpdateListeners.remove(listener) == false)
		{
			System.err.println("Error removing object " + listener.toString() + " as an early update listener.");
		}
	}
	
	private void makeEarlyUpdateCalls()
	{
		try
		{
			for(EarlyUpdateListener listener : earlyUpdateListeners)
			{
				listener.earlyUpdate();
			}
		}
		catch(ConcurrentModificationException e)
		{
			//Do nothing. Really a trivial exception that occurs when adding an element to an ArrayList while it is being iterated through
		}
	}
	
	public void addUpdateListener(UpdateListener listener)
	{
		if(updateListeners.contains(listener) == false)
		{
			updateListeners.add(listener);
		}
	}
	
	public void removeUpdateListener(UpdateListener listener)
	{
		if(updateListeners.remove(listener) == false)
		{
			System.err.println("Error removing object " + listener.toString() + " as an update listener.");
		}
	}
	
	private void makeUpdateCalls()
	{
		try
		{
			for(UpdateListener listener : updateListeners)
			{
				listener.update();
			}
		}
		catch(ConcurrentModificationException e)
		{
			//Do nothing. Really a trivial exception that occurs when adding an element to an ArrayList while it is being iterated through
		}
	}
	
	public void addGraphicsListener(GraphicsListener listener, RenderLayer layer)
	{
		addGraphicsListener(listener, (byte) layer.layer);
	}
	
	/***
	 * 
	 * @param listener
	 * @param layer (byte) - the layer to be rendered at
	 */
	public void addGraphicsListener(GraphicsListener listener, byte layer)
	{
		//-1 means it doesn't exist yet
		if(graphicsListeners.size() == 0 || graphicsListenerIndex(listener) == -1)
		{
			for(int i = -1; i < graphicsListeners.size(); i++)
			{
				if(i+1 == graphicsListeners.size() || layer < graphicsListeners.get(i+1).getKey())
				{
					graphicsListeners.add(i+1, new AbstractMap.SimpleEntry<Byte, GraphicsListener>(layer, listener));
					break;
				}
			}
		}
	}
	
	public boolean removeGraphicsListener(GraphicsListener listener)
	{
		if(graphicsListeners.remove(graphicsListeners.get(graphicsListenerIndex(listener))) == false)
		{
			System.err.println("Error removing object " + listener.toString() + " as a graphics listener.");
			return false;
		}
		
		return true;
	}
	
	/***
	 * Given the {@link updates.GraphicsListener GraphicsListener}, it will return the index it is in inside of the {@link graphicsListener} {@link ArrayList}.
	 * @param listener
	 * @return int - index of the listener in the {@link #graphicsListeners} {@link ArrayList}.
	 */
	private int graphicsListenerIndex(GraphicsListener listener)
	{
		for(int i = 0; i < graphicsListeners.size(); i++)
		{
			if(graphicsListeners.get(i).getValue().equals(listener))
			{
				return i;
			}
		}
		
		return -1;
	}
	
	public void changeGraphicsLayer(GraphicsListener listener, RenderLayer newLayer)
	{
		if(removeGraphicsListener(listener) == false)
		{
			throw new RuntimeException("Renderer | Unable to remove GraphicsListener " + listener.toString() + " from graphicsListeners ArrayList when attempting to "
					+ "change its layer.");
		}
		
		addGraphicsListener(listener, newLayer);
	}
	
	public void addLateUpdateListener(LateUpdateListener listener)
	{
		if(lateUpdateListeners.contains(listener) == false)
		{
			lateUpdateListeners.add(listener);
		}
	}
	
	public void removeLateUpdateListener(LateUpdateListener listener)
	{
		if(lateUpdateListeners.remove(listener) == false)
		{
			System.err.println("Error removing object " + listener.toString() + " as a late update listener.");
		}
	}
	
	private void makeLateUpdateCalls()
	{
		try
		{
			for(LateUpdateListener listener : lateUpdateListeners)
			{
				listener.lateUpdate();
			}
		}
		catch(ConcurrentModificationException e)
		{
			//Do nothing. Really a trivial exception that occurs when adding an element to an ArrayList while it is being iterated through
		}
	}
	
	@Override
	public void componentHidden(ComponentEvent e)
	{
		System.out.println("Component Hidden");
		timer.stop();
		Audio.outOfFocus();
	}
	
	@Override
	public void componentShown(ComponentEvent e)
	{
		timer.start();	
		Time.updateTimestamp();
	}

	@Override
	public void componentMoved(ComponentEvent e)
	{
		//NOTHING
	}

	@Override
	public void componentResized(ComponentEvent e)
	{
		if(graphicsListeners.size() == 0)
		{
			return;//This method is being called before everything is initialized. This is to ignore that initial call.
		}
		
		int oldScreenWidth = Options.SCREEN_WIDTH;
		int oldScreenHeight = Options.SCREEN_HEIGHT;
		Options.SCREEN_WIDTH = canvas.getWidth();
		Options.SCREEN_HEIGHT = canvas.getHeight();
		
		for(int i = 0; i < graphicsListeners.size(); i++)
		{
			graphicsListeners.get(i).getValue().resize(oldScreenWidth, oldScreenHeight);
		}
		
		FontManagement.resetFontSizes();
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
		System.out.println("Mouse released");
	}
}