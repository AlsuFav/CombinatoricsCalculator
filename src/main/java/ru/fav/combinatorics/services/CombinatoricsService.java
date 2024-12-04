package ru.fav.combinatorics.services;

import org.springframework.stereotype.Service;

import java.math.BigInteger;


@Service
public class CombinatoricsService {


    private BigInteger factorial(int n) {
        BigInteger result = BigInteger.valueOf(1);
        for (int i = 2; i <= n; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }


    public BigInteger permutations(int n) {
        return factorial(n);
    }


    public BigInteger arrangements(int n, int k) {
        return factorial(n).divide(factorial(n - k));
    }


    public BigInteger combinations(int n, int k) {
        return factorial(n).divide(factorial(k).multiply(factorial(n - k)));
    }


    public BigInteger permutationsWithRepetition(int n, int[] repetitions) {
        BigInteger denominator = BigInteger.valueOf(1);
        for (int r : repetitions) {
            denominator = denominator.multiply(factorial(r));
        }
        return factorial(n).divide(denominator);
    }


    public BigInteger arrangementsWithRepetition(int n, int k) {
        return BigInteger.valueOf((long) Math.pow(n, k));
    }


    public BigInteger combinationsWithRepetition(int n, int k) {
        return combinations(n + k - 1, k);
    }


    public double urnAllMarked(int n, int m, int k) {
        double numerator = combinations(m, k).doubleValue();
        double denominator = combinations(n, k).doubleValue();
        return numerator/ denominator;
    }


    public double urnSpecificMarked(int n, int m, int k, int r) {
        double numerator = combinations(m, r).multiply(combinations(n - m, k - r)).doubleValue();
        double denominator = combinations(n, k).doubleValue();
        return numerator / denominator;
    }

}