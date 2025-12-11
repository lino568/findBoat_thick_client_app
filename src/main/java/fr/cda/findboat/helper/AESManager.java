package fr.cda.findboat.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.io.FilenameUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.File;
import java.security.SecureRandom;
import java.util.Base64;

public class AESManager {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final File dbInfoFolder = new File("dbInfo");
    private static final File dbInfoFile = new File(dbInfoFolder, "databaseInfo.enc");

    /**
     * Chiffre un ObjectNode JSON et le sauvegarde dans un fichier .enc
     *
     * @param jsonObject L'objet JSON à chiffrer
     * @param key La clé AES
     * @return File Le fichier créé contenant les données chiffrées
     * @throws Exception si une erreur survient lors du chiffrement ou de l'écriture du fichier
     */
    public static File encryptObjectNode(ObjectNode jsonObject, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        // Génère un IV aléatoire (16 octets)
        byte[] iv = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);

        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));

        // Convertit l'ObjectNode en bytes directement (sans passer par String)
        byte[] jsonBytes = mapper.writeValueAsBytes(jsonObject);

        // Chiffre les données
        byte[] encrypted = cipher.doFinal(jsonBytes);

        // Combine IV + données chiffrées
        byte[] combined = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);

        // Crée le répertoire s'il n'existe pas
        dbInfoFolder.mkdirs();

        java.nio.file.Files.write(dbInfoFile.toPath(), combined);

        return dbInfoFile;
    }

    /**
     * Déchiffre un fichier .enc et retourne l'ObjectNode JSON
     *
     * @param key La clé AES
     * @return ObjectNode Le JSON déchiffré
     * @throws Exception si une erreur survient lors du déchiffrement
     */
    public static ObjectNode decryptToObjectNode(SecretKey key) throws Exception {
        // Lit le fichier chiffré
        byte[] combined = java.nio.file.Files.readAllBytes(dbInfoFile.toPath());

        // Sépare IV et ciphertext
        byte[] iv = new byte[16];
        byte[] encrypted = new byte[combined.length - 16];
        System.arraycopy(combined, 0, iv, 0, 16);
        System.arraycopy(combined, 16, encrypted, 0, encrypted.length);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));

        // Déchiffre les données
        byte[] decryptedBytes = cipher.doFinal(encrypted);

        // Convertit les bytes directement en ObjectNode
        return mapper.readValue(decryptedBytes, ObjectNode.class);
    }
}
