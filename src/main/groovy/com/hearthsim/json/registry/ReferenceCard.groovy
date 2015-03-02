package com.hearthsim.json.registry

import groovy.transform.ToString

@ToString
class ReferenceCard {

    String id
    String name
    String type
    String set
    String rarity
    Integer cost
    String playerClass
    String text
    List<String> mechanics
    Integer attack
    Integer health
    Integer durability
    Boolean collectible
    String race
}
