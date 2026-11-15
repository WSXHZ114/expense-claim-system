package springbootprojectDemo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseItem {
    private Long id;                    //主键ID
    private Long reportId;              //所属报销单ID
    private LocalDate expenseDate;      //费用发生日期
    private String description;         //费用发生日期
    private BigDecimal amount;          //金额
    private LocalDateTime createTime;   //创建时间
}
