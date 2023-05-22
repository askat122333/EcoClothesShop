package com.example.onlineStore.service.impl;

import com.example.onlineStore.dto.PaymentCardDto;
import com.example.onlineStore.entity.PaymentCard;
import com.example.onlineStore.exceptions.PaymentCardNotFoundException;
import com.example.onlineStore.repository.PaymentCardRepository;
import com.example.onlineStore.service.PaymentCardService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PaymentCardServiceImpl implements PaymentCardService {
    private final PaymentCardRepository paymentCardRepository;
    private PaymentCardDto mapToDto (PaymentCard paymentCard){
        return new PaymentCardDto(
                paymentCard.getId(),
                paymentCard.getCardNum(),
                paymentCard.getBalance(),
                paymentCard.getRdt(),
                paymentCard.getUser()
        );
    }
    @Override
    public PaymentCardDto getById(Long id) throws PaymentCardNotFoundException {
        try {
            PaymentCard paymentCard = paymentCardRepository.findByIdAndRdtIsNull(id);
            return mapToDto(paymentCard);
        }catch (NullPointerException e){
            throw new PaymentCardNotFoundException("Карта с id "+id+" не найдена.");
        }

    }

    @Override
    public PaymentCard getByIdEntity(Long id) {
        return paymentCardRepository.findByIdAndRdtIsNull(id);
    }

    @Override
    public List<PaymentCardDto> getAll() throws PaymentCardNotFoundException {
        List<PaymentCard> paymentCardList = paymentCardRepository.findAllByRdtIsNull();
        if(paymentCardList.isEmpty()){
            throw new PaymentCardNotFoundException("В базе нет платежных карт.");
        }
        List<PaymentCardDto> paymentCardDtoList = new ArrayList<>();
        for (PaymentCard paymentCard:paymentCardList) {
            paymentCardDtoList.add(mapToDto(paymentCard));
        }
        return paymentCardDtoList;
    }

    @Override
    public PaymentCardDto create(PaymentCard paymentCard) {
        return mapToDto(paymentCardRepository.save(paymentCard));
    }

    @Override
    public PaymentCardDto update(Long id, PaymentCardDto dto) throws PaymentCardNotFoundException {
        PaymentCard paymentCard = getByIdEntity(id);
        if(paymentCard==null){
            throw new PaymentCardNotFoundException("Карта с id "+id+" не найдена.");
        }
        if (dto.getCardNum() != null) {
            paymentCard.setCardNum(dto.getCardNum());
        }
        if (dto.getBalance() != null) {
            paymentCard.setBalance(dto.getBalance());
        }
        if (dto.getUser() != null) {
            paymentCard.setUser(dto.getUser());
        }

        return mapToDto(paymentCard);

    }

    @Override
    public String deleteById(Long id) throws PaymentCardNotFoundException {
        try {
            PaymentCard paymentCard = getByIdEntity(id);
            paymentCard.setRdt(LocalDate.now());
            paymentCardRepository.save(paymentCard);
            return "Платежная карта с id: "+id+" была удалена.";
        }catch (NullPointerException e){
            throw new PaymentCardNotFoundException("Карта с id "+id+" не найдена.");
        }


    }
}
