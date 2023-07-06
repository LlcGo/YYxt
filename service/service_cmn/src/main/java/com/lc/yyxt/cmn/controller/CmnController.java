package com.lc.yyxt.cmn.controller;

import com.lc.jyxt.common.result.Result;
import com.lc.yygh.model.cmn.Dict;
import com.lc.yyxt.cmn.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author Lc
 * @Date 2023/6/26
 * @Description
 */
@Api(value = "数字字典接口")
@RestController
@RequestMapping("/admin/cmn/dict")
@CrossOrigin
public class CmnController {
    @Resource
    private DictService dictService;

    @GetMapping("/export")
    public void export(HttpServletResponse response){
        dictService.exportExcel(response);
    }

    @PostMapping("/import")
    public Result importFile(MultipartFile file){
        dictService.importFile(file);
        return Result.ok();
    }

    @ApiOperation(value = "查找子标签")
    @GetMapping("/findChildDate/{id}")
    public Result<List<Dict>> findChildDate(@PathVariable("id")Long id){
         return Result.ok(dictService.findChildDate(id));
    }

    @GetMapping("/{value}/{dictCode}")
    public Result<String> getName(@PathVariable String value,
                                  @PathVariable String dictCode){
          return Result.ok(dictService.getDicName(value,dictCode));
    }


    @GetMapping("/{value}")
    public Result<String> getName(@PathVariable String value){
        return Result.ok(dictService.getDicName(value,""));
    }

    @GetMapping("/findDicChlidren/{dictCode}")
    public Result<List<Dict>> findDicChlidren(@PathVariable String dictCode){
        return Result.ok(dictService.findDicChlidren(dictCode));
    }


}
