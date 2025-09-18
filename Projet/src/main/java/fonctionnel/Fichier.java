package fonctionnel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.io.Serializable;
/**
 * Classe représentant un fichier et ses métadonnées. 
 * Fournit plusieurs utilitaires pour extraire des informations sur les fichiers.
 */
public class Fichier implements Serializable{
	/**
     * Le chemin absolu du fichier.
     */
	private String chemin;
	/**
     * La date de dernière modification du fichier en millisecondes depuis l'époque Unix (01/01/1970).
     */
	private long modif;
	/**
     * Constructeur de la classe Fichier.
     * Initialise le chemin et la date de dernière modification si le fichier existe.
     * 
     * @param path chemin du fichier à représenter
     */
	private File fich;
	public Fichier (String path) {
		File f = new File (path);
		if (f.exists() && f.isFile()) {
			this.chemin = path;
			this.modif = f.lastModified();
			this.fich=f;
		}
	}
	  /**
     * Affiche les informations détaillées d'un fichier.
     * Vérifie la validité du fichier, son type MIME, et avertit des incohérences éventuelles.
     * 
     * @param filePath chemin du fichier à analyser
     */
    public static void afficherInformationsFichier(String filePath) {
        File file = new File(filePath);

        if (!file.exists() || !file.isFile()) {
            System.out.println("Erreur : Le fichier spécifié n'existe pas ou n'est pas un fichier valide.");
            return;
        }

        try {
            Path path = file.toPath(); //creation d'un chemin type path
            BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
            String mimeType = Files.probeContentType(path);
            String extension = getExtension(file);

            // Avertissement pour les incompatibilités MIME/extension
            if (isImageMimeType(mimeType) && !isImageExtension(extension)) {
                System.out.println("Avertissement : Le fichier a un type MIME d'image (" + mimeType + ") mais son extension (" + extension + ") ne correspond pas.");
            } else if (!isImageMimeType(mimeType) && isImageExtension(extension)) {
                System.out.println("Erreur : Le fichier a une extension d'image (" + extension + ") mais son type MIME (" + mimeType + ") indique que ce n'est pas une image.");
            }

            // Convertir les dates au fuseau horaire local
            ZonedDateTime creationTime = attrs.creationTime().toInstant().atZone(ZoneId.systemDefault());
            ZonedDateTime lastModifiedTime = attrs.lastModifiedTime().toInstant().atZone(ZoneId.systemDefault());

            // Format d'affichage
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");

            // Afficher les informations
            System.out.println("\n======= Informations sur le fichier =======");
            System.out.println("Nom : " + file.getName());
            System.out.println("Chemin absolu : " + file.getAbsolutePath());
            System.out.println("Taille : " + file.length() + " octets");
            System.out.println("Type MIME : " + mimeType);
            System.out.println("Extension : " + extension);
            System.out.println("Date de création : " + creationTime.format(formatter));
            System.out.println("Date de dernière modification : " + lastModifiedTime.format(formatter));

        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du fichier : " + e.getMessage());
        }
    }
    /**
     * Récupère l'extension d'un fichier.
     * 
     * @param file fichier dont on veut obtenir l'extension
     * @return extension du fichier ou "Aucune extension" si aucune extension n'est trouvée
     */
    public static String getExtension(File file) {
        String name = file.getName();
        int lastIndex = name.lastIndexOf(".");
        if (lastIndex == -1) {
            return "Aucune extension";
        }
        return name.substring(lastIndex + 1).toLowerCase();
    }
    /**
     * Vérifie si le type MIME correspond à une image.
     * 
     * @param mimeType le type MIME à vérifier
     * @return true si le type MIME correspond à une image, sinon false
     */
    public static boolean isImageMimeType(String mimeType) {
        return mimeType != null && mimeType.startsWith("image/");
    }
    /**
     * Vérifie si l'extension correspond à un format d'image courant.
     * 
     * @param extension l'extension à vérifier
     * @return true si l'extension correspond à un format d'image, sinon false
     */

