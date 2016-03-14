package org.dementhium.model.combat.impl;

import org.dementhium.content.activity.impl.DuelActivity;
import org.dementhium.content.activity.impl.duel.DuelConfigurations;
import org.dementhium.content.activity.impl.duel.DuelConfigurations.Rules;
import org.dementhium.content.misc.Following;
import org.dementhium.identifiers.IdentifierManager;
import org.dementhium.model.Item;
import org.dementhium.model.Mob;
import org.dementhium.model.World;
import org.dementhium.model.combat.Combat;
import org.dementhium.model.combat.Combat.FightType;
import org.dementhium.model.combat.CombatAction;
import org.dementhium.model.combat.CombatHit;
import org.dementhium.model.combat.CombatUtils;
import org.dementhium.model.definition.WeaponInterface;
import org.dementhium.model.map.Directions;
import org.dementhium.model.map.path.PrimitivePathFinder;
import org.dementhium.model.mask.Graphic;
import org.dementhium.model.misc.DamageManager.DamageType;
import org.dementhium.model.misc.GodwarsUtils.Faction;
import org.dementhium.model.npc.impl.EliteBlackKnight;
import org.dementhium.model.npc.impl.TormentedDemon;
import org.dementhium.model.player.Bonuses;
import org.dementhium.model.player.Equipment;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;
import org.dementhium.net.ActionSender;
import org.dementhium.tickable.Tick;
import org.dementhium.util.Misc;

/**
 * @author 'Mystic Flow
 * @author `Discardedx2 <for small modifications to support specials>
 * @author Steve <added some specials>
 * @author Emperor <Combat following, hating on the system, ..>
 */
public final class MeleeCombat extends CombatAction {

    private static final CombatAction INSTANCE = new MeleeCombat();

    public static CombatAction getAction() {
        return INSTANCE;
    }


    private MeleeCombat() {

    }

    @Override
    public CombatHit hit(Mob mob, final Mob victim) {
        mob.getCombatState().setSpellDelay(3);
        mob.getCombatState().setAttackDelay(mob.isNPC() ? mob.getNPC().getDefinition().getAttackDelay() : mob.getPlayer().getAttackDelay());
        int maxDamage = damage(mob, FightType.MELEE);
        int damageInflicted = getHit(mob, victim, maxDamage);
        FightType type = FightType.MELEE;
        boolean specialPerformed = false;
        if (victim.isPlayer() && victim.getPlayer().getPrayer().usingCorrispondingPrayer(FightType.MELEE)) {
            damageInflicted *= 0.6;
        } else if (victim.isNPC() && Combat.usingProtection(victim.getNPC(), type)) {
            damageInflicted *= 0.6;
        }
        if (System.currentTimeMillis() - victim.getAttribute("meleeImmunity", 0L) < 60000)
            damageInflicted *= .5;
        if (mob.isPlayer() && mob.getPlayer().getEquipment().voidSet(1)) {
            damageInflicted *= 1.10;
        }
        if (mob.usingSpecial()) {
            Player player = mob.getPlayer();
            if (!performMeleeSpecials(player, victim, damageInflicted, maxDamage)) {
                player.reverseSpecialActive();
                ActionSender.sendChatMessage(player, 0, "You do not have enough special attack to perform this action.");
                return null;
            } else {
                player.reverseSpecialActive();
                specialPerformed = true;
            }
        }
        if (specialPerformed && mob.isPlayer() && mob.getPlayer().getEquipment().getSlot(Equipment.SLOT_WEAPON) == 14484) {
            return null;
        }
        if (specialPerformed) {
            if (mob.isPlayer() && mob.getPlayer().getEquipment().getSlot(Equipment.SLOT_WEAPON) == 19784 && !mob.getPlayer().isMulti()) {
                Player player = mob.getPlayer();
                int magicBonus = (int) ((player.getSkills().getLevel(Skills.MAGIC) + player.getBonuses().getBonus(Bonuses.MAGIC)) * 1.555);
                damageInflicted = Misc.random(damageInflicted) + Misc.random(magicBonus) + magicBonus;
                type = FightType.MAGIC;
            } else {
                if (damageInflicted > 0) {
                    damageInflicted *= CombatUtils.specialPower(mob.getPlayer(), mob.getPlayer().getEquipment().getSlot(3));
                }
            }
        }
        int attackAnimation = mob.getAttackAnimation();
        if (!specialPerformed) {
            mob.animate(attackAnimation);
        }
        if (!victim.isAnimating()) {
            victim.animate(victim.getDefenceAnimation());
        }
        if (damageInflicted > 0 && mob.isPlayer()) {
            appendExperience(mob.getPlayer(), damageInflicted);
        }
        if (mob.isPlayer() && mob.getPlayer().getEquipment().get(Equipment.SLOT_WEAPON) != null && mob.getPlayer().getEquipment().get(Equipment.SLOT_WEAPON).getDefinition().doesPoison()) {
            if (RANDOM.nextInt(100) < 60) {
                victim.getPoisonManager().poison(mob, mob.getPlayer().getEquipment().get(Equipment.SLOT_WEAPON).getDefinition().getPoisonAmount());
            }
        }
        if (mob.isNPC() && mob.getNPC().getId() == 1600) {
            if (RANDOM.nextInt(100) < 60) {
                victim.getPoisonManager().poison(mob, 80);
            }
        }
        if (victim.isNPC() && victim.getNPC() instanceof TormentedDemon) {
            TormentedDemon demon = (TormentedDemon) victim.getNPC();
            if (mob.isPlayer() && mob.getPlayer().getEquipment().get(Equipment.SLOT_WEAPON).getId() == 6746) {
                demon.resetShield();
            }
        }
        int cycles = 1;
        if (attackAnimation == 2661) {
            cycles = 2;
        }
        if (damageInflicted > victim.getHitPoints()) {
            damageInflicted = victim.getHitPoints();
        }
        return new CombatHit(mob, victim, damageInflicted, maxDamage, cycles).setFightType(type);
    }

