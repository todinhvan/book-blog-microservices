package vn.van.spring_ai_service.dto.response;

public record BillItem(
        String itemName,
        String unit,
        Integer quantity,
        Double price,
        Double subTotal
) {
}
