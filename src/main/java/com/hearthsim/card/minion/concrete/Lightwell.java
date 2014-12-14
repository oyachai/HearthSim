package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Lightwell extends Minion {

	private static final boolean HERO_TARGETABLE = true;
	private static final byte SPELL_DAMAGE = 0;
	
	public Lightwell() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;

	}

	@Override
	public HearthTreeNode startTurn(PlayerSide thisMinionPlayerIndex, HearthTreeNode boardModel, Deck deckPlayer0, Deck deckPlayer1) throws HSException {
		HearthTreeNode toRet = boardModel;
		if (thisMinionPlayerIndex == PlayerSide.CURRENT_PLAYER) {
			PlayerModel currentPlayer = PlayerSide.CURRENT_PLAYER.getPlayer(toRet);
			
			//check to see if anyone is damaged
			boolean isDamaged = currentPlayer.getHero().getTotalMaxHealth() > currentPlayer.getHero().getTotalHealth();
			for (Minion minion : currentPlayer.getMinions()) {
				isDamaged = isDamaged || minion.getTotalMaxHealth() > minion.getTotalHealth();
			}
			if (!isDamaged)
				return super.startTurn(thisMinionPlayerIndex, toRet, deckPlayer0, deckPlayer1);
			
			Minion targetMinion = toRet.data_.getCurrentPlayerCharacter((int)(Math.random()*(currentPlayer.getNumMinions() + 1)));
			while (targetMinion.getTotalMaxHealth() == targetMinion.getTotalHealth()) {
				targetMinion = toRet.data_.getCurrentPlayerCharacter((int)(Math.random()*(currentPlayer.getNumMinions() + 1)));
			}
			toRet = targetMinion.takeHeal((byte)3, PlayerSide.CURRENT_PLAYER, toRet, deckPlayer0, deckPlayer1);
		}
		return super.startTurn(thisMinionPlayerIndex, toRet, deckPlayer0, deckPlayer1);
	}

}
