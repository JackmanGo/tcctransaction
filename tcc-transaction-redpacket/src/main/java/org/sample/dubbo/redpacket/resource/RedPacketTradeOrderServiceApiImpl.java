package org.sample.dubbo.redpacket.resource;

import api.TccTransaction;
import api.TccTransactionContext;
import com.tcc.transaction.api.redpacket.RedPacketTradeOrderServiceApi;
import com.tcc.transaction.api.redpacket.dto.RedPacketTradeOrderDto;
import org.apache.dubbo.config.annotation.Service;
import org.sample.dubbo.redpacket.entity.RedPacketAccount;
import org.sample.dubbo.redpacket.entity.RedTradeOrder;
import org.sample.dubbo.redpacket.repository.RedPacketAccountRepository;
import org.sample.dubbo.redpacket.repository.RedTradeOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RedPacketTradeOrderServiceApiImpl implements RedPacketTradeOrderServiceApi {

    @Autowired
    RedPacketAccountRepository redPacketAccountRepository;

    @Autowired
    RedTradeOrderRepository redTradeOrderRepository;

    @Override
    @Transactional
    @TccTransaction(confirmMethod = "confirmRecord", cancelMethod = "cancelRecord")
    public String record(RedPacketTradeOrderDto tradeOrderDto, TccTransactionContext context) throws RuntimeException{

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

    @Override
    @Transactional
    public void confirmRecord(RedPacketTradeOrderDto tradeOrderDto, TccTransactionContext context) {

        RedTradeOrder tradeOrder = redTradeOrderRepository.findByMerchantOrderNo(tradeOrderDto.getMerchantOrderNo());

        if (null != tradeOrder && "DRAFT".equals(tradeOrder.getStatus())) {
            tradeOrder.confirm();
            redTradeOrderRepository.update(tradeOrder);

            RedPacketAccount transferToAccount = redPacketAccountRepository.findByUserId(tradeOrderDto.getOppositeUserId());

            transferToAccount.transferTo(tradeOrderDto.getAmount());

            redPacketAccountRepository.save(transferToAccount);
        }
    }

    @Override
    @Transactional
    public void cancelRecord(RedPacketTradeOrderDto tradeOrderDto, TccTransactionContext context) {

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
