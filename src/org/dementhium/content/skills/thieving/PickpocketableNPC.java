package org.dementhium.content.skills.thieving;

import org.dementhium.model.Item;

import java.util.HashMap;
import java.util.Map;

/**
 * An enum containing the data for all pickpocketable NPCs.
 *
 * @author Emperor
 */
public enum PickpocketableNPC {

    /**
     * The man or women npcs.
     */
    MAN(new short[]{1, 2, 3, 4, 5, 6, 16, 24, 170, 351, 663, 728, 729, 1086, 3223, 3224,
            3225, 3915, 5923, 7873, 7874, 7875, 7876, 7877, 7878, 7879, 7909, 7910, 8010, 8011,
            12345, 12346, 12347, 25, 352, 353, 354, 360, 361, 362, 363, 2776, 3226, 5924, 7880,
            7881, 7882, 7883, 7884, 8012, 8013},
            new byte[]{1, 11, 21, 31}, new byte[]{1, 1, 11, 21}, 8.0, 8, 10, new Item(995, 3)),

    /**
     * The regular farmer npcs.
     */
    FARMER(new short[]{7, 291, 1377, 1757, 1758, 1759, 1760, 1761, 3917, 4925, 7128, 7129, 7130, 7131},
            new byte[]{10, 20, 30, 40}, new byte[]{1, 10, 20, 30}, 14.5, 8, 10, new Item(995, 9), new Item(5318, 1)),

    /**
     * The female H.A.M. members.
     */
    FEMALE_HAM(new short[]{1715}, new byte[]{15, 25, 35, 45}, new byte[]{1, 15, 25, 35}, 18.5, 7, 10,
            new Item(995, 12), new Item(590), new Item(1621), new Item(1623), new Item(1625), new Item(1269),
            new Item(321), new Item(2138), new Item(4298), new Item(4300), new Item(4302), new Item(4304),
            new Item(4306), new Item(4308), new Item(4310), new Item(1267), new Item(1353), new Item(199),
            new Item(201), new Item(203), new Item(205), new Item(686), new Item(697), new Item(453),
            new Item(688), new Item(314, 3)),

    /**
     * The male H.A.M. members.
     */
    MALE_HAM(new short[]{1714, 1716}, new byte[]{20, 30, 40, 50}, new byte[]{1, 20, 30, 40}, 22.5, 7, 20,
            new Item(995, 12), new Item(590), new Item(1621), new Item(1623), new Item(1625), new Item(1269),
            new Item(321), new Item(2138), new Item(4298), new Item(4300), new Item(4302), new Item(4304),
            new Item(4306), new Item(4308), new Item(4310), new Item(1267), new Item(1353), new Item(199),
            new Item(201), new Item(203), new Item(205), new Item(686), new Item(697), new Item(453),
            new Item(688), new Item(314, 3)),

    /**
     * The H.A.M. guards.
     */
    HAM_GUARD(new short[]{1710, 1711, 1712}, new byte[]{20, 30, 40, 50}, new byte[]{1, 20, 30, 40}, 22.5, 7, 30,
            new Item(995, 12), new Item(590), new Item(1621), new Item(1623), new Item(1625), new Item(1269),
            new Item(321), new Item(2138), new Item(4298), new Item(4300), new Item(4302), new Item(4304),
            new Item(4306), new Item(4308), new Item(4310), new Item(1267), new Item(1353), new Item(199),
            new Item(201), new Item(203), new Item(205), new Item(686), new Item(697), new Item(453),
            new Item(688), new Item(314, 3), new Item(8866), new Item(8867), new Item(8868), new Item(8869)),

    /**
     * The al-kharid warrior and Warrior woman npcs.
     */
    WARRIOR(new short[]{15, 18}, new byte[]{25, 35, 45, 55}, new byte[]{1, 25, 35, 45}, 26, 8, 20, new Item(995, 18)),

    /**
     * The rogues.
     */
    ROGUE(new short[]{187, 2267, 2268, 2269, 8122}, new byte[]{32, 42, 52, 62}, new byte[]{1, 32, 42, 52}, 35.5, 8, 20,
            new Item(995, 25), new Item(995, 40), new Item(1993), new Item(556, 2), new Item(1219), new Item(1523), new Item(1944)),

