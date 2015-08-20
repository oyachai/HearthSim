package com.hearthsim.card.blackrockmountain.minion.common;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionDamagedInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterSummonNew;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class ImpGangBoss extends Minion implements MinionDamagedInterface {

    private static final EffectCharacter<Card> effect = new EffectCharacterSummonNew<Card>(Imp.class);

    public ImpGangBoss() {
        super();
    }

    @Override
    public HearthTreeNode minionDamagedEvent(PlayerSide thisMinionPlayerSide, PlayerSide damagedPlayerSide, Minion damagedMinion, HearthTreeNode boardState) {
        if (damagedMinion == this) {
            boardState = ImpGangBoss.effect.applyEffect(thisMinionPlayerSide,
                boardState.data_.modelForSide(thisMinionPlayerSide).getIndexForCharacter(damagedMinion), boardState);
        }
        return boardState;
    }
}
