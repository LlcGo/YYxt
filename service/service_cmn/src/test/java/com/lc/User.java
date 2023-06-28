package com.lc;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author Lc
 * @Date 2023/6/27
 * @Description
 */
@Data
public class User {

    @ExcelProperty(value = "姓名",index = 0)
    private String name;

    @ExcelProperty(value = "年龄",index = 1)
    private Integer age;
}