    /**
     * Cave goblins.
     */
    CAVE_GOBLIN(new short[]{5752, 5753, 5754, 5755, 5756, 5757, 5758, 5759, 5760, 5761, 5762, 5763, 5764, 5765, 5766, 5767, 5768},
            new byte[]{36, 46, 56, 66}, new byte[]{1, 36, 46, 56}, 40, 8, 10,
            new Item(995, 30), new Item(590), new Item(4522), new Item(4544), new Item(596), new Item(1939), new Item(441, 4), new Item(441), new Item(10981)),

    /**
     * The master farmer npcs.
     */
    MASTER_FARMER(new short[]{2234, 2235}, new byte[]{38, 48, 58, 68}, new byte[]{1, 38, 48, 58}, 43, 8, 30,
            new Item(5096), new Item(5097), new Item(5098), new Item(5099), new Item(5100), new Item(5101),
            new Item(5102), new Item(5103), new Item(5104), new Item(5105), new Item(5106), new Item(5291),
            new Item(5292), new Item(5293), new Item(5294), new Item(5295), new Item(5296), new Item(5297),
            new Item(5298), new Item(5299), new Item(5300), new Item(5301), new Item(5302), new Item(5304),
            new Item(5305), new Item(5306), new Item(5307), new Item(5308), new Item(5309), new Item(5310),
            new Item(5311), new Item(5312), new Item(5318), new Item(5319), new Item(5320), new Item(5321),
            new Item(5322), new Item(5323), new Item(5324)),

    /**
     * City guards.
     */
    GUARD(new short[]{9, 32, 206, 296, 297, 298, 299, 344, 345, 346, 368, 678, 812, 887, 1076, 1077, 1142,
            1143, 1144, 1145, 1146, 1147, 1148, 1149, 1150, 1296, 1297, 1374, 1805, 1806, 2699, 2700, 2701,
            2702, 2703, 2704, 2705, 2962, 2963, 2964, 2965, 2966, 2967, 2968, 2969, 2970, 2971, 2972, 2973,
            3228, 3229, 3230, 3231, 3232, 3233, 3241, 3407, 3408, 3715, 3941, 3942, 4257, 4258, 4259, 4260,
            4301, 4302, 4303, 4304, 4305, 4306, 4307, 4308, 4309, 4310, 4311, 4336, 4993, 4994, 4995, 4996,
            4997, 4998, 4999, 5000, 5001, 5002, 5489, 5490, 5491, 5492, 5800, 5801, 5919, 5920, 6183, 6184,
            6190, 6498, 6499, 6500, 6501, 6502, 6503, 7142, 8173, 10915, 10916, 10917, 10918, 10919, 10920,
            10921, 10922, 10923, 10924, 10925, 10926, 10927, 10928, 10929, 10930, 10931, 10932, 11271},
            new byte[]{40, 50, 60, 70}, new byte[]{1, 40, 50, 60}, 46.5, 8, 20, new Item(995, 30)),

    /**
     * Fremennik citizen.
     * TODO: correct npc ids.
     */
    FREMENNIK_CITIZEN(new short[]{2462}, new byte[]{45, 55, 65, 75},
            new byte[]{1, 45, 55, 65}, 65, 8, 20, new Item(995, 40)),

    /**
     * A knight of ardougne.
     */
    ARDOUGNE_KNIGHT(new short[]{23, 26}, new byte[]{55, 65, 75, 85}, new byte[]{1, 55, 65, 75}, 84.3, 8, 30, new Item(995, 50)),

    /**
     * A menaphite thug.
     */
    MENAPHITE_THUG(new short[]{1905}, new byte[]{65, 75, 85, 95}, new byte[]{1, 65, 75, 85}, 137.5, 8, 50, new Item(995, 60)),

    /**
     * A paladin.
     */
    PALADIN(new short[]{20, 2256}, new byte[]{70, 80, 90, 100}, new byte[]{1, 70, 80, 90}, 151.75, 8, 30, new Item(995, 80), new Item(562, 2)),

    /**
     * A monkey knife fighter.
     */
    MONKEY_KNIFE_FIGHTER(new short[]{13195, 13212, 13213}, new byte[]{70, 127, 127, 127}, new byte[]{1, 1, 1, 1}, 150, 8, 10,
            new Item(995, 1), new Item(995, 50), new Item(869, 4), new Item(874, 2), new Item(379), new Item(1331), new Item(1333), new Item(4587)),

    /**
     * Gnomes.
     */
    GNOME(new short[]{66, 67, 68, 168, 169, 2249, 2250, 2251, 2371, 2649, 2650, 6002, 6004}, new byte[]{75, 85, 95, 105}, new byte[]{1, 75, 85, 95}, 198.5, 8, 10,
            new Item(995, 300), new Item(557), new Item(444), new Item(569), new Item(2150), new Item(2162)),

