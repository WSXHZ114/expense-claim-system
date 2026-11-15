package springbootprojectDemo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseReport {
    private Long id;                    //主键ID
    private String reportNo;            //报销单号
    private String employeeNo;          //报销人工号
    private String reason;              //报销事由
    private String category;            //费用类别
    private BigDecimal totalAmount;     //总金额
    private String status;              //当前状态
    private LocalDateTime submitTime;   //提交时间
    private LocalDateTime createTime;   //创建时间
    private LocalDateTime updateTime;   //更新时间
}
