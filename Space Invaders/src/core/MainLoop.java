package core;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;

import javax.swing.Timer;

import input.KeyInputManagement;
import io.FontManagement;
import system.Audio;
import system.Logger;
import system.Options;
import system.Time;
import updates.CollisionListener;
import updates.EarlyUpdateListener;
import updates.GraphicsListener;
import updates.LateUpdateListener;
import updates.UpdateListener;
import utils.ConstantValues.RenderLayer;
import utils.Wall;

public class MainLoop implements ComponentListener, KeyListener, MouseListener
{
	private Renderer renderer;
	
	private ArrayList<EarlyUpdateListener> earlyUpdateListeners = new ArrayList<EarlyUpdateListener>();
	private ArrayList<EarlyUpdateListener> ADD_earlyUpdateListeners = new ArrayList<EarlyUpdateListener>();
	private ArrayList<EarlyUpdateListener> REM_earlyUpdateListeners = new ArrayList<EarlyUpdateListener>();
	
	private ArrayList<CollisionListener> collisionListeners = new ArrayList<CollisionListener>();
	private ArrayList<CollisionListener> ADD_collisionListeners = new ArrayList<CollisionListener>();
	private ArrayList<CollisionListener> REM_collisionListeners = new ArrayList<CollisionListener>();
	
	private ArrayList<UpdateListener> updateListeners = new ArrayList<UpdateListener>();
	private ArrayList<UpdateListener> ADD_updateListeners = new ArrayList<UpdateListener>();
	private ArrayList<UpdateListener> REM_updateListeners = new ArrayList<UpdateListener>();
	
	private ArrayList<LateUpdateListener> lateUpdateListeners = new ArrayList<LateUpdateListener>();
	private ArrayList<LateUpdateListener> ADD_lateUpdateListeners = new ArrayList<LateUpdateListener>();
	private ArrayList<LateUpdateListener> REM_lateUpdateListeners = new ArrayList<LateUpdateListener>();
	
	/***
	 * <i>The first value in the Entry is the render layer.</i>
	 */
	private ArrayList<AbstractMap.SimpleEntry<Byte, GraphicsListener>> graphicsListeners = new ArrayList<>();
	private ArrayList<AbstractMap.SimpleEntry<Byte, GraphicsListener>> ADD_graphicsListeners = new ArrayList<>();
	private ArrayList<AbstractMap.SimpleEntry<Byte, GraphicsListener>> REM_graphicsListeners = new ArrayList<>();
	
	public MainLoop()
	{
		renderer = new Renderer(this);
		
		renderer.addComponentListener(this);
		renderer.addKeyListener(this);
		renderer.addMouseListener(this);
	}
	
