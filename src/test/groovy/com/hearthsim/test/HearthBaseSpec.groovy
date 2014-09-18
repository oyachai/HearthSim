package com.hearthsim.test

import spock.lang.Specification

import static org.hamcrest.CoreMatchers.notNullValue
import static spock.util.matcher.HamcrestSupport.that

abstract class HearthBaseSpec extends Specification{

    def assertCardInHand(Class cardClass, List hand){
        def foundCard = hand.find { it.class == cardClass }
        assert that(foundCard, notNullValue())
    }


}
