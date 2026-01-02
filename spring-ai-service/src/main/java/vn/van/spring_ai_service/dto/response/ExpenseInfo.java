package vn.van.spring_ai_service.dto.response;

public record ExpenseInfo(
        String category,
        String itemName,
        Double amount
) {
}
