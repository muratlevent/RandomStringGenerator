package com.example.StringGenerator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RandomStringGenerator {
    private static final String OUTPUT_FILE = "randomStrings.txt";
    private static final int TOTAL_LINES = 1_000_000_000;
    private static final int MIN_STRING_LENGTH = 8;
    private static final int MAX_STRING_LENGTH = 20;
    private static final int NUMBER_OF_THREADS = 3;

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        writeRandomStringsToFile();
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        double elapsedTimeInMinutes = (double) elapsedTime / (60 * 1000);
        System.out.println("Total execution time: " + elapsedTimeInMinutes + " minutes");
    }

    private static void writeRandomStringsToFile() {
        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        int linesPerThread = TOTAL_LINES / NUMBER_OF_THREADS;
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            int start = i * linesPerThread;
            int end = (i == NUMBER_OF_THREADS - 1) ? TOTAL_LINES : start + linesPerThread;
            executor.submit(() -> {
                try {
                    writeStrings(start, end);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void writeStrings(int startLine, int endLine) throws IOException {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(MAX_STRING_LENGTH);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE, true))) {
            for (int i = startLine; i < endLine; i++) {
                String randomString = generateRandomString(random, sb, MIN_STRING_LENGTH, MAX_STRING_LENGTH);
                writer.write(randomString);
                writer.newLine();
            }
        }
    }

    private static String generateRandomString(Random random, StringBuilder sb, int min, int max) {
        sb.setLength(0);
        int stringLength = min + random.nextInt(max - min + 1);
        for (int i = 0; i < stringLength; i++) {
            char randomChar = (char) ('a' + random.nextInt(26));
            sb.append(randomChar);
        }
        return sb.toString();
    }
}
