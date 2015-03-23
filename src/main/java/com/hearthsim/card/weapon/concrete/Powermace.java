package com.hearthsim.card.weapon.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.weapon.WeaponCard;
import com.hearthsim.event.EffectMinionActionUntargetable;
import com.hearthsim.event.deathrattle.DeathrattleEffectRandomMinion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Powermace extends WeaponCard {
    public Powermace() {
        super();
        this.deathrattleAction_ = new DeathrattleEffectRandomMinion(new PowermaceEffect());
    }

    private class PowermaceEffect extends EffectMinionActionUntargetable {
        protected boolean canEffectOwnMinions() { return true; }

        protected Minion.MinionTribe tribeFilter() { return Minion.MinionTribe.MECH; }

        public HearthTreeNode applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) {
            Minion targetCharacter = boardState.data_.modelForSide(targetSide).getCharacter(targetCharacterIndex);
            targetCharacter.addAttack((byte) 2);
            targetCharacter.addHealth((byte) 2);
            targetCharacter.addMaxHealth((byte) 2);
            return boardState;
        }
    }
}
