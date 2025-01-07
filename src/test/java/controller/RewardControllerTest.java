package controller;

import dto.Customer;
import dto.RewardsResponse;
import dto.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import service.RewardService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


class RewardControllerTest {

    @InjectMocks
    private RewardController rewardController;

    @Mock
    private RewardService rewardService;

    @BeforeEach
    void setUp() {
        // Initialize Mockito annotations (mocks and injects them into the RewardController)
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetRewardPoints() {
        // Prepare mock data
        List<Customer> customers = List.of(
                new Customer(1L, "Albert"),
                new Customer(2L, "Dani")
        );

        List<Transaction> transactions = List.of(
                new Transaction(1L, 120.0, LocalDate.of(2024, 11, 1)),
                new Transaction(1L, 76.0, LocalDate.of(2024, 11, 15)),
                new Transaction(2L, 120.0, LocalDate.of(2024, 12, 1)),
                new Transaction(2L, 120.0, LocalDate.of(2024, 12, 1))
        );

        // Creating expected response for mock service
        RewardsResponse albertResponse = new RewardsResponse();
        albertResponse.setCustomerName("Albert");
        albertResponse.setMonthlyPoints(Map.of(
                "2024-11", 50  // Assume 50 points for November
        ));
        albertResponse.setTotalPoints(50);  // Total reward points for Albert

        RewardsResponse daniResponse = new RewardsResponse();
        daniResponse.setCustomerName("Dani");
        daniResponse.setMonthlyPoints(Map.of(
                "2024-12", 180  // Assume 180 points for December
        ));
        daniResponse.setTotalPoints(180);  // Total reward points for Dani

        List<RewardsResponse> expectedResponse = List.of(albertResponse, daniResponse);

        // Mock the RewardService behavior
        when(rewardService.calculateRewardPoints(customers, transactions)).thenReturn(expectedResponse);

        // Call the method in RewardController
        List<RewardsResponse> actualResponse = rewardController.getRewardPoints();

        // Assertions to validate the response

        assertNotNull(actualResponse);
        assertEquals(2, actualResponse.size());

        // Check Albert's response
        RewardsResponse albert = actualResponse.get(0);
        assertEquals("Albert", albert.getCustomerName());
        assertEquals(50, albert.getTotalPoints());
        assertEquals(1, albert.getMonthlyPoints().size());
        assertEquals(50, albert.getMonthlyPoints().get("2024-11"));

        // Check Dani's response
        RewardsResponse dani = actualResponse.get(1);
        assertEquals("Dani", dani.getCustomerName());
        assertEquals(180, dani.getTotalPoints());
        assertEquals(1, dani.getMonthlyPoints().size());
        assertEquals(180, dani.getMonthlyPoints().get("2024-12"));

        // Verify that the RewardService's method was called with the correct parameters
        verify(rewardService, times(1)).calculateRewardPoints(customers, transactions);
    }
}