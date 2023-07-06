package com.lc.cmn.client;

import com.lc.jyxt.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("service-cmn")
public interface DictFeignClient {

    @GetMapping("/admin/cmn/dict/{value}")
    Result<String> getName(@PathVariable("value") String value);

    @GetMapping("/admin/cmn/dict/{value}/{dictCode}")
    Result<String> getName(@PathVariable("value") String value,
                                  @PathVariable("dictCode") String dictCode);
}
