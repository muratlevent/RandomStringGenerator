package com.example.StringGenerator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class RandomStringGenerator {

    private static final String FILE_NAME = "randomStrings.txt";
    private static final int TOTAL_LINES = 10;
    private static final int MIN_STRING_LENGTH = 8;
    private static final int MAX_STRING_LENGTH = 20;

    public static void main(String[] args) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (int i = 0; i < TOTAL_LINES; i++) {
                String randomString = generateRandomString(MIN_STRING_LENGTH, MAX_STRING_LENGTH);
                writer.write(randomString);
                writer.newLine();
            }
            System.out.println("File created: " + FILE_NAME);
        } catch (IOException e) {
            System.err.println("Error occurred while writing to file: " + e.getMessage());
        }
    }

    public static String generateRandomString(int min, int max) {
        Random random = new Random();
        int stringLength = min + random.nextInt(max - min + 1);
        StringBuilder sb = new StringBuilder(stringLength);
        for (int i = 0; i < stringLength; i++) {
            char randomChar = (char) ('a' + random.nextInt(26));
            sb.append(randomChar);
        }
        return sb.toString();
    }
}
