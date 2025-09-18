package Graphique;

import javax.swing.*;
import fonctionnel.Fichier;
import fonctionnel.Repertoire;
import fonctionnel.Snapshot;
import fonctionnel.Image;
import java.text.SimpleDateFormat;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import com.toedter.calendar.JDateChooser;
import java.util.Date;
import java.awt.event.ItemListener; // Pour gérer les événements des cases à cocher
import java.util.Calendar;
import java.util.regex.*;
/**
 * Classe principale pour l'application GUI permettant de visualiser des images, afficher leurs métadonnées,
 * et interagir avec des fichiers et répertoires.
 */
public class GUI extends JFrame {

	/**
	 * Champ de texte pour afficher ou saisir le chemin d'un répertoire.
	 */
	private JTextField dirPathField;

	/**
	 * Champ de texte pour afficher ou saisir le chemin d'un fichier.
	 */
	private JTextField filePathField;

	/**
	 * Zone de texte pour afficher des informations sur la gauche.
	 */
	private JTextArea leftTextArea;

	/**
	 * Zone de texte pour afficher des informations sur la droite.
	 */
	private JTextArea rightTextArea;

	/**
	 * Sélecteur de fichiers pour choisir des répertoires.
	 */
	private JFileChooser dirChooser;

	/**
	 * Sélecteur de fichiers pour choisir un fichier.
	 */
	private JFileChooser fileChooser;

	/**
	 * Bouton pour afficher les informations sur un fichier ou un répertoire.
	 */
	private JButton infoButton;

	/**
	 * Bouton pour lister les fichiers ou répertoires.
	 */
	private JButton listButton;

	/**
	 * Bouton pour afficher les métadonnées d'un fichier.
	 */
	private JButton metadataButton;

	/**
	 * Bouton pour quitter l'application.
	 */
	private JButton quitButton;

	/**
	 * Bouton pour rechercher des fichiers selon certains critères.
	 */
	private JButton rechercheButton;

	/**
	 * Bouton pour sauvegarder un snapshot.
	 */
	private JButton snapshotSaveButton;

	/**
	 * Bouton pour comparer un snapshot.
	 */
	private JButton snapshotCompButton;

	/**
	 * Bouton pour afficher les statistiques.
	 */
	private JButton statsButton;

	/**
	 * Répertoire sélectionné par l'utilisateur.
	 */
	private File selectedDirectory;

	/**
	 * Fichier sélectionné par l'utilisateur.
	 */
	private File selectedFile;

	/**
	 * Étiquette pour afficher une miniature de l'image sélectionnée.
	 */
	private JLabel thumbnailLabel;

	/**
	 * Étiquette pour afficher l'image principale.
	 */
	private JLabel imageLabel;

	/**
	 * Panneau de défilement contenant l'image principale.
	 */
	private JScrollPane imageScrollPane;


    /**
     * Constructeur de la classe GUI. Initialise les composants de l'interface graphique.
     */

