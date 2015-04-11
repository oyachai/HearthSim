package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterDraw;
import com.hearthsim.event.effect.CardEffectCharacterHeal;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class GuardianOfKings extends Minion implements MinionBattlecryInterface {

    private static final CardEffectCharacter effect = new CardEffectCharacterHeal(6);

    public GuardianOfKings() {
        super();
    }

    /**
     * Battlecry: Restore 6 Health to your Hero
     */
    @Override
    public CardEffectCharacter getBattlecryEffect() {
        return GuardianOfKings.effect;
    }
}
