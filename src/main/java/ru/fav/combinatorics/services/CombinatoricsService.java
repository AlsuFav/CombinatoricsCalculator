package ru.fav.combinatorics.services;

import org.springframework.stereotype.Service;


@Service
public class CombinatoricsService {

    // Факториал числа
    private long factorial(int n) {
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    // Перестановки без повторений
    public long permutations(int n) {
        return factorial(n);
    }

    // Размещения без повторений
    public long arrangements(int n, int k) {
        return factorial(n) / (factorial(n - k));
    }

    // Сочетания без повторений
    public long combinations(int n, int k) {
        return factorial(n) / (factorial(k) * (factorial(n - k)));
    }

    // Перестановки с повторениями
    public long permutationsWithRepetition(int n, int[] repetitions) {
        long denominator = 1;
        for (int r : repetitions) {
            denominator *= factorial(r);
        }
        return factorial(n) / denominator;
    }

    // Размещения с повторениями
    public long arrangementsWithRepetition(int n, int k) {
        return (long) Math.pow(n, k);
    }

    // Сочетания с повторениями
    public long combinationsWithRepetition(int n, int k) {
        return combinations(n + k - 1, k);
    }

    // Урновая модель: вероятность всех меченых P(A) = C(m, k) / C(n, k)
    public double urnAllMarked(int n, int m, int k) {
        double numerator = combinations(m, k);
        double denominator = combinations(n, k);
        return numerator / denominator;
    }

    // Урновая модель: вероятность r меченых P(A) = C(m, r) * C(n-m, k-r) / C(n, k)
    public double urnSpecificMarked(int n, int m, int k, int r) {
        double numerator = combinations(m, r) * combinations(n - m, k - r);
        double denominator = combinations(n, k);
        return numerator / denominator;
    }

}