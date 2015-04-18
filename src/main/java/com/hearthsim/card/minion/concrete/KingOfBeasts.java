package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class KingOfBeasts extends Minion implements MinionBattlecryInterface {

    private static final FilterCharacter filter = new FilterCharacter() {

        @Override
        protected boolean includeOwnMinions() {
            return true;
        }

        @Override
        protected boolean excludeSource() {
            return true;
        }

        @Override
        protected MinionTribe tribeFilter() {
            return MinionTribe.BEAST;
        }
    };

    public KingOfBeasts() {
        super();
    }

    /**
     * Battlecry: gain +1/+0 for each friendly beast on the battlefield
     */
    @Override
    public EffectCharacter<Minion> getBattlecryEffect() {
        return (PlayerSide originSide, Minion origin, PlayerSide targetSide, int minionPlacementIndex, HearthTreeNode boardState) -> {
            int numBuffs = KingOfBeasts.filter.countMatchesForBoard(originSide, this, boardState.data_);
            this.setAttack((byte) (this.getAttack() + numBuffs));
            return boardState;
        };
    }
}