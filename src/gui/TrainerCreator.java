package gui;

import data.Pokemon;
import data.Trainer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class TrainerCreator {
    private JPanel trPanel;
    private JPanel trainerCard;
    private JLabel nameLabel;
    private JLabel favePokeLabel;
    private JLabel faveTrainerLabel;
    private JTextField nameField;
    private JComboBox favePokeCB;
    private JComboBox faveTrainerCB;
    private JPanel objectsPanel;
    private JList objectsList;
    private JButton setNameButton;
    private JButton setFavePkmButton;
    private JButton setFaveTrainerButton;
    private JButton createPkmButton;
    private JButton createTrainerButton;
    private JButton createMainTrainerButton;

    private ArrayList<Object> createdObjects = new ArrayList<Object>();
    private DefaultComboBoxModel favePokeCBModel = new DefaultComboBoxModel();
    private DefaultComboBoxModel faveTrainerCBModel = new DefaultComboBoxModel();

    public TrainerCreator(JFrame frame, ArrayList<Object> objects, DefaultListModel objectsListModel, DefaultComboBoxModel pkmCBModel, DefaultComboBoxModel trCBModel) {
        this.createdObjects = objects;
        this.favePokeCBModel = pkmCBModel;
        this.faveTrainerCBModel = trCBModel;

        // initialize CB models
        favePokeCB.setModel(favePokeCBModel);
        faveTrainerCB.setModel(faveTrainerCBModel);
        objectsList.setModel(objectsListModel);

        Trainer tr = new Trainer();

        // sets the name and adds to the trainer list
        setNameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                tr.setName(name);
                objects.add(tr);
                objectsListModel.addElement("(" + tr.getClass().getSimpleName() + "):  " + tr.getName());
                objectsList.setModel(objectsListModel);

                nameField.setEnabled(false);
                setNameButton.setVisible(false);
                refreshFaveTrainerCB();
                checkCreate();

                // after you set a name, you can't close window
                frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
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
                checkCreate();
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
                checkCreate();
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
                frame.setContentPane(new TrainerCreator(frame, createdObjects, objectsListModel, favePokeCBModel, favePokeCBModel).getTrPanel());
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

        // checks if the create button was pressed
        createMainTrainerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
    }

    public void checkCreate() {
        if (!setNameButton.isVisible() && !setFavePkmButton.isVisible() && !setFaveTrainerButton.isVisible()) {
            createMainTrainerButton.setEnabled(true);
        }
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

    public JPanel getTrPanel() {
        return trPanel;
    }
}
