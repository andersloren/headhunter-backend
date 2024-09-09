package se.sprinta.headhunterbackend.ad;

import org.springframework.web.bind.annotation.*;
import se.sprinta.headhunterbackend.account.dto.AccountDtoView;
import se.sprinta.headhunterbackend.ad.converter.AdToAdDtoView;
import se.sprinta.headhunterbackend.ad.dto.AdDtoForm;
import se.sprinta.headhunterbackend.ad.dto.AdDtoView;
import se.sprinta.headhunterbackend.system.Result;
import se.sprinta.headhunterbackend.system.StatusCode;

import java.util.List;

/**
 * Backend API endpoints for Ad.
 */

@RestController
@RequestMapping("${api.endpoint.base-url-ad}")
@CrossOrigin("http://localhost:5173")
public class AdController {

  private final AdService adService;
  private final AdToAdDtoView adToAdDtoView;

  public AdController(AdService adService, AdToAdDtoView adToAdDtoView) {
    this.adService = adService;
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
    return new Result(true, StatusCode.SUCCESS, "Find Ad Success", foundAd);
  }

  @GetMapping("/getAdDtosByJobId/{jobId}")
  public Result getAdDtosByJobId(@PathVariable long jobId) {
    List<AdDtoView> foundAdDtos = this.adService.getAdDtosByJobId(jobId);
    return new Result(true, StatusCode.SUCCESS, "Get Ad Dtos by Job Id Success", foundAdDtos);
  }

  @GetMapping("/getNumberOfAdsByJobId/{jobId}")
  public Result getNumberOfAds(@PathVariable Long jobId) {
    Long numberOfAds = this.adService.getNumberOfAdsByJobId(jobId);
    return new Result(true, StatusCode.SUCCESS, "Get Number Of Ads by Job Id Success", numberOfAds);
  }

  /**
   * Searches for ad, look at the job associated to it and returns the user that
   * owns that job.
   * Relationship: [Ad] *...1 [Job] *...1 [User]
   */

  @GetMapping("/getAccountDtoByAdId/{adId}")
  public Result findUserByAdId(@PathVariable String adId) {
    AccountDtoView foundAccountDto = this.adService.getAccountDtoByAdId(adId);
    return new Result(true, StatusCode.SUCCESS, "Get Account Dto By Ad Id Success", foundAccountDto);
  }

  @PostMapping("/addAd/{jobId}")
  public Result saveAd(@PathVariable Long jobId, @RequestBody AdDtoForm adDtoForm) {
    Ad savedAd = this.adService.addAd(jobId, adDtoForm);
    AdDtoView savedAdDtoView = this.adToAdDtoView.convert(savedAd);
    return new Result(true, StatusCode.SUCCESS, "Save Ad Success", savedAdDtoView);
  }

  @DeleteMapping("/delete/{adId}")
  public Result deleteAd(@PathVariable String adId) {
    this.adService.delete(adId);
    return new Result(true, StatusCode.SUCCESS, "Delete Ad Success");
  }
}
