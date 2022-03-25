package account.Security;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

@Component
public class UserAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        if (authException instanceof LockedException) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            try (PrintWriter writer = response.getWriter()) {
                writer.print("{\n" +
                        "  \"timestamp\": \"" + LocalDateTime.now() + "\",\n" +
                        "  \"status\": " + HttpServletResponse.SC_UNAUTHORIZED + ",\n" +
                        "  \"error\": \"Unauthorized\",\n" +
                        "  \"message\": \"User account is locked\",\n" +
                        "  \"path\": \"" + request.getServletPath() + "\"\n" +
                        "}");
            }
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
        }
    }

}
