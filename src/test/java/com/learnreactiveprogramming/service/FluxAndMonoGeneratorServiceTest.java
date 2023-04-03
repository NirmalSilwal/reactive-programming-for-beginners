package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FluxAndMonoGeneratorServiceTest {

    FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

    @Test
    void namesFlux() {
        var namesFlux = fluxAndMonoGeneratorService.namesFlux();

//        StepVerifier.create(namesFlux)
//                .expectNext("nirmal","samir","roshan","niraj")
//                .verifyComplete();

//        StepVerifier.create(namesFlux)
////                .expectNextCount(2)
//                .expectNextCount(4)
//                .verifyComplete();

        StepVerifier.create(namesFlux)
                .expectNext("nirmal")
                .expectNextCount(1)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void namesFluxMapping() {
        var namesFlux = fluxAndMonoGeneratorService.namesFluxMapping();

        StepVerifier.create(namesFlux)
                .expectNext("APPLE", "BALL", "CAT")
                .verifyComplete();
    }

    @Test
    void namesFluxImmutability() {
        var namesFlux = fluxAndMonoGeneratorService.namesFluxImmutability();

        StepVerifier.create(namesFlux)
//                .expectNext("APPLE", "BALL", "CAT")
                .expectNext("apple", "ball", "cat")
                .verifyComplete();
    }

    @Test
    void namesFluxFliterLength() {
        var namesFlux = fluxAndMonoGeneratorService.namesFluxFliterLength(4);

        StepVerifier.create(namesFlux)
                .expectNext("5-apple", "8-elephant")
                .verifyComplete();
    }

    @Test
    void namesFluxFlatMapAsync() {
        int len = 3;
        var namesFlux = fluxAndMonoGeneratorService.namesFluxFlatMapAsync(len);

        StepVerifier.create(namesFlux)
//                .expectNext("A","P","P","L","E","B","A","L","L")
                .expectNextCount(9)
                .verifyComplete();

    }

    @Test
    void namesMonoFlatmap() {
        int len = 3;
        var monoValue = fluxAndMonoGeneratorService.namesMonoFlatmap(len);

        StepVerifier.create(monoValue)
                .expectNext(List.of("H", "E", "L", "L", "O"))
                .verifyComplete();
    }

    @Test
    void namesMonoFlatmapMany() {
        int strLength = 2;
        var monoValue = fluxAndMonoGeneratorService.namesMonoFlatmapMany(strLength);

        StepVerifier.create(monoValue)
                .expectNext("H", "E", "Y")
                .verifyComplete();
    }

    @Test
    void namesFluxTransform() {
        int strLength = 3;
        var monoValue = fluxAndMonoGeneratorService.namesFluxTransform(strLength);

        StepVerifier.create(monoValue)
                .expectNext("A", "P", "P", "L", "E", "B", "A", "L", "L")
                .verifyComplete();
    }

    @Test
    void namesFluxTransformDefaultValue() {
        int strLen = 7;
        var monoValue = fluxAndMonoGeneratorService.namesFluxTransformDefaultValue(strLen);

        StepVerifier.create(monoValue)
                .expectNext("default value")
                .verifyComplete();
    }

    @Test
    void namesFluxTransformSwitchIfEmpty() {
        int strLen = 6;
        var namesFlux = fluxAndMonoGeneratorService.namesFluxTransformSwitchIfEmpty(strLen);

        StepVerifier.create(namesFlux)
                .expectNext("D", "E", "F", "A", "U", "L", "T")
                .verifyComplete();
    }

    @Test
    void explore_concat() {
        var concatFlux = fluxAndMonoGeneratorService.explore_concat();

        StepVerifier.create(concatFlux)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    void explore_merge() {
        var value = fluxAndMonoGeneratorService.explore_merge();

        StepVerifier.create(value)
                .expectNext("A", "D", "B", "E", "C", "F")
                .verifyComplete();
    }

    @Test
    void explore_mergeWith_mono() {
        var fluxMonoItem = fluxAndMonoGeneratorService.explore_concatWith_mono();

        StepVerifier.create(fluxMonoItem)
                .expectNext("A", "B")
                .verifyComplete();
    }

    @Test
    void explore_merge_sequential() {
        var fluxMono = fluxAndMonoGeneratorService.explore_merge_sequential();
        StepVerifier.create(fluxMono)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    void explore_zip() {
        var fluxMono = fluxAndMonoGeneratorService.explore_zip();
        StepVerifier.create(fluxMono)
                .expectNext("AD", "BE", "CF")
                .verifyComplete();
    }

    @Test
    void explore_zip_4Flux() {
        var fluxes = fluxAndMonoGeneratorService.explore_zip_4Flux();
        StepVerifier.create(fluxes)
                .expectNext("AD14", "BE25", "CF36")
                .verifyComplete();
    }
}