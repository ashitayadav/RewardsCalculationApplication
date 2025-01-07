package service;

import dto.Customer;
import dto.RewardsResponse;
import dto.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RewardServiceTest {

    @InjectMocks
    private RewardService rewardService;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalculateRewardPoints() {
        // Prepare mock data
        List<Customer> customers = List.of(
                new Customer(1L, "Albert"),
                new Customer(2L, "Dani")
        );

        List<Transaction> transactions = List.of(
                new Transaction(1L, 120.0, LocalDate.of(2024, 11, 1)),
                new Transaction(1L, 76.0, LocalDate.of(2024, 11, 15)),
                new Transaction(2L, 120.0, LocalDate.of(2024, 12, 1)),
                new Transaction(2L, 120.0, LocalDate.of(2024, 12, 5))
        );

        // Call the method under test
        List<RewardsResponse> rewardPoints = rewardService.calculateRewardPoints(customers, transactions);

        // Assertions for Albert
        RewardsResponse albertResponse = rewardPoints.get(0);
        assertEquals("Albert", albertResponse.getCustomerName());
        assertEquals(1, albertResponse.getMonthlyPoints().size());
        assertEquals(116, albertResponse.getMonthlyPoints().get("NOVEMBER"));
        assertEquals(116, albertResponse.getTotalPoints());

        // Assertions for Dani
        RewardsResponse daniResponse = rewardPoints.get(1);
        assertEquals("Dani", daniResponse.getCustomerName());
        assertEquals(1, daniResponse.getMonthlyPoints().size());
        assertEquals(180, daniResponse.getMonthlyPoints().get("DECEMBER"));
        assertEquals(180, daniResponse.getTotalPoints());
    }

    @Test
    void testCalculateRewardPointsNoTransactions() {
        // Prepare mock data with no transactions
        List<Customer> customers = List.of(
                new Customer(1L, "Albert"),
                new Customer(2L, "Dani")
        );

        List<Transaction> transactions = List.of(); // No transactions

        // Call the method under test
        List<RewardsResponse> rewardPoints = rewardService.calculateRewardPoints(customers, transactions);

        // Assertions to ensure no points are given when there are no transactions
        assertNotNull(rewardPoints);
        assertEquals(2, rewardPoints.size()); // Two customers
        assertEquals(0, rewardPoints.get(0).getTotalPoints());
        assertEquals(0, rewardPoints.get(1).getTotalPoints());
    }

    @Test
    void testCalculateRewardPointsMultipleTransactionsSameMonth() {
        // Prepare mock data with multiple transactions for the same month
        List<Customer> customers = List.of(
                new Customer(1L, "Albert")
        );

        List<Transaction> transactions = List.of(
                new Transaction(1L, 120.0, LocalDate.of(2024, 11, 1)),
                new Transaction(1L, 80.0, LocalDate.of(2024, 12, 15))
        );

        // Call the method under test
        List<RewardsResponse> rewardPoints = rewardService.calculateRewardPoints(customers, transactions);

        // Assertions for Albert (same month, two transactions)
        RewardsResponse albertResponse = rewardPoints.get(0);
        assertEquals("Albert", albertResponse.getCustomerName());
        assertEquals(2, albertResponse.getMonthlyPoints().size());
        assertEquals(90, albertResponse.getMonthlyPoints().get("NOVEMBER"));
        assertEquals(30, albertResponse.getMonthlyPoints().get("DECEMBER"));
        assertEquals(120, albertResponse.getTotalPoints());
    }

    @Test
    void testCalculateRewardPointsSingleTransaction() {
        // Prepare mock data with a single transaction for a customer
        List<Customer> customers = List.of(
                new Customer(1L, "Albert")
        );

        List<Transaction> transactions = List.of(
                new Transaction(1L, 120.0, LocalDate.of(2024, 11, 1))
        );

        // Call the method under test
        List<RewardsResponse> rewardPoints = rewardService.calculateRewardPoints(customers, transactions);

        // Assertions for Albert
        RewardsResponse albertResponse = rewardPoints.get(0);
        assertEquals("Albert", albertResponse.getCustomerName());
        assertEquals(1, albertResponse.getMonthlyPoints().size());
        assertEquals(90, albertResponse.getMonthlyPoints().get("NOVEMBER"));
        assertEquals(90, albertResponse.getTotalPoints());
    }

    @Test
    void testCalculatePointsEdgeCase() {
        // Edge case: Transaction with exactly 50, 100, and higher values
        assertEquals(0, rewardService.calculatePoints(50.0));
        assertEquals(0, rewardService.calculatePoints(50.01));
        assertEquals(90, rewardService.calculatePoints(120.0));
        assertEquals(150, rewardService.calculatePoints(150.0));
    }
}
