package fr.cda.findboat.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import fr.cda.findboat.model.Boat;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PDFService {

    private static final Logger log = LoggerFactory.getLogger(PDFService.class);

    private PDFService() {
    }

    /**
     * Ouvre une fenêtre de sélection et génère le PDF à l'emplacement choisi
     * @param boats Liste des bateaux à exporter
     * @param ownerWindow Fenêtre parente pour le FileChooser
     */
    public static void createPDFWithDialog(List<Boat> boats, Window ownerWindow) {
        FileChooser fileChooser = new FileChooser();

        // Configuration du FileChooser
        fileChooser.setTitle("Enregistrer le PDF");
        fileChooser.setInitialFileName(java.time.Instant.now().getEpochSecond() + "_liste_bateaux.pdf");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")+ File.separator + "Downloads"));

        // Filtre pour les fichiers PDF
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("Fichiers PDF (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);

        // Afficher la fenêtre et récupérer le fichier
        File file = fileChooser.showSaveDialog(ownerWindow);

        // Si l'utilisateur a choisi un emplacement
        if (file != null) {
            try {
                createPDFTable(boats, file);
            } catch (IOException e) {
                log.error("Erreur lors de la création du PDF", e);
                throw new RuntimeException("Une erreur est survenue lors de la création du fichier PDF.");
            }
        } else {
            log.info("Création du PDF annulée par l'utilisateur");
        }
    }

    /**
     * Crée le PDF à l'emplacement spécifié
     * @param boats Liste des bateaux
     * @param file Fichier de destination
     */
    public static void createPDFTable(List<Boat> boats, File file) throws IOException {
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(file));
        Document doc = new Document(pdfDoc);

        doc.add(new Paragraph("Liste des bateaux"));

        float[] columnWidths = {4, 8, 1, 1, 1};
        Table table = new Table(columnWidths);

        table.setMarginTop(5);
        table.setMarginBottom(5);
        table.setMarginLeft(5);
        table.setMarginRight(5);

        table.addHeaderCell("Titre");
        table.addHeaderCell("Description");
        table.addHeaderCell("Longueur");
        table.addHeaderCell("Année");
        table.addHeaderCell("Capacité");

        for (Boat boat : boats) {
            table.addCell(boat.getTitle());
            table.addCell(boat.getDescription());
            table.addCell(String.valueOf(boat.getLength()));
            table.addCell(String.valueOf(boat.getYear()));
            table.addCell(String.valueOf(boat.getCapacity()));
        }

        doc.add(table);
        doc.close();
    }
}