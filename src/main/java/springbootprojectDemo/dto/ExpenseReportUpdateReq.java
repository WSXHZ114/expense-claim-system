package springbootprojectDemo.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class ExpenseReportUpdateReq {

    @Size(max = 20, message = "报销单号最长20字")
    private String reportNo;

    @Size(max = 32, message = "报销人工号最长32字")
    private String employeeNo;

    @Size(max = 255, message = "报销事由最长 255 字")
    private String reason;

    @Pattern(regexp = "TRAVEL|MEAL|OFFICE|OTHER",
            message = "费用类别只能是 TRAVEL / MEAL / OFFICE / OTHER")
    private String category;

    @Size(max = 50, message = "一张单最多 50 条明细")
    @Valid
    private List<ExpenseReportUpdateReq.Item> items;

    @Data
    public static class Item {

        private Long id;

        @PastOrPresent(message = "费用日期不能晚于今天")
        private LocalDate expenseDate;

        @Size(max = 255, message = "费用说明最长255字")
        private String description;

        @DecimalMin(value = "0.01", message = "单笔金额必须大于0")
        @Digits(integer = 10, fraction = 2, message = "金额最多10位整数、2位小数")
        private BigDecimal amount;
    }
}
