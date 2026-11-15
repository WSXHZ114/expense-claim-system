# 中小企业费用报销审批管理系统

> Java 17 · Spring Boot 4.1 · MyBatis 4 · MySQL 8 · REST API

员工提交报销单（主表 + 多条明细），主管整单审批（通过 / 驳回），财务按人 / 时间 / 类别统计。
附带一个零依赖的单页调试控制台，启动后打开 `http://localhost:8080/index.html` 即可点着用。

---

## 功能

| 模块 | 能力 |
|---|---|
| 员工管理 | 新增、修改、分页查询（姓名 / 工号 / 手机号精确筛选）、按 ID 查看 |
| 报销单 | 建草稿（主表 + 明细，总额由后端汇总）、改草稿、提交、条件分页 |
| 审批 | 通过 / 驳回（驳回必填意见）、审批记录查询、待审批列表 |
| 统计 | 员工报销汇总、费用类别汇总、待审批列表 |
| 调试控制台 | `src/main/resources/static/index.html`，原生 JS，无需构建 |

---

## 技术栈

| 层次 | 选型 |
|---|---|
| 语言 / 运行时 | Java 17 |
| 框架 | Spring Boot 4.1.2（Web MVC） |
| 持久层 | MyBatis 4.0.1（注解 + XML 动态 SQL） |
| 数据库 | MySQL 8.x，`utf8mb4` |
| 构建 | Maven（自带 `mvnw`） |
| 校验 | Jakarta Bean Validation |

---

## 目录结构

```
src/main/java/springbootprojectDemo
├── controller/     EmpController、ExpenseReportController（只做接收与转发）
├── service/        业务接口
│   └── impl/       业务实现（事务、状态校验、金额汇总）
├── mapper/         MyBatis Mapper 接口
├── dto/            请求对象（带校验注解，不暴露给持久层）
├── pojo/           与数据库表一一对应的实体
├── utils/          字符串校验工具
├── BusinessException / GlobalExceptionHandler   统一异常处理
└── DemoApplication.java

src/main/resources
├── application.properties        数据库连接（需改成你的账号密码）
├── schema.sql                    建表脚本
├── static/index.html             调试控制台
└── springbootprojectDemo/mapper/*.xml   MyBatis 动态 SQL
```

---

## 数据库设计

四张表，四条一对多关系：

```
employee 1 ──< expense_report    （报销人）
expense_report 1 ──< expense_item     （明细，report_id 关联主键）
expense_report 1 ──< approval_record  （审批记录，report_no 关联单号）
employee 1 ──< approval_record        （审批人）
```

| 表 | 作用 | 关键约束 |
|---|---|---|
| `employee` | 员工（报销人 / 审批人）名册 | `uk_emp_no` 工号唯一；`status` 1 在职 / 0 离职 |
| `expense_report` | 报销单头：谁报的、多少钱、到哪一步 | `uk_report_no` 单号唯一；`total_amount` 非负；`status` 四态 |
| `expense_item` | 每笔费用明细 | 金额非负；`report_id` 关联主表 |
| `approval_record` | 每次审批一行，只追加 | 结果二值；驳回必须有意见 |

**几个设计取舍**

- **主表 + 明细拆开**：一张单对应 N 条费用，明细独立成表才能逐条校验、按日期排序、单独统计。
- **`total_amount` 冗余在主表**：列表排序、汇总都要用它；但只由 Service 遍历明细累加后写入，接口层不接收前端传值。
- **状态只存主表一处**：`expense_item`、`approval_record` 不存状态，避免两处不一致。
- **单号 / 工号作为业务键**：对外用 `report_no`、`emp_no` 交互，可读性好；代价是这两者一旦被修改，历史数据会对不上，因此约定「生成后不可改」。
- **金额统一 `DECIMAL(12,2)`**，Java 端用 `BigDecimal`，不用 `double`。

---

## 快速开始

**1. 建库**

```sql
CREATE DATABASE IF NOT EXISTS db05 DEFAULT CHARACTER SET utf8mb4;
```

