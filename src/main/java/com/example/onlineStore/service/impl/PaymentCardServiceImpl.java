package com.example.onlineStore.service.impl;

import com.example.onlineStore.dto.DtoForBalance;
import com.example.onlineStore.dto.PaymentCardDto;
import com.example.onlineStore.entity.PaymentCard;
import com.example.onlineStore.entity.User;
import com.example.onlineStore.exceptions.PaymentCardNotFoundException;
import com.example.onlineStore.exceptions.UserNotFoundException;
import com.example.onlineStore.exceptions.ValidException;
import com.example.onlineStore.repository.PaymentCardRepository;
import com.example.onlineStore.repository.UserRepository;
import com.example.onlineStore.service.PaymentCardService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class PaymentCardServiceImpl implements PaymentCardService {
    private final PaymentCardRepository paymentCardRepository;
    private final UserRepository userRepository;
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
            log.error("Метод getById(PaymentCard), Exception: Карта с id "+id+" не найдена.");
            throw new PaymentCardNotFoundException("Карта с id "+id+" не найдена.");
        }

    }

    @Override
    public DtoForBalance getUserBalance(Long userId) throws PaymentCardNotFoundException {
        try {
            PaymentCard paymentCard = paymentCardRepository.findByUserAndRdtIsNull(userId);
            return DtoForBalance.builder()
                    .balance(paymentCard.getBalance())
                    .cardNum(paymentCard.getCardNum().substring(8))
                    .user(paymentCard.getUser().getId())
                    .build();
        }catch (NullPointerException e){
            log.error("Метод getById(PaymentCard), Exception: Карта с user_id "+userId+" не найдена.");
            throw new PaymentCardNotFoundException("Карта с user_id "+userId+" не найдена.");
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
            log.error("Метод getAll(PaymentCard), Exception: В базе нет платежных карт.");
            throw new PaymentCardNotFoundException("В базе нет платежных карт.");
        }
        List<PaymentCardDto> paymentCardDtoList = new ArrayList<>();
        for (PaymentCard paymentCard:paymentCardList) {
            paymentCardDtoList.add(mapToDto(paymentCard));
        }
        return paymentCardDtoList;
    }
    @Transactional
    @Override
    public String create(@Valid PaymentCardDto dto, Long userId) throws UserNotFoundException {
        User user = userRepository.findByIdAndRdtIsNull(userId);
        if (user == null) {
            log.error("Метод create(paymentCard), Exception : Пользователь с id "+userId+" не найден");
            throw new UserNotFoundException("Пользователь с id "+userId+" не найден");
        } else if (user.getPaymentCard() != null) {
            return "У вас уже есть платежная карта.";
        }else {
            PaymentCard paymentCard = PaymentCard.builder()
                    .user(user)
                    .cardNum(dto.getCardNum())
                    .balance(dto.getBalance())
                    .build();


            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<PaymentCard>> violations = validator.validate(paymentCard);

            if (!violations.isEmpty()) {

                List<String> errorMessages = new ArrayList<>();
                for (ConstraintViolation<PaymentCard> violation : violations) {
                    errorMessages.add("Поле: " + violation.getPropertyPath() + " - " + violation.getMessage());
                    log.warn("Метод create(PaymentCard): " + errorMessages);
                }
                throw new ValidException(errorMessages);
            }
            user.setPaymentCard(paymentCard);
            paymentCardRepository.save(paymentCard);
            return "Платежная карта добавлена.";
        }
    }

    @Transactional
    @Override
    public PaymentCardDto update(Long id,@Valid PaymentCardDto dto) throws PaymentCardNotFoundException {
        PaymentCard paymentCard = getByIdEntity(id);
        if(paymentCard==null){
            log.error("Метод getAll(PaymentCard), Exception: Карта с id "+id+" не найдена.");
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
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<PaymentCard>> violations = validator.validate(paymentCard);

        if (!violations.isEmpty()) {

            List<String> errorMessages = new ArrayList<>();
            for (ConstraintViolation<PaymentCard> violation : violations) {
                errorMessages.add("Поле: " + violation.getPropertyPath() + " - " + violation.getMessage());
                log.warn("Метод update(PaymentCard): "+errorMessages);
            }
            throw new ValidException(errorMessages);
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
            log.error("Метод deleteById(PaymentCard), Exception: Карта с id "+id+" не найдена.");
            throw new PaymentCardNotFoundException("Карта с id "+id+" не найдена.");
        }


    }
}
