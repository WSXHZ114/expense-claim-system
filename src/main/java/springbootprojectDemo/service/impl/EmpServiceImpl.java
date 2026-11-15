package springbootprojectDemo.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springbootprojectDemo.BusinessException;
import springbootprojectDemo.dto.EmployeeCreateReq;
import springbootprojectDemo.dto.EmployeeUpdateReq;
import springbootprojectDemo.mapper.EmpMapper;
import springbootprojectDemo.pojo.Emp;
import springbootprojectDemo.pojo.PageBean;
import springbootprojectDemo.service.EmpService;
import springbootprojectDemo.utils.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

import static springbootprojectDemo.utils.StringUtils.assess;

@Service
public class EmpServiceImpl implements EmpService {

    @Autowired
    EmpMapper empMapper;

    @Override
    public void add(EmployeeCreateReq employeeCreateReq) {
        if (empMapper.countByEmpNo(employeeCreateReq.getEmpNo()) > 0) throw new BusinessException("工号不能重复");
        //调用自制的工具类检验非法数据（工号）
        StringUtils.assess(employeeCreateReq.getEmpNo().trim(), "工号", 3, 32);
        //调用自制的工具类检验非法数据（姓名）
        assess(employeeCreateReq.getName(), "姓名", 2, 32);
        //调用自制的工具类检验非法数据（部门）
        assess(employeeCreateReq.getDepartment(), "部门", 3, 32);

        Emp emp = new Emp();
        BeanUtils.copyProperties(employeeCreateReq, emp);
        emp.setCreateTime(LocalDateTime.now());
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.insert(emp);
    }

    @Override
    public Emp find(Integer id) {
        return empMapper.findById(id);
    }

    @Override
    public void update(EmployeeUpdateReq employeeUpdateReq) {
        Long count = empMapper.countById(employeeUpdateReq.getId());
        if (count == 0) throw new BusinessException("员工不存在，id=" + employeeUpdateReq.getId());

        if (employeeUpdateReq.getEmpNo() != null) {
            String newEmpNo = employeeUpdateReq.getEmpNo().trim();
            String oldEmpNo = empMapper.findEmpNoById(employeeUpdateReq.getId());
            //调用自制的工具类检验非法数据（工号）
            StringUtils.assess(newEmpNo, "工号", 3, 32);
            if (empMapper.countByEmpNo(newEmpNo) > 0) {
                if (!(newEmpNo.equals(oldEmpNo))) {
                    throw new BusinessException("工号不能重复");
                }
            }
            employeeUpdateReq.setEmpNo(newEmpNo);
        }
        //调用自制的工具类检验非法数据（姓名）
        if (employeeUpdateReq.getName() != null) {
            assess(employeeUpdateReq.getName().trim(), "姓名", 2, 32);
        }
        //调用自制的工具类检验非法数据（部门）
        if (employeeUpdateReq.getDepartment() != null) {
            assess(employeeUpdateReq.getDepartment().trim(), "部门", 3, 32);
        }
        Emp emp = new Emp();
        BeanUtils.copyProperties(employeeUpdateReq, emp);
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.updateById(emp);
    }

    @Override
    public PageBean page(Integer page, Integer size, String name, String empNo, String phone) {
        Long count = empMapper.count(name, empNo, phone);
        if (count == 0) {
            return new PageBean(0L, List.of());
        }
        Integer start = (page - 1) * size;
        List<Emp> empList = empMapper.page(start, size, name, empNo, phone);

        return new PageBean(count, empList);
    }
}
