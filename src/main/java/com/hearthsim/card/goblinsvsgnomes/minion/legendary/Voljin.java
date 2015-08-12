package com.hearthsim.card.goblinsvsgnomes.minion.legendary;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.filter.FilterCharacterInterface;
import com.hearthsim.event.filter.FilterCharacterTargetedBattlecry;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Voljin extends Minion implements MinionBattlecryInterface {
    private final static FilterCharacterTargetedBattlecry filter = new FilterCharacterTargetedBattlecry() {
        @Override
        protected boolean includeEnemyMinions() {
            return true;
        }

        @Override
        protected boolean includeOwnMinions() {
            return true;
        }
    };

    public Voljin() {
        super();
    }

    @Override
    public FilterCharacterInterface getBattlecryFilter() {
        return Voljin.filter;
    }

    @Override
    public EffectCharacter<Minion> getBattlecryEffect() {
        return (PlayerSide targetSide, CharacterIndex targetCharacterIndex, HearthTreeNode boardState) -> {
            Minion targetCharacter = boardState.data_.getCharacter(targetSide, targetCharacterIndex);
            Voljin.this.setHealth(targetCharacter.getHealth());
            Voljin.this.setMaxHealth(targetCharacter.getMaxHealth());
            targetCharacter.setHealth((byte) 2);
            targetCharacter.setMaxHealth((byte) 2);
            return boardState;
        };
    }
}
