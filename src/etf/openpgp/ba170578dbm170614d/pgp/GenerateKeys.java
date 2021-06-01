package etf.openpgp.ba170578dbm170614d.pgp;

import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPKeyPair;

import javax.crypto.spec.DHParameterSpec;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.util.Date;

public class GenerateKeys {

    private KeyPair elgKp;
    private PGPKeyPair pgpKeyPair;
    private KeyPairGenerator generator;
    private PGPPublicKey pgpPublicKey;
    private  PGPPrivateKey pgpPrivateKey;


    public GenerateKeys(){
        KeyPairGenerator    elgKpg = null;
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        try {
            elgKpg = KeyPairGenerator.getInstance("ELGAMAL", "BC");

            BigInteger g = new BigInteger("153d5d6172adb43045b68ae8e1de1070b6137005686d29d3d73a7749199681ee5b212c9b96bfdcfa5b20cd5e3fd2044895d609cf9b410b7a0f12ca1cb9a428cc", 16);
            BigInteger p = new BigInteger("9494fec095f3b85ee286542b3836fc81a5dd0a0349b4c239dd38744d488cf8e31db8bcb7d33b41abb9e5a33cca9144b1cef332c94bf0573bf047a3aca98cdf3b", 16);

            DHParameterSpec elParams = new DHParameterSpec(p, g);

            elgKpg.initialize(elParams);

            elgKp = elgKpg.generateKeyPair();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }


    }

    public PGPKeyPair generateKeys() {
        PGPKeyPair elgKeyPair = null;
        try {
            elgKeyPair = new JcaPGPKeyPair(PGPPublicKey.ELGAMAL_ENCRYPT, elgKp, new Date());
            pgpPublicKey = elgKeyPair.getPublicKey();
            pgpPrivateKey = elgKeyPair.getPrivateKey();
            System.out.println("Private key:" + pgpPrivateKey.getPrivateKeyDataPacket());
            System.out.println("publicKey.getKeyID: " + pgpPublicKey);

        } catch (PGPException e) {
            e.printStackTrace();
        }
            return elgKeyPair;
    }

}
