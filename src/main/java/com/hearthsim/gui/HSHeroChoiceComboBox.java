package com.hearthsim.gui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class HSHeroChoiceComboBox extends JComboBox<String> {
    private static final long serialVersionUID = 1L;

    Dimension arc_ = new Dimension(2, 2);

    public class HSHeroChoiceRenderer extends JLabel implements ListCellRenderer<String> {
        private static final long serialVersionUID = 1L;

        public HSHeroChoiceRenderer() {
            setOpaque(true);
            setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);
        }

        @Override
        public Component getListCellRendererComponent(
                JList<? extends String> list, String value, int index,
                boolean isSelected, boolean cellHasFocus) {

            this.setText(value);
            this.setForeground(HSColors.TEXT_COLOR);
            if (isSelected) {
                this.setBackground(HSColors.BACKGROUND_COLOR);
            } else {
                this.setBackground(HSColors.LIGHTER_BACKGROUND_COLOR);
            }
            return this;
        }
    }

    class HSHeroChoice extends BasicComboBoxUI {

        @Override
        protected JButton createArrowButton() {
            JButton toRet = new HSArrowButton(HSArrowButton.DOWN);
            toRet.setBackground(HSColors.LIGHTER_BACKGROUND_COLOR);
            toRet.setForeground(HSColors.TEXT_COLOR);
            return toRet;
        }
    }

    public HSHeroChoiceComboBox(String[] items) {
        super(items);
        this.setRenderer(new HSHeroChoiceRenderer());
        this.setMaximumRowCount(10);
        this.setBackground(HSColors.LIGHTER_BACKGROUND_COLOR);
        this.setForeground(HSColors.TEXT_COLOR);
        this.setUI(new HSHeroChoice());
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D antiAlias = (Graphics2D)g;
        antiAlias.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(HSColors.LIGHTER_BACKGROUND_COLOR);
        g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc_.width, arc_.height);

        g.setColor(HSColors.TEXT_COLOR);
        g.setFont(this.getFont());
        FontMetrics fm = g.getFontMetrics();
        Rectangle2D r = fm.getStringBounds((String)this.getSelectedItem(), antiAlias);
        g.drawString((String)this.getSelectedItem(), 10, (getHeight() - (int) r.getHeight()) / 2  + fm.getAscent());
    }

}
