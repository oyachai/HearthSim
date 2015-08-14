package com.hearthsim.card.blackrockmountain.minion.rare;

import com.hearthsim.card.Card;
import com.hearthsim.card.blackrockmountain.minion.common.BlackWhelp;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionDamagedInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterSummonNew;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

/**
 * Created by oyachai on 8/14/15.
 */
public class DragonEgg extends Minion implements MinionDamagedInterface {

    private static final EffectCharacter<Card> effect = new EffectCharacterSummonNew<Card>(BlackWhelp.class);

    public DragonEgg() {
        super();
    }

    @Override
    public HearthTreeNode minionDamagedEvent(PlayerSide thisMinionPlayerSide, PlayerSide damagedPlayerSide, Minion damagedMinion, HearthTreeNode boardState) {
        if (damagedMinion == this) {
            boardState = DragonEgg.effect.applyEffect(thisMinionPlayerSide,
                boardState.data_.modelForSide(thisMinionPlayerSide).getIndexForCharacter(damagedMinion), boardState);
        }
        return boardState;
    }
}
