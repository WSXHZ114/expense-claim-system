package springbootprojectDemo.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import springbootprojectDemo.pojo.ExpenseItem;

@Mapper
public interface ExpenseItemMapper {

    @Insert("insert into expense_item(report_id, expense_date, description, amount, create_time) VALUES (#{reportId},#{expenseDate},#{description},#{amount},#{createTime})")
    void insert(ExpenseItem expenseItem);

    //@Update("update expense_item set amount=#{amount},description=#{description},expense_date=#{expenseDate} where report_id=#{reportId} ")
    void update(ExpenseItem expenseItem);
}
