package com.systex.hw4.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.LinkedList;

import static com.systex.hw4.service.LotteryService.getNumbers;

@Controller
@RequestMapping("/lottery")
public class LotteryController {

    @GetMapping("/index")
    public String index(Model model) {
        return "lottery/main";
    }

    @PostMapping("/generate")
    public String generateLotteryNumbers(
            @RequestParam(required = false) String groups,
            @RequestParam(required = false) String excludes,
            Model model) {

        LinkedList<String> errorMsgs = new LinkedList<>();
        model.addAttribute("errors", errorMsgs); // request scope

        int numGroups = parseGroupCount(groups, errorMsgs);
        LinkedList<Integer> excludeList = parseExcludes(excludes, errorMsgs);

        if (!errorMsgs.isEmpty()) {
            return "/lottery/main";
        }

        try {
            ArrayList<String> numbers = getNumbers(numGroups, excludeList);
            model.addAttribute("lotteryNumber", numbers);
            return "/lottery/result";
        } catch (Exception e) {
            e.printStackTrace();
            errorMsgs.add(e.getMessage());
            return "/lottery/main";
        }
    }

    // Validate and parse the number of groups
    private int parseGroupCount(String groups, LinkedList<String> errorMsgs) {
        if (groups != null && !groups.isEmpty()) {
            try {
                int numGroups = Integer.parseInt(groups);
                if (numGroups <= 0) {
                    errorMsgs.add("The number of groups must be a positive integer.");
                    return 0;
                }
                return numGroups;
            } catch (NumberFormatException e) {
                errorMsgs.add("Invalid input. Please enter a valid number for the groups.");
                return 0;
            }
        } else {
            errorMsgs.add("Please input the number of groups that you want to generate.");
            return 0;
        }
    }

    // Validate and parse the exclude numbers
    private LinkedList<Integer> parseExcludes(String excludes, LinkedList<String> errorMsgs) {
        LinkedList<Integer> excludeList = new LinkedList<>();
        if (excludes != null && !excludes.isEmpty()) {
            String cleanedInput = excludes.replaceAll("[^0-9]+", " ");
            String[] excludeArr = cleanedInput.trim().split("\\s+");

            for (String excludeValue : excludeArr) {
                if (!excludeValue.isEmpty()) {
                    try {
                        int num = Integer.parseInt(excludeValue);
                        if (num < 1 || num > 49) {
                            errorMsgs.add("Exclude numbers must be between 1 and 49.");
                        } else {
                            excludeList.add(num);
                        }
                    } catch (NumberFormatException e) {
                        errorMsgs.add("Invalid exclude number: " + excludeValue);
                    }
                }
            }

            if (excludeList.isEmpty()) {
                errorMsgs.add("Please provide at least one valid number to exclude.");
            }
        } else {
            errorMsgs.add("Please provide at least one number that you would like to exclude from generating.");
        }
        return excludeList;
    }
}