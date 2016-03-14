package org.dementhium.model.combat;

import org.dementhium.cache.format.CacheNPCDefinition;
import org.dementhium.content.activity.impl.DuelActivity;
import org.dementhium.content.activity.impl.duel.DuelConfigurations.Rules;
import org.dementhium.content.skills.Prayer;
import org.dementhium.identifiers.IdentifierManager;
import org.dementhium.model.Location;
import org.dementhium.model.Mob;
import org.dementhium.model.World;
import org.dementhium.model.map.Directions.WalkingDirection;
import org.dementhium.model.map.path.PrimitivePathFinder;
import org.dementhium.model.misc.DamageManager.DamageType;
import org.dementhium.model.npc.NPC;
import org.dementhium.model.npc.impl.EliteBlackKnight;
import org.dementhium.model.npc.impl.godwars.Nex;
import org.dementhium.model.npc.impl.godwars.Nex.NexAreaEvent;
import org.dementhium.model.npc.impl.summoning.Familiar;
import org.dementhium.model.player.Equipment;
import org.dementhium.model.player.Skills;
import org.dementhium.tickable.Tick;

/**
 * @author 'Mystic Flow
 */
public final class Combat {

    /**
     * @author 'Mystic Flow
     */
    public static enum FightType {
        RANGE(Skills.RANGE, DamageType.RANGE),
        MAGIC(Skills.MAGIC, DamageType.MAGE),
        MELEE(Skills.STRENGTH, DamageType.MELEE),
        DRAGONFIRE(Skills.MAGIC, DamageType.RED_DAMAGE);

        private final int skill;
        private final DamageType damageType;

        private FightType(int skill, DamageType damageType) {
            this.skill = skill;
            this.damageType = damageType;
        }

        public int getSkill() {
            return skill;
        }
    }

    private static WalkingDirection[] DIRECTIONS =
            {
                    WalkingDirection.WEST, WalkingDirection.EAST, WalkingDirection.SOUTH, WalkingDirection.NORTH,
                    WalkingDirection.NORTH_EAST, WalkingDirection.NORTH_WEST, WalkingDirection.SOUTH_EAST, WalkingDirection.SOUTH_WEST
            };


    public static boolean canAttack(Mob mob, Mob victim) {
        if (mob == null || victim == null) {
            return false;
        }
        if (mob.isDead() || victim.isDead() || victim.destroyed()) {
            mob.resetCombat();
            return false;
        }
        if (mob.isNPC() && !mob.getNPC().isAttackable()) {
            return false;
        }
        if (victim.isNPC() && victim.getNPC().isNex()) {
            Nex nex = NexAreaEvent.getNexAreaEvent().getNex();
            if (!nex.isAttackable()) {
                return false;
            }
        }
        if (mob.isNPC() && mob.getNPC().isNex()) {
            Nex nex = NexAreaEvent.getNexAreaEvent().getNex();
            if (!nex.isAttackable()) {
                return false;
            }
        }
        if (mob.isPlayer() && victim.isNPC() && (victim.getNPC() instanceof EliteBlackKnight)) {
            return false;
        }
        if (victim.isNPC() && victim.getNPC() instanceof Familiar) {
            if (victim.isMulti() || (mob.isPlayer() && mob.getPlayer().getActivity() instanceof DuelActivity && ((DuelActivity) mob.getPlayer().getActivity()).getDuelConfigurations().getRule(Rules.SUMMONING))) {
                if (!victim.inWilderness() && !(mob.isPlayer() && mob.getPlayer().getActivity() instanceof DuelActivity && ((DuelActivity) mob.getPlayer().getActivity()).getDuelConfigurations().getRule(Rules.SUMMONING))) {
                    mob.getPlayer().sendMessage("You can only attack a familiar in the wilderness or duel arena!");
                    return false;
                }
            } else {
                if (mob.isPlayer())
                    mob.getPlayer().sendMessage("You can only attack a familiar in a multi combat zone or the duel arena!");
                return false;
            }
        }
        if (!CombatUtils.isDeepEnoughInWild(mob, victim)) {
            mob.resetCombat();
            return false;
        }
        if (!mob.getActivity().isCombatActivity(mob, victim)) {
            return false;
        }
        boolean moved = false;
        if (mob.getLocation().equals(victim.getLocation()) && !mob.getCombatState().isFrozen() && !victim.getWalkingQueue().isMoving()) {
            WalkingDirection direction = DIRECTIONS[0];
            int index = 1;
            boolean found = false;
            while (index != DIRECTIONS.length) {
                if (PrimitivePathFinder.canMove(mob.getLocation(), direction, false)) {
                    found = true;
                    break;
                } else {
                    direction = DIRECTIONS[index++];
                }
            }
            if (found) {
                Location loc = mob.getLocation().getLocation(direction);
                mob.requestWalk(loc.getX(), loc.getY());
                moved = true;
            }
        }
        if (mob.getLocation().equals(victim.getLocation()) && !moved) {
            return false;
        }
        /*if (!mob.getPlayer().refreshAttackOptions() || !victim.getPlayer().refreshAttackOptions()) {
                  ActionSender.sendMessage(mob.getPlayer(), "You cannot attack players outside of the wilderness!");
                  mob.resetCombat();
                  return false;
              }*/
        //TODO Re-do later
        if (!mob.isMulti()) {
            if (victim.getCombatState().getLastAttacker() != null && victim.getCombatState().getLastAttacker() != mob) {
                if (mob.isPlayer()) {
                    mob.getPlayer().sendMessage("That player is already in combat!");
                }
                mob.resetCombat();
                return false;
            }
            if (mob.getCombatState().getLastAttacker() != null && mob.getCombatState().getLastAttacker() != victim) {
                if (mob.isPlayer()) {
                    mob.getPlayer().sendMessage("You are already in combat!");
                }
                mob.resetCombat();
                return false;
            }
        }
        return true;
    }

