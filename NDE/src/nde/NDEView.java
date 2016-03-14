/*
 * NDEView.java
 */
package nde;

import java.awt.Font;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import nde.definitions.NDEUpdate;
import nde.definitions.XMLParsing;
import nde.resources.ResourceRequest;

/**
 * The application's main frame.
 * @author Emperor
 */
public class NDEView extends FrameView {

    private static NDEView INSTANCE;

    public NDEView(SingleFrameApplication app) {
        super(app);
        NDEView.INSTANCE = this;
        initComponents();

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        initText();
        ndeUpdate.update(this, 0);
        ndeGraphics.drawStatistics(this);
    }

    private void initText() {
        try {
            ResourceRequest rq = new ResourceRequest(new File("./src/nde/resources/NDEView.properties"));
            rq.init();
            jTextField1.setToolTipText(rq.getResource("npcIdTextField.toolTipText")); // NOI18N
            jTextField2.setToolTipText(rq.getResource("npcExamineTextField.toolTipText")); // NOI18N
            jTextField3.setToolTipText(rq.getResource("combatLevelField.toolTipText")); // NOI18N
            jTextField4.setToolTipText(rq.getResource("lifepointsField.toolTipText")); // NOI18N
            jTextField5.setToolTipText(rq.getResource("respawnField.toolTipText")); // NOI18N
            jTextField6.setToolTipText(rq.getResource("attackAnimationField.toolTipText")); // NOI18N
            jTextField7.setToolTipText(rq.getResource("defenceAnimationField.toolTipText")); // NOI18N
            jTextField8.setToolTipText(rq.getResource("deathAnimationField.toolTipText")); // NOI18N
            jTextField9.setToolTipText(rq.getResource("attackLevelField.toolTipText")); // NOI18N
            jTextField10.setToolTipText(rq.getResource("strengthLevelField.toolTipText")); // NOI18N
            jTextField11.setToolTipText(rq.getResource("defenceLevelField.toolTipText")); // NOI18N
            jTextField12.setToolTipText(rq.getResource("rangeLevelField.toolTipText")); // NOI18N
            jTextField13.setToolTipText(rq.getResource("magicLevelField.toolTipText")); // NOI18N
            jTextField14.setToolTipText(rq.getResource("attackSpeedField.toolTipText")); // NOI18N
            jTextField15.setToolTipText(rq.getResource("startGraphicsField.toolTipText")); // NOI18N
            jTextField16.setToolTipText(rq.getResource("projectileIdField.toolTipText")); // NOI18N
            jTextField17.setToolTipText(rq.getResource("endGraphicsField.toolTipText")); // NOI18N
            String[] fontSettings = rq.getResource("jLabel1.font").split("-");
            jLabel1.setFont(new Font(fontSettings[0], Font.BOLD, 18)); // NOI18N
            jLabel1.setText(rq.getResource("jLabel1.text")); // NOI18N
            jLabel2.setText(rq.getResource("jLabel2.text")); // NOI18N
            jLabel3.setText(rq.getResource("jLabel3.text")); // NOI18N
            jLabel4.setText(rq.getResource("jLabel4.text")); // NOI18N
            jLabel5.setText(rq.getResource("jLabel5.text")); // NOI18N
            jLabel6.setText(rq.getResource("jLabel6.text")); // NOI18N
            jLabel7.setText(rq.getResource("jLabel7.text")); // NOI18N
            jLabel8.setText(rq.getResource("jLabel8.text")); // NOI18N
            jLabel9.setText(rq.getResource("jLabel9.text")); // NOI18N
            jLabel10.setText(rq.getResource("jLabel10.text")); // NOI18N
            jLabel11.setText(rq.getResource("jLabel11.text")); // NOI18N
            jLabel12.setText(rq.getResource("jLabel12.text")); // NOI18N
            jLabel13.setText(rq.getResource("jLabel13.text")); // NOI18N
            jLabel14.setText(rq.getResource("jLabel14.text")); // NOI18N
            jLabel15.setText(rq.getResource("jLabel15.text")); // NOI18N
            jLabel16.setText(rq.getResource("jLabel16.text")); // NOI18N
            jLabel17.setText(rq.getResource("jLabel17.text")); // NOI18N
            jLabel18.setText(rq.getResource("jLabel18.text")); // NOI18N
            jLabel2.setSize(6, 20);
            jLabel3.setSize(6, 20);
            jLabel4.setSize(6, 20);
            jLabel5.setSize(6, 20);
            jLabel6.setSize(6, 20);
            jLabel7.setSize(6, 20);
            jLabel8.setSize(6, 20);
            jLabel9.setSize(6, 20);
            jLabel10.setSize(6, 20);
            jLabel11.setSize(6, 20);
            jLabel12.setSize(6, 20);
            jLabel13.setSize(6, 20);
            jLabel14.setSize(6, 20);
            jLabel15.setSize(6, 20);
            jLabel16.setSize(6, 20);
            jLabel17.setSize(6, 20);
            jLabel18.setSize(6, 20);
            jCheckBox1.setText("Melee combat");
            jCheckBox2.setText("Range combat");
            jCheckBox3.setText("Magic combat");
            jCheckBox4.setText("Aggressive");
            jCheckBox5.setText("Poison immunity");
            jCheckBox1.setToolTipText("If the NPC has melee combat enabled.");
            jCheckBox2.setToolTipText("If the NPC has ranged combat enabled.");
            jCheckBox3.setToolTipText("If the NPC has magic combat enabled.");
            jCheckBox4.setToolTipText("If the NPC is aggressive.");
            jCheckBox5.setToolTipText("If the NPC is immune to poison.");
            jList1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jList1.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting()) {
                        showBonusPopup();
                    }
                }
            });
        } catch (Exception ex) {
            Logger.getLogger(NDEView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showBonusPopup() {
        final int index = jList1.getSelectedIndex();
        if (bonusBox == null) {
            JFrame mainFrame = NDEApp.getApplication().getMainFrame();
            bonusBox = new JDialog(mainFrame);
            bonusBox.setModal(true);
            bonusBox.setName("aboutBox"); // NOI18N
            bonusBox.setSize(200, 100);
            bonusBox.setResizable(false);
            bonusBox.setLocationRelativeTo(mainFrame);
            bonusField = new JTextField();
            bonusBox.add(bonusField);
        }
        bonusBox.setTitle("Edit " + jList1.getModel().getElementAt(index) + " bonus."); // NOI18N
        bonusField.setText("" + ndeUpdate.getBonuses()[index]);
        bonusField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateBonus(evt);
            }
        });
        bonusBox.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        bonusBox.setVisible(true);
    }

    private void updateBonus(java.awt.event.ActionEvent evt) {
        int index = jList1.getSelectedIndex();
        short bonus = ndeUpdate.getBonuses()[index];
        try {
            System.out.println("Editting bonus " + bonus + " to " + evt.getActionCommand());
            bonus = Short.parseShort(evt.getActionCommand());
        } catch (Exception e) {
        }
        ndeUpdate.getBonuses()[index] = bonus;
        bonusBox.dispose();
        ndeGraphics.drawStatistics(this);
    }
    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = NDEApp.getApplication().getMainFrame();
            aboutBox = new NDEAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        NDEApp.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();
        jTextField10 = new javax.swing.JTextField();
        jTextField11 = new javax.swing.JTextField();
        jTextField12 = new javax.swing.JTextField();
        jTextField13 = new javax.swing.JTextField();
        jTextField14 = new javax.swing.JTextField();
        jTextField15 = new javax.swing.JTextField();
        jTextField16 = new javax.swing.JTextField();
        jTextField17 = new javax.swing.JTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        jCheckBox5 = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        jFrame1 = new javax.swing.JFrame();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jDialog1 = new javax.swing.JDialog();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        jDialog2 = new javax.swing.JDialog();
		duplicateOpt = new javax.swing.JButton();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(nde.NDEApp.class).getContext().getResourceMap(NDEView.class);
        mainPanel.setBackground(resourceMap.getColor("mainPanel.background")); // NOI18N
        mainPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, resourceMap.getColor("mainPanel.border.highlightOuterColor"), resourceMap.getColor("mainPanel.border.highlightInnerColor"), resourceMap.getColor("mainPanel.border.shadowOuterColor"), resourceMap.getColor("mainPanel.border.shadowInnerColor"))); // NOI18N
        mainPanel.setForeground(resourceMap.getColor("mainPanel.foreground")); // NOI18N
        mainPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.CROSSHAIR_CURSOR));
        mainPanel.setMaximumSize(new java.awt.Dimension(851, 618));
        mainPanel.setMinimumSize(new java.awt.Dimension(727, 533));
        mainPanel.setName("mainPanel"); // NOI18N
        mainPanel.setPreferredSize(new java.awt.Dimension(727, 533));

        jLabel1.setBackground(resourceMap.getColor("jLabel1.background")); // NOI18N
        jLabel1.setFont(resourceMap.getFont("jLabel1.font")); // NOI18N
        jLabel1.setLabelFor(mainPanel);
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jTextField1.setText(resourceMap.getString("npcIdTextField.text")); // NOI18N
        jTextField1.setToolTipText(resourceMap.getString("npcIdTextField.toolTipText")); // NOI18N
        jTextField1.setName("npcIdTextField"); // NOI18N
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jTextField2.setText(resourceMap.getString("npcExamineTextField.text")); // NOI18N
        jTextField2.setToolTipText(resourceMap.getString("npcExamineTextField.toolTipText")); // NOI18N
        jTextField2.setName("npcExamineTextField"); // NOI18N
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField2KeyReleased(evt);
            }
        });

        jTextField3.setText(resourceMap.getString("combatLevelField.text")); // NOI18N
        jTextField3.setToolTipText(resourceMap.getString("combatLevelField.toolTipText")); // NOI18N
        jTextField3.setName("combatLevelField"); // NOI18N
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });
        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField3KeyReleased(evt);
            }
        });

        jTextField4.setText(resourceMap.getString("lifepointsField.text")); // NOI18N
        jTextField4.setToolTipText(resourceMap.getString("lifepointsField.toolTipText")); // NOI18N
        jTextField4.setName("lifepointsField"); // NOI18N
        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });
        jTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField4KeyReleased(evt);
            }
        });

        jTextField5.setText(resourceMap.getString("respawnField.text")); // NOI18N
        jTextField5.setToolTipText(resourceMap.getString("respawnField.toolTipText")); // NOI18N
        jTextField5.setName("respawnField"); // NOI18N
        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });
        jTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField5KeyReleased(evt);
            }
        });

        jTextField6.setText(resourceMap.getString("attackAnimationField.text")); // NOI18N
        jTextField6.setToolTipText(resourceMap.getString("attackAnimationField.toolTipText")); // NOI18N
        jTextField6.setName("attackAnimationField"); // NOI18N
        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });
        jTextField6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField6KeyReleased(evt);
            }
        });

        jTextField7.setText(resourceMap.getString("defenceAnimationField.text")); // NOI18N
        jTextField7.setToolTipText(resourceMap.getString("defenceAnimationField.toolTipText")); // NOI18N
        jTextField7.setName("defenceAnimationField"); // NOI18N
        jTextField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField7ActionPerformed(evt);
            }
        });
        jTextField7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField7KeyReleased(evt);
            }
        });

        jTextField8.setText(resourceMap.getString("deathAnimationField.text")); // NOI18N
        jTextField8.setToolTipText(resourceMap.getString("deathAnimationField.toolTipText")); // NOI18N
        jTextField8.setName("deathAnimationField"); // NOI18N
        jTextField8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField8ActionPerformed(evt);
            }
        });
        jTextField8.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField8KeyReleased(evt);
            }
        });

        jTextField9.setText(resourceMap.getString("attackLevelField.text")); // NOI18N
        jTextField9.setToolTipText(resourceMap.getString("attackLevelField.toolTipText")); // NOI18N
        jTextField9.setName("attackLevelField"); // NOI18N
        jTextField9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField9ActionPerformed(evt);
            }
        });
        jTextField9.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField9KeyReleased(evt);
            }
        });

        jTextField10.setText(resourceMap.getString("strengthLevelField.text")); // NOI18N
        jTextField10.setToolTipText(resourceMap.getString("strengthLevelField.toolTipText")); // NOI18N
        jTextField10.setName("strengthLevelField"); // NOI18N
        jTextField10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField10ActionPerformed(evt);
            }
        });
        jTextField10.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField10KeyReleased(evt);
            }
        });

        jTextField11.setText(resourceMap.getString("defenceLevelField.text")); // NOI18N
        jTextField11.setToolTipText(resourceMap.getString("defenceLevelField.toolTipText")); // NOI18N
        jTextField11.setName("defenceLevelField"); // NOI18N
        jTextField11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField11ActionPerformed(evt);
            }
        });
        jTextField11.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField11KeyReleased(evt);
            }
        });

        jTextField12.setText(resourceMap.getString("rangeLevelField.text")); // NOI18N
        jTextField12.setToolTipText(resourceMap.getString("rangeLevelField.toolTipText")); // NOI18N
        jTextField12.setName("rangeLevelField"); // NOI18N
        jTextField12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField12ActionPerformed(evt);
            }
        });
        jTextField12.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField12KeyReleased(evt);
            }
        });

        jTextField13.setText(resourceMap.getString("magicLevelField.text")); // NOI18N
        jTextField13.setToolTipText(resourceMap.getString("magicLevelField.toolTipText")); // NOI18N
        jTextField13.setName("magicLevelField"); // NOI18N
        jTextField13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField13ActionPerformed(evt);
            }
        });
        jTextField13.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField13KeyReleased(evt);
            }
        });

        jTextField14.setText(resourceMap.getString("attackSpeedField.text")); // NOI18N
        jTextField14.setToolTipText(resourceMap.getString("attackSpeedField.toolTipText")); // NOI18N
        jTextField14.setName("attackSpeedField"); // NOI18N
        jTextField14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField14ActionPerformed(evt);
            }
        });
        jTextField14.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField14KeyReleased(evt);
            }
        });

        jTextField15.setText(resourceMap.getString("startGraphicsField.text")); // NOI18N
        jTextField15.setToolTipText(resourceMap.getString("startGraphicsField.toolTipText")); // NOI18N
        jTextField15.setName("startGraphicsField"); // NOI18N
        jTextField15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField15ActionPerformed(evt);
            }
        });
        jTextField15.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField15KeyReleased(evt);
            }
        });

        jTextField16.setText(resourceMap.getString("projectileIdField.text")); // NOI18N
        jTextField16.setToolTipText(resourceMap.getString("projectileIdField.toolTipText")); // NOI18N
        jTextField16.setName("projectileIdField"); // NOI18N
        jTextField16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField16ActionPerformed(evt);
            }
        });
        jTextField16.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField16KeyReleased(evt);
            }
        });

        jTextField17.setText(resourceMap.getString("endGraphicsField.text")); // NOI18N
        jTextField17.setToolTipText(resourceMap.getString("endGraphicsField.toolTipText")); // NOI18N
        jTextField17.setName("endGraphicsField"); // NOI18N
        jTextField17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField17ActionPerformed(evt);
            }
        });
        jTextField17.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField17KeyReleased(evt);
            }
        });
		
		duplicateOpt.setText("Copy other npc definitions");
		duplicateOpt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                duplicateOptActionPerformed(evt);
            }
        });

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(nde.NDEApp.class).getContext().getActionMap(NDEView.class, this);
        jCheckBox1.setAction(actionMap.get("setUsingMelee")); // NOI18N
        jCheckBox1.setBackground(resourceMap.getColor("meleeCombatBox.background")); // NOI18N
        jCheckBox1.setText(resourceMap.getString("meleeCombatBox.text")); // NOI18N
        jCheckBox1.setToolTipText(resourceMap.getString("meleeCombatBox.toolTipText")); // NOI18N
        jCheckBox1.setName("meleeCombatBox"); // NOI18N

        jCheckBox2.setAction(actionMap.get("setUsingRange")); // NOI18N
        jCheckBox2.setBackground(resourceMap.getColor("rangeCombatBox.background")); // NOI18N
        jCheckBox2.setText(resourceMap.getString("rangeCombatBox.text")); // NOI18N
        jCheckBox2.setToolTipText(resourceMap.getString("rangeCombatBox.toolTipText")); // NOI18N
        jCheckBox2.setName("rangeCombatBox"); // NOI18N

        jCheckBox3.setAction(actionMap.get("setUsingMagic")); // NOI18N
        jCheckBox3.setBackground(resourceMap.getColor("magicCombatBox.background")); // NOI18N
        jCheckBox3.setText(resourceMap.getString("magicCombatBox.text")); // NOI18N
        jCheckBox3.setToolTipText(resourceMap.getString("magicCombatBox.toolTipText")); // NOI18N
        jCheckBox3.setName("magicCombatBox"); // NOI18N

        jCheckBox4.setAction(actionMap.get("setAggressive")); // NOI18N
        jCheckBox4.setBackground(resourceMap.getColor("aggressiveBox.background")); // NOI18N
        jCheckBox4.setText(resourceMap.getString("aggressiveBox.text")); // NOI18N
        jCheckBox4.setToolTipText(resourceMap.getString("aggressiveBox.toolTipText")); // NOI18N
        jCheckBox4.setName("aggressiveBox"); // NOI18N

        jCheckBox5.setAction(actionMap.get("setPoisonImmunity")); // NOI18N
        jCheckBox5.setBackground(resourceMap.getColor("poisonImmunityBox.background")); // NOI18N
        jCheckBox5.setText(resourceMap.getString("poisonImmunityBox.text")); // NOI18N
        jCheckBox5.setToolTipText(resourceMap.getString("poisonImmunityBox.toolTipText")); // NOI18N
        jCheckBox5.setName("poisonImmunityBox"); // NOI18N

        jLabel2.setLabelFor(jTextField1);
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel3.setLabelFor(jTextField2);
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jLabel4.setLabelFor(jTextField3);
        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jLabel5.setLabelFor(jTextField4);
        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        jLabel6.setLabelFor(jTextField5);
        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        jLabel10.setText(resourceMap.getString("jLabel10.text")); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N

        jLabel11.setText(resourceMap.getString("jLabel11.text")); // NOI18N
        jLabel11.setName("jLabel11"); // NOI18N

        jLabel12.setText(resourceMap.getString("jLabel12.text")); // NOI18N
        jLabel12.setName("jLabel12"); // NOI18N

        jLabel13.setText(resourceMap.getString("jLabel13.text")); // NOI18N
        jLabel13.setName("jLabel13"); // NOI18N

        jLabel14.setText(resourceMap.getString("jLabel14.text")); // NOI18N
        jLabel14.setName("jLabel14"); // NOI18N

        jLabel15.setText(resourceMap.getString("jLabel15.text")); // NOI18N
        jLabel15.setName("jLabel15"); // NOI18N

        jLabel16.setText(resourceMap.getString("jLabel16.text")); // NOI18N
        jLabel16.setName("jLabel16"); // NOI18N

        jLabel17.setText(resourceMap.getString("jLabel17.text")); // NOI18N
        jLabel17.setName("jLabel17"); // NOI18N

        jLabel18.setText(resourceMap.getString("jLabel18.text")); // NOI18N
        jLabel18.setName("jLabel18"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Stab attack", "Slash attack", "Crush attack", "Magic attack", "Range attack", "Stab defence", "Slash defence", "Crush defence", "Magic defence", "Summoning defence", "Range defence", "Strength bonus", "Ranged strength", "Magic bonus" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList1.setName("jList1"); // NOI18N
        jScrollPane1.setViewportView(jList1);

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setRows(5);
        jTextArea1.setText(resourceMap.getString("jTextArea1.text")); // NOI18N
        jTextArea1.setName("jTextArea1"); // NOI18N
        jScrollPane2.setViewportView(jTextArea1);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 534, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(32, 32, 32))
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(159, 159, 159)))
                        .addGap(416, 416, 416)))
                .addGap(264, 264, 264))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel8)
                    .addComponent(jLabel15)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5)
                    .addComponent(jLabel7)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextField17, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                    .addComponent(jTextField16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                    .addComponent(jTextField15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                    .addComponent(jTextField13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                    .addComponent(jTextField12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                    .addComponent(jTextField11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                    .addComponent(jTextField10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                    .addComponent(jTextField9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                    .addComponent(jTextField8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                    .addComponent(jTextField6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                    .addComponent(jTextField14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                    .addComponent(jTextField7, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox3)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox2)
                            .addComponent(jCheckBox1))
                        .addGap(18, 18, 18)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox4)
                            .addComponent(jCheckBox5)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(duplicateOpt, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(243, 243, 243))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(6, 6, 6)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(duplicateOpt)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox1)
                    .addComponent(jCheckBox4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox2)
                    .addComponent(jCheckBox5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel18)
                            .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, 0, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        menuBar.setBackground(resourceMap.getColor("menuBar.background")); // NOI18N
        menuBar.setCursor(new java.awt.Cursor(java.awt.Cursor.CROSSHAIR_CURSOR));
        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setBackground(resourceMap.getColor("fileMenu.background")); // NOI18N
        fileMenu.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setBackground(resourceMap.getColor("jMenuItem1.background")); // NOI18N
        jMenuItem1.setText(resourceMap.getString("jMenuItem1.text")); // NOI18N
        jMenuItem1.setName("jMenuItem1"); // NOI18N
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        fileMenu.add(jMenuItem1);

        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setBackground(resourceMap.getColor("exitMenuItem.background")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setBackground(resourceMap.getColor("helpMenu.background")); // NOI18N
        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setBackground(resourceMap.getColor("aboutMenuItem.background")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        jFrame1.setName("jFrame1"); // NOI18N

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        jPopupMenu1.setName("jPopupMenu1"); // NOI18N

        jDialog1.setName("jDialog1"); // NOI18N

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        jPopupMenu2.setName("jPopupMenu2"); // NOI18N

        jDialog2.setName("jDialog2"); // NOI18N

        javax.swing.GroupLayout jDialog2Layout = new javax.swing.GroupLayout(jDialog2.getContentPane());
        jDialog2.getContentPane().setLayout(jDialog2Layout);
        jDialog2Layout.setHorizontalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog2Layout.setVerticalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        System.out.println("Action performed: " + evt.getActionCommand());
        int npcId = ndeUpdate.getNpcId();
        try {
            npcId = Integer.parseInt(evt.getActionCommand());
        } catch (Exception e) {
        }
        ndeUpdate.update(this, npcId);
        ndeGraphics.drawStatistics(this);
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        ndeUpdate.setExamine(evt.getActionCommand());
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        ndeUpdate.saveDefinitions();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        int combatLevel = ndeUpdate.getCombatLevel();
        try {
            combatLevel = Integer.parseInt(evt.getActionCommand());
        } catch (Exception e) {
        }
        ndeUpdate.setCombatLevel((short) combatLevel);
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        int lifepoints = ndeUpdate.getLifepoints();
        try {
            lifepoints = Integer.parseInt(evt.getActionCommand());
        } catch (Exception e) {
        }
        ndeUpdate.setLifepoints((short) lifepoints);
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        byte respawn = ndeUpdate.getRespawn();
        try {
            respawn = Byte.parseByte(evt.getActionCommand());
        } catch (Exception e) {
        }
        ndeUpdate.setRespawn(respawn);
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        short attackAnimation = ndeUpdate.getAttackAnimation();
        try {
            attackAnimation = Short.parseShort(evt.getActionCommand());
        } catch (Exception e) {
        }
        ndeUpdate.setAttackAnimation(attackAnimation);
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void jTextField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField7ActionPerformed
        short animation = ndeUpdate.getDefenceAnimation();
        try {
            animation = Short.parseShort(evt.getActionCommand());
        } catch (Exception e) {
        }
        ndeUpdate.setDefenceAnimation(animation);
    }//GEN-LAST:event_jTextField7ActionPerformed

	public void duplicateOptActionPerformed(ActionEvent e) {
		int npcId = Integer.parseInt(JOptionPane.showInputDialog("Enter the NPC ID to copy!"));
        File file = new File("./data/NPCs/NPCDefinition" + npcId + ".xml");
        if (!file.exists()) {
            JOptionPane.showMessageDialog(getMainFrame(), "The NPC was not found!", "NPC not found!", 1);
        } else {
            XMLParsing.load(getNdeUpdate(), file);
            getNdeUpdate().updateTextFields(this);//updateCheckBoxes
            getNdeUpdate().updateCheckBoxes(this);
            System.out.println("Loaded file " + file.getName() + "!");
        }
	
	}
	
    private void jTextField8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField8ActionPerformed
        short animation = ndeUpdate.getDeathAnimation();
        try {
            animation = Short.parseShort(evt.getActionCommand());
        } catch (Exception e) {
        }
        ndeUpdate.setDeathAnimation(animation);
    }//GEN-LAST:event_jTextField8ActionPerformed

    private void jTextField9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField9ActionPerformed
        short level = ndeUpdate.getAttackLevel();
        try {
            level = Short.parseShort(evt.getActionCommand());
        } catch (Exception e) {
        }
        ndeUpdate.setAttackLevel(level);
        ndeGraphics.drawStatistics(this);
    }//GEN-LAST:event_jTextField9ActionPerformed

    private void jTextField10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField10ActionPerformed
        short level = ndeUpdate.getStrengthLevel();
        try {
            level = Short.parseShort(evt.getActionCommand());
        } catch (Exception e) {
        }
        ndeUpdate.setStrengthLevel(level);
        ndeGraphics.drawStatistics(this);
    }//GEN-LAST:event_jTextField10ActionPerformed

    private void jTextField11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField11ActionPerformed
        short level = ndeUpdate.getDefenceLevel();
        try {
            level = Short.parseShort(evt.getActionCommand());
        } catch (Exception e) {
        }
        ndeUpdate.setDefenceLevel(level);
        ndeGraphics.drawStatistics(this);
    }//GEN-LAST:event_jTextField11ActionPerformed

    private void jTextField12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField12ActionPerformed
        short level = ndeUpdate.getRangeLevel();
        try {
            level = Short.parseShort(evt.getActionCommand());
        } catch (Exception e) {
        }
        ndeUpdate.setRangeLevel(level);
        ndeGraphics.drawStatistics(this);
    }//GEN-LAST:event_jTextField12ActionPerformed

    private void jTextField13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField13ActionPerformed
        short level = ndeUpdate.getMagicLevel();
        try {
            level = Short.parseShort(evt.getActionCommand());
        } catch (Exception e) {
        }
        ndeUpdate.setMagicLevel(level);
        ndeGraphics.drawStatistics(this);
    }//GEN-LAST:event_jTextField13ActionPerformed

    private void jTextField14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField14ActionPerformed
        byte speed = ndeUpdate.getAttackSpeed();
        try {
            speed = Byte.parseByte(evt.getActionCommand());
        } catch (Exception e) {
        }
        ndeUpdate.setAttackSpeed(speed);
    }//GEN-LAST:event_jTextField14ActionPerformed

    private void jTextField15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField15ActionPerformed
        short startGFX = ndeUpdate.getStartGraphics();
        try {
            startGFX = Short.parseShort(evt.getActionCommand());
        } catch (Exception e) {
        }
        ndeUpdate.setStartGraphics(startGFX);
    }//GEN-LAST:event_jTextField15ActionPerformed

    private void jTextField16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField16ActionPerformed
        short projectile = ndeUpdate.getProjectileId();
        try {
            projectile = Short.parseShort(evt.getActionCommand());
        } catch (Exception e) {
        }
        ndeUpdate.setProjectileId(projectile);
    }//GEN-LAST:event_jTextField16ActionPerformed

    private void jTextField17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField17ActionPerformed
        short endGFX = ndeUpdate.getEndGraphics();
        try {
            endGFX = Short.parseShort(evt.getActionCommand());
        } catch (Exception e) {
        }
        ndeUpdate.setEndGraphics(endGFX);
    }//GEN-LAST:event_jTextField17ActionPerformed

    private void jTextField10KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField10KeyReleased
        jTextField10ActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, jTextField10.getText()));
    }//GEN-LAST:event_jTextField10KeyReleased

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        if (!jTextField1.getText().equals("")) {
            jTextField1ActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, jTextField1.getText()));
        }
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased
        jTextField2ActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, jTextField2.getText()));
    }//GEN-LAST:event_jTextField2KeyReleased

    private void jTextField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyReleased
        if (!jTextField3.getText().equals("")) {
            jTextField3ActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, jTextField3.getText()));
        }
    }//GEN-LAST:event_jTextField3KeyReleased

    private void jTextField4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyReleased
       if (!jTextField4.getText().equals("")) {
            jTextField4ActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, jTextField4.getText()));
        }
    }//GEN-LAST:event_jTextField4KeyReleased

    private void jTextField5KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyReleased
        if (!jTextField5.getText().equals("")) {
            jTextField5ActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, jTextField5.getText()));
        }
    }//GEN-LAST:event_jTextField5KeyReleased

    private void jTextField6KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField6KeyReleased
        if (!jTextField6.getText().equals("")) {
            jTextField6ActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, jTextField6.getText()));
        }
    }//GEN-LAST:event_jTextField6KeyReleased

    private void jTextField7KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField7KeyReleased
        if (!jTextField7.getText().equals("")) {
            jTextField7ActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, jTextField7.getText()));
        }
    }//GEN-LAST:event_jTextField7KeyReleased

    private void jTextField8KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField8KeyReleased
        if (!jTextField8.getText().equals("")) {
            jTextField8ActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, jTextField8.getText()));
        }
    }//GEN-LAST:event_jTextField8KeyReleased

    private void jTextField9KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField9KeyReleased
        if (!jTextField9.getText().equals("")) {
            jTextField9ActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, jTextField9.getText()));
        }
    }//GEN-LAST:event_jTextField9KeyReleased

    private void jTextField11KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField11KeyReleased
        if (!jTextField11.getText().equals("")) {
            jTextField11ActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, jTextField11.getText()));
        }
    }//GEN-LAST:event_jTextField11KeyReleased

    private void jTextField12KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField12KeyReleased
        if (!jTextField12.getText().equals("")) {
            jTextField12ActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, jTextField12.getText()));
        }
    }//GEN-LAST:event_jTextField12KeyReleased

    private void jTextField13KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField13KeyReleased
        if (!jTextField13.getText().equals("")) {
            jTextField13ActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, jTextField13.getText()));
        }
    }//GEN-LAST:event_jTextField13KeyReleased

    private void jTextField14KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField14KeyReleased
        if (!jTextField14.getText().equals("")) {
            jTextField14ActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, jTextField14.getText()));
        }
    }//GEN-LAST:event_jTextField14KeyReleased

    private void jTextField15KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField15KeyReleased
        if (!jTextField15.getText().equals("")) {
            jTextField15ActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, jTextField15.getText()));
        }
    }//GEN-LAST:event_jTextField15KeyReleased

    private void jTextField16KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField16KeyReleased
        if (!jTextField16.getText().equals("")) {
            jTextField16ActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, jTextField16.getText()));
        }
    }//GEN-LAST:event_jTextField16KeyReleased

    private void jTextField17KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField17KeyReleased
       if (!jTextField17.getText().equals("")) {
            jTextField17ActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, jTextField17.getText()));
        }
    }//GEN-LAST:event_jTextField17KeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JDialog jDialog2;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jList1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
	private javax.swing.JButton duplicateOpt;
    // End of variables declaration//GEN-END:variables
    private static final NDEUpdate ndeUpdate = new NDEUpdate();
    private NDEGraphicalUpdate ndeGraphics = new NDEGraphicalUpdate();
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private JDialog aboutBox;
    /**
     * The bonus popup box instance.
     */
    private JDialog bonusBox;

    /**
     * The bonus text field instance.
     */
    private JTextField bonusField;

    public static NDEUpdate getNdeUpdate() {
        return ndeUpdate;
    }

    @Action
    public void setUsingMelee() {
        ndeUpdate.setUsingMelee(!ndeUpdate.isUsingMelee());
        ndeUpdate.updateCheckBoxes(this);
    }

    @Action
    public void setUsingRange() {
        ndeUpdate.setUsingRange(!ndeUpdate.isUsingRange());
        ndeUpdate.updateCheckBoxes(this);
    }

    @Action
    public void setUsingMagic() {
        ndeUpdate.setUsingMagic(!ndeUpdate.isUsingMagic());
        ndeUpdate.updateCheckBoxes(this);
    }

    @Action
    public void setAggressive() {
        ndeUpdate.setAggressive(!ndeUpdate.isAggressive());
        ndeUpdate.updateCheckBoxes(this);
    }

    @Action
    public void setPoisonImmunity() {
        ndeUpdate.setPoisonImmune(!ndeUpdate.isPoisonImmune());
        ndeUpdate.updateCheckBoxes(this);
    }

    public static NDEView getInstance() {
        return INSTANCE;
    }

    public javax.swing.JTextField[] getTextFields() {
        return new javax.swing.JTextField[]{
                    jTextField1, jTextField2, jTextField3, jTextField4,
                    jTextField5, jTextField6, jTextField7, jTextField8,
                    jTextField9, jTextField10, jTextField11, jTextField12,
                    jTextField13, jTextField14, jTextField15, jTextField16,
                    jTextField17
                };
    }

    public javax.swing.JCheckBox[] getCheckBoxes() {
        return new javax.swing.JCheckBox[]{jCheckBox1, jCheckBox2, jCheckBox3, jCheckBox4, jCheckBox5};
    }

    public JFrame getMainFrame() {
        return jFrame1;
    }
    public JTextArea getTextArea() {
        return jTextArea1;
    }
}
