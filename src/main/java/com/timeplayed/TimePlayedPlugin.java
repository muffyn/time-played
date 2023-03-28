package com.timeplayed;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

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

	@Provides
	TimePlayedConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TimePlayedConfig.class);
	}
}
