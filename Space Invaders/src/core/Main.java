package core;

import system.PrefsHandler;
import utils.ObjectCollection;

public class Main
{
	public static void main(String[] args) 
	{
		new PrefsHandler().importPrefs();
		ObjectCollection.getRenderer().start();
		ObjectCollection.getDebugPrints();
		ObjectCollection.getMainMenu().setInMainMenu(true);
	}
}
