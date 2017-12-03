package strings;

public interface ChangeLogs
{
	public final String _0_3_0 = "<b>Update 0.3.0 contains the following changes:</b>\n\n"
			+ " - Roughly 90% of the code has been rewritten and split into several new classes (was 15, is now 35).\n"
			+ " - Implemented a visual changelog.\n"
			+ " - Endless Mode has been renamed to Infinity Mode.\n"
			+ " - The main menu has been completely reworked, adding new submenus, new menu options, and customization options for "
			+ "Infinity Mode.\n"
			+ " - The rendering pipeline has been rewritten and contains optimizations.\n"
			+ " - The foundation for options (including control rebinding, audio mixing, graphic options, etc.) has been added.\n"
			+ " - A new music track was added for exceptional successes in Infinity Mode."
			+ " - Optimized all entities' code.\n";
	
	public final String _0_4_0 = "<b>Upcoming update 0.4.0 will contain the following changes:</b>\n\n"
			+ " - Functional mouse controls for the menu.\n"
			+ " - Multithreading support.\n"
			+ "<i>There may be more to add as development continues.</i>";
	
	public final String _0_5_0 = "<b>Upcoming update 0.5.0 will contain the following changes:</b>\n\n"
			+ "<b>Gameplay</b>:\n"
			+ " - Ships now have an \"Energy\" gauge. A ship's energy reserve will be delpeted by movement, activating an augment, firing your laser, etc. "
			+ "Thanks to the power of newly developed \"Dragon Energy\" engines, quite a lot of energy can be outputted. Not only this, but it can "
			+ "also regenerate energy reserves on its own."
			+ " - Normal laser shots now deplete your ship's overall energy instead of having their own gauge.\n"
			+ " - The inintial impliementation of the \"Augment\" system. You will be able to augment your ship, giving it special capabilities "
			+ "at the cost of energy, or something else.\n"
			+ " - Enemies will now leave behind materials when destroyed or killed. You can use these materials to upgrade your augments, build new ones, "
			+ "replenish limited ammo, repair your ship, etc.\n"
			+ " - The lives system is no longer primarily used. Ships now have hit points (HP). When a ship's HP is fully depleted, the ship will explode. "
			+ "Depending on the mode, lives may still be utilized."
			+ " - A new letter ranking system for Infinity Mode which shows in the results (shows after defeating a boss). You can " 
			+ "achieve a ranking between letters D to S (D being the lowest rank to S being exceptional).\n"
			+ " - There is now currency in the game. The currency units are called \"Credits\". In infinity mode, you can earn credits simply by eliminating waves "
			+ "of enemies. The amount of credits earned is determined by how well you performed in the wave, the number of enemies killed, special modifiers, and "
			+ "modified by your overall score."
			+ "\n"
			+ "<b>UI:</b>"
			+ " - Complete overhaul of the UI for Infinity Mode.\n"
			+ "\n"
			+ "<b>Augments:</b>"
			+ "- Augment \"Dodge\" has been added. Dodge allows you to move a certain direction quickly in a burst, allowing you to quickly evade enemy attacks.\n"
			+ "\n"
			+ "<b>Weapons:</b>"
			+ " - \"Missles\" have been added. Missles home in on enemies and don't deplenish the ship's energy. However, missles come in limited supply and can "
			+ "only be restocked by purchasing more or by special events.";
}
