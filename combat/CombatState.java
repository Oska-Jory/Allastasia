package org.dementhium.model.combat;

import org.dementhium.model.Mob;
import org.dementhium.model.combat.Combat.FightType;

/**
 * @author 'Mystic Flow
 */
public final class CombatState {

    private Mob mob;

    private Mob victim;

    private int attackDelay, spellDelay, lastHit, frozenTime;

    private long freezeTimer;

    private Mob lastAttacker;

    private long lastAttacked;

    private long lastTeleblocked;

    public CombatState(Mob mob) {
        this.mob = mob;
    }

    public void setAttackDelay(int delay) {
        this.attackDelay = delay;
    }

    public void setSpellDelay(int spellDelay) {
        this.spellDelay = spellDelay;
    }

    public int getAttackDelay() {
        return attackDelay;
    }

    public int getSpellDelay() {
        return spellDelay;
    }

    public void setVictim(Mob victim) {
        if (victim != mob)
            this.victim = victim;
    }

    public Mob getVictim() {
        return victim;
    }

    public void setLastHit(int lastHit) {
        this.lastHit = lastHit;
    }

    public int getLastHit() {
        return lastHit;
    }

    public void setFrozenTime(int frozenTime) {
        if (!mob.hasTick("freeze_immunity")) {
            if (!isFrozen() && mob.isPlayer() && !mob.isDead() && frozenTime > 0) {
                mob.getPlayer().sendMessage("You have been frozen!");
            }
            this.frozenTime = frozenTime;
            this.freezeTimer = System.currentTimeMillis();
        }
    }

    public Mob getLastAttacker() {
        if (System.currentTimeMillis() - lastAttacked > 10000) {
            lastAttacker = null;
        }
        return lastAttacker;
    }

    public void setLastAttacker(Mob lastAttacker) {
        this.lastAttacker = lastAttacker;
        this.lastAttacked = System.currentTimeMillis();
    }

    public boolean isFrozen() {
        return System.currentTimeMillis() - freezeTimer < frozenTime;
    }

    public void setTeleblock(long time) {
        this.lastTeleblocked = time;
    }

    public void teleBlock() {
        this.lastTeleblocked = System.currentTimeMillis();
        if (mob.isPlayer() && mob.getPlayer().getPrayer().usingCorrispondingPrayer(FightType.MAGIC)) {
            this.lastTeleblocked -= 150000;
        }
    }

    public boolean isTeleblocked() {
        if (lastTeleblocked < 1) {
            return false;
        }
        return !(System.currentTimeMillis() - lastTeleblocked > 300000);
    }

    public long getTeleblockTime() {
        return lastTeleblocked;
    }

}
