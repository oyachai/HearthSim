package com.hearthsim.card.classic.minion.rare;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Lightwell extends Minion {

    public Lightwell() {
        super();
    }

    @Override
    public HearthTreeNode startTurn(PlayerSide side, HearthTreeNode boardModel) throws HSException {
        HearthTreeNode toRet = boardModel;
        if (side == PlayerSide.CURRENT_PLAYER) {
            PlayerModel currentPlayer = toRet.data_.modelForSide(PlayerSide.CURRENT_PLAYER);

            //check to see if anyone is damaged
            boolean isDamaged = currentPlayer.getHero().getTotalMaxHealth() > currentPlayer.getHero().getTotalHealth();
            for (Minion minion : currentPlayer.getMinions()) {
                isDamaged = isDamaged || minion.getTotalMaxHealth() > minion.getTotalHealth();
            }
            if (!isDamaged)
                return super.startTurn(side, toRet);

            Minion targetMinion = currentPlayer.getCharacter(CharacterIndex.fromInteger((int) (Math.random() * (currentPlayer.getNumMinions() + 1))));
            while (targetMinion.getTotalMaxHealth() == targetMinion.getTotalHealth()) {
                targetMinion = currentPlayer.getCharacter(CharacterIndex.fromInteger((int)(Math.random()*(currentPlayer.getNumMinions() + 1))));
            }
            toRet = targetMinion.takeHealAndNotify((byte) 3, PlayerSide.CURRENT_PLAYER, toRet);
        }
        return super.startTurn(side, toRet);
    }

}
