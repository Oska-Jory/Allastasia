package org.dementhium.model.combat;

import org.dementhium.model.Item;
import org.dementhium.model.Mob;
import org.dementhium.model.combat.Combat.FightType;
import org.dementhium.model.combat.impl.DragonfireShieldAction;
import org.dementhium.model.combat.impl.MagicCombat;
import org.dementhium.model.combat.impl.MeleeCombat;
import org.dementhium.model.combat.impl.RangedCombat;
import org.dementhium.model.combat.MagicFormulae;
import org.dementhium.model.combat.MeleeFormulae;
import org.dementhium.model.combat.RangeFormulae;
import org.dementhium.model.definition.WeaponInterface;
import org.dementhium.model.player.Equipment;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;
import org.dementhium.util.Constants;

import java.util.Random;

/**
 * @author 'Mystic Flow
 */
public abstract class CombatAction {

    public static final double XP_RATE = 0.4; // like rs 0.4
    public static final double HP_XP_RATE = 0.1;

    public static final double DEFENCE_MULTIPLIER = 0.55;

    public static final Random RANDOM = new Random();

    public static CombatAction forType(Mob mob, FightType type) {
        if (mob.isNPC()) {
            if (mob.getNPC().getCustomCombatAction() != null) {
                return mob.getNPC().getCustomCombatAction();
            }
        }
        switch (type) {
            case MELEE:
                return MeleeCombat.getAction();
            case RANGE:
                return RangedCombat.getAction();
            case MAGIC:
                return MagicCombat.getAction();
            case DRAGONFIRE:
                return DragonfireShieldAction.getSingleton();
        }
        return null;
    }

    public void appendExperience(Player player, int damage) {
        int xp = (int) ((damage * XP_RATE));
        int hpXP = (int) ((damage * HP_XP_RATE));
        if (hpXP > 0) {
            player.getSkills().addExperience(Skills.HITPOINTS, hpXP);
        }
        if (xp > 0) {
            int style = player.getSettings().getCombatStyle();
            if (this instanceof MeleeCombat) {
                switch (style) {
                    case WeaponInterface.STYLE_ACCURATE:
                        player.getSkills().addExperience(Skills.ATTACK, xp);
                        break;
                    case WeaponInterface.STYLE_AGGRESSIVE:
                        player.getSkills().addExperience(Skills.STRENGTH, xp);
                        break;
                    case WeaponInterface.STYLE_DEFENSIVE:
                        player.getSkills().addExperience(Skills.DEFENCE, xp);
                        break;
                    case WeaponInterface.STYLE_CONTROLLED:
                        for (int i = Skills.ATTACK; i <= Skills.STRENGTH; i++) {
                            player.getSkills().addExperience(i, xp / 3);
                        }
                        break;
                }
            } else if (this instanceof RangedCombat) {
                switch (style) {
                    case WeaponInterface.STYLE_LONG_RANGE:
                        player.getSkills().addExperience(Skills.RANGE, xp / 2);
                        player.getSkills().addExperience(Skills.DEFENCE, xp / 2);
                        break;
                    default:
                        player.getSkills().addExperience(Skills.RANGE, xp);
                        break;
                }
            }
        }
    }

    public int soakedDamage(int damageInflicted, Mob victim) {
        int soakedDamage = 0;
        if (victim.isPlayer()) {
            if (damageInflicted > 200) {
                int absorptionId = getAborptionBonusId();
                if (absorptionId >= 0 && absorptionId <= 2) {
                    int excess = damageInflicted - 200;
                    double bonus = victim.getPlayer().getBonuses().getAbsorptionBonus(getAborptionBonusId()) / 100D;
                    soakedDamage = (int) (excess * bonus);
                }
            }
        }
        return soakedDamage;
    }

