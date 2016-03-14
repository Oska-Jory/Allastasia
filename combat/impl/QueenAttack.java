package org.dementhium.model.combat.impl;

import org.dementhium.model.combat.Combat.FightType;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.mask.Graphic;

/**
 * Represents the Kalphite queen's attacks.
 *
 * @author Emperor
 */
public enum QueenAttack {

    /**
     * The first form's melee attack.
     */
    MELEE_1(Animation.create(6241), Graphic.create(-1), -1, Graphic.create(-1), FightType.MELEE),

    /**
     * The first form's range attack.
     */
    RANGE_1(Animation.create(6240), Graphic.create(-1), 288, Graphic.create(-1), FightType.RANGE),

    /**
     * The first form's magic attack.
     */
    MAGIC_1(Animation.create(1172), Graphic.create(278), 280, Graphic.create(281), FightType.MAGIC),

    /**
     * The second form's melee attack.
     */
    MELEE_2(Animation.create(6235), Graphic.create(-1), -1, Graphic.create(-1), FightType.MELEE),

    /**
     * The second form's range attack.
     */
    RANGE_2(Animation.create(6234), Graphic.create(-1), 289, Graphic.create(-1), FightType.RANGE),

    /**
     * The second form's magic attack.
     */
    MAGIC_2(Animation.create(6234), Graphic.create(279), 280, Graphic.create(281), FightType.MAGIC);

    /**
     * The attack animation used.
     */
    private final Animation animation;

    /**
     * The starting graphics.
     */
    private final Graphic graphic;

    /**
     * The projectile id.
     */
    private final int projectileId;

    /**
     * The end graphic.
     */
    private final Graphic endGraphic;

    /**
     * The fight type used.
     */
    private final FightType fightType;

    /**
     * Constructs a new {@code QueenAttack} {@code Object}.
     *
     * @param animation    The attack animation.
     * @param graphic      The start graphic.
     * @param projectileId The projectile id.
     * @param endGraphic   The end graphic.
     * @param fightType    The fight type.
     */
    private QueenAttack(Animation animation, Graphic graphic, int projectileId, Graphic endGraphic, FightType fightType) {
        this.animation = animation;
        this.graphic = graphic;
        this.projectileId = projectileId;
        this.endGraphic = endGraphic;
        this.fightType = fightType;
    }

    /**
     * @return the animation
     */
    public Animation getAnimation() {
        return animation;
    }

    /**
     * @return the graphic
     */
    public Graphic getGraphic() {
        return graphic;
    }

    /**
     * @return the projectileId
     */
    public int getProjectileId() {
        return projectileId;
    }

    /**
     * @return the endGraphic
     */
    public Graphic getEndGraphic() {
        return endGraphic;
    }

    /**
     * @return the fightType
     */
    public FightType getFightType() {
        return fightType;
    }
}