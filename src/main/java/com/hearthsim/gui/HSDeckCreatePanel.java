package com.hearthsim.gui;

import com.hearthsim.exception.HSException;
import com.hearthsim.util.HeroFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HSDeckCreatePanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    private static final int TITLE_HEIGHT = 60;

    int playerIndex_;
    SpringLayout layout_;

    JPanel titlePanel_;
    JPanel controlPanel_;
    JPanel mainPanel_;
    JPanel heroChoicePanel_;
    JComboBox<String> heroChoice_;

    JLabel title_;

    HSArrowButton leftArrow_;
    HSArrowButton rightArrow_;

    HSCardSelectionList cardList_;

    public HSDeckCreatePanel(int playerIndex, final HSMainFrameModel hsModel, final JLabel label) {
        super();
        playerIndex_ = playerIndex;
        layout_ = new SpringLayout();
        super.setLayout(layout_);

        titlePanel_ = new JPanel();
        SpringLayout sl_titlePanel = new SpringLayout();
        titlePanel_.setLayout(sl_titlePanel);
        layout_.putConstraint(SpringLayout.NORTH, titlePanel_, 0, SpringLayout.NORTH, this);
        layout_.putConstraint(SpringLayout.SOUTH, titlePanel_, TITLE_HEIGHT, SpringLayout.NORTH, this);
        layout_.putConstraint(SpringLayout.EAST, titlePanel_, 0, SpringLayout.EAST, this);
        layout_.putConstraint(SpringLayout.WEST, titlePanel_, 0, SpringLayout.WEST, this);

        heroChoicePanel_ = new JPanel();
        heroChoicePanel_.setOpaque(false);
        layout_.putConstraint(SpringLayout.NORTH, heroChoicePanel_, 0, SpringLayout.SOUTH, titlePanel_);
        layout_.putConstraint(SpringLayout.SOUTH, heroChoicePanel_, 50, SpringLayout.SOUTH, titlePanel_);
        layout_.putConstraint(SpringLayout.EAST, heroChoicePanel_, 0, SpringLayout.EAST, this);
        layout_.putConstraint(SpringLayout.WEST, heroChoicePanel_, 0, SpringLayout.WEST, this);

        String[] heroes = {"None", "Druid", "Hunter", "Mage", "Paladin", "Priest", "Rogue", "Shaman", "Warlock", "Warrior"};
        heroChoice_ = new HSHeroChoiceComboBox(heroes);
        heroChoice_.setPreferredSize(new Dimension(150, 40));
        if (playerIndex == 0)
            heroChoice_.setSelectedItem(hsModel.getSimulation().getHero_p0().getName());
        else if (playerIndex == 1)
            heroChoice_.setSelectedItem(hsModel.getSimulation().getHero_p1().getName());
        heroChoice_.setFont(new Font("Helvetica Neue", Font.PLAIN, 22));


        heroChoice_.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (playerIndex_ == 0) {
                        hsModel.getSimulation().setHero_p0(HeroFactory.getHero((String)heroChoice_.getSelectedItem()));
                    } else if (playerIndex_ == 1) {
                        hsModel.getSimulation().setHero_p1(HeroFactory.getHero((String)heroChoice_.getSelectedItem()));
                    }
                    label.setText((String)heroChoice_.getSelectedItem());
                } catch (HSException exception) {
                    log.error(exception.toString());
                }
            }

        });


        JLabel heroChoiceLegend = new JLabel("Hero:");
        heroChoiceLegend.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
        heroChoiceLegend.setForeground(HSColors.TEXT_COLOR);
        heroChoicePanel_.add(heroChoiceLegend);
        heroChoicePanel_.add(heroChoice_);

        mainPanel_ = new JPanel();
        SpringLayout sl_mainPanel = new SpringLayout();
        mainPanel_.setLayout(sl_mainPanel);
        layout_.putConstraint(SpringLayout.NORTH, mainPanel_, 0, SpringLayout.SOUTH, heroChoicePanel_);
        layout_.putConstraint(SpringLayout.SOUTH, mainPanel_, 0, SpringLayout.SOUTH, this);
        layout_.putConstraint(SpringLayout.EAST, mainPanel_, 0, SpringLayout.EAST, this);
        layout_.putConstraint(SpringLayout.WEST, mainPanel_, 0, SpringLayout.WEST, this);


        this.add(heroChoicePanel_);
        this.add(titlePanel_);
        this.add(mainPanel_);

        JPanel leftArrowPanel = new JPanel();
        leftArrowPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));
        leftArrowPanel.setOpaque(false);
        sl_titlePanel.putConstraint(SpringLayout.NORTH, leftArrowPanel, 0, SpringLayout.NORTH, titlePanel_);
        sl_titlePanel.putConstraint(SpringLayout.SOUTH, leftArrowPanel, 0, SpringLayout.SOUTH, titlePanel_);
        sl_titlePanel.putConstraint(SpringLayout.WEST, leftArrowPanel, 0, SpringLayout.WEST, titlePanel_);
        sl_titlePanel.putConstraint(SpringLayout.EAST, leftArrowPanel, 100, SpringLayout.WEST, titlePanel_);

        JPanel rightArrowPanel = new JPanel();
        rightArrowPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));
        rightArrowPanel.setOpaque(false);
        sl_titlePanel.putConstraint(SpringLayout.NORTH, rightArrowPanel, 0, SpringLayout.NORTH, titlePanel_);
        sl_titlePanel.putConstraint(SpringLayout.SOUTH, rightArrowPanel, 0, SpringLayout.SOUTH, titlePanel_);
        sl_titlePanel.putConstraint(SpringLayout.WEST, rightArrowPanel, -100, SpringLayout.EAST, titlePanel_);
        sl_titlePanel.putConstraint(SpringLayout.EAST, rightArrowPanel, 0, SpringLayout.EAST, titlePanel_);

        JPanel titleTextPanel = new JPanel();
        titleTextPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 18));
        titleTextPanel.setOpaque(false);
        sl_titlePanel.putConstraint(SpringLayout.NORTH, titleTextPanel, 0, SpringLayout.NORTH, titlePanel_);
        sl_titlePanel.putConstraint(SpringLayout.SOUTH, titleTextPanel, 0, SpringLayout.SOUTH, titlePanel_);
        sl_titlePanel.putConstraint(SpringLayout.WEST, titleTextPanel, 0, SpringLayout.EAST, leftArrowPanel);
        sl_titlePanel.putConstraint(SpringLayout.EAST, titleTextPanel, 0, SpringLayout.WEST, rightArrowPanel);

        titlePanel_.add(leftArrowPanel);
        titlePanel_.add(titleTextPanel);
        titlePanel_.add(rightArrowPanel);

        title_ = new JLabel("Create a Deck for Player" + playerIndex_);
        title_.setFont(new Font("Helvetica Neue", Font.PLAIN, 20));
        title_.setForeground(Color.WHITE);
        titleTextPanel.add(title_);

        leftArrow_ = new HSArrowButton(HSArrowButton.LEFT);
        leftArrow_.setPreferredSize(new Dimension(20, 20));
        leftArrow_.setForeground(Color.WHITE);
        leftArrow_.setOpaque(false);
        leftArrowPanel.add(leftArrow_);

        rightArrow_ = new HSArrowButton(HSArrowButton.RIGHT);
        rightArrow_.setPreferredSize(new Dimension(20, 20));
        rightArrow_.setForeground(Color.WHITE);
        rightArrow_.setOpaque(false);
        rightArrowPanel.add(rightArrow_);

        cardList_ = new HSCardSelectionList();
        cardList_.setForeground(Color.WHITE);
        cardList_.setBackground(HSColors.LIGHTER_BACKGROUND_COLOR);

