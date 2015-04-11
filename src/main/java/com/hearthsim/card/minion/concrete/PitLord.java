package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterDamage;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class PitLord extends Minion implements MinionBattlecryInterface {

    private final static CardEffectCharacter effect = new CardEffectCharacterDamage(5);


    public PitLord() {
        super();
    }

    /**
     * Battlecry: Deal 5 damage to your hero
     */
    @Override
    public CardEffectCharacter getBattlecryEffect() {
        return PitLord.effect;
    }
}
