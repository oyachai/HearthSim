package com.hearthsim.card.goblinsvsgnomes.minion.rare;

import com.hearthsim.card.Card;
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

    /**
     * Battlecry: gain +1/+0 for each friendly beast on the battlefield
     */
    @Override
    public EffectCharacter<Minion> getBattlecryEffect() {
        return (PlayerSide originSide, Minion origin, PlayerSide targetSide, int minionPlacementIndex, HearthTreeNode boardState) -> {
            int numBuffs = LilExorcist.filter.countMatchesForBoard(originSide, this, boardState.data_);
            this.setAttack((byte) (this.getAttack() + numBuffs));
            this.setHealth((byte) (this.getHealth() + numBuffs));
            this.setMaxHealth((byte) (this.getMaxHealth() + numBuffs));
            return boardState;
        };
    }
}
