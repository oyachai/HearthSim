package com.hearthsim.card.weapon.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.weapon.WeaponCard;
import com.hearthsim.event.EffectMinionAction;
import com.hearthsim.event.FilterUntargetedDeathrattle;
import com.hearthsim.event.deathrattle.DeathrattleEffectRandomMinion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Powermace extends WeaponCard {
    private final static EffectMinionAction<Card> powermaceEffect = new EffectMinionAction<Card>() {
        @Override
        public HearthTreeNode applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) throws HSException {
            Minion targetCharacter = boardState.data_.modelForSide(targetSide).getCharacter(targetCharacterIndex);
            targetCharacter.addAttack((byte) 2);
            targetCharacter.addHealth((byte) 2);
            targetCharacter.addMaxHealth((byte) 2);
            return boardState;
        }
    };

    private final static FilterUntargetedDeathrattle filter = new FilterUntargetedDeathrattle() {
        @Override
        protected boolean canEffectOwnMinions() { return true; }

        @Override
        protected Minion.MinionTribe tribeFilter() { return Minion.MinionTribe.MECH; }
    };

    public Powermace() {
        super();
        this.deathrattleAction_ = new DeathrattleEffectRandomMinion(Powermace.powermaceEffect, Powermace.filter);
    }
}
