package com.lc.yyxt.cmn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lc.yygh.model.cmn.Dict;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DictMapper extends BaseMapper<Dict> {
    List<Dict> findChildDate(Long id);
}
