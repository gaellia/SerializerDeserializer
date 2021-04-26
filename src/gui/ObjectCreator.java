package gui;

import data.*;
import main.Sender;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class ObjectCreator {
    private JPanel mainPanel;
    private JComboBox selectCB;
    private JLabel selectObjectLabel;
    private JButton serializeButton;
    private JButton createObjectButton;
    private JPanel cardPanel;
    private JPanel pokemonCard;
    private JPanel trainerCard;
    private JPanel gymCard;
    private JLabel dexNumLabel;
    private JLabel levelLabel;
    private JLabel canEvolveLabel;
    private JTextField dexNumField;
    private JTextField levelField;
    private JComboBox evolveField;
    private JPanel leaderCard;
    private JPanel championCard;
    private JTextField nameField;
    private JLabel nameLabel;
    private JComboBox favePokeCB;
    private JComboBox faveTrainerCB;
    private JLabel favePokeLabel;
    private JLabel faveTrainerLabel;
    private JPanel blankCard;
    private JButton setDexNumButton;
    private JButton setLevelButton;
    private JButton setEvolveButton;
    private JPanel objectsPanel;
    private JButton setNameButton;
    private JButton setFavePkmButton;
    private JButton setFaveTrainerButton;
    private JButton createPkmButton;
    private JButton createTrainerButton;
    private JList objectsList;
    private JTextField hm1Field;
    private JTextField hm2Field;
    private JTextField hm3Field;
    private JButton setHMButton;
    private JLabel requiredHMLabel;
    private JLabel addedHMLabel;
    private JLabel hmArrayLabel;
    private JComboBox pkm1CB;
    private JComboBox pkm2CB;
    private JComboBox pkm3CB;
    private JButton createLeaderPkmButton;
    private JButton setLeaderPkmButton;
    private JPanel leaderObjPanel;
    private JList champObjList;
    private JButton createChampPkmButton;
    private JButton setChampPkmButton;

    private ArrayList<Object> createdObjects = new ArrayList<Object>();
    private DefaultListModel objectsListModel = new DefaultListModel();
    private DefaultComboBoxModel favePokeCBModel = new DefaultComboBoxModel();
    private DefaultComboBoxModel faveTrainerCBModel = new DefaultComboBoxModel();

    public ObjectCreator(JFrame frame) {
        // initialize CB models
        favePokeCBModel.addElement("(none)");
        faveTrainerCBModel.addElement("(none)");
        favePokeCB.setModel(favePokeCBModel);
        faveTrainerCB.setModel(faveTrainerCBModel);

        // checks if the create button was pressed
        createObjectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // determines what object to create
                String objectType = selectCB.getSelectedItem().toString();
                CardLayout cl = (CardLayout) cardPanel.getLayout();;
                switch (objectType) {
                    case "Pokemon":
                        setUpPokemon(cl);
                        break;

                    case "Trainer":
                        setUpTrainer(cl);
                        break;

                    case "Gym":
                        setUpGym(cl);
                        break;

                    case "Leader":
                        setUpLeader(cl);
                        break;

                    case "Champion":
                        setUpChamp(cl);
                        break;

                    default:
                }
            }
        });

        // checks if the serialize button was pressed
        serializeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Sending " + createdObjects.get(0) + " to the serializer...");
                Sender.setObj(createdObjects.get(0));
                frame.dispose();
            }
        });
    }

    public void refreshFavePokeCB() {
        for (Object pkm : createdObjects) {
            if (pkm.getClass().getSimpleName().equals("Pokemon") && (favePokeCBModel.getIndexOf(pkm) == -1)) {
                favePokeCBModel.addElement(pkm);
                favePokeCB.setModel(favePokeCBModel);
            }
        }
    }

    public void refreshFaveTrainerCB() {
        for (Object tr : createdObjects) {
            if (tr.getClass().getSimpleName().equals("Trainer") && (faveTrainerCBModel.getIndexOf(tr) == -1)) {
                faveTrainerCBModel.addElement(tr);
                faveTrainerCB.setModel(faveTrainerCBModel);
            }
        }
    }

    public void refreshPkmCB(JComboBox pkmCB, DefaultComboBoxModel pkmCBModel) {
        for (Object pkm : createdObjects) {
            if (pkm.getClass().getSimpleName().equals("Pokemon") && (pkmCBModel.getIndexOf(pkm) == -1)) {
                pkmCBModel.addElement(pkm);
                pkmCB.setModel(pkmCBModel);
            }
        }
    }

    public void checkSerialize() {
            // pokemon
        if (!setDexNumButton.isVisible() && !setLevelButton.isVisible() && !setEvolveButton.isVisible()) {
            serializeButton.setEnabled(true);
            // trainer
        } else if (!setNameButton.isVisible() && !setFavePkmButton.isVisible() && !setFaveTrainerButton.isVisible()) {
            serializeButton.setEnabled(true);
        }
    }

    public void setUpChamp(CardLayout cl) {
        Champion champ = new Champion();
        createdObjects.add(champ);
        cl.show(cardPanel, "championCard");

        selectCB.setEnabled(false);
        createObjectButton.setVisible(false);

        // creates a new pokemon
        createChampPkmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Pokemon Creator");
                frame.setContentPane(new PokemonCreator(frame, createdObjects, objectsListModel).getPkmPanel());
                frame.setPreferredSize(new Dimension(450, 300));
                frame.pack();
                frame.setVisible(true);

                // Add to list after pokemon is created
                frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e)
                    {
                        champObjList.setModel(objectsListModel);
                    }
                });
            }
        });

        // sets all top pokemon for the champ
        setChampPkmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Pokemon> pkms = new ArrayList<Pokemon>();

                for (int i = 0; i < createdObjects.size(); i++) {
                    if (createdObjects.get(i).getClass().getSimpleName().equals("Pokemon")) {
                        pkms.add((Pokemon) createdObjects.get(i));
                    }
                }

                champ.setTopPokemon(pkms);

                setChampPkmButton.setVisible(false);
                createChampPkmButton.setVisible(false);
                serializeButton.setEnabled(true);
            }
        });
    }

    public void setUpLeader(CardLayout cl) {
        Leader gymLeader = new Leader();
        createdObjects.add(gymLeader);
        cl.show(cardPanel, "leaderCard");

        selectCB.setEnabled(false);
        createObjectButton.setVisible(false);

        // initialize pokemon CB
        DefaultComboBoxModel pkm1CBModel = new DefaultComboBoxModel();
        DefaultComboBoxModel pkm2CBModel = new DefaultComboBoxModel();
        DefaultComboBoxModel pkm3CBModel = new DefaultComboBoxModel();
        pkm1CBModel.addElement("(none)");
        pkm2CBModel.addElement("(none)");
        pkm3CBModel.addElement("(none)");
        pkm1CB.setModel(pkm1CBModel);
        pkm2CB.setModel(pkm2CBModel);
        pkm3CB.setModel(pkm3CBModel);

        // creates a new pokemon
        createLeaderPkmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Pokemon Creator");
                frame.setContentPane(new PokemonCreator(frame, createdObjects, objectsListModel).getPkmPanel());
                frame.setPreferredSize(new Dimension(450, 300));
                frame.pack();
                frame.setVisible(true);

                // Add to list after pokemon is created
                frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e)
                    {
                        refreshPkmCB(pkm1CB, pkm1CBModel);
                        refreshPkmCB(pkm2CB, pkm2CBModel);
                        refreshPkmCB(pkm3CB, pkm3CBModel);
                    }
                });
            }
        });

        // sets the three specified pokemon
        setLeaderPkmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Object pkm1 = pkm1CB.getSelectedItem();
                Object pkm2 = pkm2CB.getSelectedItem();
                Object pkm3 = pkm3CB.getSelectedItem();
                gymLeader.setMembers(new Pokemon[]{(Pokemon) pkm1, (Pokemon) pkm2, (Pokemon) pkm3});

                setLeaderPkmButton.setVisible(false);
                createLeaderPkmButton.setVisible(false);
                pkm1CB.setEnabled(false);
                pkm2CB.setEnabled(false);
                pkm3CB.setEnabled(false);

                serializeButton.setEnabled(true);
            }
        });
    }

    public void setUpGym(CardLayout cl) {
        Gym gym = new Gym();
        createdObjects.add(gym);
        cl.show(cardPanel, "gymCard");

        selectCB.setEnabled(false);
        createObjectButton.setVisible(false);

        // sets the three specified HMs
        setHMButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int hm1 = Integer.parseInt(hm1Field.getText());
                int hm2 = Integer.parseInt(hm2Field.getText());
                int hm3 = Integer.parseInt(hm3Field.getText());
                gym.setRequiredHMs(new int[]{hm1, hm2, hm3});
                setHMButton.setVisible(false);
                hm1Field.setEnabled(false);
                hm2Field.setEnabled(false);
                hm3Field.setEnabled(false);

                hmArrayLabel.setText("[" + String.join(" , ", hm1Field.getText(), hm2Field.getText(), hm3Field.getText()) + "]");

                serializeButton.setEnabled(true);
            }
        });
    }

    public void setUpTrainer(CardLayout cl) {
        Trainer tr = new Trainer();
        cl.show(cardPanel, "trainerCard");

        selectCB.setEnabled(false);
        createObjectButton.setVisible(false);

        // sets the name and adds to the trainer list
        setNameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                tr.setName(name);
                createdObjects.add(tr);
                objectsListModel.addElement("(" + tr.getClass().getSimpleName() + "):  " + tr.getName());
                objectsList.setModel(objectsListModel);

                // enable creating/setting pokemon and trainers
                setFavePkmButton.setEnabled(true);
                setFaveTrainerButton.setEnabled(true);
                createPkmButton.setEnabled(true);
                createTrainerButton.setEnabled(true);

                nameField.setEnabled(false);
                setNameButton.setVisible(false);
                refreshFaveTrainerCB();
                checkSerialize();
            }
        });

        // sets the fave pokemon to the selected
        setFavePkmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Pokemon favePkm;
                String favePkmText = favePokeCB.getModel().getSelectedItem().toString();
                if (favePkmText.equals("(none)")) {
                    favePkm = null;
                } else {
                    favePkm = (Pokemon) favePokeCB.getModel().getSelectedItem();
                }
                tr.setFavePokemon(favePkm);

                favePokeCB.setEnabled(false);
                setFavePkmButton.setVisible(false);
                createPkmButton.setVisible(false);
                checkSerialize();
            }
        });

        // sets the fave trainer to the selected
        setFaveTrainerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Trainer faveTrainer;
                String faveTrainerText = faveTrainerCB.getModel().getSelectedItem().toString();
                if (faveTrainerText.equals("(none)")) {
                    faveTrainer = null;
                } else {
                    faveTrainer = (Trainer) faveTrainerCB.getModel().getSelectedItem();
                }
                tr.setFaveTrainer(faveTrainer);

                faveTrainerCB.setEnabled(false);
                setFaveTrainerButton.setVisible(false);
                createTrainerButton.setVisible(false);
                checkSerialize();
            }
        });

        // creates a new pokemon
        createPkmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Pokemon Creator");
                frame.setContentPane(new PokemonCreator(frame, createdObjects, objectsListModel).getPkmPanel());
                frame.setPreferredSize(new Dimension(450, 300));
                frame.pack();
                frame.setVisible(true);

                // Add to list after pokemon is created
                frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e)
                    {
                        objectsList.setModel(objectsListModel);
                        refreshFavePokeCB();
                    }
                });
            }
        });

        // creates a new trainer
        createTrainerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Trainer Creator");
                frame.setContentPane(new TrainerCreator(frame, createdObjects, objectsListModel, favePokeCBModel, faveTrainerCBModel).getTrPanel());
                frame.setPreferredSize(new Dimension(450, 300));
                frame.pack();
                frame.setVisible(true);

                // Add to list after trainer is created
                frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e)
                    {
                        objectsList.setModel(objectsListModel);
                        refreshFavePokeCB();
                        refreshFaveTrainerCB();
                    }
                });
            }
        });
    }

    public void setUpPokemon(CardLayout cl) {
        Pokemon pkm = new Pokemon();
        createdObjects.add(pkm);
        cl.show(cardPanel, "pokemonCard");

        selectCB.setEnabled(false);
        createObjectButton.setVisible(false);

        // sets the Pokemon's dex num when pressed
        setDexNumButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int dexNum = Integer.parseInt(dexNumField.getText());
                pkm.setDexNum(dexNum);
                setDexNumButton.setVisible(false);
                dexNumField.setEnabled(false);
                checkSerialize();
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
                checkSerialize();
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
                checkSerialize();
            }
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

}