    public static boolean isImageExtension(String extension) {
        return extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png") || extension.equals("webp");
    }
    /**
     * Récupère la date de création d'un fichier sous forme de chaîne.
     * 
     * @param f le fichier dont on veut récupérer la date de création
     * @return la date de création au format "dd/MM/yyyy HH:mm:ss" ou une date par défaut en cas d'erreur
     */
    public static String DateDeCreation(File f) {
    	Path path = f.toPath();
    	try {
    	BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);//récupération des attributs du fichier
    	ZonedDateTime creationTime = attrs.creationTime().toInstant().atZone(ZoneId.systemDefault()); //récupération de la date de création
    	String date = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").withZone(java.time.ZoneId.systemDefault()).format(creationTime);//convertissage en string
    	
    	return date;
    	} catch (IOException e) {
            System.out.println("Erreur lors de la lecture des métadonnées du fichier : " + e.getMessage());
            return "00/00/0000";
       }
		//retourne une date arbitraire en cas d'exception controlée
    }
    /**
     * Récupère l'année à partir d'une date formatée.
     *
     * @param date une chaîne de caractères représentant une date au format "dd/MM/yyyy HH:mm:ss"
     * @return l'année sous forme d'entier
     */
    public static int annee(String date) {
        String temp = date.substring(6, 10);
        return Integer.parseInt(temp);
    }

    /**
     * Récupère le jour à partir d'une date formatée.
     *
     * @param date une chaîne de caractères représentant une date au format "dd/MM/yyyy HH:mm:ss"
     * @return le jour sous forme d'entier
     */
    public static int jour(String date) {
        String temp = date.substring(0, 2);
        return Integer.parseInt(temp);
    }

    /**
     * Récupère le mois à partir d'une date formatée.
     *
     * @param date une chaîne de caractères représentant une date au format "dd/MM/yyyy HH:mm:ss"
     * @return le mois sous forme d'entier
     */
    public static int mois(String date) {
        String temp = date.substring(3, 5);
        return Integer.parseInt(temp);
    }

    /**
     * Récupère l'heure à partir d'une date formatée.
     *
     * @param date une chaîne de caractères représentant une date au format "dd/MM/yyyy HH:mm:ss"
     * @return l'heure sous forme d'entier
     */
    public static int heure(String date) {
        String temp = date.substring(11, 13);
        return Integer.parseInt(temp);
    }

    /**
     * Récupère les minutes à partir d'une date formatée.
     *
     * @param date une chaîne de caractères représentant une date au format "dd/MM/yyyy HH:mm:ss"
     * @return les minutes sous forme d'entier
     */
    public static int minute(String date) {
        String temp = date.substring(14, 16);
        return Integer.parseInt(temp);
    }

    /**
     * Récupère les secondes à partir d'une date formatée.
     *
     * @param date une chaîne de caractères représentant une date au format "dd/MM/yyyy HH:mm:ss"
     * @return les secondes sous forme d'entier
     */
    public static int seconde(String date) {
        String temp = date.substring(17, 19);
        return Integer.parseInt(temp);
    }

    /**
     * Convertit une chaîne représentant une largeur en entier.
     *
     * @param largeur largeur sous forme de chaîne
     * @return la largeur convertie en entier ou -1 si la conversion échoue
     */
    public static int larg(String largeur) {
        try {
            return Integer.parseInt(largeur);
        } catch (NumberFormatException e) {
            System.out.println("Erreur : veuillez entrer une largeur valide ");
            return -1;
        }
    }

    /**
     * Convertit une chaîne représentant une hauteur en entier.
     *
     * @param hauteur hauteur sous forme de chaîne
     * @return la hauteur convertie en entier ou -1 si la conversion échoue
     */
    public static int haut(String hauteur) {
        try {
            return Integer.parseInt(hauteur);
        } catch (NumberFormatException e) {
            System.out.println("Erreur : veuillez entrer une hauteur valide ");
            return -1;
        }
    }

    /**
     * Récupère la partie après le dernier "/" d'une chaîne MIME.
     *
     * @param my chaîne représentant un type MIME
     * @return la partie après le dernier "/" en minuscule
     */
    public static String recupMyme(String my) {
        int lastIndex = my.lastIndexOf("/");
        return my.substring(lastIndex + 1).toLowerCase();
    }

    /**
     * Retourne le chemin du fichier.
     *
     * @return le chemin du fichier sous forme de chaîne
     */
    public String get_Path() {
        
        return fich.getAbsolutePath().toLowerCase();
    }

    /**
     * Retourne la date de dernière modification du fichier.
     *
     * @return la date de dernière modification sous forme de timestamp (long)
     */
    public long get_modif() {
        return modif;
    }
    
    

}



