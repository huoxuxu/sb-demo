package com.hxx.sbweb.controller.webDemo.bindReqPara;

import com.hxx.sbcommon.common.json.JsonUtil;
import com.hxx.sbweb.controller.base.BaseRestController;
import com.hxx.sbweb.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-09-04 16:54:33
 **/
@Slf4j
@RestController
@RequestMapping("/reqbody")
public class RequestBodyController extends BaseRestController {

    /**
     * http://localhost:8082/reqbody/json
     * {"id":99,"name":"sx1999"}@Validated
     *
     * @param u
     * @return
     */
    @PostMapping("/json")
    public String reqJson(@Valid @RequestBody User u) {
        return JsonUtil.toJSON(u);
    }

    /**
     * http://localhost:8082/reqbody/list
     * [1,22,33]
     *
     * @param ids
     * @return
     */
    @PostMapping("/list")
    public List<Integer> req1(@RequestBody List<Integer> ids) {
        return ids;
    }

}
