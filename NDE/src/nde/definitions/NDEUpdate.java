/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nde.definitions;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import nde.NDEView;

/**
 *
 * @author Emperor
 */
public class NDEUpdate {

        private static final String[] VARIABLE_NAMES = new String[] { "NPC id", "Combat lvl.", "Lifepoints", "Respawn",
            "Attack anim.", "Defence anim.", "Death anim.",
            "Attack lvl.", "Strength lvl.", "Defence lvl.", "Range lvl.", "Magic lvl.",
            "Attack speed", "Start GFX", "Proj. id", "End GFX", "Melee", "Range", "Magic", "Agressive", "Examine"
        };

        private int npcId = -1;

        /**
         * The NPCs bonuses.
         */
        private short[] bonuses = new short[14];

	/**
	 * The NPCs examine info.
	 */
	private String examine = "This is an NPC.";

	/**
	 * The NPCs combat level.
	 */
	private short combatLevel = 3;

	/**
	 * The NPCs lifepoints.
	 */
	private short lifepoints = 100;

	/**
	 * The NPCs respawn time.
	 */
	private byte respawn = 17;

	/**
	 * The NPCs attack animation.
	 */
	private short attackAnimation = -1;

	/**
	 * The NPCs defence animation.
	 */
	private short defenceAnimation = -1;

	/**
	 * The NPCs death animation.
	 */
	private short deathAnimation = -1;

	/**
	 * The NPCs strength level.
	 */
	private short strengthLevel = 1;

	/**
	 * The NPCs attack level.
	 */
	private short attackLevel = 1;

	/**
	 * The NPCs defence level.
	 */
	private short defenceLevel = 1;

	/**
	 * The NPCs range level.
	 */
	private short rangeLevel = 1;

	/**
	 * The NPCs magic level.
	 */
	private short magicLevel = 1;

	/**
	 * The NPCs attack speed.
	 */
	private byte attackSpeed = 5;

	/**
	 * The start graphics (Magic-Range)
	 */
	private short startGraphics = -1;

	/**
	 * The projectile graphics id. (Magic-Range)
	 */
	private short projectileId = -1;

	/**
	 * The end graphics (Magic-Range)
	 */
	private short endGraphics = -1;

	/**
	 * If the NPC is using melee.
	 */
	private boolean usingMelee = true;

	/**
	 * If the NPC is using range.
	 */
	private boolean usingRange = false;

	/**
	 * If the NPC is using magic.
	 */
	private boolean usingMagic = false;

	/**
	 * If the NPC is aggressive.
	 */
	private boolean aggressive = false;

        /**
         * If the NPC is immune to poison.
         */
        private boolean poisonImmune = false;

        /**
         * The NPC's walking area.
         */
        private int[] walkingArea = new int[4];


    /**
     * Updates the JTextFields with the correct information.
     */
    public void update(NDEView view, int npcId) {
        this.npcId = npcId;
        File file = new File("./data/NPCs/NPCDefinition" + npcId + ".xml");
        if (!file.exists()) {
            setDefaults(npcId);
        } else {
            XMLParsing.load(this, file);
            System.out.println("Loaded file " + file.getName() + "!");
        }
        updateTextFields(view);
        updateCheckBoxes(view);
    }

    private void setDefaults(int npc) {
        npcId = npc;
        bonuses = new short[14];
        examine = "This is an NPC.";
        combatLevel = 3;
        lifepoints = 100;
        respawn = 17;
        attackAnimation = 422;
        defenceAnimation = 404;
        deathAnimation = 9055;
        strengthLevel = 1;
        attackLevel = 1;
        defenceLevel = 1;
        rangeLevel = 1;
        magicLevel = 1;
        attackSpeed = 5;
        startGraphics = -1;
        projectileId = -1;
        endGraphics = -1;
        usingMelee = true;
        usingRange = false;
        usingMagic = false;
        aggressive = false;
        poisonImmune = false;
        walkingArea = new int[4];
    }

    public void updateTextFields(NDEView nde) {
        javax.swing.JTextField[] tfs = nde.getTextFields();
        tfs[0].setText("" + npcId);
        tfs[1].setText(examine);
        tfs[2].setText("" + combatLevel);
        tfs[3].setText("" + lifepoints);
        tfs[4].setText("" + respawn);
        tfs[5].setText("" + attackAnimation);
        tfs[6].setText("" + defenceAnimation);
        tfs[7].setText("" + deathAnimation);
        tfs[8].setText("" + attackLevel);
        tfs[9].setText("" + strengthLevel);
        tfs[10].setText("" + defenceLevel);
        tfs[11].setText("" + rangeLevel);
        tfs[12].setText("" + magicLevel);
        tfs[13].setText("" + attackSpeed);
        tfs[14].setText("" + startGraphics);
        tfs[15].setText("" + projectileId);
        tfs[16].setText("" + endGraphics);
    }

    public void updateCheckBoxes(NDEView nde) {
        javax.swing.JCheckBox[] cbs = nde.getCheckBoxes();
        cbs[0].setSelected(usingMelee);
        cbs[1].setSelected(usingRange);
        cbs[2].setSelected(usingMagic);
        cbs[3].setSelected(aggressive);
        cbs[4].setSelected(poisonImmune);
    }

