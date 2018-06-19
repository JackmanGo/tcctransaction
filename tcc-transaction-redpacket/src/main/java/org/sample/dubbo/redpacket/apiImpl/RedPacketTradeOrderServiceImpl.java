package org.sample.dubbo.redpacket.apiImpl;

import org.sample.dubbo.redpacket.api.RedPacketTradeOrderService;
import org.sample.dubbo.redpacket.api.dto.RedPacketTradeOrderDto;
import org.sample.dubbo.redpacket.entity.RedPacketAccount;
import org.sample.dubbo.redpacket.entity.RedTradeOrder;
import org.sample.dubbo.redpacket.repository.RedPacketAccountRepository;
import org.sample.dubbo.redpacket.repository.RedTradeOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("redPacketTradeOrderService")
public class RedPacketTradeOrderServiceImpl implements RedPacketTradeOrderService {

    @Autowired
    RedPacketAccountRepository redPacketAccountRepository;

    @Autowired
    RedTradeOrderRepository redTradeOrderRepository;

    @Override
    @Transactional
    public String record(RedPacketTradeOrderDto tradeOrderDto) throws RuntimeException{

        try {
            Thread.sleep(1000l);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        RedTradeOrder foundTradeOrder = redTradeOrderRepository.findByMerchantOrderNo(tradeOrderDto.getMerchantOrderNo());

        //check if the trade order has need recorded.
        //if record, then this method call return success directly.
        if (foundTradeOrder == null) {

            RedTradeOrder tradeOrder = new RedTradeOrder(
                    tradeOrderDto.getSelfUserId(),
                    tradeOrderDto.getOppositeUserId(),
                    tradeOrderDto.getMerchantOrderNo(),
                    tradeOrderDto.getAmount()
            );

            try {
                redTradeOrderRepository.insert(tradeOrder);

                RedPacketAccount transferFromAccount = redPacketAccountRepository.findByUserId(tradeOrderDto.getSelfUserId());

                transferFromAccount.transferFrom(tradeOrderDto.getAmount());

                redPacketAccountRepository.save(transferFromAccount);
            } catch (DataIntegrityViolationException e) {

            }
        }

        return "success";
    }

    @Transactional
    public void confirmRecord(RedPacketTradeOrderDto tradeOrderDto) {

        try {
            Thread.sleep(1000l);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        RedTradeOrder tradeOrder = redTradeOrderRepository.findByMerchantOrderNo(tradeOrderDto.getMerchantOrderNo());

        if (null != tradeOrder && "DRAFT".equals(tradeOrder.getStatus())) {
            tradeOrder.confirm();
            redTradeOrderRepository.update(tradeOrder);

            RedPacketAccount transferToAccount = redPacketAccountRepository.findByUserId(tradeOrderDto.getOppositeUserId());

            transferToAccount.transferTo(tradeOrderDto.getAmount());

            redPacketAccountRepository.save(transferToAccount);
        }
    }

    @Transactional
    public void cancelRecord(RedPacketTradeOrderDto tradeOrderDto) {

        try {
            Thread.sleep(1000l);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        RedTradeOrder tradeOrder = redTradeOrderRepository.findByMerchantOrderNo(tradeOrderDto.getMerchantOrderNo());

        if (null != tradeOrder && "DRAFT".equals(tradeOrder.getStatus())) {
            tradeOrder.cancel();
            redTradeOrderRepository.update(tradeOrder);

            RedPacketAccount capitalAccount = redPacketAccountRepository.findByUserId(tradeOrderDto.getSelfUserId());

            capitalAccount.cancelTransfer(tradeOrderDto.getAmount());

            redPacketAccountRepository.save(capitalAccount);
        }
    }
}
