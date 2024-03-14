package com.example.StringGenerator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class RandomStringGenerator {
    private static final String OUTPUT_FILE = "randomStrings.txt";
    private static final int TOTAL_LINES = 1_000_000_000;
    private static final int MIN_STRING_LENGTH = 8;
    private static final int MAX_STRING_LENGTH = 20;
    private static final int NUMBER_OF_THREADS = 8;
    private static final int BUFFER_SIZE = 8 * 1024 * 1024;
    private static final int BATCH_SIZE = 1_000_000;
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";

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
        StringBuilder sb = new StringBuilder(MAX_STRING_LENGTH);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE, true), BUFFER_SIZE)) {
            StringBuilder batchBuilder = new StringBuilder();
            for (int i = startLine; i < endLine; i++) {
                String randomString = generateRandomString(sb, MIN_STRING_LENGTH, MAX_STRING_LENGTH);
                batchBuilder.append(randomString).append("\n");
                if (i % BATCH_SIZE == 0) {
                    writer.write(batchBuilder.toString());
                    batchBuilder.setLength(0);
                }
            }
            if (batchBuilder.length() > 0) {
                writer.write(batchBuilder.toString());
            }
        }
    }

    private static String generateRandomString(StringBuilder sb, int min, int max) {
        sb.setLength(0);
        int stringLength = min + ThreadLocalRandom.current().nextInt(max - min + 1);
        for (int i = 0; i < stringLength; i++) {
            int index = ThreadLocalRandom.current().nextInt(ALPHABET.length());
            char randomChar = ALPHABET.charAt(index);
            sb.append(randomChar);
        }
        return sb.toString();
    }
}
