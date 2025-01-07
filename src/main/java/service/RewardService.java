package service;

import dto.Customer;
import dto.RewardsResponse;
import dto.Transaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RewardService {

    /**
     * method to calculate RewardPoints based upon customer and number of transactions.
     * @param customers ie. list of customers.
     * @param transactions ie. list of transactions.
     * @return list  of RewardResponse as response.
     */

    public List<RewardsResponse> calculateRewardPoints(List<Customer> customers,List<Transaction> transactions) {
        return customers.stream().map(customer -> {
            // Filter transactions for this customer
            List<Transaction> customerTransactions = transactions.stream()
                    .filter(t -> t.getCustomerId().equals(customer.getId()))
                    .collect(Collectors.toList());

            // Calculate monthly and total points
            Map<String, Integer> monthlyPoints = new HashMap<>();
            int totalPoints = 0;

            for (Transaction transaction : customerTransactions) {
                int points = calculatePoints(transaction.getAmount());
                String month = transaction.getTransactionDate().getMonth().toString();

                monthlyPoints.put(month, monthlyPoints.getOrDefault(month, 0) + points);
                totalPoints += points;
            }

            return new RewardsResponse(customer.getName(), monthlyPoints, totalPoints);
        }).collect(Collectors.toList());

    }

    /**
     * Calculates reward points based upon the amount of transaction.
     * @param amount of the transaction.
     * @return points.
     */
    public int calculatePoints(double amount) {
        int points = 0;
        if (amount > 100) {
            points += (amount - 100) * 2;
            amount = 100;
        }
        if (amount > 50) {
            points += (amount - 50);
        }
        return points;
    }

}
