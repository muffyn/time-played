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

    public int minutes = 0;
    public int seconds = 0;


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
        String leftStr = buildLeftString();
        String rightStr = buildRightString();
        Font font = new Font(config.font(), Font.PLAIN, config.fontSize());
        Color color = config.fontColor();
        Color transparent = new Color(0, 0, 0, 0);

        panelComponent.getChildren().add(LineComponent.builder()
                     .right(leftStr + rightStr)
                     .rightFont(font)
                     .rightColor(color)
                     .build());

        panelComponent.setBackgroundColor(transparent);

        return panelComponent.render(graphics);
    }

    public String buildLeftString() {
        //int days = time / 1440;
        int hrs = minutes / 60;
        int mins = minutes % 60;
        return String.format("%d:%02d", hrs, mins);

    }

    public String buildRightString() {
        int secs = seconds / 10;
        int ms = seconds % 10;
        if (config.displayMs()) {
            return String.format(":%02d.%01d", secs, ms);
        }
        return String.format(":%02d", secs);

    }

}