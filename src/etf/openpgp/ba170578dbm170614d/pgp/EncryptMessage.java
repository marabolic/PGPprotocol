package etf.openpgp.ba170578dbm170614d.pgp;



import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.bcpg.HashAlgorithmTags;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.operator.PGPContentSignerBuilder;
import org.bouncycastle.openpgp.operator.bc.*;
import org.bouncycastle.util.io.Streams;


import java.io.*;
import java.security.SecureRandom;
import java.security.Security;
import java.security.SignatureException;
import java.util.Date;
import java.util.Iterator;


public class EncryptMessage {

    /**
     *
     * @param message
     * @param publicKey
     * @param conversion
     * @param compression
     * @param symetricAlg
     * @return
     */
    public static boolean EncryptMessage(String message, PGPPublicKey publicKey, boolean conversion, boolean compression, int symetricAlg) {
        OutputStream encryptMessage = null;
        try {
            // output, enkriptovanaPoruka
            encryptMessage = new FileOutputStream("encryptedMessage.pgp");

            // da ispisem u messageForEncrypt.txt sve sto smo napisali u TextArea
            FileWriter myWriter = null;

            myWriter = new FileWriter("messageForEncrypt.txt");
            myWriter.write(message);
            myWriter.close();

            // obavezno mora
            Security.addProvider(new BouncyCastleProvider());

            // ako smo cerkirali konverziju da je uradi
            if (conversion)
                encryptMessage = new ArmoredOutputStream(encryptMessage);

            // Simetricni algoritam za kriptovanje poruke
            BcPGPDataEncryptorBuilder dataEncryptor = null;
            if (symetricAlg == 0) {
                dataEncryptor = new BcPGPDataEncryptorBuilder(PGPEncryptedData.TRIPLE_DES);
            } else {
                dataEncryptor = new BcPGPDataEncryptorBuilder(PGPEncryptedData.AES_128);
            }

            // obezbedjivanje integriteta
            dataEncryptor.setWithIntegrityPacket(true);
            dataEncryptor.setSecureRandom(new SecureRandom());

            PGPEncryptedDataGenerator encryptedDataGenerator = new PGPEncryptedDataGenerator(dataEncryptor);

            // kljuc sa kojim kriptujemo poruku
            BcPublicKeyKeyEncryptionMethodGenerator bc = new BcPublicKeyKeyEncryptionMethodGenerator(publicKey);
            encryptedDataGenerator.addMethod(bc);

            PGPCompressedDataGenerator commpressedDataGenerator = null;

            // ako smo cerkirali konverziju
            if (compression)
                commpressedDataGenerator = new PGPCompressedDataGenerator(PGPCompressedData.ZIP);
            else
                commpressedDataGenerator = new PGPCompressedDataGenerator(PGPCompressedData.UNCOMPRESSED);


            ByteArrayOutputStream byteArrayOutStream = new ByteArrayOutputStream();
            PGPUtil.writeFileToLiteralData(commpressedDataGenerator.open(byteArrayOutStream), PGPLiteralData.BINARY, new File("messageForEncrypt.txt"));

            commpressedDataGenerator.close();

            encryptMessage = encryptedDataGenerator.open(encryptMessage, byteArrayOutStream.toByteArray().length);
            encryptMessage.write(byteArrayOutStream.toByteArray());
            encryptMessage.close();

        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        } catch (PGPException e) {
            return false;
        }
        return true;
    }

