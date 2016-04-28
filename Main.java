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
        System.out.println("|     1. Demo Mode         |");
        System.out.println("|     2. Generate Keys     |");
        System.out.println("|     3. Encrypt           |");
        System.out.println("|     4. Decrypt           |");
        System.out.println("|     5. Exit              |");
        System.out.println("============================");
        swValue = Keyin.inInt(" Select option: ");

        // Switch construct
        switch (swValue) {
            case 1:
                System.out.println("Demo Selected...");
                generateKeys();
                interactiveMode();
                break;
            case 2:
                System.out.println("Generate Keys Selected...");
                generateKeys();
                break;
            case 3:
                System.out.println("Encryption Selected");
                System.out.println("");

                boolean keepGoing = true;
                while (keepGoing) {
                    System.out.println("Please enter file to encrypt, must be a .txt format!!");
                    String input = sIn.nextLine();

                    if (input.equals("")) {
                        System.out.println("Please enter a valid respone");
                    } else {
                        File fInput = new File(input);
                        if (!fInput.exists()) {
                            System.out.println("Please enter valid file name!");
                        } else {
                            generateKeys();
                            encryptFile(input, "EncryptedOUT.txt");
                            keepGoing = false;
                        }
                    }
                }
                break;
            case 4:
                System.out.println("Decryption Selected");
                System.out.println("");

                keepGoing = true;
                while (keepGoing) {
                    System.out.println("Please enter file to decrypt, must be a .txt format!!");
                    String input = sIn.nextLine();

                    if (input.equals("")) {
                        System.out.println("Please enter a valid respone");
                    } else {
                        File fInput = new File(input);
                        if (!fInput.exists()) {
                            System.out.println("Please enter valid file name!");
                        } else {
                            //generateKeys();
                            loadKeysFromFile(0,1);

                            decryptFile(input, "DecryptedOUT.txt");
                            keepGoing = false;
                        }
                    }
                }
                System.exit(0);
                break;
            case 5:
                System.out.println("Exit Selected");
                System.exit(0);
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


        System.out.println("Would you like to encrypt file? (File.txt)");
        String input = sIn.nextLine();

        if (input.equals("y") || input.equals("Y") || input.equals("yes") || input.equals("Yes")){
            //System.out.println("Encrypting...");
        } else  if (input.equals("n") || input.equals("N") || input.equals("no") || input.equals("No")){
            System.out.println("Exiting...");
            System.exit(0);
        } else {
            System.out.println("You entered: "+input);
            System.out.println("Please enter a valid response...");
            interactiveMode();
        }

        encryptFile("File.txt", "EncryptedOUT.txt");
        System.out.println("Finished encryption");

        boolean keepGoing = true;
        while (keepGoing) {
            System.out.println("Would you like to decrypt file?");
            input = sIn.nextLine();

            if (input.equals("y") || input.equals("Y") || input.equals("yes") || input.equals("Yes")) {
                //System.out.println("Decrypting...");
                keepGoing = false;
            } else if (input.equals("n") || input.equals("N") || input.equals("no") || input.equals("No")) {
                System.out.println("Exiting...");
                System.exit(0);
            } else {
                System.out.println("You entered: " + input);
                System.out.println("Please enter a valid response...");
                interactiveMode();
            }
        }

        //System.out.println("Beginning Decryption");
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
        System.out.println("Beginning timer...");
        System.out.println("Encrypting...");
        long startTime = System.nanoTime();
        try {
            Scanner lineScanner = new Scanner(file);

            while (lineScanner.hasNextLine()) {
                String line = lineScanner.nextLine();
                Scanner scan = new Scanner(line);

                while (scan.hasNext()) { //each line
                    word = scan.next();
                    //System.out.println(word);
                    String numWord = strToNumFormat(word);
                    //System.out.println(numWord);

                    int length = numWord.length(), index = 3;
                    String subWord = "";
                    while (index < length + 3) {
                        subWord = numWord.substring(index - 3, index);

                        encrypted = encrypt(subWord);
                        //encrypted = encrypt(new BigInteger(word.toCharArray()));
                        //System.out.println("Encrypted--"+encrypted.toString());
                        bw.write(encrypted.toString());
                        bw.write(" ");
                        index += 3;
                    }
                    encrypted = encrypt("32");
                    bw.write(encrypted.toString());
                    bw.write(" ");
                }
                encrypted = encrypt("13"); //Carriage Return
                bw.write(encrypted.toString());
                bw.write(" ");
                encrypted = encrypt("10"); //NewLine
                bw.write(encrypted.toString());
                bw.write(" ");
            }

        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        long elapsedTime = System.nanoTime() - startTime;

        System.out.println("Timer stopped, Elasped Time:"+elapsedTime/1000000000.0+" s");
        bw.close();
    }

    public static void decryptFile(String iFile, String oFIle) throws IOException {
        File file = new File(iFile); //INPUT FILE
        File fOut = new File(oFIle); //OUTPUT FILE
        FileOutputStream fos = new FileOutputStream(fOut);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        String word;
        BigInteger decrypted;
        System.out.println("Beginning timer...");
        System.out.println("Decrypting...");
        long startTime = System.nanoTime();

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
        long elapsedTime = System.nanoTime() - startTime;
        System.out.println("Timer stopped, Elasped Time:"+elapsedTime/1000000000.0+" s");
        bw.close();
    }

    public static void loadKeysFromFile(int publicFlag, int privateFlag) throws IOException {
        if (publicFlag == 1) {
            System.out.println("Loading Keys/public.txt");
            File publicFile = new File("Keys/public.txt");
            if (!publicFile.exists()) {
                System.out.println("FaTaL!!\nCould not find Keys/public.txt file!");
                System.exit(0);
            }
            Scanner publicScanner = new Scanner(publicFile);
            String line = publicScanner.nextLine();
            publicKey = new BigInteger(line);

        }
        if (privateFlag == 1) {
            System.out.println("Loading Keys/private.txt");
            File privateFile = new File("Keys/private.txt");
            if (!privateFile.exists()) {
                System.out.println("FaTaL!!\nCould not find Keys/private.txt file!");
                System.exit(0);
            }
            Scanner privateScanner = new Scanner(privateFile);
            String line = privateScanner.nextLine();
            privateKey = new BigInteger(line);
        }

        System.out.println("Loading Keys/modulus.txt");
        File modulusFile = new File("Keys/modulus.txt");
        if (!modulusFile.exists()) {
            System.out.println("FaTaL!!\nCould not find Keys/modulus.txt file!");
            System.exit(0);
        }

        Scanner modulusScanner = new Scanner(modulusFile);
        String line = modulusScanner.nextLine();
        n = new BigInteger(line);
    }

    public static void generateKeys() throws IOException{
        System.out.println("Would you like to generate keys?");
        String input = sIn.nextLine();

        if (input.equals("y") || input.equals("Y") || input.equals("yes") || input.equals("Yes")){
            System.out.println("Generating keys...");
        } else  if (input.equals("n") || input.equals("N") || input.equals("no") || input.equals("No")){
            System.out.println("Using keys from file...");
            loadKeysFromFile(1,1);
            return;
        } else {
            System.out.println("You entered: "+input);
            System.out.println("Please enter a valid response...");
            generateKeys();
        }

        System.out.println("Would you like to specify a BIT LENGTH (leave blank if you want to keep current)?\n Current:"+BIT_LENGTH);
        System.out.println("Remember if you choose a large number it could affect performance!");
        input = sIn.nextLine();

        if (input.equals("")){
            System.out.println("Using current value");
        } else {
            BIT_LENGTH = Integer.parseInt(input);
            System.out.println("Using: "+input+" as BIT LENGTH");
        }
        System.out.println("\nFinding primes...");
        BigInteger p, q, phi;
        Boolean prime;

        p = findNumber();
        prime = isPrime(p);
        int counter = 0;
        while (!prime) {
            p = findNumber();
            prime = isPrime(p);
            counter++;
        }
        System.out.println("I threw away "+counter+" numbers!");
        System.out.println("Found first prime!");
        System.out.println(p+"\n");

        q = findNumber();
        prime = isPrime(q);
        counter=0;
        while (!prime || q.equals(p)) {
            q = findNumber();
            prime = isPrime(q);
            counter++;
        }
        System.out.println("I threw away "+counter+" numbers!");
        System.out.println("Found second prime!");
        System.out.println(q+"\n");

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

        System.out.println("Would you like to print these result to a file?");
        input = sIn.nextLine();

        boolean loopDone = true;
        while(loopDone) {
            if (input.equals("y") || input.equals("Y") || input.equals("yes") || input.equals("Yes")) {
                System.out.println("Writing to file...");
                writeKeyToFile();
                loopDone = false;
            } else if (input.equals("n") || input.equals("N") || input.equals("no") || input.equals("No")) {
                System.out.println("You entered no");
                loopDone = false;
            } else {
                System.out.println("You entered:" + input);
                System.out.println("Please enter a valid response...");
            }
        }
        return;
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

    public static void writeKeyToFile() throws IOException {
        File keyDir = new File("Keys");
        if (!keyDir.exists()) {
            System.out.println("creating directory: Keys");
            boolean result = false;
            try{
                keyDir.mkdir();
            }
            catch(SecurityException se){
                System.out.println("Fix Permissions!!!");
                System.exit(0);
            }
        }

        File publicFile = new File("Keys/public.txt");
        FileWriter fwPublic = new FileWriter(publicFile);
        BufferedWriter bwPublic = new BufferedWriter(fwPublic);
        bwPublic.write(publicKey.toString());
        bwPublic.close();

        File privateFile = new File("Keys/private.txt");
        FileWriter fwPrivate = new FileWriter(privateFile);
        BufferedWriter bwPrivate = new BufferedWriter(fwPrivate);
        bwPrivate.write(privateKey.toString());
        bwPrivate.close();

        File modulusFile = new File("Keys/modulus.txt");
        FileWriter fwModulus = new FileWriter(modulusFile);
        BufferedWriter bwModulus = new BufferedWriter(fwModulus);
        bwModulus.write(n.toString());
        bwModulus.close();


    }

    public static BigInteger encrypt(String message) {
        BigInteger exponent, result, number;
        number = new BigInteger(message);

        // m^e mod n
        result = number.modPow(publicKey,n);

        return result;
    }

    public static BigInteger decrypt(String message) {
        BigInteger exponent, result, number;
        number = new BigInteger(message);

        //   m^d mod n
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