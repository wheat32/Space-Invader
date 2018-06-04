package utils;

import java.util.ArrayList;

import entities.Entity;
import entities.players.SpaceShip;
import input.KeyInputManagement;
import miscEntities.Wall;
import updates.UpdateListener;

public class EntityManagement implements ConstantValues, UpdateListener
{
	private ArrayList<Entity> entities = new ArrayList<>();
	private ArrayList<Entity> removalList = new ArrayList<>();
	
	public EntityManagement()
	{
		ObjectCollection.getRenderer().addUpdateListener(this);
	}
	
	public void addEntity(Entity entity)
	{
		entities.add(entity);
	}
	
	public void addEntityGroup(ArrayList<Entity> entities)
	{
		this.entities.addAll(entities);
	}
	
	public void removeEntity(Entity entity)
	{
		removalList.add(entity);
	}
	
	public Entity getEntity(Entity requestedEntity)
	{
		if(containsEntity(requestedEntity) == false)
		{
			return null;
		}
		
		for(Entity entity : entities)
		{
			if(entity.getClass().equals(requestedEntity.getClass()) == true)
			{
				return entity;
			}
		}
		
		return null;
	}
	
	public Entity getEntity(int index)
	{
		if(index <= entities.size())
		{
			return entities.get(index);
		}
		
		return null;
	}
	
	public int getEntitiesSize()
	{
		return entities.size();
	}
	
	public boolean containsEntity(Entity entity)
	{
		return (entities.contains(entity)) ? true : false;
	}
	
	public void awaitingRespawn()
	{
		//Waiting on user input to start game
		if(KeyInputManagement.fireKeyPressed == true)
		{
			shipRespawn();
			//objColl.getGameManagement().startGame();
			//ObjectCollection.getGameManagement().setEndCode((short) 0);
		}
	}
	
	private void shipRespawn() 
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
	 * <b>Removes all* entities from the ArrayList containing the entities.</b>
	 * <p>
	 * <i>*The only entity not removed is the Wall</i>
	 * @param includeShip - true to remove the SpaceShip, false to leave it
	 * @param includeWall - true to remove the Wall, false to leave it
	 */
	public void removeAllEntities(boolean includeShip, boolean includeWall)
	{
		for(Entity entity : entities)
		{
			//Remove the spaceship if specified by the call
			if(entity instanceof SpaceShip == true && includeShip == true)
			{
				entities.remove(entity);
				continue;
			}
			
			//Remove all except for the wall
			if(entity instanceof Wall == true && includeWall == true)
			{
				entities.remove(entity);
			}
		}
	}

	@Override
	public void update()//TODO make this late update
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
