package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.entity.BaseEntity;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;

public class AncientOfLore extends Minion {

	private static final byte HEAL_AMOUNT = 5;

	private static final boolean HERO_TARGETABLE = true;
	private static final boolean SUMMONED = false;
	private static final boolean TRANSFORMED = false;
	private static final byte SPELL_DAMAGE = 0;
	
	public AncientOfLore() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;
        summoned_ = SUMMONED;
        transformed_ = TRANSFORMED;
	}

	/**
	 * 
	 * Choose one: Draw 2 cards; or Restore 5 health
	 * 
	 *
     *
     * @param side
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     *
     * @return The boardState is manipulated and returned
	 */
	
	public HearthTreeNode use_core(
			PlayerSide side,
			BaseEntity targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly)
		throws HSException
	{
		HearthTreeNode toRet = super.use_core(side, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
		
		if (toRet != null) {
			int thisMinionIndex = PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions().indexOf(this);
			{
				HearthTreeNode newState = toRet.addChild(new CardDrawNode(new HearthTreeNode((BoardModel)toRet.data_.deepCopy()), 2));
			}
			{
				{
					HearthTreeNode newState = new HearthTreeNode((BoardModel)toRet.data_.deepCopy());
					newState = newState.data_.getCurrentPlayerHero().takeHeal(HEAL_AMOUNT, PlayerSide.CURRENT_PLAYER, newState, deckPlayer0, deckPlayer1);
					toRet.addChild(newState);
				}
				
				{
					for (int index = 0; index < PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getNumMinions(); ++index) {
						if (index != thisMinionIndex) {
							HearthTreeNode newState = new HearthTreeNode((BoardModel)toRet.data_.deepCopy());
							newState = PlayerSide.CURRENT_PLAYER.getPlayer(newState).getMinions().get(index).takeHeal(HEAL_AMOUNT, PlayerSide.CURRENT_PLAYER, newState, deckPlayer0, deckPlayer1);
							toRet.addChild(newState);
						}
					}
				}

				{
					HearthTreeNode newState = new HearthTreeNode((BoardModel)toRet.data_.deepCopy());
					newState = newState.data_.getWaitingPlayerHero().takeHeal(HEAL_AMOUNT, PlayerSide.WAITING_PLAYER, newState, deckPlayer0, deckPlayer1);
					toRet.addChild(newState);
				}
				
				{
					for (int index = 0; index < PlayerSide.WAITING_PLAYER.getPlayer(toRet).getNumMinions(); ++index) {
						HearthTreeNode newState = new HearthTreeNode((BoardModel)toRet.data_.deepCopy());
						newState = PlayerSide.WAITING_PLAYER.getPlayer(newState).getMinions().get(index).takeHeal(HEAL_AMOUNT, PlayerSide.WAITING_PLAYER, newState, deckPlayer0, deckPlayer1);
						toRet.addChild(newState);
					}
				}
			}
			return toRet;
		} else {
			return null;
		}
	}
}
