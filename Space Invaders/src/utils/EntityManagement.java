package utils;

import java.util.ArrayList;

import entities.Entity;
import entities.players.SpaceShip;
import input.KeyInputManagement;
import updates.LateUpdateListener;

public class EntityManagement implements ConstantValues, LateUpdateListener
{
	private static EntityManagement entityManagementInstance;
	private static ArrayList<Entity> entities = new ArrayList<>();
	private static ArrayList<Entity> removalList = new ArrayList<>();
	
	public EntityManagement()
	{
		entityManagementInstance = this;
		ObjectCollection.getRenderer().addLateUpdateListener(entityManagementInstance);
	}
	
	public static void addEntity(Entity entity)
	{
		entities.add(entity);
	}
	
	public static void addEntities(ArrayList<Entity> entities)
	{
		entities.addAll(entities);
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
	
	public static void awaitingRespawn()
	{
		//Waiting on user input to start game
		if(KeyInputManagement.fireKeyPressed == true)
		{
			shipRespawn();
			//objColl.getGameManagement().startGame();
			//ObjectCollection.getGameManagement().setEndCode((short) 0);
		}
	}
	
	private static void shipRespawn() 
	{
		/*if(objColl.getAlienPack() != null)//TODO
		{
			objColl.getAlienPack().moveUp(6);
		}*/
		
		/*objColl.setSpaceShip(new SpaceShip(objColl, (short) 20, (short) 20));
		addEntity(objColl.getSpaceShip());
		objColl.getSpaceShip().setPosition((Stats.SCREEN_WIDTH/2 - (int)objColl.getSpaceShip().getDimension().width),
				(Stats.SCREEN_HEIGHT - 60 - (int)objColl.getSpaceShip().getDimension().getHeight()));*///TODO
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
				entities.remove(entity);
				continue;
			}
		}
	}

	@Override
	public void lateUpdate()
	{
		if(removalList.isEmpty() == false)
		{
			for(Entity entity : removalList)
			{
				if(entity != null && entities.contains(entity) == true)
				{
					entities.remove(entity);
				}
			}
		}
	}
}
