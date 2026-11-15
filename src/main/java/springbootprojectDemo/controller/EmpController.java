package springbootprojectDemo.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springbootprojectDemo.dto.EmployeeCreateReq;
import springbootprojectDemo.dto.EmployeeUpdateReq;
import springbootprojectDemo.pojo.Emp;
import springbootprojectDemo.pojo.PageBean;
import springbootprojectDemo.pojo.Result;
import springbootprojectDemo.service.EmpService;

@Slf4j
@RestController
public class EmpController {

    @Autowired
    private EmpService empService;

    @PostMapping("/emps")
    public Result add(@Valid @RequestBody EmployeeCreateReq employeeCreateReq) {
        log.info("新增员工:{}", employeeCreateReq.getEmpNo());
        empService.add(employeeCreateReq);
        return Result.success();
    }

    @GetMapping("/emps/{id}")
    public Result find(@PathVariable Integer id) {
        log.info("根据id查询员工:{}", id);
        Emp emp = empService.find(id);
        return Result.success(emp);
    }

    @GetMapping("/emps")
    public Result findByCondition(@RequestParam(defaultValue = "1") Integer page,
                                  @RequestParam(defaultValue = "10") Integer size,
                                  String name, String empNo, String phone) {
        log.info("根据条件分页查询员工");
        PageBean pageBean = empService.page(page, size, name, empNo, phone);
        return Result.success(pageBean);
    }

    @PutMapping("/emps")
    public Result update(@Valid @RequestBody EmployeeUpdateReq employeeUpdateReq) {
        log.info("根据id修改员工:{}", employeeUpdateReq.getId());
        empService.update(employeeUpdateReq);
        return Result.success();
    }

}
