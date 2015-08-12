package com.hearthsim.card.classic.minion.legendary;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.classic.minion.common.Whelp;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Onyxia extends Minion implements MinionBattlecryInterface {

    public Onyxia() {
        super();
    }

    @Override
    public EffectCharacter<Minion> getBattlecryEffect() {
        return (PlayerSide targetSide, CharacterIndex minionPlacementIndex, HearthTreeNode boardState) -> {
            HearthTreeNode toRet = boardState;
            PlayerModel currentPlayer = toRet.data_.getCurrentPlayer();
            while (!currentPlayer.isBoardFull()) {
                Minion placementTarget;
                if (currentPlayer.getNumMinions() % 2 == 0) {
                    placementTarget = toRet.data_.getCurrentPlayer().getCharacter(currentPlayer.getIndexForCharacter(this).indexToLeft());
                } else {
                    placementTarget = this;
                }
                toRet = new Whelp().summonMinion(PlayerSide.CURRENT_PLAYER, placementTarget, toRet, false);
            }
            return toRet;
        };
    }
}
