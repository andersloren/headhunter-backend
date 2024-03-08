package se.sprinta.headhunterbackend.ad;

import org.springframework.web.bind.annotation.*;
import se.sprinta.headhunterbackend.ad.converter.AdDtoFormToAdConverter;
import se.sprinta.headhunterbackend.ad.converter.AdToAdDtoView;
import se.sprinta.headhunterbackend.ad.dto.AdDtoForm;
import se.sprinta.headhunterbackend.ad.dto.AdDtoView;
import se.sprinta.headhunterbackend.system.Result;
import se.sprinta.headhunterbackend.system.StatusCode;
import se.sprinta.headhunterbackend.user.User;
import se.sprinta.headhunterbackend.user.converter.UserToUserDtoViewConverter;
import se.sprinta.headhunterbackend.user.dto.UserDtoView;

import java.util.List;

@RestController
@RequestMapping("${api.endpoint.base-url-ads}")
@CrossOrigin("http://localhost:3000")
public class AdController {

    private AdService adService;
    private UserToUserDtoViewConverter userToUserDtoViewConverter;
    private AdDtoFormToAdConverter adDtoFormToAdConverter;
    private AdToAdDtoView adToAdDtoView;

    public AdController(AdService adService, UserToUserDtoViewConverter userToUserDtoViewConverter, AdDtoFormToAdConverter adDtoFormToAdConverter, AdToAdDtoView adToAdDtoView) {
        this.adService = adService;
        this.userToUserDtoViewConverter = userToUserDtoViewConverter;
        this.adDtoFormToAdConverter = adDtoFormToAdConverter;
        this.adToAdDtoView = adToAdDtoView;
    }

    @GetMapping("/findAllAds")
    public Result findAllAds() {
        List<Ad> allAds = this.adService.findAllAds();
        List<AdDtoView> allAdDtoViews = allAds.stream().map(ad -> this.adToAdDtoView.convert(ad)).toList();
        return new Result(true, StatusCode.SUCCESS, "Find All Ads Success", allAdDtoViews);
    }

    @GetMapping("/findById/{adId}")
    public Result findById(@PathVariable String adId) {
        Ad foundAd = this.adService.findById(adId);
        AdDtoView foundAdDtoView = this.adToAdDtoView.convert(foundAd);
        return new Result(true, StatusCode.SUCCESS, "Find Ad Success", foundAdDtoView);
    }

    @GetMapping("/findAllAdsByJobId/{jobId}")
    public Result findAllAdsByJobId(@PathVariable Long jobId) {
        List<Ad> foundAds = this.adService.findAdsByJobId(jobId);
        List<AdDtoView> foundAdDtoViews = foundAds.stream().map(ad -> this.adToAdDtoView.convert(ad)).toList();
        return new Result(true, StatusCode.SUCCESS, "Find All Ads By Job Id Success", foundAdDtoViews);
    }

    @GetMapping("/findUserByAdId/{adId}")
    public Result findUserByAdId(@PathVariable String adId) {
        User foundUser = this.adService.findUserByAdId(adId);
        UserDtoView foundUserDtoView = this.userToUserDtoViewConverter.convert(foundUser);
        return new Result(true, StatusCode.SUCCESS, "Find User By Ad Id Success", foundUserDtoView);
    }

    @PostMapping("/saveAd/{jobId}")
    public Result saveAd(@PathVariable Long jobId, @RequestBody AdDtoForm adDtoForm) {
        Ad ad = this.adDtoFormToAdConverter.convert(adDtoForm);
        Ad savedAd = this.adService.addAd(jobId, ad);
        AdDtoView savedAdDtoView = this.adToAdDtoView.convert(savedAd);
        return new Result(true, StatusCode.SUCCESS, "Save Ad Success", savedAdDtoView);
    }

    @DeleteMapping("/deleteAd/{adId}")
    public Result deleteAd(@PathVariable String adId) {
        this.adService.deleteAd(adId);
        return new Result(true, StatusCode.SUCCESS, "Ad Delete Success");
    }
}
