package updates;

public interface EarlyUpdateListener
{
	/**
	 * <b>A method that gets called every frame.</b>
	 * <p>
	 * This should only be used for things that must be called every frame before anything else. This is called before any graphics updates.
	 */
	void earlyUpdate();
}
