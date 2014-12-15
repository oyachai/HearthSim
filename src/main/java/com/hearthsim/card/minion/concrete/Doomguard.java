package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.HearthAction;
import com.hearthsim.util.IdentityLinkedList;
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.util.tree.RandomEffectNode;

public class Doomguard extends Minion implements MinionUntargetableBattlecry {

	private static final boolean HERO_TARGETABLE = true;
	private static final byte SPELL_DAMAGE = 0;
	
	public Doomguard() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;

	}
	
	/**
	 * Battlecry: Discard 2 random cards
	 */
	@Override
	public HearthTreeNode useUntargetableBattlecry_core(
			Minion minionPlacementTarget,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly
		) throws HSException
	{
		HearthTreeNode toRet = boardState;
		
		if (singleRealizationOnly) {
			IdentityLinkedList<Card> hand = PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getHand();
			for(int indx = 0; indx < 2; ++indx) {
				if (hand.size() > 0) {
					Card targetCard = hand.get((int)(Math.random() * hand.size()));
					toRet.data_.removeCard_hand(targetCard);
				}
			}
		} else {
			int placementTargetIndex = minionPlacementTarget instanceof Hero ? 0 : PlayerSide.CURRENT_PLAYER.getPlayer(boardState).getMinions().indexOf(minionPlacementTarget) + 1;
			int thisMinionIndex = PlayerSide.CURRENT_PLAYER.getPlayer(boardState).getMinions().indexOf(this) + 1;
			IdentityLinkedList<Card> hand = toRet.data_.getCurrentPlayerHand();
			if (hand.size() == 0) {
				return toRet;
			}
			toRet = new RandomEffectNode(boardState, new HearthAction(HearthAction.Verb.UNTARGETABLE_BATTLECRY, PlayerSide.CURRENT_PLAYER, thisMinionIndex, PlayerSide.CURRENT_PLAYER, placementTargetIndex));
			for (int indx0 = 0; indx0 < hand.size(); ++indx0) {
				if (hand.size() > 1) {
					for (int indx1 = indx0+1; indx1 < hand.size(); ++indx1) {
						HearthTreeNode cNode = new HearthTreeNode(toRet.data_.deepCopy());
						cNode.data_.removeCard_hand(cNode.data_.getCurrentPlayerCardHand(indx1));
						cNode.data_.removeCard_hand(cNode.data_.getCurrentPlayerCardHand(indx0)); //indx1 > indx0
						toRet.addChild(cNode);
					}
				} else {
					HearthTreeNode cNode = new HearthTreeNode(toRet.data_.deepCopy());
					cNode.data_.removeCard_hand(cNode.data_.getCurrentPlayerCardHand(indx0));					
					toRet.addChild(cNode);
				}
			}
		}
		return toRet;		
	}
}
