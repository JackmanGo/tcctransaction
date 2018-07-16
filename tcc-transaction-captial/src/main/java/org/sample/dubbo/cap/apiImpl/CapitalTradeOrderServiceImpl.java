package org.sample.dubbo.cap.apiImpl;

import api.TccTransaction;
import api.TccTransactionContext;
import org.sample.dubbo.cap.api.CapitalTradeOrderService;
import org.sample.dubbo.cap.api.dto.CapitalTradeOrderDto;
import org.sample.dubbo.cap.entity.CapTradeOrder;
import org.sample.dubbo.cap.entity.CapitalAccount;
import org.sample.dubbo.cap.repository.CapitalAccountRepository;
import org.sample.dubbo.cap.repository.CapTradeOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component("capitalTradeOrderService")
public class CapitalTradeOrderServiceImpl implements CapitalTradeOrderService{

    @Autowired
    CapitalAccountRepository capitalAccountRepository;

    @Autowired
    CapTradeOrderRepository capTradeOrderRepository;

    /**
     * 创建钱包使用记录，并扣除钱包该订单使用金额
     * @param tradeOrderDto
     * @return
     */
    @Override
    @TccTransaction(confirmMethod = "confirmRecord", cancelMethod = "cancelRecord")
    @Transactional
    public String record(CapitalTradeOrderDto tradeOrderDto, TccTransactionContext context) throws RuntimeException{

        CapTradeOrder foundTradeOrder = capTradeOrderRepository.findByMerchantOrderNo(tradeOrderDto.getMerchantOrderNo());

        //check if trade order has been recorded, if yes, return success directly.
        if (foundTradeOrder == null) {

            //订单id查找不存在，创建一个余额使用订单记录
            CapTradeOrder tradeOrder = new CapTradeOrder(
                    tradeOrderDto.getSelfUserId(),
                    tradeOrderDto.getOppositeUserId(),
                    tradeOrderDto.getMerchantOrderNo(),
                    tradeOrderDto.getAmount()
            );

            try {
                capTradeOrderRepository.insert(tradeOrder);

                //扣除用户钱包
                CapitalAccount transferFromAccount = capitalAccountRepository.findByUserId(tradeOrderDto.getSelfUserId());

                transferFromAccount.transferFrom(tradeOrderDto.getAmount());

                capitalAccountRepository.save(transferFromAccount);
            } catch (DataIntegrityViolationException e) {
                //this exception may happen when insert trade order concurrently, if happened, ignore this insert operation.
            }
        }

        return "success";
    }

    @Override
    @Transactional
    public void confirmRecord(CapitalTradeOrderDto tradeOrderDto, TccTransactionContext context) {

        CapTradeOrder tradeOrder = capTradeOrderRepository.findByMerchantOrderNo(tradeOrderDto.getMerchantOrderNo());

        //check if the trade order status is DRAFT, if yes, return directly, ensure idempotency.
        if (null != tradeOrder && "DRAFT".equals(tradeOrder.getStatus())) {
            tradeOrder.confirm();
            capTradeOrderRepository.update(tradeOrder);

            CapitalAccount transferToAccount = capitalAccountRepository.findByUserId(tradeOrderDto.getOppositeUserId());

            transferToAccount.transferTo(tradeOrderDto.getAmount());

            try {
                capitalAccountRepository.save(transferToAccount);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    @Transactional
    public void cancelRecord(CapitalTradeOrderDto tradeOrderDto, TccTransactionContext context) throws Exception {

        try {
            Thread.sleep(1000l);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        CapTradeOrder tradeOrder = capTradeOrderRepository.findByMerchantOrderNo(tradeOrderDto.getMerchantOrderNo());

        //check if the trade order status is DRAFT, if yes, return directly, ensure idempotency.
        if (null != tradeOrder && "DRAFT".equals(tradeOrder.getStatus())) {
            tradeOrder.cancel();
            capTradeOrderRepository.update(tradeOrder);

            CapitalAccount capitalAccount = capitalAccountRepository.findByUserId(tradeOrderDto.getSelfUserId());

            capitalAccount.cancelTransfer(tradeOrderDto.getAmount());

            capitalAccountRepository.save(capitalAccount);
        }
    }

    //@TccTransaction(confirmMethod = "testTcc1", cancelMethod = "testTcc2")
    public void testTcc(TccTransactionContext tccTransactionContext){
        System.out.println(capitalAccountRepository == null);
    }

    public void testTcc1(TccTransactionContext tccTransactionContext){
       System.out.println(capitalAccountRepository == null);
    }

    public void testTcc2(TccTransactionContext tccTransactionContext){
        System.out.println("testTcc2");
    }

}
