package com.hearthsim.gui;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.ImplementedCardList;
import com.hearthsim.card.ImplementedCardList.ImplementedCard;
import com.hearthsim.event.HSSimulationEventListener;
import com.hearthsim.exception.HSInvalidCardException;
import com.hearthsim.exception.HSInvalidHeroException;
import com.hearthsim.io.DeckListFile;
import com.ptplot.Plot;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

public class HearthSim implements HSSimulationEventListener {

    private JFrame frame;
    private final JPanel ControlPane = new JPanel();

    private boolean isRunning_;

    private final HSMainFrameModel hsModel_;

    private JPanel middlePanel;
    private HSDeckCreatePanel deckCreatePanel_0;
    private HSDeckCreatePanel deckCreatePanel_1;

    private HSCardList deckList_0;
    private HSCardList deckList_1;

    private JLabel cardCount_0;
    private JLabel cardCount_1;

    private JFileChooser fileChooser_;

    private JLabel lblHero_0;
    private JLabel lblHero_1;

    private JLabel lblWin_0;
    private JLabel lblWinRate_0;
    private JLabel lblWinRane_frst_0;
    private JLabel lblConfNum_0;

    private JLabel lblWin_1;
    private JLabel lblWinRate_1;
    private JLabel lblWinRane_frst_1;
    private JLabel lblConfNum_1;

    private JButton btnRun;

    private JPanel plotCardPane;
    private Plot plot_aveMinions;
    private Plot plot_aveCards;
    private Plot plot_aveHealth;

    private static final ImplementedCardList IMPLEMENTED_CARD_LIST = new ImplementedCardList();

    private static final DecimalFormat pFormatter_ = new DecimalFormat("0.00");

