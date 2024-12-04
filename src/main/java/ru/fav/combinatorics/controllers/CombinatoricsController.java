package ru.fav.combinatorics.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.fav.combinatorics.services.CombinatoricsService;

import java.math.BigInteger;
import java.util.Arrays;


@Controller
public class CombinatoricsController {

    @Autowired
    private CombinatoricsService combinatoricsService;

    @GetMapping("/")
    public String home() {
        return "index";
    }


    @PostMapping("/urn-all-marked")
    public String urnAllMarked(
            @RequestParam int n,
            @RequestParam int m,
            @RequestParam int k,
            Model model) {

        String urnAllMarkedErrorMessage = null;

        if (n < 2) {
            urnAllMarkedErrorMessage = "n должно быть >= 2.";
        } else if (m < 1 || m > n) {
            urnAllMarkedErrorMessage = "Должно выполняться: 1 <= m <= n.";
        } else if (k < 1 || k >= m) {
            urnAllMarkedErrorMessage = "Должно выполняться: 1 <= k < m.";
        }

        model.addAttribute("urnAllMarkedErrorMessage", urnAllMarkedErrorMessage);

        if (urnAllMarkedErrorMessage != null) {
            model.addAttribute("n", n);
            model.addAttribute("m", m);
            model.addAttribute("k", k);
            return "index";
        }

        double probability = combinatoricsService.urnAllMarked(n, m, k);
        model.addAttribute("resultType", "urn-all-marked");
        model.addAttribute("result", probability);
        return "result";
    }


    @PostMapping("/urn-specific-marked")
    public String urnSpecificMarked(
            @RequestParam int n,
            @RequestParam int m,
            @RequestParam int k,
            @RequestParam int r,
            Model model) {

        String urnSpecificMarkedErrorMessage = null;

        if (n < 2) {
            urnSpecificMarkedErrorMessage = "n должно быть >= 2.";
        } else if (m < 1 || m > n) {
            urnSpecificMarkedErrorMessage = "Должно выполняться: 1 <= m <= n.";
        } else if (k < 1 || k >= m) {
            urnSpecificMarkedErrorMessage = "Должно выполняться: 1 <= k < m.";
        } else if (r < 0 || r > k) {
            urnSpecificMarkedErrorMessage = "Должно выполняться: 0 <= r <= k.";
        } else if (k - r > n - m) {
            urnSpecificMarkedErrorMessage = "Должно выполняться: k - r <= m - m.";
        }

        model.addAttribute("urnSpecificMarkedErrorMessage", urnSpecificMarkedErrorMessage);

        if (urnSpecificMarkedErrorMessage != null) {
            model.addAttribute("n", n);
            model.addAttribute("m", m);
            model.addAttribute("k", k);
            model.addAttribute("r", r);
            return "index";
        }

        double probability = combinatoricsService.urnSpecificMarked(n, m, k, r);
        model.addAttribute("resultType", "urn-specific-marked");
        model.addAttribute("result", probability);
        model.addAttribute("r", r);
        return "result";
    }


    @PostMapping("/calculate")
    public String calculate(
            @RequestParam String type,
            @RequestParam(required = false, defaultValue = "false") boolean repetition,
            @RequestParam int n,
            @RequestParam(required = false, defaultValue = "0") int m,
            @RequestParam(required = false) int[] repetitions,
            Model model) {


        String combinatoricsErrorMessage = null;

        if (n < 1) {
            combinatoricsErrorMessage = "n должно быть >= 1.";
        }

        switch (type) {
            case "permutation":
                if (repetition) {
                    if (repetitions == null || repetitions.length == 0) {
                        combinatoricsErrorMessage = "Для перестановок с повторениями необходимо указать повторения.";
                    } else if (Arrays.stream(repetitions).anyMatch(i -> i < 1)) {
                        combinatoricsErrorMessage = "Все повторения должны быть положительными";
                    } else if (n != Arrays.stream(repetitions).sum()) {
                        combinatoricsErrorMessage = "Сумма введенных повторений должна быть равна n.";
                    }
                }
                break;

            case "arrangement":
                System.out.println(repetition);
                if (repetition && (1 > n || 1 > m)) {
                combinatoricsErrorMessage = "Для размещений c повторениями должно выполняться: 1 <= n и 1 <= m.";
                } else if (!repetition && (m < 1 || m > n)) {
                combinatoricsErrorMessage = "Для размещений без повторений должно выполняться: 1 <= m <= n.";
            }
            break;

            case "combination":
                if (!repetition && (m < 1 || m > n)) {
                    combinatoricsErrorMessage = "Для сочетаний без повторений должно выполняться: 1 <= m <= n.";
                }
                else if (repetition && m < 1) {
                    combinatoricsErrorMessage = "Для сочетаний с повторениями m должно быть >= 1.";
                }
                break;

            default:
                combinatoricsErrorMessage = "Неизвестный тип вычислений.";
                break;
        }

        model.addAttribute("combinatoricsErrorMessage", combinatoricsErrorMessage);

        if (combinatoricsErrorMessage != null) {
            model.addAttribute("type", type);
            model.addAttribute("repetition", repetition);
            model.addAttribute("n", n);
            model.addAttribute("m", m);
            model.addAttribute("repetitions", repetitions != null ? String.join(",", Arrays.stream(repetitions).mapToObj(String::valueOf).toArray(String[]::new)) : "");
            return "index";
        }


        BigInteger result = switch (type) {
            case "permutation" -> repetition
                    ? combinatoricsService.permutationsWithRepetition(n, repetitions)
                    : combinatoricsService.permutations(n);
            case "arrangement" -> repetition
                    ? combinatoricsService.arrangementsWithRepetition(n, m)
                    : combinatoricsService.arrangements(n, m);
            case "combination" -> repetition
                    ? combinatoricsService.combinationsWithRepetition(n, m)
                    : combinatoricsService.combinations(n, m);
            default -> BigInteger.valueOf(0);
        };

        model.addAttribute("resultType", "combinatorics");
        model.addAttribute("result", result);
        return "result";
    }
}