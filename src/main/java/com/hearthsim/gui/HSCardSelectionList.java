package com.hearthsim.gui;

import com.hearthsim.card.ImplementedCardList;
import com.hearthsim.card.ImplementedCardList.ImplementedCard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;

public class HSCardSelectionList extends JList<ImplementedCard> {

    private static final long serialVersionUID = 1L;

    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    private ImplementedCardList list_;
    private ArrayList<ImplementedCard> cards_;
    private HSCardList cardListPane_;

    boolean editing_;

    public HSCardSelectionList() {
        super();
        list_ = new ImplementedCardList();
        editing_ = false;

        DefaultListModel<ImplementedCard> model = new DefaultListModel<>();
        cards_ = new ArrayList<>();
        for (ImplementedCard card : list_.getCardList()) {
            if (!card.isHero && card.collectible)
                cards_.add(card);
        }
        Collections.sort(cards_);
        for (ImplementedCard card : cards_) {
            model.addElement(card);
        }
        this.setModel(model);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                JList<?> list = (JList<?>)evt.getSource();
                int index = list.locationToIndex(evt.getPoint());
                log.debug("clicked item " + index + ": " + cards_.get(index).name_);
                if (editing_ && cardListPane_ != null) {
                    ((SortedListModel<ImplementedCard>) cardListPane_.getModel()).addElement(cards_.get(index));
                }
                cardListPane_.repaint();
            }
        });
        this.setCellRenderer(new CardSelectionCellRenderer());
    }

    public boolean getEditing() {
        return editing_;
    }

    public void setEditing(boolean value) {
        editing_ = value;
        cardListPane_.setEditing(value);
    }

    public void setCardListPane(HSCardList cardListPane) {
        cardListPane_ = cardListPane;
    }

    public static class CardSelectionCellRenderer extends JPanel implements ListCellRenderer<ImplementedCard> {
        private static final long serialVersionUID = 1L;

        JLabel manaLabel_;
        JLabel nameLabel_;
        JLabel textLabel_;
        JLabel attackLabel_;
        JLabel healthLabel_;

        public CardSelectionCellRenderer() {
            setOpaque(false);
            this.setPreferredSize(new Dimension(760, 18));
            FlowLayout flowLayout = (FlowLayout) this.getLayout();
            flowLayout.setVgap(1);
            flowLayout.setHgap(5);
            manaLabel_ = new JLabel();
            manaLabel_.setPreferredSize(new Dimension(30, 18));
            nameLabel_ = new JLabel();
            nameLabel_.setPreferredSize(new Dimension(180, 18));
            textLabel_ = new JLabel();
            textLabel_.setPreferredSize(new Dimension(450, 18));

            manaLabel_.setHorizontalAlignment(SwingConstants.RIGHT);

            attackLabel_ = new JLabel();
            attackLabel_.setPreferredSize(new Dimension(25, 18));
            healthLabel_ = new JLabel();
            healthLabel_.setPreferredSize(new Dimension(25, 18));

            manaLabel_.setForeground(HSColors.TEXT_COLOR);
            nameLabel_.setForeground(HSColors.TEXT_COLOR);
            attackLabel_.setForeground(HSColors.TEXT_COLOR);
            healthLabel_.setForeground(HSColors.TEXT_COLOR);
            textLabel_.setForeground(HSColors.TEXT_COLOR);

            manaLabel_.setFont(new Font("Helvetica Neue", Font.PLAIN, 12));
            attackLabel_.setFont(new Font("Helvetica Neue", Font.PLAIN, 12));
            healthLabel_.setFont(new Font("Helvetica Neue", Font.PLAIN, 12));
            nameLabel_.setFont(new Font("Helvetica Neue", Font.PLAIN, 12));
            textLabel_.setFont(new Font("Helvetica Neue", Font.PLAIN, 12));

            this.add(manaLabel_);
            this.add(nameLabel_);
            this.add(attackLabel_);
            this.add(healthLabel_);
            this.add(textLabel_);
        }

        public CardSelectionCellRenderer(String manaLabel, String nameLabel, String textLabel, String attackLabel, String healthLabel) {
            this();
            manaLabel_.setText(manaLabel);
            nameLabel_.setText(nameLabel);
            textLabel_.setText(textLabel);
            attackLabel_.setText(attackLabel);
            healthLabel_.setText(healthLabel);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends ImplementedCard> list, ImplementedCard value, int index, boolean isSelected, boolean cellHasFocus) {
            ImplementedCardList.ImplementedCard entry = value;
            if (entry.rarity_!=null){
                switch (entry.rarity_) {
                    case "free":
                        this.setForeground(HSColors.CARD_FREE_COLOR);
                        break;
                    case "common":
                        this.setForeground(HSColors.CARD_COMMON_COLOR);
                        break;
                    case "rare":
                        this.setForeground(HSColors.CARD_RARE_COLOR);
                        break;
                    case "epic":
                        this.setForeground(HSColors.CARD_EPIC_COLOR);
                        break;
                    case "legendary":
                        this.setForeground(HSColors.CARD_LEGENDARY_COLOR);
                        break;
                    default:
                        this.setForeground(HSColors.CARD_FREE_COLOR);
                        break;
                }
            } else {
                this.setForeground(HSColors.CARD_FREE_COLOR);
            }

            manaLabel_.setForeground(this.getForeground());
            nameLabel_.setForeground(this.getForeground());

            manaLabel_.setText("[" + entry.mana_ + "]");
            nameLabel_.setText(entry.name_);
            textLabel_.setText(entry.text_.replaceAll("<.+?>", "").replaceAll("\\$([0-9]+)", "$1").replaceAll("\\#([0-9]+)", "$1"));
            if (entry.attack_ >= 0)
                attackLabel_.setText("" + entry.attack_);
            else
                attackLabel_.setText("");
            if (entry.health_ >= 0)
                healthLabel_.setText("" + entry.health_);
            else
                healthLabel_.setText("");
            return this;
        }
    }

}
