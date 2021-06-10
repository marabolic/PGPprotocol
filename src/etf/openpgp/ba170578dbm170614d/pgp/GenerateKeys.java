package etf.openpgp.ba170578dbm170614d.pgp;

import etf.openpgp.ba170578dbm170614d.gui.GenerateKeyForm;
import etf.openpgp.ba170578dbm170614d.gui.PasswordForm;
import org.bouncycastle.bcpg.HashAlgorithmTags;
import org.bouncycastle.bcpg.SymmetricKeyAlgorithmTags;
import org.bouncycastle.bcpg.sig.Features;
import org.bouncycastle.bcpg.sig.KeyFlags;
import org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;
import org.bouncycastle.jce.spec.ElGamalParameterSpec;
import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.operator.PBESecretKeyEncryptor;
import org.bouncycastle.openpgp.operator.PGPDigestCalculator;
import org.bouncycastle.openpgp.operator.bc.BcPBESecretKeyEncryptorBuilder;
import org.bouncycastle.openpgp.operator.bc.BcPGPContentSignerBuilder;
import org.bouncycastle.openpgp.operator.bc.BcPGPDigestCalculatorProvider;
import org.bouncycastle.openpgp.operator.bc.BcPGPKeyPair;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPContentSignerBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPDigestCalculatorProviderBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPKeyPair;
import org.bouncycastle.openpgp.operator.jcajce.JcePBESecretKeyEncryptorBuilder;

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

    public static PGPPublicKeyRingCollection keyRingPublic = null;
    public static PGPSecretKeyRingCollection keyRingSecret = null;
    public static Collection<PGPPublicKeyRing> pgpPublicKeyRing = new ArrayList<>();
    public static Collection<PGPSecretKeyRing> pgpSecretKeyRing = new ArrayList<>();

    public static void generateKeys(int dsaParam, int elgParam, String email, char [] password){

        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        PGPKeyRingGenerator krgen = generateKeyRingGenerator(dsaParam, elgParam, email, password);
        System.out.println("krgen: " + krgen);
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
            BigInteger primeModulous = new BigInteger("36F0255DDE973DCB3B399D747F23E32ED6FDB1F77598338BFDF44159C4EC64DDAEB5F78671CBFB22106AE64C32C5BCE4CFD4F5920DA0EBC8B01ECA9292AE3DBA1B7A4A899DA181390BB3BD1659C81294F400A3490BF9481211C79404A576605A5160DBEE83B4E019B6D799AE131BA4C23DFF83475E9C40FA6725B7C9E3AA2C6596E9C05702DB30A07C9AA2DC235C5269E39D0CA9DF7AAD44612AD6F88F69699298F3CAB1B54367FB0E8B93F735E7DE83CD6FA1B9D1C931C41C6188D3E7F179FC64D87C5D13F85D704A3AA20F90B3AD3621D434096AA7E8E7C66AB683156A951AEA2DD9E76705FAEFEA8D71A5755355970000000000000001", 16);
            BigInteger baseGenerator = new BigInteger("2", 16);
            ElGamalParameterSpec paramSpecs = new ElGamalParameterSpec(primeModulous, baseGenerator);

            KeyPair dsaKeyPair = generateDsaKeyPair(dsaParam);
            KeyPair elGamalKeyPair = generateElGamalKeyPair(elgParam);
            //KeyPair elGamalKeyPair1 = generateElGamalKeyPair(paramSpecs);

            keyRingGen = createPGPKeyRingGenerator(dsaKeyPair, elGamalKeyPair, email, password);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException | PGPException e) {
            System.out.println("Error encrypting key");
            e.printStackTrace();

        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keyRingGen;
    }



    public static final PGPKeyRingGenerator createPGPKeyRingGenerator(KeyPair dsaKeyPair, KeyPair elGamalKeyPair, String identity, char[] passphrase) throws Exception
    {
        PGPKeyPair dsaPgpKeyPair = new JcaPGPKeyPair(PGPPublicKey.DSA, dsaKeyPair, new Date());
        PGPKeyPair elGamalPgpKeyPair = new JcaPGPKeyPair(PGPPublicKey.ELGAMAL_ENCRYPT, elGamalKeyPair, new Date());
        PGPDigestCalculator sha1Calc = new JcaPGPDigestCalculatorProviderBuilder().build().get(HashAlgorithmTags.SHA1);
        PGPKeyRingGenerator keyRingGen = new PGPKeyRingGenerator(PGPSignature.POSITIVE_CERTIFICATION, dsaPgpKeyPair, identity, sha1Calc,
                null, null, new JcaPGPContentSignerBuilder(dsaPgpKeyPair.getPublicKey().getAlgorithm(),
                HashAlgorithmTags.SHA1), new JcePBESecretKeyEncryptorBuilder(PGPEncryptedData.AES_256, sha1Calc).setProvider("BC").build(passphrase));

        keyRingGen.addSubKey(elGamalPgpKeyPair);
        return keyRingGen;
    }
    public static final KeyPair generateDsaKeyPair(int keySize) throws NoSuchAlgorithmException, NoSuchProviderException
    {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA", "BC");
        keyPairGenerator.initialize(keySize, new SecureRandom());
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }

    public static final KeyPair generateElGamalKeyPair(int keySize) throws Exception
    {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ELGAMAL", "BC");
        keyPairGenerator.initialize(keySize);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }

    public static final KeyPair generateElGamalKeyPair(ElGamalParameterSpec paramSpecs) throws Exception
    {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ELGAMAL", "BC");
        keyPairGenerator.initialize(paramSpecs);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }


}