    /**
     * Heroes.
     */
    HERO(new short[]{21}, new byte[]{80, 90, 100, 110}, new byte[]{1, 80, 90, 100}, 275, 10, 40, new Item(995, 200), new Item(995, 300), new Item(560, 2),
            new Item(565), new Item(569), new Item(1601), new Item(444), new Item(1993)),

    /**
     * Elves.
     */
    //TODO: Find what elfs are pickpocketable. ELF(new short[] { }, new byte[] { 85, 95, 105, 115 }, new byte[] { 1, 85, 95, 105 }, 353, 10, 50),

    /**
     * Dwarf traders.
     */
    DWARF_TRADER(new short[]{2109, 2110, 2111, 2112, 2113, 2114, 2115, 2116, 2117, 2118, 2119, 2120, 2121, 2122, 2123, 2124, 2125, 2126},
            new byte[]{90, 100, 110, 120}, new byte[]{1, 90, 100, 110}, 556.5, 8, 10, new Item(995, 100), new Item(995, 400),
            new Item(2350), new Item(2352), new Item(2354), new Item(2360), new Item(2362), new Item(2364), new Item(437), new Item(439),
            new Item(441), new Item(448), new Item(450), new Item(452), new Item(454));

    /**
     * A hashmap containing all the npc pickpocketing data.
     */
    private static final Map<Short, PickpocketableNPC> NPCS = new HashMap<Short, PickpocketableNPC>();

    /**
     * Gets the pickpocketing data from the mapping, depending on the NPC id.
     *
     * @param id The npc id.
     * @return The {@code PickpocketableNPC} {@code Object}, or {@code null} if the data was non-existant.
     */
    public static PickpocketableNPC get(int id) {
        return NPCS.get((short) id);
    }

    /**
     * Populate the mapping.
     */
    static {
        for (PickpocketableNPC data : PickpocketableNPC.values()) {
            for (short id : data.npcIds) {
                NPCS.put(id, data);
            }
        }
    }

    /**
     * The npc ids.
     */
    private final short[] npcIds;

    /**
     * The thieving levels required (slot 0 = normal loot, 1 = double, 2 = 3x loot, 4 = 4x loot).
     */
    private final byte[] thievingLevels;

    /**
     * The agility levels required (see thievingLevels[] comment for slots).
     */
    private final byte[] agilityLevels;

    /**
     * The experience gained.
     */
    private final double experience;

    /**
     * The stun time.
     */
    private final byte stunTime;

    /**
     * The stun damage to deal.
     */
    private final byte stunDamage;

    /**
     * The possible loot.
     */
    private final Item[] loot;

    /**
     * Constructs a new {@code PickpocketableNPC} {@code Object}.
     *
     * @param npcIds        The array containing all the npc ids of the npcs using this pickpocket data.
     * @param thievingLevel The thieving levels required (slot 0 = normal loot, 1 = double, 2 = 3x loot, 4 = 4x loot).
     * @param agilityLevel  The agility levels required (see slots above).
     * @param experience    The experience gained.
     * @param stunTime      The stun time (in 600ms ticks).
     * @param stunDamage    The stun damage to deal when stunned.
     * @param loot          The possible loot.
     */
    private PickpocketableNPC(short[] npcIds, byte[] thievingLevel, byte[] agilityLevel, double experience,
                              int stunTime, int stunDamage, Item... loot) {
        this.npcIds = npcIds;
        this.thievingLevels = thievingLevel;
        this.agilityLevels = agilityLevel;
        this.experience = experience;
        this.stunTime = (byte) stunTime;
        this.stunDamage = (byte) stunDamage;
        this.loot = loot;
    }

    /**
     * @return the npcIds
     */
    public short[] getNpcIds() {
        return npcIds;
    }

    /**
     * @return the thievingLevels
     */
    public byte[] getThievingLevels() {
        return thievingLevels;
    }

    /**
     * @return the agilityLevels
     */
    public byte[] getAgilityLevels() {
        return agilityLevels;
    }

    /**
     * @return the experience
     */
    public double getExperience() {
        return experience;
    }

    /**
     * @return the stunTime
     */
    public byte getStunTime() {
        return stunTime;
    }

    /**
     * @return the stunDamage
     */
    public byte getStunDamage() {
        return stunDamage;
    }

    /**
     * @return the loot
     */
    public Item[] getLoot() {
        return loot;
    }
}