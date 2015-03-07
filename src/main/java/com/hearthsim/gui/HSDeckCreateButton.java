package com.hearthsim.gui;

import javax.swing.*;
import java.awt.*;

public class HSDeckCreateButton extends JButton {
    private static final long serialVersionUID = 1L;

    public HSDeckCreateButton() {
        super("");
        enableInputMethods(true);
        setFocusable(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int height = getHeight();
        int width = getWidth();

        // turn on anti-alias mode
        Graphics2D antiAlias = (Graphics2D)g;
        antiAlias.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(this.getBackground());
        g.fillRect(0, 0, width, height);

        g.setColor(this.getForeground());
        g.fillRoundRect(0, 0, width, height / 5 - 1, 2, 2);
        g.fillRoundRect(0, 2 * height / 5, width, height / 5 - 1, 2, 2);
        g.fillRoundRect(0, 4 * height / 5, width, height / 5 - 1, 2, 2);
    }
}
