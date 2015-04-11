package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectHero;
import com.hearthsim.event.effect.CardEffectHeroWeaponDestroy;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class AcidicSwampOoze extends Minion implements MinionBattlecryInterface {

    private static final CardEffectHero effect = CardEffectHeroWeaponDestroy.DESTROY;

    /**
     * Battlecry: Destroy your opponent's weapon
     */
    public AcidicSwampOoze() {
        super();
    }

    @Override
    public CharacterFilter getBattlecryFilter() {
        return CharacterFilter.OPPONENT;
    }

    @Override
    public CardEffectCharacter<Minion> getBattlecryEffect() {
        return AcidicSwampOoze.effect;
    }
}