**2. 建表 + 灌测试数据**

```bash
mysql -u root -p db05 < src/main/resources/schema.sql
mysql -u root -p db05 < data.sql
```

**3. 改配置**（`src/main/resources/application.properties`）

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/db05
spring.datasource.username=root
spring.datasource.password=你的密码
```

**4. 启动**

```bash
./mvnw spring-boot:run          # Windows: mvnw.cmd spring-boot:run
```

**5. 打开调试控制台**

```
http://localhost:8080/index.html
```

---

## 接口清单

| 方法 | 路径 | 说明 |
|---|---|---|
| POST | `/emps` | 新增员工 |
| PUT | `/emps` | 修改员工（按 id） |
| GET | `/emps/{id}` | 员工详情 |
| GET | `/emps?page=&size=&name=&empNo=&phone=` | 分页查询 |
| POST | `/reports` | 新建报销单（主表 + 明细） |
| PUT | `/reports` | 修改草稿（按单号） |
| PUT | `/reports/submit/{reportNo}` | 提交 |
| GET | `/reports/approval/list` | 待审批列表 |
| POST | `/reports/approval` | 审批通过 / 驳回 |
| GET | `/reports/approval/findApp/{reportNo}` | 查询审批记录 |
| GET | `/reports/approval/amout/{category}` | 费用类别金额汇总 |
| GET | `/reports/approval/findSum/{empNo}` | 员工报销汇总 |

统一返回：

```json
{ "code": 1, "msg": "success", "data": {} }
```

`code = 1` 成功，`code = 0` 业务失败（业务异常同样返回 HTTP 200，请以 `code` 判断）。

---

## 请求示例

**建单**

```bash
curl -X POST http://localhost:8080/reports \
  -H "Content-Type: application/json" \
  -d '{
        "employeeNo": "E001",
        "reason": "10 月上海客户拜访差旅",
        "category": "TRAVEL",
        "items": [
          {"expenseDate": "2026-11-10", "description": "杭州-上海 高铁", "amount": 146.00},
          {"expenseDate": "2026-11-10", "description": "上海 2 晚住宿", "amount": 1180.00}
        ]
      }'
```

**提交**

```bash
curl -X PUT http://localhost:8080/reports/submit/BX2026111518098399
```

**审批**

```bash
curl -X POST http://localhost:8080/reports/approval \
  -H "Content-Type: application/json" \
  -d '{"reportNo":"BX2026111518098399","employeeNo":"E003","result":"APPROVED","comment":"差旅标准内，同意报销"}'
```

---

## 状态流转

```
       提交                     审批通过
DRAFT ───────► SUBMITTED ───────────────► APPROVED
                 │
                 └──── 审批驳回 ─────────► REJECTED
```

- 只有 `DRAFT` 可以修改、提交；
- 只有 `SUBMITTED` 可以审批；
- 重复审批、审核草稿都会被拦截并返回明确提示；
- 已审批的单据不再允许修改。

---

## 事务

| 操作 | 涉及表 | 说明 |
|---|---|---|
| 建草稿 | `expense_report` + `expense_item` | 主表与明细同生同死 |
| 改草稿 | `expense_report` + `expense_item` | 明细更新与总额回写一起提交 |
| 审批 | `expense_report` + `approval_record` | 状态与记录同生同死 |

事务加在 Service 的 `public` 方法上（`@Transactional(rollbackFor = Exception.class)`），Controller 不开事务。

---

## 已知限制

- 建单接口返回 `data:null`，前端拿不到单号，需查库或由后续接口补齐；
- 改草稿按明细主键更新，前端必须传 `expense_item.id`；
- 类别汇总未按状态过滤，员工汇总只统计 `status='APPROVED'`（口径不同，见 `ExpenseReportMapper`）；
- 未实现登录与权限，审批人由请求参数指定；
- `approval_record` 用 `report_no` 关联，`expense_item` 用 `report_id` 关联，两种方式并存。

---

## 作者

WSXHZ114 · 2026-11
