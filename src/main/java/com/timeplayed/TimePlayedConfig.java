package com.timeplayed;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

import java.awt.*;

@ConfigGroup("timeplayed")
public interface TimePlayedConfig extends Config
{
	@ConfigItem(
		keyName = "displayms",
		name = "Display ms?",
		description = "Check the box to display ms."
	)
	default boolean displayMs() { return false; }

	@ConfigItem(
			keyName = "ontop",
			name = "Always on top?",
			description = "Check the box to show the timer above all other interfaces."
	)
	default boolean ontop() { return false; }

	@ConfigItem(
			keyName = "font",
			name = "Font",
			description = "The font to display the timer with"
	)
	default String font() { return "Century Gothic Bold"; }

	@ConfigItem(
			keyName = "fontsize",
			name = "Font size",
			description = "The font size to display the timer at"
	)
	default int fontSize() { return 16; }

	@ConfigItem(
			keyName = "fontcolor",
			name = "Font color",
			description = "The color to display the timer with"
	)
	default Color fontColor() { return new Color(41,204,84); }

}
