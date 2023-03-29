package com.timeplayed;

import net.runelite.client.config.*;

import java.awt.*;

@ConfigGroup("timeplayed")
public interface TimePlayedConfig extends Config
{
	@ConfigItem(
			keyName = "displayms",
			name = "Display ms?",
			description = "Check the box to display ms.",
			position = 1
	)
	default boolean displayMs() { return false; }

	@ConfigItem(
			keyName = "ontop",
			name = "Always on top?",
			description = "Check the box to show the timer above all other interfaces.",
			position = 2
	)
	default boolean ontop() { return false; }

	@ConfigItem(
			keyName = "defstyle",
			name = "Use default RuneLite styling?",
			description = "Check the box to ignore all customizations below.",
			position = 3
	)
	default boolean defStyle() { return false; }

	@ConfigSection(
			name = "Customizations",
			description = "Customize the appearance of the timer to look more like LiveSplit",
			position = 4,
			closedByDefault = false
	)
	String customizationsSection = "cust";

	@ConfigItem(
			keyName = "font",
			name = "Font",
			description = "The font to display the timer with",
			section = "cust"
	)
	default String font() { return "Century Gothic Bold"; }

	@ConfigItem(
			keyName = "fontsize",
			name = "Font size",
			description = "The font size to display the timer at",
			section = "cust"
	)
	default int fontSize() { return 16; }

	@Alpha
	@ConfigItem(
			keyName = "fontcolor",
			name = "Font color",
			description = "The color to display the timer with",
			section = "cust"
	)
	default Color fontColor() { return new Color(41,204,84, 1); }

	@Alpha
	@ConfigItem(
			keyName = "bgcolor",
			name = "Background color",
			description = "The color of the background (transparent by default)",
			section = "cust",
			hidden = true // FIXME: hidden because it doesn't scale appropriate to the font size
	)
	default Color bgColor() { return new Color(70, 61, 50, 0); }

}
