package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionDamagedInterface;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterDraw;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class AcolyteOfPain extends Minion implements MinionDamagedInterface {

    private static final CardEffectCharacter effect = new CardEffectCharacterDraw(1);

    public AcolyteOfPain() {
        super();
    }

    /**
     * Draw a card whenever this minion takes damage
     * */
    @Override
    public HearthTreeNode minionDamagedEvent(PlayerSide thisMinionPlayerSide, PlayerSide damagedPlayerSide, Minion damagedMinion, HearthTreeNode boardState) {
        if (damagedMinion == this) {
            boardState = AcolyteOfPain.effect.applyEffect(thisMinionPlayerSide, this, thisMinionPlayerSide, 0, boardState);
        }
        return boardState;
    }
}