	private Timer mainLoop = new Timer(1000/Options.FPS, new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			makeEarlyUpdateCalls();
			makeCollisionCalls();
			makeUpdateCalls();
			makeLateUpdateCalls();
			checkRenderList();
			renderer.render();
		}
	});
	
	public void start()
	{
		mainLoop.start();
	}
	
	public void stop()
	{
		mainLoop.stop();
	}
	
	public void addEarlyUpdateListener(EarlyUpdateListener listener)
	{
		if(earlyUpdateListeners.contains(listener) == false)
		{
			if(ADD_earlyUpdateListeners.add(listener) == false)
			{
				Logger.stderr(MainLoop.class, "Error occured when adding object " + listener.toString() + " to the ADD_earlyUpdateListeners ArrayList.");
			}
		}
		else
		{
			Logger.stderr(MainLoop.class, "Attempted to add " + listener.toString() + " to the earlyUpdateListeners ArrayList when an instance of it already exists.");
		}
	}
	
	public void removeEarlyUpdateListener(EarlyUpdateListener listener)
	{
		if(earlyUpdateListeners.contains(listener) == true)
		{
			if(REM_earlyUpdateListeners.add(listener) == false)
			{
				Logger.stderr(MainLoop.class, "Error occured when adding object " + listener.toString() + " to the REM_earlyUpdateListeners ArrayList.");
			}
		}
		else
		{
			Logger.stderr(MainLoop.class, "Attempted to remove " + listener.toString() + " from the earlyUpdateListeners ArrayList when an instance of it did not exist.");
		}
	}
	
	private void makeEarlyUpdateCalls()
	{
		if(REM_earlyUpdateListeners.size() > 0)
		{
			if(earlyUpdateListeners.removeAll(REM_earlyUpdateListeners) == false)
			{
				Logger.stderr(MainLoop.class, "Error removing the contents of the REM_earlyUpdateListeners ArrayList from the earlyUpdateListeners ArrayList.");
			}
			REM_earlyUpdateListeners.clear();
		}
		
		if(ADD_earlyUpdateListeners.size() > 0)
		{
			if(earlyUpdateListeners.addAll(ADD_earlyUpdateListeners) == false)
			{
				Logger.stderr(MainLoop.class, "Error adding the contents of the ADD_earlyUpdateListeners ArrayList to the earlyUpdateListeners ArrayList.");
			}
			ADD_earlyUpdateListeners.clear();
		}
		
		for(EarlyUpdateListener listener : earlyUpdateListeners)
		{
			listener.earlyUpdate();
		}
	}
	
	public void addCollisionListener(CollisionListener listener)
	{
		if(collisionListeners.contains(listener) == false)
		{
			if(ADD_collisionListeners.add(listener) == false)
			{
				Logger.stderr(MainLoop.class, "Error occured when adding object " + listener.toString() + " to the ADD_collisionListeners ArrayList.");
			}
		}
		else
		{
			Logger.stderr(MainLoop.class, "Attempted to add " + listener.toString() + " to the collisionListeners ArrayList when an instance of it already exists.");
		}
	}
	
	public void removeCollisionListener(CollisionListener listener)
	{
		if(collisionListeners.contains(listener) == true)
		{
			if(REM_collisionListeners.add(listener) == false)
			{
				Logger.stderr(MainLoop.class, "Error occured when adding object " + listener.toString() + " to the REM_collisionListeners ArrayList.");
			}
		}
		else
		{
			Logger.stderr(MainLoop.class, "Attempted to remove " + listener.toString() + " from the collisionListeners ArrayList when an instance of it did not exist.");
		}
	}
	
	private void makeCollisionCalls()
	{
		if(REM_collisionListeners.size() > 0)
		{
			if(collisionListeners.removeAll(collisionListeners) == false)
			{
				Logger.stderr(MainLoop.class, "Error removing the contents of the REM_collisionListeners ArrayList from the earlyUpdateListeners ArrayList.");
			}
			REM_collisionListeners.clear();
		}
		
		if(ADD_collisionListeners.size() > 0)
		{
			if(collisionListeners.addAll(ADD_collisionListeners) == false)
			{
				Logger.stderr(MainLoop.class, "Error adding the contents of the ADD_collisionListeners ArrayList to the collisionListeners ArrayList.");
			}
			ADD_collisionListeners.clear();
		}
		
		for(CollisionListener listener1 : collisionListeners)
		{
			for(CollisionListener listener2 : collisionListeners)
			{
				if(listener1 == listener2)
				{
					continue;
				}
				
				if(listener2 instanceof Wall)
				{
					listener1.onCollision(listener2);
				}
				else if(listener1.getCollider().intersects(listener2.getCollider()) == true)
				{
					listener1.onCollision(listener2);
					listener2.onCollision(listener1);
				}
			}
		}
	}
	
	public void addUpdateListener(UpdateListener listener)
	{
		if(updateListeners.contains(listener) == false)
		{
			if(ADD_updateListeners.add(listener) == false)
			{
				Logger.stderr(MainLoop.class, "Error occured when adding object " + listener.toString() + " to the ADD_updateListeners ArrayList.");
			}
		}
		else
		{
			Logger.stderr(MainLoop.class, "Attempted to add " + listener.toString() + " to the updateListeners ArrayList when an instance of it already exists.");
		}
	}
	
	public void removeUpdateListener(UpdateListener listener)
	{
		if(updateListeners.contains(listener) == true)
		{
			if(REM_updateListeners.add(listener) == false)
			{
				Logger.stderr(MainLoop.class, "Error occured when adding object " + listener.toString() + " to the REM_updateListeners ArrayList.");
			}
		}
		else
		{
			Logger.stderr(MainLoop.class, "Attempted to remove " + listener.toString() + " from the updateListeners ArrayList when an instance of it did not exist.");
		}
	}
	
	private void makeUpdateCalls()
	{
		if(REM_updateListeners.size() > 0)
		{
			if(updateListeners.removeAll(REM_updateListeners) == false)
			{
				Logger.stderr(MainLoop.class, "Error removing the contents of the REM_updateListeners ArrayList from the updateListeners ArrayList.");
			}
			REM_updateListeners.clear();
		}
		
		if(ADD_updateListeners.size() > 0)
		{
			if(updateListeners.addAll(ADD_updateListeners) == false)
			{
				Logger.stderr(MainLoop.class, "Error adding the contents of the ADD_updateListeners ArrayList to the updateListeners ArrayList.");
			}
			ADD_updateListeners.clear();
		}
		
		for(UpdateListener listener : updateListeners)
		{
			listener.update();
		}
	}
	
	public void addLateUpdateListener(LateUpdateListener listener)
	{
		if(lateUpdateListeners.contains(listener) == false)
		{
			if(ADD_lateUpdateListeners.add(listener) == false)
			{
				Logger.stderr(MainLoop.class, "Error occured when adding object " + listener.toString() + " to the ADD_lateUpdateListeners ArrayList.");
			}
		}
		else
		{
			Logger.stderr(MainLoop.class, "Attempted to add " + listener.toString() + " to the lateUpdateListeners ArrayList when an instance of it already exists.");
		}
	}
	
	public void removeLateUpdateListener(LateUpdateListener listener)
	{
		if(lateUpdateListeners.contains(listener) == true)
		{
			if(REM_lateUpdateListeners.add(listener) == false)
			{
				Logger.stderr(MainLoop.class, "Error occured when adding object " + listener.toString() + " to the REM_lateUpdateListeners ArrayList.");
			}
		}
		else
		{
			Logger.stderr(MainLoop.class, "Attempted to remove " + listener.toString() + " from the lateUpdateListeners ArrayList when an instance of it did not exist.");
		}
	}
	
	private void makeLateUpdateCalls()
	{
		if(REM_lateUpdateListeners.size() > 0)
		{
			if(lateUpdateListeners.removeAll(REM_lateUpdateListeners) == false)
			{
				Logger.stderr(MainLoop.class, "Error removing the contents of the REM_lateUpdateListeners ArrayList from the lateUpdateListeners ArrayList.");
			}
			REM_lateUpdateListeners.clear();
		}
		
		if(ADD_lateUpdateListeners.size() > 0)
		{
			if(lateUpdateListeners.addAll(ADD_lateUpdateListeners) == false)
			{
				Logger.stderr(MainLoop.class, "Error adding the contents of the ADD_lateUpdateListeners ArrayList to the lateUpdateListeners ArrayList.");
			}
			ADD_lateUpdateListeners.clear();
		}
		
		for(LateUpdateListener listener : lateUpdateListeners)
		{
			listener.lateUpdate();
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
		//Does not contain
		if(graphicsListenerIndex(listener) == -1)
		{
			if(ADD_graphicsListeners.add(new AbstractMap.SimpleEntry<Byte, GraphicsListener>(layer, listener)) == false)
			{
				Logger.stderr(MainLoop.class, "Error occured when adding object " + listener.toString() + " with layer " + layer +
						" to the ADD_graphicsListeners ArrayList.");
			}
		}
		else
		{
			Logger.stderr(MainLoop.class, "Attempted to add " + listener.toString() + " with layer " + layer +
					" to the graphicsListeners ArrayList when an instance of it already exists.");
		}
	}
	
	public boolean removeGraphicsListener(GraphicsListener listener)
	{
		//Does contain
		if(graphicsListenerIndex(listener) != -1)
		{
			if(REM_graphicsListeners.add(graphicsListeners.get(graphicsListenerIndex(listener))) == false)
			{
				Logger.stderr(MainLoop.class, "Error occured when adding object " + listener.toString() + " to the REM_graphicsListeners ArrayList.");
				return false;
			}
			
			return true;
		}
		else
		{
			Logger.stderr(MainLoop.class, "Attempted to remove " + listener.toString() + " from the graphicsListeners ArrayList when an instance of it did not exist.");
			return false;
		}
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
			throw new RuntimeException("Unable to remove GraphicsListener " + listener.toString() + " from graphicsListeners ArrayList when attempting to "
					+ "change its layer.");
		}
		
		addGraphicsListener(listener, newLayer);//TODO this won't work
	}
	
	private void checkRenderList()
	{
		if(REM_graphicsListeners.size() > 0)
		{
			if(graphicsListeners.removeAll(REM_graphicsListeners) == false)
			{
				Logger.stderr(MainLoop.class, "Error removing the contents of the REM_graphicsListeners ArrayList to the graphicsListeners ArrayList.");
			}
			REM_graphicsListeners.clear();
		}
		
		if(ADD_graphicsListeners.size() > 0)
		{
			@SuppressWarnings("unchecked")
			ArrayList<AbstractMap.SimpleEntry<Byte, GraphicsListener>> noDesyncGfxListeners 
				= (ArrayList<SimpleEntry<Byte, GraphicsListener>>) ADD_graphicsListeners.clone();
			ADD_graphicsListeners.clear();
			
			topLoop: for(SimpleEntry<Byte, GraphicsListener> entry :  noDesyncGfxListeners)
			{
				for(int i = -1; i < graphicsListeners.size(); i++)
				{
					if(i+1 == graphicsListeners.size() || entry.getKey() < graphicsListeners.get(i+1).getKey())
					{
						graphicsListeners.add(i+1, entry);
						continue topLoop;
					}
				}
				Logger.stderr(MainLoop.class, "Error adding the contents of the ADD_graphicsListeners ArrayList to the graphicsListeners ArrayList.");
			}
		}
	}
	
	public ArrayList<SimpleEntry<Byte, GraphicsListener>> getGraphicsListeners()
	{
		return graphicsListeners;
	}
	
	@Override
	public void componentHidden(ComponentEvent e)
	{
		System.out.println("Component Hidden");
		mainLoop.stop();
		Audio.focus(false);
	}
	
	@Override
	public void componentShown(ComponentEvent e)
	{
		mainLoop.start();	
		Time.updateTimestamp();
		Audio.focus(true);
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
			FontManagement.resetFontSizes();
			return;//This method is being called before everything is initialized. This is to ignore that initial call.
		}
		
		int oldScreenWidth = Options.SCREEN_WIDTH;
		int oldScreenHeight = Options.SCREEN_HEIGHT;
		Options.SCREEN_WIDTH = renderer.getCanvas().getWidth();
		Options.SCREEN_HEIGHT = renderer.getCanvas().getHeight();
		
		FontManagement.resetFontSizes();
		
		for(int i = 0; i < graphicsListeners.size(); i++)
		{
			graphicsListeners.get(i).getValue().resize(oldScreenWidth, oldScreenHeight);
		}
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