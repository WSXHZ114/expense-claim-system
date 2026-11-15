package springbootprojectDemo.mapper;

import org.apache.ibatis.annotations.*;
import springbootprojectDemo.pojo.Emp;

import java.util.List;

@Mapper
public interface EmpMapper {

    @Insert("INSERT INTO employee(emp_no, name, department, phone, status, update_time, create_time) VALUES (#{empNo},#{name}, #{department}, #{phone}, #{status},#{updateTime},#{createTime})")
    void insert(Emp emp);

    @Select("select id, emp_no, name, department, phone, status, update_time, create_time from employee where id=#{id}")
    Emp findById(Integer id);

    @Select("select count(*) from employee where id=#{id}")
    Long countById(Long id);

    void updateById(Emp emp);

    //@Select("select count(*) from employee")
    Long count(@Param("name") String name,
               @Param("empNo") String empNo,
               @Param("phone") String phone);

    //@Select("select * from emp limit #{start},#{pageSize}")
    List<Emp> page(@Param("start") Integer start,
                   @Param("pageSize") Integer pageSize,
                   @Param("name") String name,
                   @Param("empNo") String empNo,
                   @Param("phone") String phone);


    @Select("select count(*) from employee where emp_no=#{empNo}")
    Long countByEmpNo(String empNo);
    @Select("select emp_no from employee where id=#{id}")
    String findEmpNoById(Long id);
}
