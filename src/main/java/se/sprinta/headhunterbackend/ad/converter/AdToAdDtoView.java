package se.sprinta.headhunterbackend.ad.converter;

import org.springframework.core.convert.converter.Converter;
import se.sprinta.headhunterbackend.ad.Ad;
import se.sprinta.headhunterbackend.ad.dto.AdDtoView;

public class AdToAdDtoView implements Converter<Ad, AdDtoView> {
    @Override
    public AdDtoView convert(Ad source) {
        return new AdDtoView(source.getHtmlCode());
    }
}
