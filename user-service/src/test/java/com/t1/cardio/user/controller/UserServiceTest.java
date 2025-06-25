package com.t1.cardio.user.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.t1.cardio.user.model.AppUser;
import com.t1.cardio.user.model.UserDTO;
import com.t1.cardio.user.rest.CardRestClient;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private CardRestClient cardRestClient;
    
    @InjectMocks
    private UserService userService;
    
    private AppUser testUser;
    private UserDTO testUserDTO;
    
    @BeforeEach
    void setUp() {
        testUser = new AppUser("testuser", "test@example.com", "password", 500.0);
        // Simuler un ID auto-généré
        try {
            var field = AppUser.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(testUser, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        testUserDTO = new UserDTO("testuser", "test@example.com", "password");
    }
    
    @Test
    void testGetUserById_UserExists() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        
        // Act
        AppUser result = userService.getUserById(1);
        
        // Assert
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository).findById(1);
    }
    
    @Test
    void testGetUserById_UserDoesNotExist() {
        // Arrange
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());
        
        // Act
        AppUser result = userService.getUserById(999);
        
        // Assert
        assertNull(result);
        verify(userRepository).findById(999);
    }
    
    @Test
    void testCreateUser_ValidUser() {
        // Arrange
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(AppUser.class))).thenReturn(testUser);
        doNothing().when(cardRestClient).giveCardToUser(anyInt());
        
        // Act
        AppUser result = userService.createUser(testUserDTO);
        
        // Assert
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        assertEquals(500.0, result.getFunds());
        verify(userRepository).save(any(AppUser.class));
        verify(cardRestClient).giveCardToUser(anyInt());
    }
    
    @Test
    void testCreateUser_UsernameTaken() {
        // Arrange
        when(userRepository.existsByUsername("testuser")).thenReturn(true);
        
        // Act
        AppUser result = userService.createUser(testUserDTO);
        
        // Assert
        assertNull(result);
        verify(userRepository, never()).save(any(AppUser.class));
        verify(cardRestClient, never()).giveCardToUser(anyInt());
    }
    
    @Test
    void testCreateUser_InvalidUser() {
        // Arrange
        UserDTO invalidUserDTO = new UserDTO("", "", "");
        
        // Act
        AppUser result = userService.createUser(invalidUserDTO);
        
        // Assert
        assertNull(result);
        verify(userRepository, never()).save(any(AppUser.class));
        verify(cardRestClient, never()).giveCardToUser(anyInt());
    }
    
    @Test
    void testGetAllUsers() {
        // Arrange
        AppUser user1 = new AppUser("user1", "user1@example.com", "password1", 500.0);
        AppUser user2 = new AppUser("user2", "user2@example.com", "password2", 500.0);
        List<AppUser> userList = Arrays.asList(user1, user2);
        
        when(userRepository.findAll()).thenReturn(userList);
        
        // Act
        List<AppUser> result = userService.getAllUser();
        
        // Assert
        assertEquals(2, result.size());
        verify(userRepository).findAll();
    }
    
    @Test
    void testDeleteUser_UserExists() {
        // Arrange
        when(userRepository.existsById(1)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1);
        
        // Act
        boolean result = userService.deleteUser(1);
        
        // Assert
        assertTrue(result);
        verify(userRepository).deleteById(1);
    }
    
    @Test
    void testDeleteUser_UserDoesNotExist() {
        // Arrange
        when(userRepository.existsById(999)).thenReturn(false);
        
        // Act
        boolean result = userService.deleteUser(999);
        
        // Assert
        assertFalse(result);
        verify(userRepository, never()).deleteById(anyInt());
    }
    
    @Test
    void testUpdateUserFund_Success() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(AppUser.class))).thenReturn(testUser);
        
        // Act
        boolean result = userService.updateUserFund(1, 1000.0);
        
        // Assert
        assertTrue(result);
        assertEquals(1000.0, testUser.getFunds());
        verify(userRepository).save(testUser);
    }
    
    @Test
    void testUpdateUserFund_NegativeFund() {
        // Act
        boolean result = userService.updateUserFund(1, -100.0);
        
        // Assert
        assertFalse(result);
        verify(userRepository, never()).save(any(AppUser.class));
    }
    
    @Test
    void testUpdateUserFund_UserNotFound() {
        // Arrange
        when(userRepository.findById(999)).thenReturn(Optional.empty());
        
        // Act
        boolean result = userService.updateUserFund(999, 1000.0);
        
        // Assert
        assertFalse(result);
        verify(userRepository, never()).save(any(AppUser.class));
    }
    
    @Test
    void testGetUserId_ValidCredentials() {
        // Arrange
        when(userRepository.findByUsernameAndPassword("testuser", "password")).thenReturn(testUser);
        
        // Act
        Integer result = userService.getUserId("testuser", "password");
        
        // Assert
        assertEquals(1, result);
        verify(userRepository).findByUsernameAndPassword("testuser", "password");
    }
    
    @Test
    void testGetUserId_InvalidCredentials() {
        // Arrange
        when(userRepository.findByUsernameAndPassword("testuser", "wrongpassword")).thenReturn(null);
        
        // Act
        Integer result = userService.getUserId("testuser", "wrongpassword");
        
        // Assert
        assertNull(result);
        verify(userRepository).findByUsernameAndPassword("testuser", "wrongpassword");
    }
}
