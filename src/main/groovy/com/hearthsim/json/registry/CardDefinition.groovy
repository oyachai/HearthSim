package com.hearthsim.json.registry

import groovy.transform.ToString

@ToString
class CardDefinition {

    String name
    String type
    String set
    int cost
    String playerClass
    String text
    List<String> mechanics

}
