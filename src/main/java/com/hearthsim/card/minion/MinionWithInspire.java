package com.hearthsim.card.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.filter.FilterCharacterInterface;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

import java.util.ArrayList;

/**
 * Created by oyachai on 8/15/15.
 */
public abstract class MinionWithInspire<T extends Card> extends Minion implements HeroAbilityUsedInterface<T> {

    public HearthTreeNode heroAbilityUsedEvent(PlayerSide thisMinionPlayerSide, PlayerSide heroAbilityUsedSide,
                                                       Minion heroAbilityTargetCharacter, HearthTreeNode boardState) {
        EffectCharacter<T> effect = this.getInspireEffect();
        FilterCharacterInterface filter = this.getInspireFilter();
        HearthTreeNode toRet = boardState;
        ArrayList<BoardModel.MinionPlayerPair> minions = new ArrayList<>();
        for (BoardModel.MinionPlayerPair mp : toRet.data_.getAllMinionsFIFOList()) {
            minions.add(mp);
        }

        for (BoardModel.MinionPlayerPair mp : minions) {
            if (mp.getPlayerSide() == PlayerSide.CURRENT_PLAYER &&
                filter.targetMatches(thisMinionPlayerSide, this, mp.getPlayerSide(), mp.getMinion(), boardState.data_)) {
                HearthTreeNode tempNode = effect.applyEffect(mp.getPlayerSide(), mp.getMinion(), toRet);
                if (tempNode != null)
                    toRet = tempNode;
            }
        }

        if (filter.targetMatches(thisMinionPlayerSide, this, PlayerSide.CURRENT_PLAYER, toRet.data_.getCharacter(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO), boardState.data_)) {
            toRet = effect.applyEffect(PlayerSide.CURRENT_PLAYER, toRet.data_.getCharacter(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO), toRet);
        }
        return toRet;
    }
}
