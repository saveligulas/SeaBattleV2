package sg.spring.seabattle2.controller;

import lombok.Data;
import org.jspecify.annotations.Nullable;

import java.util.List;

@Data
public class CreateTwoPlayerGameDTO {
    private String redPlayer;
    private String bluePlayer;
    @Nullable
    private List<Integer> shipSizes;
}
