package fr.cda.findboat.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.SecureRandom;
import java.util.Base64;

public class AESKeyManager {

    private static final File aesKeysFolder = new File("keys");
    private static final String aesKeyPath = aesKeysFolder + File.separator + "aes.key";
    private final static Logger log =  LoggerFactory.getLogger(AESKeyManager.class);

    /**
     * Génère une clé AES si elle n'existe pas encore, puis la sauvegarde
     * dans le répertoire prévu à cet effet.
     *
     * @throws Exception si une erreur survient lors de la génération ou de l'écriture du fichier
     */
    public static void generateAndSaveKey() throws Exception {

        if (isKeyFileCreated()) {
            return;
        }

        aesKeysFolder.mkdir();

        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128, SecureRandom.getInstanceStrong());
        SecretKey key = keyGen.generateKey();

        String b64 = Base64.getEncoder().encodeToString(key.getEncoded());
        Files.write(Paths.get(aesKeyPath), b64.getBytes("UTF-8"), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        log.info("Une nouvelle clé aes générée et enregistrée");
    }

    /**
     * Charge la clé AES depuis le fichier encodé en Base64.
     *
     * @return SecretKey
     * @throws Exception si le fichier est introuvable, vide ou corrompu
     */
    public static SecretKey loadKeyFromFile() throws Exception {
        String b64 = new String(Files.readAllBytes(Paths.get(aesKeyPath)), "UTF-8").trim();
        byte[] keyBytes = Base64.getDecoder().decode(b64);
        return new SecretKeySpec(keyBytes, "AES");
    }

    /**
     * Vérifie si le fichier de clé AES a déjà été créé dans le répertoire défini.
     *
     * @return boolean
     */
    private static boolean isKeyFileCreated() {
        if (aesKeysFolder.exists()) {
            File keyFile = new File(aesKeyPath);
            return keyFile.exists();
        } else {
            return false;
        }
    }
}
