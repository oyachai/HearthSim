package com.hearthsim.test.minion;

import static org.junit.Assert.assertEquals;

import com.hearthsim.model.PlayerModel;
import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.AmaniBerserker;
import com.hearthsim.card.minion.concrete.RiverCrocolisk;
import com.hearthsim.card.spellcard.concrete.HolyLight;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

/**
 * Serves as a general test for enrage mechanic
 */
public class TestAmaniBerserker {

    private HearthTreeNode board;
    private AmaniBerserker amaniBerserker;
    private RiverCrocolisk croc;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel());

        amaniBerserker = new AmaniBerserker();
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, amaniBerserker);

        croc = new RiverCrocolisk();
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, croc);
    }

    @Test
    public void testAttackNormal() throws HSException {
        Minion target = board.data_.modelForSide(PlayerSide.WAITING_PLAYER).getCharacter(0);
        HearthTreeNode ret = amaniBerserker.attack(PlayerSide.WAITING_PLAYER, target, board, null, null, false);
        assertEquals(board, ret);
        assertEquals(board.data_.getWaitingPlayer().getHero().getHealth(), 28);
    }

    @Test
    public void testEnrage() throws HSException {
        HearthTreeNode ret = amaniBerserker.attack(PlayerSide.WAITING_PLAYER, croc, board, null, null, false);
        assertEquals(board, ret);

        PlayerModel currentPlayer = board.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        PlayerModel waitingPlayer = board.data_.modelForSide(PlayerSide.WAITING_PLAYER);

        assertEquals(currentPlayer.getNumMinions(), 1);
        assertEquals(waitingPlayer.getNumMinions(), 1);

        assertEquals(1, amaniBerserker.getHealth());
        assertEquals(1, croc.getHealth());

        assertEquals(2 + 3, amaniBerserker.getAttack()); // enraged!
    }

    @Test
    public void testHealRemovesEngrage() throws HSException {
        HearthTreeNode ret = amaniBerserker.attack(PlayerSide.WAITING_PLAYER, croc, board, null, null, false);

        board.data_.getCurrentPlayer().placeCardHand(new HolyLight());
        board.data_.getCurrentPlayer().setMana((byte)2);
        Card theCard = board.data_.getCurrentPlayer().getHand().get(0);
        ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, amaniBerserker, board, null, null);

        assertEquals(board, ret);
        assertEquals(3, amaniBerserker.getHealth());
        assertEquals(2, amaniBerserker.getAttack()); // not enraged anymore!
    }
}
