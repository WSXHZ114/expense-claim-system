package springbootprojectDemo.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class EmployeeUpdateReq {

    @NotNull(message = "ID不能为空")
    @Positive(message = "ID必须为正数")
    private Long id;

    @Size(max = 32, message = "工号最长32位")
    @Pattern(regexp = "^[A-Za-z0-9]*$", message = "工号只能包含字母和数字")
    private String empNo;

    @Size(max = 32, message = "姓名最长32字")
    private String name;

    @Size(max = 32, message = "部门名最长32字")
    private String department;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @Min(value = 0, message = " 状态只能是0或1")
    @Max(value = 1, message = " 状态只能是0或1")
    private Integer status;

}
