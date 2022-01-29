package com.abcd.common.constant;


/**
 * 订单相关常量
 * @author 
 */
public interface OrderConstant {


    /**
     * 已拒绝
     * todo 在任何一个审核环境被拒绝
     */
    Integer ORDER_STATUS_REFUSE = -2;
    /**
     * 已取消
     * todo 在订单最终放款完成之前，用户主动取消
     */
    Integer ORDER_STATUS_CANCEL = -1;
    /**
     * 已提交
     * todo 用户刚刚提交订单，等待商家确认(审核)
     */
    Integer ORDER_STATUS_SUBMIT = 0;
    /**
     * 商家已审核
     * todo 商家已经确认审核，等待平台业务审核
     */
    Integer ORDER_STATUS_M_ADUIT = 1;
    /**
     * 业务已审核
     * todo 平台业务已审核，交由平台风控审核
     */
    Integer ORDER_STATUS_P_ADUIT = 2;
    /**
     * 风控已审核
     * todo 平台风控已审核，等待资金方审核
     */
    Integer ORDER_STATUS_R_ADUIT = 3;
    /**
     * 资方已通过
     * todo 资金方风控审核通过，等待财务审核
     */
    Integer ORDER_STATUS_Z_ADUIT = 4;
    /**
     * 财务已审核
     * todo 平台财务已经审核，等待总经理审核
     */
    Integer ORDER_STATUS_C_ADUIT = 5;
    /**
     * 审核通过
     * todo 平台总经理已经审核，所有审核均已通过，等待放款
     */
    Integer ORDER_STATUS_ALL_ADUIT = 6;
    /**
     * 已完成
     * todo 跟商家确认放款，商家可以予以交易
     */
    Integer ORDER_STATUS_FINISH = 7;

    /**
     * 账单自动还款
     * todo 账单自动还款
     */
    Integer REPAYMENT_TYPE_AUTO = 0;
    /**
     * 账单自动还款
     * todo 账单主动还款
     */
    Integer REPAYMENT_TYPE = 1;
    /**
     * 待账单确认
     * todo 待账单确认
     */
    Integer REPAYMENT_STATUS_INIT = 0;
    /**
     * 待还款
     * todo 待还款
     */
    Integer REPAYMENT_STATUS_PEND = 1;
    /**
     * 订单处理中
     * todo 订单处理中
     */
    Integer REPAYMENT_STATUS_PROCESS = 2;
    /**
     * 还款成功
     * todo 还款成功
     */
    Integer REPAYMENT_STATUS_SUCCESS= 7;
}