    public static void process(final Mob mob) {
        final Mob victim = mob.getCombatState().getVictim();
        if (!canAttack(mob, victim)) {
            return;
        }
        final FightType type = mob.getFightType();
        final CombatAction action = CombatAction.forType(mob, type);
        if (!action.canAttack(mob, victim)) {
            return;
        }
        victim.getCombatState().setLastAttacker(mob);
        boolean timerOver = type == FightType.MAGIC ? mob.getCombatState().getSpellDelay() == 0 : mob.getCombatState().getAttackDelay() == 0;
        if (timerOver) {
            mob.turnTo(victim);
            final CombatHit hit = action.hit(mob, victim);
            if (mob.isPlayer() && mob.getPlayer().getPrayer().usingPrayer(1, Prayer.TURMOIL)) {
                mob.getPlayer().getPrayer().updateTurmoil(victim);
            }
            if (hit != null) {
                final int soakedDamage = hit.calculateSoakedDamage(action);
                World.getWorld().submit(new Tick(hit.getTicks()) {
                    @Override
                    public void execute() {
                        int damage = hit.getDamage();
                        int maxDamage = hit.getMaximumDamage();
                        mob.getCombatState().setLastHit(damage);
                        IdentifierManager.get("combat_after_effect").identify(mob, victim, hit, type);
                        victim.getDamageManager().damage(mob, damage, maxDamage, hit.getFightType() != null ? hit.getFightType().damageType : type.damageType, 1);
                        if (soakedDamage > 0) {
                            victim.getDamageManager().soak(mob, soakedDamage);
                        }
                        stop();
                    }
                });
            }
        }
    }

    public static boolean diagonal(Location l, Location l1) {
        int xDial = Math.abs(l.getX() - l1.getX());
        int yDial = Math.abs(l.getY() - l1.getY());
        return xDial == 1 && yDial == 1;
    }

    /**
     * Gets the decreased dragonfire.
     *
     * @param hit      The start hit.
     * @param source   The entity using the dragonfire.
     * @param victim   The victim.
     * @param fireName The name of the dragonfire attack (firebreath, poison breath, ...)
     * @return The decreased amount of damage.
     */
    public static int getDecreasedDragonfire(int hit, Mob source, Mob victim, String fireName) {
        if (victim.isPlayer()) {
            String message = "You are horribly burnt by the dragon's " + fireName + ".";
            if (victim.getPlayer().getPrayer().usingPrayer(0, Prayer.PROTECT_FROM_MAGIC)) {
                message = "Your magic protection prayer decreases some of the dragon's " + fireName + " damage.";
                hit *= 0.6;
            } else if (victim.getPlayer().getPrayer().usingPrayer(1, Prayer.DEFLECT_MAGIC)) {
                message = "Your magic deflect curse decreases some of the dragon's " + fireName + " damage.";
                hit *= 0.6;
            }
            int itemId = victim.getPlayer().getEquipment().getSlot(Equipment.SLOT_SHIELD);
            if (itemId == 11283 || itemId == 11284) {
                //TODO: Charging.
                message = "Your shield absorbs most of the dragon's " + fireName + ".";
                hit *= 0.1;
            } else if (itemId == 1540 || itemId == 8282 || itemId == 16079 || itemId == 16933) {
                message = "Your shield absorbs most of the dragon's " + fireName + ".";
                hit *= 0.1;
            }
            //TODO: If player has antifire potion, decrease damage by 250, if player has super antifire, decrease by 450.
            if (System.currentTimeMillis() - victim.getPlayer().getAttribute("antiFire", 0L) < 360000) {
                hit -= 250;
            } else if (System.currentTimeMillis() - victim.getPlayer().getAttribute("santiFire", 0L) < 360000) {
                hit -= 450;
            }
            if (source.isNPC()) {
                if (fireName != null) {
                    victim.getPlayer().sendMessage(message);
                }
            }
        } else {
            String name = victim.getNPC().getDefinition().getName().toLowerCase();
            if (name.contains("dragon") || name.contains("fire")) {
                return 0;
            }
        }
        return hit;
    }

    public static boolean usingProtection(NPC npc, FightType fightType) {
        CacheNPCDefinition def = npc.getDefinition().getCacheDefinition();
        if (fightType == FightType.MELEE) {
            if (def.getHeadIcon() == 0 || def.getHeadIcon() == 8 || def.getHeadIcon() == 12 || def.getHeadIcon() == 16) {
                return true;
            }
            return false;
        }
        if (fightType == FightType.RANGE) {
            if (def.getHeadIcon() == 1 || def.getHeadIcon() == 6 || def.getHeadIcon() == 9 || def.getHeadIcon() == 14 || def.getHeadIcon() == 17) {
                return true;
            }
            return false;
        }
        if (fightType == FightType.MAGIC) {
            if (def.getHeadIcon() == 2 || def.getHeadIcon() == 6 || def.getHeadIcon() == 10 || def.getHeadIcon() == 13 || def.getHeadIcon() == 18) {
                return true;
            }
            return false;
        }
        return false;
    }

}
