package se.sprinta.headhunterbackend.ad;

import org.springframework.web.bind.annotation.*;
import se.sprinta.headhunterbackend.ad.converter.AdDtoFormToAdConverter;
import se.sprinta.headhunterbackend.ad.converter.AdToAdDtoView;
import se.sprinta.headhunterbackend.ad.dto.AdDtoForm;
import se.sprinta.headhunterbackend.ad.dto.AdDtoView;
import se.sprinta.headhunterbackend.system.Result;
import se.sprinta.headhunterbackend.system.StatusCode;
import se.sprinta.headhunterbackend.account.Account;
import se.sprinta.headhunterbackend.account.converter.AccountToAccountDtoViewConverter;
import se.sprinta.headhunterbackend.account.dto.AccountDtoView;

import java.util.List;

/**
 * Backend API endpoints for Ad.
 */

@RestController
@RequestMapping("${api.endpoint.base-url-ad}")
@CrossOrigin("http://localhost:5173")
public class AdController {

    private final AdService adService;
    private final AccountToAccountDtoViewConverter accountToAccountDtoViewConverter;
    private final AdDtoFormToAdConverter adDtoFormToAdConverter;
    private final AdToAdDtoView adToAdDtoView;

    public AdController(AdService adService, AccountToAccountDtoViewConverter accountToAccountDtoViewConverter, AdDtoFormToAdConverter adDtoFormToAdConverter, AdToAdDtoView adToAdDtoView) {
        this.adService = adService;
        this.accountToAccountDtoViewConverter = accountToAccountDtoViewConverter;
        this.adDtoFormToAdConverter = adDtoFormToAdConverter;
        this.adToAdDtoView = adToAdDtoView;
    }

    @GetMapping("/findAll")
    public Result findAll() {
        List<Ad> allAds = this.adService.findAll();
        return new Result(true, StatusCode.SUCCESS, "Find All Ads Success", allAds);
    }

    @GetMapping("/findById/{adId}")
    public Result findById(@PathVariable String adId) {
        Ad foundAd = this.adService.findById(adId);
        AdDtoView foundAdDtoView = this.adToAdDtoView.convert(foundAd);
        return new Result(true, StatusCode.SUCCESS, "Find Ad Success", foundAdDtoView);
    }

    /**
     * Searches for job and returns array of ads that is associated to it.
     * Relationship: [Ad] *...1 [Job]
     *
     * @param jobId The id of the Job object that holds all the ads the user are looking for.
     */

    @GetMapping("/findAllAdsByJobId/{jobId}")
    public Result findAllAdsByJobId(@PathVariable Long jobId) {
        List<Ad> foundAds = this.adService.getAdsByJobId(jobId);
        List<AdDtoView> foundAdDtoViews = foundAds.stream().map(this.adToAdDtoView::convert).toList();
        return new Result(true, StatusCode.SUCCESS, "Find All Ads By Job Id Success", foundAdDtoViews);
    }

    @GetMapping("/getNumberOfAds/{jobId}")
    public Result getNumberOfAds(@PathVariable Long jobId) {
        Long numberOfAds = this.adService.getNumberOfAds(jobId);
        return new Result(true, StatusCode.SUCCESS, "Get Number of Ads Success", numberOfAds);
    }

    /**
     * Searches for ad, look at the job associated to it and returns the user that owns that job.
     * Relationship: [Ad] *...1 [Job] *...1 [User]
     */

    @GetMapping("/findUserByAdId/{adId}")
    public Result findUserByAdId(@PathVariable String adId) {
        Account foundAccount = this.adService.getAccountByAdId(adId);
        AccountDtoView foundAccountDtoView = this.accountToAccountDtoViewConverter.convert(foundAccount);
        return new Result(true, StatusCode.SUCCESS, "Find User By Ad Id Success", foundAccountDtoView);
    }

    @PostMapping("/saveAd/{jobId}")
    public Result saveAd(@PathVariable Long jobId, @RequestBody AdDtoForm adDtoForm) {
        Ad savedAd = this.adService.addAd(jobId, adDtoForm);
        AdDtoView savedAdDtoView = this.adToAdDtoView.convert(savedAd);
        return new Result(true, StatusCode.SUCCESS, "Save Ad Success", savedAdDtoView);
    }

    @DeleteMapping("/deleteAd/{adId}")
    public Result deleteAd(@PathVariable String adId) {
        this.adService.delete(adId);
        return new Result(true, StatusCode.SUCCESS, "Ad Delete Success");
    }
}
