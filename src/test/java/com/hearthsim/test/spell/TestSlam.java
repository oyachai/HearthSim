package com.hearthsim.test.spell;

import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.basic.minion.BoulderfistOgre;
import com.hearthsim.card.basic.minion.RaidLeader;
import com.hearthsim.card.classic.spell.common.Slam;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestSlam {

    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel());
        currentPlayer = board.data_.getCurrentPlayer();
        waitingPlayer = board.data_.getWaitingPlayer();

        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new RaidLeader());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new BoulderfistOgre());

        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new RaidLeader());
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new BoulderfistOgre());

        Card fb = new Slam();
        currentPlayer.placeCardHand(fb);

        currentPlayer.setMana((byte) 10);
        waitingPlayer.setMana((byte) 10);

        currentPlayer.setMaxMana((byte) 10);
        waitingPlayer.setMaxMana((byte) 10);

        board.data_.resetMana();
        board.data_.resetMinions();
    }

    @Test
    public void test1() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.MINION_2, board);

        assertFalse(ret == null);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 2);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(currentPlayer.getMana(), 8);
        assertEquals(waitingPlayer.getMana(), 10);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), 5);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), 7);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 7);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 7);

        assertTrue(ret instanceof CardDrawNode);
        assertEquals(((CardDrawNode) ret).getNumCardsToDraw(), 1);
    }

    @Test
    public void test2() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.WAITING_PLAYER, CharacterIndex.MINION_2, board);

        assertFalse(ret == null);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 2);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(currentPlayer.getMana(), 8);
        assertEquals(waitingPlayer.getMana(), 10);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), 7);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), 5);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 7);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 7);

        assertTrue(ret instanceof CardDrawNode);
        assertEquals(((CardDrawNode)ret).getNumCardsToDraw(), 1);
    }

    @Test
    public void test3() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.WAITING_PLAYER, CharacterIndex.MINION_1, board);

        assertFalse(ret == null);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 2);
        assertEquals(waitingPlayer.getNumMinions(), 1);
        assertEquals(currentPlayer.getMana(), 8);
        assertEquals(waitingPlayer.getMana(), 10);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), 7);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), 7);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 7);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 6);

        assertFalse(ret instanceof CardDrawNode);
    }
}
