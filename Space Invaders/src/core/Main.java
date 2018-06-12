package core;

import system.PrefsHandler;
import system.Time;
import utils.ObjectCollection;

public class Main
{
	public static void main(String[] args) 
	{
		new PrefsHandler().importPrefs();
		new Time();//this sets up the time
		ObjectCollection.getRenderer().start();
		ObjectCollection.getMainMenu().setInMainMenu(true);
	}
}
