/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nde.definitions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.NodeList;

/**
 * Parses (Loads & saves) xml files.
 * @author Emperor
 */
public class XMLParsing {

    	/**
	 * The document builder instance.
	 */
	private static DocumentBuilder builder;

	/**
	 * The document builder factory instance.
	 */
	private static final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        /**
         * Loads the NPC definitions, and sets them in the {@code NDEUpdate} instance.
         * @param defs The .XML file used to load the definitions from.
         */
        public static void load(NDEUpdate npcDef, File defs) {
		if (defs.exists()) {
			Document doc;
			NodeList dialogueList = null;
			try {
				builder = factory.newDocumentBuilder();
				doc = builder.parse(defs);
				//doc.getDocumentElement().normalize();
				dialogueList = doc.getDocumentElement().getChildNodes();
			} catch (Exception e) {
                            System.out.println("Exception caught: " + e);
			}
			npcDef.setCombatLevel(Short.parseShort(dialogueList.item(1).getTextContent()));
			npcDef.setExamine(dialogueList.item(3).getTextContent());
			int count = 0;
			for (int i = 5; i < 32; i += 2) {
				npcDef.setBonus(count++, Short.parseShort(dialogueList.item(i).getTextContent()));
			}
			npcDef.setLifepoints(Short.parseShort(dialogueList.item(33).getTextContent()));
			npcDef.setRespawn(Byte.parseByte(dialogueList.item(35).getTextContent()));
			npcDef.setAttackAnimation(Short.parseShort(dialogueList.item(37).getTextContent()));
			npcDef.setDefenceAnimation(Short.parseShort(dialogueList.item(39).getTextContent()));
			npcDef.setDeathAnimation(Short.parseShort(dialogueList.item(41).getTextContent()));
			npcDef.setStrengthLevel(Short.parseShort(dialogueList.item(43).getTextContent()));
			npcDef.setAttackLevel(Short.parseShort(dialogueList.item(45).getTextContent()));
			npcDef.setDefenceLevel(Short.parseShort(dialogueList.item(47).getTextContent()));
			npcDef.setRangeLevel(Short.parseShort(dialogueList.item(49).getTextContent()));
			npcDef.setMagicLevel(Short.parseShort(dialogueList.item(51).getTextContent()));
			npcDef.setAttackSpeed(Byte.parseByte(dialogueList.item(53).getTextContent()));
			npcDef.setStartGraphics(Short.parseShort(dialogueList.item(55).getTextContent()));
			npcDef.setProjectileId(Short.parseShort(dialogueList.item(57).getTextContent()));
			npcDef.setEndGraphics(Short.parseShort(dialogueList.item(59).getTextContent()));
			npcDef.setUsingMelee(Boolean.parseBoolean(dialogueList.item(61).getTextContent()));
			npcDef.setUsingRange(Boolean.parseBoolean(dialogueList.item(63).getTextContent()));
			npcDef.setUsingMagic(Boolean.parseBoolean(dialogueList.item(65).getTextContent()));
			npcDef.setAggressive(Boolean.parseBoolean(dialogueList.item(67).getTextContent()));
                        npcDef.setPoisonImmune(Boolean.parseBoolean(dialogueList.item(69).getTextContent()));
            }
        }

        public static void save(NDEUpdate defs) throws IOException {
            File f = new File("./data/NPCs/NPCDefinition" + defs.getNpcId() + ".xml");
            f.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            bw.write("	<NpcDefinition>");
            bw.newLine();
            bw.write("		<CombatLevel>" + defs.getCombatLevel() + "</CombatLevel>");
            bw.newLine();
            bw.write("		<Examine>" + defs.getExamine() + "</Examine>");
            bw.newLine();
            for (int i = 0; i < defs.getBonuses().length; i++) {
                bw.write("		<Bonus" + i + ">" + defs.getBonuses()[i] + "</Bonus" + i + ">");
                bw.newLine();
            }
            bw.write("		<LifePoints>" + defs.getLifepoints() + "</LifePoints>");
            bw.newLine();
            bw.write("		<Respawn>" + defs.getRespawn() + "</Respawn>");
            bw.newLine();
            bw.write("          <AttackAnimation>" + defs.getAttackAnimation() + "</AttackAnimation>");
            bw.newLine();
            bw.write("          <DefenceAnimation>" + defs.getDefenceAnimation() + "</DefenceAnimation>");
            bw.newLine();
            bw.write("          <DeathAnimation>" + defs.getDeathAnimation() + "</DeathAnimation>");
            bw.newLine();
            bw.write("		<StrengthLevel>" + defs.getStrengthLevel() + "</StrengthLevel>");
            bw.newLine();
            bw.write("		<AttackLevel>" + defs.getAttackLevel() + "</AttackLevel>");
            bw.newLine();
            bw.write("		<DefenceLevel>" + defs.getDefenceLevel() + "</DefenceLevel>");
            bw.newLine();
            bw.write("		<RangeLevel>" + defs.getRangeLevel() + "</RangeLevel>");
            bw.newLine();
            bw.write("		<MagicLevel>" + defs.getMagicLevel() + "</MagicLevel>");
            bw.newLine();
            bw.write("		<AttackSpeed>" + defs.getAttackSpeed() + "</AttackSpeed>");
            bw.newLine();
            bw.write("		<StartGraphics>" + defs.getStartGraphics() + "</StartGraphics>");
            bw.newLine();
            bw.write("		<ProjectileId>" + defs.getProjectileId() + "</ProjectileId>");
            bw.newLine();
            bw.write("		<EndGraphics>" + defs.getEndGraphics() + "</EndGraphics>");
            bw.newLine();
            bw.write("		<UsingMelee>" + defs.isUsingMelee() + "</UsingMelee>");
            bw.newLine();
            bw.write("		<UsingRange>" + defs.isUsingRange() + "</UsingRange>");
            bw.newLine();
            bw.write("		<UsingMagic>" + defs.isUsingMagic() + "</UsingMagic>");
            bw.newLine();
            bw.write("		<Aggressive>" + defs.isAggressive() + "</Aggressive>");
            bw.newLine();
            bw.write("		<PoisonImmune>" + defs.isPoisonImmune() + "</PoisonImmune>");
            bw.newLine();
            bw.write("	</NpcDefinition>");
            bw.newLine();
            bw.close();
        }
}
