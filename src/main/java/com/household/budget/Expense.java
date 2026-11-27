package com.household.budget;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Expense(
        long id,
        String item,
        String category,
        BigDecimal amount,
        LocalDate expenseDate,
        String notes
) {}
