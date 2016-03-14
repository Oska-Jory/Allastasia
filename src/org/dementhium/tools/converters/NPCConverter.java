package org.dementhium.tools.converters;

import org.dementhium.cache.Cache;
import org.dementhium.io.XMLHandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Steve <golden_32@live.com>
 */
public class NPCConverter {

    public class NPCDefintion {
        /*<id>4393</id>
          <hitpoints>180</hitpoints>
          <maximumHit>60</maximumHit>
          <attackAnimation>5578</attackAnimation>
          <defenceAnimation>5579</defenceAnimation>
          <deathAnimation>5580</deathAnimation>
          <attackLevel>35</attackLevel>
          <defenceLevel>28</defenceLevel>
          <meleeAttack>30</meleeAttack>
          <stabDefence>25</stabDefence>
          <slashDefence>15</slashDefence>
          <crushDefence>28</crushDefence>
          <rangeDefence>28</rangeDefence>
          <magicDefence>28</magicDefence>*/
        public int id, hitpoints, maximumHit, attackSpeed, attackAnimation, defenceAnimation, strengthLevel, deathAnimation, attackLevel, defenceLevel, meleeAttack, stabDefence, slashDefence, crushDefence, rangeDefence, magicDefence;
        public int rangeLevel;
        public int magicLevel;
    }

    public static ArrayList<NPCDefintion> defsToConvert = new ArrayList<NPCDefintion>();

    public static void main(String[] args) {
        try {
            Cache.init();
            defsToConvert = XMLHandler.fromXML("./data/npcs/npcDefinitions.xml");
            convert();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void convert() {
        String directory = new File("./").getAbsolutePath().replace("Dementhium 637", "NDE/data/NPCs");
        for (NPCDefintion def : defsToConvert) {
            File defs = new File(directory + "/NPCDefinition" + def.id + ".xml");
            if (!defs.exists()) {
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(defs));
                    writer.write("<NpcDefinition>");
                    writer.write("		<CombatLevel>1</CombatLevel> \n"
                            + "<Examine>Examine not added</Examine> \n"
                            + "<Bonus0>" + def.stabDefence + "</Bonus0> \n"
                            + "<Bonus1>" + def.slashDefence + "</Bonus1> \n"
                            + "<Bonus2>" + def.crushDefence + "</Bonus2> \n"
                            + "<Bonus3>" + def.magicDefence + "</Bonus3> \n"
                            + "<Bonus4>" + def.rangeDefence + "</Bonus4> \n"
                            + "<Bonus5>" + def.stabDefence + "</Bonus5> \n"
                            + "<Bonus6>" + def.slashDefence + "</Bonus6> \n"
                            + "<Bonus7>" + def.crushDefence + "</Bonus7> \n"
                            + "<Bonus8>" + def.magicDefence + "</Bonus8> \n"
                            + "<Bonus9>" + def.stabDefence + "</Bonus9> \n"
                            + "<Bonus10>" + def.slashDefence + "</Bonus10> \n"
                            + "<Bonus11>" + def.stabDefence + "</Bonus11> \n"
                            + "<Bonus12>" + def.stabDefence + "</Bonus12> \n"
                            + "<Bonus13>" + def.stabDefence + "</Bonus13> \n"
                            + "<LifePoints>" + def.hitpoints + "</LifePoints> \n"
                            + "<Respawn>30</Respawn> \n"
                            + "<AttackAnimation>" + def.attackAnimation + "</AttackAnimation> \n"
                            + "<DefenceAnimation>" + def.defenceAnimation + "</DefenceAnimation> \n"
                            + " <DeathAnimation>" + def.deathAnimation + "</DeathAnimation> \n"
                            + "<StrengthLevel>" + def.strengthLevel + "</StrengthLevel> \n"
                            + "<AttackLevel>" + def.attackLevel + "</AttackLevel> \n"
                            + "<DefenceLevel>" + def.defenceLevel + "</DefenceLevel> \n"
                            + "<RangeLevel>" + def.rangeLevel + "</RangeLevel> \n"
                            + "<MagicLevel>" + def.magicLevel + "</MagicLevel> \n"
                            + "<AttackSpeed>5</AttackSpeed> \n"
                            + "<StartGraphics>-1</StartGraphics> \n"
                            + "<ProjectileId>-1</ProjectileId> \n"
                            + "<EndGraphics>-1</EndGraphics> \n"
                            + "<UsingMelee>true</UsingMelee> \n"
                            + "<UsingRange>false</UsingRange> \n"
                            + "<UsingMagic>false</UsingMagic> \n"
                            + "<Aggressive>false</Aggressive> \n"
                            + "<PoisonImmune>false</PoisonImmune> \n");
                    writer.write("</NpcDefinition>");
                    writer.flush();
                    System.out.println("Converted " + def.id);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
