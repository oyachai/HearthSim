package com.hearthsim.gui;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.hearthsim.card.ImplementedCardList;
import com.hearthsim.card.ImplementedCardList.ImplementedCard;

public class HSCardSelectionList extends JList<ImplementedCard> {
	
	ImplementedCardList list_;
	ArrayList<ImplementedCard> cards_;
	HSCardList cardListPane_;
	
	boolean editing_;
	
	public HSCardSelectionList() {
		super();
		list_ = new ImplementedCardList();
		editing_ = false;
		
		DefaultListModel<ImplementedCard> model = new DefaultListModel<ImplementedCard>();
		cards_ = list_.getCardList();
		Collections.sort(cards_);
		for (ImplementedCard card : list_.getCardList()) {
			model.addElement(card);
		}
		this.setModel(model);
		this.addMouseListener(new MouseAdapter() {
		    public void mousePressed(MouseEvent evt) {
		        JList list = (JList)evt.getSource();
	        	int index = list.locationToIndex(evt.getPoint());
	        	System.out.println("clicked item " + index + ": " + cards_.get(index).name_);
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
	
	class CardSelectionCellRenderer extends JLabel implements ListCellRenderer {

		public CardSelectionCellRenderer() {
			setOpaque(false);
			setIconTextGap(12);
		}
		
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			ImplementedCardList.ImplementedCard entry = (ImplementedCardList.ImplementedCard) value;
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
