%% COUPON COLLECTOR - MONTE CARLO SIMULATION (ALL PLOTS IN ONE FIGURE)
% Expected rolls to see all 6 faces of a fair die.
% Theoretical value = 14.7

clear; clc; close all;

%% Parameters
numExperiments = 100000;    % Number of Monte Carlo trials
numFaces = 6;               % Six-sided die
maxRollsCap = 500;          % Safety upper bound

%% Monte Carlo Simulation
rollsNeeded = zeros(1, numExperiments);

fprintf('Running %d Monte Carlo experiments...\n', numExperiments);
tic;

for expIdx = 1:numExperiments
    seen = false(1, numFaces);
    uniqueCount = 0;
    rolls = 0;
    
    while uniqueCount < numFaces
        face = randi(numFaces);
        rolls = rolls + 1;
        if ~seen(face)
            seen(face) = true;
            uniqueCount = uniqueCount + 1;
        end
        if rolls > maxRollsCap
            break;
        end
    end
    rollsNeeded(expIdx) = rolls;
end

elapsedTime = toc;
fprintf('Simulation completed in %.2f seconds.\n', elapsedTime);

%% Statistics
meanRolls = mean(rollsNeeded);
stdRolls = std(rollsNeeded);
medianRolls = median(rollsNeeded);
minRolls = min(rollsNeeded);
maxRolls = max(rollsNeeded);
theoretical = numFaces * sum(1./(1:numFaces));

fprintf('\n========== RESULTS ==========\n');
fprintf('Theoretical expected value:  %.4f\n', theoretical);
fprintf('Simulated mean:              %.4f\n', meanRolls);
fprintf('Standard deviation:          %.4f\n', stdRolls);
fprintf('Median:                      %d\n', medianRolls);
fprintf('Minimum rolls observed:      %d\n', minRolls);
fprintf('Maximum rolls observed:      %d\n', maxRolls);
fprintf('Error (mean - theoretical):  %.4f\n', meanRolls - theoretical);

figure('Position', [50, 50, 1200, 900]);

% first subplot
subplot(2, 2, 1);
histogram(rollsNeeded, 'BinMethod', 'integers', 'Normalization', 'pdf', ...
          'FaceColor', [0.2, 0.6, 0.8], 'EdgeColor', 'k');
hold on;
xline(meanRolls, '--r', sprintf('Mean = %.2f', meanRolls), 'LineWidth', 1.5);
xline(theoretical, '--g', sprintf('Theoretical = %.2f', theoretical), 'LineWidth', 1.5);
xlabel('Number of Rolls');
ylabel('Probability Density');
title('Distribution of Rolls Needed');
grid on;
legend('Empirical PDF', 'Simulated Mean', 'Theoretical Expected');

% second subplot
subplot(2, 2, 2);
cumulativeMean = cumsum(rollsNeeded) ./ (1:numExperiments);
plot(1:numExperiments, cumulativeMean, 'b-', 'LineWidth', 1);
hold on;
yline(theoretical, '--r', 'Theoretical 14.7', 'LineWidth', 1.5);
xlabel('Number of Experiments');
ylabel('Cumulative Average Rolls');
title('Convergence of Monte Carlo Estimate');
legend('Running Mean', 'Theoretical Value', 'Location', 'best');
grid on;
xlim([1, numExperiments]);

% third subplot
subplot(2, 2, 3);

% Compute quartiles and fences
q1 = prctile(rollsNeeded, 25);
q2 = prctile(rollsNeeded, 50);   % median
q3 = prctile(rollsNeeded, 75);
iqr = q3 - q1;
lower_fence = q1 - 1.5 * iqr;
upper_fence = q3 + 1.5 * iqr;

% Identify outliers
outliers = rollsNeeded(rollsNeeded < lower_fence | rollsNeeded > upper_fence);
non_outliers = rollsNeeded(rollsNeeded >= lower_fence & rollsNeeded <= upper_fence);
whisker_low = min(non_outliers);
whisker_high = max(non_outliers);

hold on;
% Box (rectangle from q1 to q3)
fill([0.8, 1.2, 1.2, 0.8], [q1, q1, q3, q3], [0.7, 0.9, 1.0], 'EdgeColor', 'k', 'LineWidth', 1.5);
% Median line
plot([0.8, 1.2], [q2, q2], 'r-', 'LineWidth', 2);
% Lower whisker
plot([1, 1], [whisker_low, q1], 'k-', 'LineWidth', 1.5);
plot([0.9, 1.1], [whisker_low, whisker_low], 'k-', 'LineWidth', 1.5);
% Upper whisker
plot([1, 1], [q3, whisker_high], 'k-', 'LineWidth', 1.5);
plot([0.9, 1.1], [whisker_high, whisker_high], 'k-', 'LineWidth', 1.5);
% Outliers (if any)
if ~isempty(outliers)
    scatter(ones(size(outliers)), outliers, 20, 'r', 'x', 'LineWidth', 1.5);
end
% Mean marker
scatter(1, meanRolls, 60, 'g', 'd', 'filled');

xlim([0.5, 1.5]);
ylim([min(rollsNeeded)-1, max(rollsNeeded)+1]);
set(gca, 'XTick', 1, 'XTickLabel', {'Rolls Data'});
ylabel('Number of Rolls');
title('Boxplot (Manual)');
grid on;
legend({'IQR Box', 'Median', 'Whiskers', 'Outliers', 'Mean'}, 'Location', 'best');

% fourth subplot (CDF)
subplot(2, 2, 4);
[counts, edges] = histcounts(rollsNeeded, 'Normalization', 'cdf');
centers = (edges(1:end-1) + edges(2:end)) / 2;
stairs(centers, counts, 'b-', 'LineWidth', 2);
hold on;
xline(theoretical, '--r', 'Theoretical Mean', 'LineWidth', 1.5);
xlabel('Number of Rolls');
ylabel('Cumulative Probability');
title('Empirical CDF of Rolls Needed');
grid on;
legend('Empirical CDF', 'Theoretical Mean', 'Location', 'best');

% Adjust layout and save
sgtitle(sprintf('Coupon Collector Problem – %d Monte Carlo Trials', numExperiments));
saveas(gcf, 'coupon_collector_all_plots.png');
fprintf('\nAll plots saved in a single figure: "coupon_collector_all_plots.png"\n');