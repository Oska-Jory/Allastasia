package org.dementhium.model.combat.impl;

import org.dementhium.content.misc.Following;
import org.dementhium.model.Location;
import org.dementhium.model.Mob;
import org.dementhium.model.combat.Combat;
import org.dementhium.model.combat.Combat.FightType;
import org.dementhium.model.combat.CombatAction;
import org.dementhium.model.combat.CombatHit;
import org.dementhium.model.combat.CombatUtils;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.misc.ProjectileManager;

import java.util.Random;

/**
 * The king black dragon's combat handling class.
 *
 * @author Emperor
 */
public class KingBlackDragonAction extends CombatAction {

    /**
     * The random instance used for randomizing values.
     */
    private static final Random RANDOM = new Random();

    /**
     * The current fight type used.
     */
    private FightType fightType = FightType.MELEE;

    /**
     * The melee attacking animation.
     */
    private static final Animation MELEE = Animation.create(80);

    /**
     * The headbutt melee attack animation.
     */
    private static final Animation HEADBUTT = Animation.create(91);

    /**
     * The dragon's fire type.
     *
     * @author Emperor
     */
    private static enum FireType {

        /**
         * The normal dragonfire fire type.
         */
        FIERY_BREATH(Animation.create(81), 393, new BreathEffect() {
            @Override
            public boolean execute(Mob victim) {
                return true;
            }
        }),

        /**
         * The shocking breath fire type.
         */
        SHOCKING_BREATH(Animation.create(84), 396, new BreathEffect() {
            @Override
            public boolean execute(Mob victim) {
                if (RANDOM.nextInt(10) < 3) {
                    victim.getPlayer().getSkills().decreaseLevelToZero(RANDOM.nextInt(3), 5);
                    victim.getPlayer().sendMessage("You have been shocked.");
                }
                return true;
            }
        }),

        /**
         * The toxic breath fire type.
         */
        TOXIC_BREATH(Animation.create(82), 394, new BreathEffect() {
            @Override
            public boolean execute(Mob victim) {
                victim.getPoisonManager().poison(victim, 80);
                return true;
            }
        }),

        /**
         * The freezing breath fire type.
         */
        ICY_BREATH(Animation.create(83), 395, new BreathEffect() {
            @Override
            public boolean execute(Mob victim) {
                if (RANDOM.nextInt(10) < 7) {
                    victim.getCombatState().setFrozenTime(25);
                }
                return true;
            }
        });

        /**
         * The attack animation.
         */
        private final Animation animation;

        /**
         * The projectile id.
         */
        private final int projectileId;

        /**
         * The breath effect.
         */
        private final BreathEffect breathEffect;

        /**
         * Constructs a new {@code FireType} {@code Object}.
         *
         * @param animation The animation.
         */
        private FireType(Animation animation, int projectileId, BreathEffect breathEffect) {
            this.animation = animation;
            this.projectileId = projectileId;
            this.breathEffect = breathEffect;
        }

    }

    @Override
    public CombatHit hit(Mob mob, Mob victim) {
        mob.getCombatState().setAttackDelay(4);
        int maximumHit = damage(mob, fightType);
        int hit = RANDOM.nextInt(maximumHit);
        if (fightType == FightType.MELEE) {
            int type = RANDOM.nextInt(2);
            if (type == 0) {
                mob.animate(MELEE);
            } else {
                mob.animate(HEADBUTT);
            }
            hit = getHit(mob, victim, maximumHit);
            return new CombatHit(mob, victim, hit, maximumHit, 0).setFightType(fightType);
        }
        FireType fireType = FireType.values()[RANDOM.nextInt(FireType.values().length)];
        mob.animate(fireType.animation);
        Location l = mob.getLocation().transform(mob.size() / 2, mob.size() / 2, 0);
        int ticks = (int) Math.floor(l.getDistance(victim.getLocation()) * 0.5);
        int speed = 46 + (l.getDistance(victim.getLocation()) * 5);
        ProjectileManager.sendGlobalProjectile(fireType.projectileId, l, victim, 40, 36, speed, 3, 50, 0);
        hit = Combat.getDecreasedDragonfire(hit, mob, victim, fireType.name().replace("_", " ").toLowerCase());
        fireType.breathEffect.execute(victim);
        return new CombatHit(mob, victim, hit, maximumHit, ticks + 1).setFightType(fightType);
    }

    @Override
    public boolean canAttack(Mob mob, Mob victim) {
        int halfedSize = mob.size() / 2;
        int victimHalfed = victim.size() / 2;
        Location center = mob.getLocation().transform(halfedSize, halfedSize, 0);
        Location victimCenter = victim.getLocation().transform(victimHalfed, victimHalfed, 0);
        int distance = (center.getDistance(victimCenter) - halfedSize) - victimHalfed;
        fightType = FightType.DRAGONFIRE;
        mob.getNPC().setCurrentFightType(fightType);
        if (CombatUtils.canMelee(mob, victim) && RANDOM.nextInt(10) < 3) {
            fightType = FightType.MELEE;
            mob.getNPC().setCurrentFightType(fightType);
            return true;
        } else if (distance < 17) {
            return true;
        }
        Following.combatFollow(mob, victim);
        return false;
    }

    /**
     * The breath effect interface, implemented by a FireType.
     *
     * @author Emperor
     */
    private interface BreathEffect {

        /**
         * Executes the breath effect.
         *
         * @param victim The victim.
         * @return {@code True}.
         */
        boolean execute(Mob victim);
    }

}
