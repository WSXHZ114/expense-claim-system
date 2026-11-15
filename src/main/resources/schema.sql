SET NAMES utf8mb4;

CREATE TABLE IF NOT EXISTS employee (
                                        id INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
                                        emp_no VARCHAR(32) NOT NULL COMMENT '工号',
                                        name VARCHAR(32) NOT NULL COMMENT '姓名',
                                        department VARCHAR(32) COMMENT '部门',
                                        phone VARCHAR(11) COMMENT '手机号',
                                        status INT NOT NULL COMMENT '状态：1(在职), 0(离职)',
                                        create_time DATETIME NOT NULL COMMENT '创建时间',
                                        update_time DATETIME NOT NULL COMMENT '更新时间',
                                        UNIQUE KEY uk_emp_no (emp_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='员工表';

CREATE TABLE IF NOT EXISTS expense_report (
                                              id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
                                              report_no VARCHAR(32) NOT NULL COMMENT '报销单号',
                                              employee_no VARCHAR(32) NOT NULL COMMENT '报销人工号',
                                              reason VARCHAR(255) NOT NULL COMMENT '报销事由',
                                              category VARCHAR(32) NOT NULL COMMENT '费用类别',
                                              total_amount DECIMAL(12,2) NOT NULL DEFAULT 0.00 COMMENT '总金额',
                                              status VARCHAR(16) NOT NULL DEFAULT 'DRAFT' COMMENT '状态：DRAFT, SUBMITTED, APPROVED, REJECTED',
                                              submit_time DATETIME NULL COMMENT '提交时间',
                                              create_time DATETIME NOT NULL COMMENT '创建时间',
                                              update_time DATETIME NOT NULL COMMENT '更新时间',
                                              UNIQUE KEY uk_report_no (report_no),
                                              KEY idx_employee_id (employee_no),
                                              KEY idx_status (status),
                                              KEY idx_submit_time (submit_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='报销单表';

CREATE TABLE IF NOT EXISTS expense_item (
                                            id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
                                            report_id BIGINT NOT NULL COMMENT '所属报销单ID',
                                            expense_date DATE NOT NULL COMMENT '费用发生日期',
                                            description VARCHAR(255) COMMENT '费用说明',
                                            amount DECIMAL(12,2) NOT NULL COMMENT '金额',
                                            create_time DATETIME NOT NULL COMMENT '创建时间',
                                            KEY idx_report_id (report_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='报销明细表';

CREATE TABLE IF NOT EXISTS approval_record (
                                               id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
                                               report_no VARCHAR(32) NOT NULL COMMENT '报销单ID',
                                               approver_no VARCHAR(32) NOT NULL COMMENT '审批人工号',
                                               result VARCHAR(16) NOT NULL COMMENT '审批结果：APPROVED, REJECTED',
                                               `comment` VARCHAR(500) COMMENT '审批意见',
                                               approval_time DATETIME NOT NULL COMMENT '审批时间',
                                               KEY idx_approval_report_id (report_no),
                                               KEY idx_approver_id (approver_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='审批记录表';