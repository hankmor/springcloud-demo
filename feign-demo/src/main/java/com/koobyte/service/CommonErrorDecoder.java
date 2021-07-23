package com.koobyte.service;

import feign.Response;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;

import java.io.IOException;

public class CommonErrorDecoder implements ErrorDecoder {
    final Decoder decoder;
    final ErrorDecoder defaultDecoder = new ErrorDecoder.Default();

    public CommonErrorDecoder(Decoder decoder) {
        this.decoder = decoder;
    }

    @Override
    public Exception decode(String methodKey, Response response) {
//        Response build = response.toBuilder().status(200).build();
        try {
            return (Exception) decoder.decode(response, CommonClientException.class);
        } catch (IOException e) {
            return defaultDecoder.decode(methodKey, response);
        }
    }
}
