package controller;

import dto.Customer;
import dto.RewardsResponse;
import dto.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import service.RewardService;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
public class RewardController {

    @Autowired
    private RewardService rewardService;

    /**
     * endpoint to calculate reward points of the customer.
     * @return list of rewards segregated on the basis of months.
     */
    @GetMapping("/rewards")
    public List<RewardsResponse> getRewardPoints() {
        log.info("Initialise reward calculation");
        List<Customer> customers = List.of(new Customer(1L,"Albert"),
                new Customer(2L, "Dani"));

        List<Transaction> transactions = List.of(
                new Transaction(1L, 120.0, LocalDate.of(2024,11,1)),
                new Transaction(1L, 76.0, LocalDate.of(2024,11,15)),
                new Transaction(2L, 120.0, LocalDate.of(2024,12,1)),
                new Transaction(2L, 120.0, LocalDate.of(2024,12,1)));

        return rewardService.calculateRewardPoints(customers,transactions);
    }
}
