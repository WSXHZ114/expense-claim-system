package springbootprojectDemo.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class EmployeeCreateReq {

    @NotBlank(message = "工号不能为空")
    @Size(max = 32, message = "工号最长32位")
    @Pattern(regexp = "^[A-Za-z0-9]*$", message = "工号只能包含字母和数字")
    private String empNo;

    @NotBlank(message = "姓名不能为空")
    @Size(max = 32, message = "姓名最长32字")
    private String name;

    @NotBlank(message = "部门不能为空")
    @Size(max = 32, message = "部门名最长32字")
    private String department;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = " 状态只能是0或1")
    @Max(value = 1, message = " 状态只能是0或1")
    private Integer status;
}
