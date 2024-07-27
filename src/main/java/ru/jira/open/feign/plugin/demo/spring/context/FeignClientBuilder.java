package ru.jira.open.feign.plugin.demo.spring.context;

import feign.Client;
import feign.Feign;
import feign.codec.Decoder;
import feign.codec.Encoder;
import lombok.RequiredArgsConstructor;
import ru.jira.open.feign.plugin.demo.dto.FeignClientConfigureData;

@RequiredArgsConstructor
public class FeignClientBuilder {

    private final Client client;
    private final Decoder jacksonDecoder;
    private final Encoder jacksonEncoder;

    public Object build(final FeignClientConfigureData clientConfigureData) {
        return Feign.builder()
                    .client(client)
                    .requestInterceptors(clientConfigureData.getRequestInterceptors())
                    .decoder(jacksonDecoder)
                    .encoder(jacksonEncoder)
                    .target(
                            clientConfigureData.getFeignClientClazz(),
                            clientConfigureData.getFeignClientUrl()
                    );
    }

}