    public void saveDefinitions() {
        System.out.println("Saving definitions... " + this.toString());
        javax.swing.JTextField[] tfs = NDEView.getInstance().getTextFields();
        /*npcId = Integer.parseInt(tfs[0].getText());
        examine = tfs[1].getText();
        combatLevel = Short.parseShort(tfs[2].getText());
        lifepoints = Short.parseShort(tfs[3].getText());
        respawn = Byte.parseByte(tfs[4].getText());
        attackAnimation = Short.parseShort(tfs[5].getText());
        defenceAnimation = Short.parseShort(tfs[6].getText());
        deathAnimation = Short.parseShort(tfs[7].getText());
        attackLevel = Short.parseShort(tfs[8].getText());
        strengthLevel = Short.parseShort(tfs[9].getText());
        defenceLevel = Short.parseShort(tfs[10].getText());
        rangeLevel = Short.parseShort(tfs[11].getText());
        magicLevel = Short.parseShort(tfs[12].getText());
        attackSpeed = Byte.parseByte(tfs[13].getText());
        startGraphics = Short.parseShort(tfs[14].getText());
        projectileId = Short.parseShort(tfs[15].getText());
        endGraphics = Short.parseShort(tfs[16].getText());
        */
        try {
            XMLParsing.save(this);
        } catch (IOException ex) {
            Logger.getLogger(NDEUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean isAggressive() {
        return aggressive;
    }

    public void setAggressive(boolean aggressive) {
        this.aggressive = aggressive;
    }

    public short getAttackAnimation() {
        return attackAnimation;
    }

    public void setAttackAnimation(short attackAnimation) {
        this.attackAnimation = attackAnimation;
    }

    public short getAttackLevel() {
        return attackLevel;
    }

    public void setAttackLevel(short attackLevel) {
        this.attackLevel = attackLevel;
    }

    public byte getAttackSpeed() {
        return attackSpeed;
    }

    public void setAttackSpeed(byte attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public short[] getBonuses() {
        return bonuses;
    }

    public void setBonus(int index, short bonuses) {
        this.bonuses[index] = bonuses;
    }

    public short getCombatLevel() {
        return combatLevel;
    }

    public void setCombatLevel(short combatLevel) {
        this.combatLevel = combatLevel;
    }

    public short getDeathAnimation() {
        return deathAnimation;
    }

    public void setDeathAnimation(short deathAnimation) {
        this.deathAnimation = deathAnimation;
    }

    public short getDefenceAnimation() {
        return defenceAnimation;
    }

    public void setDefenceAnimation(short defenceAnimation) {
        this.defenceAnimation = defenceAnimation;
    }

    public short getDefenceLevel() {
        return defenceLevel;
    }

    public void setDefenceLevel(short defenceLevel) {
        this.defenceLevel = defenceLevel;
    }

    public short getEndGraphics() {
        return endGraphics;
    }

    public void setEndGraphics(short endGraphics) {
        this.endGraphics = endGraphics;
    }

    public String getExamine() {
        return examine;
    }

    public void setExamine(String examine) {
        this.examine = examine;
    }

    public short getLifepoints() {
        return lifepoints;
    }

    public void setLifepoints(short lifepoints) {
        this.lifepoints = lifepoints;
    }

    public short getMagicLevel() {
        return magicLevel;
    }

    public void setMagicLevel(short magicLevel) {
        this.magicLevel = magicLevel;
    }

    public int getNpcId() {
        return npcId;
    }

    public void setNpcId(int npcId) {
        this.npcId = npcId;
    }

    public short getProjectileId() {
        return projectileId;
    }

    public void setProjectileId(short projectileId) {
        this.projectileId = projectileId;
    }

    public short getRangeLevel() {
        return rangeLevel;
    }

    public void setRangeLevel(short rangeLevel) {
        this.rangeLevel = rangeLevel;
    }

    public byte getRespawn() {
        return respawn;
    }

    public void setRespawn(byte respawn) {
        this.respawn = respawn;
    }

    public short getStartGraphics() {
        return startGraphics;
    }

    public void setStartGraphics(short startGraphics) {
        this.startGraphics = startGraphics;
    }

    public short getStrengthLevel() {
        return strengthLevel;
    }

    public void setStrengthLevel(short strengthLevel) {
        this.strengthLevel = strengthLevel;
    }

    public boolean isUsingMagic() {
        return usingMagic;
    }

    public void setUsingMagic(boolean usingMagic) {
        this.usingMagic = usingMagic;
    }

    public boolean isUsingMelee() {
        return usingMelee;
    }

    public void setUsingMelee(boolean usingMelee) {
        this.usingMelee = usingMelee;
    }

    public boolean isUsingRange() {
        return usingRange;
    }

    public void setUsingRange(boolean usingRange) {
        this.usingRange = usingRange;
    }

    public boolean isPoisonImmune() {
        return poisonImmune;
    }

    public void setPoisonImmune(boolean poisonImmune) {
        this.poisonImmune = poisonImmune;
    }

}
