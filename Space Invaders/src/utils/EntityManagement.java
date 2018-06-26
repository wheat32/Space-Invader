package utils;

import java.util.ArrayList;

import entities.Entity;
import entities.players.SpaceShip;
import updates.CollisionListener;
import updates.EarlyUpdateListener;
import updates.GraphicsListener;
import updates.LateUpdateListener;
import updates.UpdateListener;

public class EntityManagement implements ConstantValues, EarlyUpdateListener, LateUpdateListener
{
	private static EntityManagement entityManagementInstance;
	private static ArrayList<Entity> entities = new ArrayList<>();
	private static ArrayList<Entity> additionList = new ArrayList<>();
	private static ArrayList<Entity> removalList = new ArrayList<>();
	
	public EntityManagement()
	{
		entityManagementInstance = this;
		ObjectCollection.getMainLoop().addLateUpdateListener(entityManagementInstance);
		ObjectCollection.getMainLoop().addEarlyUpdateListener(entityManagementInstance);
	}
	
	public static void addEntity(Entity entity)
	{
		additionList.add(entity);
	}
	
	public static void addEntities(ArrayList<Entity> entities)
	{
		additionList.addAll(entities);
	}
	
	public static void removeEntity(Entity entity)
	{
		removalList.add(entity);
	}
	
	public static Entity getEntity(Entity requestedEntity)
	{
		for(Entity entity : entities)
		{
			if(entity.getClass().equals(requestedEntity.getClass()) == true)
			{
				return entity;
			}
		}
		
		return null;
	}
	
	public static Entity getEntity(int index)
	{
		if(index <= entities.size() && index >= 0)
		{
			return entities.get(index);
		}
		
		return null;
	}
	
	public static ArrayList<Entity> getEntities()
	{
		return entities;
	}
	
	public static int getEntitiesSize()
	{
		return entities.size();
	}
	
	public static boolean containsEntity(Entity entity)
	{
		return (entities.contains(entity)) ? true : false;
	}
	
	/**
	 * <b>Removes all entities from the ArrayList containing the entities.</b>
	 * @param includeShip - true to remove the SpaceShip, false to leave it
	 */
	public static void removeAllEntities(boolean includeShip)
	{
		for(Entity entity : entities)
		{
			//Remove the spaceship if specified by the call
			if(entity instanceof SpaceShip == true && includeShip == true)
			{
				removalList.add(entity);
				continue;
			}
		}
	}
	
	@Override
	public void earlyUpdate()
	{
		if(additionList.isEmpty() == false)
		{
			entities.addAll(additionList);
			additionList.clear();
		}
	}

	@Override
	public void lateUpdate()
	{
		if(removalList.isEmpty() == false)
		{
			System.out.println("removing " + removalList.size() + " entities");
			
			for(Entity entity : removalList)
			{
				if(entity != null && entities.contains(entity) == true)
				{
					if(entity instanceof EarlyUpdateListener)
					{
						ObjectCollection.getMainLoop().removeEarlyUpdateListener((EarlyUpdateListener) entity);
					}
					if(entity instanceof CollisionListener)
					{
						ObjectCollection.getMainLoop().removeCollisionListener((CollisionListener) entity);
					}
					if(entity instanceof UpdateListener)
					{
						ObjectCollection.getMainLoop().removeUpdateListener((UpdateListener) entity);
					}
					if(entity instanceof LateUpdateListener)
					{
						ObjectCollection.getMainLoop().removeLateUpdateListener((LateUpdateListener) entity);
					}
					if(entity instanceof GraphicsListener)
					{
						ObjectCollection.getMainLoop().removeGraphicsListener(entity);
					}
					entities.remove(entity);
				}
			}
			
			removalList.clear();
		}
	}
}
