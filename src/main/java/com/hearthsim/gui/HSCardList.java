package com.hearthsim.gui;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.ImplementedCardList.ImplementedCard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;

public class HSCardList extends JList<ImplementedCard> {

    private static final long serialVersionUID = 1L;

    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    boolean editing_;

    public HSCardList() {
        super();
        editing_ = false;

        SortedListModel<ImplementedCard> model = new SortedListModel<>();
        this.setModel(model);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                JList<?> list = (JList<?>)evt.getSource();
                int index = list.locationToIndex(evt.getPoint());
                String name = HSCardList.this.getModel().getElementAt(index).name_;
                log.debug("clicked item " + index + ": " + name);
                if (editing_ && list.getCellBounds(index, 100000).contains(evt.getPoint())) {
                    ((SortedListModel<ImplementedCard>) HSCardList.this.getModel()).remove(index);
                }
            }
        });
        this.setCellRenderer(new CardCellRenderer());
    }

    public boolean getEditing() {
        return editing_;
    }

    public void setEditing(boolean editing) {
        editing_ = editing;
    }

    public Deck getDeck() {
        ArrayList<Card> cards = new ArrayList<>();
        Iterator<ImplementedCard> iter = ((SortedListModel<ImplementedCard>)this.getModel()).iterator();
        while (iter.hasNext()) {
            ImplementedCard ic = iter.next();
            Constructor<?> ctor;
            try {
                ctor = ic.cardClass_.getConstructor();
                Card card = (Card)ctor.newInstance();
                cards.add(card);
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return new Deck(cards);
    }

    class CardCellRenderer extends JLabel implements ListCellRenderer<ImplementedCard> {
        private static final long serialVersionUID = 1L;

        public CardCellRenderer() {
            setOpaque(false);
            setIconTextGap(12);
            this.setForeground(Color.WHITE);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends ImplementedCard> list, ImplementedCard value, int index, boolean isSelected, boolean cellHasFocus) {
            ImplementedCard entry = value;
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
            setText("[" + entry.mana_ + "] " + entry.name_);
            return this;
        }
    }

}
