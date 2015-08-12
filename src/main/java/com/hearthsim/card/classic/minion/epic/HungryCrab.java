package com.hearthsim.card.classic.minion.epic;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedBattlecry;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class HungryCrab extends Minion implements MinionBattlecryInterface {

    /**
     * Battlecry: Destroy a murloc and gain +2/+2
     */
    private final static FilterCharacterTargetedBattlecry filter = new FilterCharacterTargetedBattlecry() {
        protected boolean includeEnemyMinions() {
            return true;
        }
        protected boolean includeOwnMinions() {
            return true;
        }
        protected MinionTribe tribeFilter() {
            return MinionTribe.MURLOC;
        }
    };


    public HungryCrab() {
        super();
    }

    @Override
    public FilterCharacter getBattlecryFilter() {
        return HungryCrab.filter;
    }

    @Override
    public EffectCharacter getBattlecryEffect() {
        return new EffectCharacter<Minion>() {

            @Override
            public HearthTreeNode applyEffect(PlayerSide targetSide, CharacterIndex targetCharacterIndex, HearthTreeNode boardState) {
                Minion targetMinion = boardState.data_.modelForSide(targetSide).getCharacter(targetCharacterIndex);
                targetMinion.setHealth((byte) -99);
                HungryCrab.this.addAttack((byte) 2);
                HungryCrab.this.addHealth((byte) 2);
                HungryCrab.this.addMaxHealth((byte) 2);
                return boardState;
            }
        };    }
}
