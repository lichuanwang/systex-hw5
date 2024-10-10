package com.systex.hw4.service;

import java.util.*;

public class LotteryService {

    public static ArrayList<String> getNumbers(int groups, LinkedList<Integer> excludes) {
        // Use a HashSet to store the excluded numbers, ensuring uniqueness
        Set<Integer> numbers = new HashSet<>();
        for (int exludeNum : excludes) {
            numbers.add(exludeNum);  // Convert each string to an integer and add to the set
        }

        // Create a TreeSet to store the generated lottery numbers for each set
        // TreeSet automatically orders the numbers in ascending order
        TreeSet<Integer> s1 = new TreeSet<>();

        // Final output
        ArrayList<String> result = new ArrayList<>();

        // Generate 'groups' number of lottery sets
        for (int i = 0; i < groups; i++) {

            // Generate 6 unique random numbers between 1 and 49, excluding the numbers in the 'numbers' set
            while (s1.size() < 6) {
                int randomNum = (int) Math.floor(Math.random() * 49 + 1);  // Generate a random number between 1 and 49

                // Only add the number if it's not in the excluded 'numbers' set
                if (!numbers.contains(randomNum)) {
                    s1.add(randomNum);  // Add the number to the TreeSet
                }
            }

            // Print the generated lottery set
            result.add(s1.toString());
            // Clear the set before generating a new lottery set
            s1.clear();
        }
        return result;
    }
}