    public GUI() {
        setTitle("Image Metadata Viewer");
        setSize(1200, 800);  // Agrandir le panneau
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialisation des composants de l'interface
        leftTextArea = new JTextArea();
        leftTextArea.setEditable(false);
        JScrollPane leftScrollPane = new JScrollPane(leftTextArea);

        rightTextArea = new JTextArea();
        rightTextArea.setEditable(false);
        JScrollPane rightScrollPane = new JScrollPane(rightTextArea);

        filePathField = new JTextField(40);
        filePathField.setEditable(false);

        dirPathField = new JTextField(40);
        dirPathField.setEditable(false);

        infoButton = new JButton("Afficher Info");
        metadataButton = new JButton("Afficher Métadonnées");
        listButton = new JButton("Lister Fichiers");
        statsButton = new JButton("Statistiques");
        quitButton = new JButton("Quitter");
        rechercheButton = new JButton("Recherche");
        snapshotSaveButton = new JButton("Sauvegarder");
        snapshotCompButton = new JButton("Comparer");
        fileChooser = new JFileChooser();
        dirChooser = new JFileChooser();
        dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        // Ajout des listeners pour les boutons
        infoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                afficherInfo();
            }
        });

        metadataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                afficherMetadonnees();
            }
        });

        listButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listerFichiers();
            }
        });

        statsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                afficherStatistiques();
            }
        });
        

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);  // Fermer l'application
            }
        });
        
        snapshotSaveButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		snapShotsave();
        	}
        });
        
        snapshotCompButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		snapshotCompare();
        	}
        });
        
        rechercheButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		recherche();
        	}
        });
        
        
        
        

        JButton chooseFileButton = new JButton("Choisir Fichier");
        chooseFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fileChooser.getSelectedFile();
                    filePathField.setText(selectedFile.getAbsolutePath());
                    afficherImageThumbnail(); // Afficher la miniature après sélection du fichier
                    afficherImageComplete(); // Afficher l'image complète
                }
            }
        });

        JButton chooseDirButton = new JButton("Choisir Répertoire");
        chooseDirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnValue = dirChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    selectedDirectory = dirChooser.getSelectedFile();
                    dirPathField.setText(selectedDirectory.getAbsolutePath());
                }
            }
        });

        // Configuration des panneaux
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());

        JPanel leftTopPanel = new JPanel();
        leftTopPanel.setLayout(new BoxLayout(leftTopPanel, BoxLayout.X_AXIS));

        leftTopPanel.add(chooseFileButton);
        leftTopPanel.add(Box.createHorizontalStrut(10));
        leftTopPanel.add(filePathField);

        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new BorderLayout());
        imagePanel.add(new JLabel("Miniature"), BorderLayout.NORTH);

        thumbnailLabel = new JLabel();
        thumbnailLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        imagePanel.add(thumbnailLabel, BorderLayout.CENTER);

        leftPanel.add(imagePanel, BorderLayout.WEST);

        JPanel leftBottomPanel = new JPanel();
        leftBottomPanel.setLayout(new GridLayout(1, 2, 10, 10));
        leftBottomPanel.add(infoButton);
        leftBottomPanel.add(metadataButton);

        leftPanel.add(leftTopPanel, BorderLayout.NORTH);
        leftPanel.add(leftScrollPane, BorderLayout.CENTER);
        leftPanel.add(leftBottomPanel, BorderLayout.SOUTH);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());

        JPanel rightTopPanel = new JPanel();
        rightTopPanel.setLayout(new BoxLayout(rightTopPanel, BoxLayout.X_AXIS));

        rightTopPanel.add(chooseDirButton);
        rightTopPanel.add(Box.createHorizontalStrut(10));
        rightTopPanel.add(dirPathField);

        JPanel rightBottomPanel = new JPanel();
        rightBottomPanel.setLayout(new GridLayout(1, 6, 10, 10));  // Ajouter plus de colonnes pour les nouveaux boutons
        rightBottomPanel.add(listButton);
        rightBottomPanel.add(statsButton);
        rightBottomPanel.add(snapshotSaveButton);  // Ajouter le bouton SnapshotSave
        rightBottomPanel.add(snapshotCompButton);  // Ajouter le bouton SnapshotCompare
        rightBottomPanel.add(rechercheButton);  // Ajouter le bouton Recherche
        rightBottomPanel.add(quitButton);

        rightPanel.add(rightTopPanel, BorderLayout.NORTH);
        rightPanel.add(rightScrollPane, BorderLayout.CENTER);
        rightPanel.add(rightBottomPanel, BorderLayout.SOUTH);


        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(500);

        add(splitPane, BorderLayout.CENTER);

        imageLabel = new JLabel();
        imageScrollPane = new JScrollPane(imageLabel);
        imageScrollPane.setPreferredSize(new Dimension(600, 400));
        add(imageScrollPane, BorderLayout.SOUTH);
    }

    /**
     * Affiche une miniature de l'image sélectionnée.
     */
    public void afficherImageThumbnail() {
        if (selectedFile != null) {
            try {
                BufferedImage image = ImageIO.read(selectedFile);
                int maxWidth = 100, maxHeight = 100;
                int width = image.getWidth(), height = image.getHeight();
                double ratio = Math.min((double) maxWidth / width, (double) maxHeight / height);
                int newWidth = (int) (width * ratio), newHeight = (int) (height * ratio);
                java.awt.Image scaledImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT);
                thumbnailLabel.setIcon(new ImageIcon(scaledImage));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erreur lors du chargement de l'image : " + e.getMessage());
            }
        }
    }

    /**
     * Affiche l'image complète sélectionnée.
     */
    public void afficherImageComplete() {
        if (selectedFile != null) {
            try {
                BufferedImage image = ImageIO.read(selectedFile);
                imageLabel.setIcon(new ImageIcon(image));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erreur lors du chargement de l'image complète : " + e.getMessage());
            }
        }
    }

    /**
     * Affiche les informations du fichier sélectionné.
     */
    public void afficherInfo() {
        if (selectedFile != null) {
            leftTextArea.setText("");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            PrintStream old = System.out;
            System.setOut(ps);
            Fichier.afficherInformationsFichier(selectedFile.getAbsolutePath());
            System.out.flush();
            System.setOut(old);
            leftTextArea.setText(baos.toString());
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un fichier d'abord.");
        }
    }

    /**
     * Affiche les métadonnées du fichier sélectionné.
     */
    public void afficherMetadonnees() {
        if (selectedFile != null) {
            leftTextArea.setText("");
            Image image = new Image(selectedFile);
            leftTextArea.setText(image.ExtractAllMetadata());
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un fichier d'abord.");
        }
    }

    /**
     * Liste les fichiers dans le répertoire sélectionné.
     */
    public void listerFichiers() {
        if (selectedDirectory != null) {
            rightTextArea.setText("");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            PrintStream old = System.out;
            System.setOut(ps);
            Repertoire.listerFichiers(selectedDirectory.getAbsolutePath());
            System.out.flush();
            System.setOut(old);
            rightTextArea.setText(baos.toString());
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un répertoire d'abord.");
        }
    }

    /**
     * Affiche les statistiques du répertoire sélectionné.
     */
    public void afficherStatistiques() {
        if (selectedDirectory != null) {
            rightTextArea.setText("");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            PrintStream old = System.out;
            System.setOut(ps);
            Repertoire.afficherStatistiques(selectedDirectory.getAbsolutePath());
            System.out.flush();
            System.setOut(old);
            rightTextArea.setText(baos.toString());
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un répertoire d'abord.");
        }
    }
    /**
     * Sauvegarde un instantané (snapshot) du répertoire sélectionné.
     * Si aucun répertoire n'est sélectionné, affiche un message demandant à l'utilisateur
     * de sélectionner un répertoire.
     * 
     * Cette méthode redirige temporairement la sortie standard pour capturer les messages 
     * générés par le processus de sauvegarde et les affiche dans le composant `rightTextArea`.
     */
    public void snapShotsave() {
    	if(selectedDirectory != null) {
    		rightTextArea.setText("");
    		ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            PrintStream old = System.out;
            System.setOut(ps);
            Snapshot.sauvegarder(selectedDirectory.getAbsolutePath());
            System.out.flush();
            System.setOut(old);
            rightTextArea.setText(baos.toString());
    	}
    	else {
    		JOptionPane.showMessageDialog(this, "Veuillez sélectionner un répertoire d'abord.");
    		
    	}
    }
    /**
     * Compare un instantané sauvegardé avec le répertoire actuellement sélectionné.
     * 
     * L'utilisateur est invité à choisir un fichier snapshot via un sélecteur de fichiers. 
     * Si un répertoire est sélectionné, cette méthode redirige temporairement la sortie standard
     * pour capturer les messages générés par le processus de comparaison et les affiche dans 
     * le composant `rightTextArea`.
     * 
     * Si aucun répertoire n'est sélectionné, affiche un message demandant à l'utilisateur 
     * de sélectionner un répertoire.
     */
    public void snapshotCompare() {
        if (selectedDirectory != null) {
            // Choisir un répertoire fixe
        	File snapshotDirectory = new File(selectedDirectory, "snapshot");
            JFileChooser fileChooser = new JFileChooser(snapshotDirectory);
            int returnValue = fileChooser.showOpenDialog(this);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File snapsel = fileChooser.getSelectedFile();
                String Snap_path = snapsel.getAbsolutePath();
                rightTextArea.setText("");
        		ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(baos);
                PrintStream old = System.out;
                System.setOut(ps);
                System.out.println(Snap_path);
                System.out.println(selectedDirectory.getAbsolutePath());
                Snapshot.comparer(selectedDirectory.getAbsolutePath(), Snap_path);
                System.out.flush();
                System.setOut(old);
                rightTextArea.setText(baos.toString());    
            }
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un répertoire d'abord.");
        }
    }
    /**
     * Effectue une recherche de fichiers dans le répertoire sélectionné en fonction de critères définis par l'utilisateur.
     * Les critères disponibles incluent : date, hauteur, largeur, nom et dimensions.
     * L'utilisateur peut sélectionner jusqu'à trois critères simultanément
     * 
     * <p>Les critères possibles incluent :</p>
     * <ul>
     *   <li><b>Date :</b> Permet de filtrer par égalité, différence, infériorité ou supériorité d'une date donnée.</li>
     *   <li><b>Hauteur :</b> Permet de filtrer selon une hauteur spécifiée.</li>
     *   <li><b>Largeur :</b> Permet de filtrer selon une largeur spécifiée.</li>
     *   <li><b>Nom :</b> Permet de rechercher les fichiers par nom exact ou différent.</li>
     *   <li><b>Dimensions :</b> Permet de rechercher des fichiers selon leurs dimensions (largeur x hauteur).</li>
     * </ul>
     * 
     * <p>Les entrées invalides (comme une date, une hauteur ou une largeur incorrecte) affichent un avertissement à l'utilisateur.</p>
     */

    public void recherche() {
        if (selectedDirectory != null) {
            // Menu déroulant pour sélectionner le groupe de critères
            JLabel groupLabel = new JLabel("Sélectionnez un groupe de critères :");
            String[] groups = {"Date", "Hauteur", "Largeur", "Nom", "Dimension"};
            JComboBox<String> groupComboBox = new JComboBox<>(groups);

            // Limite des sélections
            final int maxSelections = 3;
            final int[] selectedCount = {0};

            // Fonction pour gérer la limite de sélection
            ItemListener listener = e -> {
                JCheckBox source = (JCheckBox) e.getSource();
                if (source.isSelected()) {
                    selectedCount[0]++;
                    if (selectedCount[0] > maxSelections) {
                        source.setSelected(false);
                        JOptionPane.showMessageDialog(
                            null,
                            "Vous ne pouvez sélectionner que " + maxSelections + " options.",
                            "Limite atteinte",
                            JOptionPane.WARNING_MESSAGE
                        );
                    }
                } else {
                    selectedCount[0]--;
                }
            };

            // Création des cases à cocher pour chaque groupe
            JCheckBox eqdate = new JCheckBox("Date égale");
            JCheckBox nedate = new JCheckBox("Date différente");
            JCheckBox ledate = new JCheckBox("Date inférieure ou égale");
            JCheckBox ltdate = new JCheckBox("Date strictement inférieure");
            JCheckBox gedate = new JCheckBox("Date supérieure ou égale");
            JCheckBox gtdate = new JCheckBox("Date strictement supérieure");

            JCheckBox eqhaut = new JCheckBox("Hauteur égale");
            JCheckBox nehaut = new JCheckBox("Hauteur différente");
            JCheckBox lehaut = new JCheckBox("Hauteur inférieure ou égale");
            JCheckBox lthaut = new JCheckBox("Hauteur strictement inférieure");
            JCheckBox gehaut = new JCheckBox("Hauteur supérieure ou égale");
            JCheckBox gthaut = new JCheckBox("Hauteur strictement supérieure");

            JCheckBox eqlarg = new JCheckBox("Largeur égale");
            JCheckBox nelarg = new JCheckBox("Largeur différente");
            JCheckBox lelarg = new JCheckBox("Largeur inférieure ou égale");
            JCheckBox ltlarg = new JCheckBox("Largeur strictement inférieure");
            JCheckBox gelarg = new JCheckBox("Largeur supérieure ou égale");
            JCheckBox gtlarg = new JCheckBox("Largeur strictement supérieure");

            JCheckBox eqname = new JCheckBox("Nom égal");
            JCheckBox nename = new JCheckBox("Nom différent");

            JCheckBox eqdim = new JCheckBox("Dimension égale");
            JCheckBox nedim = new JCheckBox("Dimension différente");

            // Ajout du listener aux cases à cocher
            eqdate.addItemListener(listener);
            nedate.addItemListener(listener);
            ledate.addItemListener(listener);
            ltdate.addItemListener(listener);
            gedate.addItemListener(listener);
            gtdate.addItemListener(listener);

            eqhaut.addItemListener(listener);
            nehaut.addItemListener(listener);
            lehaut.addItemListener(listener);
            lthaut.addItemListener(listener);
            gehaut.addItemListener(listener);
            gthaut.addItemListener(listener);

            eqlarg.addItemListener(listener);
            nelarg.addItemListener(listener);
            lelarg.addItemListener(listener);
            ltlarg.addItemListener(listener);
            gelarg.addItemListener(listener);
            gtlarg.addItemListener(listener);

            eqname.addItemListener(listener);
            nename.addItemListener(listener);

            eqdim.addItemListener(listener);
            nedim.addItemListener(listener);

            // Champs pour les valeurs associées
            JDateChooser dateChooser = new JDateChooser();
            dateChooser.setDateFormatString("dd/MM/yyyy");

            // Ajout d'un sélecteur pour l'heure
            SpinnerDateModel timeModel = new SpinnerDateModel();
            JSpinner timeSpinner = new JSpinner(timeModel);
            JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm:ss");
            timeSpinner.setEditor(timeEditor);
            timeSpinner.setValue(new Date()); // Initialiser à l'heure actuelle

            JTextField dimField = new JTextField(10); // Pour les dimensions
            JTextField nameField = new JTextField(10); // Pour les noms
            JTextField hautField = new JTextField(10);// Pour les hauteurs
            JTextField largField = new JTextField(10); // Pour les largeurs        

            // Panneaux pour chaque groupe
            JPanel datePanel = new JPanel();
            datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.Y_AXIS));
            datePanel.add(eqdate);
            datePanel.add(nedate);
            datePanel.add(ledate);
            datePanel.add(ltdate);
            datePanel.add(gedate);
            datePanel.add(gtdate);
            datePanel.add(new JLabel("Sélectionnez une date :"));
            datePanel.add(dateChooser);
            datePanel.add(new JLabel("Sélectionnez une heure :"));
            datePanel.add(timeSpinner);

            JPanel hauteurPanel = new JPanel();
            hauteurPanel.setLayout(new BoxLayout(hauteurPanel, BoxLayout.Y_AXIS));
            hauteurPanel.add(eqhaut);
            hauteurPanel.add(nehaut);
            hauteurPanel.add(lehaut);
            hauteurPanel.add(lthaut);
            hauteurPanel.add(gehaut);
            hauteurPanel.add(gthaut);
            hauteurPanel.add(new JLabel("Entrez une hauteur :"));
            hauteurPanel.add(hautField);

            JPanel largeurPanel = new JPanel();
            largeurPanel.setLayout(new BoxLayout(largeurPanel, BoxLayout.Y_AXIS));
            largeurPanel.add(eqlarg);
            largeurPanel.add(nelarg);
            largeurPanel.add(lelarg);
            largeurPanel.add(ltlarg);
            largeurPanel.add(gelarg);
            largeurPanel.add(gtlarg);
            largeurPanel.add(new JLabel("Entrez une largeur :"));
            largeurPanel.add(largField);

            JPanel nomPanel = new JPanel();
            nomPanel.setLayout(new BoxLayout(nomPanel, BoxLayout.Y_AXIS));
            nomPanel.add(eqname);
            nomPanel.add(nename);
            nomPanel.add(new JLabel("Entrez un nom :"));
            nomPanel.add(nameField);

            JPanel dimensionPanel = new JPanel();
            dimensionPanel.setLayout(new BoxLayout(dimensionPanel, BoxLayout.Y_AXIS));
            dimensionPanel.add(eqdim);
            dimensionPanel.add(nedim);
            dimensionPanel.add(new JLabel("Entrez une dimension (format : largeur x hauteur) :"));
            dimensionPanel.add(dimField);

            // Panneau dynamique
            JPanel dynamicPanel = new JPanel(new BorderLayout());
            dynamicPanel.add(datePanel, BorderLayout.CENTER); // Par défaut, affiche le panneau "Date"

            // Listener pour basculer entre les groupes
            groupComboBox.addActionListener(e -> {
                dynamicPanel.removeAll();
                String selectedGroup = (String) groupComboBox.getSelectedItem();
                switch (selectedGroup) {
                    case "Date":
                        dynamicPanel.add(datePanel, BorderLayout.CENTER);
                        break;
                    case "Hauteur":
                        dynamicPanel.add(hauteurPanel, BorderLayout.CENTER);
                        break;
                    case "Largeur":
                        dynamicPanel.add(largeurPanel, BorderLayout.CENTER);
                        break;
                    case "Nom":
                        dynamicPanel.add(nomPanel, BorderLayout.CENTER);
                        break;
                    case "Dimension":
                        dynamicPanel.add(dimensionPanel, BorderLayout.CENTER);
                        break;
                }
                dynamicPanel.revalidate();
                dynamicPanel.repaint();
            });

            // Organisation du panneau principal
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(groupLabel);
            mainPanel.add(groupComboBox);
            mainPanel.add(dynamicPanel);

            // Fenêtre de dialogue
            int result = JOptionPane.showConfirmDialog(
                this,
                mainPanel,
                "Options de recherche",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
            );

            if (result == JOptionPane.OK_OPTION) {
            	//récupération des saisis
            	Date selectedDate = dateChooser.getDate();
            	Date selectedTime = (Date) timeSpinner.getValue();
            	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            	SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            	String selectedDateStr = selectedDate != null ? dateFormat.format(selectedDate) : "";
            	String selectedTimeStr = selectedTime != null ? timeFormat.format(selectedTime) : "";
            	String date = selectedDateStr+" "+selectedTimeStr;
            	String dimension = dimField.getText();//récupération de la dim en supprimant les espaces
            	String name = nameField.getText().trim(); //récupération du nom en supprimant les espaces
            	String haut = hautField.getText();
            	String larg = largField.getText();
            	int largeur =larg(larg);
            	int hauteur = haut(haut);
            	
            	//récupération des cases selectionées
                boolean isNeDate = nedate.isSelected();
                boolean isLeDate = ledate.isSelected();
                boolean isLtDate = ltdate.isSelected();
                boolean isGeDate = gedate.isSelected();
                boolean isGtDate = gtdate.isSelected();
                boolean isEqDate = eqdate.isSelected();
                
                boolean isEqDim = eqdim.isSelected();
                boolean isNeDim = nedim.isSelected();
                
                boolean isEqName = eqname.isSelected();
                boolean isNeName = nename.isSelected();
                
                boolean isNeHaut = nehaut.isSelected();
                boolean isLeHaut = lehaut.isSelected();
                boolean isLtHaut = lthaut.isSelected();
                boolean isGtHaut = gthaut.isSelected();
                boolean isGeHaut = gehaut.isSelected();
                boolean isEqHaut = eqhaut.isSelected();
                
                boolean isNeLarg = nelarg.isSelected();
                boolean isLeLarg = lelarg.isSelected();
                boolean isLtLarg = ltlarg.isSelected();
                boolean isGtLarg = gtlarg.isSelected();
                boolean isGeLarg = gelarg.isSelected();
                boolean isEqLarg = eqlarg.isSelected();
                
                boolean DateV = isNeDate || isLeDate || isLtDate || isGeDate || isGtDate || isEqDate;
                boolean DimV = isEqDim || isNeDim;
                boolean HautV = isNeHaut || isLeHaut || isLtHaut || isGtHaut || isGeHaut || isEqHaut;
                boolean LargV = isNeLarg || isLeLarg || isLtLarg || isGtLarg || isGeLarg || isEqLarg;
                boolean NameV = isEqName || isNeName ;
                
                //céation d'un ArrayList pour les résultats
                ArrayList<File> resultat = new ArrayList<>();
                listerfichier(selectedDirectory,resultat);
                
                int dra =0;
                
                // Lancer la recherche basée sur les critères
                rightTextArea.setText("");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(baos);
                PrintStream old = System.out;
                System.setOut(ps);
                if (DateV) {
                	if (Repertoire.IsDate(date)) {
                		if (isNeDate) {
                			ArrayList<File> difdate = new ArrayList<>();
                			Repertoire.recherche_fichier_date_dif(selectedDirectory, date, difdate);
                			resultat= Repertoire.compare2(resultat, difdate);
                		}
                		if(isEqDate) {
                			ArrayList<File> egdate = new ArrayList<>();
                		
                			Repertoire.recherche_fichier_date(selectedDirectory, date, egdate);
                			resultat = Repertoire.compare2(resultat, egdate);
                		}
                		if(isLeDate) {
                			ArrayList<File> infegdate = new ArrayList<>();
                			Repertoire.recherche_fichier_date_inf_egale(selectedDirectory, date, infegdate);
                			resultat = Repertoire.compare2(resultat, infegdate);
                		}
                		if(isLtDate) {
                			ArrayList<File> infdate = new ArrayList<>();
                			Repertoire.recherche_fichier_date_inf(selectedDirectory, date, infdate);
                			resultat = Repertoire.compare2(resultat, infdate);
                			
                		}
                		if(isGtDate) {
                			ArrayList<File> supdate = new ArrayList<>();
                			Repertoire.recherche_fichier_date_sup(selectedDirectory, date, supdate);
                			resultat = Repertoire.compare2(resultat, supdate);
                			
                		}
                		if(isGeDate) {
                			ArrayList<File> supegdate = new ArrayList<>();
                			Repertoire.recherche_fichier_date_sup_egale(selectedDirectory, date, supegdate);
                			resultat = Repertoire.compare2(resultat, supegdate);
                			
                		}
                	}
                		else {
                			dra =1;
                			// Afficher un panneau d'avertissement si la date est invalide
                	        JOptionPane.showMessageDialog(null, 
                	            "Veuillez entrer une date valide.", 
                	            "Avertissement", 
                	            JOptionPane.WARNING_MESSAGE);               			
                		}
                }
                	
                		if (DimV) {
                			if(isDimension(dimension)) {
                				int l = getLargeur(dimension);
                				int h = getHauteur(dimension);
                				
                				if (isEqDim) {
                					ArrayList<File> eqdimt = new ArrayList<>();
                					Repertoire.recherche_fichier_dim(selectedDirectory,h,l, eqdimt);  // DimValue est la dimension entrée par l'utilisateur
                					resultat = Repertoire.compare2(resultat, eqdimt);
                				}
                				if (isNeDim) {
                					ArrayList<File> nedimt = new ArrayList<>();
                					Repertoire.recherche_fichier_dim_dif(selectedDirectory, h,l, nedimt);
                					resultat =Repertoire.compare2(resultat, nedimt);
                				}
                			}
                		    else {
                		    	dra =1;
                		    	// Afficher un panneau d'avertissement si la dim est invalide
                		        JOptionPane.showMessageDialog(null, 
                		            "Veuillez entrer une dimension valide.", 
                		            "Avertissement", 
                		            JOptionPane.WARNING_MESSAGE);
                		    }
                		}

                		if (HautV) {
                			if(hauteur!= -1) {
                				if (isEqHaut) {
                					ArrayList<File> eqhautt = new ArrayList<>();
                					Repertoire.recherche_fichier_hauteur_egale(selectedDirectory, hauteur, eqhautt);  // HauteurValue est la hauteur entrée par l'utilisateur
                					resultat = Repertoire.compare2(resultat, eqhautt);
                				}
                				if (isNeHaut) {
                					ArrayList<File> nehautt = new ArrayList<>();
                					Repertoire.recherche_fichier_hauteur_dif(selectedDirectory, hauteur, nehautt);
                					resultat =Repertoire.compare2(resultat, nehautt);
                				}
                				if (isLeHaut) {
                					ArrayList<File> lehautt = new ArrayList<>();
                					Repertoire.recherche_fichier_hauteur_inf_egale(selectedDirectory, hauteur, lehautt);
                					resultat = Repertoire.compare2(resultat, lehautt);
                				}
                				if (isLtHaut) {
                					ArrayList<File> lthautt = new ArrayList<>();
                					Repertoire.recherche_fichier_hauteur_inf(selectedDirectory, hauteur, lthautt);
                					resultat = Repertoire.compare2(resultat, lthautt);
                				}
                		    	if (isGtHaut) {
                		    		ArrayList<File> gthautt = new ArrayList<>();
                		    		Repertoire.recherche_fichier_hauteur_sup(selectedDirectory, hauteur, gthautt);
                		    		resultat = Repertoire.compare2(resultat, gthautt);
                		    	}
                		    	if (isGeHaut) {
                		    		ArrayList<File> gehautt = new ArrayList<>();
                		    		Repertoire.recherche_fichier_hauteur_sup_egale(selectedDirectory, hauteur, gehautt);
                		    		resultat = Repertoire.compare2(resultat, gehautt);
                		    	}
                			}
                			else {
                				dra =1;
                				// Afficher un panneau d'avertissement si la hateur est invalide
                		        JOptionPane.showMessageDialog(null, 
                		            "Veuillez entrer une hauteur valide.", 
                		            "Avertissement", 
                		            JOptionPane.WARNING_MESSAGE);
                		}
                		}

                		if (LargV) {
                			if (largeur !=-1 ){ 
                				if (isEqLarg) {
                					ArrayList<File> eqlargt = new ArrayList<>();
                					Repertoire.recherche_fichier_largeur_egale(selectedDirectory, largeur, eqlargt);  // LargeurValue est la largeur entrée par l'utilisateur
                					resultat = Repertoire.compare2(resultat, eqlargt);
                				}
                				if (isNeLarg) {
                					ArrayList<File> nelargt = new ArrayList<>();
                					Repertoire.recherche_fichier_largeur_dif(selectedDirectory, largeur, nelargt);
                					resultat = Repertoire.compare2(resultat, nelargt);
                				}
                				if (isLeLarg) {
                					ArrayList<File> lelargt = new ArrayList<>();
                					Repertoire.recherche_fichier_largeur_inf_egale(selectedDirectory, largeur, lelargt);
                					resultat = Repertoire.compare2(resultat, lelargt);
                				}
                				if (isLtLarg) {
                					ArrayList<File> ltlargt = new ArrayList<>();
                					Repertoire.recherche_fichier_largeur_inf(selectedDirectory, largeur, ltlargt);
                					Repertoire.compare2(resultat, ltlargt);
                				}
                				if (isGtLarg) {
                					ArrayList<File> gtlargt = new ArrayList<>();
                					Repertoire.recherche_fichier_largeur_sup(selectedDirectory, largeur, gtlargt);
                					resultat = Repertoire.compare2(resultat, gtlargt);
                				}
                				if (isGeLarg) {
                					ArrayList<File> gelargt = new ArrayList<>();
                					Repertoire.recherche_fichier_largeur_sup_egale(selectedDirectory, largeur, gelargt);
                					resultat = Repertoire.compare2(resultat, gelargt);
                				}
                			}
                			else {
                				dra =1;
                				// Afficher un panneau d'avertissement si la largeur est invalide
                		        JOptionPane.showMessageDialog(null, 
                		            "Veuillez entrer une largeur valide.", 
                		            "Avertissement", 
                		            JOptionPane.WARNING_MESSAGE);
                			}
                		}
                		if(NameV) {
                			if(isEqName) {
                				ArrayList<File> eqnamet = new ArrayList<>();
                				Repertoire.recherche_fichier_nom(selectedDirectory,name,eqnamet);
                				resultat = Repertoire.compare2(resultat, eqnamet);
                				
                			}
                			if(isNeName) {
                				ArrayList<File> nenamet = new ArrayList<>();
                				Repertoire.recherche_fichier_nom_dif(selectedDirectory,name,nenamet);
                				resultat = Repertoire.compare2(resultat, nenamet);
                				
                			}
                		}
 
                		if(dra == 1 || selectedCount[0] == 0) {
                			System.out.println("Erreur : Veuillez entrer des arguments valides");
                		}
                		else {
                			Repertoire.affichage(resultat);
                		}
                		
                		System.out.flush();
                        System.setOut(old);
                        rightTextArea.setText(baos.toString());
                        
                		
                }
                // Appel à une méthode dans votre class.e fonctionnelle pour exécuter la recherche
                // Exemple : Repertoire.recherche(selectedDirectory, selectedDate, dimension, name, ...);


            }
        else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un répertoire d'abord.");
        }
    }
    /**
     * Convertit une chaîne représentant une largeur en un entier.
     * Si la chaîne n'est pas valide, retourne -1.
     *
     * @param largeur La chaîne à convertir en entier.
     * @return La largeur en entier ou -1 en cas d'erreur de conversion.
     */
    public static int larg (String largeur) {
    	try {
    		int larg = Integer.parseInt(largeur);
    		return larg;
    	}catch (NumberFormatException e) {
    		return -1;
    	}
    }
    /**
     * Convertit une chaîne représentant une hauteur en un entier.
     * Si la chaîne n'est pas valide, retourne -1.
     *
     * @param hauteur La chaîne à convertir en entier.
     * @return La hauteur en entier ou -1 en cas d'erreur de conversion.
     */
    public static int haut (String hauteur) {
    	try {
    		int haut = Integer.parseInt(hauteur);
    		return haut;
    	}catch (NumberFormatException e) {
    		return -1;
    	}
    }
    /**
     * Vérifie si une chaîne est dans le format "nombre x nombre".
     *
     * @param dim La chaîne à vérifier.
     * @return {@code true} si la chaîne correspond au format "nombre x nombre", {@code false} sinon.
     */
