/**
 * 1. Probability of a sum with n fair dice.
 * 2. Probability of a sum with n biased dice.
 * 3. Expected number of rolls to see all six faces (Coupon Collector).
 */
public class DiceProbability {
    /**
     * Calculates the probability that the sum of n fair six-sided dice equals targetSum.
     * Uses dynamic programming: dp[i][s] = probability of sum s with i dice.
     * Time complexity: O(n^2) with n dice (max sum = 6n).
     *
     * @param n         number of dice
     * @param targetSum desired sum (must be between n and 6n inclusive)
     * @return probability as a double
     */
    public static double fairSumProbability(int n, int targetSum) {
        if (n <= 0) return 0.0;
        if (targetSum < n || targetSum > 6 * n) return 0.0;

        double[][] dp = new double[n + 1][6 * n + 1];
        dp[0][0] = 1.0; // zero dice, sum zero with probability 1

        for (int i = 1; i <= n; i++) {
            for (int s = 1; s <= 6 * i; s++) {
                for (int face = 1; face <= 6; face++) {
                    if (s - face >= 0) {
                        dp[i][s] += dp[i - 1][s - face] / 6.0;
                    }
                }
            }
        }
        return dp[n][targetSum];
    }

    public static double biasedSumProbability(int n, int targetSum, double[] probs) {
    /**
     * Calculates the probability that the sum of n biased six-sided dice equals targetSum.
     * The bias is given by an array of probabilities for faces 1..6 (probs[0] = face 1).
     * Uses the same DP approach, multiplying by the face probability instead of 1/6.
     *
     * @param n         number of dice
     * @param targetSum desired sum
     * @param probs     array of length 6 with probabilities summing to 1.0
     * @return probability as a double */

        if (n <= 0) return 0.0;
        if (targetSum < n || targetSum > 6 * n) return 0.0;
        if (probs == null || probs.length != 6) {
            throw new IllegalArgumentException("probs must be an array of length 6");
        }

        double[][] dp = new double[n + 1][6 * n + 1];
        dp[0][0] = 1.0;

        for (int i = 1; i <= n; i++) {
            for (int s = 1; s <= 6 * i; s++) {
                for (int face = 1; face <= 6; face++) {
                    if (s - face >= 0) {
                        dp[i][s] += dp[i - 1][s - face] * probs[face - 1];
                    }
                }
            }
        }
        return dp[n][targetSum];
    }

    public static double expectedRollsToSeeAllFaces() {
    /**
     * Expected number of rolls of a fair six-sided die to see all six faces.
     * This is the classic Coupon Collector problem.
     * E = 6/6 + 6/5 + 6/4 + 6/3 + 6/2 + 6/1 = 14.7
     *
     * @return expected number of rolls */

        double expected = 0.0;
        for (int i = 1; i <= 6; i++) {
            expected += 6.0 / i;
        }
        return expected;
    }



    // montecarlo simulation (1000)
    public static double simulateExpectedRolls(int trials) {

        long totalRolls = 0;
        java.util.Random rand = new java.util.Random();

        for (int t = 0; t < trials; t++) {
            boolean[] seen = new boolean[6];
            int uniqueCount = 0;
            int rolls = 0;
            while (uniqueCount < 6) {
                int face = rand.nextInt(6);
                if (!seen[face]) {
                    seen[face] = true;
                    uniqueCount++;
                }
                rolls++;
            }
            totalRolls += rolls;
        }
        return (double) totalRolls / trials;
    }

    // exmple
    public static void main(String[] args) {
        // Fair dice
        System.out.println("Fair 2 dice, sum=7: " + fairSumProbability(2, 7));

        // Biased dice: face 6 heavily weighted (0.5), others 0.1 each
        double[] biasedProbs = {0.1, 0.1, 0.1, 0.1, 0.1, 0.5};
        System.out.println("Biased 2 dice, sum=7: " + biasedSumProbability(2, 7, biasedProbs));

        // Coupon collector: expected rolls to see all six faces
        System.out.println("Expected rolls to see all faces: " + expectedRollsToSeeAllFaces());
        
        System.out.println("Simulated (1M trials): " + simulateExpectedRolls(1_000_000));
    }
}
