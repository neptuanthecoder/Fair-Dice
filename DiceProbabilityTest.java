/**
 * Test suite for DiceProbability.
 * Run with: java DiceProbabilityTest
 *
 * This class verifies the probability calculations for fair dice,
 * biased dice, and the coupon collector expected value.
 */
public class DiceProbabilityTest {

    private static final double EPSILON = 0.0001;
    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        System.out.println("=== Running DiceProbability Tests ===\n");

        testFairDice();
        testBiasedDice();
        testCouponCollector();
        testEdgeCases();

        System.out.println("\n=== Test Results ===");
        System.out.println("Passed: " + testsPassed);
        System.out.println("Failed: " + testsFailed);

        if (testsFailed == 0) {
            System.out.println("\n ALL TESTS PASSED!");
        } else {
            System.out.println("\n SOME TESTS FAILED!");
            System.exit(1);
        }
    }

    private static void assertEqual(double expected, double actual, String message) {
        if (Math.abs(expected - actual) < EPSILON) {
            testsPassed++;
            System.out.println("✅ " + message);
        } else {
            testsFailed++;
            System.out.println("test failed " + message + " | Expected: " + expected + ", Got: " + actual);
        }
    }

    private static void assertTrue(boolean condition, String message) {
        if (condition) {
            testsPassed++;
            System.out.println("✅ " + message);
        } else {
            testsFailed++;
            System.out.println("test failed " + message);
        }
    }

    private static void testFairDice() {
        System.out.println("--- Testing Fair Dice ---");

        // Single die: each face probability = 1/6
        assertEqual(1.0 / 6.0, DiceProbability.fairSumProbability(1, 1),
                "1 die, sum=1 should be 1/6");
        assertEqual(1.0 / 6.0, DiceProbability.fairSumProbability(1, 6),
                "1 die, sum=6 should be 1/6");

        // Two dice: known exact probabilities
        assertEqual(1.0 / 36.0, DiceProbability.fairSumProbability(2, 2),
                "2 dice, sum=2 should be 1/36");
        assertEqual(2.0 / 36.0, DiceProbability.fairSumProbability(2, 3),
                "2 dice, sum=3 should be 2/36");
        assertEqual(3.0 / 36.0, DiceProbability.fairSumProbability(2, 4),
                "2 dice, sum=4 should be 3/36");
        assertEqual(4.0 / 36.0, DiceProbability.fairSumProbability(2, 5),
                "2 dice, sum=5 should be 4/36");
        assertEqual(5.0 / 36.0, DiceProbability.fairSumProbability(2, 6),
                "2 dice, sum=6 should be 5/36");
        assertEqual(6.0 / 36.0, DiceProbability.fairSumProbability(2, 7),
                "2 dice, sum=7 should be 6/36");

        // Total probability distribution must sum to 1
        double total = 0.0;
        for (int s = 2; s <= 12; s++) {
            total += DiceProbability.fairSumProbability(2, s);
        }
        assertEqual(1.0, total, "Sum of all probabilities for 2 dice should be 1.0");
    }

    private static void testBiasedDice() {
        System.out.println("\n--- Testing Biased Dice ---");

        // Completely biased die: only face 6 is possible
        double[] alwaysSix = {0.0, 0.0, 0.0, 0.0, 0.0, 1.0};
        assertEqual(1.0, DiceProbability.biasedSumProbability(1, 6, alwaysSix),
                "Biased die (always 6): P(sum=6) should be 1.0");
        assertEqual(0.0, DiceProbability.biasedSumProbability(1, 1, alwaysSix),
                "Biased die (always 6): P(sum=1) should be 0.0");

        // Two biased dice both always 6
        assertEqual(1.0, DiceProbability.biasedSumProbability(2, 12, alwaysSix),
                "2 biased dice (always 6): P(sum=12) should be 1.0");

        // Fair probabilities passed through biased method should match fair method
        double[] fairProbs = {1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0};
        assertEqual(DiceProbability.fairSumProbability(2, 7),
                DiceProbability.biasedSumProbability(2, 7, fairProbs),
                "Fair probabilities via biased method should match fair method");

        // Distribution must sum to 1 for custom biased probabilities
        double[] customProbs = {0.1, 0.1, 0.1, 0.1, 0.1, 0.5};
        double total = 0.0;
        for (int s = 2; s <= 12; s++) {
            total += DiceProbability.biasedSumProbability(2, s, customProbs);
        }
        assertEqual(1.0, total, "Sum of biased probabilities should be 1.0");
    }

    private static void testCouponCollector() {
        System.out.println("\n--- Testing Coupon Collector ---");

        // Theoretical value: 6/6 + 6/5 + 6/4 + 6/3 + 6/2 + 6/1 = 14.7
        double expected = 1.0 + 6.0 / 5.0 + 6.0 / 4.0 + 6.0 / 3.0 + 6.0 / 2.0 + 6.0 / 1.0;
        assertEqual(expected, DiceProbability.expectedRollsToSeeAllFaces(),
                "Expected rolls should be exactly 14.7");

        // Monte Carlo simulation sanity check (allow ±0.5 tolerance with 100k trials)
        double simulated = DiceProbability.simulateExpectedRolls(100_000);
        assertTrue(Math.abs(simulated - 14.7) < 0.5,
                "Monte Carlo simulation (100k trials) should be near 14.7 | Got: " + simulated);
    }

    private static void testEdgeCases() {
        System.out.println("\n--- Testing Edge Cases ---");

        // n = 0 dice should return 0.0
        assertEqual(0.0, DiceProbability.fairSumProbability(0, 0),
                "Zero dice should return 0.0");

        // Target sum less than minimum possible (n)
        assertEqual(0.0, DiceProbability.fairSumProbability(2, 1),
                "Sum below possible minimum should return 0.0");

        // Target sum greater than maximum possible (6*n)
        assertEqual(0.0, DiceProbability.fairSumProbability(2, 13),
                "Sum above possible maximum should return 0.0");

        // Biased dice with invalid probability array
        try {
            DiceProbability.biasedSumProbability(2, 7, new double[]{0.5, 0.5});
            assertTrue(false, "Should have thrown IllegalArgumentException for wrong array length");
        } catch (IllegalArgumentException e) {
            assertTrue(true, "Threw IllegalArgumentException for wrong array length as expected");
        }

        // Large number of dice (performance sanity check, not exact probability)
        double prob = DiceProbability.fairSumProbability(10, 35);
        assertTrue(prob > 0.0 && prob < 1.0,
                "10 dice, sum=35 should return a valid probability (got " + prob + ")");
    }
}
