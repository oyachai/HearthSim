package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterTargetedBattlecry;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class CrazedAlchemist extends Minion implements MinionBattlecryInterface {

    /**
     * Battlecry: Swap the Attack and Health of a minion
     */
    private final static CharacterFilterTargetedBattlecry filter = new CharacterFilterTargetedBattlecry() {
        protected boolean includeEnemyMinions() { return true; }
        protected boolean includeOwnMinions() { return true; }
    };

    private final static CardEffectCharacter battlecryAction = (originSide, origin, targetSide, targetCharacterIndex, boardState) -> {
        Minion targetMinion = boardState.data_.modelForSide(targetSide).getCharacter(targetCharacterIndex);
        byte newHealth = targetMinion.getTotalAttack();
        byte newAttack = targetMinion.getTotalHealth();
        targetMinion.setAttack(newAttack);
        targetMinion.setHealth(newHealth);
        return boardState;
    };

    public CrazedAlchemist() {
        super();
    }

    @Override
    public CharacterFilter getBattlecryFilter() {
        return CrazedAlchemist.filter;
    }

    @Override
    public CardEffectCharacter getBattlecryEffect() {
        return CrazedAlchemist.battlecryAction;
    }
}
