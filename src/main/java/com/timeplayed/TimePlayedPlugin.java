package com.timeplayed;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.RuneScapeProfileChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@PluginDescriptor(
	name = "Time Played",
	description = "Displays time played",
	enabledByDefault = false,
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
		setStoredSeconds(myOverlay.seconds);
		setStoredMinutes(myOverlay.minutes);
		overlayManager.remove(myOverlay);
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
	public void onGameTick(GameTick event) {

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
			return client.getVarcIntValue(526);
		}
	}

	private void setStoredMinutes(int minutes)
	{
		configManager.setRSProfileConfiguration("timeplayed", "minutes", minutes);

	}
}
