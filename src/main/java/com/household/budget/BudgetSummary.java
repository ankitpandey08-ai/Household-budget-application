package com.household.budget;

import java.math.BigDecimal;
import java.util.Map;

public record BudgetSummary(String period, int expenseCount, BigDecimal totalAmount, Map<String, BigDecimal> totalsByCategory) {}
