package com.example.config.filters;

import com.alibaba.fastjson2.JSONObject;
import com.wentry.wrpc.common.RpcReq;
import com.wentry.wrpc.common.RpcResp;
import com.wentry.wrpc.filter.InOutBoundFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description:
 * @Author: tangwc
 */
public class MetricsOutBoundFilter implements InOutBoundFilter {

    private static final Logger log = LoggerFactory.getLogger(MetricsOutBoundFilter.class);

    private ConcurrentHashMap<String, Long> startMap = new ConcurrentHashMap<>();

    @Override
    public void inbound(RpcReq req) {
        startMap.put(req.getReqId(), System.nanoTime());
    }

    @Override
    public boolean target(RpcReq req) {
        return true;
    }

    @Override
    public void outbound(RpcReq req, RpcResp resp) {
        long ns = System.nanoTime() - startMap.remove(req.getReqId());
        double ms = new BigDecimal(ns).divide(new BigDecimal(1000000), 2, RoundingMode.HALF_UP).doubleValue();
        log.info("req:{},cost:{}", JSONObject.toJSONString(req), ns + "ns, " + ms + "ms");
    }

    @Override
    public boolean target(RpcReq req, RpcResp resp) {
        return true;
    }

}
