package fonctionnel;
import java.util.ArrayList;
import java.util.regex.*;
import java.io.Serializable;
import java.nio.file.Files;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
/**
 * Classe Repertoire utilisée pour représenter un répertoire et effectuer diverses opérations
 * telles que la collecte de statistiques et la recherche de fichiers.
 */
public class Repertoire implements Serializable {
	/**
	 * Liste des fichiers contenus dans ce répertoire.
	 */
	protected ArrayList<Fichier> tab = new ArrayList<>();

    /**
     * Constructeur qui initialise un répertoire à partir d'un chemin donné.
     *
     * @param path le chemin du répertoire à analyser
     */
    public Repertoire(String path) {
        File d = new File(path);
        if (d.exists() && d.isDirectory()) {
            File[] tabd = d.listFiles();
            int i = 0;
            while (i < tabd.length) {
                if (tabd[i].isDirectory()) {
                    if (!(tabd[i].getName().contains("SnapShot"))) {
                        liste_fichiers(tabd[i]);
                    }
                } else {
                    this.tab.add(new Fichier(tabd[i].getAbsolutePath()));
                }
                i++;
            }
        }
    }

    /**
     * Affiche les statistiques sur les fichiers présents dans un répertoire.
     *
     * @param directoryPath le chemin du répertoire à analyser
     */
    public static void afficherStatistiques(String directoryPath) {
        File directory = new File(directoryPath);

        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("Erreur : Le chemin spécifié n'est pas un répertoire valide.");
            return;
        }

        int totalFiles = 0;
        Map<String, Integer> imageFormats = new HashMap<>();

        totalFiles = collectStatistics(directory, imageFormats);

        int totalImages = imageFormats.values().stream().mapToInt(Integer::intValue).sum();