    @Override
    public boolean canAttack(Mob mob, Mob victim) {
        //		if(!PrimitivePathFinder.canMove(mob.getLocation(), mob.getLocation().getX(), mob.getLocation().getY(), victim.getLocation().getX(), victim.getLocation().getY(), mob.getLocation().getZ(), mob.size(), mob.size())) {
        //			System.out.println("Hi");
        //			return false;
        //		}
        if (mob.isPlayer() && victim.isNPC() && (victim.getNPC() instanceof EliteBlackKnight)) {
            return false;
        }
        if (mob.isPlayer()) {
            Player player = mob.getPlayer();
            if (player.getActivity() instanceof DuelActivity) {
                if (((DuelActivity) player.getActivity()).getDuelConfigurations().getRule(Rules.MELEE)) {
                    player.sendMessage("Melee isn't allowed during this duel!");
                    player.resetCombat();
                    return false;
                }
                if (((DuelActivity) player.getActivity()).getDuelConfigurations().getRule(Rules.FUN_WEAPONS)) {
                    boolean hasWeapon = false;
                    for (int item : DuelConfigurations.FUN_WEAPONS) {
                        if (player.getEquipment().getSlot(Equipment.SLOT_WEAPON) == item) {
                            hasWeapon = true;
                            break;
                        }
                    }
                    if (!hasWeapon) {
                        player.sendMessage("You can only use fun weapons during this duel!");
                        return false;
                    }
                }
            }
            if (victim.isNPC()) {
                if (victim.getNPC().getDefinition().getFaction() != null && victim.getNPC().getDefinition().getFaction().equals(Faction.ARMADYL)) {
                    return false;
                }
            }
        }
        if (!CombatUtils.canMelee(mob, victim)) {
            Following.combatFollow(mob, victim);
            return false;
        }
        if (mob.size() < 2 && victim.size() < 2 && Combat.diagonal(mob.getLocation(), victim.getLocation())) {
            if (!mob.getCombatState().isFrozen() && !mob.getWalkingQueue().isMoving() && !victim.getWalkingQueue().isMoving()) {
                mob.requestClippedWalk(victim.getLocation().getX(), mob.getLocation().getY());
            }
        }
        if (victim.size() < 2 && !PrimitivePathFinder.canMove(mob.getLocation(), Directions.directionFor(mob.getLocation(), victim.getLocation()), false)) {
            return false;
        }
        return true;
    }

