package com.wentry.wrpc.filter;

/**
 * @Description:
 * @Author: tangwc
 */
public interface Filter {

    /**
     * 是否可共享
     * 一般来说：
     * 1.带有状态的不可共享
     * 2.InOutBound，如果有插粧操作，则必须要共享，状态隔离问题需要在实现里面自己解决
     */
    default boolean sharable() {
        return true;
    }

}
