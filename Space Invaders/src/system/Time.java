package system;

import updates.EarlyUpdateListener;
import utils.ObjectCollection;

public class Time implements EarlyUpdateListener
{
	private static Time timeInstance;
	private static long timestamp = System.currentTimeMillis();
	private static int deltaTime = 0;
	
	public Time()
	{
		timeInstance = this;
		ObjectCollection.getMainLoop().addEarlyUpdateListener(timeInstance);
	}
	
	@Override
	public void earlyUpdate()
	{
		//System.out.println(System.currentTimeMillis() + " - " + timestamp);
		deltaTime = (int) (System.currentTimeMillis() - timestamp);
		timestamp = System.currentTimeMillis();
	}
	
	/***
	 * This method returns the time it took to complete this frame.
	 * @return deltaTime - The time (in milliseconds) it took to complete the frame.
	 */
	public static int deltaTime()
	{
		return deltaTime;
	}
	
	public static void updateTimestamp()
	{
		timestamp = System.currentTimeMillis();
	}
}