public static boolean isDimension(String dim) {
	// Fonction qui vérifie que la chaîne est dans le format "nombre x nombre"
        // Expression régulière pour vérifier le format "nombre x nombre"
        String regex = "^\\d+\\s+x\\s+\\d+$";
        
        // Créer le Pattern
        Pattern pattern = Pattern.compile(regex);
        
        // Créer le Matcher
        Matcher matcher = pattern.matcher(dim);
        
        // Retourner vrai si la chaîne correspond à l'expression régulière
        return matcher.matches();
    }
/**
 * Extrait la largeur d'une chaîne de dimension au format "nombre x nombre".
 * Si le format est incorrect ou la conversion échoue, retourne -1.
 *
 * @param dim La chaîne contenant les dimensions.
 * @return La largeur extraite ou -1 en cas d'erreur.
 */
public static int getLargeur(String dim){
    // Diviser la chaîne en utilisant le séparateur " x "
    String[] parts = dim.split(" x ");
    
    if (parts.length == 2) {
        try {
            // Convertir la première partie (largeur) en entier
            int largeur = Integer.parseInt(parts[0].trim());  // La première partie est la largeur
            return largeur;  // Retourner la largeur
        } catch (NumberFormatException e) {
            System.out.println("Erreur de format pour la largeur.");
            return -1; // Retourne -1 en cas d'erreur de format
        }
    } else {
        System.out.println("Le format de la chaîne n'est pas valide. Il devrait être 'nombre x nombre'.");
        return -1; // Retourne -1 si le format est incorrect
    }
}
/**
 * Extrait la hauteur d'une chaîne de dimension au format "nombre x nombre".
 * Si le format est incorrect ou la conversion échoue, retourne -1.
 *
 * @param dim La chaîne contenant les dimensions.
 * @return La hauteur extraite ou -1 en cas d'erreur.
 */
