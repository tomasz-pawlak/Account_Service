package account.Services;

import account.Model.Event;
import account.Model.EventType;
import account.Model.User;
import account.Repo.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Locale;

@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;
    @Autowired
    HttpServletRequest request;


    public Event initEvent(User user) {
        return new Event(
                LocalDate.now(),
                EventType.CREATE_USER,
                "Anonymous",
                user.getEmail().toLowerCase(Locale.ROOT),
                request.getRequestURI()
        );
    }

    public void createUserEvent(User user) {
        Event event = initEvent(user);

        eventRepository.save(event);
    }

    public void changePasswordEvent(User user) {
        Event event = initEvent(user);

        event.setAction(EventType.CHANGE_PASSWORD);
        event.setSubject(user.getEmail());

        eventRepository.save(event);
    }

    public void grantRoleEvent(User user, String role) {
        Event event = initEvent(user);

        event.setAction(EventType.GRANT_ROLE);
        event.setSubject(request.getRemoteUser());
        event.setObject("Grant role " + role + " to " + user.getEmail());

        eventRepository.save(event);
    }

    public void removeRoleEvent(User user, String role) {
        Event event = initEvent(user);

        event.setAction(EventType.REMOVE_ROLE);
        event.setSubject(request.getRemoteUser());
        event.setObject("Remove role " + role + " from " + user.getEmail());

        eventRepository.save(event);
    }

    public void deleteUserEvent(User user) {
        Event event = initEvent(user);

        event.setAction(EventType.DELETE_USER);
        event.setSubject(request.getRemoteUser());
        event.setObject(user.getEmail());

        eventRepository.save(event);
    }


    public void failedLoginEvent(String email) {

        Event event = new Event(
                LocalDate.now(),
                EventType.LOGIN_FAILED,
                email,
                request.getRequestURI(),
                request.getRequestURI()
        );

        eventRepository.save(event);
    }

    public void lockUserEvent(User user) {
        Event event = initEvent(user);

        event.setAction(EventType.LOCK_USER);
        event.setSubject(user.getEmail());
        event.setObject("Lock user " + user.getEmail());

        eventRepository.save(event);
    }

    public void unlockUserEvent(User user) {
        Event event = initEvent(user);

        event.setAction(EventType.UNLOCK_USER);
//        event.setSubject(AccessController.getContext().toString());
        event.setSubject(request.getRemoteUser());
        event.setObject("Unlock user " + user.getEmail());

        eventRepository.save(event);
    }

    public void accessDeniedEvent(String email) {
        Event event = new Event(
                LocalDate.now(),
                EventType.ACCESS_DENIED,
                email,
                request.getRequestURI(),
                request.getRequestURI()
        );

        eventRepository.save(event);
    }

    public void bruteForceEvent(User user) {
        Event event = initEvent(user);

        event.setAction(EventType.BRUTE_FORCE);
        event.setSubject(user.getEmail());
        event.setObject(request.getRequestURI());

        eventRepository.save(event);
    }

}
