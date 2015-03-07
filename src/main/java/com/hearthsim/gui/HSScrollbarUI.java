package com.hearthsim.gui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class HSScrollbarUI extends BasicScrollBarUI {

    private static final Color LIGHTER_BACKGROUND_COLOR = new Color(64, 64, 64);
    private static final Color BACKGROUND_COLOR = new Color(32, 32, 32);

    @Override
    protected JButton createDecreaseButton(int orientation) {
        JButton btnL = new JButton("");
        btnL.setPreferredSize(new Dimension(0, 0));
        return btnL;
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        JButton btnL = new JButton("");
        btnL.setPreferredSize(new Dimension(0, 0));
        return btnL;
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        g.setColor(LIGHTER_BACKGROUND_COLOR);
        g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(BACKGROUND_COLOR);
        g.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 20, 20);
    }





}
