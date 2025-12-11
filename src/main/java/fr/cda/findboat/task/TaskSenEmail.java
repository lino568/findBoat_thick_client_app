package fr.cda.findboat.task;

import com.mashape.unirest.http.exceptions.UnirestException;
import fr.cda.findboat.service.MGEmail;
import java.io.File;

public class TaskSenEmail implements Runnable {

    private String emailReceiver;
    private File emailFile;

    public TaskSenEmail(File attachment, String emailReceiver) {
        this.emailReceiver = emailReceiver;
        this.emailFile = attachment;
    }

    @Override
    public void run() {
        try {
            MGEmail.sendEmailWithAttachment(this.emailFile);
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }

    }
}
