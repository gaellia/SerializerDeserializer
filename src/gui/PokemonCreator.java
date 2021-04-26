package gui;

import data.Pokemon;
import main.Sender;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class PokemonCreator {
    private JPanel pkmPanel;
    private JPanel pokemonCard;
    private JLabel dexNumLabel;
    private JLabel levelLabel;
    private JLabel canEvolveLabel;
    private JTextField dexNumField;
    private JTextField levelField;
    private JComboBox evolveField;
    private JButton setDexNumButton;
    private JButton setLevelButton;
    private JButton setEvolveButton;
    private JButton createPokemonButton;

    public PokemonCreator(JFrame frame, ArrayList<Object> objects, DefaultListModel objectsListModel) {
        Pokemon pkm = new Pokemon();

        // sets the Pokemon's dex num when pressed
        setDexNumButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int dexNum = Integer.parseInt(dexNumField.getText());
                pkm.setDexNum(dexNum);
                setDexNumButton.setVisible(false);
                dexNumField.setEnabled(false);
                checkCreate();
            }
        });

        // sets the Pokemon's level when pressed
        setLevelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int level = Integer.parseInt(levelField.getText());
                pkm.setLevel(level);
                setLevelButton.setVisible(false);
                levelField.setEnabled(false);
                checkCreate();
            }
        });

        // sets the Pokemon's evolve when pressed
        setEvolveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean evolve = false;
                String evolveStr = evolveField.getSelectedItem().toString();
                if (evolveStr.equals("Yes")) {
                    evolve = true;
                } else {
                    evolve = false;
                }
                pkm.setCanEvolve(evolve);
                setEvolveButton.setVisible(false);
                evolveField.setEnabled(false);
                checkCreate();
            }
        });

        // checks if the create button was pressed
        createPokemonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                objects.add(pkm);
                objectsListModel.addElement("(" + pkm.getClass().getSimpleName() + "):  " + pkm.getDexNum());
                frame.dispose();
            }
        });
    }

    public void checkCreate() {
        if (!setDexNumButton.isVisible() && !setLevelButton.isVisible() && !setEvolveButton.isVisible()) {
            createPokemonButton.setEnabled(true);
        }
    }

    public JPanel getPkmPanel() {
        return pkmPanel;
    }
}

