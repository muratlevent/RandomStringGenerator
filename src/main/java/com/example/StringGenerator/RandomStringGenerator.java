package com.example.StringGenerator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class RandomStringGenerator {
    private static final String OUTPUT_FILE = "randomStrings.txt";
    private static final String STATUS_FILE = "lastIndex.txt";
    private static final int TOTAL_LINES = 1_000_000_000;
    private static final int CHECKPOINT_INTERVAL = 100_000;
    private static final int MIN_STRING_LENGTH = 8;
    private static final int MAX_STRING_LENGTH = 20;

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        try {
            int startIndex = readLastIndex();
            writeRandomStringsToFile(startIndex);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            double elapsedTimeInMinutes = (double) elapsedTime / (60 * 1000);
            System.out.println("Total execution time: " + elapsedTimeInMinutes + " minutes");
        }
    }

    private static void writeRandomStringsToFile(int startIndex) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE, true))) {
            for (int i = startIndex; i < TOTAL_LINES; i++) {
                String randomString = generateRandomString(MIN_STRING_LENGTH, MAX_STRING_LENGTH);
                writer.write(randomString);
                writer.newLine();
                if (i % CHECKPOINT_INTERVAL == 0) {
                    saveLastIndex(i);
                    writer.flush();
                }
            }
        }
    }

    private static String generateRandomString(int min, int max) {
        Random random = new Random();
        int stringLength = min + random.nextInt(max - min + 1);
        StringBuilder sb = new StringBuilder(stringLength);
        for (int i = 0; i < stringLength; i++) {
            char randomChar = (char) ('a' + random.nextInt(26));
            sb.append(randomChar);
        }
        return sb.toString();
    }

    private static void saveLastIndex(int index) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(STATUS_FILE))) {
            writer.write(String.valueOf(index));
        }
    }

    private static int readLastIndex() throws IOException {
        if (!Files.exists(Paths.get(STATUS_FILE))) {
            return 0;
        }
        String lastIndexStr = new String(Files.readAllBytes(Paths.get(STATUS_FILE)));
        return Integer.parseInt(lastIndexStr.trim());
    }
}
