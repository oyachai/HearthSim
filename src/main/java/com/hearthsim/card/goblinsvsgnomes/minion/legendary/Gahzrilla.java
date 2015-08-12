package com.hearthsim.card.goblinsvsgnomes.minion.legendary;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionDamagedInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Gahzrilla extends Minion implements MinionDamagedInterface {

    private static final EffectCharacter<Minion> effect = (targetSide, targetCharacterIndex, boardState) -> {
        Minion targetCharacter = boardState.data_.getCharacter(targetSide, targetCharacterIndex);
        targetCharacter.setAttack((byte) (targetCharacter.getAttack() * 2));
        return boardState;
    };

    public Gahzrilla() {
        super();
    }

    @Override
    public HearthTreeNode minionDamagedEvent(PlayerSide thisMinionPlayerSide, PlayerSide damagedPlayerSide, Minion damagedMinion, HearthTreeNode boardState) {
        if (this != damagedMinion) {
            return boardState;
        }
        boardState = Gahzrilla.effect.applyEffect(thisMinionPlayerSide, this, boardState);
        return boardState;
    }
}