    /**
     *
     * @param message
     * @param name
     * @param publicKey
     * @param secretKey
     * @param privateKey
     * @param conversion
     * @param compression
     * @param encryption
     * @param symetricAlg
     * @return
     */
    public static boolean SignEncryptMessage(String message, String name, PGPPublicKey publicKey, PGPSecretKey secretKey, PGPPrivateKey privateKey, boolean conversion, boolean compression, boolean encryption, int symetricAlg) {
        OutputStream encryptMessage = null;
        try {
            // output
            encryptMessage = new FileOutputStream("encryptedMessage.pgp");

            // ispis iz textArea u txt
            FileWriter myWriter = null;

            myWriter = new FileWriter("messageForEncrypt.txt");
            myWriter.write(message);
            myWriter.close();

            Security.addProvider(new BouncyCastleProvider());
            PGPEncryptedDataGenerator encryptedDataGenerator = null;




            BcPGPDataEncryptorBuilder dataEncryptor = null;
            if (symetricAlg == 0) {
                dataEncryptor = new BcPGPDataEncryptorBuilder(PGPEncryptedData.TRIPLE_DES);
            } else {
                dataEncryptor = new BcPGPDataEncryptorBuilder(PGPEncryptedData.AES_128);
            }

            OutputStream outputCompress = null;

            PGPCompressedDataGenerator commpressedDataGenerator = null;

            // ako smo naznacili enkripciju da samo uradimo integritet i enkriptovanje poruke sa javnim kljucem
            if (encryption) {
                dataEncryptor.setWithIntegrityPacket(true);
                dataEncryptor.setSecureRandom(new SecureRandom());

                encryptedDataGenerator = new PGPEncryptedDataGenerator(dataEncryptor);

                BcPublicKeyKeyEncryptionMethodGenerator bc = new BcPublicKeyKeyEncryptionMethodGenerator(publicKey);
                encryptedDataGenerator.addMethod(bc);
            }
            OutputStream encrOut = null;
            if(encryption){
                encrOut = encryptedDataGenerator.open(encryptMessage, new byte[1 << 16]);
            }

            // kompresija ako je cerkirana
            if (compression)
                commpressedDataGenerator = new PGPCompressedDataGenerator(PGPCompressedData.ZIP);
            else
                commpressedDataGenerator = new PGPCompressedDataGenerator(PGPCompressedData.UNCOMPRESSED);

            if (encryption) {
                outputCompress = commpressedDataGenerator.open(encrOut, new byte[1 << 16]);
            }else{
                outputCompress = commpressedDataGenerator.open(encryptMessage, new byte[1 << 16]);
            }

            PGPContentSignerBuilder signerBuilder = new BcPGPContentSignerBuilder(secretKey.getPublicKey().getAlgorithm(), HashAlgorithmTags.SHA1);
            PGPSignatureGenerator signatureGenerator = new PGPSignatureGenerator(signerBuilder);

            signatureGenerator.init(PGPSignature.BINARY_DOCUMENT, privateKey);
            PGPSignatureSubpacketGenerator signatureSubpacket = new PGPSignatureSubpacketGenerator();
            signatureSubpacket.setSignerUserID(false, name);
            signatureGenerator.setHashedSubpackets(signatureSubpacket.generate());

            signatureGenerator.generateOnePassVersion(false).encode(outputCompress);

            PGPLiteralDataGenerator literalDataGenerator = new PGPLiteralDataGenerator();
            OutputStream literalOut = null;

            literalOut = literalDataGenerator.open(outputCompress, PGPLiteralData.BINARY, "messageForEncrypt.txt", new Date(), new byte[1 << 16]);

            // ovo cita poruku i enkriptuje ga sa svim podesavanjima od gore bajt po bajt
            FileInputStream in = new FileInputStream("messageForEncrypt.txt");;
            try {
                byte[] buffer = new byte[1 << 16];
                int i;
                while ((i = in.read(buffer)) > 0) {
                    literalOut.write(buffer, 0, i);
                    signatureGenerator.update(buffer, 0, i);
                }
                in.close();
                literalDataGenerator.close();

            } catch (FileNotFoundException e) {
                return false;
            } catch (IOException e) {
                return false;
            }

            signatureGenerator.generate().encode(outputCompress);

            // konverzija ako je cerkirana
            if (conversion)
                encryptMessage = new ArmoredOutputStream(encryptMessage);

            commpressedDataGenerator.close();
            if (encryption) {
                encryptedDataGenerator.close();
            }

            if(conversion){
                encryptMessage.close();
            }

        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PGPException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     *
     * @param passwd
     * @param o
     */
    public static void DecryptMessage(char [] passwd, Object o){

    }

    /**
     *
     * @param o
     */
    public static void ValidateMessage(Object o){

    }

}
