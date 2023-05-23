//package com.example.onlineStore;
//
//import com.example.onlineStore.entity.User;
//import com.example.onlineStore.repository.UserRepository;
//import com.example.onlineStore.service.UserService;
//import com.example.onlineStore.service.impl.DefaultEmailService;
//import com.example.onlineStore.service.impl.UserServiceImpl;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.*;
//
//@RunWith(MockitoJUnitRunner.class)
//public class tokenTest {
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private DefaultEmailService defaultEmailService;
//
//    @InjectMocks
//    private UserServiceImpl userService;
//
//    @Test
//    public void testToken() {
//        // Arrange
//        User user = new User();
//        user.setEmail("user@gmail.com");
//        user.setId(1L);
//        Mockito.when(this.userRepository.findByEmail("user@gmail.com")).thenReturn(user);
//        doNothing().when(defaultEmailService).sendSimpleEmail(anyString(), anyString(), anyString());
//
//        // Act
//        String result = ("user@gmail.com");
//
//        // Assert
//        verify(userRepository).findByEmail("user@gmail.com");
//        verify(defaultEmailService).sendSimpleEmail("user@gmail.com", "Link for new password.", "http://localhost:8082/user/newPassword/1/" + user.getToken());
//        assertNotNull(user.getToken());
//        assertNotNull(user.getTokenExpiry());
//        assertEquals("Ссылка на изменение пароля была отправлена на адрес: user@gmail.com", result);
//    }
//
//}
