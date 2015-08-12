package com.hearthsim.card.goblinsvsgnomes.minion.epic;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.basic.minion.SilverHandRecruit;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

/**
 * Created by oyachai on 6/7/15.
 */
public class Quartermaster extends Minion implements MinionBattlecryInterface {

    public Quartermaster() {
        super();
    }

    /**
     * Battlecry: Gain +2/+2 to your Silverhand Recruits.
     */
    @Override
    public EffectCharacter<Minion> getBattlecryEffect() {
        return (PlayerSide targetSide, CharacterIndex minionPlacementIndex, HearthTreeNode boardState) -> {
            for (Minion minion : boardState.data_.getCurrentPlayer().getMinions()) {
                if (minion instanceof SilverHandRecruit) {
                    minion.addMaxHealth((byte)2);
                    minion.addHealth((byte)2);
                    minion.addAttack((byte)2);
                }
            }
            return boardState;
        };
    }

}
