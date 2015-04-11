package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterTargetedBattlecry;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class CruelTaskmaster extends Minion implements MinionBattlecryInterface {

    /**
     * Battlecry: Deal 1 damage to a minion and give it +2 Attack
     */
    private final static CharacterFilterTargetedBattlecry filter = new CharacterFilterTargetedBattlecry() {
        protected boolean includeEnemyMinions() { return true; }
        protected boolean includeOwnMinions() { return true; }
    };

    private final static CardEffectCharacter battlecryAction = new CardEffectCharacter() {
        @Override
        public HearthTreeNode applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) {
            Minion targetMinion = boardState.data_.modelForSide(targetSide).getCharacter(targetCharacterIndex);
            HearthTreeNode toRet = boardState;
            targetMinion.setAttack((byte)(targetMinion.getAttack() + 2));
            toRet = targetMinion.takeDamageAndNotify((byte) 1, PlayerSide.CURRENT_PLAYER, targetSide, toRet, false, true);
            return toRet;
        }
    };

    public CruelTaskmaster() {
        super();
    }

    @Override
    public CharacterFilter getBattlecryFilter() {
        return CruelTaskmaster.filter;
    }

    @Override
    public CardEffectCharacter getBattlecryEffect() {
        return CruelTaskmaster.battlecryAction;
    }
}
