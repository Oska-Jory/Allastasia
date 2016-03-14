package org.dementhium.model.combat.impl;

import org.dementhium.content.misc.Following;
import org.dementhium.model.Mob;
import org.dementhium.model.combat.Combat;
import org.dementhium.model.combat.Combat.FightType;
import org.dementhium.model.combat.CombatAction;
import org.dementhium.model.combat.CombatHit;
import org.dementhium.model.combat.CombatUtils;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.mask.Graphic;

import java.util.Random;

/**
 * The chromatic dragon combat handling class.
 *
 * @author Emperor
 */
public class ChromaticDragonAction extends CombatAction {

    /**
     * The random instance used for randomizing values.
     */
    private static final Random RANDOM = new Random();

    /**
     * The bite melee attack animation.
     */
    private static final Animation BITE_ANIMATION = Animation.create(12252);

    /**
     * The dragonfire attack animation.
     */
    private static final Animation DRAGONFIRE_ANIMATION = Animation.create(14245);

    /**
     * The dragonfire attack graphics.
     */
    private static final Graphic DRAGONFIRE_GFX = Graphic.create(2465);

    /**
     * The current fight type used.
     */
    private FightType fightType = FightType.MELEE;

    @Override
    public CombatHit hit(Mob mob, Mob victim) {
        fightType = FightType.MELEE;
        if (RANDOM.nextInt(10) < 3) {
            /*
                * Chromatic dragons rarely use their dragonfire, and can only use it at close range.
                */
            fightType = FightType.DRAGONFIRE;
        }
        mob.getNPC().setCurrentFightType(fightType);
        mob.getCombatState().setAttackDelay(5);
        int maximumHit = damage(mob, fightType);
        int hit = RANDOM.nextInt(maximumHit);
        if (fightType == FightType.MELEE) {
            mob.animate(BITE_ANIMATION);
            hit = getHit(mob, victim, maximumHit);
        } else {
            mob.animate(DRAGONFIRE_ANIMATION);
            mob.graphics(DRAGONFIRE_GFX);
            hit = Combat.getDecreasedDragonfire(hit, mob, victim, "fiery breath");
        }
        return new CombatHit(mob, victim, hit, maximumHit, 0).setFightType(fightType);
    }

    @Override
    public boolean canAttack(Mob mob, Mob victim) {
        /*
           * Calculating the distance, while keeping record of the sizes.
           */
        if (!CombatUtils.canMelee(mob, victim)) {
            Following.combatFollow(mob, victim);
            return false;
        }
        return true;
    }

}
