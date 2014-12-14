package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class AncientOfWar extends Minion {

	private static final boolean HERO_TARGETABLE = true;
	private static final byte SPELL_DAMAGE = 0;
	
	public AncientOfWar() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;

	}

	/**
	 * 
	 * Choose one: +5 health and Taunt; or +5 Attack
	 * 
	 *
     *
     * @param side
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     *
     * @return The boardState is manipulated and returned
	 */
	@Override
	public HearthTreeNode use_core(
			PlayerSide side,
			Minion targetMinion,
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
				HearthTreeNode newState = toRet.addChild(new HearthTreeNode(toRet.data_.deepCopy()));
				PlayerSide.CURRENT_PLAYER.getPlayer(newState).getMinions().get(thisMinionIndex).setTaunt(true);
				PlayerSide.CURRENT_PLAYER.getPlayer(newState).getMinions().get(thisMinionIndex).setMaxHealth((byte)10);
				PlayerSide.CURRENT_PLAYER.getPlayer(newState).getMinions().get(thisMinionIndex).setHealth((byte)10);
			}
			{
				HearthTreeNode newState = toRet.addChild(new HearthTreeNode(toRet.data_.deepCopy()));
				PlayerSide.CURRENT_PLAYER.getPlayer(newState).getMinions().get(thisMinionIndex).setAttack((byte)10);
			}
		}
		return toRet;
	}
}
