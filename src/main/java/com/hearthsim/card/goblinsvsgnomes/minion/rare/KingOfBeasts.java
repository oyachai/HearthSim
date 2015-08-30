package com.hearthsim.card.goblinsvsgnomes.minion.rare;

import com.hearthsim.card.CharacterIndex;
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
        return (PlayerSide targetSide, CharacterIndex minionPlacementIndex, HearthTreeNode boardState) -> {
            int numBuffs = KingOfBeasts.filter.countMatchesForBoard(PlayerSide.CURRENT_PLAYER, this, boardState.data_);
            this.addAttack(numBuffs);
            return boardState;
        };
    }
}
