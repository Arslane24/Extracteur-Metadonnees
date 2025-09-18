package Console;
import fonctionnel.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
/**
 * Cette classe représente l'interface en ligne de commande (CLI) pour l'application.
 * Elle gère l'interprétation des arguments et les actions associées.
 */
public class CLI {
	/**
     * Point d'entrée du programme CLI.
     *
     * @param args Les arguments passés en ligne de commande.
     *             - Le premier argument doit être -f ou -d pour spécifier un fichier ou un répertoire.
     *             - Le second argument doit être le chemin vers le fichier ou répertoire.
     *             - Les arguments suivants peuvent être des options pour effectuer des opérations.
     */
	
	/**
     * Constructeur par défaut de la classe CLI.
     * Initialise une instance de CLI sans paramètres.
     */
    public CLI() {
        // Constructeur par défaut explicite
    }
    /**
     * Point d'entrée principal de l'application.
     * Cette méthode gère les arguments passés en ligne de commande et exécute les actions correspondantes.
     *
     * @param args les arguments passés via la ligne de commande
     */
    public static void main(String[] args) {
        // Gestion prioritaire de l'option -h ou --help
        if (args.length == 1 && (args[0].equals("-h") || args[0].equals("--help"))) {
            printHelp();
            return;
        }


        // Vérification minimale d'arguments
        if (args.length < 2) {
            System.out.println("Erreur : Paramètres insuffisants.");
            System.out.println("Utilisez -h ou --help pour afficher la liste des fonctionnalités.");
           return;
        }

        // Déterminer le type (-f pour fichier ou -d pour répertoire)
        String typeOption = args[0];
        if (!typeOption.equals("-f") && !typeOption.equals("-d")) {
            System.out.println("Erreur : Vous devez spécifier un type valide (-f pour fichier ou -d pour répertoire).");
            System.out.println("Utilisez -h ou --help pour afficher la liste des fonctionnalités.");
            return;
        }
        boolean isFileOption = typeOption.equals("-f") || typeOption.equals("--file");
        boolean isDirectoryOption = typeOption.equals("-d") || typeOption.equals("--directory");

        // Vérification du chemin (doit être le deuxième argument)
        String path = args[1];
        File fileOrDirectory = new File(path);
        String mimeType = null;

        try {
            mimeType = Files.probeContentType(fileOrDirectory.toPath());
        } catch (IOException e) {
            System.out.println("Erreur lors de la détection du type MIME: " + e.getMessage());
        }

        if (isFileOption && (!fileOrDirectory.exists() || !fileOrDirectory.isFile())) {
            System.out.println("Erreur : Le chemin spécifié n'est pas un fichier valide.");
            return;
        }

        // Avertissement si l'image a un type MIME d'image mais une extension incompatible
        if (isFileOption && Fichier.isImageMimeType(mimeType) && Fichier.isImageExtension(Fichier.getExtension(fileOrDirectory))) {
            if(!(Fichier.recupMyme(mimeType).equals(Fichier.getExtension(fileOrDirectory)))) {
                System.out.println("Avertissement : Le fichier a un type MIME d'image (" + mimeType + ") mais son extension (" + Fichier.getExtension(fileOrDirectory) + ") ne correspond pas.");
            }
        }

        

        if (isDirectoryOption && (!fileOrDirectory.exists() || !fileOrDirectory.isDirectory())) {
            System.out.println("Erreur : Le chemin spécifié n'est pas un répertoire valide.");
            return;
        }

        // Exécuter toutes les options spécifiées à partir du 3ème argument
        if (args.length < 3) {
            System.out.println("Erreur : Aucune option d'opération spécifiée.");
            System.out.println("Utilisez -h ou --help pour afficher la liste des fonctionnalités.");
            return;
        }

        for (int i = 2; i < args.length; i++) {
        	String operationOption = args[i];

        	switch (operationOption) {

        	    /**
        	     * Affiche les informations d'un fichier.
        	     * Cette option est valide uniquement pour un fichier (-f).
        	     */
        	    case "--i":
        	        if (isFileOption) {
        	            Fichier.afficherInformationsFichier(path);
        	        } else {
        	            System.out.println("Erreur : L'option --info est uniquement valable pour les fichiers (-f).");
        	        }
        	        break;

        	    /**
        	     * Affiche les informations d'un fichier.
        	     * Cette option est valide uniquement pour un fichier (-f).
        	     */
        	    case "--info":
        	        if (isFileOption) {
        	            Fichier.afficherInformationsFichier(path);
        	        } else {
        	            System.out.println("Erreur : L'option --info est uniquement valable pour les fichiers (-f).");
        	        }
        	        break;

        	    /**
        	     * Affiche les statistiques d'un répertoire.
        	     * Cette option est valide uniquement pour un répertoire (-d).
        	     */
        	    case "--stat":
        	        if (isDirectoryOption) {
        	            Repertoire.afficherStatistiques(path);
        	        } else {
        	            System.out.println("Erreur : L'option --stat est uniquement valable pour les répertoires (-d).");
        	        }
        	        break;

        	    /**
        	     * Liste les fichiers dans un répertoire.
        	     * Cette option est valide uniquement pour un répertoire (-d).
        	     */
        	    case "--list":
        	        if (isDirectoryOption) {
        	            Repertoire.listerFichiers(path);
        	        } else {
        	            System.out.println("Erreur : L'option --list est uniquement valable pour les répertoires (-d).");
        	        }
        	        break;

        	    /**
        	     * Extrait et affiche les métadonnées d'une image.
        	     * Cette option est valide uniquement pour un fichier image (-f).
        	     */
        	    case "--metadata":
        	        if (isFileOption) {
        	            File file = new File(path);
        	            Image image = new Image(file); // Crée une instance de la classe Image
        	            String metadata = image.ExtractAllMetadata(); // Récupère les métadonnées
        	            System.out.println(metadata); // Affiche les métadonnées
        	        } else {
        	            System.out.println("Erreur : L'option --metadata est uniquement valable pour les fichiers (-f).");
        	        }
        	        break;

        	    /**
        	     * Sauvegarde l'état actuel d'un répertoire sous forme de snapshot.
        	     * Cette option est valide uniquement pour un répertoire (-d).
        	     */
        	    case "--snapshotsave":
        	        if (isDirectoryOption) {
        	            Snapshot.sauvegarder(path);
        	        } else {
        	            System.out.println("Erreur : L'option --snapshotsave est uniquement valable pour un dossier");
        	        }
        	        break;

        	    /**
        	     * Compare un répertoire avec un snapshot spécifié.
        	     * Cette option est valide uniquement pour un répertoire (-d).
        	     * Le snapshot doit être présent dans le sous-répertoire "SnapShot" du chemin spécifié.
        	     * @param args[3] Le nom du snapshot à comparer.
        	     */
        	    case "--snapshotcompare":
        	        if (isDirectoryOption) {
        	            if (args.length >= 4) {
        	                String pathS = path + "/SnapShot/" + args[3];
        	                Snapshot.comparer(path, pathS);
        	            }
        	        } else {
        	            System.out.println("Erreur : L'option --snapshotcompare est uniquement valable pour un dossier");
        	        }
        	        i++;
        	        break;
        	        /**
        	         * Recherche et affiche les fichiers créés à une date spécifique dans un répertoire.
        	         * Cette option est valide uniquement pour un répertoire (-d).
        	         * 
        	         * - La recherche peut inclure des choix spécifiques (via `AllChoice`) pour filtrer les fichiers.
        	         * - Plusieurs variantes sont gérées selon les arguments passés :
        	         *    - Comparaison entre deux groupes de fichiers et une date.
        	         *    - Filtrage d'un groupe de fichiers selon une date.
        	         *    - Recherche directe selon une date.
        	         * 
        	         * @param args Les arguments passés en ligne de commande :
        	         *   - args[3] et args[4] peuvent représenter des choix ou des parties d'une date.
        	         *   - args[5] et args[6] peuvent compléter une date si applicable.
        	         * @param fileOrDirectory Le répertoire dans lequel effectuer la recherche.
        	         * @throws IllegalArgumentException Si les arguments fournis sont invalides ou mal formatés.
        	         */
        	        case "-eqdate":
        	            if (isDirectoryOption) {
        	                int j = 2;

        	                // Cas : Deux choix suivis d'une date
        	                if (args.length >= 9 && AllChoice(args[3]) && AllChoice(args[4])) {
        	                    ArrayList<File> Choix1 = new ArrayList<>();
        	                    ArrayList<File> Choix2 = new ArrayList<>();
        	                    j = activeChoix(args[3], j + 2, args, fileOrDirectory, Choix1);
        	                    j = activeChoix(args[4], j, args, fileOrDirectory, Choix2);
        	                    i += j - 1;

        	                    String fd = args[5];
        	                    String sd = args[6];
        	                    String date = fd + " " + sd;

        	                    if (Repertoire.IsDate(date)) {
        	                        ArrayList<File> eqdateList = new ArrayList<>();
        	                        Repertoire.recherche_fichier_date(fileOrDirectory, date, eqdateList);
        	                        ArrayList<File> Final = new ArrayList<>();
        	                        Final = Repertoire.compare3(Choix1, Choix2, eqdateList);
        	                        System.out.println("Liste des fichiers égaux à la date de création " + date + " :");
        	                        Repertoire.affichage(Final);
        	                    } else {
        	                        System.out.println("Erreur : Veuillez entrer une date valide");
        	                    }

        	                // Cas : Un choix suivi d'une date
        	                } else if (args.length >= 7 && AllChoice(args[3]) && !AllChoice(args[4])) {
        	                    ArrayList<File> Choix1 = new ArrayList<>();
        	                    j = activeChoix(args[3], j + 1, args, fileOrDirectory, Choix1);
        	                    i += j - 1;
        	                    System.out.println(i);

        	                    String fd = args[4];
        	                    String sd = args[5];
        	                    String date = fd + " " + sd;

        	                    if (Repertoire.IsDate(date)) {
        	                        ArrayList<File> eqdateList = new ArrayList<>();
        	                        Repertoire.recherche_fichier_date(fileOrDirectory, date, eqdateList);
        	                        ArrayList<File> Final = new ArrayList<>();
        	                        Final = Repertoire.compare2(Choix1, eqdateList);
        	                        System.out.println("Liste des fichiers égaux à la date de création " + date + " :");
        	                        Repertoire.affichage(Final);
        	                    } else {
        	                        System.out.println("Erreur : Veuillez entrer une date valide");
        	                    }

        	                // Cas : Recherche directe avec une date
        	                } else if (args.length >= 5 && !AllChoice(args[3])) {
        	                    String fd = args[3];
        	                    String sd = args[4];
        	                    String date = fd + " " + sd;

        	                    if (Repertoire.IsDate(date)) {
        	                        ArrayList<File> eqdateList = new ArrayList<>();
        	                        Repertoire.recherche_fichier_date(fileOrDirectory, date, eqdateList);
        	                        System.out.println("Liste des fichiers égaux à la date de création " + date + " :");
        	                        Repertoire.affichage(eqdateList);
        	                    } else {
        	                        System.out.println("Erreur : Veuillez entrer une date valide");
        	                    }

        	                // Cas : Arguments invalides
        	                } else {
        	                    System.out.println("Erreur : Veuillez entrer des arguments valides");
        	                }

        	            } else {
        	                System.out.println("Erreur : L'option -eqdate est uniquement valable pour un dossier");
        	            }

        	            i += 2;
        	            break;
        	            /**
        	             * Gestion des options de filtre par date pour les fichiers dans un répertoire.
        	             * Les options incluent -gedate, -gtdate, et -ledate, permettant de rechercher
        	             * des fichiers en fonction de leur date de création.
        	             */

        	            // Traitement des différentes options liées aux dates
        	            case "-gedate":
        	                // Vérifie si l'option est appliquée à un répertoire
        	                if (isDirectoryOption) {
        	                    int j = 2;
        	                    
        	                    // Cas où deux choix sont spécifiés
        	                    if (args.length >= 9 && AllChoice(args[3]) && AllChoice(args[4])) {
        	                        ArrayList<File> Choix1 = new ArrayList<>();
        	                        ArrayList<File> Choix2 = new ArrayList<>();
        	                        j = activeChoix(args[3], j + 2, args, fileOrDirectory, Choix1);
        	                        j = activeChoix(args[4], j, args, fileOrDirectory, Choix2);
        	                        i += j - 1;
        	                        
        	                        String fd = args[5];
        	                        String sd = args[6];
        	                        String date = fd + " " + sd;

        	                        // Vérifie si la date est valide
        	                        if (Repertoire.IsDate(date)) {
        	                            ArrayList<File> gedateList = new ArrayList<>();  
        	                            Repertoire.recherche_fichier_date_sup_egale(fileOrDirectory, date, gedateList);
        	                            ArrayList<File> Final = new ArrayList<>();
        	                            Final = Repertoire.compare3(Choix1, Choix2, gedateList);
        	                            System.out.println("Liste des fichiers supérieurs ou égaux à la date de création " + date + " :");
        	                            Repertoire.affichage(Final);
        	                        } else {
        	                            System.out.println("Erreur : Veuillez entrer une date valide");
        	                        }
        	                    } 
        	                    // Cas où un seul choix est spécifié
        	                    else if (args.length >= 7 && AllChoice(args[3]) && !AllChoice(args[4])) {
        	                        ArrayList<File> Choix1 = new ArrayList<>(); 
        	                        j = activeChoix(args[3], j + 1, args, fileOrDirectory, Choix1);
        	                        i += j - 2;
        	                        
        	                        String fd = args[4];
        	                        String sd = args[5];
        	                        String date = fd + " " + sd;

        	                        if (Repertoire.IsDate(date)) {
        	                            ArrayList<File> gedateList = new ArrayList<>();  
        	                            Repertoire.recherche_fichier_date_sup_egale(fileOrDirectory, date, gedateList);
        	                            ArrayList<File> Final = new ArrayList<>();
        	                            Final = Repertoire.compare2(Choix1, gedateList);
        	                            System.out.println("Liste des fichiers supérieurs ou égaux à la date de création " + date + " :");
        	                            Repertoire.affichage(Final);
        	                        } else {
        	                            System.out.println("Erreur : Veuillez entrer une date valide");
        	                        }
        	                    } 
        	                    // Cas sans choix
        	                    else if (args.length >= 5 && !AllChoice(args[3])) {
        	                        String fd = args[3];
        	                        String sd = args[4];
        	                        String date = fd + " " + sd;

        	                        if (Repertoire.IsDate(date)) {
        	                            ArrayList<File> gedateList = new ArrayList<>();                
        	                            Repertoire.recherche_fichier_date_sup_egale(fileOrDirectory, date, gedateList);
        	                            System.out.println("Liste des fichiers supérieurs ou égaux à la date de création " + date + " :");
        	                            Repertoire.affichage(gedateList);
        	                        } else {
        	                            System.out.println("Erreur : Veuillez entrer une date valide");
        	                        }
        	                    } else {
        	                        System.out.println("Erreur : Veuillez entrer des arguments valides");
        	                    }
        	                } else {
        	                    System.out.println("Erreur : L'option -gedate est uniquement valable pour un dossier");
        	                }
        	                i += 2;
        	                break;

        	            case "-gtdate":
        	                if (isDirectoryOption) {
        	                    int j = 2;

        	                    if (args.length >= 9 && AllChoice(args[3]) && AllChoice(args[4])) {
        	                        ArrayList<File> Choix1 = new ArrayList<>();
        	                        ArrayList<File> Choix2 = new ArrayList<>();
        	                        j = activeChoix(args[3], j + 2, args, fileOrDirectory, Choix1);
        	                        j = activeChoix(args[4], j, args, fileOrDirectory, Choix2);
        	                        i += j - 1;

        	                        String fd = args[5];
        	                        String sd = args[6];
        	                        String date = fd + " " + sd;

        	                        if (Repertoire.IsDate(date)) {
        	                            ArrayList<File> gedateList = new ArrayList<>();  
        	                            Repertoire.recherche_fichier_date_sup(fileOrDirectory, date, gedateList);
        	                            ArrayList<File> Final = new ArrayList<>();
        	                            Final = Repertoire.compare3(Choix1, Choix2, gedateList);
        	                            System.out.println("Liste des fichiers supérieurs à la date de création " + date + " :");
        	                            Repertoire.affichage(Final);
        	                        } else {
        	                            System.out.println("Erreur : Veuillez entrer une date valide");
        	                        }
        	                    } else if (args.length >= 7 && AllChoice(args[3]) && !AllChoice(args[4])) {
        	                        ArrayList<File> Choix1 = new ArrayList<>(); 
        	                        j = activeChoix(args[3], j + 1, args, fileOrDirectory, Choix1);
        	                        i += j - 2;

        	                        String fd = args[4];
        	                        String sd = args[5];
        	                        String date = fd + " " + sd;

        	                        if (Repertoire.IsDate(date)) {
        	                            ArrayList<File> gedateList = new ArrayList<>();  
        	                            Repertoire.recherche_fichier_date_sup(fileOrDirectory, date, gedateList);
        	                            ArrayList<File> Final = new ArrayList<>();
        	                            Final = Repertoire.compare2(Choix1, gedateList);
        	                            System.out.println("Liste des fichiers supérieurs à la date de création " + date + " :");
        	                            Repertoire.affichage(Final);
        	                        } else {
        	                            System.out.println("Erreur : Veuillez entrer une date valide");
        	                        }
        	                    } else if (args.length >= 5 && !AllChoice(args[3])) {
        	                        String fd = args[3];
        	                        String sd = args[4];
        	                        String date = fd + " " + sd;

        	                        if (Repertoire.IsDate(date)) {
        	                            ArrayList<File> gedateList = new ArrayList<>();                
        	                            Repertoire.recherche_fichier_date_sup(fileOrDirectory, date, gedateList);
        	                            System.out.println("Liste des fichiers supérieurs à la date de création " + date + " :");
        	                            Repertoire.affichage(gedateList);
        	                        } else {
        	                            System.out.println("Erreur : Veuillez entrer une date valide");
        	                        }
        	                    } else {
        	                        System.out.println("Erreur : Veuillez entrer des arguments valides");
        	                    }
        	                } else {
        	                    System.out.println("Erreur : L'option -gtdate est uniquement valable pour un dossier");
        	                }
        	                i += 2;
        	                break;

        	            case "-ledate":
        	                if (isDirectoryOption) {
        	                    int j = 2;

        	                    if (args.length >= 9 && AllChoice(args[3]) && AllChoice(args[4])) {
        	                        ArrayList<File> Choix1 = new ArrayList<>();
        	                        ArrayList<File> Choix2 = new ArrayList<>();
        	                        j = activeChoix(args[3], j + 2, args, fileOrDirectory, Choix1);
        	                        j = activeChoix(args[4], j, args, fileOrDirectory, Choix2);
        	                        i += j - 1;

        	                        String fd = args[5];
        	                        String sd = args[6];
        	                        String date = fd + " " + sd;

        	                        if (Repertoire.IsDate(date)) {
        	                            ArrayList<File> gedateList = new ArrayList<>();  
        	                            Repertoire.recherche_fichier_date_inf_egale(fileOrDirectory, date, gedateList);
        	                            ArrayList<File> Final = new ArrayList<>();
        	                            Final = Repertoire.compare3(Choix1, Choix2, gedateList);
        	                            System.out.println("Liste des fichiers inférieurs ou égaux à la date de création " + date + " :");
        	                            Repertoire.affichage(Final);
        	                        } else {
        	                            System.out.println("Erreur : Veuillez entrer une date valide");
        	                        }
        	                    } else if (args.length >= 7 && AllChoice(args[3]) && !AllChoice(args[4])) {
        	                        ArrayList<File> Choix1 = new ArrayList<>(); 
        	                        j = activeChoix(args[3], j + 1, args, fileOrDirectory, Choix1);
        	                        i += j - 2;

        	                        String fd = args[4];
        	                        String sd = args[5];
        	                        String date = fd + " " + sd;

        	                        if (Repertoire.IsDate(date)) {
        	                            ArrayList<File> gedateList = new ArrayList<>();  
        	                            Repertoire.recherche_fichier_date_inf_egale(fileOrDirectory, date, gedateList);
        	                            ArrayList<File> Final = new ArrayList<>();
        	                            Final = Repertoire.compare2(Choix1, gedateList);
        	                            System.out.println("Liste des fichiers inférieurs ou égaux à la date de création " + date + " :");
        	                            Repertoire.affichage(Final);
        	                        } else {
        	                            System.out.println("Erreur : Veuillez entrer une date valide");
        	                        }
        	                    } else if (args.length >= 5 && !AllChoice(args[3])) {
        	                        String fd = args[3];
        	                        String sd = args[4];
        	                        String date = fd + " " + sd;

        	                        if (Repertoire.IsDate(date)) {
        	                            ArrayList<File> gedateList = new ArrayList<>();                
        	                            Repertoire.recherche_fichier_date_inf_egale(fileOrDirectory, date, gedateList);
        	                            System.out.println("Liste des fichiers inférieurs ou égaux à la date de création " + date + " :");
        	                            Repertoire.affichage(gedateList);
        	                        } else {
        	                            System.out.println("Erreur : Veuillez entrer une date valide");
        	                        }
        	                    } else {
        	                        System.out.println("Erreur : Veuillez entrer des arguments valides");
        	                    }
        	                } else {
        	                    System.out.println("Erreur : L'option -ledate est uniquement valable pour un dossier");
        	                }
        	                i += 2;
        	                break;

        	                /**
        	                 * Option -ltdate : Permet de rechercher des fichiers dont la date de création est 
        	                 * antérieure à une date spécifiée. 
        	                 * @param args Les arguments fournis par l'utilisateur.
        	                 * @param isDirectoryOption Indique si l'option s'applique à un répertoire.
        	                 * @param fileOrDirectory Le fichier ou répertoire cible.
        	                 */
        	                case "-ltdate":
        	                    if (isDirectoryOption) {
        	                        int j = 2;
        	                        if (args.length >= 9 && AllChoice(args[3]) && AllChoice(args[4])) {
        	                            ArrayList<File> Choix1 = new ArrayList<>();
        	                            ArrayList<File> Choix2 = new ArrayList<>();
        	                            j = activeChoix(args[3], j + 2, args, fileOrDirectory, Choix1);
        	                            j = activeChoix(args[4], j, args, fileOrDirectory, Choix2);
        	                            i += j - 1;
        	                            String fd = args[5];
        	                            String sd = args[6];
        	                            String date = fd + " " + sd;
        	                            if (Repertoire.IsDate(date)) {
        	                                ArrayList<File> gedateList = new ArrayList<>();
        	                                Repertoire.recherche_fichier_date_inf(fileOrDirectory, date, gedateList);
        	                                ArrayList<File> Final = Repertoire.compare3(Choix1, Choix2, gedateList);
        	                                System.out.println("Liste des fichiers inférieurs à la date de création " + date + " :");
        	                                Repertoire.affichage(Final);
        	                            } else {
        	                                System.out.println("Erreur : Veuillez entrer une date valide");
        	                            }
        	                        } else if (args.length >= 7 && AllChoice(args[3]) && !AllChoice(args[4])) {
        	                            ArrayList<File> Choix1 = new ArrayList<>();
        	                            j = activeChoix(args[3], j + 1, args, fileOrDirectory, Choix1);
        	                            i += j - 2;
        	                            String fd = args[4];
        	                            String sd = args[5];
        	                            String date = fd + " " + sd;
        	                            if (Repertoire.IsDate(date)) {
        	                                ArrayList<File> gedateList = new ArrayList<>();
        	                                Repertoire.recherche_fichier_date_inf(fileOrDirectory, date, gedateList);
        	                                ArrayList<File> Final = Repertoire.compare2(Choix1, gedateList);
        	                                System.out.println("Liste des fichiers inférieurs à la date de création " + date + " :");
        	                                Repertoire.affichage(Final);
        	                            } else {
        	                                System.out.println("Erreur : Veuillez entrer une date valide");
        	                            }
        	                        } else if (args.length >= 5 && !AllChoice(args[3])) {
        	                            String fd = args[3];
        	                            String sd = args[4];
        	                            String date = fd + " " + sd;
        	                            if (Repertoire.IsDate(date)) {
        	                                ArrayList<File> gedateList = new ArrayList<>();
        	                                Repertoire.recherche_fichier_date_inf(fileOrDirectory, date, gedateList);
        	                                System.out.println("Liste des fichiers inférieurs à la date de création " + date + " :");
        	                                Repertoire.affichage(gedateList);
        	                            } else {
        	                                System.out.println("Erreur : Veuillez entrer une date valide");
        	                            }
        	                        } else {
        	                            System.out.println("Erreur : Veuillez entrer des arguments valides");
        	                        }
        	                    } else {
        	                        System.out.println("Erreur : L'option -ltdate est uniquement valable pour un dossier");
        	                    }
        	                    i += 2;
        	                    break;

        	                /**
        	                 * Option -nedate : Permet de rechercher des fichiers dont la date de création 
        	                 * est différente d'une date spécifiée.
        	                 * @param args Les arguments fournis par l'utilisateur.
        	                 * @param isDirectoryOption Indique si l'option s'applique à un répertoire.
        	                 * @param fileOrDirectory Le fichier ou répertoire cible.
        	                 */
        	                case "-nedate":
        	                    if (isDirectoryOption) {
        	                        int j = 2;
        	                        if (args.length >= 9 && AllChoice(args[3]) && AllChoice(args[4])) {
        	                            ArrayList<File> Choix1 = new ArrayList<>();
        	                            ArrayList<File> Choix2 = new ArrayList<>();
        	                            j = activeChoix(args[3], j + 2, args, fileOrDirectory, Choix1);
        	                            j = activeChoix(args[4], j, args, fileOrDirectory, Choix2);
        	                            i += j - 1;
        	                            String fd = args[5];
        	                            String sd = args[6];
        	                            String date = fd + " " + sd;
        	                            if (Repertoire.IsDate(date)) {
        	                                ArrayList<File> gedateList = new ArrayList<>();
        	                                Repertoire.recherche_fichier_date_dif(fileOrDirectory, date, gedateList);
        	                                ArrayList<File> Final = Repertoire.compare3(Choix1, Choix2, gedateList);
        	                                System.out.println("Liste des fichiers différents de la date de création " + date + " :");
        	                                Repertoire.affichage(Final);
        	                            } else {
        	                                System.out.println("Erreur : Veuillez entrer une date valide");
        	                            }
        	                        } else if (args.length >= 7 && AllChoice(args[3]) && !AllChoice(args[4])) {
        	                            ArrayList<File> Choix1 = new ArrayList<>();
        	                            j = activeChoix(args[3], j + 1, args, fileOrDirectory, Choix1);
        	                            i += j - 2;
        	                            String fd = args[4];
        	                            String sd = args[5];
        	                            String date = fd + " " + sd;
        	                            if (Repertoire.IsDate(date)) {
        	                                ArrayList<File> gedateList = new ArrayList<>();
        	                                Repertoire.recherche_fichier_date_dif(fileOrDirectory, date, gedateList);
        	                                ArrayList<File> Final = Repertoire.compare2(Choix1, gedateList);
        	                                System.out.println("Liste des fichiers différents de la date de création " + date + " :");
        	                                Repertoire.affichage(Final);
        	                            } else {
        	                                System.out.println("Erreur : Veuillez entrer une date valide");
        	                            }
        	                        } else if (args.length >= 5 && !AllChoice(args[3])) {
        	                            String fd = args[3];
        	                            String sd = args[4];
        	                            String date = fd + " " + sd;
        	                            if (Repertoire.IsDate(date)) {
        	                                ArrayList<File> gedateList = new ArrayList<>();
        	                                Repertoire.recherche_fichier_date_dif(fileOrDirectory, date, gedateList);
        	                                System.out.println("Liste des fichiers différents de la date de création " + date + " :");
        	                                Repertoire.affichage(gedateList);
        	                            } else {
        	                                System.out.println("Erreur : Veuillez entrer une date valide");
        	                            }
        	                        } else {
        	                            System.out.println("Erreur : Veuillez entrer des arguments valides");
        	                        }
        	                    } else {
        	                        System.out.println("Erreur : L'option -nedate est uniquement valable pour un dossier");
        	                    }
        	                    i += 2;
        	                    break;
             
                	
        	                    /**
        	                     * Gère l'option -eqdim qui permet de rechercher des fichiers ayant des dimensions 
        	                     * spécifiques dans un répertoire donné.
        	                     * @param args Les arguments passés en ligne de commande.
        	                     * @param isDirectoryOption Indique si l'option est appliquée sur un répertoire.
        	                     * @param fileOrDirectory Le fichier ou répertoire cible.
        	                     * @return Affiche les fichiers correspondant aux dimensions spécifiées.
        	                     */
        	                    case "-eqdim":
        	                        if (isDirectoryOption) {
        	                            int j = 3;
        	                            // Cas où deux choix sont spécifiés avec une dimension
        	                            if (args.length >= 10 && AllChoice(args[3]) && AllChoice(args[4])) {
        	                                ArrayList<File> Choix1 = new ArrayList<>();
        	                                ArrayList<File> Choix2 = new ArrayList<>();
        	                                j = activeChoix(args[3], j + 2, args, fileOrDirectory, Choix1);
        	                                j = activeChoix(args[4], j, args, fileOrDirectory, Choix2);
        	                                i += j - 1;
        	                                if (args[6].equals("x")) {
        	                                    int largeur = Fichier.larg(args[5]);
        	                                    int haut = Fichier.haut(args[7]);
        	                                    if (haut != -1 && largeur != -1) {
        	                                        ArrayList<File> eqdim = new ArrayList<>();
        	                                        Repertoire.recherche_fichier_dim(fileOrDirectory, haut, largeur, eqdim);
        	                                        ArrayList<File> Final = Repertoire.compare3(Choix1, Choix2, eqdim);
        	                                        System.out.println("Liste des fichiers égaux à la dimension " + largeur + " x " + haut);
        	                                        Repertoire.affichage(Final);
        	                                    }
        	                                } else {
        	                                    System.out.println("Erreur : veuillez entrer une dimension");
        	                                }
        	                            } 
        	                            // Cas où un choix est spécifié avec une dimension
        	                            else if (args.length >= 8 && AllChoice(args[3]) && !AllChoice(args[4])) {
        	                                ArrayList<File> Choix1 = new ArrayList<>();
        	                                j = activeChoix(args[3], j + 1, args, fileOrDirectory, Choix1);
        	                                i += j - 1;
        	                                if (args[5].equals("x")) {
        	                                    int largeur = Fichier.larg(args[4]);
        	                                    int haut = Fichier.haut(args[6]);
        	                                    if (haut != -1 && largeur != -1) {
        	                                        ArrayList<File> eqdim = new ArrayList<>();
        	                                        Repertoire.recherche_fichier_dim(fileOrDirectory, haut, largeur, eqdim);
        	                                        ArrayList<File> Final = Repertoire.compare2(Choix1, eqdim);
        	                                        System.out.println("Liste des fichiers égaux à la dimension " + largeur + " x " + haut);
        	                                        Repertoire.affichage(Final);
        	                                    }
        	                                } else {
        	                                    System.out.println("Erreur : veuillez entrer une dimension");
        	                                }
        	                            } 
        	                            // Cas où seule une dimension est spécifiée
        	                            else if (args.length >= 6 && !AllChoice(args[3])) {
        	                                if (args[4].equals("x")) {
        	                                    int largeur = Fichier.larg(args[3]);
        	                                    int haut = Fichier.haut(args[5]);
        	                                    if (haut != -1 && largeur != -1) {
        	                                        ArrayList<File> eqdim = new ArrayList<>();
        	                                        Repertoire.recherche_fichier_dim(fileOrDirectory, haut, largeur, eqdim);
        	                                        System.out.println("Liste des fichiers égaux à la dimension " + largeur + " x " + haut);
        	                                        Repertoire.affichage(eqdim);
        	                                    }
        	                                } else {
        	                                    System.out.println("Erreur : veuillez entrer une dimension");
        	                                }
        	                            } else {
        	                                System.out.println("Erreur : Veuillez entrer des arguments valides");
        	                            }
        	                        } else {
        	                            System.out.println("Erreur : L'option -eqdim est uniquement valable pour un dossier");
        	                        }
        	                        i = i + 3;
        	                        break;

        	                    /**
        	                     * Gère l'option -nedim qui permet de rechercher des fichiers ayant des dimensions 
        	                     * différentes d'une dimension spécifiée dans un répertoire donné.
        	                     * @param args Les arguments passés en ligne de commande.
        	                     * @param isDirectoryOption Indique si l'option est appliquée sur un répertoire.
        	                     * @param fileOrDirectory Le fichier ou répertoire cible.
        	                     * @return Affiche les fichiers ayant des dimensions différentes de celles spécifiées.
        	                     */
        	                    case "-nedim":
        	                        if (isDirectoryOption) {
        	                            int j = 3;
        	                            // Cas où deux choix sont spécifiés avec une dimension
        	                            if (args.length >= 10 && AllChoice(args[3]) && AllChoice(args[4])) {
        	                                ArrayList<File> Choix1 = new ArrayList<>();
        	                                ArrayList<File> Choix2 = new ArrayList<>();
        	                                j = activeChoix(args[3], j + 2, args, fileOrDirectory, Choix1);
        	                                j = activeChoix(args[4], j, args, fileOrDirectory, Choix2);
        	                                i += j - 1;
        	                                if (args[6].equals("x")) {
        	                                    int largeur = Fichier.larg(args[5]);
        	                                    int haut = Fichier.haut(args[7]);
        	                                    if (haut != -1 && largeur != -1) {
        	                                        ArrayList<File> nedim = new ArrayList<>();
        	                                        Repertoire.recherche_fichier_dim_dif(fileOrDirectory, haut, largeur, nedim);
        	                                        ArrayList<File> Final = Repertoire.compare3(Choix1, Choix2, nedim);
        	                                        System.out.println("Liste des fichiers différents à la dimension " + largeur + " x " + haut);
        	                                        Repertoire.affichage(Final);
        	                                    }
        	                                } else {
        	                                    System.out.println("Erreur : veuillez entrer une dimension");
        	                                }
        	                            } 
        	                            // Cas où un choix est spécifié avec une dimension
        	                            else if (args.length >= 8 && AllChoice(args[3]) && !AllChoice(args[4])) {
        	                                ArrayList<File> Choix1 = new ArrayList<>();
        	                                j = activeChoix(args[3], j + 1, args, fileOrDirectory, Choix1);
        	                                i += j - 1;
        	                                if (args[5].equals("x")) {
        	                                    int largeur = Fichier.larg(args[4]);
        	                                    int haut = Fichier.haut(args[6]);
        	                                    if (haut != -1 && largeur != -1) {
        	                                        ArrayList<File> nedim = new ArrayList<>();
        	                                        Repertoire.recherche_fichier_dim_dif(fileOrDirectory, haut, largeur, nedim);
        	                                        ArrayList<File> Final = Repertoire.compare2(Choix1, nedim);
        	                                        System.out.println("Liste des fichiers différents à la dimension " + largeur + " x " + haut);
        	                                        Repertoire.affichage(Final);
        	                                    }
        	                                } else {
        	                                    System.out.println("Erreur : veuillez entrer une dimension");
        	                                }
        	                            } 
        	                            // Cas où seule une dimension est spécifiée
        	                            else if (args.length >= 6 && !AllChoice(args[3])) {
        	                                if (args[4].equals("x")) {
        	                                    int largeur = Fichier.larg(args[3]);
        	                                    int haut = Fichier.haut(args[5]);
        	                                    if (haut != -1 && largeur != -1) {
        	                                        ArrayList<File> nedim = new ArrayList<>();
        	                                        Repertoire.recherche_fichier_dim_dif(fileOrDirectory, haut, largeur, nedim);
        	                                        System.out.println("Liste des fichiers différents à la dimension " + largeur + " x " + haut);
        	                                        Repertoire.affichage(nedim);
        	                                    }
        	                                } else {
        	                                    System.out.println("Erreur : veuillez entrer une dimension");
        	                                }
        	                            } else {
        	                                System.out.println("Erreur : Veuillez entrer des arguments valides");
        	                            }
        	                        } else {
        	                            System.out.println("Erreur : L'option -nedim est uniquement valable pour un dossier");
        	                        }
        	                        i = i + 3;
        	                        break;

                		
        	                        /**
        	                         * Trouve les fichiers dont le nom correspond exactement à un nom donné (-eqname).
        	                         * @param isDirectoryOption Indique si la cible est un répertoire.
        	                         * @param args Les arguments fournis par l'utilisateur.
        	                         * @param fileOrDirectory Le fichier ou répertoire cible de l'opération.
        	                         */
        	                        case "-eqname":
        	                            if (isDirectoryOption) {
        	                                int j = 1;
        	                                if (args.length >= 8 && AllChoice(args[3]) && AllChoice(args[4])) {
        	                                    ArrayList<File> Choix1 = new ArrayList<>();
        	                                    ArrayList<File> Choix2 = new ArrayList<>();
        	                                    j = activeChoix(args[3], j + 2, args, fileOrDirectory, Choix1);
        	                                    j = activeChoix(args[4], j, args, fileOrDirectory, Choix2);
        	                                    i += j - 1;
        	                                    ArrayList<File> eqname = new ArrayList<>();
        	                                    ArrayList<File> Final = new ArrayList<>();
        	                                    Repertoire.recherche_fichier_nom(fileOrDirectory, args[5], eqname);
        	                                    Final = Repertoire.compare3(Choix1, Choix2, eqname);
        	                                    System.out.println("Liste des fichiers dont le nom est : " + args[5]);
        	                                    Repertoire.affichage(Final);
        	                                } else if (args.length >= 6 && AllChoice(args[3]) && !AllChoice(args[4])) {
        	                                    ArrayList<File> Choix1 = new ArrayList<>();
        	                                    j = activeChoix(args[3], j + 1, args, fileOrDirectory, Choix1);
        	                                    i += j - 1;
        	                                    ArrayList<File> eqname = new ArrayList<>();
        	                                    ArrayList<File> Final = new ArrayList<>();
        	                                    Repertoire.recherche_fichier_nom(fileOrDirectory, args[4], eqname);
        	                                    Final = Repertoire.compare2(Choix1, eqname);
        	                                    System.out.println("Liste des fichiers dont le nom est : " + args[4]);
        	                                    Repertoire.affichage(Final);
        	                                } else if (args.length >= 4 && !AllChoice(args[3])) {
        	                                    ArrayList<File> eqname = new ArrayList<>();
        	                                    Repertoire.recherche_fichier_nom(fileOrDirectory, args[3], eqname);
        	                                    System.out.println("Liste des fichiers dont le nom est : " + args[3]);
        	                                    Repertoire.affichage(eqname);
        	                                } else {
        	                                    System.out.println("Erreur : Veuillez entrer des arguments valides.");
        	                                }
        	                            } else {
        	                                System.out.println("Erreur : L'option -eqname est uniquement valable pour un dossier.");
        	                            }
        	                            i++;
        	                            break;

        	                        /**
        	                         * Trouve les fichiers dont le nom est différent d'un nom donné (-nename).
        	                         * @param isDirectoryOption Indique si la cible est un répertoire.
        	                         * @param args Les arguments fournis par l'utilisateur.
        	                         * @param fileOrDirectory Le fichier ou répertoire cible de l'opération.
        	                         */
        	                        case "-nename":
        	                            if (isDirectoryOption) {
        	                                int j = 1;
        	                                if (args.length >= 8 && AllChoice(args[3]) && AllChoice(args[4])) {
        	                                    ArrayList<File> Choix1 = new ArrayList<>();
        	                                    ArrayList<File> Choix2 = new ArrayList<>();
        	                                    j = activeChoix(args[3], j + 2, args, fileOrDirectory, Choix1);
        	                                    j = activeChoix(args[4], j, args, fileOrDirectory, Choix2);
        	                                    i += j - 1;
        	                                    ArrayList<File> nename = new ArrayList<>();
        	                                    ArrayList<File> Final = new ArrayList<>();
        	                                    Repertoire.recherche_fichier_nom_dif(fileOrDirectory, args[5], nename);
        	                                    Final = Repertoire.compare3(Choix1, Choix2, nename);
        	                                    System.out.println("Liste des fichiers dont le nom est différent de : " + args[5]);
        	                                    Repertoire.affichage(Final);
        	                                } else if (args.length >= 6 && AllChoice(args[3]) && !AllChoice(args[4])) {
        	                                    ArrayList<File> Choix1 = new ArrayList<>();
        	                                    j = activeChoix(args[3], j + 1, args, fileOrDirectory, Choix1);
        	                                    i += j - 1;
        	                                    ArrayList<File> nename = new ArrayList<>();
        	                                    ArrayList<File> Final = new ArrayList<>();
        	                                    Repertoire.recherche_fichier_nom_dif(fileOrDirectory, args[4], nename);
        	                                    Final = Repertoire.compare2(Choix1, nename);
        	                                    System.out.println("Liste des fichiers dont le nom est différent de : " + args[4]);
        	                                    Repertoire.affichage(Final);
        	                                } else if (args.length >= 4 && !AllChoice(args[3])) {
        	                                    ArrayList<File> nename = new ArrayList<>();
        	                                    Repertoire.recherche_fichier_nom_dif(fileOrDirectory, args[3], nename);
        	                                    System.out.println("Liste des fichiers dont le nom est différent de : " + args[3]);
        	                                    Repertoire.affichage(nename);
        	                                } else {
        	                                    System.out.println("Erreur : Veuillez entrer des arguments valides.");
        	                                }
        	                            } else {
        	                                System.out.println("Erreur : L'option -nename est uniquement valable pour un dossier.");
        	                            }
        	                            i++;
        	                            break;

        	                        /**
        	                         * Trouve les fichiers dont la hauteur est supérieure à une valeur donnée (-gthaut).
        	                         * @param isDirectoryOption Indique si la cible est un répertoire.
        	                         * @param args Les arguments fournis par l'utilisateur.
        	                         * @param fileOrDirectory Le fichier ou répertoire cible de l'opération.
        	                         */
        	                        case "-gthaut":
        	                            if (isDirectoryOption) {
        	                                int j = 1;
        	                                if (args.length >= 8 && AllChoice(args[3]) && AllChoice(args[4])) {
        	                                    int haut = Fichier.haut(args[5]);
        	                                    if (haut != -1) {
        	                                        ArrayList<File> gthaut = new ArrayList<>();
        	                                        ArrayList<File> Choix1 = new ArrayList<>();
        	                                        ArrayList<File> Choix2 = new ArrayList<>();
        	                                        ArrayList<File> Final = new ArrayList<>();
        	                                        j = activeChoix(args[3], j + 2, args, fileOrDirectory, Choix1);
        	                                        j = activeChoix(args[4], j, args, fileOrDirectory, Choix2);
        	                                        i += j - 1;
        	                                        Repertoire.recherche_fichier_hauteur_sup(fileOrDirectory, haut, gthaut);
        	                                        System.out.println("Liste des fichiers dont la hauteur est supérieure à : " + haut);
        	                                        Final = Repertoire.compare3(Choix1, Choix2, gthaut);
        	                                        Repertoire.affichage(Final);
        	                                    }
        	                                } else if (args.length >= 6 && AllChoice(args[3]) && !AllChoice(args[4])) {
        	                                    int haut = Fichier.haut(args[4]);
        	                                    if (haut != -1) {
        	                                        ArrayList<File> gthaut = new ArrayList<>();
        	                                        ArrayList<File> Choix1 = new ArrayList<>();
        	                                        ArrayList<File> Final = new ArrayList<>();
        	                                        j = activeChoix(args[3], j + 1, args, fileOrDirectory, Choix1);
        	                                        i += j - 1;
        	                                        Repertoire.recherche_fichier_hauteur_sup(fileOrDirectory, haut, gthaut);
        	                                        System.out.println("Liste des fichiers dont la hauteur est supérieure à : " + haut);
        	                                        Final = Repertoire.compare2(Choix1, gthaut);
        	                                        Repertoire.affichage(Final);
        	                                    }
        	                                } else if (args.length >= 4 && !AllChoice(args[3])) {
        	                                    int haut = Fichier.haut(args[3]);
        	                                    if (haut != -1) {
        	                                        ArrayList<File> gthaut = new ArrayList<>();
        	                                        Repertoire.recherche_fichier_hauteur_sup(fileOrDirectory, haut, gthaut);
        	                                        System.out.println("Liste des fichiers dont la hauteur est supérieure à : " + haut);
        	                                        Repertoire.affichage(gthaut);
        	                                    }
        	                                } else {
        	                                    System.out.println("Erreur : Veuillez entrer des arguments valides.");
        	                                }
        	                            } else {
        	                                System.out.println("Erreur : L'option -gthaut est uniquement valable pour un dossier.");
        	                            }
        	                            i++;
        	                            /**
        	                             * Cette méthode gère l'option `-gehaut`, qui permet de rechercher des fichiers dont la hauteur est supérieure ou égale à une certaine valeur.
        	                             * 
        	                             * @param isDirectoryOption Indique si l'option est appliquée à un dossier.
        	                             * @param args Les arguments passés par l'utilisateur.
        	                             * @param fileOrDirectory L'objet représentant le fichier ou le dossier.
        	                             */
        	                            case "-gehaut":
        	                                if (isDirectoryOption) {
        	                                    int j = 1;

        	                                    // Vérifie si suffisamment d'arguments sont fournis et si les choix sont valides
        	                                    if (args.length >= 8 && AllChoice(args[3]) && AllChoice(args[4])) {
        	                                        int haut = Fichier.haut(args[5]); // Calcule la hauteur spécifiée
        	                                        if (haut != -1) {
        	                                            ArrayList<File> gehaut = new ArrayList<>();
        	                                            ArrayList<File> Choix1 = new ArrayList<>();
        	                                            ArrayList<File> Choix2 = new ArrayList<>();
        	                                            ArrayList<File> Final = new ArrayList<>();
        	                                            j = activeChoix(args[3], j + 2, args, fileOrDirectory, Choix1);
        	                                            j = activeChoix(args[4], j, args, fileOrDirectory, Choix2);
        	                                            i += j - 1;

        	                                            // Recherche les fichiers correspondant au critère
        	                                            Repertoire.recherche_fichier_hauteur_sup_egale(fileOrDirectory, haut, gehaut);
        	                                            System.out.println("Liste des fichiers dont la hauteur est supérieure ou égale : " + haut);

        	                                            // Compare et affiche les résultats finaux
        	                                            Final = Repertoire.compare3(Choix1, Choix2, gehaut);
        	                                            Repertoire.affichage(Final);
        	                                        }
        	                                    } else if (args.length >= 6 && AllChoice(args[3]) && !AllChoice(args[4])) {
        	                                        int haut = Fichier.haut(args[4]);
        	                                        if (haut != -1) {
        	                                            ArrayList<File> gehaut = new ArrayList<>();
        	                                            ArrayList<File> Choix1 = new ArrayList<>();
        	                                            ArrayList<File> Final = new ArrayList<>();
        	                                            j = activeChoix(args[3], j + 1, args, fileOrDirectory, Choix1);
        	                                            i += j - 1;

        	                                            Repertoire.recherche_fichier_hauteur_sup_egale(fileOrDirectory, haut, gehaut);
        	                                            System.out.println("Liste des fichiers dont la hauteur est supérieure ou égale : " + haut);

        	                                            Final = Repertoire.compare2(Choix1, gehaut);
        	                                            Repertoire.affichage(Final);
        	                                        }
        	                                    } else if (args.length >= 4 && !AllChoice(args[3])) {
        	                                        int haut = Fichier.haut(args[3]);
        	                                        if (haut != -1) {
        	                                            ArrayList<File> gehaut = new ArrayList<>();
        	                                            Repertoire.recherche_fichier_hauteur_sup_egale(fileOrDirectory, haut, gehaut);
        	                                            System.out.println("Liste des fichiers dont la hauteur est supérieure ou égale : " + haut);
        	                                            Repertoire.affichage(gehaut);
        	                                        }
        	                                    } else {
        	                                        System.out.println("Erreur : Veuillez entrer des arguments valides");
        	                                    }
        	                                } else {
        	                                    System.out.println("Erreur : L'option -gehaut est uniquement valable pour un dossier");
        	                                }
        	                                i++;
        	                                break;

        	                            /**
        	                             * Cette méthode gère l'option `-lthaut`, qui permet de rechercher des fichiers dont la hauteur est inférieure à une certaine valeur.
        	                             *
        	                             * @param isDirectoryOption Indique si l'option est appliquée à un dossier.
        	                             * @param args Les arguments passés par l'utilisateur.
        	                             * @param fileOrDirectory L'objet représentant le fichier ou le dossier.
        	                             */
        	                            case "-lthaut":
        	                                if (isDirectoryOption) {
        	                                    int j = 1;

        	                                    // Vérifie si suffisamment d'arguments sont fournis et si les choix sont valides
        	                                    if (args.length >= 8 && AllChoice(args[3]) && AllChoice(args[4])) {
        	                                        int haut = Fichier.haut(args[5]); // Calcule la hauteur spécifiée
        	                                        if (haut != -1) {
        	                                            ArrayList<File> lthaut = new ArrayList<>();
        	                                            ArrayList<File> Choix1 = new ArrayList<>();
        	                                            ArrayList<File> Choix2 = new ArrayList<>();
        	                                            ArrayList<File> Final = new ArrayList<>();
        	                                            j = activeChoix(args[3], j + 2, args, fileOrDirectory, Choix1);
        	                                            j = activeChoix(args[4], j, args, fileOrDirectory, Choix2);
        	                                            i += j - 1;

        	                                            // Recherche les fichiers correspondant au critère
        	                                            Repertoire.recherche_fichier_hauteur_inf(fileOrDirectory, haut, lthaut);
        	                                            System.out.println("Liste des fichiers dont la hauteur est inférieure : " + haut);

        	                                            // Compare et affiche les résultats finaux
        	                                            Final = Repertoire.compare3(Choix1, Choix2, lthaut);
        	                                            Repertoire.affichage(Final);
        	                                        }
        	                                    } else if (args.length >= 6 && AllChoice(args[3]) && !AllChoice(args[4])) {
        	                                        int haut = Fichier.haut(args[4]);
        	                                        if (haut != -1) {
        	                                            ArrayList<File> lthaut = new ArrayList<>();
        	                                            ArrayList<File> Choix1 = new ArrayList<>();
        	                                            ArrayList<File> Final = new ArrayList<>();
        	                                            j = activeChoix(args[3], j + 1, args, fileOrDirectory, Choix1);
        	                                            i += j - 1;

        	                                            Repertoire.recherche_fichier_hauteur_inf(fileOrDirectory, haut, lthaut);
        	                                            System.out.println("Liste des fichiers dont la hauteur est inférieure : " + haut);

        	                                            Final = Repertoire.compare2(Choix1, lthaut);
        	                                            Repertoire.affichage(Final);
        	                                        }
        	                                    } else if (args.length >= 4 && !AllChoice(args[3])) {
        	                                        int haut = Fichier.haut(args[3]);
        	                                        if (haut != -1) {
        	                                            ArrayList<File> lthaut = new ArrayList<>();
        	                                            Repertoire.recherche_fichier_hauteur_inf(fileOrDirectory, haut, lthaut);
        	                                            System.out.println("Liste des fichiers dont la hauteur est inférieure : " + haut);
        	                                            Repertoire.affichage(lthaut);
        	                                        }
        	                                    } else {
        	                                        System.out.println("Erreur : Veuillez entrer des arguments valides");
        	                                    }
        	                                } else {
        	                                    System.out.println("Erreur : L'option -lthaut est uniquement valable pour un dossier");
        	                                }
        	                                i++;
        	                                break;

                	
        	                                /**
        	                                 * Traite l'option -eqhaut pour trouver les fichiers dont la hauteur est égale à une valeur spécifiée.
        	                                 * Cette option est uniquement valable pour un dossier.
        	                                 *
        	                                 * @param args les arguments passés en ligne de commande
        	                                 * @param fileOrDirectory le fichier ou le dossier cible
        	                                 * @param isDirectoryOption indique si l'option est appliquée sur un dossier
        	                                 */
        	                                case "-eqhaut":
        	                                    if (isDirectoryOption) {
        	                                        int j = 1;
        	                                        if (args.length >= 8 && AllChoice(args[3]) && AllChoice(args[4])) {
        	                                            int haut = Fichier.haut(args[5]); // Récupère la hauteur
        	                                            if (haut != -1) {
        	                                                ArrayList<File> eqhaut = new ArrayList<>();
        	                                                ArrayList<File> Choix1 = new ArrayList<>();
        	                                                ArrayList<File> Choix2 = new ArrayList<>();
        	                                                ArrayList<File> Final = new ArrayList<>();
        	                                                
        	                                                // Active les choix passés en arguments
        	                                                j = activeChoix(args[3], j + 2, args, fileOrDirectory, Choix1);
        	                                                j = activeChoix(args[4], j, args, fileOrDirectory, Choix2);
        	                                                i += j - 1;

        	                                                // Recherche les fichiers dont la hauteur est égale à la valeur spécifiée
        	                                                Repertoire.recherche_fichier_hauteur_egale(fileOrDirectory, haut, eqhaut);
        	                                                System.out.println("Liste des fichiers dont la hauteur est égale : " + haut);

        	                                                // Combine les résultats et les affiche
        	                                                Final = Repertoire.compare3(Choix1, Choix2, eqhaut);
        	                                                Repertoire.affichage(Final);
        	                                            }
        	                                        } else if (args.length >= 6 && AllChoice(args[3]) && !AllChoice(args[4])) {
        	                                            int haut = Fichier.haut(args[4]); // Récupère la hauteur
        	                                            if (haut != -1) {
        	                                                ArrayList<File> eqhaut = new ArrayList<>();
        	                                                ArrayList<File> Choix1 = new ArrayList<>();
        	                                                ArrayList<File> Final = new ArrayList<>();

        	                                                // Active un seul choix passé en argument
        	                                                j = activeChoix(args[3], j + 1, args, fileOrDirectory, Choix1);
        	                                                i += j - 1;

        	                                                // Recherche les fichiers dont la hauteur est égale à la valeur spécifiée
        	                                                Repertoire.recherche_fichier_hauteur_egale(fileOrDirectory, haut, eqhaut);
        	                                                System.out.println("Liste des fichiers dont la hauteur est égale : " + haut);

        	                                                // Combine les résultats et les affiche
        	                                                Final = Repertoire.compare2(Choix1, eqhaut);
        	                                                Repertoire.affichage(Final);
        	                                            }
        	                                        } else if (args.length >= 4 && !AllChoice(args[3])) {
        	                                            int haut = Fichier.haut(args[3]); // Récupère la hauteur
        	                                            if (haut != -1) {
        	                                                ArrayList<File> eqhaut = new ArrayList<>();

        	                                                // Recherche les fichiers dont la hauteur est égale à la valeur spécifiée
        	                                                Repertoire.recherche_fichier_hauteur_egale(fileOrDirectory, haut, eqhaut);
        	                                                System.out.println("Liste des fichiers dont la hauteur est égale : " + haut);

        	                                                // Affiche les résultats
        	                                                Repertoire.affichage(eqhaut);
        	                                            }
        	                                        } else {
        	                                            System.out.println("Erreur : Veuillez entrer des arguments valides");
        	                                        }
        	                                    } else {
        	                                        System.out.println("Erreur : L'option -eqhaut est uniquement valable pour un dossier");
        	                                    }
        	                                    i++;
        	                                    break;

        	                                /**
        	                                 * Traite l'option -lelarg pour trouver les fichiers dont la largeur est inférieure ou égale
        	                                 * à une valeur spécifiée. Cette option est uniquement valable pour un dossier.
        	                                 *
        	                                 * @param args les arguments passés en ligne de commande
        	                                 * @param fileOrDirectory le fichier ou le dossier cible
        	                                 * @param isDirectoryOption indique si l'option est appliquée sur un dossier
        	                                 */
        	                                case "-lelarg":
        	                                    if (isDirectoryOption) {
        	                                        int j = 1;
        	                                        if (args.length >= 8 && AllChoice(args[3]) && AllChoice(args[4])) {
        	                                            int larg = Fichier.larg(args[5]); // Récupère la largeur
        	                                            if (larg != -1) {
        	                                                ArrayList<File> lelarg = new ArrayList<>();
        	                                                ArrayList<File> Choix1 = new ArrayList<>();
        	                                                ArrayList<File> Choix2 = new ArrayList<>();
        	                                                ArrayList<File> Final = new ArrayList<>();

        	                                                // Active les choix passés en arguments
        	                                                j = activeChoix(args[3], j + 2, args, fileOrDirectory, Choix1);
        	                                                j = activeChoix(args[4], j, args, fileOrDirectory, Choix2);
        	                                                i += j - 1;

        	                                                // Recherche les fichiers dont la largeur est inférieure ou égale à la valeur spécifiée
        	                                                Repertoire.recherche_fichier_largeur_inf_egale(fileOrDirectory, larg, lelarg);
        	                                                System.out.println("Liste des fichiers dont la largeur est inférieure ou égale : " + larg);

        	                                                // Combine les résultats et les affiche
        	                                                Final = Repertoire.compare3(Choix1, Choix2, lelarg);
        	                                                Repertoire.affichage(Final);
        	                                            }
        	                                        } else if (args.length >= 6 && AllChoice(args[3]) && !AllChoice(args[4])) {
        	                                            int larg = Fichier.larg(args[4]); // Récupère la largeur
        	                                            if (larg != -1) {
        	                                                ArrayList<File> lelarg = new ArrayList<>();
        	                                                ArrayList<File> Choix1 = new ArrayList<>();
        	                                                ArrayList<File> Final = new ArrayList<>();

        	                                                // Active un seul choix passé en argument
        	                                                j = activeChoix(args[3], j + 1, args, fileOrDirectory, Choix1);
        	                                                i += j - 1;

        	                                                // Recherche les fichiers dont la largeur est inférieure ou égale à la valeur spécifiée
        	                                                Repertoire.recherche_fichier_largeur_inf_egale(fileOrDirectory, larg, lelarg);
        	                                                System.out.println("Liste des fichiers dont la largeur est inférieure ou égale : " + larg);

        	                                                // Combine les résultats et les affiche
        	                                                Final = Repertoire.compare2(Choix1, lelarg);
        	                                                Repertoire.affichage(Final);
        	                                            }
        	                                        } else if (args.length >= 4 && !AllChoice(args[3])) {
        	                                            int larg = Fichier.larg(args[3]); // Récupère la largeur
        	                                            if (larg != -1) {
        	                                                ArrayList<File> lelarg = new ArrayList<>();

        	                                                // Recherche les fichiers dont la largeur est inférieure ou égale à la valeur spécifiée
        	                                                Repertoire.recherche_fichier_largeur_inf_egale(fileOrDirectory, larg, lelarg);
        	                                                System.out.println("Liste des fichiers dont la largeur est inférieure ou égale : " + larg);

        	                                                // Affiche les résultats
        	                                                Repertoire.affichage(lelarg);
        	                                            }
        	                                        } else {
        	                                            System.out.println("Erreur : Veuillez entrer des arguments valides");
        	                                        }
        	                                    } else {
        	                                        System.out.println("Erreur : L'option -lelarg est uniquement valable pour un dossier");
        	                                    }
        	                                    i++;
        	                                    break;
                	
        	                                    /**
        	                                     * Traite l'option -ltlarg pour trouver les fichiers dont la largeur est strictement inférieure
        	                                     * à une valeur spécifiée. Cette option est uniquement valable pour un dossier.
        	                                     *
        	                                     * @param args les arguments passés en ligne de commande
        	                                     * @param fileOrDirectory le fichier ou le dossier cible
        	                                     * @param isDirectoryOption indique si l'option est appliquée sur un dossier
        	                                     */
        	                                    case "-ltlarg":
        	                                        if (isDirectoryOption) {
        	                                            int j = 1;
        	                                            if (args.length >= 8 && AllChoice(args[3]) && AllChoice(args[4])) {
        	                                                int larg = Fichier.larg(args[5]); // Récupère la largeur
        	                                                if (larg != -1) {
        	                                                    ArrayList<File> ltlarg = new ArrayList<>();
        	                                                    ArrayList<File> Choix1 = new ArrayList<>();
        	                                                    ArrayList<File> Choix2 = new ArrayList<>();
        	                                                    ArrayList<File> Final = new ArrayList<>();

        	                                                    // Active les choix passés en arguments
        	                                                    j = activeChoix(args[3], j + 2, args, fileOrDirectory, Choix1);
        	                                                    j = activeChoix(args[4], j, args, fileOrDirectory, Choix2);
        	                                                    i += j - 1;

        	                                                    // Recherche les fichiers dont la largeur est strictement inférieure à la valeur spécifiée
        	                                                    Repertoire.recherche_fichier_largeur_inf(fileOrDirectory, larg, ltlarg);
        	                                                    System.out.println("Liste des fichiers dont la largeur est strictement inférieure : " + larg);

        	                                                    // Combine les résultats et les affiche
        	                                                    Final = Repertoire.compare3(Choix1, Choix2, ltlarg);
        	                                                    Repertoire.affichage(Final);
        	                                                }
        	                                            } else if (args.length >= 6 && AllChoice(args[3]) && !AllChoice(args[4])) {
        	                                                int larg = Fichier.larg(args[4]); // Récupère la largeur
        	                                                if (larg != -1) {
        	                                                    ArrayList<File> ltlarg = new ArrayList<>();
        	                                                    ArrayList<File> Choix1 = new ArrayList<>();
        	                                                    ArrayList<File> Final = new ArrayList<>();

        	                                                    // Active un seul choix passé en argument
        	                                                    j = activeChoix(args[3], j + 1, args, fileOrDirectory, Choix1);
        	                                                    i += j - 1;

        	                                                    // Recherche les fichiers dont la largeur est strictement inférieure à la valeur spécifiée
        	                                                    Repertoire.recherche_fichier_largeur_inf(fileOrDirectory, larg, ltlarg);
        	                                                    System.out.println("Liste des fichiers dont la largeur est strictement inférieure : " + larg);

        	                                                    // Combine les résultats et les affiche
        	                                                    Final = Repertoire.compare2(Choix1, ltlarg);
        	                                                    Repertoire.affichage(Final);
        	                                                }
        	                                            } else if (args.length >= 4 && !AllChoice(args[3])) {
        	                                                int larg = Fichier.larg(args[3]); // Récupère la largeur
        	                                                if (larg != -1) {
        	                                                    ArrayList<File> ltlarg = new ArrayList<>();

        	                                                    // Recherche les fichiers dont la largeur est strictement inférieure à la valeur spécifiée
        	                                                    Repertoire.recherche_fichier_largeur_inf(fileOrDirectory, larg, ltlarg);
        	                                                    System.out.println("Liste des fichiers dont la largeur est strictement inférieure : " + larg);

        	                                                    // Affiche les résultats
        	                                                    Repertoire.affichage(ltlarg);
        	                                                }
        	                                            } else {
        	                                                System.out.println("Erreur : Veuillez entrer des arguments valides");
        	                                            }
        	                                        } else {
        	                                            System.out.println("Erreur : L'option -ltlarg est uniquement valable pour un dossier");
        	                                        }
        	                                        i++;
        	                                        break;

                	
        	                                        /**
        	                                         * Traite l'option -gtlarg pour trouver les fichiers dont la largeur est strictement supérieure
        	                                         * à une valeur spécifiée. Cette option est uniquement valable pour un dossier.
        	                                         *
        	                                         * @param args les arguments passés en ligne de commande
        	                                         * @param fileOrDirectory le fichier ou le dossier cible
        	                                         * @param isDirectoryOption indique si l'option est appliquée sur un dossier
        	                                         */
        	                                        case "-gtlarg":
        	                                            if (isDirectoryOption) {
        	                                                int j = 1;
        	                                                if (args.length >= 8 && AllChoice(args[3]) && AllChoice(args[4])) {
        	                                                    int larg = Fichier.larg(args[5]); // Récupère la largeur
        	                                                    if (larg != -1) {
        	                                                        ArrayList<File> gtlarg = new ArrayList<>();
        	                                                        ArrayList<File> Choix1 = new ArrayList<>();
        	                                                        ArrayList<File> Choix2 = new ArrayList<>();
        	                                                        ArrayList<File> Final = new ArrayList<>();

        	                                                        j = activeChoix(args[3], j + 2, args, fileOrDirectory, Choix1);
        	                                                        j = activeChoix(args[4], j, args, fileOrDirectory, Choix2);
        	                                                        i += j - 1;

        	                                                        Repertoire.recherche_fichier_largeur_sup(fileOrDirectory, larg, gtlarg);
        	                                                        System.out.println("Liste des fichiers dont la largeur est supérieure : " + larg);

        	                                                        Final = Repertoire.compare3(Choix1, Choix2, gtlarg);
        	                                                        Repertoire.affichage(Final);
        	                                                    }
        	                                                } else if (args.length >= 6 && AllChoice(args[3]) && !AllChoice(args[4])) {
        	                                                    int larg = Fichier.larg(args[4]);
        	                                                    if (larg != -1) {
        	                                                        ArrayList<File> gtlarg = new ArrayList<>();
        	                                                        ArrayList<File> Choix1 = new ArrayList<>();
        	                                                        ArrayList<File> Final = new ArrayList<>();

        	                                                        j = activeChoix(args[3], j + 1, args, fileOrDirectory, Choix1);
        	                                                        i += j - 1;

        	                                                        Repertoire.recherche_fichier_largeur_sup(fileOrDirectory, larg, gtlarg);
        	                                                        System.out.println("Liste des fichiers dont la largeur est supérieure : " + larg);

        	                                                        Final = Repertoire.compare2(Choix1, gtlarg);
        	                                                        Repertoire.affichage(Final);
        	                                                    }
        	                                                } else if (args.length >= 4 && !AllChoice(args[3])) {
        	                                                    int larg = Fichier.larg(args[3]);
        	                                                    if (larg != -1) {
        	                                                        ArrayList<File> gtlarg = new ArrayList<>();

        	                                                        Repertoire.recherche_fichier_largeur_sup(fileOrDirectory, larg, gtlarg);
        	                                                        System.out.println("Liste des fichiers dont la largeur est supérieure : " + larg);

        	                                                        Repertoire.affichage(gtlarg);
        	                                                    }
        	                                                } else {
        	                                                    System.out.println("Erreur : Veuillez entrer des arguments valides");
        	                                                }
        	                                            } else {
        	                                                System.out.println("Erreur : L'option -gtlarg est uniquement valable pour un dossier");
        	                                            }
        	                                            i++;
        	                                            break;

        	                                        /**
        	                                         * Traite l'option -gelarg pour trouver les fichiers dont la largeur est supérieure ou égale
        	                                         * à une valeur spécifiée. Cette option est uniquement valable pour un dossier.
        	                                         *
        	                                         * @param args les arguments passés en ligne de commande
        	                                         * @param fileOrDirectory le fichier ou le dossier cible
        	                                         * @param isDirectoryOption indique si l'option est appliquée sur un dossier
        	                                         */
        	                                        case "-gelarg":
        	                                            if (isDirectoryOption) {
        	                                                int j = 1;
        	                                                if (args.length >= 8 && AllChoice(args[3]) && AllChoice(args[4])) {
        	                                                    int larg = Fichier.larg(args[5]);
        	                                                    if (larg != -1) {
        	                                                        ArrayList<File> gelarg = new ArrayList<>();
        	                                                        ArrayList<File> Choix1 = new ArrayList<>();
        	                                                        ArrayList<File> Choix2 = new ArrayList<>();
        	                                                        ArrayList<File> Final = new ArrayList<>();

        	                                                        j = activeChoix(args[3], j + 2, args, fileOrDirectory, Choix1);
        	                                                        j = activeChoix(args[4], j, args, fileOrDirectory, Choix2);
        	                                                        i += j - 1;

        	                                                        Repertoire.recherche_fichier_largeur_sup_egale(fileOrDirectory, larg, gelarg);
        	                                                        System.out.println("Liste des fichiers dont la largeur est supérieure ou égale : " + larg);

        	                                                        Final = Repertoire.compare3(Choix1, Choix2, gelarg);
        	                                                        Repertoire.affichage(Final);
        	                                                    }
        	                                                } else if (args.length >= 6 && AllChoice(args[3]) && !AllChoice(args[4])) {
        	                                                    int larg = Fichier.larg(args[4]);
        	                                                    if (larg != -1) {
        	                                                        ArrayList<File> gelarg = new ArrayList<>();
        	                                                        ArrayList<File> Choix1 = new ArrayList<>();
        	                                                        ArrayList<File> Final = new ArrayList<>();

        	                                                        j = activeChoix(args[3], j + 1, args, fileOrDirectory, Choix1);
        	                                                        i += j - 1;

        	                                                        Repertoire.recherche_fichier_largeur_sup_egale(fileOrDirectory, larg, gelarg);
        	                                                        System.out.println("Liste des fichiers dont la largeur est supérieure ou égale : " + larg);

        	                                                        Final = Repertoire.compare2(Choix1, gelarg);
        	                                                        Repertoire.affichage(Final);
        	                                                    }
        	                                                } else if (args.length >= 4 && !AllChoice(args[3])) {
        	                                                    int larg = Fichier.larg(args[3]);
        	                                                    if (larg != -1) {
        	                                                        ArrayList<File> gelarg = new ArrayList<>();

        	                                                        Repertoire.recherche_fichier_largeur_sup_egale(fileOrDirectory, larg, gelarg);
        	                                                        System.out.println("Liste des fichiers dont la largeur est supérieure ou égale : " + larg);

        	                                                        Repertoire.affichage(gelarg);
        	                                                    }
        	                                                } else {
        	                                                    System.out.println("Erreur : Veuillez entrer des arguments valides");
        	                                                }
        	                                            } else {
        	                                                System.out.println("Erreur : L'option -gelarg est uniquement valable pour un dossier");
        	                                            }
        	                                            i++;
        	                                            break;

        	                                        /**
        	                                         * Traite l'option -eqlarg pour trouver les fichiers dont la largeur est égale
        	                                         * à une valeur spécifiée. Cette option est uniquement valable pour un dossier.
        	                                         *
        	                                         * @param args les arguments passés en ligne de commande
        	                                         * @param fileOrDirectory le fichier ou le dossier cible
        	                                         * @param isDirectoryOption indique si l'option est appliquée sur un dossier
        	                                         */
        	                                        case "-eqlarg":
        	                                            if (isDirectoryOption) {
        	                                                int j = 1;
        	                                                if (args.length >= 8 && AllChoice(args[3]) && AllChoice(args[4])) {
        	                                                    int larg = Fichier.larg(args[5]);
        	                                                    if (larg != -1) {
        	                                                        ArrayList<File> eqlarg = new ArrayList<>();
        	                                                        ArrayList<File> Choix1 = new ArrayList<>();
        	                                                        ArrayList<File> Choix2 = new ArrayList<>();
        	                                                        ArrayList<File> Final = new ArrayList<>();

        	                                                        j = activeChoix(args[3], j + 2, args, fileOrDirectory, Choix1);
        	                                                        j = activeChoix(args[4], j, args, fileOrDirectory, Choix2);
        	                                                        i += j - 1;

        	                                                        Repertoire.recherche_fichier_largeur_egale(fileOrDirectory, larg, eqlarg);
        	                                                        System.out.println("Liste des fichiers dont la largeur est égale : " + larg);

        	                                                        Final = Repertoire.compare3(Choix1, Choix2, eqlarg);
        	                                                        Repertoire.affichage(Final);
        	                                                    }
        	                                                } else if (args.length >= 6 && AllChoice(args[3]) && !AllChoice(args[4])) {
        	                                                    int larg = Fichier.larg(args[4]);
        	                                                    if (larg != -1) {
        	                                                        ArrayList<File> eqlarg = new ArrayList<>();
        	                                                        ArrayList<File> Choix1 = new ArrayList<>();
        	                                                        ArrayList<File> Final = new ArrayList<>();

        	                                                        j = activeChoix(args[3], j + 1, args, fileOrDirectory, Choix1);
        	                                                        i += j - 1;

        	                                                        Repertoire.recherche_fichier_largeur_egale(fileOrDirectory, larg, eqlarg);
        	                                                        System.out.println("Liste des fichiers dont la largeur est égale : " + larg);

        	                                                        Final = Repertoire.compare2(Choix1, eqlarg);
        	                                                        Repertoire.affichage(Final);
        	                                                    }
        	                                                } else if (args.length >= 4 && !AllChoice(args[3])) {
        	                                                    int larg = Fichier.larg(args[3]);
        	                                                    if (larg != -1) {
        	                                                        ArrayList<File> eqlarg = new ArrayList<>();

        	                                                        Repertoire.recherche_fichier_largeur_egale(fileOrDirectory, larg, eqlarg);
        	                                                        System.out.println("Liste des fichiers dont la largeur est égale : " + larg);

        	                                                        Repertoire.affichage(eqlarg);
        	                                                    }
        	                                                } else {
        	                                                    System.out.println("Erreur : Veuillez entrer des arguments valides");
        	                                                }
        	                                            } else {
        	                                                System.out.println("Erreur : L'option -eqlarg est uniquement valable pour un dossier");
        	                                            }
        	                                            i++;
        	                                            break;

                	
        	                                            /**
        	                                             * Gère les options passées en argument à l'application.
        	                                             * Les options traitées incluent "-nelarg" et "-nehaut", permettant de rechercher des fichiers
        	                                             * dans un dossier selon des critères spécifiques de largeur ou hauteur différentes.
        	                                             *
        	                                             * @param args Tableau des arguments passés en ligne de commande.
        	                                             * @param fileOrDirectory Fichier ou répertoire sur lequel appliquer les traitements.
        	                                             * @param isDirectoryOption Indique si l'opération s'applique à un répertoire.
        	                                             * @param operationOption Option d'opération spécifiée par l'utilisateur.
        	                                             */
        	                                            case "-nelarg":
        	                                                if (isDirectoryOption) {
        	                                                    int j = 1;
        	                                                    /**
        	                                                     * Option "-nelarg" :
        	                                                     * Permet de rechercher des fichiers dans un répertoire dont la largeur est différente d'une valeur donnée.
        	                                                     *
        	                                                     * @param args[3] et args[4] (facultatifs) : Choix supplémentaires pour filtrer les résultats.
        	                                                     * @param args[5] (obligatoire dans ce cas) : Largeur de référence pour la comparaison.
        	                                                     */
        	                                                    if (args.length >= 8 && AllChoice(args[3]) && AllChoice(args[4])) {
        	                                                        int larg = Fichier.larg(args[5]);
        	                                                        if (larg != -1) {
        	                                                            ArrayList<File> nelarg = new ArrayList<>();
        	                                                            ArrayList<File> Choix1 = new ArrayList<>();
        	                                                            ArrayList<File> Choix2 = new ArrayList<>();
        	                                                            ArrayList<File> Final = new ArrayList<>();
        	                                                            j = activeChoix(args[3], j + 2, args, fileOrDirectory, Choix1);
        	                                                            j = activeChoix(args[4], j, args, fileOrDirectory, Choix2);
        	                                                            i += j - 1;
        	                                                            Repertoire.recherche_fichier_largeur_dif(fileOrDirectory, larg, nelarg);
        	                                                            System.out.println("Liste des fichiers dont la largeur est différente : " + larg);
        	                                                            Final = Repertoire.compare3(Choix1, Choix2, nelarg);
        	                                                            Repertoire.affichage(Final);
        	                                                        }
        	                                                    } 
        	                                                    // Cas où un seul choix est donné et un argument pour la largeur
        	                                                    else if (args.length >= 6 && AllChoice(args[3]) && !AllChoice(args[4])) {
        	                                                        /**
        	                                                         * @param args[3] : Premier choix pour filtrer les fichiers.
        	                                                         * @param args[4] : Largeur de référence pour la comparaison.
        	                                                         */
        	                                                        int larg = Fichier.larg(args[4]);
        	                                                        if (larg != -1) {
        	                                                            ArrayList<File> nelarg = new ArrayList<>();
        	                                                            ArrayList<File> Choix1 = new ArrayList<>();
        	                                                            ArrayList<File> Final = new ArrayList<>();
        	                                                            j = activeChoix(args[3], j + 1, args, fileOrDirectory, Choix1);
        	                                                            i += j - 1;
        	                                                            Repertoire.recherche_fichier_largeur_dif(fileOrDirectory, larg, nelarg);
        	                                                            System.out.println("Liste des fichiers dont la largeur est différente : " + larg);
        	                                                            Final = Repertoire.compare2(Choix1, nelarg);
        	                                                            Repertoire.affichage(Final);
        	                                                        }
        	                                                    } 
        	                                                    // Cas où seule la largeur est spécifiée
        	                                                    else if (args.length >= 4 && !AllChoice(args[3])) {
        	                                                        /**
        	                                                         * @param args[3] : Largeur de référence pour la comparaison.
        	                                                         */
        	                                                        int larg = Fichier.larg(args[3]);
        	                                                        if (larg != -1) {
        	                                                            ArrayList<File> nelarg = new ArrayList<>();
        	                                                            Repertoire.recherche_fichier_largeur_dif(fileOrDirectory, larg, nelarg);
        	                                                            System.out.println("Liste des fichiers dont la largeur est différente : " + larg);
        	                                                            Repertoire.affichage(nelarg);
        	                                                        }
        	                                                    } else {
        	                                                        System.out.println("Erreur : Veuillez entrer des arguments valides");
        	                                                    }
        	                                                } else {
        	                                                    System.out.println("Erreur : L'option -nelarg est uniquement valable pour un dossier");
        	                                                }
        	                                                i++;
        	                                                break;

        	                                            case "-nehaut":
        	                                                if (isDirectoryOption) {
        	                                                    int j = 1;
        	                                                    /**
        	                                                     * Option "-nehaut" :
        	                                                     * Permet de rechercher des fichiers dans un répertoire dont la hauteur est différente d'une valeur donnée.
        	                                                     *
        	                                                     * @param args[3] et args[4] (facultatifs) : Choix supplémentaires pour filtrer les résultats.
        	                                                     * @param args[5] (obligatoire dans ce cas) : Hauteur de référence pour la comparaison.
        	                                                     */
        	                                                    if (args.length >= 8 && AllChoice(args[3]) && AllChoice(args[4])) {
        	                                                        int haut = Fichier.haut(args[5]);
        	                                                        if (haut != -1) {
        	                                                            ArrayList<File> nehaut = new ArrayList<>();
        	                                                            ArrayList<File> Choix1 = new ArrayList<>();
        	                                                            ArrayList<File> Choix2 = new ArrayList<>();
        	                                                            ArrayList<File> Final = new ArrayList<>();
        	                                                            j = activeChoix(args[3], j + 2, args, fileOrDirectory, Choix1);
        	                                                            j = activeChoix(args[4], j, args, fileOrDirectory, Choix2);
        	                                                            i += j - 1;
        	                                                            Repertoire.recherche_fichier_hauteur_dif(fileOrDirectory, haut, nehaut);
        	                                                            System.out.println("Liste des fichiers dont la hauteur est différente : " + haut);
        	                                                            Final = Repertoire.compare3(Choix1, Choix2, nehaut);
        	                                                            Repertoire.affichage(Final);
        	                                                        }
        	                                                    } 
        	                                                    // Cas où un seul choix est donné et un argument pour la hauteur
        	                                                    else if (args.length >= 6 && AllChoice(args[3]) && !AllChoice(args[4])) {
        	                                                        /**
        	                                                         * @param args[3] : Premier choix pour filtrer les fichiers.
        	                                                         * @param args[4] : Hauteur de référence pour la comparaison.
        	                                                         */
        	                                                        int haut = Fichier.haut(args[4]);
        	                                                        if (haut != -1) {
        	                                                            ArrayList<File> nehaut = new ArrayList<>();
        	                                                            ArrayList<File> Choix1 = new ArrayList<>();
        	                                                            ArrayList<File> Final = new ArrayList<>();
        	                                                            j = activeChoix(args[3], j + 1, args, fileOrDirectory, Choix1);
        	                                                            i += j - 1;
        	                                                            Repertoire.recherche_fichier_hauteur_dif(fileOrDirectory, haut, nehaut);
        	                                                            System.out.println("Liste des fichiers dont la hauteur est différente : " + haut);
        	                                                            Final = Repertoire.compare2(Choix1, nehaut);
        	                                                            Repertoire.affichage(Final);
        	                                                        }
        	                                                    } 
        	                                                    // Cas où seule la hauteur est spécifiée
        	                                                    else if (args.length >= 4 && !AllChoice(args[3])) {
        	                                                        /**
        	                                                         * @param args[3] : Hauteur de référence pour la comparaison.
        	                                                         */
        	                                                        int haut = Fichier.haut(args[3]);
        	                                                        if (haut != -1) {
        	                                                            ArrayList<File> nehaut = new ArrayList<>();
        	                                                            Repertoire.recherche_fichier_hauteur_dif(fileOrDirectory, haut, nehaut);
        	                                                            System.out.println("Liste des fichiers dont la hauteur est différente : " + haut);
        	                                                            Repertoire.affichage(nehaut);
        	                                                        }
        	                                                    } else {
        	                                                        System.out.println("Erreur : Veuillez entrer des arguments valides");
        	                                                    }
        	                                                } else {
        	                                                    System.out.println("Erreur : L'option -nehaut est uniquement valable pour un dossier");
        	                                                }
        	                                                i++;
        	                                                break;

                	
                	
                	
                	
                	
                	
                default:
                    System.out.println("Erreur : Option inconnue \"" + operationOption + "\". Utilisez -h ou --help pour voir les fonctionnalités.");
                    break;
            }
            
        }
        
    }

