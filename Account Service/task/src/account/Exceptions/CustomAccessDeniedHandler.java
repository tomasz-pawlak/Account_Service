package account.Exceptions;

import account.Services.EventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    EventService eventService;

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.info("Access denied");
        eventService.accessDeniedEvent(request.getRemoteUser());
        response.sendError(HttpStatus.FORBIDDEN.value(), "Access Denied!");
    }
}
