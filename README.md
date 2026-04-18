# 🎲 Dice Probability Calculator

A comprehensive Java implementation for calculating dice probabilities, featuring both fair and biased dice, plus the classic Coupon Collector problem. Perfect for interview preparation and probability theory practice.


## Features

- **Fair Dice Probability**: Calculate exact probabilities for any sum with `n` fair six-sided dice using dynamic programming
- **Biased Dice Support**: Extend calculations to dice with custom face probabilities
- **Coupon Collector Problem**: Calculate expected rolls to see all six faces (14.7)
- **Clean OOP Design**: Modular, testable, and well-documented code
PS: Includes time/space complexity analysis and alternative approaches


## Quick Start

Probability of rolling sum 7 with 2 fair dice
double prob = DiceProbability.fairSumProbability(2, 7);
-- Result: 0.166666... (6/36)

Expected rolls to see all six faces
double expected = DiceProbability.expectedRollsToSeeAllFaces();
-- Result: 14.7
