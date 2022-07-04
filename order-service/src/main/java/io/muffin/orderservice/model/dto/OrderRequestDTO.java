package io.muffin.orderservice.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderRequestDTO {

    private Long customerId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private BigDecimal amount;
    private String shippingAddress;
    private String status;
    private List<OrderItemsRequestDTO> orderItems;
}
