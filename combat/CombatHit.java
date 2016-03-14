package org.dementhium.model.combat;

import org.dementhium.model.Mob;
import org.dementhium.model.combat.Combat.FightType;

/**
 * @author 'Mystic Flow
 */
public class CombatHit {

    private Mob mob;
    private Mob victim;
    private int damageInflicted, maximumDamage;
    private int ticks;
    private FightType fightType;

    public CombatHit(Mob mob, Mob victim, int damageInflicted, int maximumDamage, int ticks) {
        if (damageInflicted < 1) {
            damageInflicted = 0;
        }
        this.mob = mob;
        this.victim = victim;
        this.damageInflicted = damageInflicted;
        this.maximumDamage = maximumDamage;
        this.ticks = ticks;
    }

    public Mob getEntity() {
        return mob;
    }

    public Mob getVictim() {
        return victim;
    }

    public int getDamage() {
        return damageInflicted;
    }

    public int getMaximumDamage() {
        return maximumDamage;
    }

    public int getTicks() {
        return ticks;
    }

    public FightType getFightType() {
        return fightType;
    }

    public int calculateSoakedDamage(CombatAction action) {
        int soakedDamage = action.soakedDamage(damageInflicted, victim);
        if (soakedDamage > 0) {
            damageInflicted -= soakedDamage;
        }
        return soakedDamage;
    }

    public CombatHit setFightType(FightType fightType) {
        this.fightType = fightType;
        return this;
    }

}
