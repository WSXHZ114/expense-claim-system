package springbootprojectDemo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class ApprovalRecordCreateReq {

    @NotBlank(message = "报销单号不能为空")
    @Size(max = 32, message = "报销单号最长32位")
    private String reportNo;                 //报销单号

    @NotBlank(message = "审批人ID不能为空")
    private String employeeNo;                //审批人工号

    @NotBlank(message = "审批结果不能为空")
    @Pattern(regexp = "APPROVED|REJECTED",
            message = "审批结果只能是 APPROVED / REJECTED")
    private String result;                  //审批结果

    @NotBlank(message = "审批意见不能为空")
    @Size(max = 500, message = "审批意见最长500字")
    private String comment;                 //审批意见
}
