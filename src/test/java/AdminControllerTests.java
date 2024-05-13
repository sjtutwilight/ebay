import com.example.ebay.controller.AdminController;
import com.example.ebay.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.anyMap;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class AdminControllerTests {

    @Mock
    private UserService userService;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void addUserAccess_WhenCalledWithValidData_ReturnsSuccess() {
        when(userService.addUserAccess(anyMap())).thenReturn(true);
        ResponseEntity<String> response = adminController.addUserAccess(Map.of("userId", 123456, "endpoint", List.of("resource A")),"admin");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Access granted successfully.", response.getBody());
    }

    @Test
    void addUserAccess_WhenCalledWithInvalidData_ReturnsError() {
        when(userService.addUserAccess(anyMap())).thenReturn(false);
        ResponseEntity<String> response = adminController.addUserAccess(Map.of("userId", 123456, "endpoint", List.of("resource A")),"admin");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Failed to grant access.", response.getBody());
    }
    @Test
    void addUserAccess_WhenCalledWithInValidUser_ReturnsError() {
        when(userService.addUserAccess(anyMap())).thenReturn(true);
        ResponseEntity<String> response = adminController.addUserAccess(Map.of("userId", 123456, "endpoint", List.of("resource A")),"user");

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("no authority for common users.", response.getBody());
    }
}
