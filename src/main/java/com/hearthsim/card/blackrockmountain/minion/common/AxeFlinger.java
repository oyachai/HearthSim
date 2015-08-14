package com.hearthsim.card.blackrockmountain.minion.common;

import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionDamagedInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterDamage;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class AxeFlinger extends Minion implements MinionDamagedInterface {

    private static final EffectCharacter<Card> effect = new EffectCharacterDamage<>(2);

    public AxeFlinger() {
        super();
    }

    @Override
    public HearthTreeNode minionDamagedEvent(PlayerSide thisMinionPlayerSide, PlayerSide damagedPlayerSide, Minion damagedMinion, HearthTreeNode boardState) {
        if (damagedMinion == this) {
            boardState = AxeFlinger.effect.applyEffect(thisMinionPlayerSide.getOtherPlayer(), CharacterIndex.HERO, boardState);
        }
        return boardState;
    }
}
