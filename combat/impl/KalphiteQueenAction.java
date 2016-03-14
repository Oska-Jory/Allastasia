package org.dementhium.model.combat.impl;

import org.dementhium.content.misc.Following;
import org.dementhium.model.Location;
import org.dementhium.model.Mob;
import org.dementhium.model.World;
import org.dementhium.model.combat.Combat.FightType;
import org.dementhium.model.combat.CombatAction;
import org.dementhium.model.combat.CombatHit;
import org.dementhium.model.combat.CombatUtils;
import org.dementhium.model.misc.ProjectileManager;
import org.dementhium.model.npc.impl.KalphiteQueen;
import org.dementhium.tickable.Tick;

import java.util.Random;

/**
 * The Kalphite Queen's combat action.
 *
 * @author Emperor
 */
public class KalphiteQueenAction extends CombatAction {

    /**
     * The random instance used for randomizing values.
     */
    private static final Random RANDOM = new Random();

    /**
     * The current queen attack used.
     */
    private QueenAttack currentAttack = QueenAttack.RANGE_1;

    /**
     * The amount of attacks left before fight type change.
     */
    private int attacksLeft = 4;

    @Override
    public CombatHit hit(Mob mob, final Mob victim) {
        attacksLeft--;
        mob.getCombatState().setAttackDelay(4);
        int maximumHit = damage(mob, currentAttack.getFightType());
        int hit = RANDOM.nextInt(maximumHit);
        mob.animate(currentAttack.getAnimation());
        mob.graphics(currentAttack.getGraphic());
        int ticks = 0;
        Location l = mob.getLocation().transform(mob.size() / 2, mob.size() / 2, 0);
        int speed = 46 + (l.getDistance(victim.getLocation()) * 5);
        if (victim.isPlayer() && victim.getPlayer().getPrayer().usingCorrispondingPrayer(currentAttack.getFightType())) {
            hit = 0;
        }
        if (currentAttack.getFightType() == FightType.MELEE) {
            hit = getHit(mob, victim, maximumHit);
        } else if (currentAttack.getFightType() == FightType.RANGE) {
            ticks = (int) Math.floor(l.getDistance(victim.getLocation()) * 0.5);
            ProjectileManager.sendGlobalProjectile(currentAttack.getProjectileId(), l, victim, 40, 0, speed, 3, 50, 0);
        } else if (currentAttack.getFightType() == FightType.MAGIC) {
            ticks = (int) Math.floor(l.getDistance(victim.getLocation()) * 0.5);
            ProjectileManager.sendGlobalProjectile(currentAttack.getProjectileId(), l, victim, 40, 36, speed, 3, 50, 0);
            World.getWorld().submit(new Tick(ticks) {
                @Override
                public void execute() {
                    victim.graphics(currentAttack.getEndGraphic());
                    stop();
                }
            });
        }
        return new CombatHit(mob, victim, hit, maximumHit, ticks).setFightType(currentAttack.getFightType());
    }

    @Override
    public boolean canAttack(Mob mob, Mob victim) {
        int halfedSize = mob.size() / 2;
        int victimHalfed = victim.size() / 2;
        Location center = mob.getLocation().transform(halfedSize, halfedSize, 0);
        Location victimCenter = victim.getLocation().transform(victimHalfed, victimHalfed, 0);
        int distance = (center.getDistance(victimCenter) - halfedSize) - victimHalfed;
        if (attacksLeft < 1) {
            int attackType = (((KalphiteQueen) mob.getNPC()).getState().ordinal() * 3) + RANDOM.nextInt(3);
            while (attackType == currentAttack.ordinal()) {
                attackType = (((KalphiteQueen) mob.getNPC()).getState().ordinal() * 3) + RANDOM.nextInt(3);
            }
            currentAttack = QueenAttack.values()[attackType];
            attacksLeft = 3 + RANDOM.nextInt(2);
        }
        if (currentAttack.getFightType() != FightType.MAGIC)
            mob.getNPC().setCurrentFightType(currentAttack.getFightType());
        if (currentAttack.getFightType() == FightType.MELEE) {
            if (!CombatUtils.canMelee(mob, victim)) {
                int attackType = (((KalphiteQueen) mob.getNPC()).getState().ordinal() * 3) + 1 + RANDOM.nextInt(2);
                currentAttack = QueenAttack.values()[attackType];
                if (currentAttack.getFightType() != FightType.MAGIC)
                    mob.getNPC().setCurrentFightType(currentAttack.getFightType());
            } else {
                return true;
            }
        }
        if (distance < 17) {
            return true;
        }
        Following.combatFollow(mob, victim);
        return false;
    }

    /**
     * Sets the current attack.
     *
     * @param currentAttack The current attack.
     */
    public void setCurrentAttack(QueenAttack currentAttack) {
        this.currentAttack = currentAttack;
    }

}
