package springbootprojectDemo.service;

import springbootprojectDemo.dto.EmployeeCreateReq;
import springbootprojectDemo.dto.EmployeeUpdateReq;
import springbootprojectDemo.pojo.Emp;
import springbootprojectDemo.pojo.PageBean;

public interface EmpService {
    void add(EmployeeCreateReq employeeCreateReq);

    Emp find(Integer id);

    void update(EmployeeUpdateReq employeeUpdateReq);

    PageBean page(Integer page, Integer size,String name, String empNo,String phone);
}