    private boolean performMeleeSpecials(final Player player, final Mob victim, int damage, final int maxDamage) {
        if (player.getSpecialAmount() > 1000 || player.getSpecialAmount() < 0) {
            return false;
        }
        Item wep = player.getEquipment().get(Equipment.SLOT_WEAPON);
        int id = wep != null ? wep.getId() : -1;
        if (player.getSpecialAmount() >= getSpecialDeduction(player, wep)) {
            switch (id) {
                case 11694://ags
                    player.animate(7074);
                    player.graphics(1222);
                    break;
                case 11696://bgs
                    player.animate(7073);
                    player.graphics(1223, 30 << 16);
                    break;
                case 11700:
                    player.animate(7070);
                    player.graphics(2110);
                    victim.graphics(2111);
                    victim.getCombatState().setFrozenTime(20);
                    break;
                case 1305: //d long
                    player.graphics(248, 100 << 16);
                    player.animate(1658);
                    break;
                case 13899:
                    player.animate(10502);
                    break;
                case 13905:
                    player.animate(10499);
                    player.graphics(1835, 0);
                    break;
                case 4151:
                    player.animate(11971);
                    victim.graphics(Graphic.create(2108, 96 << 16));
                    break;
                case 4153:
                    player.animate(1667);
                    player.graphics(340);
                    break;
                case 13902:
                    player.animate(10505);
                    player.graphics(1840);
                    if (victim != null && victim.isPlayer()) {
                        victim.getPlayer().getSkills().decreaseLevelToZero(3, (int) damage * (1 / 3));
                    }
                    break;
                case 4587: //d scim
                    player.animate(15078);
                    player.graphics(347, (100 << 16));
                    if (victim.isPlayer()) {
                        victim.getPlayer().setAttribute("restrict protection", 20);
                    }
                    break;
                case 11698://sgs
                    player.animate(7071);
                    player.graphics(1220);
                    int hitpointsHeal = damage / 2;
                    int prayerHeal = damage / 4;
                    player.getSkills().heal(hitpointsHeal);
                    player.getSkills().restorePray(prayerHeal);
                    break;
                case 6746://sgs
                    player.animate(2890);
                    player.graphics(483);
                    break;
                case 14484:
                    player.graphics(1950);
                    player.animate(10961);
                    final int hit = (int) (damage * 1.20);
                    int second = hit / 2, third = second / 2, fourth = third + 1;
                    if (hit == 0) second = third = 0;
                    if (hit == 0) {
                        if ((Math.random() * 10) < 4) {
                            second = (int) ((Math.random() * maxDamage) * 1.2);
                            third = second / 2;
                            fourth = third + 1;
                        }
                    }
                    if (second < 1 && (Math.random() * 10) < 6) {
                        third = (int) ((Math.random() * maxDamage) / 2);
                        fourth = third + 1;
                    }
                    if (third < 1 && (Math.random() * 10) < 8) {
                        fourth = (int) ((Math.random() * maxDamage) * 1.5);
                    }
                    final int[] hits = {hit, second, third, fourth};
                    World.getWorld().submit(new Tick(1) {
                        @Override
                        public void execute() {
                            stop();
                            victim.getDamageManager().damage(player, hits[0], maxDamage, DamageType.MELEE);
                            victim.getDamageManager().damage(player, hits[1], maxDamage, DamageType.MELEE);
                            victim.getDamageManager().damage(player, hits[2], maxDamage, DamageType.MELEE, 30);
                            victim.getDamageManager().damage(player, hits[3], maxDamage, DamageType.MELEE, 30);
                            IdentifierManager.get("combat_after_effect").identify(player, victim, null, FightType.MELEE);
                        }
                    });
                    break;
                case 1215:
                case 1231:
                case 5680:
                case 5698:
                case 13465:
                case 13466:
                case 13467:
                case 13468:
                case 13976:
                    player.animate(1062);
                    player.graphics(252, (100 << 16));
                    World.getWorld().submit(new Tick(1) {
                        @Override
                        public void execute() {
                            int damage = damage(player, FightType.MELEE);
                            int damageInflicted = getHit(player, victim, damage);
                            victim.getDamageManager().damage(player, damageInflicted, maxDamage, DamageType.MELEE);
                            this.stop();
                        }
                    });
                    break;
                case 19784:
                    player.animate(14788);
                    victim.graphics(2795);
                    /*	if (player.isMulti()) {
                         final int hit1 = (int) (damage * 1.40), hit2 = (int) (hit1 * .50), hit3 = (int) (hit1 * .75);
                         List<Player> players = Region.getLocalPlayers(player.getLocation(), 4);
                         for (int count = 0; count < 2; count++) {
                             final Player p = players.get(count);
                             if (p.equals(player)) {
                                 count--;
                                 continue;
                             }
                             if(canAttack(player, p) && CombatUtils.isDeepEnoughInWild(player, p)) {
                                 if (count == 0) {
                                     World.getWorld().submit(new Tick(2) {

                                         @Override
                                         public void execute() {
                                             p.getDamageManager().damage(player, hit2, maxDamage, DamageType.MELEE);
                                             p.graphics(2795);
                                             stop();
                                         }
                                     });
                                 } else if (count == 1) {
                                     World.getWorld().submit(new Tick(3) {

                                         @Override
                                         public void execute() {
                                             p.getDamageManager().damage(player, hit3, maxDamage, DamageType.MELEE);
                                             p.graphics(2795);
                                             stop();
                                         }
                                     });
                                 }
                             }
                         }
                     }*/
                    break;
            }
            player.deductSpecial(getSpecialDeduction(player, wep));
            return true;
        }
        return false;
    }

    public double attackBonus(Mob mob) {
        if (mob.isPlayer()) {
            Player player = mob.getPlayer();
            double attackLevel = player.getSkills().getLevel(Skills.ATTACK) + player.getPrayer().getAttackModifier();
            int type = player.getSettings().getCombatStyle();
            switch (type) {
                case WeaponInterface.STYLE_ACCURATE:
                    attackLevel += 3;
                    break;
                case WeaponInterface.STYLE_CONTROLLED:
                    attackLevel += 1;
                    break;
            }
            if (player.getEquipment().voidSet(1)) {
                attackLevel += 10;
            }
            int styleBonus = WeaponInterface.getBonusForType(player, player.getSettings().getCombatType(), false) + 1;
            return attackLevel + styleBonus;

        }
        int attackBonus = mob.getNPC().getDefinition().getBonuses()[3];
        if (attackBonus < 1) {
            attackBonus = 1;
        }
        return mob.getNPC().getDefinition().getAttackLevel() + attackBonus;
    }

    @Override
    public int getAborptionBonusId() {
        return Bonuses.MELEE_ABSORPTION;
    }
}
