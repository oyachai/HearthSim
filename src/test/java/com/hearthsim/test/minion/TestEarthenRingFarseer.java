package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.basic.minion.BoulderfistOgre;
import com.hearthsim.card.basic.minion.DreadInfernal;
import com.hearthsim.card.basic.minion.RaidLeader;
import com.hearthsim.card.classic.minion.common.EarthenRingFarseer;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestEarthenRingFarseer {

    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel());
        currentPlayer = board.data_.getCurrentPlayer();
        waitingPlayer = board.data_.getWaitingPlayer();

        Minion minion0_0 = new DreadInfernal();
        Minion minion0_1 = new BoulderfistOgre();
        Minion minion0_2 = new RaidLeader();
        Minion minion1_0 = new BoulderfistOgre();
        Minion minion1_1 = new RaidLeader();

        Card fb = new EarthenRingFarseer();
        currentPlayer.placeCardHand(fb);

        currentPlayer.setMana((byte) 8);
        waitingPlayer.setMana((byte) 8);

        minion0_0.summonMinion(PlayerSide.CURRENT_PLAYER, currentPlayer.getHero(), board, false);
        minion0_1.summonMinion(PlayerSide.CURRENT_PLAYER, currentPlayer.getHero(), board, false);
        minion0_2.summonMinion(PlayerSide.CURRENT_PLAYER, currentPlayer.getHero(), board, false);
        minion1_0.summonMinion(PlayerSide.WAITING_PLAYER, waitingPlayer.getHero(), board, false);
        minion1_1.summonMinion(PlayerSide.WAITING_PLAYER, waitingPlayer.getHero(), board, false);

        minion0_0.setHealth((byte)1);
        minion0_1.setHealth((byte)1);

        minion1_0.setHealth((byte)1);

        currentPlayer.getHero().setHealth((byte)20);
        waitingPlayer.getHero().setHealth((byte)20);
    }

    @Test
    public void test0() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.WAITING_PLAYER, CharacterIndex.HERO, board);

        assertNull(ret);

        assertEquals(currentPlayer.getHand().size(), 1);
        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(currentPlayer.getMana(), 8);
        assertEquals(waitingPlayer.getMana(), 8);
        assertEquals(currentPlayer.getHero().getHealth(), 20);
        assertEquals(waitingPlayer.getHero().getHealth(), 20);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getTotalHealth(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getTotalHealth(), 1);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getTotalHealth(), 1);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getTotalHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getTotalHealth(), 1);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 7);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getTotalAttack(), 7);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 7);
    }

    @Test
    public void test1() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.MINION_3, board);

        assertFalse(ret == null);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 4);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(currentPlayer.getMana(), 5);
        assertEquals(waitingPlayer.getMana(), 8);
        assertEquals(currentPlayer.getHero().getHealth(), 20);
        assertEquals(waitingPlayer.getHero().getHealth(), 20);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getTotalHealth(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getTotalHealth(), 1);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getTotalHealth(), 1);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_4).getTotalHealth(), 3);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getTotalHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getTotalHealth(), 1);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 7);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getTotalAttack(), 7);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_4).getTotalAttack(), 4);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 7);

        //------------------------------------------------------------------------------------------------
        HearthTreeNode c0 = ret.getChildren().get(0);
        currentPlayer = c0.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        waitingPlayer = c0.data_.modelForSide(PlayerSide.WAITING_PLAYER);
        assertEquals(c0.data_.getCurrentPlayer().getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 4);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(c0.data_.getCurrentPlayer().getMana(), 5);
        assertEquals(c0.data_.getWaitingPlayer().getMana(), 8);
        assertEquals(c0.data_.getCurrentPlayer().getHero().getHealth(), 23);
        assertEquals(c0.data_.getWaitingPlayer().getHero().getHealth(), 20);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getTotalHealth(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getTotalHealth(), 1);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getTotalHealth(), 1);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_4).getTotalHealth(), 3);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getTotalHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getTotalHealth(), 1);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 7);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getTotalAttack(), 7);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_4).getTotalAttack(), 4);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 7);

        //------------------------------------------------------------------------------------------------
        HearthTreeNode c1 = ret.getChildren().get(4);
        currentPlayer = c1.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        waitingPlayer = c1.data_.modelForSide(PlayerSide.WAITING_PLAYER);
        assertEquals(c1.data_.getCurrentPlayer().getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 4);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(c1.data_.getCurrentPlayer().getMana(), 5);
        assertEquals(c1.data_.getWaitingPlayer().getMana(), 8);
        assertEquals(c1.data_.getCurrentPlayer().getHero().getHealth(), 20);
        assertEquals(c1.data_.getWaitingPlayer().getHero().getHealth(), 23);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getTotalHealth(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getTotalHealth(), 1);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getTotalHealth(), 1);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_4).getTotalHealth(), 3);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getTotalHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getTotalHealth(), 1);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 7);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getTotalAttack(), 7);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_4).getTotalAttack(), 4);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 7);

        //------------------------------------------------------------------------------------------------
        HearthTreeNode c3 = ret.getChildren().get(2);
        currentPlayer = c3.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        waitingPlayer = c3.data_.modelForSide(PlayerSide.WAITING_PLAYER);
        assertEquals(c3.data_.getCurrentPlayer().getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 4);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(c3.data_.getCurrentPlayer().getMana(), 5);
        assertEquals(c3.data_.getWaitingPlayer().getMana(), 8);
        assertEquals(c3.data_.getCurrentPlayer().getHero().getHealth(), 20);
        assertEquals(c3.data_.getWaitingPlayer().getHero().getHealth(), 20);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getTotalHealth(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getTotalHealth(), 4);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getTotalHealth(), 1);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_4).getTotalHealth(), 3);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getTotalHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getTotalHealth(), 1);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 7);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getTotalAttack(), 7);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_4).getTotalAttack(), 4);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 7);

        //------------------------------------------------------------------------------------------------

        HearthTreeNode c5 = ret.getChildren().get(5);
        currentPlayer = c5.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        waitingPlayer = c5.data_.modelForSide(PlayerSide.WAITING_PLAYER);
        assertEquals(c5.data_.getCurrentPlayer().getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 4);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(c5.data_.getCurrentPlayer().getMana(), 5);
        assertEquals(c5.data_.getWaitingPlayer().getMana(), 8);
        assertEquals(c5.data_.getCurrentPlayer().getHero().getHealth(), 20);
        assertEquals(c5.data_.getWaitingPlayer().getHero().getHealth(), 20);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getTotalHealth(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getTotalHealth(), 1);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getTotalHealth(), 1);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_4).getTotalHealth(), 3);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getTotalHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getTotalHealth(), 1);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 7);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getTotalAttack(), 7);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_4).getTotalAttack(), 4);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 7);

        //------------------------------------------------------------------------------------------------
        HearthTreeNode c6 = ret.getChildren().get(6);
        currentPlayer = c6.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        waitingPlayer = c6.data_.modelForSide(PlayerSide.WAITING_PLAYER);
        assertEquals(c6.data_.getCurrentPlayer().getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 4);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(c6.data_.getCurrentPlayer().getMana(), 5);
        assertEquals(c6.data_.getWaitingPlayer().getMana(), 8);
        assertEquals(c6.data_.getCurrentPlayer().getHero().getHealth(), 20);
        assertEquals(c6.data_.getWaitingPlayer().getHero().getHealth(), 20);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getTotalHealth(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getTotalHealth(), 1);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getTotalHealth(), 1);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_4).getTotalHealth(), 3);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getTotalHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getTotalHealth(), 4);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 7);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getTotalAttack(), 7);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_4).getTotalAttack(), 4);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 7);
    }
}
