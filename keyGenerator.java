import java.math.*;
import java.util.Random;
import java.security.SecureRandom;

public class tester {
    static int BIT_LENGTH = 2048;
    public static void main(String[] args) {
        BigInteger p, q, n, phi, publicKey, privateKey;
        Boolean prime;

        p = findNumber();
        //p = new BigInteger("123456785");
        prime = p.isProbablePrime(1);
        while (!prime) {
            System.out.println("nonprime-");
            p = findNumber();
            prime = p.isProbablePrime(1);
        }

        q = findNumber();
        //q = new BigInteger("123456785");
        prime = q.isProbablePrime(1);
        while (!prime) {
            q = findNumber();
            prime = q.isProbablePrime(1);
        }

        //find n = p * q
        n = p.multiply(q);

        //find totient of n = (p-1)*(q-1) = phi
        phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        //----key Gen-----
        //public key
        publicKey = new BigInteger("65537");

        //private key
        Random rand = new SecureRandom();
        do publicKey = new BigInteger(phi.bitLength(), rand);
        while (publicKey.compareTo(BigInteger.ONE) <= 0
                || publicKey.compareTo(phi) >= 0
                || !publicKey.gcd(phi).equals(BigInteger.ONE));
        privateKey = publicKey.modInverse(phi);


        System.out.println("public (encryption):");
        System.out.println(publicKey);
        System.out.println("");
        System.out.println("private (decryption):");
        System.out.println(privateKey);
        System.out.println("");
        System.out.println("n (modulus):");
        System.out.println(n);
    }

    public static BigInteger findNumber() {
        Random rand = new SecureRandom();
        BigInteger num = new BigInteger(BIT_LENGTH / 2, 100, rand);
        return num;
    }
