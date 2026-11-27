package com.household.budget;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ExpenseController {
    private final BudgetService service;
    public ExpenseController(BudgetService service) { this.service = service; }

    @GetMapping("/health")
    public java.util.Map<String, String> health() {
        return java.util.Map.of("status", "UP", "application", "household-budget-api");
    }

    @PostMapping("/expenses")
    @ResponseStatus(HttpStatus.CREATED)
    public Expense create(@Valid @RequestBody ExpenseRequest request) { return service.create(request); }

    @GetMapping("/expenses")
    public List<Expense> list(
            @RequestParam(name = "category", required = false)
            String category,

            @RequestParam(name = "month", required = false)
            @DateTimeFormat(pattern = "yyyy-MM")
            YearMonth month) {

         List<Expense> data = service.list(category, month);
        return data;
    }

    @GetMapping("/expenses/{id}")
    public Expense get(@PathVariable long id) { return service.get(id); }

    @PutMapping("/expenses/{id}")
    public Expense update(@PathVariable long id, @Valid @RequestBody ExpenseRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/expenses/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) { service.delete(id); }

    @GetMapping("/budget/summary")
    public BudgetSummary summary(@RequestParam(required = false)
                                 @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {
        return service.summary(month);
    }
}
