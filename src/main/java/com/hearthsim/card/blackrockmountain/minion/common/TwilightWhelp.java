package com.hearthsim.card.blackrockmountain.minion.common;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.conditional.Conditional;
import com.hearthsim.event.effect.conditional.EffectCharacterConditional;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TwilightWhelp extends Minion implements MinionBattlecryInterface {

    private EffectCharacter<Minion> effect;


    public TwilightWhelp() {
        super();
    }

    @Override
    public EffectCharacter<Minion> getBattlecryEffect() {
        if (effect == null) {
            effect = new EffectCharacterConditional<Minion>(
                (PlayerSide targetSide,CharacterIndex minionPlacementIndex, HearthTreeNode boardState) -> {
                    this.addHealth((byte) 2);
                    this.addMaxHealth((byte) 2);
                    return boardState;
                },
                Conditional.HOLDING_DRAGON,
                PlayerSide.CURRENT_PLAYER
            );
        }
        return effect;
    }
}
