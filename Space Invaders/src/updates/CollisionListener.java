package updates;

import java.awt.Rectangle;

public interface CollisionListener
{
	/**
	 * <b>A method that can get called multiple times per frame.</b>
	 * <p>
	 * This gets called when an entity collides with it.
	 * @param e (Entity) - The entity that collided with this entity.
	 */
	void onCollision(CollisionListener o);
	
	Rectangle getCollider();
}
