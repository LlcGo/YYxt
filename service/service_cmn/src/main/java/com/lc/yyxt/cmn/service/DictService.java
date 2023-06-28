package com.lc.yyxt.cmn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lc.yygh.model.cmn.Dict;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface DictService extends IService<Dict> {
    /**
     * 根据id查找出所有的子标签
     * @param id 标签id
     * @return 所有子标签
     */
    List<Dict> findChildDate(Long id);

    /**
     * 直接导出数据
     */
    void exportExcel(HttpServletResponse response);

    /**
     * 导入数据
     * @param multipartFile
     */
    void importFile(MultipartFile multipartFile);
}
