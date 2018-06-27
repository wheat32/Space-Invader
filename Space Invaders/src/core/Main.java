package core;

import io.DebugPrints;
import system.Logger;
import system.PrefsHandler;
import system.Time;
import utils.EntityManagement;
import utils.ObjectCollection;

public class Main
{
	public static void main(String[] args) 
	{
		new Logger();
		new PrefsHandler().importPrefs();//TODO
		new Time();//this sets up the time
		new DebugPrints();//this sets up the debug prings
		new EntityManagement();//this sets up the entity management class
		ObjectCollection.getMainLoop().start();
		ObjectCollection.getMainMenu().setInMainMenu(true);
	}
}
