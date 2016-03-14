/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nde;

import nde.definitions.NDEUpdate;

/**
 *
 * @author Emperor
 */
public class NDEGraphicalUpdate {

    /**
     * Constructs a new {@code NDEGraphicalUpdate} instance.
     */
    public NDEGraphicalUpdate() {
        /* empty */
    }

    public static boolean isValidChar(char ch) {
        switch (ch) {
            case ',':
            case ';':
            case ' ':
            case ':':
            case '-':
            case '+':
                return true;
        }
        return false;
    }
    /**
     * Draws the statistics interface, holding all the current combat-related calculations.
     * @param g The Graphics used.
     */
    public void drawStatistics(NDEView ndeView) {
        NDEUpdate defs = NDEView.getNdeUpdate();
        //ndeView.getTextArea().r
        ndeView.getTextArea().selectAll();
        ndeView.getTextArea().replaceSelection("");
        ndeView.getTextArea().append("Maximum melee hit: " + getMaximumMeleeHit(defs) + "." + System.getProperty("line.separator"));
        ndeView.getTextArea().append("Maximum magic hit: " + getMaximumMagicHit(defs) + "." + System.getProperty("line.separator"));
        ndeView.getTextArea().append("Maximum range hit: " + getMaximumRangeHit(defs) + "." + System.getProperty("line.separator"));
        ndeView.getTextArea().append(System.getProperty("line.separator"));
        ndeView.getTextArea().append("Melee accuracy: " + getMaxMeleeAcc(defs) + "." + System.getProperty("line.separator"));
        ndeView.getTextArea().append("Magic accuracy: " + getMaxMagicAcc(defs) + "." + System.getProperty("line.separator"));
        ndeView.getTextArea().append("Range accuracy: " + getMaxRangeAcc(defs) + "." + System.getProperty("line.separator"));
        ndeView.getTextArea().append(System.getProperty("line.separator"));
        ndeView.getTextArea().append("Maximum defence: " + getMaximumDefence(defs) + ".");
       /* NDEUpdate defs = ndeView.getNdeUpdate();
	BufferedImage image = null;
	try {
            image = ImageIO.read(new File("./src/nde/resources/BackGround1.png"));
	} catch (IOException e) {
            e.printStackTrace();
	}
	try {
            g.drawImage(image, 515, 84, ndeView.getMainFrame());
        } catch (Exception e) {
            e.printStackTrace();
        }
	Font ar = new Font("SansSerif", Font.BOLD, 11);
	Font ars = new Font("SansSerif", Font.BOLD, 10);
	g.setColor(Color.BLACK);
	g.setFont(ars);
	g.drawString("Melee max hit: " + getMaximumMeleeHit(defs), 529, 133);
	g.drawString("Range max hit: " + getMaximumRangeHit(defs), 529, 144);
	g.drawString("Magic max hit: " + getMaximumMagicHit(defs), 529, 155);
	g.drawString("Melee max acc: " + getMaxMeleeAcc(defs), 529, 187);
	g.drawString("Range max acc: " + getMaxRangeAcc(defs), 529, 198);
	g.drawString("Magic max acc: " + getMaxMagicAcc(defs), 529, 209);
	g.drawString("Max defence: " + getMaximumDefence(defs), 529, 224);
	g.setFont(ar);
	g.drawString("NPC id: " + defs.getNpcId(), 546, 101);
	g.drawString("Max damages:", 529, 121);
	g.drawString("Max bonuses:", 529, 175);
	g.drawString("Save", 571, 249);
	g.setColor(Color.ORANGE);
	g.drawString("NPC id: " + defs.getNpcId(), 545/*581 - fm.stringWidth("NPC id: " + npcId) / 2/, 100 );
	g.drawString("Max damages:", 529, 120);
	g.drawString("Max bonuses:", 529, 174);
	g.drawString("Save", 570, 248);
	g.setFont(ars);
	g.drawString("Melee max hit: " + getMaximumMeleeHit(defs), 528, 132);
	g.drawString("Range max hit: " + getMaximumRangeHit(defs), 528, 143);
	g.drawString("Magic max hit: " + getMaximumMagicHit(defs), 528, 154);
	g.drawString("Melee max acc: " + getMaxMeleeAcc(defs), 528, 186);
	g.drawString("Range max acc: " + getMaxRangeAcc(defs), 528, 197);
	g.drawString("Magic max acc: " + getMaxMagicAcc(defs), 528, 208);
	g.drawString("Max defence: " + getMaximumDefence(defs), 528, 223);*/
    }
    
    	private double getMaximumMeleeHit(NDEUpdate defs) {
		int strLvl = defs.getStrengthLevel();
		int strBonus = defs.getBonuses()[11];
		double cumulativeStr = strLvl + 1;
		return (14 + cumulativeStr + (strBonus / 8) + ((cumulativeStr * strBonus) / 64)) * 1;
	}
	
	private double getMaximumRangeHit(NDEUpdate defs) {
		int strLvl = defs.getRangeLevel();
		int strBonus = defs.getBonuses()[12];
		double cumulativeStr = strLvl + 1;
		return (14 + cumulativeStr + (strBonus / 8) + ((cumulativeStr * strBonus) / 64)) * 1;
	}
	
	private double getMaximumMagicHit(NDEUpdate defs) {
		int strLvl = defs.getMagicLevel();
		int strBonus = defs.getBonuses()[13];
		double cumulativeStr = strLvl + 1;
		return (14 + cumulativeStr + (strBonus / 8) + ((cumulativeStr * strBonus) / 64)) * 1;
	}
	
	private double getMaxMeleeAcc(NDEUpdate defs) {
		int style = 1;
		int attLvl = defs.getAttackLevel();
		int attBonus = defs.getBonuses()[1];
		double cumulativeAtt = attLvl + style;
		return (14 + cumulativeAtt + (attBonus / 8) + ((cumulativeAtt * attBonus) / 64));
	}
	
	private double getMaxRangeAcc(NDEUpdate defs) {
		int style = 1;
		int attLvl = defs.getRangeLevel();
		int attBonus = defs.getBonuses()[4];
		double cumulativeAtt = attLvl + style;
		return (14 + cumulativeAtt + (attBonus / 8) + ((cumulativeAtt * attBonus) / 64));
	}
	
	private double getMaxMagicAcc(NDEUpdate defs) {
		int style = 1;
		int attLvl = defs.getMagicLevel();
		int attBonus = defs.getBonuses()[3];
		double cumulativeAtt = attLvl + style;
		return (14 + cumulativeAtt + (attBonus / 8) + ((cumulativeAtt * attBonus) / 64));
	}

	private double getMaximumDefence(NDEUpdate defs) {
		int style = 1;
		int defLvl = defs.getDefenceLevel();
		int defBonus = defs.getBonuses()[6];
		double cumulativeDef = defLvl + style;
		double defence = ((14 + cumulativeDef + (defBonus / 8) + ((cumulativeDef * defBonus) / 64)));
		return defence;
	}
}
