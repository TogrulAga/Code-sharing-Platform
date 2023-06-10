package platform;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
public class APIController {
    @Autowired
    private CodeService service;

    @GetMapping("/api/code/latest")
    public List<Code> getLatest() {
        return service.getLatest();
    }

    @GetMapping("/api/code/{id}")
    public ResponseEntity<Code> getCodeWithId(@PathVariable("id") UUID id) {
        Code code = service.getCode(id);
        if (code == null || code.isExpired()) {
            return ResponseEntity.notFound().build();
        }

        service.updateTime(code);

        if (code.isExpired()) {
            return ResponseEntity.notFound().build();
        }
        service.updateViews(code);

        return ResponseEntity.ok(code);
    }

    @PostMapping("/api/code/new")
    public String postNew(@RequestBody String codeJson) {
        Code code;
        try {
            code = new ObjectMapper().readValue(codeJson, Code.class);
            code.setDate(LocalDateTime.now());

            code.setExpiresByTime(code.getTime() > 0);
            code.setExpiresByViews(code.getViews() > 0);
            code.setExpired(false);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        service.saveCode(code);
        return "{\"id\": \"%s\"}".formatted(code.getId());
    }
}
