package com.hxx.sbrest.webUtil;

import com.hxx.sbcommon.common.other.traceId.TraceUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-01-10 14:03:22
 **/
@Slf4j
public class HttpServletRequestUtil {
    private static final String TRACE_ID = TraceUtil.TRACE_ID;

    public static void initTraceFromRequest(HttpServletRequest request) {
        String traceId;

        // 从前端表单提交里获取traceId
        traceId = request.getParameter(TRACE_ID);
        if (StringUtils.isNotBlank(traceId)) {
            TraceUtil.set(traceId);
            return;
        }

        // 从forward后端转发请求里获取traceId
        traceId = (String) request.getAttribute(TRACE_ID);
        if (StringUtils.isNotBlank(traceId)) {
            TraceUtil.set(traceId);
            return;
        }

        // 从请求url路径中里获取traceId
        Map pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        if (pathVariables != null) {
            traceId = (String) pathVariables.get(TRACE_ID);
        }
        if (StringUtils.isNotBlank(traceId)) {
            TraceUtil.set(traceId);
            return;
        }

        // 以上都获取不到的话，就生成一个traceId并填充到MDC以及Session中
        String traceId1 = TraceUtil.init();
        HttpSession session = request.getSession();
        session.setAttribute(TraceUtil.TRACE_ID, traceId1);
    }


}
