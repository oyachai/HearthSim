package com.hearthsim.event.deathrattle;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.event.effect.EffectHero;
import com.hearthsim.event.effect.EffectHeroAddSparePart;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

/**
 * Created by oyachai on 8/11/15.
 */
public class DeathrattlePutSpareParts extends DeathrattleAction {

    private static EffectHero effect = new EffectHeroAddSparePart<>();
    public DeathrattlePutSpareParts() {}

    @Override
    public HearthTreeNode performAction(CharacterIndex originIndex,
                                        PlayerSide playerSide,
                                        HearthTreeNode boardState) {
        HearthTreeNode toRet = super.performAction(originIndex, playerSide, boardState);
        if (toRet == null)
            return null;

        return effect.applyEffect(playerSide, boardState);
    }

}
