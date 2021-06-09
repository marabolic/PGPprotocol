package etf.openpgp.ba170578dbm170614d.pgp;

import etf.openpgp.ba170578dbm170614d.gui.GenerateKeyForm;
import etf.openpgp.ba170578dbm170614d.gui.PasswordForm;
import org.bouncycastle.bcpg.HashAlgorithmTags;
import org.bouncycastle.bcpg.SymmetricKeyAlgorithmTags;
import org.bouncycastle.bcpg.sig.Features;
import org.bouncycastle.bcpg.sig.KeyFlags;
import org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;
import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.operator.PBESecretKeyEncryptor;
import org.bouncycastle.openpgp.operator.PGPDigestCalculator;
import org.bouncycastle.openpgp.operator.bc.BcPBESecretKeyEncryptorBuilder;
import org.bouncycastle.openpgp.operator.bc.BcPGPContentSignerBuilder;
import org.bouncycastle.openpgp.operator.bc.BcPGPDigestCalculatorProvider;
import org.bouncycastle.openpgp.operator.bc.BcPGPKeyPair;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPKeyPair;

import javax.crypto.spec.DHParameterSpec;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.DSAKeyPairGenerator;
import java.security.interfaces.DSAParams;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class GenerateKeys {

    private KeyPair elgKp;
    private PGPKeyPair pgpKeyPair;
    private KeyPairGenerator generator;
    private PGPPublicKey pgpPublicKey;
    private PGPPrivateKey pgpPrivateKey;

    public static PGPPublicKeyRingCollection keyRingPublic = null;
    public static PGPSecretKeyRingCollection keyRingSecret = null;
    public static Collection<PGPPublicKeyRing> pgpPublicKeyRing;
    public static Collection<PGPSecretKeyRing> pgpSecretKeyRing;

    private ArrayList<PGPPublicKey> pgpPublicKeyArrayList;


    public static void generateKeys(int dsaParam, int elgParam, String email, char [] password){

        pgpPublicKeyRing = new ArrayList<>();
        pgpSecretKeyRing = new ArrayList<>();
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        PGPKeyRingGenerator krgen = generateKeyRingGenerator(dsaParam, elgParam, email, password);

        try {
            PGPPublicKeyRing pkr = krgen.generatePublicKeyRing();
            pgpPublicKeyRing.add(pkr);
            keyRingPublic = new PGPPublicKeyRingCollection(pgpPublicKeyRing);

            PGPSecretKeyRing skr = krgen.generateSecretKeyRing();
            pgpSecretKeyRing.add(skr);
            keyRingSecret = new PGPSecretKeyRingCollection(pgpSecretKeyRing);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PGPException e) {
            e.printStackTrace();
        }

    }


    public final static PGPKeyRingGenerator generateKeyRingGenerator(int dsaParam, int elgParam, String email, char[] password) {


        PGPKeyRingGenerator keyRingGen = null;
        try {
            KeyPairGenerator elgKpg = KeyPairGenerator.getInstance("ELGAMAL", "BC");

            BigInteger g = new BigInteger("153d5d6172adb43045b68ae8e1de1070b6137005686d29d3d73a7749199681ee5b212" +
                    "c9b96bfdcfa5b20cd5e3fd2044895d609cf9b410b7a0f12ca1cb9a428cc", 16);
            BigInteger p = new BigInteger ("9494fec095f3b85ee286542b3836fc81a5dd0a0349b4c239dd38744d488cf8e31db8b" +
                    "cb7d33b41abb9e5a33cca9144b1cef332c94bf0573bf047a3aca98cdf3b", 16);


            DHParameterSpec elParams = new DHParameterSpec(p, g);

            elgKpg.initialize(elParams);
            KeyPair elgKeyPair = elgKpg.generateKeyPair();
            PGPKeyPair encryptionKeyPair = new JcaPGPKeyPair(PGPPublicKey.ELGAMAL_GENERAL, elgKeyPair, new Date());

            KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA");

            kpg.initialize(dsaParam, new SecureRandom());
            KeyPair dsaKeyPair = kpg.generateKeyPair();
            PGPKeyPair signatureKeyPair = new JcaPGPKeyPair(PGPPublicKey.DSA, dsaKeyPair , new Date());



            PGPSignatureSubpacketGenerator signatureSG = new PGPSignatureSubpacketGenerator();
            PGPSignatureSubpacketGenerator encryptionSG = new PGPSignatureSubpacketGenerator();

            PGPDigestCalculator sha1Calc = new BcPGPDigestCalculatorProvider().get(HashAlgorithmTags.SHA1);
            PGPDigestCalculator sha256Calc = new BcPGPDigestCalculatorProvider().get(HashAlgorithmTags.SHA256);

            PBESecretKeyEncryptor pske = (new BcPBESecretKeyEncryptorBuilder(PGPEncryptedData.AES_256,
                    sha256Calc, 0xc0)).build(password);


            signatureSG.setKeyFlags(false, KeyFlags.SIGN_DATA | KeyFlags.CERTIFY_OTHER);

            signatureSG.setPreferredSymmetricAlgorithms(false, new int[]{SymmetricKeyAlgorithmTags.AES_128});
            signatureSG.setPreferredHashAlgorithms(false, new int[]{HashAlgorithmTags.SHA1});

            signatureSG.setFeature(false, Features.FEATURE_MODIFICATION_DETECTION);

            encryptionSG.setKeyFlags(false, KeyFlags.ENCRYPT_COMMS | KeyFlags.ENCRYPT_STORAGE);

            keyRingGen = new PGPKeyRingGenerator(PGPSignature.POSITIVE_CERTIFICATION, signatureKeyPair,
                    email, sha1Calc, signatureSG.generate(), null,
                    new BcPGPContentSignerBuilder(signatureKeyPair.getPublicKey().getAlgorithm(),
                    HashAlgorithmTags.SHA1),pske);

            keyRingGen.addSubKey(encryptionKeyPair, encryptionSG.generate(), null);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException | PGPException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return keyRingGen;
    }




}
