package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterSummon;
import com.hearthsim.event.effect.CardEffectHero;
import com.hearthsim.event.effect.CardEffectHeroWeaponDestroy;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class RazorfenHunter extends Minion implements MinionBattlecryInterface {

    public RazorfenHunter() {
        super();
    }

    /**
     * Battlecry: Summon a 1/1 Boar
     */
    @Override
    public CardEffectCharacter getBattlecryEffect() {
        return new CardEffectCharacterSummon(new Boar());
    }
}
