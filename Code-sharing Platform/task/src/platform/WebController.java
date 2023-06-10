package platform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.UUID;


@Controller
public class WebController {
    @Autowired
    private CodeService service;

    @GetMapping("/code/latest")
    @ResponseBody
    public ModelAndView getLatest(ModelAndView modelAndView) {
        modelAndView.setViewName("latest");
        modelAndView.addObject("codes", service.getLatest());
        return modelAndView;
    }

    @GetMapping("/code/{id}")
    public ModelAndView getCodeWithId(ModelAndView modelAndView, @PathVariable("id") UUID id) {
        Code code = service.getCode(id);
        if (code == null || code.isExpired()) {
            modelAndView.setStatus(HttpStatus.NOT_FOUND);
            modelAndView.setViewName("404");
            return modelAndView;
        }


        service.updateTime(code);

        if (code.isExpired()) {
            modelAndView.setStatus(HttpStatus.NOT_FOUND);
            modelAndView.setViewName("404");
            return modelAndView;
        }
        service.updateViews(code);

        modelAndView.setViewName("code");
        modelAndView.addObject("codes", code);
        return modelAndView;
    }

    @GetMapping("/code/new")
    public ModelAndView getNew(ModelAndView modelAndView) {
        modelAndView.setViewName("new");

        return modelAndView;
    }

    @PostMapping ModelAndView postNew(ModelAndView modelAndView,
                                      @RequestBody String code,
                                      @RequestBody long time,
                                      @RequestBody long views) {
        modelAndView.setViewName("code");
        Code code1 = new Code(code, LocalDateTime.now(), time, views);
        service.saveCode(code1);

        modelAndView.addObject("codes", code1);

        return modelAndView;
    }
}
