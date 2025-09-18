package fonctionnel;

import java.io.*;

/**
 * Classe Snapshot permettant de sauvegarder l'état d'un répertoire et de comparer les modifications 
 * entre deux snapshots.
 */
public class Snapshot implements Serializable {
	 /**
     * Constructeur par défaut de la classe Snapshot.
     * Initialise une instance permettant la gestion des instantanés.
     */
    public Snapshot() {
        // Constructeur par défaut
    }

    /**
     * Méthode pour sauvegarder l'état d'un répertoire dans un snapshot.
     *
     * @param path le chemin du répertoire à sauvegarder
     */
    public static void sauvegarder(String path) {
        try {
            String pathSnap = path + "/SnapShot";
            File dossier = new File(pathSnap);

            if (dossier.mkdir()) {
                System.out.println("Dossier snapshot créé avec le snapshot.");
            } else {
                System.out.println("Dossier déjà créé dans le dossier " + path + "\nCréation d'un autre snapshot.");
            }

            File[] tabd = dossier.listFiles();
            int nmb = tabd != null ? tabd.length : 0;
            File file = new File(pathSnap, "snapshot" + nmb + ".ser");

            Repertoire rep = new Repertoire(path);
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(rep);
            oos.close();

        } catch (FileNotFoundException e) {
            System.err.println("Erreur fichier non trouvé.");
        } catch (NotSerializableException e) {
            System.err.println("Erreur de la sérialization.");
        } catch (IOException e) {
            System.err.println("Erreur d'entrée/sortie : " + e.getMessage());
        }
    }

    /**
     * Méthode pour comparer l'état actuel d'un répertoire avec un snapshot.
     *
     * @param path le chemin du répertoire actuel
     * @param snap le chemin du fichier snapshot à comparer
     */
    public static void comparer(String path, String snap) {
        Repertoire rep = new Repertoire(path);
        Nombre_supprimer(snap, rep);
        System.out.println("----------------------------------------------\n");
        Nombre_ajouter(snap, rep);
        System.out.println("----------------------------------------------\n");
        Fichiers_modifié(snap, rep);
        System.out.println("----------------------------------------------\n");
    }

    /**
     * Méthode privée pour identifier les fichiers supprimés depuis la dernière sauvegarde.
     *
     * @param pathSnap le chemin du fichier snapshot
     * @param r        l'état actuel du répertoire
     */
    private static void Nombre_supprimer(String pathSnap, Repertoire r) {
        File snap = new File(pathSnap);

        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(snap));
            Repertoire r1 = (Repertoire) (ois.readObject());
            int cp = 0;
            boolean trouve = false;

            System.out.println("*** Noms de tous les fichiers supprimés depuis la dernière sauvegarde ***");

            for (int i = 0; i < r1.tab.size(); i++) {
                for (int j = 0; j < r.tab.size() && !trouve; j++) {
                    if (r1.tab.get(i).get_Path().equals(r.tab.get(j).get_Path())) {
                        trouve = true;
                    }
                }
                if (!trouve) {
                    System.out.println("Le fichier : " + r1.tab.get(i).get_Path() + " a été supprimé.");
                    cp++;
                } else {
                    trouve = false;
                }
            }

            System.out.println("** Nombre de fichiers supprimés : " + cp + " **");

        } catch (FileNotFoundException e) {
            System.err.println("Erreur fichier non trouvé.");
        } catch (ClassNotFoundException e) {
            System.err.println("Erreur de la sérialization.");
        } catch (IOException e) {
            System.err.println("Erreur d'entrée/sortie : " + e.getMessage());
        }
    }

    /**
     * Méthode privée pour identifier les fichiers ajoutés depuis la dernière sauvegarde.
     *
     * @param snapP le chemin du fichier snapshot
     * @param r     l'état actuel du répertoire
     */
    private static void Nombre_ajouter(String snapP, Repertoire r) {
        File snap = new File(snapP);

        if (snap.exists()) {
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(snap));
                Repertoire r1 = (Repertoire) (ois.readObject());
                int cp = 0;

                System.out.println("*** Noms de tous les fichiers ajoutés depuis la dernière sauvegarde ***");

                for (int i = 0; i < r.tab.size(); i++) {
                    boolean trouve = false;

                    for (int j = 0; j < r1.tab.size() && !trouve; j++) {
                        if (r.tab.get(i).get_Path().equals(r1.tab.get(j).get_Path())) {
                            trouve = true;
                        }
                    }

                    if (!trouve) {
                        System.out.println("Le fichier : " + r.tab.get(i).get_Path() + " a été ajouté.");
                        cp++;
                    }
                }

                System.out.println("** Nombre de fichiers ajoutés : " + cp + " **");

            } catch (FileNotFoundException e) {
                System.err.println("Erreur fichier non trouvé.");
            } catch (ClassNotFoundException e) {
                System.err.println("Erreur de la sérialization.");
            } catch (IOException e) {
                System.err.println("Erreur d'entrée/sortie : " + e.getMessage());
            }
        }
    }

    /**
     * Méthode privée pour identifier les fichiers modifiés depuis la dernière sauvegarde.
     *
     * @param pathSnap le chemin du fichier snapshot
     * @param r        l'état actuel du répertoire
     */
    private static void Fichiers_modifié(String pathSnap, Repertoire r) {
        File snap = new File(pathSnap);

        if (snap.exists()) {
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(snap));
                Repertoire r1 = (Repertoire) (ois.readObject());
                int ind = 0;
                int cp = 0;
                boolean trouve = false;

                System.out.println("*** Noms de tous les fichiers modifiés depuis la dernière sauvegarde ***");

                for (int i = 0; i < r.tab.size(); i++) {
                    for (int j = 0; j < r1.tab.size() && !trouve; j++) {
                        if (r.tab.get(i).get_Path().equals(r1.tab.get(j).get_Path())) {
                            trouve = true;
                            ind = j;
                        }
                    }

                    if (trouve) {
                        long m1 = r1.tab.get(ind).get_modif();
                        long m2 = r.tab.get(i).get_modif();

                        if (m1 != m2) {
                            cp++;
                            System.out.println("Le fichier " + r.tab.get(i).get_Path() + " a été modifié.");
                        }

                        trouve = false;
                    }
                }

                System.out.println("** Nombre de fichiers modifiés : " + cp + " **");

            } catch (FileNotFoundException e) {
                System.err.println("Erreur fichier non trouvé.");
            } catch (ClassNotFoundException e) {
                System.err.println("Erreur de la sérialization.");
            } catch (IOException e) {
                System.err.println("Erreur d'entrée/sortie : " + e.getMessage());
            }
        }
    }
}

		
		
	



