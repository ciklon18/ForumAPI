package utils;


import com.user.api.dto.RegistrationRequestDto;
import com.user.core.entity.User;

import java.time.LocalDateTime;
import java.util.UUID;

public class DataUtils {

    public static RegistrationRequestDto getIvanIvanovTransient(){
        return new RegistrationRequestDto(
                "email@mail.ru",
                "password123",
                "ivan_ivanov",
                "ivan",
                "ivanov",
                "89085784781"
        );
    }

    public static User getConfirmedIvanIvanovPersisted(){
        return new User(
                UUID.randomUUID(),
                "email@mail.ru",
                "ivan_ivanov",
                "$2a$10$CQvDUJHtaddDvTxSKZt/cOxCyFs.NMVOprr37SlXbv7FOHEdLwgmy",
                "ivan",
                "ivanov",
                "89085784781",
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                null,
                true
        );
    }
    public static User getUnconfirmedIvanIvanovPersisted(){
        return new User(
                UUID.randomUUID(),
                "email@mail.ru",
                "ivan_ivanov",
                "$2a$10$CQvDUJHtaddDvTxSKZt/cOxCyFs.NMVOprr37SlXbv7FOHEdLwgmy",
                "ivan",
                "ivanov",
                "89085784781",
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                null,
                false
        );
    }
}
