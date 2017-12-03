package utils;

public interface FrameListener
{
	/**
	 * <b>A method that gets called every frame.</b>
	 * <p>
	 * This should only be used for things that must be called every frame.
	 * @param elapsedTime TODO
	 */
	void frameCall(int elapsedTime);
}
