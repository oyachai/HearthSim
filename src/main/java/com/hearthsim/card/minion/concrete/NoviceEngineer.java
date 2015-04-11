package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterDraw;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class NoviceEngineer extends Minion implements MinionBattlecryInterface {

    private static final CardEffectCharacter<Minion> effect = new CardEffectCharacterDraw<>(1);

    public NoviceEngineer() {
        super();
    }

    /**
     * Battlecry: Draw one card
     */
    @Override
    public CardEffectCharacter<Minion> getBattlecryEffect() {
        return NoviceEngineer.effect;
    }
}
