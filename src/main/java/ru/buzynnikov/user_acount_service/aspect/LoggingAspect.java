package ru.buzynnikov.user_acount_service.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.buzynnikov.user_acount_service.exceptions.*;

import java.util.Arrays;
import java.util.List;

@Component
@Aspect
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("@annotation(TransferLog)")
    public Object transferLogger(ProceedingJoinPoint jp) throws Throwable {
        Object[] args = jp.getArgs();
        logger.info("Перевод со счета пользователя {} на счет пользователя {}. Сумма перевода:  {}", args[0], args[1], args[2]);
        return jp.proceed();
    }
    @Around("@annotation(ErrorLog)")
    public Object errorLogger(ProceedingJoinPoint jp) throws Throwable {
        Object[]  args = jp.getArgs();
        if(args[0] instanceof EmailAlreadyExistException exception){
            logger.error("Email уже зарегистрирован: {}", args[0]);
        }
        if(args[0] instanceof EmailNotFoundException exception){
            logger.error("Email не найден: {}", args[0]);
        }
        if(args[0] instanceof LimitEmailException exception){
            logger.error("Попытка удалить последний Email пользователя: {}", args[0]);
        }
        if(args[0] instanceof LimitPhoneException exception){
            logger.error("Попытка удалить последний номер телефона пользователя: {}", args[0]);
        }
        if(args[0] instanceof NotAuthenticatedException exception){
            logger.error("Не авторизован: {}", args[0]);
        }
        if(args[0] instanceof PhoneAlreadyExistsException exception){
            logger.error("Номер телефона уже зарегистрирован: {}", args[0]);
        }
        if(args[0] instanceof NotEnoughMoneyException exception){
            logger.error("Недостаточно денег: {}", args[0]);
        }
        if(args[0] instanceof PhoneNotFoundException exception){
            logger.error("Номер телефона не найден: {}", args[0]);
        }
        if(args[0] instanceof UserNotFoundException exception){
            logger.error("Имя пользователя не найдено: {}", args[0]);
        }
        if(args[0] instanceof HttpMessageNotReadableException exception){
            logger.error("Не верное тело запроса: {}", exception.getMessage());
        }
        if(args[0] instanceof MethodArgumentNotValidException exception){
            StringBuilder stringBuilder = new StringBuilder();
            for(var err : exception.getBindingResult().getAllErrors()){
                stringBuilder.append(err.getDefaultMessage()).append(", ");
            }
            logger.error("Не данные запроса: {}", stringBuilder.toString());
        }
        return jp.proceed();
    }

    @Around("@annotation(CreateLog)")
    public Object createLogger(ProceedingJoinPoint jp) throws Throwable {
        String methodName = jp.getSignature().getName();
        Object[] args = jp.getArgs();
        if(methodName.equals("addEmail")){
            logger.info("Добавление email: {} пользователю: {}", args[0], args[1]);
        }
        if(methodName.equals("addPhone")){
            logger.info("Добавление номера телефона: {} пользователю: {}", args[0], args[1]);
        }
        return jp.proceed();
    }

    @Around("@annotation(UpdateLog)")
    public Object updateLogger(ProceedingJoinPoint jp) throws Throwable {
        String methodName = jp.getSignature().getName();
        Object[] args = jp.getArgs();
        if(methodName.equals("removePhone")){
            logger.info("Удаление номера телефона: {} пользователю: {}", args[0], args[1]);
        }
        if(methodName.equals("updatePhone")){
            logger.info("Обновление номера телефона: {} пользователю: {}", args[0], args[1]);
        }
        if(methodName.equals("updateDateOfBirth")){
            logger.info("Обновление даты рождения пользователя: {}, новая дата рождения: {}", args[0], args[1]);
        }
        if(methodName.equals("updateName")){
            logger.info("Обновление имени пользователя: {}, новое имя: {}", args[0], args[1]);
        }
        if(methodName.equals("updateEmail")){
            logger.info("Обновление email пользователя: {}, новый email: {}", args[0], args[1]);
        }

        if(methodName.equals("deleteEmail")){
            logger.info("Удаление email: {} пользователю: {}", args[0], args[1]);
        }

        return jp.proceed();
    }


}
