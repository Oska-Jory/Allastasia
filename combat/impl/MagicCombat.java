package org.dementhium.model.combat.impl;

import org.dementhium.content.activity.impl.DuelActivity;
import org.dementhium.content.activity.impl.duel.DuelConfigurations.Rules;
import org.dementhium.content.misc.Following;
import org.dementhium.content.skills.magic.MagicHandler;
import org.dementhium.content.skills.magic.Spell;
import org.dementhium.model.Mob;
import org.dementhium.model.World;
import org.dementhium.model.combat.Combat.FightType;
import org.dementhium.model.combat.CombatAction;
import org.dementhium.model.combat.CombatHit;
import org.dementhium.model.map.path.ProjectilePathFinder;
import org.dementhium.model.misc.ProjectileManager;
import org.dementhium.model.npc.impl.EliteBlackKnight;
import org.dementhium.model.player.Bonuses;
import org.dementhium.tickable.Tick;

/**
 * @author 'Mystic Flow
 * @author `Discardedx2 <the_shawn@discardedx2.info>
 * @author Stephen
 */
public final class MagicCombat extends CombatAction {

    private static final CombatAction INSTANCE = new MagicCombat();

    public static CombatAction getAction() {
        return INSTANCE;
    }

    @Override
    public CombatHit hit(final Mob attacker, final Mob victim) {
        if (attacker.isPlayer()) {
            if (attacker.getAttribute("spellQueued") != null) {
                return null;
            }
            Spell spell = (Spell) attacker.getAttribute("autoCastSpell");
            MagicHandler.cast(attacker.getPlayer(), victim, attacker.getPlayer().getSettings().getSpellBook(), spell.getSpellId());
        } else if (attacker.isNPC()) {
            attacker.getCombatState().setAttackDelay(5);
            attacker.getCombatState().setSpellDelay(5);
            int maxDamage = damage(attacker, FightType.MAGIC);
            int hit = getHit(attacker, victim, maxDamage);
            attacker.animate(attacker.getNPC().getDefinition().getAttackAnimation());
            attacker.graphics(attacker.getNPC().getDefinition().getStartGraphics());
            sendProjectile(attacker, victim);
            if (hit > victim.getHitPoints()) {
                hit = victim.getHitPoints();
            }
            if (hit > 0) {
                World.getWorld().submit(new Tick(2) {
                    @Override
                    public void execute() {
                        victim.graphics(attacker.getNPC().getDefinition().getEndGraphics(), 100 << 16);
                        this.stop();
                    }
                });
                return new CombatHit(attacker, victim, hit, maxDamage, 2);

            } else {
                victim.graphics(85, 100 << 16);
            }
        }
        return null;
    }

    private void sendProjectile(Mob attacker, Mob victim) {
        ProjectileManager.sendDelayedProjectile(attacker, victim, attacker.getNPC().getDefinition().getProjectileId(), false);
    }

    @Override
    public boolean canAttack(Mob mob, Mob victim) {
        if (mob.isPlayer() && victim.isNPC() && (victim.getNPC() instanceof EliteBlackKnight)) {
            return false;
        }
        int projectileClip = ProjectilePathFinder.projectileClip(mob, victim.getLocation());
        if (projectileClip == 0) {
            mob.resetCombat();
            return false;
        } else if (projectileClip == 2) {
            return false; // next cycle
        }
        if (mob.isPlayer()) {
            if (mob.getActivity() instanceof DuelActivity) {
                if (((DuelActivity) mob.getActivity()).getDuelConfigurations().getRule(Rules.MAGIC)) {
                    mob.getPlayer().sendMessage("Magic isn't allowed during this duel!");
                    mob.getPlayer().resetCombat();
                    return false;
                }
            }
        }
        if (!mob.getLocation().withinDistance(victim.getLocation(), 8) && (mob.getFightType() == FightType.RANGE || mob.getFightType() == FightType.MAGIC)) {
            Following.combatFollow(mob, victim);
            return false;
        }
        return true;
    }

    @Override
    public int getAborptionBonusId() {
        return Bonuses.MAGIC_ABSORPTION;
    }
}
