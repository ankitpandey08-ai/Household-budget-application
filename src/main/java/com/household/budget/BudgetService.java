package com.household.budget;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class BudgetService {
    private final ConcurrentHashMap<Long, Expense> expenses = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    public Expense create(ExpenseRequest request) {
        long id = sequence.incrementAndGet();
        Expense expense = new Expense(id, request.item().trim(), request.category().trim(),
                request.amount(), request.expenseDate(), request.notes() == null ? "" : request.notes().trim());
        expenses.put(id, expense);
        return expense;
    }

    public List<Expense> list(String category, YearMonth month) {
        return expenses.values().stream()
                .filter(e -> category == null || category.isBlank() || e.category().equalsIgnoreCase(category))
                .filter(e -> month == null || YearMonth.from(e.expenseDate()).equals(month))
                .sorted(Comparator.comparing(Expense::expenseDate).reversed().thenComparing(Expense::id, Comparator.reverseOrder()))
                .toList();
    }

    public Expense get(long id) {
        Expense e = expenses.get(id);
        if (e == null) throw new ExpenseNotFoundException(id);
        return e;
    }

    public Expense update(long id, ExpenseRequest request) {
        if (!expenses.containsKey(id)) throw new ExpenseNotFoundException(id);
        Expense updated = new Expense(id, request.item().trim(), request.category().trim(),
                request.amount(), request.expenseDate(), request.notes() == null ? "" : request.notes().trim());
        expenses.put(id, updated);
        return updated;
    }

    public void delete(long id) {
        if (expenses.remove(id) == null) throw new ExpenseNotFoundException(id);
    }

    public BudgetSummary summary(YearMonth month) {
        List<Expense> selected = list(null, month);
        BigDecimal total = selected.stream().map(Expense::amount).reduce(BigDecimal.ZERO, BigDecimal::add);
        var byCategory = selected.stream().collect(java.util.stream.Collectors.groupingBy(
                Expense::category, java.util.TreeMap::new,
                java.util.stream.Collectors.reducing(BigDecimal.ZERO, Expense::amount, BigDecimal::add)));
        return new BudgetSummary(month == null ? "ALL" : month.toString(), selected.size(), total, byCategory);
    }
}