    /**
     * Affiche l'aide ou les instructions d'utilisation pour l'application en console.
     * Cette méthode est destinée à guider l'utilisateur sur les commandes et options disponibles.
     */
    public static void printHelp() {
        System.out.println("\n======= Aide - Liste des fonctionnalités disponibles =======");
        System.out.println("Usage général : <type> <chemin> <option(s)>");
        System.out.println("Options disponibles :");
        System.out.println("  -f <fichier> --info                Affiche les informations détaillées sur le fichier spécifié.");
        System.out.println("  -f <fichier> --metadata            Affiche les métadonnées de l'image spécifiée.");
        System.out.println("  -d <répertoire> --stat             Affiche les statistiques sur les fichiers et images dans le répertoire spécifié.");
        System.out.println("  -d <répertoire> --list             Liste tous les fichiers présents dans le répertoire spécifié.");
        System.out.println("  -d <répertoire> -eqdate <date> <heure>  Affiche les fichiers créés à une date et une heure précises.");
        System.out.println("  -d <répertoire> -gedate <date> <heure> Affiche les fichiers créés après ou à une date et une heure précises.");
        System.out.println("  -d <répertoire> -gtdate <date> <heure> Affiche les fichiers créés après une date et une heure précises.");
        System.out.println("  -d <répertoire> -ledate <date> <heure> Affiche les fichiers créés avant ou à une date et une heure précises.");
        System.out.println("  -d <répertoire> -ltdate <date> <heure> Affiche les fichiers créés avant une date et une heure précises.");
        System.out.println("  -d <répertoire> -nedate <date> <heure> Affiche les fichiers dont la date de création est différente de la date spécifiée.");
        System.out.println("  -d <répertoire> -eqdim <longueur> x <largeur> Affiche les fichiers ayant des dimensions spécifiques.");
        System.out.println("  -d <répertoire> -nedim <longueur> x <largeur> Affiche les fichiers ayant des dimensions différentes.");
        System.out.println("  -d <répertoire> -gehaut <hauteur>  Affiche les fichiers ayant une hauteur supérieure ou égale à la valeur spécifiée.");
        System.out.println("  -d <répertoire> -gthaut <hauteur>  Affiche les fichiers ayant une hauteur strictement supérieure à la valeur spécifiée.");
        System.out.println("  -d <répertoire> -lthaut <hauteur>  Affiche les fichiers ayant une hauteur inférieure à la valeur spécifiée.");
        System.out.println("  -d <répertoire> -eqhaut <hauteur>  Affiche les fichiers ayant une hauteur égale à la valeur spécifiée.");
        System.out.println("  -d <répertoire> -lehaut <hauteur>  Affiche les fichiers ayant une hauteur inférieure ou égale à la valeur spécifiée."); // Ajout
        System.out.println("  -d <répertoire> -lelarg <largeur>  Affiche les fichiers ayant une largeur inférieure ou égale à la valeur spécifiée.");
        System.out.println("  -d <répertoire> -ltlarg <largeur>  Affiche les fichiers ayant une largeur strictement inférieure à la valeur spécifiée.");
        System.out.println("  -d <répertoire> -gtlarg <largeur>  Affiche les fichiers ayant une largeur strictement supérieure à la valeur spécifiée.");
        System.out.println("  -d <répertoire> -gelarg <largeur>  Affiche les fichiers ayant une largeur supérieure ou égale à la valeur spécifiée.");
        System.out.println("  -d <répertoire> -eqlarg <largeur>  Affiche les fichiers ayant une largeur égale à la valeur spécifiée.");
        System.out.println("  -d <répertoire> -nelarg <largeur>  Affiche les fichiers ayant une largeur différente de la valeur spécifiée.");
        System.out.println("  -d <répertoire> -nehaut <hauteur>  Affiche les fichiers ayant une hauteur différente de la valeur spécifiée.");
        System.out.println("  -d <répertoire> -eqname <nom>      Affiche les fichiers ayant un nom égal au nom spécifié.");
        System.out.println("  -d <répertoire> -nename <nom>      Affiche les fichiers ayant un nom différent du nom spécifié.");
        System.out.println("  -h, --help                         Affiche cette aide.");
        System.out.println("\nFormat de date attendu : JJ/MM/AAAA HH:MM:SS");
        System.out.println("\nExemples :");
        System.out.println("  -f /chemin/vers/fichier.txt --info");
        System.out.println("  -f /chemin/vers/image.jpg --metadata");
        System.out.println("  -d /chemin/vers/repertoire --stat");
        System.out.println("  -d /chemin/vers/repertoire -eqdate 15/03/2023 10:30:00");
        System.out.println("  -d /chemin/vers/repertoire -gedate 01/01/2024 00:00:00");
        System.out.println("  -d /chemin/vers/repertoire -gtdate 20/05/2023 15:45:00");
        System.out.println("  -d /chemin/vers/repertoire -ledate 10/12/2023 08:00:00");
        System.out.println("  -d /chemin/vers/repertoire -ltdate 15/03/2023 10:30:00");
        System.out.println("  -d /chemin/vers/repertoire -nedate 20/05/2023 15:45:00");
        System.out.println("  -d /chemin/vers/repertoire -eqdim 1920 x 1080");
        System.out.println("  -d /chemin/vers/repertoire -nedim 1280 x 720");
        System.out.println("  -d /chemin/vers/repertoire -gehaut 1080");
        System.out.println("  -d /chemin/vers/repertoire -gthaut 1080");
        System.out.println("  -d /chemin/vers/repertoire -lthaut 720");
        System.out.println("  -d /chemin/vers/repertoire -lehaut 1080"); // Exemple ajouté
        System.out.println("  -d /chemin/vers/repertoire -eqhaut 900");
        System.out.println("  -d /chemin/vers/repertoire -lelarg 1280");
        System.out.println("  -d /chemin/vers/repertoire -ltlarg 800");
        System.out.println("  -d /chemin/vers/repertoire -gtlarg 1200");
        System.out.println("  -d /chemin/vers/repertoire -gelarg 1024");
        System.out.println("  -d /chemin/vers/repertoire -eqlarg 800");
        System.out.println("  -d /chemin/vers/repertoire -nelarg 600");
        System.out.println("  -d /chemin/vers/repertoire -nehaut 720");
        System.out.println("  -d /chemin/vers/repertoire -eqname fichier.txt");
        System.out.println("  -d /chemin/vers/repertoire -nename exemple.txt");
        System.out.println("  -h ou --help");
        System.out.println("\n===========================================================");
    }





    
    /**
     * Vérifie si le choix fourni correspond à une option valide.
     * Les choix incluent diverses options pour comparer les attributs des fichiers (dimensions, noms, dates, etc.).
     *
     * @param choix L'option fournie par l'utilisateur.
     * @return true si l'option est reconnue, false sinon.
     */
    private static boolean AllChoice(String choix) {
        return (choix.equals("-nehaut") || choix.equals("-lehaut") || choix.equals("-lthaut") || choix.equals("-gehaut") || 
                choix.equals("-eqhaut") || choix.equals("-gthaut") || choix.equals("-eqdim") || choix.equals("-nedim") || 
                choix.equals("-eqname") || choix.equals("-nename") || choix.equals("-eqdate") || choix.equals("-nedate") || 
                choix.equals("-ledate") || choix.equals("-ltdate") || choix.equals("-gedate") || choix.equals("-gtdate") || 
                choix.equals("-lelarg") || choix.equals("-ltlarg") || choix.equals("-gelarg") || choix.equals("-gtlarg") || 
                choix.equals("-nelarg") || choix.equals("-eqlarg"));
    }

