package springbootprojectDemo.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springbootprojectDemo.BusinessException;
import springbootprojectDemo.dto.ApprovalRecordCreateReq;
import springbootprojectDemo.dto.ExpenseReportCreateReq;
import springbootprojectDemo.dto.ExpenseReportUpdateReq;
import springbootprojectDemo.mapper.ExpenseItemMapper;
import springbootprojectDemo.mapper.ExpenseReportMapper;
import springbootprojectDemo.pojo.*;
import springbootprojectDemo.service.ExpenseReportService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import springbootprojectDemo.utils.StringUtils;

@Service
public class ExpenseReportServiceImpl implements ExpenseReportService {
    private static final String DRAFT = "DRAFT";

    @Autowired
    ExpenseReportMapper expenseReportMapper;

    @Autowired
    ExpenseItemMapper expenseItemMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createReport(ExpenseReportCreateReq expenseReportCreateReq) {
        Emp empById = expenseReportMapper.findByNo(expenseReportCreateReq.getEmployeeNo());
        if (empById == null) throw new BusinessException("没有这个员工");
        if (empById.getStatus() == 0) throw new BusinessException("离职员工不可报销");


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        String timeStr = LocalDateTime.now().format(formatter);

        int randomNum = ThreadLocalRandom.current().nextInt(1000, 10000);

        String reportNo = "BX" + timeStr + randomNum;

        ExpenseReport report = new ExpenseReport();
        report.setReportNo(reportNo);                                        //报销单号
        report.setEmployeeNo(expenseReportCreateReq.getEmployeeNo());        //报销人工号
        report.setReason(expenseReportCreateReq.getReason());                //报销事由
        report.setCategory(expenseReportCreateReq.getCategory());            //报销类型
        report.setStatus(DRAFT);                                //当前状态（新建默认草稿）
        report.setTotalAmount(BigDecimal.ZERO);                 //总金额（先默认为零）
        report.setCreateTime(LocalDateTime.now());              //创建时间
        report.setUpdateTime(LocalDateTime.now());              //修改时间
        expenseReportMapper.insert(report);

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (ExpenseReportCreateReq.Item item : expenseReportCreateReq.getItems()) {
            ExpenseItem expenseItem = new ExpenseItem();
            expenseItem.setReportId(report.getId());            //所属报销单号
            expenseItem.setExpenseDate(item.getExpenseDate());  //费用发生日期
            expenseItem.setDescription(item.getDescription());  //费用说明
            expenseItem.setAmount(item.getAmount());            //金额
            expenseItem.setCreateTime(LocalDateTime.now());     //创建时间
            totalAmount = totalAmount.add(expenseItem.getAmount());
            expenseItemMapper.insert(expenseItem);
        }
        expenseReportMapper.updateTotalAmount(report.getId(), totalAmount);
    }

    @Override
    public String findNameByNo(String empNo) {
        return expenseReportMapper.findNameById(empNo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateReport(ExpenseReportUpdateReq expenseReportUpdateReq) {
        String status = expenseReportMapper.findStatusByReportNo(expenseReportUpdateReq.getReportNo());
        if (status == null) throw new BusinessException("报销单不存在");
        if (!status.equals(DRAFT)) throw new BusinessException("只有草稿可以修改");


        if (expenseReportUpdateReq.getReason() != null) {
            StringUtils.assess(expenseReportUpdateReq.getReason(), "报销事由", 0, 255);
        }
        if (expenseReportUpdateReq.getCategory() != null) {
            if (expenseReportUpdateReq.getCategory().isBlank()) throw new BusinessException("费用类型不能为空");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        if (!(expenseReportUpdateReq.getItems().isEmpty())) {
            for (ExpenseReportUpdateReq.Item item : expenseReportUpdateReq.getItems()) {
                ExpenseItem expenseItem = new ExpenseItem();
                expenseItem.setExpenseDate(item.getExpenseDate());     //费用发生日期
                if (item.getDescription() != null) {
                    StringUtils.assess(item.getDescription(), "费用说明", 0, 255);
                    expenseItem.setDescription(item.getDescription());  //费用说明
                }
                expenseItem.setAmount(item.getAmount());               //金额
                expenseItem.setId(item.getId());                       //所属报销单ID
                totalAmount = totalAmount.add(expenseItem.getAmount());
                expenseItemMapper.update(expenseItem);
            }
        }

        ExpenseReport expenseReport = new ExpenseReport();
        BeanUtils.copyProperties(expenseReportUpdateReq, expenseReport);
        expenseReport.setUpdateTime(LocalDateTime.now());
        expenseReportMapper.update(expenseReport);
        if (!(expenseReportUpdateReq.getItems().isEmpty())) {
            expenseReportMapper.updateTotalAmountByReportNo(expenseReport.getReportNo(), totalAmount);
        }
    }

    @Override
    public void submitReport(String reportNo) {
        if (expenseReportMapper.findStatusByReportNo(reportNo).equals(DRAFT)) {
            expenseReportMapper.submitById(reportNo);
        } else {
            throw new BusinessException("只有草稿状态可以提交");
        }


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createRecord(ApprovalRecordCreateReq approvalRecordCreateReq) {

        if (expenseReportMapper.findIdByapproverNo(approvalRecordCreateReq.getEmployeeNo()) == 0) {
            throw new BusinessException("审批人ID不存在");
        }
        if (expenseReportMapper.findReportNo(approvalRecordCreateReq.getReportNo()) != null) {
            throw new BusinessException("不得重复审批");
        }
        if (expenseReportMapper.findStatusByReportNo(approvalRecordCreateReq.getReportNo()).equals(DRAFT)) {
            throw new BusinessException("草稿状态不能审批");
        }

        ApprovalRecord approvalRecord = new ApprovalRecord();
        BeanUtils.copyProperties(approvalRecordCreateReq, approvalRecord);
        approvalRecord.setApproverNo(approvalRecordCreateReq.getEmployeeNo());
        approvalRecord.setApprovalTime(LocalDateTime.now());
        expenseReportMapper.createRecord(approvalRecord);
        expenseReportMapper.updateStatus(approvalRecord.getReportNo(), approvalRecord.getResult());
    }

    @Override
    public ApprovalRecord findApproval(String reportNo) {
        return expenseReportMapper.findApprovalByReportNo(reportNo);
    }

    @Override
    public List<ExpenseReport> findAllSUBMITTED() {
        return expenseReportMapper.findAllSUBMITTED();
    }

    @Override
    public BigDecimal findSumByCategory(String category) {
        List<ExpenseReport> expenseReport = expenseReportMapper.findSumByCategory(category);
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (ExpenseReport report : expenseReport) {
            totalAmount = totalAmount.add(report.getTotalAmount());
        }
        return totalAmount;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExpenseSum findSumByEmpNo(String empNo) {
        List<ExpenseReport> report = expenseReportMapper.findSumByEmpNo(empNo);
        Long numByEmpNo = expenseReportMapper.findNumByEmpNo(empNo);
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (ExpenseReport expenseReport : report) {
            totalAmount = totalAmount.add(expenseReport.getTotalAmount());
        }

        ExpenseSum expenseSum = new ExpenseSum();
        expenseSum.setAmount(totalAmount);
        expenseSum.setNumber(numByEmpNo);
        return expenseSum;
    }

}
