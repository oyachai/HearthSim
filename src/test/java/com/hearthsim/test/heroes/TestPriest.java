package com.hearthsim.test.heroes;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.basic.minion.ChillwindYeti;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.heroes.Priest;
import com.hearthsim.card.minion.heroes.TestHero;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.player.playercontroller.BruteForceSearchAI;
import com.hearthsim.util.factory.BoardStateFactoryBase;
import com.hearthsim.util.factory.DepthBoardStateFactory;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestPriest {

    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel(new Priest(), new TestHero()));
        currentPlayer = board.data_.getCurrentPlayer();
        waitingPlayer = board.data_.getWaitingPlayer();

        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new ChillwindYeti());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new ChillwindYeti());

        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new ChillwindYeti());
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new ChillwindYeti());

        currentPlayer.setMana((byte) 8);
        waitingPlayer.setMana((byte) 8);
    }

    @Test
    public void testAiHealAfterTrading() throws HSException {
        BoardStateFactoryBase factory = new DepthBoardStateFactory(null, null, 2000000000, true);
        HearthTreeNode tree = new HearthTreeNode(board.data_);
        BruteForceSearchAI ai0 = BruteForceSearchAI.buildStandardAI1();
        try {
            tree = factory.doMoves(tree, ai0.scorer);
        } catch(HSException e) {
            e.printStackTrace();
            assertTrue(false);
        }

        // best move is to kill one enemy yeti with 2 of your own yeti and heal
        // one of your own yeti
        HearthTreeNode bestPlay = tree.findMaxOfFunc(ai0.scorer);

        assertFalse(tree == null);
        assertEquals(bestPlay.data_.getCurrentPlayer().getHand().size(), 0);
        assertEquals(bestPlay.data_.getCurrentPlayer().getNumMinions(), 2);
        assertEquals(bestPlay.data_.getWaitingPlayer().getNumMinions(), 1);
        assertEquals(bestPlay.data_.getCurrentPlayer().getMana(), 6);
        assertEquals(bestPlay.data_.getWaitingPlayer().getMana(), 8);
        assertEquals(bestPlay.data_.getCurrentPlayer().getHero().getHealth(), 30);
        assertEquals(bestPlay.data_.getWaitingPlayer().getHero().getHealth(), 30);
        assertEquals(bestPlay.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_1).getHealth(), 3);
        assertEquals(bestPlay.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_2).getHealth(), 1);
        assertEquals(bestPlay.data_.getWaitingPlayer().getCharacter(CharacterIndex.MINION_1).getHealth(), 5);

        assertEquals(bestPlay.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 4);
        assertEquals(bestPlay.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 4);
        assertEquals(bestPlay.data_.getWaitingPlayer().getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 4);
    }

    @Test
    public void testAiHealBeforeTrading() throws HSException {

        // one of your yeti is damaged
        currentPlayer.getCharacter(CharacterIndex.MINION_1).setHealth((byte)3);

        BoardStateFactoryBase factory = new DepthBoardStateFactory(null, null, 2000000000, true);
        HearthTreeNode tree = new HearthTreeNode(board.data_);
        BruteForceSearchAI ai0 = BruteForceSearchAI.buildStandardAI1();
        try {
            tree = factory.doMoves(tree, ai0.scorer);
        } catch(HSException e) {
            e.printStackTrace();
            assertTrue(false);
        }

        // best move is to heal the damaged yeti and attack with both to kill
        // one of the enemy's yeti
        HearthTreeNode bestPlay = tree.findMaxOfFunc(ai0.scorer);

        assertFalse(tree == null);
        assertEquals(bestPlay.data_.getCurrentPlayer().getHand().size(), 0);
        assertEquals(bestPlay.data_.getCurrentPlayer().getNumMinions(), 2);
        assertEquals(bestPlay.data_.getWaitingPlayer().getNumMinions(), 1);
        assertEquals(bestPlay.data_.getCurrentPlayer().getMana(), 6);
        assertEquals(bestPlay.data_.getWaitingPlayer().getMana(), 8);
        assertEquals(bestPlay.data_.getCurrentPlayer().getHero().getHealth(), 30);
        assertEquals(bestPlay.data_.getWaitingPlayer().getHero().getHealth(), 30);
        assertEquals(bestPlay.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_1).getHealth(), 1);
        assertEquals(bestPlay.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_2).getHealth(), 1);
        assertEquals(bestPlay.data_.getWaitingPlayer().getCharacter(CharacterIndex.MINION_1).getHealth(), 5);

        assertEquals(bestPlay.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 4);
        assertEquals(bestPlay.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 4);
        assertEquals(bestPlay.data_.getWaitingPlayer().getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 4);
    }

    @Test
    public void testHeropowerAgainstOpponent() throws HSException {
        Minion target = waitingPlayer.getCharacter(CharacterIndex.HERO); // Opponent hero
        target.setHealth((byte)20);

        Hero priest = currentPlayer.getHero();

        HearthTreeNode ret = priest.useHeroAbility(PlayerSide.WAITING_PLAYER, target, board);
        assertEquals(board, ret);

        assertEquals(currentPlayer.getMana(), 6);
        assertEquals(waitingPlayer.getHero().getHealth(), 22);
    }

    @Test
    public void testHeropowerAgainstMinion() throws HSException {
        Minion target = waitingPlayer.getCharacter(CharacterIndex.MINION_1);
        target.setHealth((byte)1);

        Hero priest = currentPlayer.getHero();

        HearthTreeNode ret = priest.useHeroAbility(PlayerSide.WAITING_PLAYER, target, board);
        assertEquals(board, ret);

        assertEquals(currentPlayer.getMana(), 6);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), 3);
    }

    @Test
    public void testHeropowerAgainstSelf() throws HSException {
        Minion target = currentPlayer.getCharacter(CharacterIndex.HERO); // Self
        target.setHealth((byte)20);

        Hero priest = currentPlayer.getHero();

        HearthTreeNode ret = priest.useHeroAbility(PlayerSide.WAITING_PLAYER, target, board);
        assertEquals(board, ret);

        assertEquals(currentPlayer.getMana(), 6);
        assertEquals(currentPlayer.getHero().getHealth(), 22);
    }

    @Test
    public void testHeropowerAgainstOwnMinion() throws HSException {
        Minion target = currentPlayer.getCharacter(CharacterIndex.MINION_1);
        target.setHealth((byte)1);

        Hero priest = currentPlayer.getHero();

        HearthTreeNode ret = priest.useHeroAbility(PlayerSide.CURRENT_PLAYER, target, board);
        assertEquals(board, ret);

        assertEquals(currentPlayer.getMana(), 6);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), 3);
    }
}
