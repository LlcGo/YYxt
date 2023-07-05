package com.lc.yyxt.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lc.yygh.model.cmn.Dict;
import com.lc.yygh.vo.cmn.DictEeVo;
import com.lc.yyxt.cmn.config.DicDataListener;
import com.lc.yyxt.cmn.mapper.DictMapper;
import com.lc.yyxt.cmn.service.DictService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Lc
 * @Date 2023/6/26
 * @Description
 */
@Service
@Slf4j
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Cacheable(value = "dict",keyGenerator = "keyGenerator")
    @Override
    public List<Dict> findChildDate(Long id) {
        List<Dict> childDate = this.baseMapper.findChildDate(id);
        childDate.stream().peek(data -> data.setHasChildren(isChilderen(data.getId()))).collect(Collectors.toList());
        return childDate;
    }

    @Override
    public void exportExcel(HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("数据字典", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename="+ fileName + ".xlsx");
            //查询所有数据
            List<Dict> list = this.list(null);

            List<DictEeVo> dictVoList = list.stream().map(dict -> {
                DictEeVo dictEeVo = new DictEeVo();
                BeanUtils.copyProperties(dict, dictEeVo);
                return dictEeVo;
            }).collect(Collectors.toList());

            EasyExcel.write(response.getOutputStream(), DictEeVo.class)
                    .sheet("数据字典").doWrite(dictVoList);
        } catch (IOException e) {
            log.error("exportExcel err",e);
        }
    }

    @CacheEvict(value = "dict", allEntries=true)
    @Override
    public void importFile(MultipartFile multipartFile) {
        try {
            EasyExcel.read(multipartFile.getInputStream(),DictEeVo.class,new DicDataListener(baseMapper)).sheet().doRead();
        } catch (IOException e) {
            log.error("importFile err",e);
        }
    }

    @Override
    public String getDicName(String value, String dictCode) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isEmpty(dictCode)){
            queryWrapper.eq("value",value);
            Dict dict = this.baseMapper.selectOne(queryWrapper);
            return dict.getName();
        }
        queryWrapper.eq("dict_code",dictCode);
        Dict dict = this.baseMapper.selectOne(queryWrapper);
        Long parentId = dict.getId();
        queryWrapper.eq("parent_id",parentId).eq("value",value);
        Dict finalDict = this.baseMapper.selectOne(queryWrapper);
        return finalDict.getName();
    }


    /**
     * 查询该id是否有子标签
     * @param id
     * @return
     */
    public boolean isChilderen(Long id) {
        LambdaQueryWrapper<Dict> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dict::getParentId,id);
        Integer count = baseMapper.selectCount(queryWrapper);
        return count > 0;
    }
}
