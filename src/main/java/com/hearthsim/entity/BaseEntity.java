package com.hearthsim.entity;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.ImplementedCardList;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.DeepCopyable;
import com.hearthsim.util.tree.HearthTreeNode;

public abstract class BaseEntity extends Card implements DeepCopyable
{
	protected byte health_;
	protected byte attack_;
	
	// Empty placeholder constructor
	public BaseEntity()
	{
        ImplementedCardList cardList = ImplementedCardList.getInstance();
        ImplementedCardList.ImplementedCard implementedCard = cardList.getCardForClass(this.getClass());
        if (implementedCard!=null)
        {
        	attack_ = (byte) implementedCard.attack_;
        	health_ = (byte) implementedCard.health_;
        }
	}
	
	public BaseEntity(String name, byte mana, boolean hasBeenUsed, boolean isInHand)
	{
		super(name,mana,hasBeenUsed,isInHand);
	}
	
	public abstract HearthTreeNode takeDamage(
			byte damage,
			PlayerSide attackPlayerSide,
			PlayerSide thisPlayerSide,
			HearthTreeNode boardState,
			Deck deckPlayer0, 
			Deck deckPlayer1,
			boolean isSpellDamage,
			boolean handleMinionDeath)
		throws HSException;
}