package com.example.StringGenerator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class RandomStringGenerator {
    public static void main(String[] args) {
        int totalLines = 10;
        String fileName = "randomStrings.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (int i = 0; i < totalLines; i++) {
                String randomString = generateRandomString(8, 20);
                writer.write(randomString);
                writer.newLine();
            }
            System.out.println("File created: " + fileName);
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
