package springbootprojectDemo.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class ExpenseReportCreateReq {
    @NotNull(message = "报销人工号不能为空")
    @Size(max = 32, message = "报销人工号最长32字")
    private String employeeNo;

    @NotBlank(message = "报销事由不能为空")
    @Size(max = 255, message = "报销事由最长 255 字")
    private String reason;

    @NotBlank(message = "费用类别不能为空")
    @Pattern(regexp = "TRAVEL|MEAL|OFFICE|OTHER",
            message = "费用类别只能是 TRAVEL / MEAL / OFFICE / OTHER")
    private String category;

    @NotEmpty(message = "至少需要一条明细")
    @Size(max = 50, message = "一张单最多 50 条明细")
    @Valid
    private List<Item> items;

    @Data
    public static class Item {
        @NotNull(message = "费用发生日期不能为空")
        @PastOrPresent(message = "费用日期不能晚于今天")
        private LocalDate expenseDate;

        @NotBlank(message = "费用说明不能为空")
        @Size(max = 255, message = "费用说明最长 255 字")
        private String description;

        @NotNull(message = "金额不能为空")
        @DecimalMin(value = "0.01", message = "单笔金额必须大于 0")
        @Digits(integer = 10, fraction = 2, message = "金额最多 10 位整数、2 位小数")
        private BigDecimal amount;
    }
}
