package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.deathrattle.DeathrattleAction;
import com.hearthsim.event.deathrattle.DeathrattleDamageAll;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.HearthAction;
import com.hearthsim.util.factory.BoardStateFactoryBase;
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.util.tree.RandomEffectNode;

import java.util.ArrayList;
import java.util.List;

public class DarkCultist extends Minion {

    public DarkCultist() {
        super();
        deathrattleAction_ = new DarkCulistDeathrattle(3);
    }

    private class DarkCulistDeathrattle extends DeathrattleAction {
        private byte effect;

        public DarkCulistDeathrattle(int effect) {
            this.effect = (byte)effect;
        }

        public HearthTreeNode performAction(Card origin, PlayerSide playerSide, HearthTreeNode boardState, boolean singleRealizationOnly) throws HSException {
            if (!(origin instanceof Minion)) {
                return null;
            }
            PlayerModel owner = boardState.data_.modelForSide(playerSide);
            int originIndex = owner.getIndexForCharacter((Minion)origin);

            // TODO could probably be faster and belongs in a more common location
            List<Minion> possibleTargets = new ArrayList<>();
            for (Minion minion : owner.getMinions()) {
                if (minion != origin) {
                    possibleTargets.add(minion);
                }
            }

            Minion targetMinion;
            switch (possibleTargets.size()) {
                case 0: // no targets, do nothing
                    break;
                case 1: // one target, no RNG needed
                    targetMinion = possibleTargets.get(0);
                    targetMinion.addHealth(this.effect);
                    targetMinion.addMaxHealth(this.effect);
                    break;
                default: // more than 1 option, generate all possible futures
                    RandomEffectNode rngNode = new RandomEffectNode(boardState, boardState.getAction());
                    for (Minion possibleTarget : possibleTargets) {
                        int targetIndex = owner.getIndexForCharacter(possibleTarget);

                        HearthTreeNode newState = new HearthTreeNode(rngNode.data_.deepCopy());
                        targetMinion = newState.data_.modelForSide(playerSide).getCharacter(targetIndex);
                        targetMinion.addHealth(this.effect);
                        targetMinion.addMaxHealth(this.effect);

                        // TODO need to do this manually for now. we should handle this in the death handler
                        newState.data_.removeMinion(playerSide, originIndex - 1);
                        rngNode.addChild(newState);
                    }

                    boardState = rngNode;
                    break;
            }
            return boardState;
        }
    }
}