        System.out.println("\n======= Statistiques du répertoire =======");
        System.out.println("Nombre total de fichiers : " + totalFiles);
        System.out.println("Nombre total d'images : " + totalImages);
        for (Map.Entry<String, Integer> entry : imageFormats.entrySet()) {
            System.out.println("Nombre d'images " + entry.getKey() + " : " + entry.getValue());
        }
    }

    /**
     * Collecte les statistiques sur les fichiers dans un répertoire et ses sous-dossiers.
     *
     * @param directory     le répertoire à analyser
     * @param imageFormats  un map pour stocker les extensions d'images et leurs occurrences
     * @return le nombre total de fichiers trouvés
     */
    public static int collectStatistics(File directory, Map<String, Integer> imageFormats) {
        int totalFiles = 0;
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    totalFiles++;
                    String extension = Fichier.getExtension(file).toLowerCase();
                    if (isImage(extension)) {
                        imageFormats.put(extension, imageFormats.getOrDefault(extension, 0) + 1);
                        String mime = leMyme(file);
                        if(!(Fichier.recupMyme(mime).equals(Fichier.getExtension(file)))) {
                            System.out.println("Avertissement : Le fichier "+file.getPath()+" a un type MIME d'image (" + mime + ") mais son extension (" + Fichier.getExtension(file) + ") ne correspond pas.");
                        }
                    }
                } else if (file.isDirectory()) {
                    totalFiles += collectStatistics(file, imageFormats);
                }
            }
        }
        return totalFiles;
    }
    public static String leMyme(File f) {

        try {
            String mimeType = Files.probeContentType(f.toPath());
            return mimeType;
        } catch (IOException e) {
            System.out.println("Erreur lors de la détection du type MIME: " + e.getMessage());
            return "";
        }
    }

    /**
     * Liste les fichiers présents dans un répertoire et ses sous-dossiers.
     *
     * @param directoryPath le chemin du répertoire à lister
     */
    public static void listerFichiers(String directoryPath) {
        File directory = new File(directoryPath);

        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("Erreur : Le chemin spécifié n'est pas un répertoire valide.");
            return;
        }

        System.out.println("\n======= Liste des fichiers dans le répertoire et ses sous-dossiers =======");
        listFilesRecursively(directory);
    }

    /**
     * Parcourt récursivement un répertoire pour lister tous ses fichiers.
     *
     * @param directory le répertoire à parcourir
     */
    public static void listFilesRecursively(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    System.out.println(file.getName());
                } else if (file.isDirectory()) {
                    listFilesRecursively(file);
                }
            }
        }
    }

    /**
     * Vérifie si une extension de fichier correspond à une image.
     *
     * @param extension l'extension du fichier
     * @return true si c'est une image, sinon false
     */
    public static boolean isImage(String extension) {
        return extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png") || extension.equals("webp");
    }

    /**
     * Liste les fichiers dans un répertoire et ses sous-dossiers.
     *
     * @param d le répertoire à parcourir
     */
    public void liste_fichiers(File d) {
        File[] tabd = d.listFiles();

        int i = 0;
        while (i < tabd.length) {
            if (tabd[i].isDirectory()) {
                if (tabd[i].getName().contains("SnapShot")) {
                    i++;
                } else {
                    liste_fichiers(tabd[i]);
                }
            } else {
                this.tab.add(new Fichier(tabd[i].getAbsolutePath()));
            }
            i++;
        }
    }

    /**
     * Recherche les fichiers ayant une date exacte donnée.
     *
     * @param d        le répertoire à parcourir
     * @param date     la date à rechercher
     * @param fileList la liste des fichiers trouvés
     */
    public static void recherche_fichier_date(File d, String date, ArrayList<File> fileList) {
        File[] tabd = d.listFiles();
        int i = 0;
        while (i < tabd.length) {
            if (tabd[i].isDirectory()) {
                if (!(tabd[i].getName().contains("SnapShot"))) {
                    recherche_fichier_date(tabd[i], date, fileList);
                }
            } else {
                if (DateEgale(tabd[i], date)) {
                    fileList.add(tabd[i]);
                }
            }
            i++;
        }
    }

    /**
     * Recherche les fichiers ayant une date supérieure ou égale à une date donnée.
     *
     * @param d        le répertoire à parcourir
     * @param date     la date de référence
     * @param fileList la liste des fichiers trouvés
     */
    public static void recherche_fichier_date_sup_egale(File d, String date, ArrayList<File> fileList) {
        File[] tabd = d.listFiles();
        int i = 0;
        while (i < tabd.length) {
            if (tabd[i].isDirectory()) {
                if (!(tabd[i].getName().contains("SnapShot"))) {
                    recherche_fichier_date_sup_egale(tabd[i], date, fileList);
                }
            } else {
                if (DateSup(tabd[i], date) || DateEgale(tabd[i], date)) {
                    fileList.add(tabd[i]);
                }
            }
            i++;
        }
    }
    
    /**
     * Recherche les fichiers ayant une date strictement supérieure à la date donnée.
     *
     * @param d        le répertoire à parcourir
     * @param date     la date de référence
     * @param fileList la liste des fichiers trouvés
     */
    public static void recherche_fichier_date_sup(File d, String date, ArrayList<File> fileList) {
        File[] tabd = d.listFiles();
        int i = 0;
        while (i < tabd.length) {
            if (tabd[i].isDirectory()) {
                if (!(tabd[i].getName().contains("SnapShot"))) {
                    recherche_fichier_date_sup(tabd[i], date, fileList);
                }
            } else {
                if (DateSup(tabd[i], date)) {
                    fileList.add(tabd[i]);
                }
            }
            i++;
        }
    }

    /**
     * Recherche les fichiers ayant une date inférieure ou égale à la date donnée.
     *
     * @param d        le répertoire à parcourir
     * @param date     la date de référence
     * @param fileList la liste des fichiers trouvés
     */
    public static void recherche_fichier_date_inf_egale(File d, String date, ArrayList<File> fileList) {
        File[] tabd = d.listFiles();
        int i = 0;
        while (i < tabd.length) {
            if (tabd[i].isDirectory()) {
                if (!(tabd[i].getName().contains("SnapShot"))) {
                    recherche_fichier_date_inf_egale(tabd[i], date, fileList);
                }
            } else {
                if (!DateSup(tabd[i], date)) {
                    fileList.add(tabd[i]);
                }
            }
            i++;
        }
    }

    /**
     * Recherche les fichiers ayant une date strictement inférieure à la date donnée.
     *
     * @param d        le répertoire à parcourir
     * @param date     la date de référence
     * @param fileList la liste des fichiers trouvés
     */
    public static void recherche_fichier_date_inf(File d, String date, ArrayList<File> fileList) {
        File[] tabd = d.listFiles();
        int i = 0;
        while (i < tabd.length) {
            if (tabd[i].isDirectory()) {
                if (!(tabd[i].getName().contains("SnapShot"))) {
                    recherche_fichier_date_inf(tabd[i], date, fileList);
                }
            } else {
                if (!DateSup(tabd[i], date) && !(DateEgale(tabd[i], date))) {
                    fileList.add(tabd[i]);
                }
            }
            i++;
        }
    }

    /**
     * Recherche les fichiers ayant une date différente de la date donnée.
     *
     * @param d        le répertoire à parcourir
     * @param date     la date de référence
     * @param fileList la liste des fichiers trouvés
     */
    public static void recherche_fichier_date_dif(File d, String date, ArrayList<File> fileList) {
        File[] tabd = d.listFiles();
        int i = 0;
        while (i < tabd.length) {
            if (tabd[i].isDirectory()) {
                if (!(tabd[i].getName().contains("SnapShot"))) {
                    recherche_fichier_date_dif(tabd[i], date, fileList);
                }
            } else {
                if (!(DateEgale(tabd[i], date))) {
                    fileList.add(tabd[i]);
                }
            }
            i++;
        }
    }

    /**
     * Recherche les fichiers ayant un nom exact donné.
     *
     * @param d        le répertoire à parcourir
     * @param nom      le nom du fichier recherché
     * @param fileList la liste des fichiers trouvés
     */
    public static void recherche_fichier_nom(File d, String nom, ArrayList<File> fileList) {
        File[] tabd = d.listFiles();
        int i = 0;
        while (i < tabd.length) {
            if (tabd[i].isDirectory()) {
                if (!(tabd[i].getName().contains("SnapShot"))) {
                    recherche_fichier_nom(tabd[i], nom, fileList);
                }
            } else {
                if (nom.equals(tabd[i].getName())) {
                    fileList.add(tabd[i]);
                }
            }
            i++;
        }
    }

    /**
     * Recherche les fichiers ayant un nom différent de celui donné.
     *
     * @param d        le répertoire à parcourir
     * @param nom      le nom du fichier à exclure
     * @param fileList la liste des fichiers trouvés
     */
    public static void recherche_fichier_nom_dif(File d, String nom, ArrayList<File> fileList) {
        File[] tabd = d.listFiles();
        int i = 0;
        while (i < tabd.length) {
            if (tabd[i].isDirectory()) {
                if (!(tabd[i].getName().contains("SnapShot"))) {
                    recherche_fichier_nom_dif(tabd[i], nom, fileList);
                }
            } else {
                if (!(nom.equals(tabd[i].getName()))) {
                    fileList.add(tabd[i]);
                }
            }
            i++;
        }
    }

    /**
     * Recherche les fichiers ayant des dimensions exactes données (hauteur et largeur).
     *
     * @param d        le répertoire à parcourir
     * @param h        la hauteur recherchée
     * @param l        la largeur recherchée
     * @param fileList la liste des fichiers trouvés
     */
    public static void recherche_fichier_dim(File d, int h, int l, ArrayList<File> fileList) {
        File[] tabd = d.listFiles();
        int i = 0;
        while (i < tabd.length) {
            if (tabd[i].isDirectory()) {
                if (!(tabd[i].getName().contains("SnapShot"))) {
                    recherche_fichier_dim(tabd[i], h, l, fileList);
                }
            } else {
                Image im = new Image(tabd[i]);
                if (im.estExif()) {
                    int hau = im.hauteur();
                    int la = im.largeur();
                    if ((la == l) && (h == hau)) {
                        fileList.add(tabd[i]);
                    }
                }
            }
            i++;
        }
    }

    /**
     * Recherche les fichiers ayant des dimensions différentes des dimensions données.
     *
     * @param d        le répertoire à parcourir
     * @param h        la hauteur à exclure
     * @param l        la largeur à exclure
     * @param fileList la liste des fichiers trouvés
     */
    public static void recherche_fichier_dim_dif(File d, int h, int l, ArrayList<File> fileList) {
        File[] tabd = d.listFiles();
        int i = 0;
        while (i < tabd.length) {
            if (tabd[i].isDirectory()) {
                if (!(tabd[i].getName().contains("SnapShot"))) {
                    recherche_fichier_dim_dif(tabd[i], h, l, fileList);
                }
            } else {
                Image im = new Image(tabd[i]);
                if (im.estExif()) {
                    int hau = im.hauteur();
                    int la = im.largeur();
                    if ((la != l) || (h != hau)) {
                        fileList.add(tabd[i]);
                    }
                }
            }
            i++;
        }
    }
    /**
     * Recherche les fichiers ayant une hauteur strictement supérieure à la hauteur donnée.
     *
     * @param d        le répertoire à parcourir
     * @param h        la hauteur minimale recherchée
     * @param fileList la liste des fichiers trouvés
     */
    public static void recherche_fichier_hauteur_sup(File d, int h, ArrayList<File> fileList) {
        File[] tabd = d.listFiles();
        int i = 0;
        while (i < tabd.length) {
            if (tabd[i].isDirectory()) {
                if (!(tabd[i].getName().contains("SnapShot"))) {
                    recherche_fichier_hauteur_sup(tabd[i], h, fileList);
                }
            } else {
                Image im = new Image(tabd[i]);
                if (im.estExif()) {
                    int hau = im.hauteur();
                    if ((h < hau && hau != -1)) {
                        fileList.add(tabd[i]);
                    }
                }
            }
            i++;
        }
    }

    /**
     * Recherche les fichiers ayant une hauteur supérieure ou égale à la hauteur donnée.
     *
     * @param d        le répertoire à parcourir
     * @param h        la hauteur minimale recherchée
     * @param fileList la liste des fichiers trouvés
     */
    public static void recherche_fichier_hauteur_sup_egale(File d, int h, ArrayList<File> fileList) {
        File[] tabd = d.listFiles();
        int i = 0;
        while (i < tabd.length) {
            if (tabd[i].isDirectory()) {
                if (!(tabd[i].getName().contains("SnapShot"))) {
                    recherche_fichier_hauteur_sup_egale(tabd[i], h, fileList);
                }
            } else {
                Image im = new Image(tabd[i]);
                if (im.estExif()) {
                    int hau = im.hauteur();
                    if ((h <= hau && hau != -1)) {
                        fileList.add(tabd[i]);
                    }
                }
            }
            i++;
        }
    }

    /**
     * Recherche les fichiers ayant une hauteur strictement inférieure à la hauteur donnée.
     *
     * @param d        le répertoire à parcourir
     * @param h        la hauteur maximale recherchée
     * @param fileList la liste des fichiers trouvés
     */
    public static void recherche_fichier_hauteur_inf(File d, int h, ArrayList<File> fileList) {
        File[] tabd = d.listFiles();
        int i = 0;
        while (i < tabd.length) {
            if (tabd[i].isDirectory()) {
                if (!(tabd[i].getName().contains("SnapShot"))) {
                    recherche_fichier_hauteur_inf(tabd[i], h, fileList);
                }
            } else {
                Image im = new Image(tabd[i]);
                if (im.estExif()) {
                    int hau = im.hauteur();
                    if ((h > hau) && hau != -1) {
                        fileList.add(tabd[i]);
                    }
                }
            }
            i++;
        }
    }

    /**
     * Recherche les fichiers ayant une hauteur inférieure ou égale à la hauteur donnée.
     *
     * @param d        le répertoire à parcourir
     * @param h        la hauteur maximale recherchée
     * @param fileList la liste des fichiers trouvés
     */
    public static void recherche_fichier_hauteur_inf_egale(File d, int h, ArrayList<File> fileList) {
        File[] tabd = d.listFiles();
        int i = 0;
        while (i < tabd.length) {
            if (tabd[i].isDirectory()) {
                if (!(tabd[i].getName().contains("SnapShot"))) {
                    recherche_fichier_hauteur_inf_egale(tabd[i], h, fileList);
                }
            } else {
                Image im = new Image(tabd[i]);
                if (im.estExif()) {
                    int hau = im.hauteur();
                    if ((h >= hau) && hau != -1) {
                        fileList.add(tabd[i]);
                    }
                }
            }
            i++;
        }
    }

    /**
     * Recherche les fichiers ayant une hauteur égale à la hauteur donnée.
     *
     * @param d        le répertoire à parcourir
     * @param h        la hauteur recherchée
     * @param fileList la liste des fichiers trouvés
     */
    public static void recherche_fichier_hauteur_egale(File d, int h, ArrayList<File> fileList) {
        File[] tabd = d.listFiles();
        int i = 0;
        while (i < tabd.length) {
            if (tabd[i].isDirectory()) {
                if (!(tabd[i].getName().contains("SnapShot"))) {
                    recherche_fichier_hauteur_egale(tabd[i], h, fileList);
                }
            } else {
                Image im = new Image(tabd[i]);
                if (im.estExif()) {
                    int hau = im.hauteur();
                    if ((h == hau) && hau != -1) {
                        fileList.add(tabd[i]);
                    }
                }
            }
            i++;
        }
    }

    /**
     * Recherche les fichiers ayant une largeur inférieure ou égale à la largeur donnée.
     *
     * @param d        le répertoire à parcourir
     * @param l        la largeur maximale recherchée
     * @param fileList la liste des fichiers trouvés
     */
    public static void recherche_fichier_largeur_inf_egale(File d, int l, ArrayList<File> fileList) {
        File[] tabd = d.listFiles();
        int i = 0;
        while (i < tabd.length) {
            if (tabd[i].isDirectory()) {
                if (!(tabd[i].getName().contains("SnapShot"))) {
                    recherche_fichier_largeur_inf_egale(tabd[i], l, fileList);
                }
            } else {
                Image im = new Image(tabd[i]);
                if (im.estExif()) {
                    int larg = im.largeur();
                    if ((l >= larg) && larg != -1) {
                        fileList.add(tabd[i]);
                    }
                }
            }
            i++;
        }
    }

    /**
     * Recherche les fichiers ayant une largeur strictement inférieure à la largeur donnée.
     *
     * @param d        le répertoire à parcourir
     * @param l        la largeur maximale recherchée
     * @param fileList la liste des fichiers trouvés
     */
    public static void recherche_fichier_largeur_inf(File d, int l, ArrayList<File> fileList) {
        File[] tabd = d.listFiles();
        int i = 0;
        while (i < tabd.length) {
            if (tabd[i].isDirectory()) {
                if (!(tabd[i].getName().contains("SnapShot"))) {
                    recherche_fichier_largeur_inf(tabd[i], l, fileList);
                }
            } else {
                Image im = new Image(tabd[i]);
                if (im.estExif()) {
                    int larg = im.largeur();
                    if ((l > larg) && larg != -1) {
                        fileList.add(tabd[i]);
                    }
                }
            }
            i++;
        }
    }
    /**
     * Recherche les fichiers ayant une largeur strictement supérieure à la largeur donnée.
     *
     * @param d        le répertoire à parcourir
     * @param l        la largeur minimale recherchée
     * @param fileList la liste des fichiers trouvés
     */

    public static void recherche_fichier_largeur_sup(File d, int l,ArrayList<File> fileList) {
    	File [] tabd = d.listFiles();
    	int i =0;
    	while (i<tabd.length) {
    		if(tabd[i].isDirectory()) {
    			if(!(tabd[i].getName().contains("SnapShot"))) {
					recherche_fichier_largeur_sup(tabd[i],l,fileList);
				}
    		}
    		else {
    			Image im = new Image(tabd[i]);
    			if(im.estExif()) {
    				int larg = im.largeur();
    				if( (l < larg) && larg!=-1) {
    					fileList.add(tabd[i]);
    				}
    			}
    		}
    		i = i+1;
    	}
    }
    /**
     * Recherche les fichiers ayant une largeur supérieure ou égale à la largeur donnée.
     *
     * @param d        le répertoire à parcourir
     * @param l        la largeur minimale recherchée
     * @param fileList la liste des fichiers trouvés
     */
    public static void recherche_fichier_largeur_sup_egale(File d, int l,ArrayList<File> fileList) {
    	File [] tabd = d.listFiles();
    	int i =0;
    	while (i<tabd.length) {
    		if(tabd[i].isDirectory()) {
    			if(!(tabd[i].getName().contains("SnapShot"))) {
					recherche_fichier_largeur_sup_egale(tabd[i],l,fileList);
				}
    		}
    		else {
    			Image im = new Image(tabd[i]);
    			if(im.estExif()) {
    				int larg = im.largeur();
    				if( (l <=larg) && larg!=-1) {
    					fileList.add(tabd[i]);
    				}
    			}
    		}
    		i = i+1;
    	}
    }
    /**
     * Recherche les fichiers ayant une largeur égale à la largeur donnée.
     *
     * @param d        le répertoire à parcourir
     * @param l        la largeur recherchée
     * @param fileList la liste des fichiers trouvés
     */
    public static void recherche_fichier_largeur_egale(File d, int l,ArrayList<File> fileList) {
    	File [] tabd = d.listFiles();
    	int i =0;
    	while (i<tabd.length) {
    		if(tabd[i].isDirectory()) {
    			if(!(tabd[i].getName().contains("SnapShot"))) {
					recherche_fichier_largeur_egale(tabd[i],l,fileList);
				}
    		}
    		else {
    			Image im = new Image(tabd[i]);
    			if(im.estExif()) {
    				int larg = im.largeur();
    				if( (l ==larg) && larg!=-1) {
    					fileList.add(tabd[i]);
    				}
    			}
    		}
    		i = i+1;
    	}
    }
    /**
     * Recherche les fichiers ayant une largeur différente de la largeur donnée.
     *
     * @param d        le répertoire à parcourir
     * @param l        la largeur à exclure
     * @param fileList la liste des fichiers trouvés
     */
    public static void recherche_fichier_largeur_dif(File d, int l,ArrayList<File> fileList) {
    	File [] tabd = d.listFiles();
    	int i =0;
    	while (i<tabd.length) {
    		if(tabd[i].isDirectory()) {
    			if(!(tabd[i].getName().contains("SnapShot"))) {
					recherche_fichier_largeur_dif(tabd[i],l,fileList);
				}
    		}
    		else {
    			Image im = new Image(tabd[i]);
    			if(im.estExif()) {
    				int larg = im.largeur();
    				if( (l !=larg) && larg!=-1) {
    					fileList.add(tabd[i]);
    				}
    			}
    		}
    		i = i+1;
    	}
    }
    /**
     * Recherche les fichiers ayant une hauteur différente de la hauteur donnée.
     *
     * @param d        le répertoire à parcourir
     * @param h        la hauteur à exclure
     * @param fileList la liste des fichiers trouvés
     */
    public static void recherche_fichier_hauteur_dif (File d, int h,ArrayList<File> fileList) {
    	File [] tabd = d.listFiles();
    	int i =0;
    	while (i<tabd.length) {
    		if(tabd[i].isDirectory()) {
    			if(!(tabd[i].getName().contains("SnapShot"))) {
					recherche_fichier_hauteur_dif(tabd[i],h,fileList);
				}
    		}
    		else {
    			Image im = new Image(tabd[i]);
    			if(im.estExif()) {
    				int hau = im.hauteur();
    				if( (h != hau) && hau!=-1) {
    					fileList.add(tabd[i]);
    				}
    			}
    		}
    		i = i+1;
    	}
    }
    
    
    
    
    
    
    
    /**
     * Affiche les chemins des fichiers contenus dans une liste.
     *
     * @param list la liste des fichiers à afficher
     */
    
    public static void affichage( ArrayList<File> list) {
    	for (int i =0; i<list.size();i++) {
    		System.out.println(list.get(i).getPath());
    	}
    }
    
    
    
    /**
     * Vérifie si la date de création d'un fichier est égale à une date donnée.
     *
     * @param f     le fichier à vérifier
     * @param date1 la date à comparer
     * @return true si la date correspond, false sinon
     */
    private static boolean DateEgale(File f,String date1) {
    	
    	String date = Fichier.DateDeCreation(f);
    	return date.equals(date1) ;
    }
    /**
     * Vérifie si la date de création d'un fichier est strictement supérieure à une date donnée.
     *
     * @param f     le fichier à vérifier
     * @param date1 la date à comparer
     * @return true si la date du fichier est supérieure, false sinon
     */
    private static boolean DateSup(File f,String date1) {
    	String date = Fichier.DateDeCreation(f);
    	int annee1 = Fichier.annee(date1);
    	int jour1 = Fichier.jour(date1);
    	int mois1 = Fichier.mois(date1);
    	int heure1 = Fichier.heure(date1);
    	int minute1 = Fichier.minute(date1);
    	int seconde1 = Fichier.seconde(date1);
    	int annee = Fichier.annee(date);
    	int jour = Fichier.jour(date);
    	int mois = Fichier.mois(date);
    	int heure = Fichier.heure(date);
    	int minute = Fichier.minute(date);
    	int seconde = Fichier.seconde(date);
    	if (annee>annee1) {
    		return true;
    	}
    	else if(annee==annee1) {
    		if(mois>mois1) {
    			return true;
    		}
    		else if (mois == mois1) {
    			if( jour>jour1) {
    				return true;
    			}
    			else if(jour == jour1) {
    				if(heure > heure1 ) {
    					return true;
    				}
    				else if(heure == heure1) {
    					if(minute>minute1) {
    						return true;
    					}
    					else if(minute == minute1) {
    						return (seconde>seconde1 ) ;
    					}
    				}
    			}
    		}
    	}
    	return false;
    }
    /**
     * Vérifie si une chaîne de caractères correspond au format d'une date valide avec heure.
     *
     * @param d la chaîne de caractères à vérifier
     * @return true si la chaîne est une date valide, false sinon
     */
    public static boolean IsDate(String d) {
        // La regex corrigée pour valider une date avec l'heure (jj/mm/aaaa HH:mm:ss)
        String typechaine = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4} (0[0-9]|1[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";
        
        // Création du pattern à partir de la regex
        Pattern pattern = Pattern.compile(typechaine);
        
        // Création du matcher
        Matcher matcher = pattern.matcher(d);
        
        // Retourne true si la chaîne correspond à l'expression régulière, sinon false
        return matcher.matches();
    }
    /**
     * Compare deux listes de fichiers et retourne une nouvelle liste contenant
     * les fichiers communs aux deux listes.
     *
     * @param c1 La première liste de fichiers.
     * @param c2 La deuxième liste de fichiers.
     * @return Une liste contenant les fichiers présents à la fois dans c1 et c2.
     */
    public static ArrayList<File> compare2(ArrayList<File> c1, ArrayList<File> c2) {
        ArrayList<File> result = new ArrayList<>();
        for (int i = 0; i < c1.size(); i++) {
            boolean trouve = false;
            File f1 = c1.get(i);
            for (int j = 0; j < c2.size() && !trouve; j++) {
                File f2 = c2.get(j);
                if (f2.equals(f1)) {
                    result.add(f2);
                    trouve = true;
                }
            }
        }
        return result;
    }

    /**
     * Compare trois listes de fichiers et retourne une nouvelle liste contenant
     * les fichiers communs aux trois listes.
     *
     * @param c1 La première liste de fichiers.
     * @param c2 La deuxième liste de fichiers.
     * @param c3 La troisième liste de fichiers.
     * @return Une liste contenant les fichiers présents dans c1, c2 et c3.
     */
    public static ArrayList<File> compare3(ArrayList<File> c1, ArrayList<File> c2, ArrayList<File> c3) {
        ArrayList<File> result = new ArrayList<>();
        for (int i = 0; i < c1.size(); i++) {
            File f1 = c1.get(i);
            boolean trouve = false;
            for (int j = 0; j < c2.size() && !trouve; j++) {
                File f2 = c2.get(j);
                for (int l = 0; l < c3.size() && !trouve; l++) {
                    File f3 = c3.get(l);
                    if (f1.equals(f2) && f2.equals(f3)) {
                        result.add(f1);
                        trouve = true;
                    }
                }
            }
        }
        return result;
    }

}

    
