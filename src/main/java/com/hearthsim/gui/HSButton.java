package com.hearthsim.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class HSButton extends JButton {
    private static final long serialVersionUID = 1L;
    private Dimension arc_;

    public HSButton(String buttonText) {
        super(buttonText);
        arc_ = new Dimension(5, 5);
        this.setPreferredSize(new Dimension(80, 30));
        enableInputMethods(true);
        setFocusable(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // turn on anti-alias mode
        Graphics2D antiAlias = (Graphics2D)g;
        antiAlias.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (this.getModel().isPressed()) {

            float hsbVals[] = Color.RGBtoHSB( this.getBackground().getRed(), this.getBackground().getGreen(), this.getBackground().getBlue(), null );
            Color shadow = Color.getHSBColor( hsbVals[0], hsbVals[1], 0.75f * hsbVals[2] );
            g.setColor(shadow);
        } else {
            g.setColor(this.getBackground());
        }
        g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc_.width, arc_.height);

        g.setFont(this.getFont());
        g.setColor(this.getForeground());
        FontMetrics fm = g.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(this.getText(), antiAlias);
        int x = (this.getWidth() - (int) r.getWidth()) / 2;
        int y = (this.getHeight() - (int) r.getHeight()) / 2 + fm.getAscent();
        g.drawString(this.getText(), x, y);
    }

}
