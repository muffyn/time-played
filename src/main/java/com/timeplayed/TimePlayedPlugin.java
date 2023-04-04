package com.timeplayed;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.*;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.events.RuneScapeProfileChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
	name = "Time Played",
	description = "Displays time played",
	enabledByDefault = true,
	tags = {"time played, playtime, play, time, timer, ingame, in game"}
)

public class TimePlayedPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private TimePlayedOverlay myOverlay;

	@Inject
	private TimePlayedConfig config;

	@Inject
	private ConfigManager configManager;

	private boolean paused = true;


	@Override
	protected void startUp() throws Exception {
		overlayManager.add(myOverlay);
		myOverlay.seconds = getStoredSeconds();
		myOverlay.minutes = getStoredMinutes();
	}

	@Override
	protected void shutDown() throws Exception {
		if (config.clearReportButton() || config.showOnReportButton()) {
			Widget reportButton = client.getWidget(WidgetInfo.CHATBOX_REPORT_TEXT);
			if (reportButton != null) {
				reportButton.setText("Report");
			}
		}
		setStoredSeconds(myOverlay.seconds);
		setStoredMinutes(myOverlay.minutes);
		overlayManager.remove(myOverlay);
	}

	@Subscribe
	private void onConfigChanged(ConfigChanged event) {
		if (!event.getGroup().equals("timeplayed"))
		{
			return;
		}

		if (event.getKey().equals("ontop")) {
			if (config.ontop()) {
				myOverlay.setLayer(OverlayLayer.ABOVE_WIDGETS);
			} else {
				myOverlay.setLayer(OverlayLayer.ABOVE_SCENE);
			}
		}

		if (event.getKey().equals("showonreportbutton") || event.getKey().equals("clearreportbutton")) {
			Widget reportButton = client.getWidget(WidgetInfo.CHATBOX_REPORT_TEXT);
			if (reportButton != null) {
				if (config.showOnReportButton() || config.clearReportButton()) {
					reportButton.setText("");
				} else {
					reportButton.setText("Report");
				}
			}
		}
	}

	@Subscribe
	private void onGameStateChanged(GameStateChanged event) {
		if (event.getGameState() == GameState.LOADING ||
				event.getGameState() == GameState.LOGGED_IN ||
				event.getGameState() == GameState.CONNECTION_LOST) {
			if (paused) {
				paused = false;

				myOverlay.seconds = getStoredSeconds();
				myOverlay.minutes = getStoredMinutes();
				if (myOverlay.minutes == 0) {
					myOverlay.minutes = client.getVarcIntValue(526);
				}

				// there is a 1.2 second penalty every time you hop
				myOverlay.seconds += 12;
				if (myOverlay.seconds >= 600) {
					myOverlay.minutes += 1;
					myOverlay.seconds -= 600;
				}

				// after penalty, see if we were desynced
				int timePlayed = client.getVarcIntValue(526);
				if (timePlayed > myOverlay.minutes) {
					myOverlay.minutes = timePlayed;
					myOverlay.seconds = 0;
				}

			}
		} else if (!paused) {
			paused = true;
			setStoredSeconds(myOverlay.seconds);
			setStoredMinutes(myOverlay.minutes);
		}
	}

	@Subscribe
	public void onRuneScapeProfileChanged(RuneScapeProfileChanged event) {
		myOverlay.seconds = getStoredSeconds();
		myOverlay.minutes = getStoredMinutes();
	}

	@Subscribe
	public void onGameTick(GameTick event) {
		if (myOverlay == null) {
			return;
		}
		myOverlay.lastTick = java.time.Instant.now().toEpochMilli();
		myOverlay.msOffset = 0;
		myOverlay.seconds += 6;

		if (myOverlay.seconds >= 600) {
			myOverlay.minutes += 1;
			myOverlay.seconds -= 600;
		}



	}

	@Subscribe
	public void onVarbitChanged(VarbitChanged event) {
		int timePlayed = client.getVarcIntValue(526);
		if (timePlayed > myOverlay.minutes) {
			myOverlay.minutes = timePlayed;
			myOverlay.seconds = 0;
		}
	}

	@Subscribe
	public void onClientTick(ClientTick event) {
		if (paused) {
			return;
		}
		// clear report button
		if (config.clearReportButton() || config.showOnReportButton()) {
			Widget reportButton = client.getWidget(WidgetInfo.CHATBOX_REPORT_TEXT);
			if (reportButton != null && reportButton.getText().equals("Report")) {
				reportButton.setText("");
			}
		}

		// save current time for smooth timer
		if (myOverlay != null) {
			long time = java.time.Instant.now().toEpochMilli();
			myOverlay.msOffset = (int) ((time - myOverlay.lastTick) / 100);
		}

	}

	@Provides
	TimePlayedConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TimePlayedConfig.class);
	}

	private int getStoredSeconds()
	{
		try
		{
			return Integer.parseInt(configManager.getRSProfileConfiguration("timeplayed", "seconds"));
		}
		catch (NumberFormatException ignored)
		{
			return 0;
		}
	}

	private void setStoredSeconds(int seconds)
	{
		configManager.setRSProfileConfiguration("timeplayed", "seconds", seconds);

	}

	private int getStoredMinutes()
	{
		try
		{
			return Integer.parseInt(configManager.getRSProfileConfiguration("timeplayed", "minutes"));
		}
		catch (NumberFormatException ignored)
		{
			paused = true;
			return 0;
		}
	}

	private void setStoredMinutes(int minutes)
	{
		configManager.setRSProfileConfiguration("timeplayed", "minutes", minutes);

	}
}
