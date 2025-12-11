package fr.cda.findboat.controller;

import fr.cda.findboat.model.Boat;
import fr.cda.findboat.service.PDFService;
import fr.cda.findboat.task.TaskSenEmail;
import fr.cda.findboat.view.EmailForPdfViewInterface;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class EmailForPdfController {

    private List<Boat> boats;
    private final EmailForPdfViewInterface emailForPdfView;

    /**
     * Constructeur du contrôleur d'envoi d'email avec PDF.
     *
     * @param emailForPdfView l'interface de la vue
     */
    public EmailForPdfController(EmailForPdfViewInterface emailForPdfView) {
        this.emailForPdfView = emailForPdfView;
    }

    /**
     * Configure les écouteurs d'événements de la vue.
     */
    public void setupListeners() {
        this.emailForPdfView.setSendEmailListener(e -> sendEmail());
    }

    /**
     * Définit la liste des bateaux à inclure dans le PDF.
     *
     * @param boats la liste des bateaux
     */
    public void setListBoat(List<Boat> boats) {
        this.boats = boats;
    }

    /**
     * Traite l'envoi de l'email avec le PDF des bateaux.
     * Valide l'email, génère le PDF et lance l'envoi en arrière-plan.
     */
    private void sendEmail() {
        String email = emailForPdfView.getEmail();

        if (!isValidEmail(email)) {
            this.emailForPdfView.setErrorMessage("Email invalid");
            return;
        }

        File pdfFile = new File("pdfFile", "boatList.pdf");

        try {
            PDFService.createPDFTable(this.boats, pdfFile);

        } catch (IOException _) {
            this.emailForPdfView.setErrorMessage("Problème lors de la création du fichier PDF.");
            return;
        }
        this.emailForPdfView.closeWindow();

        Thread emailThread = new Thread(new TaskSenEmail(pdfFile, email));
        emailThread.start();
    }

    /**
     * Vérifie si l'email est valide selon un format standard.
     *
     * @param email l'adresse email à valider
     * @return true si l'email est valide, false sinon
     */
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.[a-z]{2,}$");
    }

}