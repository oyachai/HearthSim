#!/usr/bin/env python

import json

prefix = "com.hearthsim.card."

orig = open("/Users/oyachai/Documents/HearthSim/src/main/resources/implemented_cards.json", "r")
new = open("implemented_cards.json", "w")

datafile = open("/Users/oyachai/Documents/HearthSim/src/main/resources/AllSets.json", "r")

orig_json = json.loads(orig.read())
data_json = json.loads(datafile.read())



for card in orig_json:
	cardname = card["name"]
	found = False
	for card_set_key in data_json.keys():
		for data_card in data_json[card_set_key]:
			if data_card["name"] == cardname:
				found = True

				set_name = card_set_key.lower().replace(' ', '')
				type_name = data_card['type'].lower()
				if not type_name in ('minion', 'spell', 'weapon'):
					continue
				rarity_name = data_card['rarity'].lower() if 'rarity' in data_card else ""
				card_name = data_card['name'].replace("'", "").title().replace(" ", "").replace("-", "").replace(":", "")
				new_class = prefix + set_name + "." + type_name + ( "." + rarity_name if set_name != "basic" else "" ) + "." + card_name
				card['class'] = new_class
				print new_class
				break
		if found:
			break


orig.close()
datafile.close()

new.write(json.dumps(orig_json, indent=4))
new.close()

