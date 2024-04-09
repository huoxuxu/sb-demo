package com.hxx.sbcommon.module.chain2;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-12-25 17:00:50
 **/
public abstract class AbstractPaymentProcessor {
    /**
     * 下一个节点
     */
    protected AbstractPaymentProcessor next = null;

    public void execute(Payment context) throws Exception {
        // 上层未执行成功，不再执行
        if (!context.isSuccess()) {
            return;
        }
        // 执行当前阶段
        doHandler(context);
        // 判断是否还有下个责任链节点，没有的话，说明已经是最后一个节点
        if (getNext() != null) {
            getNext().execute(context);
        }
    }

    public AbstractPaymentProcessor getNext() {
        return next;
    }

    public void setNext(AbstractPaymentProcessor next) {
        this.next = next;
    }

    public abstract void doHandler(Payment content) throws Exception;

    public static class Builder {
        private AbstractPaymentProcessor head;
        private AbstractPaymentProcessor tail;

        public Builder addHandler(AbstractPaymentProcessor handler) {
            if (this.head == null) {
                this.head = handler;
            } else {
                this.tail.setNext(handler);
            }
            this.tail = handler;
            return this;
        }

        public AbstractPaymentProcessor build() {
            return this.head;
        }
    }

}
