package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.Deck;
import com.hearthsim.card.basic.minion.BoulderfistOgre;
import com.hearthsim.card.basic.minion.RaidLeader;
import com.hearthsim.card.basic.minion.StormwindChampion;
import com.hearthsim.card.basic.spell.TheCoin;
import com.hearthsim.card.classic.minion.common.CruelTaskmaster;
import com.hearthsim.card.classic.minion.common.HarvestGolem;
import com.hearthsim.card.classic.minion.common.LootHoarder;
import com.hearthsim.card.classic.minion.rare.Abomination;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestCruelTaskmaster {
    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    @Before
    public void setup() throws HSException {
        Card cards[] = new Card[10];
        for (int index = 0; index < 10; ++index) {
            cards[index] = new TheCoin();
        }

        Deck deck = new Deck(cards);

        board = new HearthTreeNode(new BoardModel(deck, deck));
        currentPlayer = board.data_.getCurrentPlayer();
        waitingPlayer = board.data_.getWaitingPlayer();

        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new HarvestGolem());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new RaidLeader());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new StormwindChampion());

        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new LootHoarder());
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new Abomination());
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new RaidLeader());
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new BoulderfistOgre());

        currentPlayer.setMana((byte) 10);
        waitingPlayer.setMana((byte) 10);

        Minion fb = new CruelTaskmaster();
        currentPlayer.placeCardHand(fb);
    }

    @Test
    public void test0() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.WAITING_PLAYER, CharacterIndex.HERO, board);

        assertNull(ret);

        assertEquals(currentPlayer.getHand().size(), 1);
        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(waitingPlayer.getNumMinions(), 4);
        assertEquals(currentPlayer.getMana(), 10);
        assertEquals(waitingPlayer.getMana(), 10);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getTotalHealth(), 4);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getTotalHealth(), 3);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getTotalHealth(), 6);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getTotalHealth(), 1);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getTotalHealth(), 4);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_3).getTotalHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_4).getTotalHealth(), 7);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 4);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 3);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getTotalAttack(), 7);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 3);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 5);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_3).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_4).getTotalAttack(), 7);
    }

    @Test
    public void test1() throws HSException {

        //set the remaining total health of Abomination to 1
        waitingPlayer.getCharacter(CharacterIndex.MINION_2).setHealth((byte)1);

        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);

        assertFalse(ret == null);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 4);
        assertEquals(waitingPlayer.getNumMinions(), 4);
        assertEquals(currentPlayer.getMana(), 8);
        assertEquals(waitingPlayer.getMana(), 10);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getTotalHealth(), 3);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getTotalHealth(), 4);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getTotalHealth(), 3);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_4).getTotalHealth(), 6);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getTotalHealth(), 1);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getTotalHealth(), 1);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_3).getTotalHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_4).getTotalHealth(), 7);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 4);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 4);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getTotalAttack(), 3);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_4).getTotalAttack(), 7);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 3);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 5);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_3).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_4).getTotalAttack(), 7);

        assertEquals(ret.numChildren(), 7);

        //Forth child node is the one that kills the Loot Hoarder
        HearthTreeNode cn3 = ret.getChildren().get(3);
        assertEquals(cn3.data_.getCurrentPlayer().getHand().size(), 0);
        assertEquals(cn3.data_.getWaitingPlayer().getHand().size(), 1);
        assertEquals(cn3.data_.getCurrentPlayer().getNumMinions(), 4);
        assertEquals(cn3.data_.getWaitingPlayer().getNumMinions(), 3);
        assertEquals(cn3.data_.getCurrentPlayer().getMana(), 8);
        assertEquals(cn3.data_.getWaitingPlayer().getMana(), 10);
        assertEquals(cn3.data_.getCurrentPlayer().getHero().getHealth(), 30);
        assertEquals(cn3.data_.getWaitingPlayer().getHero().getHealth(), 30);

        assertEquals(cn3.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_1).getTotalHealth(), 3);
        assertEquals(cn3.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_2).getTotalHealth(), 4);
        assertEquals(cn3.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_3).getTotalHealth(), 3);
        assertEquals(cn3.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_4).getTotalHealth(), 6);
//        assertEquals(cn3.data_.getWaitingPlayer().getCharacter(CharacterIndex.MINION_1).getTotalHealth(), 1);
        assertEquals(cn3.data_.getWaitingPlayer().getCharacter(CharacterIndex.MINION_1).getTotalHealth(), 1);
        assertEquals(cn3.data_.getWaitingPlayer().getCharacter(CharacterIndex.MINION_2).getTotalHealth(), 2);
        assertEquals(cn3.data_.getWaitingPlayer().getCharacter(CharacterIndex.MINION_3).getTotalHealth(), 7);

        assertEquals(cn3.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 4);
        assertEquals(cn3.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 4);
        assertEquals(cn3.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_3).getTotalAttack(), 3);
        assertEquals(cn3.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_4).getTotalAttack(), 7);
//        assertEquals(cn3.data_.getWaitingPlayer().getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 3);
        assertEquals(cn3.data_.getWaitingPlayer().getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 5);
        assertEquals(cn3.data_.getWaitingPlayer().getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 2);
        assertEquals(cn3.data_.getWaitingPlayer().getCharacter(CharacterIndex.MINION_3).getTotalAttack(), 7);

        //Fifth child node is the one that kills the Abomination
        HearthTreeNode cn4 = ret.getChildren().get(4);
        assertEquals(cn4.data_.getCurrentPlayer().getHand().size(), 0);
        assertEquals(cn4.data_.getWaitingPlayer().getHand().size(), 1);
        assertEquals(cn4.data_.getCurrentPlayer().getNumMinions(), 4);
        assertEquals(cn4.data_.getWaitingPlayer().getNumMinions(), 1);
        assertEquals(cn4.data_.getCurrentPlayer().getMana(), 8);
        assertEquals(cn4.data_.getWaitingPlayer().getMana(), 10);
        assertEquals(cn4.data_.getCurrentPlayer().getHero().getHealth(), 28);
        assertEquals(cn4.data_.getWaitingPlayer().getHero().getHealth(), 28);

        assertEquals(cn4.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_1).getTotalHealth(), 1);
        assertEquals(cn4.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_2).getTotalHealth(), 2);
        assertEquals(cn4.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_3).getTotalHealth(), 1);
        assertEquals(cn4.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_4).getTotalHealth(), 4);
        assertEquals(cn4.data_.getWaitingPlayer().getCharacter(CharacterIndex.MINION_1).getTotalHealth(), 5);

        assertEquals(cn4.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 4);
        assertEquals(cn4.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 4);
        assertEquals(cn4.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_3).getTotalAttack(), 3);
        assertEquals(cn4.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_4).getTotalAttack(), 7);
        assertEquals(cn4.data_.getWaitingPlayer().getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 6);
    }
}
