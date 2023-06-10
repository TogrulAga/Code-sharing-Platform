package platform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CodeService {
    @Autowired
    private CodeRepository codeRepository;

    public void saveCode(Code code) {
        if (code.getLongId() == null) {
            code.setLongId(codeRepository.count() + 1);
        }
        codeRepository.save(code);
    }

    public Code getCode(UUID id) {
        return codeRepository.findById(id).orElse(null);
    }

    public List<Code> getLatest() {
        return codeRepository.findTop10DoesNotExpire();
    }

    public void updateViews(Code code) {
        if (code.isExpiresByViews() && !code.isExpired()) {
            if (code.getViews() > 1) {
                code.setViews(code.getViews() - 1);
            } else {
                code.setViews(0);
                code.setExpired(true);
            }
            codeRepository.save(code);
        }
    }

    public void updateTime(Code code) {
        if (code.isExpiresByTime() && !code.isExpired()) {
            LocalDateTime currentTime = LocalDateTime.now();

            long timeLeft = Duration.between(currentTime, code.getEndDate()).getSeconds();

            if (timeLeft > 0) {
                code.setTime(timeLeft);
            } else {
                code.setTime(0);
                code.setExpired(true);
            }
            codeRepository.save(code);
        }
    }
}
