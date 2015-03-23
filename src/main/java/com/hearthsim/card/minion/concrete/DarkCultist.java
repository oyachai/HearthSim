package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.EffectMinionActionUntargetable;
import com.hearthsim.event.deathrattle.DeathrattleEffectRandomMinion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class DarkCultist extends Minion {

    public DarkCultist() {
        super();
        deathrattleAction_ = new DeathrattleEffectRandomMinion(new DarkCultistEffect(3));
    }

    private class DarkCultistEffect extends EffectMinionActionUntargetable {
        private byte effect;

        public DarkCultistEffect(int effect) {
            this.effect = (byte)effect;
        }

        public boolean canEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, Minion targetCharacter, BoardModel board) {
            if (targetSide != originSide) {
                return false;
            }

            if (targetCharacter == origin) {
                return false;
            }

            if (targetCharacter.isHero()) {
                return false;
            }

            return true;
        }

        public void applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, BoardModel board) {
            Minion targetCharacter = board.modelForSide(targetSide).getCharacter(targetCharacterIndex);
            targetCharacter.addHealth(this.effect);
            targetCharacter.addMaxHealth(this.effect);
        }
    }
}
