package com.wentry.wrpc.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: tangwc
 */
public class Pipeline {

    //越早添加，越早执行
    protected final List<InboundFilter> inboundFilters = new ArrayList<>();
    //越早添加，越晚执行
    protected final List<OutboundFilter> outBoundFilters = new ArrayList<>();

    public Pipeline addLast(Filter filter) {
        if (filter instanceof InboundFilter) {
            inboundFilters.add((InboundFilter) filter);
        }
        if (filter instanceof OutboundFilter) {
            outBoundFilters.add((OutboundFilter) filter);
        }
        return this;
    }

}
