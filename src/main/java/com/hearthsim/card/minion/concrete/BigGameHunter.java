package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.card.minion.MinionTargetableBattlecry;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterTargetedBattlecry;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class BigGameHunter extends Minion implements MinionBattlecryInterface {

    /**
     * Battlecry: Destroy a minion with an Attack of 7 or more
     */
    private final static CharacterFilterTargetedBattlecry filter = new CharacterFilterTargetedBattlecry() {
        protected boolean includeEnemyMinions() { return true; }
        protected boolean includeOwnMinions() { return true; }

        @Override
        public boolean targetMatches(PlayerSide originSide, Card origin, PlayerSide targetSide, Minion targetCharacter, BoardModel board) {
            if (!super.targetMatches(originSide, origin, targetSide, targetCharacter, board)) {
                return false;
            }

            return targetCharacter.getTotalAttack() >= 7;
        }
    };

    private final static CardEffectCharacter battlecryAction = CardEffectCharacter.DESTROY;

    public BigGameHunter() {
        super();
    }

    @Override
    public CharacterFilter getBattlecryFilter() {
        return BigGameHunter.filter;
    }

    @Override
    public CardEffectCharacter getBattlecryEffect() {
        return BigGameHunter.battlecryAction;
    }
}
