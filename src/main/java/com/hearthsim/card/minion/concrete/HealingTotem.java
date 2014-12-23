package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;

public class HealingTotem extends Minion {

	private static final boolean HERO_TARGETABLE = true;
	private static final byte SPELL_DAMAGE = 0;
	
	public HealingTotem() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;

        this.tribe = MinionTribe.TOTEM;
	}
	
	/**
	 * Called at the end of a turn
	 * 
	 * At the end of your turn, restore 1 Health to all friendly minions
	 * 
	 */
	@Override
	public HearthTreeNode endTurn(PlayerSide thisMinionPlayerIndex, HearthTreeNode boardModel, Deck deckPlayer0, Deck deckPlayer1) throws HSException {
		HearthTreeNode tmpState = super.endTurn(thisMinionPlayerIndex, boardModel, deckPlayer0, deckPlayer1);
		if (isWaitingPlayer(thisMinionPlayerIndex))
			return tmpState;
		
	
		for (Minion minion : PlayerSide.CURRENT_PLAYER.getPlayer(tmpState).getMinions()) {
			tmpState = minion.takeHeal((byte)1, PlayerSide.CURRENT_PLAYER, tmpState, deckPlayer0, deckPlayer1);
		}
		
		if (tmpState instanceof CardDrawNode) {
			tmpState = ((CardDrawNode) tmpState).finishAllEffects(deckPlayer0, deckPlayer1);
		}
		
		return tmpState;
	}
}
