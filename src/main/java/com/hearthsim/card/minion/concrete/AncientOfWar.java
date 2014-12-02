package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.entity.BaseEntity;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class AncientOfWar extends Minion {

	private static final String NAME = "Ancient of War";
	private static final byte MANA_COST = 7;
	private static final byte ATTACK = 5;
	private static final byte HEALTH = 5;
	
	private static final boolean TAUNT = false;
	private static final boolean DIVINE_SHIELD = false;
	private static final boolean WINDFURY = false;
	private static final boolean CHARGE = false;
	
	private static final boolean STEALTHED = false;
	private static final boolean HERO_TARGETABLE = true;
	private static final boolean SUMMONED = false;
	private static final boolean TRANSFORMED = false;
	private static final byte SPELL_DAMAGE = 0;
	
	public AncientOfWar() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;
        summoned_ = SUMMONED;
        transformed_ = TRANSFORMED;
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
				HearthTreeNode newState = toRet.addChild(new HearthTreeNode((BoardModel)toRet.data_.deepCopy()));
				PlayerSide.CURRENT_PLAYER.getPlayer(newState).getMinions().get(thisMinionIndex).setTaunt(true);
				PlayerSide.CURRENT_PLAYER.getPlayer(newState).getMinions().get(thisMinionIndex).setMaxHealth((byte)10);
				PlayerSide.CURRENT_PLAYER.getPlayer(newState).getMinions().get(thisMinionIndex).setHealth((byte)10);
			}
			{
				HearthTreeNode newState = toRet.addChild(new HearthTreeNode((BoardModel)toRet.data_.deepCopy()));
				PlayerSide.CURRENT_PLAYER.getPlayer(newState).getMinions().get(thisMinionIndex).setAttack((byte)10);
			}
		}
		return toRet;
	}
}
