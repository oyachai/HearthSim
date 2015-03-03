package com.hearthsim.gui;

import com.hearthsim.player.playercontroller.BruteForceSearchAI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class HSSimulationSettingsFrame extends JDialog {
    private static final long serialVersionUID = 1L;
    private static final Color BACKGROUND_COLOR = new Color(64, 64, 64);
    private static final Color SUCCESS_BUTTON_COLOR = new Color(67, 172, 106);


    private JPanel contentPane;
    private HSSimulation simulation_;
    private JTextField fldNumSims;
    private JTextField fldNumThreads;
    private JTextField fldSimName;

    private JCheckBox flag_useFastPlacement_p0;
    private JCheckBox flag_useFastPlacement_p1;

    private JCheckBox flag_useDuplicateNodePruning_p0;
    private JCheckBox flag_useDuplicateNodePruning_p1;

    /**
     * Create the frame.
     */
    public HSSimulationSettingsFrame(JFrame parent, HSSimulation simulation) {
        super(parent, true);
        simulation_ = simulation;

        setBackground(BACKGROUND_COLOR);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(200, 200, 840, 560);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(BACKGROUND_COLOR);
        setContentPane(contentPane);
        SpringLayout sl_contentPane = new SpringLayout();
        contentPane.setLayout(sl_contentPane);

        JPanel bottom_panel = new JPanel();
        bottom_panel.setBorder(new MatteBorder(1, 0, 0, 0, Color.GRAY));
        bottom_panel.setBackground(BACKGROUND_COLOR);
        sl_contentPane.putConstraint(SpringLayout.WEST, bottom_panel, 0, SpringLayout.WEST, contentPane);
        sl_contentPane.putConstraint(SpringLayout.SOUTH, bottom_panel, 0, SpringLayout.SOUTH, contentPane);
        sl_contentPane.putConstraint(SpringLayout.EAST, bottom_panel, 0, SpringLayout.EAST, contentPane);
        contentPane.add(bottom_panel);

        JPanel main_panel = new JPanel();
        main_panel.setBackground(BACKGROUND_COLOR);
        sl_contentPane.putConstraint(SpringLayout.SOUTH, main_panel, -40, SpringLayout.SOUTH, contentPane);
        sl_contentPane.putConstraint(SpringLayout.NORTH, bottom_panel, 0, SpringLayout.SOUTH, main_panel);
        bottom_panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JButton btnSettingsDone = new HSButton("Done");
        btnSettingsDone.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                HSSimulationSettingsFrame.this.setVisible(false);
            }
        });
        btnSettingsDone.setForeground(Color.WHITE);
        btnSettingsDone.setBackground(SUCCESS_BUTTON_COLOR);
        bottom_panel.add(btnSettingsDone);
        sl_contentPane.putConstraint(SpringLayout.NORTH, main_panel, 0, SpringLayout.NORTH, contentPane);
        sl_contentPane.putConstraint(SpringLayout.WEST, main_panel, 0, SpringLayout.WEST, contentPane);
        sl_contentPane.putConstraint(SpringLayout.EAST, main_panel, 0, SpringLayout.EAST, contentPane);
        contentPane.add(main_panel);
        SpringLayout sl_main_panel = new SpringLayout();
        main_panel.setLayout(sl_main_panel);

        JPanel settings_P0 = new JPanel();
        sl_main_panel.putConstraint(SpringLayout.EAST, settings_P0, 240, SpringLayout.WEST, main_panel);
        settings_P0.setBorder(new MatteBorder(0, 0, 0, 1, Color.GRAY));
        sl_main_panel.putConstraint(SpringLayout.NORTH, settings_P0, 0, SpringLayout.NORTH, main_panel);
        sl_main_panel.putConstraint(SpringLayout.WEST, settings_P0, 0, SpringLayout.WEST, main_panel);
        sl_main_panel.putConstraint(SpringLayout.SOUTH, settings_P0, 0, SpringLayout.SOUTH, main_panel);
        sl_contentPane.putConstraint(SpringLayout.NORTH, settings_P0, 0, SpringLayout.NORTH, main_panel);
        sl_contentPane.putConstraint(SpringLayout.WEST, settings_P0, 0, SpringLayout.WEST, main_panel);
        sl_contentPane.putConstraint(SpringLayout.SOUTH, settings_P0, 0, SpringLayout.SOUTH, main_panel);
        sl_contentPane.putConstraint(SpringLayout.EAST, settings_P0, 260, SpringLayout.WEST, main_panel);
        main_panel.add(settings_P0);
        settings_P0.setBackground(BACKGROUND_COLOR);
        SpringLayout sl_settings_P0 = new SpringLayout();
        settings_P0.setLayout(sl_settings_P0);

        JPanel settings_P0_title = new JPanel();
        settings_P0_title.setBackground(BACKGROUND_COLOR);
        sl_settings_P0.putConstraint(SpringLayout.NORTH, settings_P0_title, 0, SpringLayout.NORTH, settings_P0);
        sl_settings_P0.putConstraint(SpringLayout.WEST, settings_P0_title, 0, SpringLayout.WEST, settings_P0);
        sl_settings_P0.putConstraint(SpringLayout.SOUTH, settings_P0_title, 40, SpringLayout.NORTH, settings_P0);
        sl_settings_P0.putConstraint(SpringLayout.EAST, settings_P0_title, 0, SpringLayout.EAST, settings_P0);
        settings_P0.add(settings_P0_title);

        JLabel lblNewLabel = new JLabel("Player0");
        lblNewLabel.setFont(new Font("Helvetica Neue", Font.PLAIN, 20));
        lblNewLabel.setForeground(Color.WHITE);
        settings_P0_title.add(lblNewLabel);

        JPanel settings_P0_main = new JPanel();
        settings_P0_main.setBackground(BACKGROUND_COLOR);
        sl_settings_P0.putConstraint(SpringLayout.NORTH, settings_P0_main, 0, SpringLayout.SOUTH, settings_P0_title);
        sl_settings_P0.putConstraint(SpringLayout.WEST, settings_P0_main, 0, SpringLayout.WEST, settings_P0);
        sl_settings_P0.putConstraint(SpringLayout.SOUTH, settings_P0_main, 0, SpringLayout.SOUTH, settings_P0);
        sl_settings_P0.putConstraint(SpringLayout.EAST, settings_P0_main, 0, SpringLayout.EAST, settings_P0);
        settings_P0.add(settings_P0_main);
        GridBagLayout gbl_settings_P0_main = new GridBagLayout();
        gbl_settings_P0_main.rowHeights = new int[] {0, 28, 28, 28, 0};
        gbl_settings_P0_main.columnWeights = new double[]{0.0, 0.0};
        gbl_settings_P0_main.rowWeights = new double[]{0.0, Double.MIN_VALUE};
        settings_P0_main.setLayout(gbl_settings_P0_main);

        JLabel lblSettings_p0_0 = new JLabel("Fast Minion Placement");
        lblSettings_p0_0.setForeground(HSColors.TEXT_COLOR);
        GridBagConstraints gbc_lblSettings_p0_0 = new GridBagConstraints();
        gbc_lblSettings_p0_0.insets = new Insets(0, 0, 0, 5);
        gbc_lblSettings_p0_0.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblSettings_p0_0.gridx = 0;
        gbc_lblSettings_p0_0.gridy = 0;
        settings_P0_main.add(lblSettings_p0_0, gbc_lblSettings_p0_0);

        flag_useFastPlacement_p0 = new JCheckBox("");
        GridBagConstraints gbc_flag_useFastPlacement_p0 = new GridBagConstraints();
        gbc_flag_useFastPlacement_p0.gridx = 1;
        gbc_flag_useFastPlacement_p0.gridy = 0;
        flag_useFastPlacement_p0.setSelected(((BruteForceSearchAI)simulation_.getAI_p0()).getUseSparseBoardStateFactory());
        settings_P0_main.add(flag_useFastPlacement_p0, gbc_flag_useFastPlacement_p0);

        //----------------------------------------------------------------------------
        JLabel lblUseDuplicateNodePruning_p0_0 = new JLabel("Duplicate Node Pruning");
        lblUseDuplicateNodePruning_p0_0.setForeground(HSColors.TEXT_COLOR);
        GridBagConstraints gbc_lblUseDuplicateNodePruning_p0_0 = new GridBagConstraints();
        gbc_lblUseDuplicateNodePruning_p0_0.insets = new Insets(0, 0, 0, 5);
        gbc_lblUseDuplicateNodePruning_p0_0.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblUseDuplicateNodePruning_p0_0.gridx = 0;
        gbc_lblUseDuplicateNodePruning_p0_0.gridy = 1;
        settings_P0_main.add(lblUseDuplicateNodePruning_p0_0, gbc_lblUseDuplicateNodePruning_p0_0);

        flag_useDuplicateNodePruning_p0 = new JCheckBox("");
        GridBagConstraints gbc_flag_useDuplicateNodePruning_p0 = new GridBagConstraints();
        gbc_flag_useDuplicateNodePruning_p0.gridx = 1;
        gbc_flag_useDuplicateNodePruning_p0.gridy = 1;
        flag_useDuplicateNodePruning_p0.setSelected(((BruteForceSearchAI)simulation_.getAI_p1()).getUseDuplicateNodePruning());
        settings_P0_main.add(flag_useDuplicateNodePruning_p0, gbc_flag_useDuplicateNodePruning_p0);
        //----------------------------------------------------------------------------


        JPanel settings_P1 = new JPanel();
        settings_P1.setBorder(new MatteBorder(0, 1, 0, 0, Color.GRAY));
        sl_main_panel.putConstraint(SpringLayout.NORTH, settings_P1, 0, SpringLayout.NORTH, main_panel);
        sl_main_panel.putConstraint(SpringLayout.WEST, settings_P1, -240, SpringLayout.EAST, main_panel);
        sl_main_panel.putConstraint(SpringLayout.SOUTH, settings_P1, 0, SpringLayout.SOUTH, main_panel);
        sl_main_panel.putConstraint(SpringLayout.EAST, settings_P1, 0, SpringLayout.EAST, main_panel);
        sl_contentPane.putConstraint(SpringLayout.NORTH, settings_P1, 0, SpringLayout.NORTH, main_panel);
        sl_contentPane.putConstraint(SpringLayout.WEST, settings_P1, -260, SpringLayout.EAST, main_panel);
        sl_contentPane.putConstraint(SpringLayout.SOUTH, settings_P1, 0, SpringLayout.SOUTH, main_panel);
        sl_contentPane.putConstraint(SpringLayout.EAST, settings_P1, 0, SpringLayout.EAST, main_panel);
        main_panel.add(settings_P1);
        settings_P1.setBackground(BACKGROUND_COLOR);
        SpringLayout sl_settings_P1 = new SpringLayout();
        settings_P1.setLayout(sl_settings_P1);

        JPanel settings_P1_title = new JPanel();
        settings_P1_title.setBackground(BACKGROUND_COLOR);
        sl_settings_P1.putConstraint(SpringLayout.NORTH, settings_P1_title, 0, SpringLayout.NORTH, settings_P1);
        sl_settings_P1.putConstraint(SpringLayout.WEST, settings_P1_title, 0, SpringLayout.WEST, settings_P1);
        sl_settings_P1.putConstraint(SpringLayout.SOUTH, settings_P1_title, 40, SpringLayout.NORTH, settings_P1);
        sl_settings_P1.putConstraint(SpringLayout.EAST, settings_P1_title, 0, SpringLayout.EAST, settings_P1);
        settings_P1.add(settings_P1_title);

        JLabel lblNewLabel_1 = new JLabel("Player1");
        lblNewLabel_1.setFont(new Font("Helvetica Neue", Font.PLAIN, 20));
        lblNewLabel_1.setForeground(Color.WHITE);
        settings_P1_title.add(lblNewLabel_1);

        JPanel settings_P1_main = new JPanel();
        settings_P1_main.setBackground(BACKGROUND_COLOR);
        sl_settings_P1.putConstraint(SpringLayout.NORTH, settings_P1_main, 0, SpringLayout.SOUTH, settings_P1_title);
        sl_settings_P1.putConstraint(SpringLayout.WEST, settings_P1_main, 0, SpringLayout.WEST, settings_P1);
        sl_settings_P1.putConstraint(SpringLayout.SOUTH, settings_P1_main, 0, SpringLayout.SOUTH, settings_P1);
        sl_settings_P1.putConstraint(SpringLayout.EAST, settings_P1_main, 0, SpringLayout.EAST, settings_P1);
        settings_P1.add(settings_P1_main);
        GridBagLayout gbl_settings_P1_main = new GridBagLayout();
        gbl_settings_P1_main.rowHeights = new int[] {0, 28, 28, 28, 0};
        gbl_settings_P1_main.columnWeights = new double[]{0.0, 0.0};
        gbl_settings_P1_main.rowWeights = new double[]{0.0, Double.MIN_VALUE};
        settings_P1_main.setLayout(gbl_settings_P1_main);

        JLabel lblSettings_p1_0 = new JLabel("Fast Minion Placement");
        lblSettings_p1_0.setForeground(HSColors.TEXT_COLOR);
        GridBagConstraints gbc_lblSettings_p1_0 = new GridBagConstraints();
        gbc_lblSettings_p1_0.insets = new Insets(0, 0, 0, 5);
        gbc_lblSettings_p1_0.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblSettings_p1_0.gridx = 0;
        gbc_lblSettings_p1_0.gridy = 0;
        settings_P1_main.add(lblSettings_p1_0, gbc_lblSettings_p1_0);

        flag_useFastPlacement_p1 = new JCheckBox("");
        GridBagConstraints gbc_flag_useFastPlacement_p1 = new GridBagConstraints();
        gbc_flag_useFastPlacement_p1.gridx = 1;
        gbc_flag_useFastPlacement_p1.gridy = 0;
        flag_useFastPlacement_p1.setSelected(((BruteForceSearchAI)simulation_.getAI_p1()).getUseSparseBoardStateFactory());
        settings_P1_main.add(flag_useFastPlacement_p1, gbc_flag_useFastPlacement_p1);

        //----------------------------------------------------------------------------
        JLabel lblUseDuplicateNodePruning_p1_0 = new JLabel("Duplicate Node Pruning");
        lblUseDuplicateNodePruning_p1_0.setForeground(HSColors.TEXT_COLOR);
        GridBagConstraints gbc_lblUseDuplicateNodePruning_p1_0 = new GridBagConstraints();
        gbc_lblUseDuplicateNodePruning_p1_0.insets = new Insets(0, 0, 0, 5);
        gbc_lblUseDuplicateNodePruning_p1_0.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblUseDuplicateNodePruning_p1_0.gridx = 0;
        gbc_lblUseDuplicateNodePruning_p1_0.gridy = 1;
        settings_P1_main.add(lblUseDuplicateNodePruning_p1_0, gbc_lblUseDuplicateNodePruning_p1_0);

        flag_useDuplicateNodePruning_p1 = new JCheckBox("");
        GridBagConstraints gbc_flag_useDuplicateNodePruning_p1 = new GridBagConstraints();
        gbc_flag_useDuplicateNodePruning_p1.gridx = 1;
        gbc_flag_useDuplicateNodePruning_p1.gridy = 1;
        flag_useDuplicateNodePruning_p1.setSelected(((BruteForceSearchAI)simulation_.getAI_p1()).getUseDuplicateNodePruning());
        settings_P1_main.add(flag_useDuplicateNodePruning_p1, gbc_flag_useDuplicateNodePruning_p1);
        //----------------------------------------------------------------------------


        JPanel settings_sim = new JPanel();
        sl_main_panel.putConstraint(SpringLayout.NORTH, settings_sim, 0, SpringLayout.NORTH, main_panel);
        sl_main_panel.putConstraint(SpringLayout.WEST, settings_sim, 20, SpringLayout.EAST, settings_P0);
        sl_main_panel.putConstraint(SpringLayout.SOUTH, settings_sim, 0, SpringLayout.SOUTH, main_panel);
        sl_main_panel.putConstraint(SpringLayout.EAST, settings_sim, -20, SpringLayout.WEST, settings_P1);
        sl_contentPane.putConstraint(SpringLayout.NORTH, settings_sim, 5, SpringLayout.NORTH, main_panel);
        sl_contentPane.putConstraint(SpringLayout.WEST, settings_sim, 425, SpringLayout.WEST, main_panel);
        main_panel.add(settings_sim);
        sl_contentPane.putConstraint(SpringLayout.SOUTH, settings_sim, 0, SpringLayout.SOUTH, contentPane);
        SpringLayout sl_settings_sim = new SpringLayout();
        settings_sim.setLayout(sl_settings_sim);

        JPanel settings_general = new JPanel();
        sl_settings_sim.putConstraint(SpringLayout.WEST, settings_general, 0, SpringLayout.WEST, settings_sim);
        sl_settings_sim.putConstraint(SpringLayout.SOUTH, settings_general, 0, SpringLayout.SOUTH, settings_sim);
        sl_settings_sim.putConstraint(SpringLayout.EAST, settings_general, 0, SpringLayout.EAST, settings_sim);
        sl_contentPane.putConstraint(SpringLayout.NORTH, settings_general, 0, SpringLayout.NORTH, contentPane);

        sl_contentPane.putConstraint(SpringLayout.SOUTH, settings_general, 0, SpringLayout.SOUTH, contentPane);
        settings_sim.add(settings_general);
        settings_general.setBackground(BACKGROUND_COLOR);
        GridBagLayout gbl_settings_general = new GridBagLayout();
        gbl_settings_general.columnWidths = new int[] {160, 150, 0};
        gbl_settings_general.rowHeights = new int[]{28, 28, 28, 0};
        gbl_settings_general.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
        gbl_settings_general.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
        settings_general.setLayout(gbl_settings_general);

        JLabel lblSettings_general_0 = new JLabel("Simulation Name:");
        lblSettings_general_0.setForeground(Color.WHITE);
        GridBagConstraints gbc_lblSettings_general_0 = new GridBagConstraints();
        gbc_lblSettings_general_0.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblSettings_general_0.insets = new Insets(0, 0, 5, 5);
        gbc_lblSettings_general_0.gridx = 0;
        gbc_lblSettings_general_0.gridy = 0;
        settings_general.add(lblSettings_general_0, gbc_lblSettings_general_0);

        fldSimName = new JTextField();
        fldSimName.setText("HearthSim");
        GridBagConstraints gbc_fldSimName = new GridBagConstraints();
        gbc_fldSimName.anchor = GridBagConstraints.NORTH;
        gbc_fldSimName.fill = GridBagConstraints.HORIZONTAL;
        gbc_fldSimName.insets = new Insets(0, 0, 5, 0);
        gbc_fldSimName.gridx = 1;
        gbc_fldSimName.gridy = 0;
        settings_general.add(fldSimName, gbc_fldSimName);
        fldSimName.setColumns(10);

        JLabel lblSettings_general_1 = new JLabel("Number of Simulations:");
        lblSettings_general_1.setForeground(Color.WHITE);
        GridBagConstraints gbc_lblSettings_general_1 = new GridBagConstraints();
        gbc_lblSettings_general_1.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblSettings_general_1.insets = new Insets(0, 0, 5, 5);
        gbc_lblSettings_general_1.gridx = 0;
        gbc_lblSettings_general_1.gridy = 1;
        settings_general.add(lblSettings_general_1, gbc_lblSettings_general_1);

        fldNumSims = new JTextField();
        if (simulation_.getConfig().numSimulations_ > 0)
            fldNumSims.setText("" + simulation_.getConfig().numSimulations_);
        else
            fldNumSims.setText("10");
        GridBagConstraints gbc_fldNumSims = new GridBagConstraints();
        gbc_fldNumSims.anchor = GridBagConstraints.NORTH;
        gbc_fldNumSims.fill = GridBagConstraints.HORIZONTAL;
        gbc_fldNumSims.insets = new Insets(0, 0, 5, 0);
        gbc_fldNumSims.gridx = 1;
        gbc_fldNumSims.gridy = 1;
        settings_general.add(fldNumSims, gbc_fldNumSims);
        fldNumSims.setColumns(10);

        JLabel lblSettings_general_2 = new JLabel("Number of Threads:");
        lblSettings_general_2.setForeground(Color.WHITE);
        GridBagConstraints gbc_lblSettings_general_2 = new GridBagConstraints();
        gbc_lblSettings_general_2.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblSettings_general_2.insets = new Insets(0, 0, 0, 5);
        gbc_lblSettings_general_2.gridx = 0;
        gbc_lblSettings_general_2.gridy = 2;
        settings_general.add(lblSettings_general_2, gbc_lblSettings_general_2);

        fldNumThreads = new JTextField();
        if (simulation_.getConfig().numThreads_ > 0)
            fldNumThreads.setText("" + simulation_.getConfig().numThreads_);
        else
            fldNumThreads.setText("1");
        GridBagConstraints gbc_fldNumThreads = new GridBagConstraints();
        gbc_fldNumThreads.anchor = GridBagConstraints.NORTH;
        gbc_fldNumThreads.fill = GridBagConstraints.HORIZONTAL;
        gbc_fldNumThreads.gridx = 1;
        gbc_fldNumThreads.gridy = 2;
        settings_general.add(fldNumThreads, gbc_fldNumThreads);
        fldNumThreads.setColumns(10);

        JPanel settings_sim_title = new JPanel();
        settings_sim_title.setBackground(BACKGROUND_COLOR);
        sl_settings_sim.putConstraint(SpringLayout.NORTH, settings_general, 0, SpringLayout.SOUTH, settings_sim_title);
        sl_settings_sim.putConstraint(SpringLayout.NORTH, settings_sim_title, 0, SpringLayout.NORTH, settings_sim);
        sl_settings_sim.putConstraint(SpringLayout.WEST, settings_sim_title, 0, SpringLayout.WEST, settings_sim);
        sl_settings_sim.putConstraint(SpringLayout.SOUTH, settings_sim_title, 40, SpringLayout.NORTH, settings_sim);
        sl_settings_sim.putConstraint(SpringLayout.EAST, settings_sim_title, 0, SpringLayout.EAST, settings_sim);
        settings_sim.add(settings_sim_title);

        JLabel lblNewLabel_2 = new JLabel("General");
        lblNewLabel_2.setFont(new Font("Helvetica Neue", Font.PLAIN, 20));
        lblNewLabel_2.setForeground(Color.WHITE);
        settings_sim_title.add(lblNewLabel_2);
        sl_contentPane.putConstraint(SpringLayout.WEST, settings_general, 6, SpringLayout.EAST, settings_P0);
        sl_contentPane.putConstraint(SpringLayout.EAST, settings_sim, -6, SpringLayout.WEST, settings_P1);
        sl_contentPane.putConstraint(SpringLayout.EAST, settings_general, -6, SpringLayout.WEST, settings_P1);

        GridBagConstraints dummyConstraints = new GridBagConstraints();
        dummyConstraints.gridy = 2;
        dummyConstraints.weighty = 1.0;
        JPanel dummyPanel0 = new JPanel();
        JPanel dummyPanel1 = new JPanel();
        dummyPanel0.setOpaque(false);
        dummyPanel1.setOpaque(false);
        settings_P0_main.add(dummyPanel0, dummyConstraints);
        settings_P1_main.add(dummyPanel1, dummyConstraints);

        this.addWindowListeners();
    }

    private void addWindowListeners() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowDeactivated(WindowEvent e) {
                simulation_.getConfig().numSimulations_ = Integer.parseInt(fldNumSims.getText());
                simulation_.getConfig().numThreads_ = Integer.parseInt(fldNumThreads.getText());
                simulation_.getConfig().simName_ = fldSimName.getText();

                simulation_.setAI_p0(BruteForceSearchAI.buildStandardAI2());
                simulation_.setAI_p1(BruteForceSearchAI.buildStandardAI2());

                ((BruteForceSearchAI)simulation_.getAI_p0()).setUseSparseBoardStateFactory(flag_useFastPlacement_p0.isSelected());
                ((BruteForceSearchAI)simulation_.getAI_p1()).setUseSparseBoardStateFactory(flag_useFastPlacement_p1.isSelected());

                ((BruteForceSearchAI)simulation_.getAI_p0()).setUseDuplicateNodePruning(flag_useDuplicateNodePruning_p0.isSelected());
                ((BruteForceSearchAI)simulation_.getAI_p1()).setUseDuplicateNodePruning(flag_useDuplicateNodePruning_p1.isSelected());
            }
        });
    }
}
