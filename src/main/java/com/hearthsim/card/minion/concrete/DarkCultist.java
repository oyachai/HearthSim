package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.EffectMinionAction;
import com.hearthsim.event.deathrattle.DeathrattleAction;
import com.hearthsim.event.deathrattle.DeathrattleEffectRandomMinion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.util.tree.RandomEffectNode;

import java.util.ArrayList;
import java.util.List;

public class DarkCultist extends Minion {

    public DarkCultist() {
        super();
        deathrattleAction_ = new DeathrattleEffectRandomMinion(new DarkCultistEffect(3));
    }

    private class DarkCultistEffect extends EffectMinionAction {
        private byte effect;

        public DarkCultistEffect(int effect) {
            this.effect = (byte)effect;
        }

        public boolean canEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, Minion targetCharacter) {
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

        public void applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, Minion targetCharacter) {
            targetCharacter.addHealth(this.effect);
            targetCharacter.addMaxHealth(this.effect);
        }
    }
}
