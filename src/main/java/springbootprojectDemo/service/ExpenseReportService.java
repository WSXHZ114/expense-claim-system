package springbootprojectDemo.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import springbootprojectDemo.dto.ApprovalRecordCreateReq;
import springbootprojectDemo.dto.ExpenseReportCreateReq;
import springbootprojectDemo.dto.ExpenseReportUpdateReq;
import springbootprojectDemo.pojo.ApprovalRecord;
import springbootprojectDemo.pojo.ExpenseReport;
import springbootprojectDemo.pojo.ExpenseSum;

import java.math.BigDecimal;
import java.util.List;

public interface ExpenseReportService {
    void createReport(ExpenseReportCreateReq expenseReportCreateReq);

    String findNameByNo(String reportNo);

    void updateReport(@Valid ExpenseReportUpdateReq expenseReportUpdateReq);

    void submitReport(String reportNo);

    void createRecord(@Valid ApprovalRecordCreateReq approvalRecordCreateReq);

    ApprovalRecord findApproval(String reportNo);

    List<ExpenseReport> findAllSUBMITTED();

    BigDecimal findSumByCategory(String category);

    ExpenseSum findSumByEmpNo(String empNo);
}
