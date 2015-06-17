package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.basic.minion.RiverCrocolisk;
import com.hearthsim.card.basic.spell.HolyLight;
import com.hearthsim.card.classic.minion.common.AmaniBerserker;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Serves as a general test for enrage mechanic
 */
public class TestAmaniBerserker {

    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    private AmaniBerserker amaniBerserker;
    private RiverCrocolisk croc;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel());
        currentPlayer = board.data_.getCurrentPlayer();
        waitingPlayer = board.data_.getWaitingPlayer();

        amaniBerserker = new AmaniBerserker();
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, amaniBerserker);

        croc = new RiverCrocolisk();
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, croc);
    }

    @Test
    public void testAttackNormal() throws HSException {
        HearthTreeNode ret = amaniBerserker.attack(PlayerSide.WAITING_PLAYER, CharacterIndex.HERO, board);
        assertEquals(board, ret);
        assertEquals(waitingPlayer.getHero().getHealth(), 28);
    }

    @Test
    public void testEnrage() throws HSException {
        HearthTreeNode ret = amaniBerserker.attack(PlayerSide.WAITING_PLAYER, croc, board);
        assertEquals(board, ret);

        assertEquals(currentPlayer.getNumMinions(), 1);
        assertEquals(waitingPlayer.getNumMinions(), 1);

        assertEquals(1, amaniBerserker.getHealth());
        assertEquals(1, croc.getHealth());

        assertEquals(2 + 3, amaniBerserker.getAttack()); // enraged!
    }

    @Test
    public void testHealRemovesEngrage() throws HSException {
        HearthTreeNode ret = amaniBerserker.attack(PlayerSide.WAITING_PLAYER, croc, board);

        currentPlayer.placeCardHand(new HolyLight());
        currentPlayer.setMana((byte) 2);
        Card theCard = currentPlayer.getHand().get(0);
        ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.MINION_1, ret);

        assertEquals(board, ret);
        assertEquals(3, amaniBerserker.getHealth());
        assertEquals(2, amaniBerserker.getAttack()); // not enraged anymore!
    }
}
