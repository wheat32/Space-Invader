package updates;

public interface LateUpdateListener
{
	/**
	 * <b>A method that gets called after every graphic frame call.</b>
	 * <p>
	 * This should only be used for things that must be called every frame.
	 */
	void lateUpdate();
}
