package etf.openpgp.ba170578dbm170614d.pgp;

import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.operator.bc.BcPGPDataEncryptorBuilder;
import org.bouncycastle.openpgp.operator.bc.BcPublicKeyKeyEncryptionMethodGenerator;


import java.io.*;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Collection;
import java.util.Iterator;

public class EncryptMessage {
    public static boolean EncryptMessage(String message, PGPPublicKey publicKey, boolean conversion, boolean compression, int symetricAlg) {
        OutputStream encryptMessage = null;
        try {
            encryptMessage = new FileOutputStream("enkriptovano.pgp");
        } catch (FileNotFoundException e) {
            return false;
        }

        FileWriter myWriter = null;
        try {
            myWriter = new FileWriter("messageForEncrypt.txt");
            myWriter.write(message);
            myWriter.close();
        } catch (IOException e) {
            return false;
        }


        Security.addProvider(new BouncyCastleProvider());

        if (conversion)
            encryptMessage = new ArmoredOutputStream(encryptMessage);

        ByteArrayOutputStream byteArrayOutStream = new ByteArrayOutputStream();

        try {
            PGPCompressedDataGenerator commpressedDataGenerator = null;

            if (compression)
                commpressedDataGenerator = new PGPCompressedDataGenerator(PGPCompressedData.ZIP);
            else
                commpressedDataGenerator = new PGPCompressedDataGenerator(PGPCompressedData.UNCOMPRESSED);



            PGPUtil.writeFileToLiteralData(commpressedDataGenerator.open(byteArrayOutStream), PGPLiteralData.BINARY, new File("messageForEncrypt.txt"));

            commpressedDataGenerator.close();
        } catch (IOException e) {
            return false;
        }


        BcPGPDataEncryptorBuilder dataEncryptor = null;
        if (symetricAlg == 0) {
            dataEncryptor = new BcPGPDataEncryptorBuilder(PGPEncryptedData.TRIPLE_DES);
        } else {
            dataEncryptor = new BcPGPDataEncryptorBuilder(PGPEncryptedData.AES_128);
        }

        dataEncryptor.setWithIntegrityPacket(true);
        dataEncryptor.setSecureRandom(new SecureRandom());

        PGPEncryptedDataGenerator encryptedDataGenerator = new PGPEncryptedDataGenerator(dataEncryptor);

        BcPublicKeyKeyEncryptionMethodGenerator bc = new BcPublicKeyKeyEncryptionMethodGenerator(publicKey);
        encryptedDataGenerator.addMethod(bc);

        byte[] bytes = byteArrayOutStream.toByteArray();

        try {
            encryptMessage = encryptedDataGenerator.open(encryptMessage, bytes.length);
            encryptMessage.write(bytes);
            encryptMessage.close();
        } catch (IOException e) {
            return false;
        } catch (PGPException e) {
            return false;
        }
        return true;
    }

}
