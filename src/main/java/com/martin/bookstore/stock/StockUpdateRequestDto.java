package com.martin.bookstore.stock;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockUpdateRequestDto {

    @NotNull
    @Min(0)
    private Integer quantityAvailable;

    @NotNull
    @Min(0)
    private Integer quantityReserved;

    @NotNull
    @Min(0)
    private Integer quantitySold;
}
