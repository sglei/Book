package com.sglei.basemodule.net.event;

/**
 * @creator zane
 * @time 2018/12/19 15:56
 */
public interface BasePresenter {
    /**
     * 默认的开始,在activity中初始化
     */
    void start();

    /**
     * 在activity中的ondestoy 调用 在此方法中将资源至null,
     * 此处略嫌麻烦,但是如果把presenter层搞成抽象类,在里面添加成员变量和方法体,
     * 就有点失去了味道,所以还是选择了这种方式代替下列注释的部分.
     */
    void onDestroy();
}