    /**
     * Gets the current hit.
     *
     * @param attacker The attacking mob.
     * @param victim   The mob being attacked.
     * @param damage   The maximum damage to deal (Use -1 for auto-calculation).
     * @return The calculated hit.
     */
    public int getHit(Mob attacker, Mob victim, int damage) {
        if (victim.isPlayer() && attacker.isNPC()) {
            if (victim.getPlayer().getPrayer().usingCorrispondingPrayer(attacker.getNPC().getFightType()) && !attacker.getNPC().hitsThroughPrayer()) {
                return 0;
            }
        } else if (victim.isNPC() && attacker.isPlayer()) {
            if (Combat.usingProtection(victim.getNPC(), attacker.getPlayer().getFightType())) {
                damage *= 0.6;
            }
        }
        boolean special = attacker.usingSpecial();
        double atk = special ? CombatUtils.specialBonus(attacker.getPlayer()) : 1.0;
        if (attacker.getFightType() == FightType.MELEE) {
            if (damage > -1) {
                return MeleeFormulae.getDamage(attacker, victim, atk, damage, 1.0);
            } else {
                return MeleeFormulae.getDamage(attacker, victim, atk, 1.0, 1.0);
            }
        } else if (attacker.getFightType() == FightType.RANGE) {
            if (damage > -1) {
                return RangeFormulae.getDamage(attacker, victim, atk, damage, 1.0);
            } else {
                return RangeFormulae.getDamage(attacker, victim, atk, 1.0, 1.0);
            }
        } else if (attacker.isNPC() && attacker.getFightType() == FightType.MAGIC) {
            if (damage > -1) {
                return MagicFormulae.getDamage(attacker.getNPC(), victim, atk, damage, 1.0);
            } else {
                return MagicFormulae.getDamage(attacker.getNPC(), victim, atk, 1.0, 1.0);
            }
        }
        return 0;
    }

    /**
     * Gets the maximum damage.
     *
     * @param mob  The attacking Mob.
     * @param type The fight type.
     * @return The maximum hit.
     */
    public static int damage(Mob mob, FightType type) {
        if (type == FightType.MELEE) {
            return MeleeFormulae.getMeleeDamage(mob, 1.0);
        } else if (type == FightType.MAGIC) {
            return (int) MagicFormulae.getMaximumMagicDamage(mob.getNPC(), 1.0);
        } else if (type == FightType.RANGE) {
            return RangeFormulae.getRangeDamage(mob, 1.0);
        } else if (type == FightType.DRAGONFIRE) {
            return 594;
        }
        return 0;
    }

    /**
     * Gets the bonus type used.
     *
     * @param source The attacking mob.
     * @return The bonus type.
     */
    public static int getBonusType(Mob source) {
        if (source.isPlayer()) {
            return source.getPlayer().getSettings().getCombatType();
        }
        if (source.getNPC().getCurrentFightType() == FightType.MAGIC) {
            return 3;
        } else if (source.getNPC().getCurrentFightType() == FightType.RANGE) {
            return 4;
        }
        int type = 0;
        int bonus = 0;
        for (int i = 0; i < 3; i++) {
            if (source.getNPC().getDefinition().getBonuses()[i] > bonus) {
                bonus = source.getNPC().getDefinition().getBonuses()[i];
                type = i;
            }
        }
        return type;
    }

    public int getAborptionBonusId() {
        return -1;
    }

    protected int getSpecialDeduction(Player player, Item item) {
        if (item == null) {
            return 0;
        }
        int amount = 0;
        switch (item.getId()) {
            case 11694: //godswords
            case 11698:
            case 14484:
            case 13905:
            case 4153:
            case 4151:
            case 6746:
                amount = Constants.SPEC_BAR_HALF;
                break;
            case 11700:
                amount = 600;
                break;
            case 11696:
                amount = Constants.SPEC_BAR_FULL;
                break;

            case 1215:
            case 13899:
            case 1231:
            case 5680:
            case 5698:
            case 13465:
            case 13466:
            case 13467:
            case 13468:
            case 13976:
                amount = 250;
                break;
            case 13902:
                amount = 350;
                break;
            case 861: //bows
                amount = 750;
                break;
            case 19784://korasi
            case 11235:
                amount = 600;
            case 4587:
                amount = 550;
                break;
        }
        if (player.getEquipment().getSlot(Equipment.SLOT_RING) == 19669) {
            int subtract = Math.round(amount * 0.10F);
            amount -= subtract;
        }
        return amount;
    }

    public abstract CombatHit hit(Mob mob, Mob victim);

    public abstract boolean canAttack(Mob mob, Mob victim);
}
