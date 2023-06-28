package com.lc.yyxt.cmn.config;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.lc.yygh.model.cmn.Dict;
import com.lc.yygh.vo.cmn.DictEeVo;
import com.lc.yyxt.cmn.mapper.DictMapper;
import org.springframework.beans.BeanUtils;

/**
 * @Author Lc
 * @Date 2023/6/27
 * @Description
 */
public class DicDataListener extends AnalysisEventListener<DictEeVo> {

    private DictMapper dictMapper;

    public DicDataListener(DictMapper dictMapper) {
        this.dictMapper = dictMapper;
    }


    @Override
    public void invoke(DictEeVo dictEeVo, AnalysisContext analysisContext) {
        Dict dict = new Dict();
        BeanUtils.copyProperties(dictEeVo,dict);
        dictMapper.insert(dict);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
