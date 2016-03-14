package org.dementhium.model.combat.impl;

import org.dementhium.content.activity.impl.DuelActivity;
import org.dementhium.content.activity.impl.duel.DuelConfigurations.Rules;
import org.dementhium.content.misc.Following;
import org.dementhium.identifiers.IdentifierManager;
import org.dementhium.model.Item;
import org.dementhium.model.Mob;
import org.dementhium.model.World;
import org.dementhium.model.combat.Combat.FightType;
import org.dementhium.model.combat.CombatAction;
import org.dementhium.model.combat.CombatHit;
import org.dementhium.model.map.Region;
import org.dementhium.model.map.path.ProjectilePathFinder;
import org.dementhium.model.mask.Graphic;
import org.dementhium.model.misc.DamageManager.DamageType;
import org.dementhium.model.misc.ProjectileManager;
import org.dementhium.model.npc.NPC;
import org.dementhium.model.npc.impl.EliteBlackKnight;
import org.dementhium.model.player.Bonuses;
import org.dementhium.model.player.Equipment;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;
import org.dementhium.tickable.Tick;
import org.dementhium.util.Misc;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public final class RangedCombat extends CombatAction {

	/*
	 * Kati says (4:13 PM):
	 *morrigan javelin animation is
	 *10501
	 *gfx is
	 *1837
	 *axe is 10504 (anim)
	 *gfx 1836

	 */

	//2962 = zaryte bow gfx
	private static final CombatAction INSTANCE = new RangedCombat();

	public static final Graphic ONYX_GFX = Graphic.create(753), DRAGON_GFX = Graphic.create(756), DIAMOND_GFX = Graphic.create(758), RUBY_GFX = Graphic.create(754), EMERALD_GFX = Graphic.create(752);

	public static CombatAction getAction() {
		return INSTANCE;
	}

	private RangedCombat() {

	}

	@Override
	public CombatHit hit(final Mob mob, final Mob victim) {
		if (mob.getWalkingQueue().isMoving()) {
			mob.getWalkingQueue().reset();
		}
		final Player player = mob.getPlayer();
		RangeWeapon weapon = null;
		if (player != null) {
			weapon = getRangeWeapon(player);
		}
		mob.getCombatState().setAttackDelay(mob.isNPC() ? 5 : mob.getPlayer().getAttackDelay());
		int distance = mob.getLocation().getDistance(victim.getLocation());
		int maxDamage = damage(mob, FightType.RANGE);
		int damageInflicted = getHit(mob, victim, maxDamage);
		int cycles = 3;
		if (mob.isPlayer() && mob.getPlayer().getEquipment().voidSet(2)) {
			damageInflicted *= 1.10;
		}
		if (victim.isPlayer() && victim.getPlayer().getPrayer().usingCorrispondingPrayer(FightType.RANGE)) {
			damageInflicted -= damageInflicted * 0.40;
		}
		if (weapon != null) {
			if ((weapon.is(WeaponType.KNIVES) || weapon.is(WeaponType.JAVELINE)) && distance < 5) {
				cycles--;
			}
			if (weapon.is(WeaponType.DARTS) || weapon.is(WeaponType.CBOW) || weapon.is(WeaponType.MULTI_HIT)) {
				cycles--;
			}
			weapon.performDrawback(player);
			weapon.performProjectile(player, victim, distance);
		} else {
			if (mob.isNPC()) {
				mob.graphics(mob.getNPC().getDefinition().getStartGraphics(), 100 << 16);
				ProjectileManager.sendDelayedProjectile(mob, victim, mob.getNPC().getDefinition().getProjectileId(), false);
			} else {
				mob.resetCombat();
				return null; //This should not happen.
			}
		}
		if (distance == 1) {
			if (weapon != null && (weapon.is(WeaponType.CBOW) || weapon.is(WeaponType.BOW) || weapon.is(WeaponType.JAVELINE) || weapon.is(WeaponType.MULTI_HIT))) {
				cycles = 2;
			} else {
				cycles = 1;
			}
		}
		if (mob.isPlayer()) {
			if (player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == 11235 && !mob.usingSpecial()) {
				World.getWorld().submit(new Tick(3) {

					@Override
					public void execute() {
						int maxDamage = damage(player, FightType.RANGE);
						int damageInflicted = getHit(player, victim, maxDamage);
						if (mob.isPlayer() && mob.getPlayer().getEquipment().voidSet(2)) {
							damageInflicted *= 1.10;
						}
						if (victim.isPlayer() && victim.getPlayer().getPrayer().usingCorrispondingPrayer(FightType.RANGE)) {
							damageInflicted *= .40;
						}

						victim.getDamageManager().damage(player, damageInflicted, maxDamage, DamageType.RANGE);
						stop();
					}

				});
			}
		}
		if (mob.usingSpecial()) {
			if (player.getSpecialAmount() > 0) {
				if (player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == 11235) {
					damageInflicted *= (player.getEquipment().get(13).getId() == 11212 ? 1.50 : 1.30);
				}
				performRangedSpecials(player, victim);
			} else {
				ActionSender.sendChatMessage(player, 0, "You do not have enough special attack to perform this action.");
				player.reverseSpecialActive();
				return null;
			}
		} else {
			mob.animate(mob.getAttackAnimation());
			final int animationCycles = cycles - 1;
			final boolean animating = victim.isAnimating();
			final RangeWeapon weaponNoob = weapon;
			if (animationCycles > 0) {
				World.getWorld().submit(new Tick(animationCycles) {
					public void execute() {
						if (!animating && !victim.isAnimating()) {
							if (weaponNoob != null && weaponNoob.is(WeaponType.MULTI_HIT))
								victim.graphics(954);
							victim.animate(victim.getDefenceAnimation());
						}
						stop();
					}
				});
			} else if (!animating) {
				victim.animate(victim.getDefenceAnimation());
			}
		}
		if (damageInflicted > 0 && RANDOM.nextDouble() < 0.15 && player != null && player.getEquipment().get(Equipment.SLOT_ARROWS) != null && player.getEquipment().get(Equipment.SLOT_ARROWS).getDefinition().getName().contains("(e)")) {
			int id = player.getEquipment().getSlot(Equipment.SLOT_ARROWS);
			if (id == 9242) { //ruby bolts (e)
				float removedLife = player.getSkills().getHitPoints() * 0.10F;
				if (removedLife > 1.0F && player.getSkills().getHitPoints() - removedLife > 1) {
					player.getDamageManager().damage(victim, (int) removedLife, maxDamage, DamageType.RED_DAMAGE);
					float victimRemoval = victim.getHitPoints() * 0.20F;
					damageInflicted = (int) victimRemoval;
					victim.graphics(RUBY_GFX);
				}
			} else {
				final int finalDamageInflicted = damageInflicted;
				World.getWorld().submit(new Tick(cycles) {
					@Override
					public void execute() {
						stop();
						int id = mob.getPlayer().getEquipment().getSlot(Equipment.SLOT_ARROWS);
						switch (id) {
						case 9236: //opal bolts (e)

						break;
						case 9237: //jade bolts (e)

						break;
						case 9238: //pearl bolts (e)

							break;
						case 9239: //topaz bolts (e)

							break;
						case 9240: //sapphire bolts (e)

							break;
						case 9241: //emerald bolts (e)
							if (RANDOM.nextInt(100) < 60) {
								victim.getPoisonManager().poison(mob, 58);
							}
							break;
						case 9243: //diamond bolts (e)

							break;
						case 9244: //dragon bolts (e)

							break;
						case 9245: //onyx bolts (e)
							int heal = Math.round(finalDamageInflicted * 0.25F);
							player.getSkills().heal(heal);
							victim.graphics(ONYX_GFX);
							break;
						}
					}
				});
			}
		}
		if (damageInflicted > 500 && victim.isNPC() && victim.getNPC().isNex()) {
			damageInflicted = 500;
		}
		if (mob.isPlayer() && (mob.getPlayer().getEquipment().get(Equipment.SLOT_WEAPON) != null && mob.getPlayer().getEquipment().get(Equipment.SLOT_WEAPON).getDefinition().doesPoison() || (mob.getPlayer().getEquipment().get(Equipment.SLOT_ARROWS) != null && mob.getPlayer().getEquipment().get(Equipment.SLOT_ARROWS).getDefinition().doesPoison()))) {
			if (RANDOM.nextInt(100) < 60 && mob.getPlayer().getEquipment().get(Equipment.SLOT_ARROWS).getId() != 9241) {
				int amount = mob.getPlayer().getEquipment().get(Equipment.SLOT_WEAPON) != null ? mob.getPlayer().getEquipment().get(Equipment.SLOT_WEAPON).getDefinition().getPoisonAmount() : mob.getPlayer().getEquipment().get(Equipment.SLOT_ARROWS).getDefinition().getPoisonAmount();
				victim.getPoisonManager().poison(mob, amount);
			}
		}
		if (weapon != null && weapon.is(WeaponType.KNIVES)) {
			damageInflicted /= 1.55;
		}
		if (victim.isPlayer() && victim.getPlayer().getPrayer().usingCorrispondingPrayer(FightType.RANGE)) {
			damageInflicted -= damageInflicted * 0.60;
		}
		if (damageInflicted > 0 && mob.isPlayer()) {
			appendExperience(mob.getPlayer(), damageInflicted);
		}
		if (damageInflicted > victim.getHitPoints()) {
			damageInflicted = victim.getHitPoints();
		}
		IdentifierManager.get("drop_arrows").identify(mob, victim, cycles);
		final int damageAmount = Misc.random(damageInflicted) / 2;
		if (weapon != null && weapon.is(WeaponType.MULTI_HIT)) {
			World.getWorld().submit(new Tick(2) {

				@Override
				public void execute() {
					for (NPC n : Region.getLocalNPCs(victim.getLocation(), 3)) {
						if (n != victim && n.isAttackable() && n.isMulti()) {
							n.getDamageManager().miscDamage(Misc.random(damageAmount), DamageType.RED_DAMAGE);
							appendExperience(player, damageAmount);
						}
						stop();
					}
				}
			});

		}
		return new CombatHit(mob, victim, damageInflicted, maxDamage, cycles);

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
		mob.getWalkingQueue().reset();
		if (mob.isPlayer()) {
			Player player = mob.getPlayer();
			if (mob.getActivity() instanceof DuelActivity) {
				if (((DuelActivity) mob.getActivity()).getDuelConfigurations().getRule(Rules.RANGE)) {
					player.sendMessage("Ranging isn't allowed during this duel!");
					player.resetCombat();
					return false;
				}
			}
			RangeWeapon weapon = getRangeWeapon(player);
			if (weapon != null) {
				if (!weapon.canAttack(player)) {
					mob.resetCombat();
					Item ammo = player.getEquipment().get(Equipment.SLOT_ARROWS);
					if (weapon.allowedAmmunition != null && ammo != null) {
						boolean allowed = false;
						RangeAmmo[] properAmmo = null;
						switch (weapon.type) {
						case BOW:
							properAmmo = BOW_AMMUNITION;
							break;
						case CBOW:
							properAmmo = CBOW_AMMUNITION;
							break;
						}
						if (properAmmo != null) {
							for (RangeAmmo bowAmmo : properAmmo) {
								for (int i : bowAmmo.items) {
									if (ammo.getId() == i) {
										allowed = true;
										break;
									}
								}
							}
						}
						if (allowed) {
							switch (weapon.type) {
							case BOW:
								player.sendMessage("Your bow isn't powerful enough for those arrows.");
								return false;
							case CBOW:
								player.sendMessage("Your crossbow isn't powerful enough for those bolts.");
								return false;
							}
						} else {
							String type = weapon.is(WeaponType.BOW) ? "bow" : "crossbow";
							player.sendMessage("You can't use that ammo with your " + type + ".");
							return false;
						}
					} else if (weapon.allowedAmmunition != null && ammo == null) {
						player.sendMessage("There is no ammo left in your quiver!");
						return false;
					} else {
						player.sendMessage("You have ran out of ammunition!");
						return false;
					}
				}

			}
		}
		if (!mob.getLocation().withinDistance(victim.getLocation(), 8)) {
			Following.combatFollow(mob, victim);
			return false;
		}
		return true;
	}

	private void performRangedSpecials(final Player player, final Mob victim) {
		if (player.getSpecialAmount() > 1000 || player.getSpecialAmount() < 0) {
			return;
		}
		player.reverseSpecialActive();
		Item wep = player.getEquipment().get(Equipment.SLOT_WEAPON);
		int id = wep != null ? wep.getId() : -1;
		switch (id) {
		case 861:
			player.animate(1074);
			player.graphics(256);
			ProjectileManager.sendProjectile(player, victim, 249, false);
			World.getWorld().submit(new Tick(1) {
				public void execute() {
					victim.graphics(256);
					ProjectileManager.sendProjectile(player, victim, 249, false);
					this.stop();
				}
			});
			break;
		case 11235:
			player.animate(426);
			if (player.getEquipment().get(13).getId() == 11212) {
				ProjectileManager.sendDelayedProjectile(player, victim, 1099, 51, 40, 27, false);
				ProjectileManager.sendProjectile(player, victim, 1099, false);
			} else {
				ProjectileManager.sendDelayedProjectile(player, victim, 1102, 51, 40, 27, false);
				ProjectileManager.sendProjectile(player, victim, 1102, false);
			}
			World.getWorld().submit(new Tick(3) {

				@Override
				public void execute() {
					int maxDamage = damage(player, FightType.RANGE);
					int damageInflicted = getHit(player, victim, maxDamage);
					damageInflicted *= (player.getEquipment().get(13).getId() == 11212 ? 1.50 : 1.30);
					if (player.getEquipment().voidSet(2)) {
						damageInflicted *= 1.10;
					}
					if (victim.isPlayer() && victim.getPlayer().getPrayer().usingCorrispondingPrayer(FightType.RANGE)) {
						damageInflicted *= .40;
					}
					victim.getDamageManager().damage(player, damageInflicted, maxDamage, DamageType.RANGE);
					stop();
				}

			});
			break;
		}
		player.deductSpecial(getSpecialDeduction(player, wep));
	}

	@Override
	public int getAborptionBonusId() {
		return Bonuses.RANGE_ABSORPTION;
	}

	public RangeWeapon getRangeWeapon(Player player) {
		return RangeWeapon.rangeWeapons.get(player.getEquipment().getSlot(Equipment.SLOT_WEAPON));
	}

	private static RangeAmmo[] BOW_AMMUNITION = {
		RangeAmmo.BRONZE_ARROW, RangeAmmo.IRON_ARROW, RangeAmmo.STEEL_ARROW, RangeAmmo.MITHRIL_ARROW,
		RangeAmmo.RUNE_ARROW
	};

	private static RangeAmmo[] CBOW_AMMUNITION = {
		RangeAmmo.BRONZE_BOLT, RangeAmmo.OPAL_BOLTS,
		RangeAmmo.IRON_BOLT, RangeAmmo.PEARL_BOLTS,
		RangeAmmo.BLURITE_BOLTS, RangeAmmo.JADE_BOLTS,
		RangeAmmo.STEEL_BOLT, RangeAmmo.TOPAZ_BOLTS,
		RangeAmmo.MITHRIL_BOLT, RangeAmmo.SAPPHIRE_BOLTS,
		RangeAmmo.EMERALD_BOLTS, RangeAmmo.ADAMANT_BOLT,
		RangeAmmo.DIAMOND_BOLTS, RangeAmmo.RUBY_BOLTS, RangeAmmo.RUNE_BOLT,
		RangeAmmo.DRAGON_BOLTS, RangeAmmo.ONYX_BOLTS
	};
	
	/**
	 * @author 'Mystic Flow
	 */
	 private enum WeaponType {
		BOW, CBOW, KNIVES, DARTS, JAVELINE, MULTI_HIT
	}

	/**
	 * @author 'Mystic Flow
	 */
	 private enum RangeWeapon {
		NORMAL_BOW(new int[]{841, 839}, new RangeAmmo[]{RangeAmmo.BRONZE_ARROW, RangeAmmo.IRON_ARROW}, WeaponType.BOW),
		OAK_BOW(new int[]{843, 845}, new RangeAmmo[]{RangeAmmo.BRONZE_ARROW, RangeAmmo.IRON_ARROW, RangeAmmo.STEEL_ARROW}, WeaponType.BOW),
		WILLOW_BOW(new int[]{849, 847}, new RangeAmmo[]{RangeAmmo.BRONZE_ARROW, RangeAmmo.IRON_ARROW, RangeAmmo.STEEL_ARROW, RangeAmmo.MITHRIL_ARROW}, WeaponType.BOW),
		MAPLE_BOW(new int[]{853, 851}, new RangeAmmo[]{RangeAmmo.BRONZE_ARROW, RangeAmmo.IRON_ARROW, RangeAmmo.STEEL_ARROW, RangeAmmo.MITHRIL_ARROW, RangeAmmo.ADAMANT_ARROW}, WeaponType.BOW),
		YEW_BOW(new int[]{857, 855}, new RangeAmmo[]{RangeAmmo.BRONZE_ARROW, RangeAmmo.IRON_ARROW, RangeAmmo.STEEL_ARROW, RangeAmmo.MITHRIL_ARROW, RangeAmmo.ADAMANT_ARROW, RangeAmmo.RUNE_ARROW}, WeaponType.BOW),
		MAGIC_BOW(new int[]{861, 859}, new RangeAmmo[]{RangeAmmo.BRONZE_ARROW, RangeAmmo.IRON_ARROW, RangeAmmo.STEEL_ARROW, RangeAmmo.MITHRIL_ARROW, RangeAmmo.ADAMANT_ARROW, RangeAmmo.RUNE_ARROW}, WeaponType.BOW),

		BRONZE_CBOW(new int[]{9174}, new RangeAmmo[]{RangeAmmo.BRONZE_BOLT, RangeAmmo.OPAL_BOLTS, RangeAmmo.IRON_BOLT, RangeAmmo.PEARL_BOLTS}, WeaponType.CBOW),
		IRON_CBOW(new int[]{9177}, new RangeAmmo[]{RangeAmmo.BRONZE_BOLT, RangeAmmo.OPAL_BOLTS, RangeAmmo.IRON_BOLT, RangeAmmo.PEARL_BOLTS, RangeAmmo.BLURITE_BOLTS, RangeAmmo.JADE_BOLTS, RangeAmmo.STEEL_BOLT, RangeAmmo.TOPAZ_BOLTS}, WeaponType.CBOW),
		STEEL_CBOW(new int[]{9179}, new RangeAmmo[]{RangeAmmo.BRONZE_BOLT, RangeAmmo.OPAL_BOLTS, RangeAmmo.IRON_BOLT, RangeAmmo.PEARL_BOLTS, RangeAmmo.BLURITE_BOLTS, RangeAmmo.JADE_BOLTS, RangeAmmo.STEEL_BOLT, RangeAmmo.TOPAZ_BOLTS, RangeAmmo.MITHRIL_BOLT, RangeAmmo.SAPPHIRE_BOLTS, RangeAmmo.EMERALD_BOLTS}, WeaponType.CBOW),
		MITHRIL_CBOW(new int[]{9181}, new RangeAmmo[]{RangeAmmo.BRONZE_BOLT, RangeAmmo.OPAL_BOLTS, RangeAmmo.IRON_BOLT, RangeAmmo.PEARL_BOLTS, RangeAmmo.BLURITE_BOLTS, RangeAmmo.JADE_BOLTS, RangeAmmo.STEEL_BOLT, RangeAmmo.TOPAZ_BOLTS, RangeAmmo.MITHRIL_BOLT, RangeAmmo.SAPPHIRE_BOLTS, RangeAmmo.EMERALD_BOLTS, RangeAmmo.ADAMANT_BOLT, RangeAmmo.DIAMOND_BOLTS, RangeAmmo.RUBY_BOLTS}, WeaponType.CBOW),
		ADAMANT_CBOW(new int[]{9183}, new RangeAmmo[]{RangeAmmo.BRONZE_BOLT, RangeAmmo.OPAL_BOLTS, RangeAmmo.IRON_BOLT, RangeAmmo.PEARL_BOLTS, RangeAmmo.BLURITE_BOLTS, RangeAmmo.JADE_BOLTS, RangeAmmo.STEEL_BOLT, RangeAmmo.TOPAZ_BOLTS, RangeAmmo.MITHRIL_BOLT, RangeAmmo.SAPPHIRE_BOLTS, RangeAmmo.EMERALD_BOLTS, RangeAmmo.ADAMANT_BOLT, RangeAmmo.DIAMOND_BOLTS, RangeAmmo.RUBY_BOLTS, RangeAmmo.RUNE_BOLT, RangeAmmo.DRAGON_BOLTS, RangeAmmo.ONYX_BOLTS}, WeaponType.CBOW),
		RUNE_CBOW(new int[]{9185}, new RangeAmmo[]{RangeAmmo.BRONZE_BOLT, RangeAmmo.OPAL_BOLTS, RangeAmmo.IRON_BOLT, RangeAmmo.PEARL_BOLTS, RangeAmmo.BLURITE_BOLTS, RangeAmmo.JADE_BOLTS, RangeAmmo.STEEL_BOLT, RangeAmmo.TOPAZ_BOLTS, RangeAmmo.MITHRIL_BOLT, RangeAmmo.SAPPHIRE_BOLTS, RangeAmmo.EMERALD_BOLTS, RangeAmmo.ADAMANT_BOLT, RangeAmmo.DIAMOND_BOLTS, RangeAmmo.RUBY_BOLTS, RangeAmmo.RUNE_BOLT, RangeAmmo.DRAGON_BOLTS, RangeAmmo.ONYX_BOLTS}, WeaponType.CBOW),
		CHAOTIC_BOW(new int[]{18357}, new RangeAmmo[]{RangeAmmo.BRONZE_BOLT, RangeAmmo.OPAL_BOLTS, RangeAmmo.IRON_BOLT, RangeAmmo.PEARL_BOLTS, RangeAmmo.BLURITE_BOLTS, RangeAmmo.JADE_BOLTS, RangeAmmo.STEEL_BOLT, RangeAmmo.TOPAZ_BOLTS, RangeAmmo.MITHRIL_BOLT, RangeAmmo.SAPPHIRE_BOLTS, RangeAmmo.EMERALD_BOLTS, RangeAmmo.ADAMANT_BOLT, RangeAmmo.DIAMOND_BOLTS, RangeAmmo.RUBY_BOLTS, RangeAmmo.RUNE_BOLT, RangeAmmo.DRAGON_BOLTS, RangeAmmo.ONYX_BOLTS}, WeaponType.CBOW),

		ZARYTE_BOW(new int[]{20171, 20172, 20173}, null, WeaponType.BOW),
		CRYSTAL_BOW(new int[]{4212, 4213, 4214, 4215, 4216, 4217, 4218, 4219, 4220, 4221, 4222, 4223}, null, WeaponType.BOW, 249, 250),
		DARK_BOW(new int[]{11235}, new RangeAmmo[]{RangeAmmo.BRONZE_ARROW, RangeAmmo.IRON_ARROW, RangeAmmo.STEEL_ARROW, RangeAmmo.MITHRIL_ARROW, RangeAmmo.ADAMANT_ARROW, RangeAmmo.RUNE_ARROW, RangeAmmo.DRAGON_AMMO}, WeaponType.BOW),

		BRONZE_KNIFE(new int[]{864}, null, WeaponType.KNIVES, 212, 219),
		IRON_KNIFE(new int[]{863}, null, WeaponType.KNIVES, 213, 220),
		STEEL_KNIFE(new int[]{865}, null, WeaponType.KNIVES, 214, 221),
		BLACK_KNIFE(new int[]{869}, null, WeaponType.KNIVES, 215, 222),
		MITHRIL_KNIFE(new int[]{866}, null, WeaponType.KNIVES, 216, 223),
		ADAMANT_KNIFE(new int[]{867}, null, WeaponType.KNIVES, 217, 224),
		RUNE_KNIFE(new int[]{868}, null, WeaponType.KNIVES, 218, 225),
		BRONZE_DART(new int[]{806}, null, WeaponType.DARTS, 226, 1234),
		IRON_DART(new int[]{807}, null, WeaponType.DARTS, 227, 1235),
		STEEL_DART(new int[]{808}, null, WeaponType.DARTS, 228, 1236),
		MITHRIL_DART(new int[]{809}, null, WeaponType.DARTS, 229, 1237),
		ADAMANT_DART(new int[]{810}, null, WeaponType.DARTS, 230, 1239),
		RUNE_DART(new int[]{811}, null, WeaponType.DARTS, 231, 1240),
		BRONZE_JAVELIN(new int[]{825}, null, WeaponType.JAVELINE, 200, 206),
		IRON_JAVELIN(new int[]{826}, null, WeaponType.JAVELINE, 201, 207),
		STEEL_JAVELIN(new int[]{827}, null, WeaponType.JAVELINE, 202, 208),
		MITHRIL_JAVELIN(new int[]{828}, null, WeaponType.JAVELINE, 203, 209),
		ADAMANT_JAVELINE(new int[]{829}, null, WeaponType.JAVELINE, 204, 210),
		RUNE_JAVELINE(new int[]{830}, null, WeaponType.JAVELINE, 205, 211),

		RED_CHINCHOMPA(new int[]{10034}, null, WeaponType.MULTI_HIT, 909, -1);

		private WeaponType type;
		private RangeAmmo[] allowedAmmunition;
		private Graphic drawbackGraphic;
		private int[] items;
		private int projectileId;

		private RangeWeapon(int[] items, RangeAmmo[] disallowedAmmunition, WeaponType type) {
			this(items, disallowedAmmunition, type, -1, -1);
		}

		private RangeWeapon(int[] items, RangeAmmo[] allowedAmmunition, WeaponType type, int projectileId) {
			this(items, allowedAmmunition, type, projectileId, -1);
		}

		private RangeWeapon(int[] items, RangeAmmo[] allowedAmmunition, WeaponType type, int projectileId, int graphic) {
			this.items = items;
			this.type = type;
			this.projectileId = projectileId;
			this.allowedAmmunition = allowedAmmunition;
			if (graphic != -1) {
				this.drawbackGraphic = Graphic.create(graphic, 100 << 16);
			}
		}

		public boolean canAttack(Player player) {
			if (allowedAmmunition == null) {
				return player.getEquipment().getSlot(Equipment.SLOT_WEAPON) == items[0];
			}
			for (RangeAmmo ammo : allowedAmmunition) {
				for (int i : ammo.items) {
					if (player.getEquipment().getSlot(Equipment.SLOT_ARROWS) == i) {
						return true;
					}
				}
			}
			return false;
		}

		public void performDrawback(Player player) {
			if (drawbackGraphic != null) {
				player.graphics(drawbackGraphic);
				return;
			}
			if (allowedAmmunition != null) {
				for (RangeAmmo ammo : allowedAmmunition) {
					for (int i : ammo.items) {
						if (player.getEquipment().getSlot(Equipment.SLOT_ARROWS) == i) {
							player.graphics(ammo.drawbackGraphic);
							return;
						}
					}
				}
			}
		}

		public void performProjectile(Player player, Mob victim, int distance) {
			int projectile = -1;
			if (projectileId != -1) {
				projectile = projectileId;
			} else {
				for (RangeAmmo ammo : allowedAmmunition) {
					for (int i : ammo.items) {
						if (player.getEquipment().getSlot(Equipment.SLOT_ARROWS) == i) {
							projectile = ammo.projectileId;
							break;
						}
					}
				}
			}
			switch (type) {
			case BOW:
				if (this.equals(RangeWeapon.DARK_BOW)) {
					ProjectileManager.sendDelayedProjectile(player, victim, projectile, 51, 40, 27, false);
				}
				ProjectileManager.sendDelayedProjectile(player, victim, projectile, false);
				break;
			case CBOW:
				ProjectileManager.sendGlobalProjectile(projectile, player, victim, 45, 41, 54, 3, 45);//lemme get right speed
				break;
			case KNIVES:
				ProjectileManager.sendGlobalProjectile(projectile, player, victim, 39, 32, 32 + (distance * 5), 7, 40);
				break;
			case DARTS:
				ProjectileManager.sendGlobalProjectile(projectile, player, victim, 41, 32, 32 + (distance * 5), 13, 12);
				break;
			case JAVELINE:
				ProjectileManager.sendGlobalProjectile(projectile, player, victim, 40, 32, 32 + (distance * 5), 0, 40);
				break;
			case MULTI_HIT:
				ProjectileManager.sendGlobalProjectile(projectile, player, victim, 40, 32, 32 + (distance * 5), 0, 23);
				break;
			}
		}

		public boolean is(WeaponType type) {
			return this.type == type;
		}

		private static Map<Integer, RangeWeapon> rangeWeapons = new HashMap<Integer, RangeWeapon>();

		static {
			for (RangeWeapon weapon : RangeWeapon.values()) {
				for (int i : weapon.items) {
					rangeWeapons.put(i, weapon);
				}
			}
		}

	 }

	 /**
	  * @author 'Mystic Flow
	  */
	 private enum RangeAmmo {
		 BRONZE_ARROW(882, 10, 19),
		 IRON_ARROW(884, 9, 18),
		 STEEL_ARROW(886, 11, 20),
		 MITHRIL_ARROW(888, 12, 21),
		 ADAMANT_ARROW(890, 13, 22),
		 RUNE_ARROW(892, 15, 24),
		 BRONZE_BOLT(877, 27, -1),
		 BLURITE_BOLTS(9139, 21, -1),
		 IRON_BOLT(9140, 27, -1),
		 STEEL_BOLT(9141, 27, -1),
		 MITHRIL_BOLT(9142, 27, -1),
		 ADAMANT_BOLT(9143, 27, -1),
		 RUNE_BOLT(9144, 27, -1),
		 OPAL_BOLTS(new int[]{879, 9236}, 27, -1),
		 JADE_BOLTS(new int[]{9335, 9237}, 27, -1),
		 PEARL_BOLTS(new int[]{880, 9238}, 27, -1),
		 TOPAZ_BOLTS(new int[]{9336, 9239}, 27, -1),
		 SAPPHIRE_BOLTS(new int[]{9337, 9240}, 27, -1),
		 EMERALD_BOLTS(new int[]{9338, 9241}, 27, -1),
		 RUBY_BOLTS(new int[]{9339, 9242}, 27, -1),
		 DIAMOND_BOLTS(new int[]{9340, 9243}, 27, -1),
		 DRAGON_BOLTS(new int[]{9341, 9244}, 27, -1),
		 ONYX_BOLTS(new int[]{9342, 9245}, 27, -1),
		 DRAGON_AMMO(11212, 1120, 1111),
		 RED_CHINCHOMPA(10034, 909, -1);

		 private int[] items;
		 private int projectileId;
		 private Graphic drawbackGraphic;

		 private RangeAmmo(int itemId, int projectileId, int graphic) {
			 this(new int[]{itemId}, projectileId, graphic);
		 }

		 private RangeAmmo(int[] items, int projectileId, int graphic) {
			 this.items = items;
			 this.projectileId = projectileId;
			 if (graphic != -1) {
				 this.drawbackGraphic = Graphic.create(graphic, 100 << 16);
			 }
		 }

	 }

}
