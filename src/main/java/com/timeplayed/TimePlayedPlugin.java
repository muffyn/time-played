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

	private boolean paused = true;

	/*
	private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
	ScheduledFuture future = null;
	*/
	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(myOverlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(myOverlay);
	}

	@Subscribe
	private void onGameStateChanged(GameStateChanged event) {
		if (event.getGameState() == GameState.LOADING ||
				event.getGameState() == GameState.LOGGED_IN ||
				event.getGameState() == GameState.CONNECTION_LOST) {
			if (paused) {
				paused = false;
				// there is a 1.2 second penalty every time you hop
				myOverlay.seconds += 12;
				if (myOverlay.seconds >= 600) {
					myOverlay.minutes += 1;
					myOverlay.seconds -= 600;
				}

			}
		} else if (!paused) {
			paused = true;
		}
	}

	@Subscribe
	public void onGameTick(GameTick event) {

		myOverlay.seconds += 6;
		//myOverlay.seconds = seconds;

		if (myOverlay.seconds >= 600) {
			myOverlay.minutes += 1;
			myOverlay.seconds -= 600;
		}

		/*if (future != null) {
			future.cancel(true);
		}

		future = executorService.scheduleAtFixedRate(() -> myOverlay.seconds++,
				0, 100, TimeUnit.MILLISECONDS);
		*/
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
}
