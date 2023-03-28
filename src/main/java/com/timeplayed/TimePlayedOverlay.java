package com.timeplayed;

import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;

import javax.inject.Inject;
import java.awt.*;
import net.runelite.client.ui.overlay.tooltip.TooltipManager;


import static net.runelite.api.MenuAction.RUNELITE_OVERLAY_CONFIG;
import static net.runelite.client.ui.overlay.OverlayManager.OPTION_CONFIGURE;

public class TimePlayedOverlay extends Overlay {

    private TimePlayedPlugin plugin;
    private TimePlayedConfig config;
    private Client client;
    private TooltipManager tooltipManager;

    private PanelComponent panelComponent = new PanelComponent();


    @Inject
    public TimePlayedOverlay(TimePlayedPlugin plugin, TimePlayedConfig config, Client client, TooltipManager tooltipManager) {
        super(plugin);
        setPosition(OverlayPosition.ABOVE_CHATBOX_RIGHT);
        setLayer(OverlayLayer.ABOVE_SCENE);
        this.plugin = plugin;  //set plugin field to plugin object given as input
        this.config = config;
        this.client = client;
        this.tooltipManager = tooltipManager;
        getMenuEntries().add(new OverlayMenuEntry(RUNELITE_OVERLAY_CONFIG, OPTION_CONFIGURE, "Time played overlay"));
    }

    //render method
    @Override
    public Dimension render(Graphics2D graphics) {
        panelComponent.getChildren().clear();
        String timeStr = buildTimeString();

        panelComponent.getChildren().add(LineComponent.builder()
                    .left("Played:")
                    .right(timeStr)
                    .build());

        return panelComponent.render(graphics);
    }

    public String buildTimeString() {
        int timePlayed = client.getVarcIntValue(526);
        int days = timePlayed / 1440;
        int hours = (timePlayed % 1440) / 60;
        int minutes = timePlayed % 60;
        return Integer.toString(days) + "d" +  Integer.toString(hours) + "h" + Integer.toString(minutes) + "m";

    }
}