//        JPanel headerPanel = new HSCardSelectionList.CardSelectionCellRenderer("Mana", "Name", "Attack", "Health", "Text");
//        sl_mainPanel.putConstraint(SpringLayout.NORTH, headerPanel, 0, SpringLayout.NORTH, mainPanel_);
//        sl_mainPanel.putConstraint(SpringLayout.SOUTH, headerPanel, 18, SpringLayout.NORTH, mainPanel_);
//        sl_mainPanel.putConstraint(SpringLayout.WEST, headerPanel, 5, SpringLayout.WEST, mainPanel_);
//        sl_mainPanel.putConstraint(SpringLayout.EAST, headerPanel, -5, SpringLayout.EAST, mainPanel_);

        JScrollPane DeckPane_0 = new JScrollPane();
        sl_mainPanel.putConstraint(SpringLayout.NORTH, DeckPane_0, 0, SpringLayout.NORTH, mainPanel_);
        sl_mainPanel.putConstraint(SpringLayout.SOUTH, DeckPane_0, 0, SpringLayout.SOUTH, mainPanel_);
        sl_mainPanel.putConstraint(SpringLayout.WEST, DeckPane_0, 5, SpringLayout.WEST, mainPanel_);
        sl_mainPanel.putConstraint(SpringLayout.EAST, DeckPane_0, -5, SpringLayout.EAST, mainPanel_);
        DeckPane_0.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        DeckPane_0.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        DeckPane_0.setOpaque(false);
        DeckPane_0.setBackground(Color.DARK_GRAY);
        DeckPane_0.setLayout(new ScrollPaneLayout());
        DeckPane_0.getViewport().setOpaque(false);
        DeckPane_0.setBorder(BorderFactory.createEmptyBorder());
        DeckPane_0.setViewportView(cardList_);
        DeckPane_0.getVerticalScrollBar().setUI(new HSScrollbarUI());
        DeckPane_0.getHorizontalScrollBar().setUI(new HSScrollbarUI());


//        mainPanel_.add(headerPanel);
        mainPanel_.add(DeckPane_0);
    }

    @Override
    public void setLayout(LayoutManager mgr) {
        //can't change the layout of this panel
    }

    @Override
    public void setBackground(Color color) {
        super.setBackground(color);
        if (titlePanel_ != null) titlePanel_.setBackground(this.getBackground());
        if (mainPanel_ != null) mainPanel_.setBackground(this.getBackground());
        if (controlPanel_ != null) controlPanel_.setBackground(this.getBackground());
        if (leftArrow_ != null) leftArrow_.setBackground(this.getBackground());
        if (rightArrow_ != null) rightArrow_.setBackground(this.getBackground());
    }

    public boolean getEditing() {
        return cardList_.editing_;
    }

    public void setEditing(boolean value) {
        cardList_.setEditing(value);
    }

    public void setCardListPane(HSCardList cardListPane) {
        this.cardList_.setCardListPane(cardListPane);
    }

    /**
     * Set the player index for the deck.  0 or 1.
     * @param playerIndex
     */
    public void setPlayer(int playerIndex) {
        playerIndex_ = playerIndex;
        title_.setText("Create a Deck for Player" + playerIndex_);
        if (playerIndex_ == 0) {
            leftArrow_.setVisible(true);
            rightArrow_.setVisible(false);
        } else if (playerIndex_ == 1) {
            leftArrow_.setVisible(false);
            rightArrow_.setVisible(true);
        }
    }
}
