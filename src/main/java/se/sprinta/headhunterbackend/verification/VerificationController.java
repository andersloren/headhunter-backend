package se.sprinta.headhunterbackend.verification;

import org.apache.juli.VerbatimFormatter;
import org.springframework.web.bind.annotation.*;
import se.sprinta.headhunterbackend.system.Result;
import se.sprinta.headhunterbackend.system.StatusCode;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("${api.endpoint.base-url-verification}")
// TODO: 10/3/2024 Might want to check these methods, in case some of them might not be used
@CrossOrigin(origins = "${CORS_ALLOWED_ORIGIN}"
        , methods = {
        RequestMethod.GET,
        RequestMethod.POST
})
public class VerificationController {

    private final VerificationService verificationService;

    public VerificationController(VerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @GetMapping("/findAll")
    public Result findAll() {
        List<Verification> foundVerifications = this.verificationService.findAll();
        return new Result(true, StatusCode.SUCCESS, "Find All Verifications Success", foundVerifications);
    }

    @PostMapping("/verifyRegistration/{email}/{verificationCode}")
    public Result verifyRegistration(@PathVariable String email, @PathVariable String verificationCode) throws IOException, URISyntaxException {
        this.verificationService.verifyRegistration(email, verificationCode);
        return new Result(true, StatusCode.SUCCESS, "Verification Success");
    }
}
