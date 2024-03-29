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
			keyName = "displaydays",
			name = "Display days?",
			description = "Check the box to display days instead of just hours.",
			position = 1
	)
	default boolean displayDays() { return false; }

	@ConfigItem(
			keyName = "smoothtimer",
			name = "Smooth timer?",
			description = "Check the box to use a smooth timer. This means it will interpolate and count up ms instead of just counting up ticks, and the seconds section will change every second rather than at a variable rate dependent on ticks.",
			position = 2
	)
	default boolean smoothTimer() { return true; }

	@ConfigItem(
			keyName = "ontop",
			name = "Always on top?",
			description = "Check the box to show the timer above all other interfaces.",
			position = 3
	)
	default boolean ontop() { return true; }

	@ConfigItem(
			keyName = "showonreportbutton",
			name = "Show time on Report button?",
			description = "Check the box to draw the timer on the report button. Hides the default timer.",
			position = 5
	)
	default boolean showOnReportButton() { return false; }

	@ConfigItem(
			keyName = "clearreportbutton",
			name = "Clear text on Report button?",
			description = "Check the box to clear the text from the Report button (to put the fancy timer on top).",
			position = 6
	)
	default boolean clearReportButton() { return false; }

	@ConfigItem(
			keyName = "defstyle",
			name = "Use default RuneLite styling?",
			description = "Check the box to ignore all customizations below.",
			position = 4
	)
	default boolean defStyle() { return false; }

	@ConfigSection(
			name = "Timer Offset",
			description = "Subtract time from the display on the timer. Useful to reset the time from zero for challenges.",
			position = 7,
			closedByDefault = false
	)
	String offsetSection = "offs";
	@ConfigItem(
			keyName = "useoffset",
			name = "Use offset?",
			description = "Check the box to subtract the offset from the displayed time.",
			section = "offs",
			position = 1
	)
	default boolean useOffset() { return false; }
	@ConfigItem(
			keyName = "offset",
			name = "Offset (minutes)",
			description = "The amount of minutes to subtract from the timer.\n" +
					"Calculate this number by doing (hours * 60) + minutes.\n" +
					"If the number entered is too high, no offset will be used.",
			section = "offs",
			position = 2
	)
	default int offset() { return 0; }

	@ConfigSection(
			name = "Customizations",
			description = "Customize the appearance of the timer to look more like LiveSplit",
			position = 8,
			closedByDefault = false
	)
	String customizationsSection = "cust";

	@ConfigItem(
			keyName = "fontsize",
			name = "Font size",
			description = "The font size to display the timer at",
			section = "cust",
			position = 1
	)
	default int fontSize() { return 16; }

	@Alpha
	@ConfigItem(
			keyName = "fontcolor",
			name = "Font color",
			description = "The color to display the timer with",
			section = "cust",
			position = 2
	)
	default Color fontColor() { return new Color(41,204,84, 255); }

	@ConfigItem(
			keyName = "font",
			name = "Font",
			description = "The font to display the timer with",
			section = "cust",
			position = 3
	)
	default String font() { return "Century Gothic Bold"; }

	@Alpha
	@ConfigItem(
			keyName = "bgcolor",
			name = "Background color",
			description = "The color of the background (transparent by default)",
			section = "cust",
			position = 4
			//hidden = true // FIXME: hidden because it doesn't scale appropriate to the font size
	)
	default Color bgColor() { return new Color(70, 61, 50, 0); }

}