    private Plot currentShownPlot_;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    HearthSim window = new HearthSim();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public HearthSim() {
        hsModel_ = new HSMainFrameModel(this);
        isRunning_ = false;
        initialize();
        hsModel_.getSimulation().addSimulationEventListener(this);
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.getContentPane().setBackground(HSColors.BACKGROUND_COLOR);
        frame.setBounds(100, 100, 960, 640);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SpringLayout springLayout = new SpringLayout();
        frame.getContentPane().setLayout(springLayout);

        JPanel Player0Panel = new JPanel();
        Player0Panel.setBorder(new MatteBorder(0, 0, 0, 1, Color.GRAY));
        Player0Panel.setOpaque(false);
        Player0Panel.setBackground(Color.DARK_GRAY);
        springLayout.putConstraint(SpringLayout.NORTH, Player0Panel, 0,
                SpringLayout.NORTH, frame.getContentPane());
        springLayout.putConstraint(SpringLayout.WEST, Player0Panel, 0,
                SpringLayout.WEST, frame.getContentPane());
        springLayout.putConstraint(SpringLayout.SOUTH, Player0Panel, 0,
                SpringLayout.SOUTH, frame.getContentPane());
        springLayout.putConstraint(SpringLayout.EAST, Player0Panel, 200,
                SpringLayout.WEST, frame.getContentPane());
        frame.getContentPane().add(Player0Panel);
        SpringLayout sl_Player0Panel = new SpringLayout();
        Player0Panel.setLayout(sl_Player0Panel);

        JPanel HeroPane_0 = new JPanel();
        sl_Player0Panel.putConstraint(SpringLayout.SOUTH, HeroPane_0, 50,
                SpringLayout.NORTH, Player0Panel);
        HeroPane_0.setOpaque(false);
        HeroPane_0.setBackground(Color.DARK_GRAY);
        sl_Player0Panel.putConstraint(SpringLayout.NORTH, HeroPane_0, 0,
                SpringLayout.NORTH, Player0Panel);
        sl_Player0Panel.putConstraint(SpringLayout.WEST, HeroPane_0, 0,
                SpringLayout.WEST, Player0Panel);
        sl_Player0Panel.putConstraint(SpringLayout.EAST, HeroPane_0, 0,
                SpringLayout.EAST, Player0Panel);
        springLayout.putConstraint(SpringLayout.NORTH, HeroPane_0, 0,
                SpringLayout.NORTH, Player0Panel);
        springLayout.putConstraint(SpringLayout.WEST, HeroPane_0, 0,
                SpringLayout.WEST, Player0Panel);
        springLayout.putConstraint(SpringLayout.SOUTH, HeroPane_0, 50,
                SpringLayout.NORTH, Player0Panel);
        springLayout.putConstraint(SpringLayout.EAST, HeroPane_0, 0,
                SpringLayout.EAST, Player0Panel);
        Player0Panel.add(HeroPane_0);

        JScrollPane DeckPane_0 = new JScrollPane();
        sl_Player0Panel.putConstraint(SpringLayout.WEST, DeckPane_0, 5,
                SpringLayout.WEST, Player0Panel);
        DeckPane_0
                .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        DeckPane_0
                .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        DeckPane_0.setOpaque(false);
        DeckPane_0.setBackground(Color.DARK_GRAY);
        DeckPane_0.setLayout(new ScrollPaneLayout());
        DeckPane_0.getViewport().setOpaque(false);
        DeckPane_0.setBorder(BorderFactory.createEmptyBorder());
        sl_Player0Panel.putConstraint(SpringLayout.NORTH, DeckPane_0, 0,
                SpringLayout.SOUTH, HeroPane_0);
        HeroPane_0.setLayout(null);

        lblHero_0 = new JLabel("None");
        lblHero_0.setHorizontalAlignment(SwingConstants.CENTER);
        lblHero_0.setBounds(0, 0, 200, 50);
        lblHero_0.setFont(new Font("Helvetica Neue", Font.PLAIN, 20));
        lblHero_0.setForeground(Color.WHITE);
        HeroPane_0.add(lblHero_0);

        JButton btnDeckCreate_0 = new HSDeckCreateButton();
        btnDeckCreate_0.setBounds(160, 18, 20, 15);
        btnDeckCreate_0.setBackground(HSColors.BACKGROUND_COLOR);
        btnDeckCreate_0.setForeground(Color.WHITE);
        HeroPane_0.add(btnDeckCreate_0);

        sl_Player0Panel.putConstraint(SpringLayout.EAST, DeckPane_0, 0,
                SpringLayout.EAST, Player0Panel);
        Player0Panel.add(DeckPane_0);

        JPanel ControlPane_0 = new JPanel();
        sl_Player0Panel.putConstraint(SpringLayout.SOUTH, DeckPane_0, -20,
                SpringLayout.NORTH, ControlPane_0);
        ControlPane_0.setBackground(Color.DARK_GRAY);
        ControlPane_0.setOpaque(false);

        deckList_0 = new HSCardList();
        deckList_0.setForeground(Color.WHITE);
        deckList_0.setBackground(HSColors.BACKGROUND_COLOR);
        deckList_0.setOpaque(false);
        DeckPane_0.setViewportView(deckList_0);
        sl_Player0Panel.putConstraint(SpringLayout.NORTH, ControlPane_0, -40,
                SpringLayout.SOUTH, Player0Panel);
        sl_Player0Panel.putConstraint(SpringLayout.WEST, ControlPane_0, 0,
                SpringLayout.WEST, Player0Panel);
        sl_Player0Panel.putConstraint(SpringLayout.SOUTH, ControlPane_0, 0,
                SpringLayout.SOUTH, Player0Panel);
        sl_Player0Panel.putConstraint(SpringLayout.EAST, ControlPane_0, 0,
                SpringLayout.EAST, Player0Panel);
        Player0Panel.add(ControlPane_0);

        SpringLayout sl_ControlPane_0 = new SpringLayout();
        ControlPane_0.setLayout(sl_ControlPane_0);

        HSButton p0_load = new HSButton("Load...");
        p0_load.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (fileChooser_ == null) {
                    fileChooser_ = new JFileChooser();
                }
                FileFilter filter = new FileNameExtensionFilter("HSDeck file", new String[] {"hsdeck"});
                fileChooser_.addChoosableFileFilter(filter);
                fileChooser_.setFileFilter(filter);
                int retVal = fileChooser_.showOpenDialog(frame);
                if (retVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        DeckListFile deckList = new DeckListFile(fileChooser_
                                .getSelectedFile().toPath());
                        ((SortedListModel<ImplementedCard>) deckList_0
                                .getModel()).clear();
                        for (int indx = 0; indx < deckList.getDeck()
                                .getNumCards(); ++indx) {
                            Card card = deckList.getDeck().drawCard(indx);
                            ((SortedListModel<ImplementedCard>) deckList_0
                                    .getModel())
                                    .addElement(IMPLEMENTED_CARD_LIST
                                            .getCardForClass(card.getClass()));
                        }
                        hsModel_.getSimulation().setDeck_p0(deckList.getDeck());
                        hsModel_.getSimulation().setHero_p0(deckList.getHero());
                        lblHero_0.setText(deckList.getHero().getHeroClass());
                    } catch (HSInvalidCardException e1) {
                        JOptionPane.showMessageDialog(frame, e1.getMessage(),
                                "Error: Card not valid.", JOptionPane.ERROR_MESSAGE);
                    } catch (HSInvalidHeroException e1) {
                        JOptionPane.showMessageDialog(frame,
                                "Error: Hero not valid.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(frame,
                                "Error opening the deck file", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        p0_load.setForeground(Color.WHITE);
        p0_load.setBackground(HSColors.DEFAULT_BUTTON_COLOR);
        p0_load.setPreferredSize(new Dimension(80, 30));
        sl_ControlPane_0.putConstraint(SpringLayout.NORTH, p0_load, 5,
                SpringLayout.NORTH, ControlPane_0);
        sl_ControlPane_0.putConstraint(SpringLayout.WEST, p0_load, 5,
                SpringLayout.WEST, ControlPane_0);
        ControlPane_0.add(p0_load);

        JButton p0_save = new HSButton("Save...");
        p0_save.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (fileChooser_ == null) {
                    fileChooser_ = new JFileChooser();
                }
                FileFilter filter = new FileNameExtensionFilter("HSDeck file", new String[] {"hsdeck"});
                fileChooser_.addChoosableFileFilter(filter);
                fileChooser_.setFileFilter(filter);
                fileChooser_.setCurrentDirectory(new File("/home/me/Documents"));
                int retVal = fileChooser_.showSaveDialog(null);
                if (retVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        File saveFile = fileChooser_.getSelectedFile();
                        String p0_heroName = lblHero_0.getText();
                        Deck p0_Deck = deckList_0.getDeck();

                        DeckListFile deckList = new DeckListFile(p0_heroName, p0_Deck);
                        deckList.writeDeckListToFile(saveFile);
                    } catch (HSInvalidHeroException e1) {
                        JOptionPane.showMessageDialog(frame,
                                "Error: Hero not valid.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(frame,
                                "Error opening the deck file", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        p0_save.setForeground(Color.WHITE);
        p0_save.setBackground(HSColors.DEFAULT_BUTTON_COLOR);
        p0_save.setPreferredSize(new Dimension(80, 30));
        sl_ControlPane_0.putConstraint(SpringLayout.EAST, p0_save, -5,
                SpringLayout.EAST, ControlPane_0);
        sl_ControlPane_0.putConstraint(SpringLayout.NORTH, p0_save, 5,
                SpringLayout.NORTH, ControlPane_0);
        ControlPane_0.add(p0_save);

        middlePanel = new JPanel();
        middlePanel.setForeground(Color.WHITE);
        middlePanel.setBackground(HSColors.BACKGROUND_COLOR);
        springLayout.putConstraint(SpringLayout.NORTH, ControlPane, -40,
                SpringLayout.SOUTH, middlePanel);
        springLayout.putConstraint(SpringLayout.WEST, ControlPane, 0,
                SpringLayout.WEST, middlePanel);
        springLayout.putConstraint(SpringLayout.SOUTH, ControlPane, 0,
                SpringLayout.SOUTH, middlePanel);
        springLayout.putConstraint(SpringLayout.EAST, ControlPane, 0,
                SpringLayout.EAST, middlePanel);
        springLayout.putConstraint(SpringLayout.NORTH, middlePanel, 0,
                SpringLayout.NORTH, frame.getContentPane());
        springLayout.putConstraint(SpringLayout.WEST, middlePanel, 0,
                SpringLayout.EAST, Player0Panel);
        springLayout.putConstraint(SpringLayout.SOUTH, middlePanel, 0,
                SpringLayout.SOUTH, frame.getContentPane());
        frame.getContentPane().add(middlePanel);
        SpringLayout sl_middlePanel = new SpringLayout();
        sl_middlePanel.putConstraint(SpringLayout.NORTH, ControlPane, -40,
                SpringLayout.SOUTH, middlePanel);
        sl_middlePanel.putConstraint(SpringLayout.WEST, ControlPane, 0,
                SpringLayout.WEST, middlePanel);
        sl_middlePanel.putConstraint(SpringLayout.SOUTH, ControlPane, 0,
                SpringLayout.SOUTH, middlePanel);
        sl_middlePanel.putConstraint(SpringLayout.EAST, ControlPane, 0,
                SpringLayout.EAST, middlePanel);
        middlePanel.setLayout(sl_middlePanel);

        JPanel InfoPane = new JPanel();
        sl_middlePanel.putConstraint(SpringLayout.NORTH, InfoPane, 0,
                SpringLayout.NORTH, middlePanel);
        sl_middlePanel.putConstraint(SpringLayout.WEST, InfoPane, 0,
                SpringLayout.WEST, middlePanel);
        sl_middlePanel.putConstraint(SpringLayout.SOUTH, InfoPane, 240,
                SpringLayout.NORTH, middlePanel);
        sl_middlePanel.putConstraint(SpringLayout.EAST, InfoPane, 0,
                SpringLayout.EAST, middlePanel);
        springLayout.putConstraint(SpringLayout.NORTH, InfoPane, 0,
                SpringLayout.NORTH, middlePanel);
        springLayout.putConstraint(SpringLayout.WEST, InfoPane, 0,
                SpringLayout.WEST, middlePanel);
        springLayout.putConstraint(SpringLayout.SOUTH, InfoPane, 250,
                SpringLayout.NORTH, middlePanel);
        springLayout.putConstraint(SpringLayout.EAST, InfoPane, 0,
                SpringLayout.EAST, middlePanel);
        middlePanel.add(InfoPane);
        InfoPane.setOpaque(false);
        SpringLayout sl_InfoPane = new SpringLayout();
        InfoPane.setLayout(sl_InfoPane);

        JPanel Player0Info = new JPanel();
        sl_InfoPane.putConstraint(SpringLayout.WEST, Player0Info, 15,
                SpringLayout.WEST, InfoPane);
        sl_InfoPane.putConstraint(SpringLayout.SOUTH, Player0Info, 0,
                SpringLayout.SOUTH, InfoPane);
        sl_InfoPane.putConstraint(SpringLayout.EAST, Player0Info, 225,
                SpringLayout.WEST, InfoPane);
        Player0Info.setOpaque(false);
        sl_InfoPane.putConstraint(SpringLayout.NORTH, Player0Info, 5,
                SpringLayout.NORTH, InfoPane);
        InfoPane.add(Player0Info);

        JLabel lblLabel1_0 = new JLabel("P0 Wins");
        lblLabel1_0.setFont(new Font("Helvetica Neue", Font.BOLD, 16));
        lblLabel1_0.setForeground(Color.WHITE);
        Player0Info.add(lblLabel1_0);

        lblWin_0 = new JLabel("--");
        lblWin_0.setFont(new Font("Helvetica Neue", Font.PLAIN, 22));
        lblWin_0.setHorizontalAlignment(SwingConstants.CENTER);
        lblWin_0.setPreferredSize(new Dimension(200, 30));
        lblWin_0.setForeground(Color.WHITE);
        Player0Info.add(lblWin_0);

        Component rigidArea_1 = Box.createRigidArea(new Dimension(20, 10));
        rigidArea_1.setPreferredSize(new Dimension(200, 5));
        Player0Info.add(rigidArea_1);

        JLabel lblLabel2_0 = new JLabel("Win Rate");
        lblLabel2_0.setFont(new Font("Helvetica Neue", Font.BOLD, 14));
        lblLabel2_0.setBackground(HSColors.BACKGROUND_COLOR);
        lblLabel2_0.setForeground(HSColors.TEXT_COLOR);
        Player0Info.add(lblLabel2_0);

        lblWinRate_0 = new JLabel("--");
        lblWinRate_0.setFont(new Font("Helvetica Neue", Font.PLAIN, 22));
        lblWinRate_0.setHorizontalAlignment(SwingConstants.CENTER);
        lblWinRate_0.setHorizontalTextPosition(SwingConstants.CENTER);
        lblWinRate_0.setForeground(Color.WHITE);
        lblWinRate_0.setPreferredSize(new Dimension(200, 30));
        Player0Info.add(lblWinRate_0);

        lblWinRane_frst_0 = new JLabel("--");
        lblWinRane_frst_0.setPreferredSize(new Dimension(200, 30));
        lblWinRane_frst_0.setHorizontalTextPosition(SwingConstants.CENTER);
        lblWinRane_frst_0.setHorizontalAlignment(SwingConstants.CENTER);
        lblWinRane_frst_0.setForeground(HSColors.TEXT_COLOR);
        lblWinRane_frst_0.setFont(new Font("Helvetica Neue", Font.PLAIN, 12));
        Player0Info.add(lblWinRane_frst_0);

        Component rigidArea = Box.createRigidArea(new Dimension(20, 20));
        rigidArea.setPreferredSize(new Dimension(135, 10));
        Player0Info.add(rigidArea);

        JLabel lblConf_0 = new JLabel("95% Conf Range");
        lblConf_0.setForeground(Color.WHITE);
        lblConf_0.setFont(new Font("Helvetica Neue", Font.BOLD, 12));
        Player0Info.add(lblConf_0);

        lblConfNum_0 = new JLabel("--");
        lblConfNum_0.setHorizontalAlignment(SwingConstants.CENTER);
        lblConfNum_0.setPreferredSize(new Dimension(200, 30));
        lblConfNum_0.setFont(new Font("Helvetica Neue", Font.PLAIN, 16));
        lblConfNum_0.setForeground(Color.WHITE);
        Player0Info.add(lblConfNum_0);

        JPanel Player1Info = new JPanel();
        sl_InfoPane.putConstraint(SpringLayout.WEST, Player1Info, -225,
                SpringLayout.EAST, InfoPane);
        sl_InfoPane.putConstraint(SpringLayout.SOUTH, Player1Info, 0,
                SpringLayout.SOUTH, InfoPane);
        sl_InfoPane.putConstraint(SpringLayout.EAST, Player1Info, -15,
                SpringLayout.EAST, InfoPane);
        Player1Info.setOpaque(false);
        sl_InfoPane.putConstraint(SpringLayout.NORTH, Player1Info, 5,
                SpringLayout.NORTH, InfoPane);
        InfoPane.add(Player1Info);

        JLabel lblLabel1_1 = new JLabel("P1 Wins");
        lblLabel1_1.setFont(new Font("Helvetica Neue", Font.BOLD, 16));
        lblLabel1_1.setForeground(Color.WHITE);
        Player1Info.add(lblLabel1_1);

        lblWin_1 = new JLabel("--");
        lblWin_1.setFont(new Font("Helvetica Neue", Font.PLAIN, 22));
        lblWin_1.setHorizontalAlignment(SwingConstants.CENTER);
        lblWin_1.setPreferredSize(new Dimension(200, 30));
        lblWin_1.setForeground(Color.WHITE);
        Player1Info.add(lblWin_1);

        Component rigidArea_1_1 = Box.createRigidArea(new Dimension(20, 20));
        rigidArea_1_1.setPreferredSize(new Dimension(200, 5));
        Player1Info.add(rigidArea_1_1);

        JLabel lblLabel2_1 = new JLabel("Win Rate");
        lblLabel2_1.setFont(new Font("Helvetica Neue", Font.BOLD, 14));
        lblLabel2_1.setBackground(HSColors.BACKGROUND_COLOR);
        lblLabel2_1.setForeground(HSColors.TEXT_COLOR);
        Player1Info.add(lblLabel2_1);

        lblWinRate_1 = new JLabel("--");
        lblWinRate_1.setFont(new Font("Helvetica Neue", Font.PLAIN, 22));
        lblWinRate_1.setHorizontalAlignment(SwingConstants.CENTER);
        lblWinRate_1.setHorizontalTextPosition(SwingConstants.CENTER);
        lblWinRate_1.setForeground(Color.WHITE);
        lblWinRate_1.setPreferredSize(new Dimension(200, 30));
        Player1Info.add(lblWinRate_1);

        lblWinRane_frst_1 = new JLabel("--");
        lblWinRane_frst_1.setPreferredSize(new Dimension(200, 30));
        lblWinRane_frst_1.setHorizontalTextPosition(SwingConstants.CENTER);
        lblWinRane_frst_1.setHorizontalAlignment(SwingConstants.CENTER);
        lblWinRane_frst_1.setForeground(HSColors.TEXT_COLOR);
        lblWinRane_frst_1.setFont(new Font("Helvetica Neue", Font.PLAIN, 12));
        Player1Info.add(lblWinRane_frst_1);

        Component rigidArea_0_2 = Box.createRigidArea(new Dimension(20, 20));
        rigidArea_0_2.setPreferredSize(new Dimension(135, 10));
        Player1Info.add(rigidArea_0_2);

        JLabel lblConf_1 = new JLabel("95% Conf Range");
        lblConf_1.setForeground(Color.WHITE);
        lblConf_1.setFont(new Font("Helvetica Neue", Font.BOLD, 12));
        Player1Info.add(lblConf_1);

        lblConfNum_1 = new JLabel("--");
        lblConfNum_1.setHorizontalAlignment(SwingConstants.CENTER);
        lblConfNum_1.setPreferredSize(new Dimension(200, 30));
        lblConfNum_1.setFont(new Font("Helvetica Neue", Font.PLAIN, 16));
        lblConfNum_1.setForeground(Color.WHITE);
        Player1Info.add(lblConfNum_1);

        JPanel generalInfo = new JPanel();
        generalInfo.setForeground(Color.WHITE);
        generalInfo.setBackground(HSColors.BACKGROUND_COLOR);
        sl_InfoPane.putConstraint(SpringLayout.NORTH, generalInfo, 0,
                SpringLayout.NORTH, InfoPane);
        sl_InfoPane.putConstraint(SpringLayout.WEST, generalInfo, 0,
                SpringLayout.EAST, Player0Info);
        sl_InfoPane.putConstraint(SpringLayout.SOUTH, generalInfo, 0,
                SpringLayout.SOUTH, InfoPane);
        sl_InfoPane.putConstraint(SpringLayout.EAST, generalInfo, 0,
                SpringLayout.WEST, Player1Info);
        InfoPane.add(generalInfo);

        JPanel Player1Panel = new JPanel();
        springLayout.putConstraint(SpringLayout.EAST, middlePanel, 0,
                SpringLayout.WEST, Player1Panel);

        JPanel PlotPane = new JPanel();
        sl_middlePanel.putConstraint(SpringLayout.NORTH, PlotPane, 0,
                SpringLayout.SOUTH, InfoPane);
        sl_middlePanel.putConstraint(SpringLayout.WEST, PlotPane, 0,
                SpringLayout.WEST, middlePanel);
        sl_middlePanel.putConstraint(SpringLayout.SOUTH, PlotPane, 0,
                SpringLayout.NORTH, ControlPane);
        sl_middlePanel.putConstraint(SpringLayout.EAST, PlotPane, 0,
                SpringLayout.EAST, middlePanel);
        springLayout.putConstraint(SpringLayout.NORTH, PlotPane, 0,
                SpringLayout.SOUTH, InfoPane);
        springLayout.putConstraint(SpringLayout.WEST, PlotPane, 0,
                SpringLayout.WEST, middlePanel);
        springLayout.putConstraint(SpringLayout.SOUTH, PlotPane, 0,
                SpringLayout.NORTH, middlePanel);
        springLayout.putConstraint(SpringLayout.EAST, PlotPane, 0,
                SpringLayout.EAST, middlePanel);
        middlePanel.add(PlotPane);
        PlotPane.setBorder(null);
        PlotPane.setOpaque(false);
        SpringLayout sl_PlotPane = new SpringLayout();
        PlotPane.setLayout(sl_PlotPane);

        JPanel plotTabPane = new JPanel();
        sl_PlotPane.putConstraint(SpringLayout.SOUTH, plotTabPane, 20,
                SpringLayout.NORTH, PlotPane);
        FlowLayout flowLayout = (FlowLayout) plotTabPane.getLayout();
        flowLayout.setVgap(1);
        flowLayout.setHgap(1);
        plotTabPane.setBackground(HSColors.BACKGROUND_COLOR);
        sl_PlotPane.putConstraint(SpringLayout.NORTH, plotTabPane, 0,
                SpringLayout.NORTH, PlotPane);
        sl_PlotPane.putConstraint(SpringLayout.WEST, plotTabPane, 0,
                SpringLayout.WEST, PlotPane);
        sl_PlotPane.putConstraint(SpringLayout.EAST, plotTabPane, 0,
                SpringLayout.EAST, PlotPane);
        PlotPane.add(plotTabPane);

        plotCardPane = new JPanel();
        sl_PlotPane.putConstraint(SpringLayout.NORTH, plotCardPane, 0,
                SpringLayout.SOUTH, plotTabPane);

        HSTabButton tabAveMinions = new HSTabButton("Ave # Minions");
        tabAveMinions.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                CardLayout cl = (CardLayout) (plotCardPane.getLayout());
                currentShownPlot_ = plot_aveMinions;
                cl.show(plotCardPane, "plot_aveMinions");
                HearthSim.this.updatePlotPanel();
            }
        });
        tabAveMinions.setBorder(null);
        tabAveMinions.setFont(new Font("Helvetica Neue", Font.PLAIN, 10));
        tabAveMinions.setBackground(HSColors.WARNING_BUTTON_COLOR);
        tabAveMinions.setForeground(Color.WHITE);
        plotTabPane.add(tabAveMinions);

        HSTabButton tabAveCards = new HSTabButton("Ave # Cards");
        tabAveCards.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                CardLayout cl = (CardLayout) (plotCardPane.getLayout());
                currentShownPlot_ = plot_aveCards;
                cl.show(plotCardPane, "plot_aveCards");
                HearthSim.this.updatePlotPanel();
            }
        });
        tabAveCards.setBorder(null);
        tabAveCards.setFont(new Font("Helvetica Neue", Font.PLAIN, 10));
        tabAveCards.setBackground(HSColors.WARNING_BUTTON_COLOR);
        tabAveCards.setForeground(Color.WHITE);
        plotTabPane.add(tabAveCards);

        HSTabButton tabAveHealth = new HSTabButton("Ave Health");
        tabAveHealth.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                CardLayout cl = (CardLayout) (plotCardPane.getLayout());
                currentShownPlot_ = plot_aveHealth;
                cl.show(plotCardPane, "plot_aveHealth");
                HearthSim.this.updatePlotPanel();
            }
        });
        tabAveHealth.setBorder(null);
        tabAveHealth.setFont(new Font("Helvetica Neue", Font.PLAIN, 10));
        tabAveHealth.setBackground(HSColors.WARNING_BUTTON_COLOR);
        tabAveHealth.setForeground(HSColors.TEXT_COLOR);
        plotTabPane.add(tabAveHealth);

        sl_PlotPane.putConstraint(SpringLayout.WEST, plotCardPane, 0,
                SpringLayout.WEST, PlotPane);
        sl_PlotPane.putConstraint(SpringLayout.SOUTH, plotCardPane, 0,
                SpringLayout.SOUTH, PlotPane);
        sl_PlotPane.putConstraint(SpringLayout.EAST, plotCardPane, 0,
                SpringLayout.EAST, PlotPane);
        PlotPane.add(plotCardPane);
        plotCardPane.setLayout(new CardLayout(0, 0));

        plot_aveMinions = new Plot();
        FlowLayout flowLayout_1 = (FlowLayout) plot_aveMinions.getLayout();
        flowLayout_1.setVgap(0);
        plot_aveMinions.setYRange(0, 6.0);
        plot_aveMinions.setXRange(0, 30.0);
        plot_aveMinions.setTitle("Average Number of Minions");
        plot_aveMinions.addLegend(0, "Player0");
        plot_aveMinions.addLegend(1, "Player1");
        plot_aveMinions.setXLabel("Turn");
        plot_aveMinions.setBackground(HSColors.BACKGROUND_COLOR);
        plot_aveMinions.setForeground(Color.WHITE);
        plot_aveMinions.setGrid(false);
        plot_aveMinions.setLabelFont("Helvetica Neue");
        plotCardPane.add(plot_aveMinions, "plot_aveMinions");

        currentShownPlot_ = plot_aveMinions;

        plot_aveCards = new Plot();
        FlowLayout flowLayout_2 = (FlowLayout) plot_aveCards.getLayout();
        flowLayout_2.setVgap(0);
        plot_aveCards.setYRange(0, 6.0);
        plot_aveCards.setXRange(0, 30.0);
        plot_aveCards.setTitle("Average Number of Cards");
        plot_aveCards.addLegend(0, "Player0");
        plot_aveCards.addLegend(1, "Player1");
        plot_aveCards.setXLabel("Turn");
        plot_aveCards.setBackground(HSColors.BACKGROUND_COLOR);
        plot_aveCards.setForeground(Color.WHITE);
        plot_aveCards.setGrid(false);
        plot_aveCards.setLabelFont("Helvetica Neue");
        plotCardPane.add(plot_aveCards, "plot_aveCards");

        plot_aveHealth = new Plot();
        FlowLayout flowLayout_3 = (FlowLayout) plot_aveHealth.getLayout();
        flowLayout_3.setVgap(0);
        plot_aveHealth.setYRange(0, 40.0);
        plot_aveHealth.setXRange(0, 30.0);
        plot_aveHealth.setTitle("Average Hero Health");
        plot_aveHealth.addLegend(0, "Player0");
        plot_aveHealth.addLegend(1, "Player1");
        plot_aveHealth.setXLabel("Turn");
        plot_aveHealth.setBackground(HSColors.BACKGROUND_COLOR);
        plot_aveHealth.setForeground(Color.WHITE);
        plot_aveHealth.setGrid(false);
        plot_aveHealth.setLabelFont("Helvetica Neue");
        plotCardPane.add(plot_aveHealth, "plot_aveHealth");

        middlePanel.add(ControlPane);

        ControlPane.setOpaque(false);

        HSButton btnSetting = new HSButton("Settings");
        btnSetting.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                HSSimulationSettingsFrame settingsFrame = new HSSimulationSettingsFrame(
                        frame, hsModel_.getSimulation());
                settingsFrame.setVisible(true);
            }
        });
        btnSetting.setForeground(Color.WHITE);
        btnSetting.setBackground(HSColors.DEFAULT_BUTTON_COLOR);
        ControlPane.add(btnSetting);

        Component rigidArea_3 = Box.createRigidArea(new Dimension(20, 20));
        rigidArea_3.setPreferredSize(new Dimension(100, 20));
        ControlPane.add(rigidArea_3);

        HSButton btnReset = new HSButton("Reset");
        btnReset.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                hsModel_.resetSimulationResults();
                HearthSim.this.updatePlotPanel();
                HearthSim.this.updateInfoPanel();
            }
        });
        btnReset.setForeground(Color.WHITE);
        btnReset.setBackground(HSColors.DEFAULT_BUTTON_COLOR);
        ControlPane.add(btnReset);

        btnRun = new HSButton("Run");
        btnRun.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!isRunning_) {
                    if (hsModel_.getSimulation().getDeck_p0() == null) {
                        JOptionPane.showMessageDialog(frame,
                                "Player0 deck is missing", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (hsModel_.getSimulation().getDeck_p1() == null) {
                        JOptionPane.showMessageDialog(frame,
                                "Player1 deck is missing", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Runnable runner = new Runnable() {
                        @Override
                        public void run() {
                            hsModel_.runSimulation();
                        }
                    };
                    new Thread(runner).start();
                } else {
                    hsModel_.stopSimulation();
                }
            }
        });
        btnRun.setBackground(HSColors.SUCCESS_BUTTON_COLOR);
        btnRun.setForeground(Color.WHITE);
        ControlPane.add(btnRun);
        Player1Panel.setBorder(new MatteBorder(0, 1, 0, 0, Color.GRAY));
        Player1Panel.setOpaque(false);
        springLayout.putConstraint(SpringLayout.NORTH, Player1Panel, 0,
                SpringLayout.NORTH, frame.getContentPane());
        springLayout.putConstraint(SpringLayout.WEST, Player1Panel, -200,
                SpringLayout.EAST, frame.getContentPane());
        springLayout.putConstraint(SpringLayout.SOUTH, Player1Panel, 0,
                SpringLayout.SOUTH, frame.getContentPane());
        springLayout.putConstraint(SpringLayout.EAST, Player1Panel, 0,
                SpringLayout.EAST, frame.getContentPane());

        frame.getContentPane().add(Player1Panel);
        SpringLayout sl_Player1Panel = new SpringLayout();
        Player1Panel.setLayout(sl_Player1Panel);

        JPanel HeroPane_1 = new JPanel();
        sl_Player1Panel.putConstraint(SpringLayout.SOUTH, HeroPane_1, 50,
                SpringLayout.NORTH, Player1Panel);
        HeroPane_1.setOpaque(false);
        sl_Player1Panel.putConstraint(SpringLayout.NORTH, HeroPane_1, 0,
                SpringLayout.NORTH, Player1Panel);
        sl_Player1Panel.putConstraint(SpringLayout.WEST, HeroPane_1, 0,
                SpringLayout.WEST, Player1Panel);
        sl_Player1Panel.putConstraint(SpringLayout.EAST, HeroPane_1, 0,
                SpringLayout.EAST, Player1Panel);
        Player1Panel.add(HeroPane_1);

        JButton btnDeckCreate_1 = new HSDeckCreateButton();
        btnDeckCreate_1.setBounds(20, 18, 20, 15);
        btnDeckCreate_1.setBackground(HSColors.BACKGROUND_COLOR);
        btnDeckCreate_1.setForeground(Color.WHITE);
        HeroPane_1.add(btnDeckCreate_1);

        JScrollPane DeckPane_1 = new JScrollPane();
        sl_Player1Panel.putConstraint(SpringLayout.WEST, DeckPane_1, 5,
                SpringLayout.WEST, Player1Panel);
        DeckPane_1.setOpaque(false);
        DeckPane_1.setBackground(Color.DARK_GRAY);
        DeckPane_1.setLayout(new ScrollPaneLayout());
        DeckPane_1.getViewport().setOpaque(false);
        DeckPane_1.setBorder(BorderFactory.createEmptyBorder());
        sl_Player1Panel.putConstraint(SpringLayout.NORTH, DeckPane_1, 0,
                SpringLayout.SOUTH, HeroPane_1);
        HeroPane_1.setLayout(null);

        lblHero_1 = new JLabel("None");
        lblHero_1.setBounds(0, 0, 200, 50);
        lblHero_1.setHorizontalAlignment(SwingConstants.CENTER);
        lblHero_1.setForeground(Color.WHITE);
        lblHero_1.setFont(new Font("Helvetica Neue", Font.PLAIN, 20));
        HeroPane_1.add(lblHero_1);
        sl_Player1Panel.putConstraint(SpringLayout.EAST, DeckPane_1, 0,
                SpringLayout.EAST, Player1Panel);
        Player1Panel.add(DeckPane_1);

        JPanel ControlPane_1 = new JPanel();
        sl_Player1Panel.putConstraint(SpringLayout.SOUTH, DeckPane_1, -20,
                SpringLayout.NORTH, ControlPane_1);
        ControlPane_1.setOpaque(false);
        sl_Player1Panel.putConstraint(SpringLayout.NORTH, ControlPane_1, -40,
                SpringLayout.SOUTH, Player1Panel);
        sl_Player1Panel.putConstraint(SpringLayout.WEST, ControlPane_1, 0,
                SpringLayout.WEST, Player1Panel);
        sl_Player1Panel.putConstraint(SpringLayout.SOUTH, ControlPane_1, 0,
                SpringLayout.SOUTH, Player1Panel);
        sl_Player1Panel.putConstraint(SpringLayout.EAST, ControlPane_1, 0,
                SpringLayout.EAST, Player1Panel);
        Player1Panel.add(ControlPane_1);
        SpringLayout sl_ControlPane_1 = new SpringLayout();
        ControlPane_1.setLayout(sl_ControlPane_1);

        deckList_1 = new HSCardList();
        deckList_1.setForeground(Color.WHITE);
        deckList_1.setBackground(HSColors.BACKGROUND_COLOR);
        deckList_1.setOpaque(false);
        DeckPane_1.setViewportView(deckList_1);
        sl_Player1Panel.putConstraint(SpringLayout.NORTH, ControlPane_1, -40,
                SpringLayout.SOUTH, Player1Panel);
        sl_Player1Panel.putConstraint(SpringLayout.WEST, ControlPane_1, 0,
                SpringLayout.WEST, Player1Panel);
        sl_Player1Panel.putConstraint(SpringLayout.SOUTH, ControlPane_1, 0,
                SpringLayout.SOUTH, Player1Panel);
        sl_Player1Panel.putConstraint(SpringLayout.EAST, ControlPane_1, 0,
                SpringLayout.EAST, Player1Panel);

        HSButton p1_Load = new HSButton("Load...");
        p1_Load.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (fileChooser_ == null) {
                    fileChooser_ = new JFileChooser();
                    FileFilter filter = new FileNameExtensionFilter("HSDeck file", new String[] {"hsdeck"});
                    fileChooser_.addChoosableFileFilter(filter);
                    fileChooser_.setFileFilter(filter);
                }
                int retVal = fileChooser_.showOpenDialog(frame);
                if (retVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        DeckListFile deckList = new DeckListFile(fileChooser_
                                .getSelectedFile().toPath());
                        ((SortedListModel<ImplementedCard>) deckList_1
                                .getModel()).clear();
                        for (int indx = 0; indx < deckList.getDeck()
                                .getNumCards(); ++indx) {
                            Card card = deckList.getDeck().drawCard(indx);
                            ((SortedListModel<ImplementedCard>) deckList_1
                                    .getModel())
                                    .addElement(IMPLEMENTED_CARD_LIST
                                            .getCardForClass(card.getClass()));
                        }
                        hsModel_.getSimulation().setDeck_p1(deckList.getDeck());
                        hsModel_.getSimulation().setHero_p1(deckList.getHero());
                        lblHero_1.setText(deckList.getHero().getHeroClass());
                    } catch (HSInvalidCardException e1) {
                        JOptionPane.showMessageDialog(frame, e1.getMessage(),
                                "Error: Card not valid.", JOptionPane.ERROR_MESSAGE);
                    } catch (HSInvalidHeroException e1) {
                        JOptionPane.showMessageDialog(frame,
                                "Error: Hero not valid.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(frame,
                                "Error opening the deck file", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        sl_ControlPane_1.putConstraint(SpringLayout.NORTH, p1_Load, 5,
                SpringLayout.NORTH, ControlPane_1);
        sl_ControlPane_1.putConstraint(SpringLayout.WEST, p1_Load, 5,
                SpringLayout.WEST, ControlPane_1);
        p1_Load.setPreferredSize(new Dimension(80, 30));
        p1_Load.setForeground(Color.WHITE);
        p1_Load.setBackground(HSColors.DEFAULT_BUTTON_COLOR);
        ControlPane_1.add(p1_Load);

        JButton p1_save = new HSButton("Save...");
        p1_save.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (fileChooser_ == null) {
                    fileChooser_ = new JFileChooser();
                }
                FileFilter filter = new FileNameExtensionFilter("HSDeck file", new String[] {"hsdeck"});
                fileChooser_.addChoosableFileFilter(filter);
                fileChooser_.setFileFilter(filter);
                fileChooser_.setCurrentDirectory(new File("/home/me/Documents"));
                int retVal = fileChooser_.showSaveDialog(null);
                if (retVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        File saveFile = fileChooser_.getSelectedFile();
                        String p1_HeroName = lblHero_1.getText();
                        Deck p1_Deck = deckList_1.getDeck();

                        DeckListFile deckList = new DeckListFile(p1_HeroName, p1_Deck);
                        deckList.writeDeckListToFile(saveFile);
                    } catch (HSInvalidHeroException e1) {
                        JOptionPane.showMessageDialog(frame,
                                "Error: Hero not valid.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(frame,
                                "Error opening the deck file", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        p1_save.setForeground(Color.WHITE);
        p1_save.setBackground(HSColors.DEFAULT_BUTTON_COLOR);
        p1_save.setPreferredSize(new Dimension(80, 30));
        sl_ControlPane_1.putConstraint(SpringLayout.EAST, p1_save, -5,
                SpringLayout.EAST, ControlPane_1);
        sl_ControlPane_1.putConstraint(SpringLayout.NORTH, p1_save, 5,
                SpringLayout.NORTH, ControlPane_1);
        ControlPane_1.add(p1_save);

        // --------------------------------------------------------------------------------
        // --------------------------------------------------------------------------------

        JPanel cardCountPanel_0 = new JPanel();
        cardCountPanel_0.setBackground(HSColors.BACKGROUND_COLOR);
        cardCountPanel_0.setForeground(HSColors.TEXT_COLOR);
        sl_Player0Panel.putConstraint(SpringLayout.NORTH, cardCountPanel_0, 0,
                SpringLayout.SOUTH, DeckPane_0);
        sl_Player0Panel.putConstraint(SpringLayout.WEST, cardCountPanel_0, 0,
                SpringLayout.WEST, DeckPane_0);
        sl_Player0Panel.putConstraint(SpringLayout.SOUTH, cardCountPanel_0, 0,
                SpringLayout.NORTH, ControlPane_0);
        sl_Player0Panel.putConstraint(SpringLayout.EAST, cardCountPanel_0, 0,
                SpringLayout.EAST, Player0Panel);
        Player0Panel.add(cardCountPanel_0);
        cardCountPanel_0.setLayout(null);

        cardCount_0 = new JLabel("0 / 30");
        cardCount_0.setHorizontalAlignment(SwingConstants.CENTER);
        cardCount_0.setBounds(0, 0, 195, 20);
        cardCount_0.setFont(new Font("Helvetica Neue", Font.PLAIN, 12));
        cardCount_0.setForeground(HSColors.TEXT_COLOR);
        cardCountPanel_0.add(cardCount_0);

        JPanel cardCountPanel_1 = new JPanel();
        cardCountPanel_1.setBackground(HSColors.BACKGROUND_COLOR);
        cardCountPanel_1.setForeground(HSColors.TEXT_COLOR);
        sl_Player1Panel.putConstraint(SpringLayout.NORTH, cardCountPanel_1, 0,
                SpringLayout.SOUTH, DeckPane_1);
        sl_Player1Panel.putConstraint(SpringLayout.WEST, cardCountPanel_1, 0,
                SpringLayout.WEST, DeckPane_1);
        sl_Player1Panel.putConstraint(SpringLayout.SOUTH, cardCountPanel_1, 0,
                SpringLayout.NORTH, ControlPane_1);
        sl_Player1Panel.putConstraint(SpringLayout.EAST, cardCountPanel_1, 0,
                SpringLayout.EAST, Player1Panel);
        Player1Panel.add(cardCountPanel_1);
        cardCountPanel_1.setLayout(null);

        cardCount_1 = new JLabel("0 / 30");
        cardCount_1.setHorizontalAlignment(SwingConstants.CENTER);
        cardCount_1.setBounds(0, 0, 195, 20);
        cardCount_1.setFont(new Font("Helvetica Neue", Font.PLAIN, 12));
        cardCount_1.setForeground(HSColors.TEXT_COLOR);
        cardCountPanel_1.add(cardCount_1);

        deckList_0.getModel().addListDataListener(new ListDataListener() {

            @Override
            public void intervalAdded(ListDataEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                // TODO Auto-generated method stub
                cardCount_0.setText(deckList_0.getModel().getSize() + " / 30");
            }

        });

        deckList_1.getModel().addListDataListener(new ListDataListener() {

            @Override
            public void intervalAdded(ListDataEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                // TODO Auto-generated method stub
                cardCount_1.setText(deckList_1.getModel().getSize() + " / 30");
            }

        });
        // --------------------------------------------------------------------
        // Deck creation
        // --------------------------------------------------------------------

        btnDeckCreate_0.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (middlePanel.isVisible()) {
                    middlePanel.setVisible(false);
                    deckCreatePanel_0.setVisible(true);
                    deckCreatePanel_0.setEditing(true);
                } else if (deckCreatePanel_0.isVisible()) {
                    middlePanel.setVisible(true);
                    deckCreatePanel_0.setVisible(false);
                    deckCreatePanel_0.setEditing(false);
                    if (deckList_0.getModel().getSize() > 0)
                        hsModel_.getSimulation().setDeck_p0(
                                deckList_0.getDeck());
                }
            }
        });

        btnDeckCreate_1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (middlePanel.isVisible()) {
                    middlePanel.setVisible(false);
                    deckCreatePanel_1.setVisible(true);
                    deckCreatePanel_1.setEditing(true);
                } else if (deckCreatePanel_1.isVisible()) {
                    middlePanel.setVisible(true);
                    deckCreatePanel_1.setVisible(false);
                    deckCreatePanel_1.setEditing(false);
                    if (deckList_1.getModel().getSize() > 0)
                        hsModel_.getSimulation().setDeck_p1(
                                deckList_1.getDeck());
                }
            }
        });

        deckCreatePanel_0 = new HSDeckCreatePanel(0, hsModel_, lblHero_0);
        deckCreatePanel_0.setForeground(Color.WHITE);
        deckCreatePanel_0.setBackground(HSColors.LIGHTER_BACKGROUND_COLOR);
        springLayout.putConstraint(SpringLayout.WEST, deckCreatePanel_0, 0,
                SpringLayout.EAST, Player0Panel);
        springLayout.putConstraint(SpringLayout.EAST, deckCreatePanel_0, 0,
                SpringLayout.WEST, Player1Panel);
        springLayout.putConstraint(SpringLayout.NORTH, deckCreatePanel_0, 0,
                SpringLayout.NORTH, frame.getContentPane());
        springLayout.putConstraint(SpringLayout.SOUTH, deckCreatePanel_0, 0,
                SpringLayout.SOUTH, frame.getContentPane());
        deckCreatePanel_0.setPlayer(0);
        deckCreatePanel_0.setVisible(false);
        deckCreatePanel_0.setCardListPane(deckList_0);
        frame.getContentPane().add(deckCreatePanel_0);

        deckCreatePanel_1 = new HSDeckCreatePanel(1, hsModel_, lblHero_1);
        deckCreatePanel_1.setForeground(Color.WHITE);
        deckCreatePanel_1.setBackground(HSColors.LIGHTER_BACKGROUND_COLOR);
        springLayout.putConstraint(SpringLayout.WEST, deckCreatePanel_1, 0,
                SpringLayout.EAST, Player0Panel);
        springLayout.putConstraint(SpringLayout.EAST, deckCreatePanel_1, 0,
                SpringLayout.WEST, Player1Panel);
        springLayout.putConstraint(SpringLayout.NORTH, deckCreatePanel_1, 0,
                SpringLayout.NORTH, frame.getContentPane());
        springLayout.putConstraint(SpringLayout.SOUTH, deckCreatePanel_1, 0,
                SpringLayout.SOUTH, frame.getContentPane());
        deckCreatePanel_1.setPlayer(1);
        deckCreatePanel_1.setVisible(false);
        deckCreatePanel_1.setCardListPane(deckList_1);
        frame.getContentPane().add(deckCreatePanel_1);
        // --------------------------------------------------------------------
        // --------------------------------------------------------------------
    }

    public void updateInfoPanel() {
        int nWins_0 = hsModel_.getGameStats().getWins_p0();
        int nWins_1 = hsModel_.getGameStats().getWins_p1();
        lblWin_0.setText("" + nWins_0);
        lblWin_1.setText("" + nWins_1);
        lblWinRate_0.setText(pFormatter_.format(100.0 * hsModel_.getGameStats()
                .getWinRate_p0()) + "%");
        lblWinRate_1.setText(pFormatter_.format(100.0 * hsModel_.getGameStats()
                .getWinRate_p1()) + "%");

        lblWinRane_frst_0.setText(pFormatter_.format(100.0 * hsModel_
                .getGameStats().getWinRateWhenGoingFirst_p0())
                + "% (going first)");
        lblWinRane_frst_1.setText(pFormatter_.format(100.0 * hsModel_
                .getGameStats().getWinRateWhenGoingFirst_p1())
                + "% (going first)");

        try {
            lblConfNum_0.setText(pFormatter_.format(100.0 * hsModel_
                    .getGameStats().getWinRateContRange_lower(0.95, nWins_0,
                            nWins_0 + nWins_1))
                    + "%"
                    + " -- "
                    + pFormatter_.format(100.0 * hsModel_.getGameStats()
                            .getWinRateContRange_upper(0.95, nWins_0,
                                    nWins_0 + nWins_1)) + "%");
        } catch (Exception e) {
            lblConfNum_0.setText("--");
        }

        try {
            lblConfNum_1.setText(pFormatter_.format(100.0 * hsModel_
                    .getGameStats().getWinRateContRange_lower(0.95, nWins_1,
                            nWins_0 + nWins_1))
                    + "%"
                    + " -- "
                    + pFormatter_.format(100.0 * hsModel_.getGameStats()
                            .getWinRateContRange_upper(0.95, nWins_1,
                                    nWins_0 + nWins_1)) + "%");
        } catch (Exception e) {
            lblConfNum_1.setText("--");
        }
        frame.repaint();
    }

    public void updatePlotPanel() {
        plot_aveMinions.clear(false);
        plot_aveCards.clear(false);
        plot_aveHealth.clear(false);

        plot_aveMinions.repaint();
        plot_aveCards.repaint();
        plot_aveHealth.repaint();

        double[] data0;
        double[] data1;

        if (currentShownPlot_ == plot_aveMinions) {
            data0 = hsModel_.getGameStats().getAveNumMinions_p0();
            data1 = hsModel_.getGameStats().getAveNumMinions_p1();
        } else if (currentShownPlot_ == plot_aveCards) {
            data0 = hsModel_.getGameStats().getAveNumCards_p0();
            data1 = hsModel_.getGameStats().getAveNumCards_p1();
        } else if (currentShownPlot_ == plot_aveHealth) {
            data0 = hsModel_.getGameStats().getAveHeroHealth_p0();
            data1 = hsModel_.getGameStats().getAveHeroHealth_p1();
        } else {
            return;
        }
        for (int indx = 0; indx < 50; ++indx) {
            currentShownPlot_.addPoint(0, indx, data0[indx], true);
            currentShownPlot_.addPoint(1, indx, data1[indx], true);
        }
        currentShownPlot_.repaint();
    }

    @Override
    public void simulationStarted() {
        // TODO Auto-generated method stub
        btnRun.setBackground(HSColors.ERROR_BUTTON_COLOR);
        btnRun.setText("Stop");
        isRunning_ = true;
    }

    @Override
    public void simulationFinished() {
        // TODO Auto-generated method stub
        btnRun.setBackground(HSColors.SUCCESS_BUTTON_COLOR);
        btnRun.setText("Run");
        isRunning_ = false;
    }
}
