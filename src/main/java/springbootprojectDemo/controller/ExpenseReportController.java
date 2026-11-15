package springbootprojectDemo.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springbootprojectDemo.dto.ApprovalRecordCreateReq;
import springbootprojectDemo.dto.ExpenseReportCreateReq;
import springbootprojectDemo.dto.ExpenseReportUpdateReq;
import springbootprojectDemo.pojo.Result;
import springbootprojectDemo.service.ExpenseReportService;

@Slf4j
@RestController
public class ExpenseReportController {

    @Autowired
    private ExpenseReportService expenseReportService;

    @PostMapping("/reports")
    public Result create(@Valid @RequestBody ExpenseReportCreateReq expenseReportCreateReq) {
        log.info("创建新的报销表:{}", expenseReportService.findNameByNo(expenseReportCreateReq.getEmployeeNo()));
        expenseReportService.createReport(expenseReportCreateReq);
        return Result.success();
    }

    @PutMapping("/reports")
    public Result update(@Valid @RequestBody ExpenseReportUpdateReq expenseReportUpdateReq) {
        log.info("更新报销表:{}", expenseReportUpdateReq.getReportNo());
        expenseReportService.updateReport(expenseReportUpdateReq);
        return Result.success();
    }

    @PutMapping("/reports/submit/{reportNo}")
    public Result submit(@PathVariable String reportNo) {
        log.info("提交报销表:{}", reportNo);
        expenseReportService.submitReport(reportNo);
        return Result.success();
    }

    @PostMapping("/reports/approval")
    public Result approval(@Valid @RequestBody ApprovalRecordCreateReq approvalRecordCreateReq) {
        log.info("创建新的审批表:{}", expenseReportService.findNameByNo(approvalRecordCreateReq.getReportNo()));
        expenseReportService.createRecord(approvalRecordCreateReq);
        return Result.success();
    }

    @GetMapping("/reports/approval/findApp/{reportNo}")
    public Result findApproval(@PathVariable String reportNo) {
        log.info("查询审批表:{}", reportNo);
        return Result.success(expenseReportService.findApproval(reportNo));
    }

    @GetMapping("/reports/approval/list")
    public Result findAllSUBMITTED() {
        log.info("查询待审批报销单列表");
        return Result.success(expenseReportService.findAllSUBMITTED());
    }

    @GetMapping("/reports/approval/amout/{category}")
    public Result findSumByCategory(@PathVariable String category) {
        log.info("查询报销费用总金额:{}", category);
        return Result.success(expenseReportService.findSumByCategory(category));
    }

    @GetMapping("/reports/approval/findSum/{empNo}")
    public Result findSumByEmpNo(@PathVariable String empNo) {
        log.info("查询员⼯的报销汇总:{}", empNo);
        return  Result.success(expenseReportService.findSumByEmpNo(empNo));
    }

}
