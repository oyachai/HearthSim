package com.hearthsim.card.weapon.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.weapon.WeaponCard;
import com.hearthsim.event.EffectMinionAction;
import com.hearthsim.event.deathrattle.DeathrattleEffectRandomMinion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class Powermace extends WeaponCard {
    public Powermace() {
        super();
        this.deathrattleAction_ = new DeathrattleEffectRandomMinion(new PowermaceEffect());
    }

    private class PowermaceEffect extends EffectMinionAction {
        protected boolean canEffectOwnMinions() { return true; }

        public boolean canEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, Minion targetCharacter, BoardModel board) {
            if (!super.canEffect(originSide, origin, targetSide, targetCharacter, board)) {
                return false;
            }

            if (targetCharacter.getTribe() != Minion.MinionTribe.MECH) {
                return false;
            }

            return true;
        }

        public void applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, BoardModel board) {
            Minion targetCharacter = board.modelForSide(targetSide).getCharacter(targetCharacterIndex);
            targetCharacter.addAttack((byte) 2);
            targetCharacter.addHealth((byte) 2);
            targetCharacter.addMaxHealth((byte) 2);
        }
    }
}
