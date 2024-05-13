import com.example.ebay.controller.UserController;
import com.example.ebay.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class UserControllerTests {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void checkAccess_WhenUserHasAccess_ReturnsSuccess() {
        when(userService.checkUserAccess("resource A","123456")).thenReturn(true);
        ResponseEntity<String> response = userController.checkAccess("resource A","123456");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Access granted to resource: resource A", response.getBody());
    }

    @Test
    void checkAccess_WhenUserHasNoAccess_ReturnsForbidden() {
        when(userService.checkUserAccess("resource A","123456")).thenReturn(false);
        ResponseEntity<String> response = userController.checkAccess("resource A","123456");

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Access denied to resource: resource A", response.getBody());
    }
}
