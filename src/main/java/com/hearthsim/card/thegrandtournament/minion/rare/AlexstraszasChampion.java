package com.hearthsim.card.thegrandtournament.minion.rare;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.conditional.Conditional;
import com.hearthsim.event.effect.conditional.EffectCharacterConditional;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class AlexstraszasChampion extends Minion implements MinionBattlecryInterface {

    private EffectCharacter<Minion> effect;

    public AlexstraszasChampion() {
        super();
    }

    @Override
    public EffectCharacter<Minion> getBattlecryEffect() {
        if (effect == null) {
            effect = new EffectCharacterConditional<Minion>(
                (PlayerSide targetSide,CharacterIndex minionPlacementIndex, HearthTreeNode boardState) -> {
                    this.addAttack((byte) 1);
                    this.setCharge(true);
                    return boardState;
                },
                Conditional.HOLDING_DRAGON,
                PlayerSide.CURRENT_PLAYER
            );
        }
        return effect;
    }
}
