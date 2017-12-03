package utils;

import java.util.ArrayList;

import core.Entity;
import miscEntities.Wall;
import players.SpaceShip;

public class EntityManagement implements ConstantValues
{
	private ArrayList<Entity> entities = new ArrayList<>();
	
	public void addEntity(Entity entity)
	{
		entities.add(entity);
		entity.setVelocity(0, 0);
	}
	
	public void removeEntity(Entity entity)
	{
		if(entity != null && entities.contains(entity) == true)
		{
			entities.remove(entity);
		}
	}
	
	public Entity getEntity(Entity requestedEntity)
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
	
	public void onEndOfLife(Entity e) 
	{
		if(e instanceof SpaceShip == true)
		{
			Stats.lives--;
			requestShipRespawn();
		}
		
		entities.remove(e);
	}
	
	public void requestShipRespawn() 
	{
		//entities.remove(objColl.getSpaceShip());//TODO
		
		if(Stats.lives == 0)
		{
			ObjectCollection.getGameManagement().setEndCode((short) 2);
			return;
		}
		
		/*if(getEntity(objColl.getBossAlien()) != null)//TODO
		{
			objColl.getBossAlien().reset();
		}*/
		
		ObjectCollection.getGameManagement().stopGame();
		KeyInputManagement.fireKeyPressed = false;
		ObjectCollection.getGameManagement().setEndCode((short) 3);
	}
	
	public void awaitingRespawn()
	{
		//Waiting on user input to start game
		if(KeyInputManagement.fireKeyPressed == true)
		{
			shipRespawn();
			//objColl.getGameManagement().startGame();
			ObjectCollection.getGameManagement().setEndCode((short) 0);
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
}
