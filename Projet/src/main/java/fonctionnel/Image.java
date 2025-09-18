package fonctionnel;

import java.io.File;
import com.drew.imaging.*;
import com.drew.metadata.*;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import com.drew.metadata.xmp.XmpDirectory;

/**
 * Classe représentant une image et permettant l'extraction de métadonnées EXIF et XMP.
 */
public class Image {
	/**
	 * Constante représentant l'échelle par défaut utilisée pour redimensionner les images.
	 * Cette valeur est généralement utilisée comme paramètre dans les méthodes de redimensionnement.
	 */
    public static final int SCALE_DEFAULT = 0; // Échelle par défaut
    private File file;

    /**
     * Constructeur pour initialiser une instance avec un fichier image.
     *
     * @param file le fichier image à traiter
     */
    public Image(File file) {
        this.file = file;
    }

    /**
     * Méthode pour extraire toutes les métadonnées (EXIF et XMP) de l'image.
     *
     * @return une chaîne contenant les métadonnées EXIF et XMP
     */
    public String ExtractAllMetadata() {
        String exifMetadata = ExtractExifMetadata();
        String xmpMetadata = ExtractXmpMetadata();
        return "EXIF Metadata:\n" + exifMetadata + "\nXMP Metadata:\n" + xmpMetadata;
    }

    /**
     * Méthode pour extraire les métadonnées EXIF de l'image.
     *
     * @return une chaîne contenant les métadonnées EXIF
     */
    public String ExtractExifMetadata() {
        StringBuilder exifInfo = new StringBuilder();

        try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);

            // Extraction de la résolution (DPI)
            ExifIFD0Directory ifd0Directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            if (ifd0Directory != null) {
                exifInfo.append("DPI: ")
                        .append(ifd0Directory.getInteger(ExifIFD0Directory.TAG_X_RESOLUTION))
                        .append(" x ")
                        .append(ifd0Directory.getInteger(ExifIFD0Directory.TAG_Y_RESOLUTION))
                        .append("\n");
            }

            // Extraction des dimensions (largeur et hauteur)
            ExifSubIFDDirectory dir = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
            if (dir != null) {
                Integer width = dir.getInteger(ExifSubIFDDirectory.TAG_EXIF_IMAGE_WIDTH);
                Integer height = dir.getInteger(ExifSubIFDDirectory.TAG_EXIF_IMAGE_HEIGHT);

                if (width != null && height != null) {
                    exifInfo.append("Dimensions: ").append(width).append(" x ").append(height).append("\n");
                } else {
                    exifInfo.append("Dimensions: Non disponibles.\n");
                }
            }

            // Extraction des informations GPS
            GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
            if (gpsDirectory != null) {
                exifInfo.append("GPS Location: ").append(gpsDirectory.getGeoLocation()).append("\n");
            }

        } catch (Exception e) {
            exifInfo.append("Error extracting EXIF metadata: ").append(e.getMessage()).append("\n");
        }

        return exifInfo.toString();
    }

    /**
     * Méthode pour extraire les métadonnées XMP de l'image.
     *
     * @return une chaîne contenant les métadonnées XMP
     */
    public String ExtractXmpMetadata() {
        StringBuilder xmpInfo = new StringBuilder();

        try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            XmpDirectory xmpDirectory = metadata.getFirstDirectoryOfType(XmpDirectory.class);

            if (xmpDirectory != null) {
                String title = xmpDirectory.getXmpProperties().get("dc:title[1]");
                String description = xmpDirectory.getXmpProperties().get("dc:description[1]");
                String creator = xmpDirectory.getXmpProperties().get("dc:creator[1]");

                if (title != null) {
                    xmpInfo.append("Titre : ").append(title).append("\n");
                }
                if (description != null) {
                    xmpInfo.append("Description : ").append(description).append("\n");
                }
                if (creator != null) {
                    xmpInfo.append("Createur : ").append(creator).append("\n");
                }
            } else {
                xmpInfo.append("Il n'y a pas de métadonnées XMP.\n");
            }

        } catch (Exception e) {
            xmpInfo.append("Erreur de l'extraction des métadonnées XMP: ").append(e.getMessage()).append("\n");
        }

        return xmpInfo.toString();
    }

    /**
     * Vérifie si l'image contient des métadonnées EXIF.
     *
     * @return {@code true} si des métadonnées EXIF sont présentes, sinon {@code false}
     */
    public boolean estExif() {
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            ExifIFD0Directory ifd0Directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            ExifSubIFDDirectory dir = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
            return dir != null && ifd0Directory != null;
        } catch (Exception e) {
            System.err.println("Erreur lors de l'extraction");
            return false;
        }
    }

    /**
     * Récupère la hauteur de l'image à partir des métadonnées EXIF.
     *
     * @return la hauteur en pixels ou -1 en cas d'erreur
     */
    public int hauteur() {
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            ExifSubIFDDirectory dir = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
            Integer hauteur = dir.getInteger(ExifSubIFDDirectory.TAG_EXIF_IMAGE_HEIGHT);
            return hauteur;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Récupère la largeur de l'image à partir des métadonnées EXIF.
     *
     * @return la largeur en pixels ou -1 en cas d'erreur
     */
    public int largeur() {
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            ExifSubIFDDirectory dir = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
            Integer largeur = dir.getInteger(ExifSubIFDDirectory.TAG_EXIF_IMAGE_WIDTH);
            return largeur;
        } catch (Exception e) {
            return -1;
        }
    }
}



