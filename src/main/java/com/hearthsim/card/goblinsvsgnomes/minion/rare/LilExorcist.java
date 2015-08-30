package com.hearthsim.card.goblinsvsgnomes.minion.rare;

import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class LilExorcist extends Minion implements MinionBattlecryInterface {

    private static final FilterCharacter filter = new FilterCharacter() {

        @Override
        protected boolean includeEnemyMinions() {
            return true;
        }

        @Override
        protected boolean excludeSource() {
            return true;
        }

        @Override
        public boolean targetMatches(PlayerSide originSide, Card origin, PlayerSide targetSide, Minion targetCharacter, BoardModel board) {
            if (!super.targetMatches(originSide, origin, targetSide, targetCharacter, board)) {
                return false;
            }

            if (!targetCharacter.hasDeathrattle()) {
                return false;
            }

            return true;
        }


    };

    public LilExorcist() {
        super();
    }

    @Override
    public EffectCharacter<Minion> getBattlecryEffect() {
        return (PlayerSide targetSide, CharacterIndex minionPlacementIndex, HearthTreeNode boardState) -> {
            int numBuffs = LilExorcist.filter.countMatchesForBoard(PlayerSide.CURRENT_PLAYER, this, boardState.data_);
            this.addAttack(numBuffs);
            this.addHealth(numBuffs);
            this.addMaxHealth(numBuffs);
            return boardState;
        };
    }
}
