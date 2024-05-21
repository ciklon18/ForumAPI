package it;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.UserApplication;
import com.user.api.constant.ApiPaths;
import com.user.api.dto.RegistrationRequestDto;
import com.user.core.entity.User;
import com.user.core.entity.UserConfirmation;
import com.user.core.repository.UserConfirmationRepository;
import com.user.core.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import utils.DataUtils;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ActiveProfiles(value = "test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {UserApplication.class}
)
public class ItUserRestControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserConfirmationRepository userConfirmationRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        userConfirmationRepository.deleteAll();
    }

    @Test
    @DisplayName("Test user registration functionality")
    public void givenRegisterDto_whenSave_thenUserIsRegistered() throws Exception {
        // given
        RegistrationRequestDto dto = DataUtils.getIvanIvanovTransient();
        // when
        ResultActions result = mockMvc.perform(
                post(ApiPaths.REGISTER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        );
        // then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers
                                   .jsonPath("$.message")
                                   .value("User successfully registered")
                );

    }

    @Test
    @DisplayName("Test user registration with duplicated login functionality")
    public void givenRegisterDto_whenSave_thenUserIsAlreadyRegistered() throws Exception {
        // given
        User user = DataUtils.getConfirmedIvanIvanovPersisted();
        userRepository.save(user);
        RegistrationRequestDto dto = DataUtils.getIvanIvanovTransient();

        // when
        ResultActions result = mockMvc.perform(
                post(ApiPaths.REGISTER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        );
        // then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers
                                   .jsonPath("$.message")
                                   .value("Login is already used")
                );

    }

    @Test
    @DisplayName("Test confirm user registration by correct confirmation code functionality")
    public void givenConfirmationCode_whenConfirmById_thenUserIsSuccessfullyConfirmed() throws Exception {
        // given
        User user = userRepository.save( DataUtils.getUnconfirmedIvanIvanovPersisted());

        UserConfirmation userConfirmation = userConfirmationRepository.save(
                new UserConfirmation(user.getId())
        );
        // when
        ResultActions result = mockMvc.perform(
                get(ApiPaths.CONFIRMATION, userConfirmation.getConfirmationCode())
                        .contentType(MediaType.APPLICATION_JSON)
        );
        // then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers
                                   .jsonPath("$.userId")
                                   .value(user.getId().toString())
                )
                .andExpect(MockMvcResultMatchers
                                   .jsonPath("$.accessToken")
                                   .isNotEmpty()
                )
                .andExpect(MockMvcResultMatchers
                                   .jsonPath("$.refreshToken")
                                   .isNotEmpty()
                );
    }


    @Test
    @DisplayName("Test confirm user registration by wrong confirmation code functionality")
    public void givenWrongConfirmationCode_whenConfirmById_thenUserConfirmationIsFailed() throws Exception {
        // given
        UUID wrongConfirmationCode = UUID.randomUUID();
        // when
        ResultActions result = mockMvc.perform(
                get(ApiPaths.CONFIRMATION, wrongConfirmationCode)
                        .contentType(MediaType.APPLICATION_JSON)
        );
        // then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers
                                   .jsonPath("$.message")
                                   .value("Wrong confirmation code")
                );
    }
}