    /**
     * Active l'option spécifiée pour rechercher les fichiers correspondant aux critères donnés.
     * Traite plusieurs types de filtres, comme la comparaison des dates de création.
     *
     * @param choix L'option choisie pour filtrer les fichiers.
     * @param i Index de l'argument courant.
     * @param args Tableau des arguments passés en ligne de commande.
     * @param fileOrDirectory Fichier ou répertoire sur lequel effectuer la recherche.
     * @param Al Liste où les fichiers correspondant aux critères seront ajoutés.
     * @return L'index mis à jour après traitement de l'option.
     */
    private static int activeChoix(String choix, int i, String[] args, File fileOrDirectory, ArrayList<File> Al) {
        switch (choix) {
            case "-eqdate":
                /**
                 * Option "-eqdate" :
                 * Recherche les fichiers dont la date de création est exactement égale à la date spécifiée.
                 *
                 * @param args[3+i] et args[4+i] : Parties de la date (format attendu : "JJ/MM/AAAA HH:mm:ss").
                 */
                if (args.length >= 5 + i) {
                    String fd = args[3 + i];
                    String sd = args[4 + i];
                    String date = fd + " " + sd;
                    if (Repertoire.IsDate(date)) {
                        Repertoire.recherche_fichier_date(fileOrDirectory, date, Al);
                        System.out.println("Liste des fichiers égaux à la date de création " + date + " + ");
                    } else {
                        System.out.println("Erreur : Veuillez entrer une date valide");
                    }
                }
                i += 2;
                break;

            case "-gedate":
                /**
                 * Option "-gedate" :
                 * Recherche les fichiers dont la date de création est supérieure ou égale à la date spécifiée.
                 *
                 * @param args[3+i] et args[4+i] : Parties de la date (format attendu : "JJ/MM/AAAA HH:mm:ss").
                 */
                if (args.length >= 5 + i) {
                    String fd = args[3 + i];
                    String sd = args[4 + i];
                    String date = fd + " " + sd;
                    if (Repertoire.IsDate(date)) {
                        Repertoire.recherche_fichier_date_sup_egale(fileOrDirectory, date, Al);
                        System.out.println("Liste des fichiers supérieurs ou égaux à la date de création " + date + " + ");
                    } else {
                        System.out.println("Erreur : Veuillez entrer une date valide");
                    }
                }
                i += 2;
                break;

            case "-gtdate":
                /**
                 * Option "-gtdate" :
                 * Recherche les fichiers dont la date de création est strictement supérieure à la date spécifiée.
                 *
                 * @param args[3+i] et args[4+i] : Parties de la date (format attendu : "JJ/MM/AAAA HH:mm:ss").
                 */
                if (args.length >= 5 + i) {
                    String fd = args[3 + i];
                    String sd = args[4 + i];
                    String date = fd + " " + sd;
                    if (Repertoire.IsDate(date)) {
                        Repertoire.recherche_fichier_date_sup(fileOrDirectory, date, Al);
                        System.out.println("Liste des fichiers supérieurs à la date de création " + date + " + ");
                    } else {
                        System.out.println("Erreur : Veuillez entrer une date valide");
                    }
                }
                i += 2;
                break;

            case "-ledate":
                /**
                 * Option "-ledate" :
                 * Recherche les fichiers dont la date de création est inférieure ou égale à la date spécifiée.
                 *
                 * @param args[3+i] et args[4+i] : Parties de la date (format attendu : "JJ/MM/AAAA HH:mm:ss").
                 */
                if (args.length >= 5 + i) {
                    String fd = args[3 + i];
                    String sd = args[4 + i];
                    String date = fd + " " + sd;
                    if (Repertoire.IsDate(date)) {
                        Repertoire.recherche_fichier_date_inf_egale(fileOrDirectory, date, Al);
                        System.out.println("Liste des fichiers inférieurs ou égaux à la date de création " + date + " + ");
                    } else {
                        System.out.println("Erreur : Veuillez entrer une date valide");
                    }
                }
                i += 2;
                break;
                /**
                 * Option "-ltdate" :
                 * Recherche les fichiers dont la date de création est strictement inférieure à la date spécifiée.
                 *
                 * @param args[3+i] et args[4+i] Parties de la date (format attendu : "JJ/MM/AAAA HH:mm:ss").
                 */
                case "-ltdate":
                    if (args.length >= 5 + i) {
                        String fd = args[3 + i];
                        String sd = args[4 + i];
                        String date = fd + " " + sd;
                        if (Repertoire.IsDate(date)) {
                            Repertoire.recherche_fichier_date_inf(fileOrDirectory, date, Al);
                            System.out.println("Liste des fichiers inférieurs à la date de création " + date + " + ");
                        } else {
                            System.out.println("Erreur : Veuillez entrer une date valide");
                        }
                    }
                    i += 2;
                    break;

                /**
                 * Option "-nedate" :
                 * Recherche les fichiers dont la date de création est différente de la date spécifiée.
                 *
                 * @param args[3+i] et args[4+i] Parties de la date (format attendu : "JJ/MM/AAAA HH:mm:ss").
                 */
                case "-nedate":
                    if (args.length >= 5 + i) {
                        String fd = args[3 + i];
                        String sd = args[4 + i];
                        String date = fd + " " + sd;
                        if (Repertoire.IsDate(date)) {
                            Repertoire.recherche_fichier_date_dif(fileOrDirectory, date, Al);
                            System.out.println("Liste des fichiers différents de la date de création " + date + " + ");
                        } else {
                            System.out.println("Erreur : Veuillez entrer une date valide");
                        }
                    }
                    i += 2;
                    break;

                /**
                 * Option "-eqdim" :
                 * Recherche les fichiers ayant des dimensions (largeur x hauteur) égales à celles spécifiées.
                 *
                 * @param args[3+i] Largeur des fichiers.
                 * @param args[5+i] Hauteur des fichiers.
                 */
                case "-eqdim":
                    if (args.length >= 6 + i) {
                        if (args[4 + i].equals("x")) {
                            int largeur = Fichier.larg(args[3 + i]);
                            int haut = Fichier.haut(args[5 + i]);
                            if (haut != -1 && largeur != -1) {
                                Repertoire.recherche_fichier_dim(fileOrDirectory, haut, largeur, Al);
                                System.out.println("Liste des fichiers égaux à la dimension " + largeur + " x " + haut + "+");
                            }
                        } else {
                            System.out.println("Erreur : veuillez entrer une dimension");
                        }
                    } else {
                        System.out.println("Erreur : veuillez entrer une dimension");
                    }
                    i += 3;
                    break;

                /**
                 * Option "-eqname" :
                 * Recherche les fichiers dont le nom est exactement égal au nom spécifié.
                 *
                 * @param args[3+i] Nom du fichier recherché.
                 */
                case "-eqname":
                    if (args.length >= 4 + i) {
                        Repertoire.recherche_fichier_nom(fileOrDirectory, args[3 + i], Al);
                        System.out.println("Liste des fichiers dont le nom est : " + args[3 + i] + " + ");
                    } else {
                        System.out.println("Erreur : veuillez entrer un nom");
                    }
                    i++;
                    break;

                /**
                 * Option "-gthaut" :
                 * Recherche les fichiers ayant une hauteur strictement supérieure à celle spécifiée.
                 *
                 * @param args[3+i] Hauteur minimale des fichiers.
                 */
                case "-gthaut":
                    if (args.length >= 4 + i) {
                        int haut = Fichier.haut(args[3 + i]);
                        if (haut != -1) {
                            System.out.println("Liste des fichiers dont la hauteur est supérieure à : " + haut + " + ");
                            Repertoire.recherche_fichier_hauteur_sup(fileOrDirectory, haut, Al);
                        }
                    } else {
                        System.out.println("Erreur : veuillez entrer une hauteur");
                    }
                    i++;
                    break;

                /**
                 * Option "-gehaut" :
                 * Recherche les fichiers ayant une hauteur supérieure ou égale à celle spécifiée.
                 *
                 * @param args[3+i] Hauteur minimale des fichiers.
                 */
                case "-gehaut":
                    if (args.length >= 4 + i) {
                        int haut = Fichier.haut(args[3 + i]);
                        if (haut != -1) {
                            System.out.println("Liste des fichiers dont la hauteur est supérieure ou égale à : " + haut + " + ");
                            Repertoire.recherche_fichier_hauteur_sup_egale(fileOrDirectory, haut, Al);
                        }
                    } else {
                        System.out.println("Erreur : veuillez entrer une hauteur");
                    }
                    i++;
                    break;

                /**
                 * Option "-lthaut" :
                 * Recherche les fichiers ayant une hauteur strictement inférieure à celle spécifiée.
                 *
                 * @param args[3+i] Hauteur maximale des fichiers.
                 */
                case "-lthaut":
                    if (args.length >= 4 + i) {
                        int haut = Fichier.haut(args[3 + i]);
                        if (haut != -1) {
                            System.out.println("Liste des fichiers dont la hauteur est inférieure à : " + haut + " + ");
                            Repertoire.recherche_fichier_hauteur_inf(fileOrDirectory, haut, Al);
                        }
                    } else {
                        System.out.println("Erreur : veuillez entrer une hauteur");
                    }
                    i++;
                    break;

                /**
                 * Option "-lehaut" :
                 * Recherche les fichiers ayant une hauteur inférieure ou égale à celle spécifiée.
                 *
                 * @param args[3+i] Hauteur maximale des fichiers.
                 */
                case "-lehaut":
                    if (args.length >= 4 + i) {
                        int haut = Fichier.haut(args[3 + i]);
                        if (haut != -1) {
                            System.out.println("Liste des fichiers dont la hauteur est inférieure ou égale à : " + haut + " + ");
                            Repertoire.recherche_fichier_hauteur_inf_egale(fileOrDirectory, haut, Al);
                        }
                    } else {
                        System.out.println("Erreur : veuillez entrer une hauteur");
                    }
                    i++;
                    break;
                    /**
                     * Option "-nehaut" :
                     * Recherche les fichiers ayant une hauteur différente de la hauteur spécifiée.
                     *
                     * @param args[3+i] Hauteur à exclure.
                     */
                    case "-nehaut":
                        if (args.length >= 4 + i) {
                            int haut = Fichier.haut(args[3 + i]);
                            if (haut != -1) {
                                System.out.println("Liste des fichiers dont la hauteur est différente : " + haut + " + ");
                                Repertoire.recherche_fichier_hauteur_dif(fileOrDirectory, haut, Al);
                            }
                        } else {
                            System.out.println("Erreur : veuillez entrer une hauteur");
                        }
                        i++;
                        break;

                    /**
                     * Option "-nelarg" :
                     * Recherche les fichiers ayant une largeur différente de la largeur spécifiée.
                     *
                     * @param args[3+i] Largeur à exclure.
                     */
                    case "-nelarg":
                        if (args.length >= 4 + i) {
                            int larg = Fichier.larg(args[3 + i]);
                            if (larg != -1) {
                                System.out.println("Liste des fichiers dont la largeur est différente : " + larg + " + ");
                                Repertoire.recherche_fichier_largeur_dif(fileOrDirectory, larg, Al);
                            }
                        } else {
                            System.out.println("Erreur : veuillez entrer une largeur");
                        }
                        i++;
                        break;

                    /**
                     * Option "-gelarg" :
                     * Recherche les fichiers ayant une largeur supérieure ou égale à celle spécifiée.
                     *
                     * @param args[3+i] Largeur minimale.
                     */
                    case "-gelarg":
                        if (args.length >= 4 + i) {
                            int larg = Fichier.larg(args[3 + i]);
                            if (larg != -1) {
                                System.out.println("Liste des fichiers dont la largeur est supérieure ou égale à : " + larg + " + ");
                                Repertoire.recherche_fichier_largeur_sup_egale(fileOrDirectory, larg, Al);
                            }
                        } else {
                            System.out.println("Erreur : veuillez entrer une largeur");
                        }
                        i++;
                        break;

                    /**
                     * Option "-gtlarg" :
                     * Recherche les fichiers ayant une largeur strictement supérieure à celle spécifiée.
                     *
                     * @param args[3+i] Largeur minimale.
                     */
                    case "-gtlarg":
                        if (args.length >= 4 + i) {
                            int larg = Fichier.larg(args[3 + i]);
                            if (larg != -1) {
                                System.out.println("Liste des fichiers dont la largeur est strictement supérieure à : " + larg + " + ");
                                Repertoire.recherche_fichier_largeur_sup(fileOrDirectory, larg, Al);
                            }
                        } else {
                            System.out.println("Erreur : veuillez entrer une largeur");
                        }
                        i++;
                        break;

                    /**
                     * Option "-lelarg" :
                     * Recherche les fichiers ayant une largeur inférieure ou égale à celle spécifiée.
                     *
                     * @param args[3+i] Largeur maximale.
                     */
                    case "-lelarg":
                        if (args.length >= 4 + i) {
                            int larg = Fichier.larg(args[3 + i]);
                            if (larg != -1) {
                                System.out.println("Liste des fichiers dont la largeur est inférieure ou égale à : " + larg + " + ");
                                Repertoire.recherche_fichier_largeur_inf_egale(fileOrDirectory, larg, Al);
                            }
                        } else {
                            System.out.println("Erreur : veuillez entrer une largeur");
                        }
                        i++;
                        break;

                    /**
                     * Option "-ltlarg" :
                     * Recherche les fichiers ayant une largeur strictement inférieure à celle spécifiée.
                     *
                     * @param args[3+i] Largeur maximale.
                     */
                    case "-ltlarg":
                        if (args.length >= 4 + i) {
                            int larg = Fichier.larg(args[3 + i]);
                            if (larg != -1) {
                                System.out.println("Liste des fichiers dont la largeur est strictement inférieure à : " + larg + " + ");
                                Repertoire.recherche_fichier_largeur_inf(fileOrDirectory, larg, Al);
                            }
                        } else {
                            System.out.println("Erreur : veuillez entrer une largeur");
                        }
                        i++;
                        break;

                    /**
                     * Option "-eqlarg" :
                     * Recherche les fichiers ayant une largeur égale à celle spécifiée.
                     *
                     * @param args[3+i] Largeur recherchée.
                     */
                    case "-eqlarg":
                        if (args.length >= 4 + i) {
                            int larg = Fichier.larg(args[3 + i]);
                            if (larg != -1) {
                                System.out.println("Liste des fichiers dont la largeur est égale à : " + larg + " + ");
                                Repertoire.recherche_fichier_largeur_egale(fileOrDirectory, larg, Al);
                            }
                        } else {
                            System.out.println("Erreur : veuillez entrer une largeur");
                        }
                        i++;
                        break;

                    /**
                     * Option "-eqhaut" :
                     * Recherche les fichiers ayant une hauteur égale à celle spécifiée.
                     *
                     * @param args[3+i] Hauteur recherchée.
                     */
                    case "-eqhaut":
                        if (args.length >= 4 + i) {
                            int haut = Fichier.haut(args[3 + i]);
                            if (haut != -1) {
                                System.out.println("Liste des fichiers dont la hauteur est égale à : " + haut + " + ");
                                Repertoire.recherche_fichier_hauteur_egale(fileOrDirectory, haut, Al);
                            }
                        } else {
                            System.out.println("Erreur : veuillez entrer une hauteur");
                        }
                        i++;
                        break;

                    /**
                     * Option "-nedim" :
                     * Recherche les fichiers ayant des dimensions (largeur x hauteur) différentes de celles spécifiées.
                     *
                     * @param args[3+i] Largeur.
                     * @param args[5+i] Hauteur.
                     */
                    case "-nedim":
                        if (args.length >= 6 + i) {
                            if (args[4 + i].equals("x")) {
                                int largeur = Fichier.larg(args[3 + i]);
                                int haut = Fichier.haut(args[5 + i]);
                                if (haut != -1 && largeur != -1) {
                                    Repertoire.recherche_fichier_dim_dif(fileOrDirectory, haut, largeur, Al);
                                    System.out.println("Liste des fichiers différents de la dimension " + largeur + " x " + haut + "+");
                                }
                            } else {
                                System.out.println("Erreur : veuillez entrer une dimension");
                            }
                        } else {
                            System.out.println("Erreur : veuillez entrer une dimension");
                        }
                        i += 3;
                        break;

                    /**
                     * Option "-nename" :
                     * Recherche les fichiers ayant un nom différent de celui spécifié.
                     *
                     * @param args[3+i] Nom à exclure.
                     */
                    case "-nename":
                        if (args.length >= 4 + i) {
                            Repertoire.recherche_fichier_nom_dif(fileOrDirectory, args[3 + i], Al);
                            System.out.println("Liste des fichiers dont le nom est différent de : " + args[3 + i] + " + ");
                        } else {
                            System.out.println("Erreur : veuillez entrer un nom");
                        }
                        i++;
                        break;
                }
                return i;
    }
   

    
}
