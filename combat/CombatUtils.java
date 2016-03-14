package org.dementhium.model.combat;

import org.dementhium.content.misc.Following;
import org.dementhium.model.Item;
import org.dementhium.model.Location;
import org.dementhium.model.Mob;
import org.dementhium.model.definition.WeaponInterface;
import org.dementhium.model.player.Equipment;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;

import java.util.List;

/**
 * @author `Discardedx2 <the_shawn@discardedx2.info>
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class CombatUtils {

    public static boolean isDeepEnoughInWild(Mob mob, Mob victim) {
        if (mob.isPlayer() && victim.isPlayer()) {
            if (mob.getActivity().isRunning()) {
                return true;
            }
            int mobRange = mob.getPlayer().getSkills().getCombatLevel() + mob.getLocation().getWildernessLevel();
            int mobRange2 = mobRange - (mob.getLocation().getWildernessLevel() * 2);//Multiply by two because we have to remove what we added before!
            int victimRange = victim.getPlayer().getSkills().getCombatLevel() + victim.getLocation().getWildernessLevel();
            int victimRange2 = victimRange - (victim.getLocation().getWildernessLevel() * 2);
            if (!mob.inWilderness() || !victim.inWilderness()) {
                return false;
            }
            if (victim.getPlayer().getSkills().getCombatLevel() > mobRange || victim.getPlayer().getSkills().getCombatLevel() < mobRange2) {
                ActionSender.sendMessage(mob.getPlayer(), "You need to move deeper into the Wilderness to attack this player");
                //	mob.requestWalk(victim.getLocation().getX() - 1, victim.getLocation().getY());
                return false;
            }
            if (mob.getPlayer().getSkills().getCombatLevel() > victimRange || mob.getPlayer().getSkills().getCombatLevel() < victimRange2) {
                ActionSender.sendMessage(mob.getPlayer(), "That player is not deep enough in the Wilderness for you to attack.");
                //	mob.requestWalk(victim.getLocation().getX() - 1, victim.getLocation().getY());
                return false;
            }
            return mob.inWilderness() && victim.inWilderness();
        }
        return true;
    }


    public static double specialPower(Player p, int sword) {
    	//Hai I am Emperor and I declare this method: Shit.
        switch (sword) { //whoever has been editing this message Mystic
            case 13899:
                return 1.43;
            case 13902:
                return 1.55;
            case 19784:
                return 1.40;
            case 11694:
                return 1.25;
            case 11696:
                return 1.15;
            case 11698:
            case 11700:
                return 1.10;
            case 3101:
            case 3204:
            case 1215:
            case 1231:
            case 5680:
            case 5698:
                return 1.18;
            case 14484:
                return 1.18;
            case 1305:
                return 1.1;
            case 1434:
                return 1.1;
            case 6746:
                return 1.05;
        }
        return 1.1;
    }

    public static int getAttackAnimation(Mob mob) {
    	//Hai I am Emperor and I declare this method: Extreme Shit.
        if (mob.isPlayer()) {
            Player player = mob.getPlayer();
            Item weapon = player.getEquipment().get(Equipment.SLOT_WEAPON);
            if (weapon != null) {
                String name = weapon.getDefinition().getName();
                if (name.contains("javelin"))
                    return 806;
                if (name.contains("knife"))
                    return 929;
                if (name.contains("dart"))
                    return 582;
                if (name.contains("maul")) {
                    if (name.startsWith("Granite"))
                        return 1665; //Granite Maul anim
                    return 2661;
                }
                if (name.contains("scimitar") || name.contains("Darklight") && !name.equals("Dragon scimitar")) {
                    if (player.getSettings().getCombatStyle() == WeaponInterface.STYLE_CONTROLLED)
                        return 12028;
                    return 12029;
                }
                if (name.equals("Dragon scimitar")) {
                    return 15071;
                }
                switch (weapon.getId()) {
                    case -1:
                        if (player.getSettings().getCombatStyle() == WeaponInterface.STYLE_AGGRESSIVE) {
                            return 423; // kick
                        } else {
                            return 422; // punch
                        }
                    case 19784: //korasi'
                        return 12029;
                    case 4726:
                        return 2080;
                    case 4718:
                        if (player.getSettings().getCombatType() == WeaponInterface.TYPE_CRUSH)
                            return 12003;
                        return 12002;
                    case 14484:
                        return 393;
                    case 15241:
                        return 12152;
                    case 10034:
                        return 2779;
                    case 6526:
                    case 6908:
                    case 6910:
                    case 6912:
                    case 6914:
                    case 13867:
                    case 13869:
                    case 13941:
                    case 13943:
                    case 18355:
                        return 419;
                    case 4068:
                    case 4503:
                    case 4508:
                    case 18705:
                        if (player.getSettings().getCombatStyle() == WeaponInterface.STYLE_CONTROLLED)
                            return 12310;
                        return 12311;
                    case 11696:
                    case 11694:
                    case 11698:
                    case 11700:
                    case 11730:
                    case 1307:
                    case 1309:
                    case 1311:
                    case 1313:
                    case 1315:
                    case 1317:
                    case 1319:
                        if (player.getSettings().getCombatStyle() == WeaponInterface.STYLE_DEFENSIVE)
                            return 7049;
                        else if (player.getSettings().getCombatType() == WeaponInterface.TYPE_CRUSH)
                            return 7048;
                        return 7041;
                    case 18349:
                        if (player.getSettings().getCombatStyle() == WeaponInterface.STYLE_CONTROLLED)
                            return 13048;
                        return 13049;
                    case 18351:
                        if (player.getSettings().getCombatStyle() == WeaponInterface.STYLE_CONTROLLED)
                            return 13049;
                        return 13048;
                    case 14679:
                        return 401;
                    case 13899:
                    case 13901:
                    case 13923:
                    case 13925:
                        if (player.getSettings().getCombatStyle() == WeaponInterface.STYLE_CONTROLLED)
                            return 13049;
                        return 13048;
                    case 13902:
                    case 13904:
                    case 13926:
                    case 13928:
                        return 401;
                    case 15486:
                        if (player.getSettings().getCombatStyle() == WeaponInterface.STYLE_AGGRESSIVE)
                            return 12029;
                        else if (player.getSettings().getCombatStyle() == WeaponInterface.STYLE_DEFENSIVE)
                            return 414;
                        return 12028;
                    case 11716:
                        if (player.getSettings().getCombatType() == WeaponInterface.TYPE_STAB)
                            return 12006;
                        else if (player.getSettings().getCombatType() == WeaponInterface.TYPE_SLASH)
                            return 12005;
                        else if (player.getSettings().getCombatType() == WeaponInterface.TYPE_CRUSH)
                            return 12009;
                        return 12006;
                    case 9174:
                    case 9175:
                    case 9176:
                    case 9177:
                    case 9178:
                    case 9179:
                    case 9180:
                    case 9181:
                    case 9182:
                    case 9183:
                    case 9184:
                    case 9185:
                    case 9186:
                        return 4230;
                    case 1265:
                    case 1266:
                    case 1267:
                    case 1268:
                    case 1269:
                    case 1270:
                    case 1271:
                    case 1272:
                    case 1273:
                    case 1274:
                    case 1275:
                    case 1276:
                        return 401;
                    case 4755:
                        return 2062;
                    case 10887:
                        return 5865;
                    case 4151:
                    case 15441:
                    case 15442:
                    case 15443:
                    case 15444:
                        return 11968; // Whip
                    case 1215:
                    case 1231:
                    case 5680:
                    case 5698:
                    case 13465:
                    case 13467:
                    case 13976:
                    case 13978:
                        return 402; // Dragon daggers
                    case 4214:
                    case 6724:
                    case 4212:
                    case 4827:
                    case 11235:
                    case 841:
                    case 843:
                    case 849:
                    case 853:
                    case 856:
                    case 861:
                    case 839:
                    case 845:
                    case 847:
                    case 851:
                    case 855:
                    case 859:
                        return 426; // Bows
                    case 18357:
                        return 4230; // Crossbows
                    case 4734:
                        return 2075; // Karil x-bow
                    case 6528:
                        return 2661; // Obby maul
                    case 1434:
                        if (player.getSettings().getCombatType() == WeaponInterface.TYPE_STAB)
                            return 400;
                        return 401;
                    case 1305:
                        if (player.getSettings().getCombatType() == WeaponInterface.TYPE_STAB)
                            return 12310;
                        return 12311;
                }
            } else {
                return player.getSettings().getCombatStyle() == WeaponInterface.STYLE_AGGRESSIVE ? 423 : 422;
            }
        } else {
            return 1403;
        }
        return 422;
    }


    public static double specialBonus(Player player) {
        switch (player.getEquipment().getSlot(Equipment.SLOT_WEAPON)) {
            case 3101:
            case 3204:
            case 1215:
            case 1231:
            case 5680:
            case 5698:
                return 1.15;
            case 14484:
                return 1.20;
            case 6746:
                return 1.05;
        }
        return 1.0;
    }

    /**
     * Checks if the mob can use melee combat to attack the victim.
     *
     * @param mob    The mob.
     * @param victim The victim.
     * @return {@code True} if so, {@code false} if not.
     */
    public static boolean canMelee(Mob mob, Mob victim) {
        boolean sizeOne = mob.size() < 2 && victim.size() < 2;
        List<Location> mobTiles = Following.getInternTiles(mob, mob.size() < 2);
        List<Location> victimTiles = Following.getExternTiles(victim, sizeOne);
        for (Location sl : mobTiles) {
            if (victimTiles.contains(sl)) {
                mob.getWalkingQueue().reset();
                return true;
            }
        }
        return false;
    }

}
