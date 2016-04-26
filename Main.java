import java.math.*;
import java.util.*;
import java.security.SecureRandom;
import java.io.*;

public class Main {
    static int BIT_LENGTH = 1024;
    static BigInteger n, publicKey, privateKey;
    static Scanner sIn = new Scanner(System.in); //user input

    public static void main(String[] args) throws IOException {
            // Local variable
        int swValue;

        // Display menu graphics
        System.out.println("============================");
        System.out.println("|     -RSA Encryption-     |");
        System.out.println("============================");
        System.out.println("| Options:                 |");
        System.out.println("|     1. Interactive       |");
        System.out.println("|     2. Generate Keys     |");
        System.out.println("|     3. Encrypt           |");
        System.out.println("|     4. Decrypt           |");
        System.out.println("|     5. Exit              |");
        System.out.println("============================");
        swValue = Keyin.inInt(" Select option: ");

        // Switch construct
        switch (swValue) {
            case 1:
                System.out.println("Interactive Mode Selected...");
                generateKeys();
                interactiveMode();
                break;
            case 2:
                System.out.println("Generate Keys Selected...");
                generateKeys();
                break;
            case 3:
                System.out.println("Encryption Selected");

                break;
            case 4:
                System.out.println("Decryption Selected");
                System.exit(0);
                break;
            case 5:
                System.out.println("Exit Selected");
                break;
            default:
                System.out.println("-Error-");
                break;
        }


        //generateKeys();
        //encryptFile("File.txt", "EncryptedOUT.txt"); //
        //System.out.println("Finished encryption");
        //System.out.println("----------Beginning Decryption--------");
        //decryptFile("EncryptedOUT.txt", "DecryptedOUT.txt");
    }

    public static void interactiveMode() throws IOException {
        //System.out.println("Would you like to print these result to a file?");
        //String input = sIn.nextLine();

        //if (input == "y" || input == "Y" || input == "yes" || input == "Yes"){
        //    System.out.println("Printing to file...")
        //    writeKeyToFile();
        //} else  if (input == "n" || input == "N" || input == "no" || input == "No"){
        //    System.out.println("You entered no")
        //} else {
        //    System.out.println("Please enter a valid response...");
        //    interactiveMode();
        //}
        //System.out.println("");
        encryptFile("File.txt", "EncryptedOUT.txt");
        System.out.println("Finished encryption");
        System.out.println("----------Beginning Decryption--------");
        decryptFile("EncryptedOUT.txt", "DecryptedOUT.txt");
    }