public static int getHauteur(String dim){
    // Diviser la chaîne en utilisant le séparateur " x "
    String[] parts = dim.split(" x ");
    
    if (parts.length == 2) {
        try {
            // Convertir la première partie (largeur) en entier
            int hauteur = Integer.parseInt(parts[1].trim());  // La première partie est la largeur
            return hauteur;  // Retourner la largeur
        } catch (NumberFormatException e) {
            System.out.println("Erreur de format pour la largeur.");
            return -1; // Retourne -1 en cas d'erreur de format
        }
    } else {
        System.out.println("Le format de la chaîne n'est pas valide. Il devrait être 'nombre x nombre'.");
        return -1; // Retourne -1 si le format est incorrect
    }
}





    	
/**
 * Parcourt récursivement un répertoire pour lister tous les fichiers qu'il contient,
 * en ignorant les répertoires nommés "SnapShot".
 *
 * @param d Le répertoire de départ pour la recherche.
 * @param fileList La liste qui contiendra les fichiers trouvés.
 */
public static void listerfichier(File d,ArrayList<File> fileList) {
	File [] tabd = d.listFiles();
	int i = 0;
	while (i<tabd.length) {
		if(tabd[i].isDirectory()) {
			if(!(tabd[i].getName().contains("SnapShot"))) {
				listerfichier(tabd[i],fileList);
			}
		}
		else {
				fileList.add(tabd[i]);
			}
		i = i+1;
		}
	}

        
        

     

      

 
    /**
     * Méthode principale pour lancer l'application.
     * @param args Arguments de ligne de commande.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI().setVisible(true);
            }
        });
    }
}







