package com.george.shortener.service;

import com.george.shortener.dto.request.NotificationOptionsRequest;
import com.george.shortener.dto.response.UrlsResponse;
import com.george.shortener.entity.Response;
import com.george.shortener.entity.Url;
import com.george.shortener.mapper.ResponseMapper;
import com.george.shortener.repository.ResponseRepository;
import com.george.shortener.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResponseService {

    private final ResponseRepository responseRepository;
    private final UrlRepository urlRepository;
    private final ResponseMapper mapper;

    public Long createResponse(NotificationOptionsRequest request) {
        return Optional.of(request)
                .map(mapper::mapToResponse) // TODO: retrieve Response with equal options if exists
                .map(responseRepository::saveAndFlush)
                .map(Response::getId)
                .orElseThrow(); // TODO
    }

    public UrlsResponse generate(Long responseId) {
        Map<String, String> urlOptionMap = responseRepository.findById(responseId)
                .map(Response::getOptions)
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(
                        strings -> UUID.randomUUID().toString(),
                        Function.identity()
                ));

        Url url = Url.builder()
                .urlOptionMap(urlOptionMap)
                .build();// TODO: mapper

        Url save = urlRepository.save(url);
        return new UrlsResponse(
                save.getId(),
                urlOptionMap
        ); // TODO: mapper

    }
}
