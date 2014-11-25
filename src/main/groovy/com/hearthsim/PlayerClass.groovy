package com.hearthsim


public enum PlayerClass {

    DRUID('Druid'),
    WARRIOR('Warrior'),
    MAGE('Mage'),
    ROGUE('Rogue'),
    SHAMAN('Shaman'),
    WARLOCK('Warlock'),
    HUNTER('Hunter'),
    PALADIN('Paladin'),
    PRIEST('Priest'),
    NEUTRAL('Neutral')

    PlayerClass(String referenceName) {
        this.referenceName = referenceName
    }

    String referenceName
}
