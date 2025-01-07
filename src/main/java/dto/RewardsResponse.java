package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RewardsResponse {
    private String customerName;

    private Map<String,Integer> monthlyPoints;

    private int totalPoints;
}
