package com.hearthsim.gui;

import javax.swing.*;
import java.awt.*;

public class HSArrowButton extends JButton  {
    private static final long serialVersionUID = 1L;
    public static final int LEFT = 10;
    public static final int RIGHT = 20;
    public static final int UP = 30;
    public static final int DOWN = 40;

    int direction_;

    public HSArrowButton(int direction) {
        super("");
        direction_ = direction;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // turn on anti-alias mode
        Graphics2D antiAlias = (Graphics2D)g;
        antiAlias.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int height = getHeight();
        int width = getWidth();

        int[] tr_x = new int[3];
        int[] tr_y = new int[3];

        if (direction_ == LEFT) {
            tr_x[0] = width;
            tr_x[1] = 0;
            tr_x[2] = width;
            tr_y[0] = 0;
            tr_y[1] = height / 2;
            tr_y[2] = height;
        } else if (direction_ == RIGHT) {
            tr_x[0] = 0;
            tr_x[1] = width;
            tr_x[2] = 0;
            tr_y[0] = 0;
            tr_y[1] = height / 2;
            tr_y[2] = height;
        } else if (direction_ == DOWN) {
            tr_x[0] = 2 * width / 7;
            tr_x[1] = 5 * width / 7;
            tr_x[2] = width / 2;
            tr_y[0] = 3 * height / 7;
            tr_y[1] = 3 * height / 7;
            tr_y[2] = 5 * height / 7;
        } else {
            return;
        }

        g.setColor(this.getBackground());
        g.fillRect(0, 0, width, height);

        g.setColor(this.getForeground());
        g.fillPolygon(new Polygon(tr_x, tr_y, 3));

    }

}
