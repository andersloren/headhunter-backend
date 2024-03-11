package se.sprinta.headhunterbackend.ad.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import se.sprinta.headhunterbackend.ad.Ad;
import se.sprinta.headhunterbackend.ad.dto.AdDtoView;

@Component
public class AdToAdDtoView implements Converter<Ad, AdDtoView> {
    @Override
    public AdDtoView convert(Ad source) {
        return new AdDtoView(
                source.getId(),
                source.getCreatedDateTime(),
                source.getHtmlCode());
    }
}
