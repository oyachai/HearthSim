package com.hearthsim.test.heroes;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.basic.minion.*;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.heroes.Shaman;
import com.hearthsim.card.minion.heroes.TestHero;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.player.playercontroller.BruteForceSearchAI;
import com.hearthsim.util.HearthActionBoardPair;
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.util.tree.RandomEffectNode;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class TestShaman {

    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    private HearthTreeNode board;
    private PlayerModel currentPlayer;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel(new Shaman(), new TestHero()));
        currentPlayer = board.data_.getCurrentPlayer();
        PlayerModel waitingPlayer = board.data_.getWaitingPlayer();

        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new RaidLeader());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new BoulderfistOgre());

        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new RaidLeader());
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new BoulderfistOgre());

        currentPlayer.setMana((byte) 8);
        waitingPlayer.setMana((byte) 8);
    }

    @Test
    public void testHeropowerNode() throws HSException {
        Hero hero = currentPlayer.getHero();
        HearthTreeNode ret = hero.useHeroAbility(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertNotEquals(board, ret);
        assertTrue(ret instanceof RandomEffectNode);
        assertEquals(ret.getChildren().size(), 4);

        assertEquals(currentPlayer.getNumMinions(), 2);
        assertEquals(currentPlayer.getMana(), 8);
    }

    @Test
    public void testTotemSearing() throws HSException {
        Hero hero = currentPlayer.getHero();
        HearthTreeNode ret = hero.useHeroAbility(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);

        HearthTreeNode searingNode = ret.getChildren().get(0);
        assertEquals(searingNode.data_.getCurrentPlayer().getNumMinions(), 3);
        assertEquals(searingNode.data_.getCurrentPlayer().getMana(), 6);
        assertTrue(searingNode.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_3) instanceof SearingTotem);

        assertEquals(searingNode.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_1).getTotalHealth(), 2);
        assertEquals(searingNode.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_2).getTotalHealth(), 7);
        assertEquals(searingNode.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_3).getTotalHealth(), 1);

        // Raid Leader is in effect
        assertEquals(searingNode.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 2);
        assertEquals(searingNode.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 7);
        assertEquals(searingNode.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_3).getTotalAttack(), 2);
    }

    @Test
    public void testTotemStoneclaw() throws HSException {
        Hero hero = currentPlayer.getHero();
        HearthTreeNode ret = hero.useHeroAbility(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);

        HearthTreeNode searingNode = ret.getChildren().get(1);
        assertEquals(searingNode.data_.getCurrentPlayer().getNumMinions(), 3);
        assertEquals(searingNode.data_.getCurrentPlayer().getMana(), 6);
        assertTrue(searingNode.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_3) instanceof StoneclawTotem);

        assertEquals(searingNode.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_1).getTotalHealth(), 2);
        assertEquals(searingNode.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_2).getTotalHealth(), 7);
        assertEquals(searingNode.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_3).getTotalHealth(), 2);

        // Raid Leader is in effect
        assertEquals(searingNode.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 2);
        assertEquals(searingNode.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 7);
        assertEquals(searingNode.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_3).getTotalAttack(), 1);
    }

    @Test
    public void testTotemHealing() throws HSException {
        Hero hero = currentPlayer.getHero();
        HearthTreeNode ret = hero.useHeroAbility(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);

        HearthTreeNode healingNode = ret.getChildren().get(2);
        assertEquals(healingNode.data_.getCurrentPlayer().getNumMinions(), 3);
        assertEquals(healingNode.data_.getCurrentPlayer().getMana(), 6);
        assertTrue(healingNode.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_3) instanceof HealingTotem);

        assertEquals(healingNode.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_1).getTotalHealth(), 2);
        assertEquals(healingNode.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_2).getTotalHealth(), 7);
        assertEquals(healingNode.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_3).getTotalHealth(), 2);

        // Raid Leader is in effect
        assertEquals(healingNode.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 2);
        assertEquals(healingNode.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 7);
        assertEquals(healingNode.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_3).getTotalAttack(), 1);
    }

    @Test
    public void testTotemWrathOfAir() throws HSException {
        Hero hero = currentPlayer.getHero();
        HearthTreeNode ret = hero.useHeroAbility(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);

        HearthTreeNode wrathOfAirNode = ret.getChildren().get(3);
        assertEquals(wrathOfAirNode.data_.getCurrentPlayer().getNumMinions(), 3);
        assertEquals(wrathOfAirNode.data_.getCurrentPlayer().getMana(), 6);
        assertTrue(wrathOfAirNode.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_3) instanceof WrathOfAirTotem);

        assertEquals(wrathOfAirNode.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_1).getTotalHealth(), 2);
        assertEquals(wrathOfAirNode.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_2).getTotalHealth(), 7);
        assertEquals(wrathOfAirNode.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_3).getTotalHealth(), 2);

        // Raid Leader is in effect
        assertEquals(wrathOfAirNode.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 2);
        assertEquals(wrathOfAirNode.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 7);
        assertEquals(wrathOfAirNode.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_3).getTotalAttack(), 1);
    }

    @Test
    public void testTotemPosition() throws HSException {
        BruteForceSearchAI ai0 = BruteForceSearchAI.buildStandardAI1();
        List<HearthActionBoardPair> ab = ai0.playTurn(0, board.data_);
        BoardModel resBoard = ab.get(ab.size() - 1).board;

        assertEquals(resBoard.getCurrentPlayer().getMana(), 6);
        assertEquals(resBoard.getWaitingPlayer().getMana(), 8);
        assertEquals(resBoard.modelForSide(PlayerSide.CURRENT_PLAYER).getNumMinions(), 3);
        assertEquals(resBoard.modelForSide(PlayerSide.WAITING_PLAYER).getNumMinions(), 1);

        int index = resBoard.getCurrentPlayer().getNumMinions();
        boolean isRightTotem = false;
        isRightTotem = isRightTotem || resBoard.getCurrentPlayer().getCharacter(CharacterIndex.fromInteger(index)) instanceof HealingTotem;
        isRightTotem = isRightTotem || resBoard.getCurrentPlayer().getCharacter(CharacterIndex.fromInteger(index)) instanceof SearingTotem;
        isRightTotem = isRightTotem || resBoard.getCurrentPlayer().getCharacter(CharacterIndex.fromInteger(index)) instanceof StoneclawTotem;
        isRightTotem = isRightTotem || resBoard.getCurrentPlayer().getCharacter(CharacterIndex.fromInteger(index)) instanceof WrathOfAirTotem;
        assertTrue(isRightTotem);

        log.info("{}", resBoard.modelForSide(PlayerSide.CURRENT_PLAYER).getCharacter(CharacterIndex.MINION_3).getClass());
    }

    @Test
    public void testHeropowerWithFullBoard() throws HSException {
        Hero shaman = currentPlayer.getHero();
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new SilverHandRecruit());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new SilverHandRecruit());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new SilverHandRecruit());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new SilverHandRecruit());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new SilverHandRecruit());

        assertEquals(currentPlayer.getNumMinions(), 7);

        HearthTreeNode ret = shaman.useHeroAbility(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertNull(ret);

        assertEquals(currentPlayer.getNumMinions(), 7);
        assertEquals(currentPlayer.getMana(), 8);
    }

    @Test
    public void testHeropowerWithExistingTotem() throws HSException {
        Hero shaman = currentPlayer.getHero();
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new SearingTotem());

        assertEquals(currentPlayer.getNumMinions(), 3);

        HearthTreeNode ret = shaman.useHeroAbility(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertNotEquals(board, ret);
        assertTrue(ret instanceof RandomEffectNode);
        assertEquals(ret.getChildren().size(), 3);

        HearthTreeNode childNode = ret.getChildren().get(0);
        assertFalse(childNode.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_4) instanceof SearingTotem);

        childNode = ret.getChildren().get(1);
        assertFalse(childNode.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_4) instanceof SearingTotem);

        childNode = ret.getChildren().get(2);
        assertFalse(childNode.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_4) instanceof SearingTotem);

        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(currentPlayer.getMana(), 8);
    }

    @Test
    public void testHeropowerWithAllTotems() throws HSException {
        Hero shaman = currentPlayer.getHero();
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new SearingTotem());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new StoneclawTotem());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new HealingTotem());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new WrathOfAirTotem());

        assertEquals(currentPlayer.getNumMinions(), 6);

        HearthTreeNode ret = shaman.useHeroAbility(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertNull(ret);

        assertEquals(currentPlayer.getNumMinions(), 6);
        assertEquals(currentPlayer.getMana(), 8);
    }
}
