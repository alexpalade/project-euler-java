package alexpalade.projecteuler.p14;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static List<String> readInputData() {
        File inFile = new File("13-input.txt");
        List<String> result = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(inFile);

            while (scanner.hasNext()) {
                String value = scanner.next();
                result.add(value);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Input file error");
        }
        return result;
    }

    public static void main(String[] args) throws InterruptedException {

        List<String> input = readInputData();
        int length = input.get(0).length();

        // We'll sum the input numbers' columns into the "temp" array
        long startTimeST = System.currentTimeMillis();
        int[] temp = new int[length * 2];
        Arrays.fill(temp, 0, length * 2, 0);
        for (String number : input) {
            int[] n = new int[length];
            for (int i = 0; i < length; i++) {
                n[i] = Integer.parseInt(number.substring(i, i + 1));
                temp[i] += n[i];
            }
        }
        long timeElapsedST = System.currentTimeMillis() - startTimeST;

        long startTimeMT = System.currentTimeMillis();
        // MULTITHREADING
        HashMap<Integer, Integer> tempMT = new HashMap<>();
        int processors = Runtime.getRuntime().availableProcessors();
        int rounds = (int) (Math.ceil((double) length / processors));
        for (int j = 0; j < length; j++) {
                Thread t = new Thread(new AddMT(tempMT, input, j));
                t.start();
        }
        long timeElapsedMT = System.currentTimeMillis() - startTimeMT;
        
        System.out.println("Single threaded: " + timeElapsedST + " ms.");
        System.out.println("Multi threaded: " + timeElapsedMT + " ms.");
        
        // Construct the proper number using a carry
        int carry = 0;
        for (int i = length - 1; i >= 0; i--) {
            int digit = (temp[i] + carry) % 10;
            if (temp[i] + carry > 9) {
                carry = Math.round((temp[i] + carry) / 10);
            } else {
                carry = 0;
            }
            temp[i] = digit;
        }

        // Create the final result string from carry (+) digits array
        StringBuilder result = new StringBuilder(String.valueOf(carry));
        for (int i = 0; i < length; i++) {
            result.append(String.valueOf(temp[i]));
        }

        // Done! Print the result.       
        System.out.println("Result = " + result);

        // Write the temp to a file
        File outFile = new File("13-output.txt");
        try {
            PrintWriter outstream = new PrintWriter(outFile);
            outstream.print(result);
            outstream.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Output file error");
        }
    }

}

class AddMT implements Runnable {

    List<String> strings;
    int column;
    HashMap<Integer, Integer> target;

    public AddMT(HashMap<Integer, Integer> target, List<String> strings, int column) {
        this.strings = strings;
        this.column = column;
        this.target = target;
    }

    @Override
    public void run() {
        int sum = 0;
        for (String numberString : strings) {
            int digit = Integer.parseInt(numberString.substring(column, column + 1));
            sum += digit;
        }
        target.put(column, sum);
    }

}
