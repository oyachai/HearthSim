package com.hearthsim.test.card;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.BloodfenRaptor;
import com.hearthsim.card.minion.concrete.Huffer;
import com.hearthsim.card.minion.concrete.Leokk;
import com.hearthsim.card.minion.concrete.Misha;
import com.hearthsim.card.spellcard.concrete.AnimalCompanion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.util.tree.RandomEffectNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestAnimalCompanion {

    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    private static final byte mana = 2;
    private static final byte attack0 = 5;
    private static final byte health0 = 3;
    private static final byte health1 = 7;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel());
        currentPlayer = board.data_.getCurrentPlayer();
        waitingPlayer = board.data_.getWaitingPlayer();

        Minion minion0_0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
        Minion minion0_1 = new Minion("" + 0, mana, attack0, (byte)(health1 - 1), attack0, health1, health1);
        Minion minion1_0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
        Minion minion1_1 = new Minion("" + 0, mana, attack0, (byte)(health1 - 1), attack0, health1, health1);

        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, minion0_0);
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, minion0_1);

        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1_0);
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1_1);

        AnimalCompanion fb = new AnimalCompanion();
        currentPlayer.placeCardHand(fb);

        currentPlayer.setMana((byte) 4);
    }

    @Test
    public void testLeokkBuffs() throws HSException {

        Card leokk = new Leokk();
        currentPlayer.placeCardHand(leokk);

        Card theCard = currentPlayer.getHand().get(1);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 2, board);

        //Use Leokk.  The other minions should now be buffed with +1 attack
        assertEquals(board, ret);

        assertEquals(currentPlayer.getHand().size(), 1);
        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(currentPlayer.getMinions().get(0).getHealth(), health0);
        assertEquals(currentPlayer.getMinions().get(1).getHealth(), health1 - 1);
        assertEquals(currentPlayer.getMinions().get(2).getHealth(), 4);
        assertEquals(waitingPlayer.getMinions().get(0).getHealth(), health0);
        assertEquals(waitingPlayer.getMinions().get(1).getHealth(), health1 - 1);

        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), attack0 + 1);
        assertEquals(currentPlayer.getMinions().get(1).getTotalAttack(), attack0 + 1);
        assertEquals(currentPlayer.getMinions().get(2).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), attack0);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalAttack(), attack0);

        //Now, attack and kill Leokk.  All minions should go back to their original attack
        Minion minion = currentPlayer.getMinions().get(2);
        minion.hasAttacked(false);
        ret = minion.attack(PlayerSide.WAITING_PLAYER, 1, board, false);

        assertEquals(board, ret);
        assertEquals(currentPlayer.getHand().size(), 1);
        assertEquals(currentPlayer.getNumMinions(), 2);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(currentPlayer.getMinions().get(0).getHealth(), health0);
        assertEquals(currentPlayer.getMinions().get(1).getHealth(), health1 - 1);
        assertEquals(waitingPlayer.getMinions().get(0).getHealth(), health0 - 2);
        assertEquals(waitingPlayer.getMinions().get(1).getHealth(), health1 - 1);

        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), attack0);
        assertEquals(currentPlayer.getMinions().get(1).getTotalAttack(), attack0);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), attack0);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalAttack(), attack0);
    }

    @Test
    public void testSummonsHufferLeokkOrMisha() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board);
        assertNotNull(ret); // ret != null because of how AnimalCompanion creates its RNG node
        assertTrue(ret instanceof RandomEffectNode);

        // Check that the original node is not touched
        assertEquals(1, currentPlayer.getHand().size());
        assertEquals(4, currentPlayer.getMana());

        assertEquals(2, currentPlayer.getNumMinions());
        assertEquals(2, waitingPlayer.getNumMinions());

        //child node 0 = Huffer
        HearthTreeNode c0 = ret.getChildren().get(0);
        PlayerModel currentPlayer0 = c0.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        assertEquals(3, currentPlayer0.getNumMinions());
        assertEquals(1, currentPlayer0.getMana());
        assertTrue(currentPlayer0.getMinions().get(2) instanceof Huffer);

        //child node 1 = Leokk
        HearthTreeNode c1 = ret.getChildren().get(1);
        PlayerModel currentPlayer1 = c1.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        assertEquals(3, currentPlayer1.getNumMinions());
        assertEquals(1, currentPlayer1.getMana());
        assertTrue(currentPlayer1.getMinions().get(2) instanceof Leokk);

        //child node 2 = Misha
        HearthTreeNode c2 = ret.getChildren().get(2);
        PlayerModel currentPlayer2 = c2.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        assertEquals(3, currentPlayer2.getNumMinions());
        assertEquals(1, currentPlayer2.getMana());
        assertTrue(currentPlayer2.getMinions().get(2) instanceof Misha);
    }

    @Test
    public void testCannotPlayWithFullBoard() throws HSException {
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new BloodfenRaptor());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new BloodfenRaptor());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new BloodfenRaptor());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new BloodfenRaptor());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new BloodfenRaptor());

        Card theCard = currentPlayer.getHand().get(0);
        assertFalse(theCard.canBeUsedOn(PlayerSide.CURRENT_PLAYER, 0, board.data_));

        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board);
        assertNull(ret);
    }
}
