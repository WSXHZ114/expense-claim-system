package springbootprojectDemo.mapper;

import org.apache.ibatis.annotations.*;
import springbootprojectDemo.pojo.ApprovalRecord;
import springbootprojectDemo.pojo.Emp;
import springbootprojectDemo.pojo.ExpenseReport;

import java.time.LocalDateTime;
import java.util.List;

import java.math.BigDecimal;


@Mapper
public interface ExpenseReportMapper {

    @Select("select id, emp_no, name, department, phone, status, update_time, create_time from employee where emp_no=#{empNo}")
    Emp findByNo(String no);

    @Insert("insert into expense_report(report_no, employee_no, reason, category, total_amount, status,create_time, update_time) VALUES (#{reportNo},#{employeeNo},#{reason},#{category},#{totalAmount},#{status},#{createTime},#{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(ExpenseReport report);

    @Update("update expense_report set total_amount=#{totalAmount} where id=#{id}")
    void updateTotalAmount(Long id, BigDecimal totalAmount);

    @Select("select name from employee where emp_no=#{empNo}")
    String findNameById(String empNo);

    void update(ExpenseReport expenseReport);

    @Select("select status from expense_report where report_no=#{reportNo}")
    String findStatusByReportNo(String reportNo);

    @Update("update expense_report set total_amount=#{totalAmount} where report_no=#{reportNo}")
    void updateTotalAmountByReportNo(String reportNo, BigDecimal totalAmount);

    @Update("update expense_report set status='SUBMITTED',submit_time=NOW() where report_no=#{reportNo}")
    void submitById(String reportNo);

    @Insert("insert into approval_record (report_no, approver_no, result, comment, approval_time) VALUES(#{reportNo},#{approverNo},#{result},#{comment},#{approvalTime}) ")
    void createRecord(ApprovalRecord approvalRecord);

    @Select("select report_no from approval_record where report_no=#{reportNo}")
    String findReportNo(String reportNo);

    @Select("select * from approval_record where report_no=#{reportNo}")
    ApprovalRecord findApprovalByReportNo(String reportNo);

    @Select("select count(*) from employee where emp_no=#{reportNo}")
    Long findIdByapproverNo(String reportNo);

    @Update("update expense_report set status=#{result} where report_no=#{reportNo}")
    void updateStatus(String reportNo, String result);

    @Select("select * from expense_report where status='SUBMITTED' ")
    List<ExpenseReport> findAllSUBMITTED();

    @Select("select total_amount from expense_report where category=#{category}")
    List<ExpenseReport> findSumByCategory(String category);

    @Select("select total_amount from expense_report where employee_no=#{empNo} and status='APPROVED' ")
    List<ExpenseReport> findSumByEmpNo(String empNo);

    @Select("select count(*) from expense_report where employee_no=#{empNo} and status='APPROVED' ")
    Long findNumByEmpNo(String empNo);

}
