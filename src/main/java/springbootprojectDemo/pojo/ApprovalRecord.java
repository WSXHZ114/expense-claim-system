package springbootprojectDemo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalRecord {
    private Long id;                        //主键ID
    private String reportNo;                //报销单号
    private String approverNo;              //审批人工号
    private String result;                  //审批结果
    private String comment;                 //审批意见
    private LocalDateTime approvalTime;     //审批时间
}
