# Bloody Coven

They fled. Their gardens were burnt, their familiars slaughtered and their stashes wiped.

Their art was decimated. In the hidden grove, using last ingredients they had, they summoned a demon.

They pledged for the help. For the help against magic-nullifying alchemy of the cowards proclaiming themselves hunters. And for the revenge.

The demon was appeased by the despair, the anguish and the wraith of the summoners. Or, maybe, he was just entertained...

In any case, he taught them a new way of performing their rituals. The gardens, the foraging, the alchemy, all of that was no longer needed... only the need for revenge... and blood.

# TODO

> basically, replacing gardening from Witchery with butchering both enemies and innocents and paying for rituals with your max hp

- [x] Blood and Glyphs
  - [x] Normal Glyphs
  - [x] Center Glyph - placed when four normal glyphs are around
  - [x] Blood Clot item - places blood glyphs

- [x] Boline item - collect materials and run rituals
  - [ ] drop new ingredients from entities
  - [x] remember last damaged entity, changing texture to blood covered one and displaying entity name in tooltip

- [ ] Hemorrhage effect – affected entities slowly lose hp and can die from this effect – unsure
- [ ] Anemia effect – affected entities don't regenerate or something else – unsure

- [ ] Ritual system:
  - [x] rituals performed by dropping items atop of a heart rune, then activating the rune and waiting for items to be consumed
  - [x] heart rune has to be surrounded by 4 normal runes, some rituals might use additional runes to shape the magic or increase the range of affected area
  - [ ] rituals are paid with the health of caster or casters, most advanced rituals reduce health permanently (i.e. creating blood golems/homunculi)
  - [ ] Boline covered in blood is used to target an entity in rituals (à la Witchery Taglock)

## Rituals

- ### Vital Transfusion

  Renders blood and vital energy from the ritual victim, leaving pale, drained body and increasing caster's maximum health. Only some creatures can be targeted (villagers, illagers, witches, other players wearing an item with curse of magic susceptibility).

- ### Lifeforce Funnel
  
  Blood lost within the area around ritual is captured by this ritual magic and transferred it to the caster or their allies, regaining lost hit points. Only creatures with blood are affected.

- ### Blood Mist

  You cause blood to boil and froth, creating a strong scent of blood that incites primal instincts in affected creatures. Predators will become agitated and especially aggressive, while pray animals will flee in terror.

- ### Blood Apparition

  You animate a bit of blood, creating a translucent ghastly creature. The apparition last for a short time, attacking creatures around and increasing it duration every time it successfully damages a creature with blood.

- ### Blood Ooze

  You rend the blood out of a victim and animate it, creating semi-sentient ooze, that will stay near the ritual circle and attack any creatures that have blood, except summoner or its allies. When killed, it explodes, damaging entities nearby and splitting into smaller a few smaller copies. If it kills a creature with blood or touches a pool of fresh blood, it absorbs it, regaining the size.

- ### Bloodbeast

  You drain the blood of a few victims and infuse it with sentience. This creature will guard the ritual circle, attacking any trespassers or enemies of its creator. If ritual is ended, the creature will no longer be able to regain hit points and will die within a day. The beast attacks enemies with its claws and powerful blood spells.

- ### Dark Synergy
  
  Creates a nexus of life force for a short time. While it's active, averages current life ratio of allies close to it.

- ### Dark Barrier

  A magical barrier springs forth from the blood runes in area. The barrier is a few blocks tall and cannot be passed while the ritual is active. (Runes neighboring a heart glyph are not affected.)