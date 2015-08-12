package com.hearthsim.card.goblinsvsgnomes.minion.common;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionDamagedInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterBuffDelta;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class FloatingWatcher extends Minion implements MinionDamagedInterface {

    private static final EffectCharacter<Minion> effect = new EffectCharacterBuffDelta<>(2, 2);

    public FloatingWatcher() {
        super();
    }

    @Override
    public HearthTreeNode minionDamagedEvent(PlayerSide thisMinionPlayerSide, PlayerSide damagedPlayerSide, Minion damagedMinion, HearthTreeNode boardState) {
        if (thisMinionPlayerSide != PlayerSide.CURRENT_PLAYER) {
            return boardState;
        }

        if (thisMinionPlayerSide != damagedPlayerSide) {
            return boardState;
        }

        if (damagedMinion != boardState.data_.modelForSide(damagedPlayerSide).getHero()) {
            return boardState;
        }

        boardState = FloatingWatcher.effect.applyEffect(thisMinionPlayerSide, this, boardState);

        return boardState;
    }
}
