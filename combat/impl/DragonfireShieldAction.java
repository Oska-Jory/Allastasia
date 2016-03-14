package org.dementhium.model.combat.impl;

import org.dementhium.content.activity.impl.DuelActivity;
import org.dementhium.content.activity.impl.duel.DuelConfigurations.Rules;
import org.dementhium.content.misc.Following;
import org.dementhium.model.Mob;
import org.dementhium.model.World;
import org.dementhium.model.combat.Combat;
import org.dementhium.model.combat.Combat.FightType;
import org.dementhium.model.combat.CombatAction;
import org.dementhium.model.combat.CombatHit;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.mask.Graphic;
import org.dementhium.model.misc.ProjectileManager;
import org.dementhium.net.ActionSender;
import org.dementhium.tickable.Tick;

import java.util.Random;

/**
 * Handles the dragonfire shield special attack.
 *
 * @author Emperor
 */
public class DragonfireShieldAction extends CombatAction {

    /**
     * The singleton of this class.
     */
    private static final DragonfireShieldAction SINGLETON = new DragonfireShieldAction();

    /**
     * The random instance used for randomizing values.
     */
    private static final Random RANDOM = new Random();

    /**
     * The Discharge animation.
     */
    private static final Animation DISCHARGE_ANIMATION = Animation.create(6696);

    /**
     * The start GFX of the discharge.
     */
    private static final Graphic DISCHARGE_GFX = Graphic.create(1165);

    /**
     * The end GFX of the discharge.
     */
    private static final Graphic DISCHARGE_END_GFX = Graphic.create(1167, 96 << 16);

    @Override
    public CombatHit hit(final Mob mob, final Mob victim) {
        int damage = Combat.getDecreasedDragonfire(RANDOM.nextInt(230), mob, victim, null);
        mob.animate(DISCHARGE_ANIMATION);
        mob.graphics(DISCHARGE_GFX);
        mob.setAttribute("dischargeDelay", World.getTicks() + 200); //Creates a 120 seconds delay.
        mob.setAttribute("dragonfireShieldActivated", false);
        mob.getCombatState().setAttackDelay(5);
        final int speed = 36 + (mob.getLocation().getDistance(victim.getLocation()) * 5);
        World.getWorld().submit(new Tick(2) {
            @Override
            public void execute() {
                ProjectileManager.sendGlobalProjectile(1166, mob, victim, 24, 24, speed, 0, 18);
                this.stop();
            }
        });
        if (!mob.getPlayer().usingSpecial()) {
            ActionSender.sendConfig(mob.getPlayer(), 301, 0);
        }
        int ticks = (int) Math.floor(mob.getLocation().getDistance(victim.getLocation()) * 0.6);
        World.getWorld().submit(new Tick(ticks + 2) {
            @Override
            public void execute() {
                if (!victim.isAnimating()) {
                    victim.animate(victim.getDefenceAnimation());
                }
                victim.graphics(DISCHARGE_END_GFX);
                stop();
            }
        });
        return new CombatHit(mob, victim, damage, damage, ticks + 2);
    }

    @Override
    public boolean canAttack(Mob mob, Mob victim) {
        if (mob.isPlayer()) {
            if (mob.getActivity() instanceof DuelActivity) {
                if (((DuelActivity) mob.getActivity()).getDuelConfigurations().getRule(Rules.MAGIC)) {
                    mob.getPlayer().sendMessage("You can't use your dragonfire shield special during this duel!");
                    return false;
                }
            }
        }
        if (!mob.getLocation().withinDistance(victim.getLocation(), 12) && mob.getFightType() == FightType.DRAGONFIRE) {
            Following.combatFollow(mob, victim);
            return false;
        }
        return true;
    }

    /**
     * @return the singleton
     */
    public static DragonfireShieldAction getSingleton() {
        return SINGLETON;
    }

}
