package account.Controller;

import account.Model.Event;
import account.Repo.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/security/")
public class AuditorController {

    @Autowired
    EventRepository eventRepository;

    @GetMapping("events")
    public List<Event> showAllEvents(){
        return (List<Event>) eventRepository.findAll();
    }
}
