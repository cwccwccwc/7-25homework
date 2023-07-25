package zy.ATM.Mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface ATMMapper {
    //存 取
    @Update("update atm set money=#{money} where id=#{id}")
    void update(@Param("id") int id, @Param("money")double money);

    //查
    @Select("select money from atm where id=#{id}")
    double select (int id);
}
