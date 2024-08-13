package com.wentry.wrpc.filter;

import com.alibaba.fastjson2.JSONObject;
import com.wentry.wrpc.common.RpcReq;
import com.wentry.wrpc.common.RpcResp;
import com.wentry.wrpc.exchange.Invoker;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: tangwc
 */
public class PipelineInvoker implements Invoker {

    private Invoker delegate;
    private final List<InboundFilter> inboundFilters = new ArrayList<>();
    private final List<OutboundFilter> outboundFilters = new ArrayList<>();

    private PipelineInvoker() {
    }

    public PipelineInvoker(Pipeline pipeline, Invoker delegate) {

        for (InboundFilter inboundFilter : pipeline.inboundFilters) {
            if (inboundFilter.sharable()) {
                inboundFilters.add(inboundFilter);
            }else{
                //简单搞个json克隆了，存在效率问题
                inboundFilters.add(JSONObject.parseObject(JSONObject.toJSONString(inboundFilter), inboundFilter.getClass()));
            }
        }

        for (OutboundFilter outBoundFilter : pipeline.outBoundFilters) {
            if (outBoundFilter.sharable()) {
                outboundFilters.add(outBoundFilter);
            }else{
                //简单搞个json克隆了，存在效率问题
                outboundFilters.add(JSONObject.parseObject(JSONObject.toJSONString(outBoundFilter), outBoundFilter.getClass()));
            }
        }

        this.delegate = delegate;
    }

    @Override
    public RpcResp invoke(RpcReq req) {
        //顺序
        for (InboundFilter inboundFilter : inboundFilters) {
            if (inboundFilter.target(req)) {
                inboundFilter.inbound(req);
            }
        }

        //执行
        RpcResp resp = delegate.invoke(req);

        //倒叙
        for (int i = outboundFilters.size() - 1; i >= 0; i--) {
            OutboundFilter outboundFilter = outboundFilters.get(i);
            if (outboundFilter.target(req, resp)) {
                outboundFilter.outbound(req, resp);
            }
        }
        return resp;
    }

    @Override
    public void shutDown() {
        delegate.shutDown();
    }
}
