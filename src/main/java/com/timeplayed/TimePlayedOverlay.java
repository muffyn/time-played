package com.timeplayed;

import net.runelite.api.Client;
import net.runelite.api.widgets.ComponentID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.ComponentConstants;
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
    public long lastTick = 0;
    public int msOffset = 0;


    @Inject
    public TimePlayedOverlay(TimePlayedPlugin plugin, TimePlayedConfig config, Client client, TooltipManager tooltipManager) {
        super(plugin);
        setPosition(OverlayPosition.ABOVE_CHATBOX_RIGHT);
        if (config.ontop()) {
            setLayer(OverlayLayer.ABOVE_WIDGETS);
        } else {
            setLayer(OverlayLayer.ABOVE_SCENE);
        }

        this.plugin = plugin;
        this.config = config;
        this.client = client;
        this.tooltipManager = tooltipManager;

        setMinimumSize(10);
        panelComponent.setBorder(new Rectangle(4, 4));

        getMenuEntries().add(new OverlayMenuEntry(RUNELITE_OVERLAY_CONFIG, OPTION_CONFIGURE, "Time played overlay"));
    }

    //render method
    @Override
    public Dimension render(Graphics2D graphics) {
        panelComponent.getChildren().clear();


        String leftStr = buildLeftString();
        String rightStr = buildRightString();
        String str = leftStr + rightStr;

        if (config.showOnReportButton()) {
            Widget reportButton = client.getWidget(ComponentID.CHATBOX_REPORT_TEXT);
            if (reportButton != null) {
                reportButton.setText(str);
            }
            return null;
        }

        if (config.defStyle()) {
            FontMetrics metrics = graphics.getFontMetrics(FontManager.getRunescapeFont());
            int hgt = metrics.getHeight();
            int adv = metrics.stringWidth(leftStr + rightStr);
            panelComponent.getChildren().add(LineComponent.builder()
                    .right(str)
                    .build());

            Color bgcolor = ComponentConstants.STANDARD_BACKGROUND_COLOR;
            panelComponent.setBackgroundColor(bgcolor);

            panelComponent.setPreferredSize(new Dimension(adv + 8, hgt + 8));

        } else {
            Font font = new Font(config.font(), Font.PLAIN, config.fontSize());
            Color color = config.fontColor();
            Color bgcolor = config.bgColor();
            FontMetrics metrics = graphics.getFontMetrics(font);
            int hgt = metrics.getHeight();
            int adv = metrics.stringWidth(leftStr + rightStr);

            panelComponent.getChildren().add(LineComponent.builder()
                    .right(leftStr + rightStr)
                    .rightFont(font)
                    .rightColor(color)
                    .build());

            panelComponent.setBackgroundColor(bgcolor);

            panelComponent.setPreferredSize(new Dimension(adv + 8, hgt + 8));

        }

        return panelComponent.render(graphics);
    }

    public String buildLeftString() {
        int offsetMinutes = minutes;
        if (config.useOffset() && (offsetMinutes - config.offset()) >= 0) {
            offsetMinutes -= config.offset();
        }

        if (config.displayDays()) {
            int days = offsetMinutes / 1440;
            int hrs = (offsetMinutes % 1440) / 60;
            int mins = offsetMinutes % 60;
            return String.format("%01dd%02d:%02d", days, hrs, mins);
        } else {
            int hrs = offsetMinutes / 60;
            int mins = offsetMinutes % 60;
            return String.format("%d:%02d", hrs, mins);
        }

    }

    public String buildRightString() {
        int effectiveSec;
        if (config.smoothTimer()) {
            if (msOffset >= 6) {
                msOffset = 5;
            }
            effectiveSec = seconds + msOffset;

        } else {
            effectiveSec = seconds;
        }
        if (config.displayMs()) {
            int secs = effectiveSec / 10;
            int ms = effectiveSec % 10;
            return String.format(":%02d.%01d", secs, ms);
        } else {
            int secs = effectiveSec / 10;
            return String.format(":%02d", secs);
        }


    }

}