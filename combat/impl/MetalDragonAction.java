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
 * Handles the Metal Dragon combat action.
 *
 * @author Emperor
 */
public class MetalDragonAction extends CombatAction {

    /**
     * The random instance used for randomizing values.
     */
    private static final Random RANDOM = new Random();

    /**
     * The bite melee attack animation.
     */
    private static final Animation BITE_ANIMATION = Animation.create(13158);

    /**
     * The dragonfire attack animation. (13164 before)
     */
    private static final Animation DRAGONFIRE_ANIMATION = Animation.create(13160);

    /**
     * The dragonfire projectile id.
     */
    private static final int DRAGONFIRE_ID = 2464;

    /**
     * The current fight type used.
     */
    private FightType fightType = FightType.MELEE;

    @Override
    public CombatHit hit(Mob mob, Mob victim) {
        mob.getCombatState().setAttackDelay(5);
        int maximumHit = damage(mob, fightType);
        if (fightType == FightType.MELEE) {
            mob.animate(BITE_ANIMATION);
            return new CombatHit(mob, victim, getHit(mob, victim, maximumHit), maximumHit, 0).setFightType(fightType);
        }
        mob.animate(DRAGONFIRE_ANIMATION);
        Location l = mob.getLocation().transform(mob.size() / 2, mob.size() / 2, 0);
        int ticks = (int) Math.floor(l.getDistance(victim.getLocation()) * 0.5);
        int speed = 26 + (l.getDistance(victim.getLocation()) * 5);
        ProjectileManager.sendGlobalProjectile(DRAGONFIRE_ID, l, victim, 40, 36, speed, 28, 50, 5);
        int hit = RANDOM.nextInt(maximumHit);
        hit = Combat.getDecreasedDragonfire(hit, mob, victim, "fiery breath");
        return new CombatHit(mob, victim, hit, maximumHit, ticks).setFightType(fightType);
    }

    @Override
    public boolean canAttack(Mob mob, Mob victim) {
        fightType = FightType.DRAGONFIRE;
        if (CombatUtils.canMelee(mob, victim) && RANDOM.nextInt(10) < 5) {
            fightType = FightType.MELEE;
        }
        mob.getNPC().setCurrentFightType(fightType);
        int distance = mob.getLocation().getDistance(victim.getLocation());
        if (distance > 16) {
            Following.combatFollow(mob, victim);
            return false;
        }
        Location l = mob.getLocation();
        Location v = victim.getLocation();
        if (v.getX() >= l.getX() && v.getX() < l.getX() + mob.size()
                && v.getY() >= l.getY() && v.getY() < l.getY() + mob.size()) {
            Following.combatFollow(mob, victim);
            return false;
        }
        return true;
    }

}
