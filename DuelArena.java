package org.dementhium.content.minigames;

import org.dementhium.model.Container;
import org.dementhium.model.Item;
import org.dementhium.model.Location;
import org.dementhium.model.World;
import org.dementhium.model.combat.Combat.FightType;
import org.dementhium.model.misc.IconManager;
import org.dementhium.model.player.Equipment;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;
import org.dementhium.tickable.Tick;
import org.dementhium.util.InputHandler;
import org.dementhium.util.Misc;

/**
 * @author Steve <golden_32@live.com>
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class DuelArena {

	// 638 is the axe i think;
	// 18,21 friendly button
	// 19,22 staked button
	// 20 challenge button
	// 631 staked
	// 286
	
	public enum Stage {
		FIRST_SCREEN, SECOND_SCREEN, IN_DUEL
    }
	
	private static final Object[] INTERFACE_SCRIPT =
    {
		"", "", "", "", "", -1, 0, 6, 6, 136, 634 << 16 | 28
	};

	private static final int[][] DUEL_BUTTON =
	{
		{ 36, 1 }, { 44, 2 },
		{ 28, 16 }, { 30, 32 },  { 32, 64 }, {38, 140}, { 40, 256 }, { 42, 512 },
		{ 46, 1024 }, { 34, 4096 }, { 48, 8192 }, { 54, 16384 },
		{ 55, 32768 }, { 56, 65536 }, { 58, 131072 }, { 59, 262144 },
		{ 60, 524288 }, { 61, 2097152 }, { 64, 8388608 }, { 63, 16777216 },
		{ 62, 67108864 }, { 57, 134217728 }, { 50, 268435456 }
	};
	

	public static final String[] DUEL_STRING_CONFIGURATION =
	{
		"Boosted stats will be restored", 
		"Existing prayers will be stopped.", 
		"Some worn items will be taken off."
	};
	
	public static final int[] DUEL_SLOT_IDS = { Equipment.SLOT_HAT,
		Equipment.SLOT_CAPE, Equipment.SLOT_AMULET, Equipment.SLOT_WEAPON,
		Equipment.SLOT_CHEST, Equipment.SLOT_SHIELD,
		Equipment.SLOT_LEGS, Equipment.SLOT_HANDS,
		Equipment.SLOT_FEET, Equipment.SLOT_RING, Equipment.SLOT_ARROWS,
	};
	
	public static final String[] DUEL_STRING_CONFIG = 
	{	
		"You cannot forfeit the duel.",
		"You cannot move.",
		"You cannot use Ranged attacks.",
		"You cannot use melee attacks.",
		"You cannot use Magic attacks.",
		"You cannot use drinks.",
		"You cannot use food.",
		"You cannot use Prayer.",
		"There will be obstacles in the arena.",
	};
	
	public static final Location[] RANDOM_LOCATIONS = 
	{
		Location.locate(3358, 3268, 0),
		Location.locate(3361, 3271, 0),
		Location.locate(3376, 3269, 0),
		Location.locate(3377, 3277, 0),
		Location.locate(3355, 3276, 0),
		Location.locate(3366, 3275, 0)
	};

	public static final int NO_FORFEIT = 0;
	public static final int NO_MOVEMENT = 1;
	public static final int NO_RANGE = 2;
	public static final int NO_MELEE = 3;
	public static final int NO_MAGIC = 4;
	public static final int NO_DRINKS = 5;
	public static final int NO_FOOD = 6;
	public static final int NO_PRAYER = 7;
	public static final int OBSTACLES = 8;
	public static final int FUN_WEAPONS = 9;
	public static final int NO_SPECIAL_ATTACKS = 10;
	public static final int NO_HATS = 11;
	public static final int NO_CAPES = 12;
	public static final int NO_AMULETS = 13;
	public static final int NO_SWORDS = 14;
	public static final int NO_BODIES = 15;
	public static final int NO_SHIELDS = 16;
	public static final int NO_LEGS = 17;
	public static final int NO_GLOVES = 18;
	public static final int NO_BOOTS = 19;
	public static final int NO_RINGS = 20;
	public static final int NO_ARROWS = 21;
	public static final int SUMMONING = 22;




	private Player p1;
	private Player p2;
	private boolean isStaked;
	private Stage currentStage = Stage.FIRST_SCREEN;
	private Container p1Items = new Container(9, false), p2Items = new Container(9, false);
	private boolean p1Accept = false, p2Accept = false;
	private int rulesActivated;
	Location oMin = Location.locate(3335, 3227, 0);
	Location oMax = Location.locate(3355, 3237, 0);
	Location nMin = Location.locate(3366, 3227, 0);
	Location nMax = Location.locate(3386, 3237, 0);

	public DuelArena(Player p1, Player p2, boolean isStaked) {
		this.p1 = p1;
		this.p2 = p2;
		this.isStaked = isStaked;
		p1.setAttribute("didRequestDuel", Boolean.FALSE);
		p2.setAttribute("didRequestDuel", Boolean.FALSE);
		openFirstInterface(p1);
		openFirstInterface(p2);
		refreshConfigs(0);
		refreshScreen(false, 0);
	}

	private void refreshScreen(boolean sendConfigs, int config) {
		ActionSender.sendItems(p1, 134, p1Items, false);
		ActionSender.sendItems(p2, 134, p2Items, false);
		ActionSender.sendItems(p1, 134, p2Items, true);
		ActionSender.sendItems(p2, 134, p1Items, true);
		if (sendConfigs) {
			if(config == -1) {
				return;
			}
			refreshConfigs(0);
			if((rulesActivated & config) == 0) {
				rulesActivated |= config;
			} else {
				rulesActivated ^= config;
			}
			refreshConfigs(rulesActivated);
		}
	}

	private void refreshConfigs(int config) {
		ActionSender.sendConfig(p1, 286, config);
		ActionSender.sendConfig(p2, 286, config);
	}

	public void stakeItem(Player p, int slot, int amt) {
		if (getContainer(p).getTakenSlots() >= 9) {
			p.sendMessage("You cannot put anymore items in this stake");
			return;
		}
		Item inventoryItem = p.getInventory().getContainer().get(slot);
		if (inventoryItem != null) {
			if (amt > p.getInventory().getContainer().getNumberOf(inventoryItem)) {
				amt = p.getInventory().getContainer().getItemCount(inventoryItem.getId());
			}
		}
		Item item = new Item(p.getInventory().getContainer().get(slot).getId(), amt);
		resetAccept();
		if (item != null) {
			if (p.getInventory().getContainer().getItemCount(item.getId()) < amt) {
				item.setAmount(p.getInventory().getContainer().getItemCount(item.getId()));
			}
			if (getContainer(p).getFreeSlots() < amt && !p.getInventory().getContainer().get(slot).getDefinition().isNoted() && !p.getInventory().getContainer().get(slot).getDefinition().isStackable()) {
				item.setAmount(getContainer(p).getFreeSlots());
			}
			getContainer(p).add(item);
			p.getInventory().getContainer().remove(new Item(p.getInventory().getContainer().get(slot).getId(), amt));
			p.getInventory().refresh();
		}
		refreshScreen(false, 0);
	}

	private Container getContainer(Player p) {
		return p.equals(p1) ? p1Items : p2Items;
	}
	private void openFirstInterface(Player p) {
		if (isStaked) {
			ActionSender.sendInventoryInterface(p, 628);
			ActionSender.sendInterface(p, 631);
			ActionSender.sendDuelOptions(p);
			ActionSender.sendString(p, 631, 23, Misc.formatPlayerNameForDisplay(getOpponent(p).getUsername()));
			ActionSender.sendString(p, 631, 25, Integer.toString(getOpponent(p).getSkills().getCombatLevel()));
		} else {
			ActionSender.sendInterface(p, 637);
			ActionSender.sendString(p, 637, 15, Misc.formatPlayerNameForDisplay(getOpponent(p).getUsername()));
			ActionSender.sendString(p, 637, 17, Integer.toString(getOpponent(p).getSkills().getCombatLevel()));
		}
	}

	private Player getOpponent(Player p) {
		return p == p1 ? p2 : p1;
	}

	public void handleButtons(Player p, int opcode, int slot, int buttonId) {
		if ((buttonId == 21 && isStaked) || buttonId == 100 || buttonId == 10 || buttonId == 85 || buttonId == 13) {
			duelCancelled(p);
		}
		switch (currentStage) {
		case FIRST_SCREEN:
			if (buttonId == 93 || buttonId == 82) {
				handleAcceptButton(p);
				return;
			}
			if (buttonId < 90) {
				if (!isStaked) {
					buttonId += 9;
				}
				if(buttonId < 51 && buttonId % 2 != 0) {
					buttonId++;
				}
				int config = configForButton(buttonId);
				resetAccept();
				refreshScreen(true, config);
			} else {
				switch(opcode) {
				case 6: 
					removeItem(p, slot, 1);
					break;
				case 13:
					removeItem(p, slot, 5);
					break;
				case 0:
					removeItem(p, slot, 10);
					break;
				case 15:
					Item item = p.getInventory().getContainer().get(slot);
					removeItem(p, slot, p.getInventory().getContainer().getNumberOf(item));// getContainer(slot).getAmount());
					break;
				case 46:
					InputHandler.requestIntegerInput(p, 2, "Please enter an amount:");
					p.setAttribute("inputId", 6);
					p.setAttribute("slotId", slot);
					break;
				case 58:
					p.sendMessage(p.getInventory().getContainer().get(slot).getDefinition().getExamine());
					break;
				}
			}
			break;
		case SECOND_SCREEN:
			System.out.println(buttonId);
			if (buttonId == 35) {
				handleAcceptButton(p);
			}
			break;
		}
	}

	private void handleAcceptButton(Player p) {
		switch (currentStage) {
		case FIRST_SCREEN:
			String check = checkRules();
			if(check != null) {
				ActionSender.sendString(p, check, isStaked ? 631 : 637, isStaked ? 26 : 44);
				return;
			}
			if (p.equals(p1)) {
				p1Accept = true;
				if (p1Accept && p2Accept) {
					displaySecond(p1, p2);
				} else {
					ActionSender.sendString(p1, "Waiting for other player...", isStaked ? 631 : 637, isStaked ? 26 : 44);
					ActionSender.sendString(p2, "Other player has accepted", isStaked ? 631 : 637, isStaked ? 26 : 44);
				}
			} else {
				p2Accept = true;
				if (p1Accept && p2Accept) {
					displaySecond(p1, p2);
				} else {
					ActionSender.sendString(p2, "Waiting for other player...", isStaked ? 631 : 637, isStaked ? 26 : 44);
					ActionSender.sendString(p1, "Other player has accepted", isStaked ? 631 : 637, isStaked ? 26 : 44);
				}
			}
			break;
		case SECOND_SCREEN:
			if (p.equals(p1)) {
				p1Accept = true;
				if (p1Accept && p2Accept) {
					startDuel(p1, p2);
				} else {
					ActionSender.sendString(p1, "Waiting for other player...", 639, 33);
					ActionSender.sendString(p2, "Other player has accepted", 639, 33);
				}
			} else {
				p2Accept = true;
				if (p1Accept && p2Accept) {
					startDuel(p1, p2);
				} else {
					ActionSender.sendString(p2, "Waiting for other player...", 639, 33);
					ActionSender.sendString(p1, "Other player has accepted", 639, 33);
				}
			}
			break;
		}
		
	}
	
	private String checkRules() {
		if(ruleActivated(NO_MELEE) && ruleActivated(NO_MAGIC) && ruleActivated(NO_RANGE)) {
			return "You can't have No Ranged, No Melee and No Magic - how would you fight?";
		} else if(ruleActivated(NO_MOVEMENT) && ruleActivated(OBSTACLES)) {
			return "You can't have No Movement and Obstacles";
		}
		return null;
	}

	private void startDuel(Player... players) {
		for (Player p : players) {
			ActionSender.sendCloseInterface(p);
			checkEquipment(p);
			p.getPrayer().closeAllPrayers();
			sendToArena(p);
			p.setAttribute("isInDuelArena", Boolean.TRUE);
			p.setAttribute("cantMove", Boolean.TRUE);
			ActionSender.sendInterfaceConfig(p, 638, 1, false);
		}
		World.getWorld().submit(new Tick(2) {
			int time = 3;
			@Override
			public void execute() {
				if (time > 0) {
					if (time == 3) {
						IconManager.iconOnMob(p1, p2, 1, -1);
						IconManager.iconOnMob(p2, p1, 1, -1);
					}
					p1.forceText(Integer.toString(time));
					p2.forceText(Integer.toString(time));
					time--;
				} else  {
					p1.forceText("FIGHT!");
					p2.forceText("FIGHT!");
					currentStage = Stage.IN_DUEL;
					p1.removeAttribute("cantMove");
					p2.removeAttribute("cantMove");
					this.stop();
				}
			}
		});
		
	}

	private void sendToArena(Player p) {
		if (ruleActivated(OBSTACLES)) {
			p.teleport(Location.locate(oMin.getX() + Misc.random(oMax.getX() - oMin.getX()), oMin.getY() + Misc.random(oMax.getY() - oMin.getY()), 0));
		} else {
			p.teleport(Location.locate(nMin.getX() + Misc.random(nMax.getX() - nMin.getX()), nMin.getY() + Misc.random(nMax.getY() - nMin.getY()), 0));
		}
		
	}

	private void checkEquipment(Player p) {
		for (int i = NO_HATS; i <= NO_ARROWS; i++) {
			if(ruleActivated(i)) {
				if(p.getEquipment().get(DUEL_SLOT_IDS[i - 11]) != null) {
					p.getInventory().getContainer().add(p.getEquipment().get(DUEL_SLOT_IDS[i - 11]));
					p.getEquipment().set(DUEL_SLOT_IDS[i - 11], null);
					p.getInventory().refresh();
				}
			}
		}
		
	}
	
	public boolean canEquip(int slot) {
		return ruleActivated(slot + 11);
	}

	private void displaySecond(Player... players) {
		resetAccept();
		for(Player p : players) {
			ActionSender.closeInventoryInterface(p);
			if (isStaked) {
				if (!p.getInventory().getContainer().hasSpaceFor(getContainer(getOpponent(p))) || p.getInventory().getFreeSlots() < amtOfItmsRemoved(p)) {
					p.sendMessage("You do not have enough space in your inventory for the stake!");
					duelCancelled(p);
					return;
				}
			}
			for(int i = 16; i < 34; i++) {
				if(i == 21) {
					continue;
				}
				ActionSender.sendString(p, "", 639, i);
			}
			ActionSender.sendString(p, "Boosted stats will be restored", 639, 16);
			int childId = 17;
			if(ruleActivated(NO_PRAYER)) {
				ActionSender.sendString(p, "Existing prayers will be stopped.", 639, childId);
				childId++;
			}
			for(int i = 11; i < 21; i++) {
				if(ruleActivated(i)) {
					ActionSender.sendString(p, "Some worn items will be taken off.", 639, childId);
					childId++;
					break;
				}
			}
			int childIndex = 1;
			for(int i = 0; i < 23; i++) {
				if(ruleActivated(i)) {
					if(i >= DUEL_STRING_CONFIG.length) {
						break;
					}
					ActionSender.sendString(p, DUEL_STRING_CONFIG[i], 639, 21+childIndex);
					childIndex++;
				}
			}
			ActionSender.sendInterface(p, 639);
		}
		currentStage = Stage.SECOND_SCREEN;
	}
	
	private int amtOfItmsRemoved(Player p) {
		int amt = 0;
		for (int i = NO_HATS; i <= NO_ARROWS; i++) {
			if(ruleActivated(i)) {
				if(p.getEquipment().get(DUEL_SLOT_IDS[i - 11]) != null) {
					amt++;
				}
			}
		}
		return amt;
	}

	public boolean ruleActivated(int rule) {
		return (rulesActivated & DUEL_BUTTON[rule][1]) != 0;
	}
	

	public void removeItem(Player p, int slot, int amt) {
		resetAccept();
		Item item = new Item(getContainer(p).get(slot).getId(), amt);
		if (item != null) {
			if (getContainer(p).getItemCount(item.getId()) < amt) {
				item.setAmount(getContainer(p).getItemCount(item.getId()));
			}
			if (p.getInventory().getFreeSlots() < amt && !getContainer(p).get(slot).getDefinition().isNoted() && !getContainer(p).get(slot).getDefinition().isStackable()) {
				item.setAmount(p.getInventory().getFreeSlots());
			}
			p.getInventory().getContainer().add(new Item(getContainer(p).get(slot).getId(), item.getAmount()));
			p.getInventory().refresh();
			getContainer(p).remove(item);
			refreshScreen(false, 0);
		}

	}

	private void resetAccept() {
		p1Accept = p2Accept = false; // let me check something
	}

	private void duelCancelled(Player canceller) {
		for (Item itemAtIndex : p1Items.toArray()) {
			if (itemAtIndex != null) {
				p1.getInventory().getContainer().add(itemAtIndex);
			}
		}
		for (Item itemAtIndex : p2Items.toArray()) {
			if (itemAtIndex != null) {
				p2.getInventory().getContainer().add(itemAtIndex);
			}
		}
		endDuel();
		p1.getInventory().refresh();
		p2.getInventory().refresh();
		if(canceller == p1) {
			p2.sendMessage("Other player declined stake and duel options.");
		} else {
			p1.sendMessage("Other player declined stake and duel options.");
		}
	}

	private void endDuel() {
		p1.removeAttribute("duelSession"); 
		p2.removeAttribute("duelSession"); 
		ActionSender.sendCloseInterface(p1);
		ActionSender.sendCloseInterface(p2);
		ActionSender.closeInventoryInterface(p1);
		ActionSender.closeInventoryInterface(p2);
	}

	public int configForButton(int buttonId) {
		for(int[] array : DUEL_BUTTON) {
			if(array[0] == buttonId) {
				return array[1];
			}
		}
		return -1;
	}

	public void end(Player loser) {
		Player winner = getOpponent(loser);
		loser.teleport(RANDOM_LOCATIONS[Misc.random(RANDOM_LOCATIONS.length - 1)]);
		getOpponent(loser).teleport(RANDOM_LOCATIONS[Misc.random(RANDOM_LOCATIONS.length - 1)]);
		if (isStaked) {
			for (Item itemAtIndex : getContainer(winner).toArray()) {
				if (itemAtIndex != null) {
					winner.getInventory().getContainer().add(itemAtIndex);
				}
			}
			ActionSender.sendInterface(winner, 634);
			ActionSender.sendAMask(winner, 1026, 634, 28, 0, 35);
			ActionSender.sendClientScript(winner, 149, INTERFACE_SCRIPT, "noooobsssss");
			ActionSender.sendItems(winner, 136, getContainer(loser), false);
			ActionSender.sendString(winner, 634, 33, Misc.formatPlayerNameForDisplay(loser.getUsername()));
			ActionSender.sendString(winner, 634, 32, Integer.toString(loser.getSkills().getCombatLevel()));
		} else {
			ActionSender.sendInterface(winner, 634);
			endDuel();
		}
		IconManager.removeIcon(p1, p2);
		IconManager.removeIcon(p2, p1);
		p1.removeAttribute("isInDuelArena");
		p2.removeAttribute("isInDuelArena");
		
	}

	public void finishCompletely(Player p) {
		endDuel();
		for (Item itemAtIndex : getContainer(getOpponent(p)).toArray()) {
			if (itemAtIndex != null) {
				p.getInventory().getContainer().add(itemAtIndex);
			}
		}
		p.getInventory().refresh();
		
	}

	public boolean canUseStyle(FightType type) {
		switch (type) {
		case MAGIC:
			return ruleActivated(NO_MAGIC);
		case MELEE:
			return ruleActivated(NO_MELEE);
		case RANGE:
			return ruleActivated(NO_RANGE);
		}
		return false;
	}
	
	public Stage getCurrentStage() {
		return currentStage;
	}
}
