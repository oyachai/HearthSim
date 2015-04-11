package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterDraw;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class GnomishInventor extends Minion implements MinionBattlecryInterface {

    private static final CardEffectCharacter effect = new CardEffectCharacterDraw(1);

    public GnomishInventor() {
        super();
    }

    /**
     * Battlecry: Draw one card
     */
    @Override
    public CardEffectCharacter<Minion> getBattlecryEffect() {
        return GnomishInventor.effect;
    }
}