    public static void encryptFile(String iFile, String oFile) throws IOException {
        File file = new File(iFile);
        File fOut = new File(oFile);
        if (!fOut.exists()) {
            fOut.createNewFile();
        }
        //FileOutputStream fos = new FileOutputStream(fOut);
        //BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        FileWriter fw = new FileWriter(fOut.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        String word, subMes;
        BigInteger encrypted;

        try {
            Scanner scan = new Scanner(file);

            while (scan.hasNext()) { //each word sperated by space
                word = scan.next();
                //System.out.println(word);
                String numWord = strToNumFormat(word);
                //System.out.println(numWord);

                int length = numWord.length(), index=3;
                String subWord = "";
                while(index < length+3) {
                    subWord = numWord.substring(index-3, index);

                    encrypted = encrypt(subWord);
                    //encrypted = encrypt(new BigInteger(word.toCharArray()));
                    //System.out.println("Encrypted--"+encrypted.toString());
                    bw.write(encrypted.toString());
                    bw.write(" ");
                    index+=3;
                }
                encrypted = encryptSpace();
                bw.write(encrypted.toString());
                bw.write(" ");
            }

        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        //catch(IOException e)
        //{
        //    e.printStackTrace();
        //}
        bw.close();
    }

    public static void decryptFile(String iFile, String oFIle) throws IOException {
        File file = new File(iFile); //INPUT FILE
        File fOut = new File(oFIle); //OUTPUT FILE
        FileOutputStream fos = new FileOutputStream(fOut);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        String word;
        BigInteger decrypted;

        try {
            Scanner scan = new Scanner(file);

            while (scan.hasNext()) {
                word = scan.next();
                //System.out.println(word);
                decrypted = decrypt(word);
                String letter = numFormatToStr(decrypted.toString());
                //System.out.println("decrypted letter: "+letter);
                //decrypted = decrypt(new BigInteger(word));
                bw.write(letter.toString());
                //if (letter)
                //bw.write(" ");
            }

        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        //catch(IOException e)
        //{
        //    e.printStackTrace();
        //}
        bw.close();
    }

    public static void generateKeys() {
        System.out.println("Generating keys..");
        BigInteger p, q, phi;
        Boolean prime;

        p = findNumber();
        prime = isPrime(p);
        while (!prime) {
            p = findNumber();
            prime = isPrime(p);
        }
        System.out.println("Found first prime!");

        q = findNumber();
        prime = isPrime(q);
        while (!prime) {
            q = findNumber();
            prime = isPrime(q);
        }
        System.out.println("Found second prime!");

        //find n = p * q
        n = p.multiply(q);

        //find totient of n = (p-1)*(q-1) = phi
        phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        //----key Gen-----
        System.out.println("Generating keys...");
        Random rand = new SecureRandom();

        do publicKey = new BigInteger(phi.bitLength(), new Random());
        while( (publicKey.compareTo(phi) != 1)
                ||
                (publicKey.gcd(phi).compareTo(BigInteger.valueOf(1)) != 0));
            privateKey = publicKey.modInverse(phi);


        System.out.println("public (encryption):");
        System.out.println(publicKey);
        System.out.println("");
        System.out.println("private (decryption):");
        System.out.println(privateKey);
        System.out.println("");
        System.out.println("n (modulus):");
        System.out.println(n);

        return;
    }

    public static void printKeyToFile(){
        //stuff
    }

    public static String strToNumFormat(String word) {
        char ch;
        String numFormat = "", encrypted;
        int length = word.length(), num, index = 0;

        Scanner scan = new Scanner(word);

        while(index < length)
        {
            ch = word.charAt(index);
            num = (int)ch;
            //numFormat = numFormat.concat(num);
            // converts accii to numbers associated from
            if(num < 100) {
                numFormat = numFormat + "0" + Integer.toString(num);
            }
            if(num < 10) {
                numFormat = numFormat + "00" + Integer.toString(num);
            }
            if(num >= 100) {
                numFormat = numFormat + Integer.toString(num);
            }

            index++;
        }

        //System.out.println("numFormat12:"+numFormat);
        return numFormat;
    }

    public static String numFormatToStr(String word) {
        //System.out.println("Format to String...");
        char ch;
        String numFormat = "", encrypted;
        int length = word.length(), num, index = 0;

        num = Integer.parseInt(word);
        //System.out.println(num);
        ch = (char)num;
        //System.out.println(ch);


        //System.out.println(numFormat);
        return String.valueOf(ch);
    }

    public static BigInteger encrypt(String message) {
        // will use global values publicKey and n
        BigInteger exponent, result, number;
        number = new BigInteger(message);
        result = number.modPow(publicKey,n);

        return result;
    }

    public static BigInteger encryptSpace() {
        // will use global values publicKey and n
        BigInteger exponent, result, number=new BigInteger("32");
        result = number.modPow(publicKey,n);

        return result;
    }

    public static BigInteger decrypt(String message) {
        // will use global values
        BigInteger exponent, result, number;
        number = new BigInteger(message);

        //   m^e mod n
        result = number.modPow(privateKey,n);

        return result;
    }

    public static BigInteger findNumber() {
        Random rand = new SecureRandom();
        BigInteger num = new BigInteger(BIT_LENGTH / 2, 100, rand);
        return num;
    }

    public static boolean isPrime(BigInteger number) {
        if (!number.isProbablePrime(5))
            return false;

        BigInteger two = new BigInteger("2");
        if (!two.equals(number) && BigInteger.ZERO.equals(number.mod(two)))
            return false;

//        for (BigInteger i = new BigInteger("3"); i.multiply(i).compareTo(number) < 1; i = i.add(two)) {
//            if (BigInteger.ZERO.equals(number.mod(i)))
//                return false;
//        }
        return true;
    }
}


class Keyin {

    //*******************************
    //   support methods
    //*******************************
    //Method to display the user's prompt string
    public static void printPrompt(String prompt) {
        System.out.print(prompt + " ");
        System.out.flush();
    }

    //Method to make sure no data is available in the
    //input stream
    public static void inputFlush() {
        int dummy;
        int bAvail;

        try {
            while ((System.in.available()) != 0)
                dummy = System.in.read();
        } catch (java.io.IOException e) {
            System.out.println("Input error");
        }
    }

    //********************************
    //  data input methods for
    //string, int, char, and double
    //********************************
    public static String inString(String prompt) {
        inputFlush();
        printPrompt(prompt);
        return inString();
    }

    public static String inString() {
        int aChar;
        String s = "";
        boolean finished = false;

        while (!finished) {
            try {
                aChar = System.in.read();
                if (aChar < 0 || (char) aChar == '\n')
                    finished = true;
                else if ((char) aChar != '\r')
                    s = s + (char) aChar; // Enter into string
            }

            catch (java.io.IOException e) {
                System.out.println("Input error");
                finished = true;
            }
        }
        return s;
    }

    public static int inInt(String prompt) {
        while (true) {
            inputFlush();
            printPrompt(prompt);
            try {
                return Integer.valueOf(inString().trim()).intValue();
            }

            catch (NumberFormatException e) {
                System.out.println("Invalid input. Not an integer");
            }
        }
    }

    public static char inChar(String prompt) {
        int aChar = 0;

        inputFlush();
        printPrompt(prompt);

        try {
            aChar = System.in.read();
        }

        catch (java.io.IOException e) {
            System.out.println("Input error");
        }
        inputFlush();
        return (char) aChar;
    }

    public static double inDouble(String prompt) {
        while (true) {
            inputFlush();
            printPrompt(prompt);
            try {
                return Double.valueOf(inString().trim()).doubleValue();
            }

            catch (NumberFormatException e) {
                System.out
                        .println("Invalid input. Not a floating point number");
            }
        }
    }
}