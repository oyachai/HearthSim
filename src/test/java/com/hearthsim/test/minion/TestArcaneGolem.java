package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.basic.minion.BoulderfistOgre;
import com.hearthsim.card.basic.minion.RaidLeader;
import com.hearthsim.card.classic.minion.rare.ArcaneGolem;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.player.playercontroller.BruteForceSearchAI;
import com.hearthsim.util.HearthActionBoardPair;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestArcaneGolem {

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

        Card fb = new ArcaneGolem();
        currentPlayer.placeCardHand(fb);

        currentPlayer.setMana((byte) 18);
        waitingPlayer.setMana((byte) 18);

        currentPlayer.setMaxMana((byte) 8);
        waitingPlayer.setMaxMana((byte) 8);

        board.data_.resetMana();
        board.data_.resetMinions();
    }

    @Test
    public void testIncreasesEnemyMana() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertNotNull(ret);
        currentPlayer = ret.data_.getCurrentPlayer();
        waitingPlayer = ret.data_.getWaitingPlayer();

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(currentPlayer.getMana(), 5);
        assertEquals(currentPlayer.getMaxMana(), 8);
        assertEquals(waitingPlayer.getMana(), 9);
        assertEquals(waitingPlayer.getMaxMana(), 9);
    }

    @Test
    public void testDoesNotIncreaseOverTen() throws HSException {
        waitingPlayer.setMaxMana((byte) 10);
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertNotNull(ret);
        waitingPlayer = ret.data_.getWaitingPlayer();
        assertEquals(waitingPlayer.getMana(), 9);
        assertEquals(waitingPlayer.getMaxMana(), 10);
    }

    @Test
    public void testAiAttacksAfterPlaying() throws HSException {
        currentPlayer.setMana((byte) 3);
        waitingPlayer.setMana((byte) 3);

        currentPlayer.setMaxMana((byte) 3);
        waitingPlayer.setMaxMana((byte) 3);

        currentPlayer.getCharacter(CharacterIndex.MINION_1).hasAttacked(true);
        currentPlayer.getCharacter(CharacterIndex.MINION_2).hasAttacked(true);

        BruteForceSearchAI ai0 = BruteForceSearchAI.buildStandardAI1();
        List<HearthActionBoardPair> ab = ai0.playTurn(0, board.data_);
        BoardModel resBoard = ab.get(ab.size() - 1).board;

        assertEquals(resBoard.getCurrentPlayer().getHand().size(), 0);
        assertEquals(resBoard.modelForSide(PlayerSide.CURRENT_PLAYER).getNumMinions(), 3);
        assertEquals(resBoard.modelForSide(PlayerSide.WAITING_PLAYER).getNumMinions(), 2); //1 minion should have been killed
        assertEquals(resBoard.getCurrentPlayer().getMana(), 0); //3 mana used
        assertEquals(resBoard.getWaitingPlayer().getMana(), 4); //1 mana given by the Arcane Golem
        assertEquals(resBoard.getCurrentPlayer().getHero().getHealth(), 30);
        assertEquals(resBoard.getWaitingPlayer().getHero().getHealth(), 25); //5 damage to the face... 4 from Arcane Golem (he has charge!), 1 more from Raid Leader's buff
    }
}
