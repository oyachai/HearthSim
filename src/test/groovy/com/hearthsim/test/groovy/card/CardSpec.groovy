package com.hearthsim.test.groovy.card

import com.hearthsim.model.BoardModel
import com.hearthsim.test.helpers.BoardModelBuilder
import org.junit.Assert
import spock.lang.Specification

class CardSpec extends Specification{

    void assertBoardDelta(BoardModel oldBoard, BoardModel newBoard, Closure deltaClosure) {
        def oldBoardCopy = oldBoard.deepCopy()
        def expectedBoard = new BoardModelBuilder(boardModel: oldBoardCopy).make(deltaClosure)
        Assert.assertEquals(expectedBoard, newBoard)
    }
}
