package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Stream;

public class FluxAndMonoGeneratorService {

    public Flux<String> namesFlux() {
        return Flux.fromIterable(List.of("nirmal", "samir", "roshan", "niraj")).log();
    }

    public Mono<String> nameMono() {
        return Mono.just("Nirmal")
                .log();
    }

    public Flux<String> namesFluxMapping() {
        return Flux.fromIterable(List.of("apple", "ball", "cat"))
                .map(String::toUpperCase)
                .log();
    }

    public Flux<Integer> namesFluxFilter() {
        return Flux.fromIterable(List.of(2, 3, 4, 5, 6, 7, 8))
                .filter(n -> n % 2 == 0);
    }


    public Flux<String> namesFluxImmutability() {
        var nameFlux = Flux.fromIterable(List.of("apple", "ball", "cat"));
        nameFlux.map(String::toUpperCase);
        return nameFlux;
    }

    public Flux<String> namesFluxFliterLength(int len) {
        return Flux.fromIterable(List.of("apple", "ball", "cat", "dog", "elephant"))
                .filter(str -> str.length() > len)
                .map(s -> s.length() + "-" + s);
    }

    public Flux<String> namesFluxFlatMap() {
        return Flux.fromIterable(List.of("apple", "ball"))
                .flatMap(s -> splitString(s));
    }

    public Flux<String> splitString(String str) {
        var arr = str.split("");
        return Flux.fromArray(arr);
    }

    public Flux<String> namesFluxFlatMapAsync(int len) {
        return Flux.fromIterable(List.of("apple", "ball"))
                .map(n -> n.toUpperCase())
                .filter(s -> s.length() > len)
                .flatMap(s -> splitStrWithDelay(s))
                .log();
    }

    public Flux<String> splitStrWithDelay(String str) {
        var arr = str.split("");
        var delay = new Random().nextInt(1000);
        return Flux.fromArray(arr)
                .delayElements(Duration.ofMillis(delay));
    }

    public Mono<List<String>> namesMonoFlatmap(int len) {
        return Mono.just("hello")
                .map(String::toUpperCase)
                .filter(n -> n.length() > len)
                .flatMap(this::splitMonoStr)
                .log();
    }

    private Mono<List<String>> splitMonoStr(String s) {
        var arr = s.split("");
        var charList = List.of(arr);
        return Mono.just(charList);
    }

    public Flux<String> namesMonoFlatmapMany(int len) {
        return Mono.just("hey")
                .map(String::toUpperCase)
                .filter(n -> n.length() > len)
                .flatMapMany(this::splitString)
                .log();
    }

    public Flux<String> namesFluxTransform(int strLen) {

        // since tranform() takes Function as input parameter
        Function<Flux<String>, Flux<String>> filterMap = name -> name.map(String::toUpperCase)
                .filter(n -> n.length() > strLen);

        return Flux.fromIterable(List.of("apple", "ball", "cat"))
                .transform(filterMap)
                .flatMap(n -> splitString(n))
                .log();
    }

    public Flux<String> namesFluxTransformDefaultValue(int strLen) {
        Function<Flux<String>, Flux<String>> mapFilter = name -> name.map(String::toUpperCase).filter(n -> n.length() > strLen);

        return Flux.fromIterable(List.of("hi", "bye", "apple"))
                .transform(mapFilter)
                .flatMap(item -> splitString(item))
                .defaultIfEmpty("default value")
                .log();
    }

    public Flux<String> namesFluxTransformSwitchIfEmpty(int strLen) {
        Function<Flux<String>, Flux<String>> mapFilter = name -> name.map(String::toUpperCase)
                .filter(n -> n.length() > strLen)
                .flatMap(item -> splitString(item));

        var defaultFlux = Flux.just("default")
                .transform(mapFilter);

        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .transform(mapFilter)
                .switchIfEmpty(defaultFlux)
                .log();
    }

    public Flux<String> explore_concat() {
        Flux<String> first = Flux.just("A", "B", "C");
//        Flux<String> test = Flux.just("A", "B", "C","D","e","f","g","h","t","w","q","r","z","o"); // to test if Flux.just can take more than 10 arguments, it can take.
        Flux<String> second = Flux.just("D", "E", "F");

        return Flux.concat(first, second).log();
    }

    // note: concat is static method and concatWith is instance method of Flux
    public Flux<String> explore_concat_with() {
        Flux<String> first = Flux.just("A", "B", "C");
        Flux<String> second = Flux.just("D", "E", "F");

        return first.concatWith(second).log();
    }

    public Flux<String> explore_concatWith_mono() {
        var aMono = Mono.just("A");
        var bMono = Mono.just("B");

        return aMono.concatWith(bMono).log();
    }

    public Flux<String> explore_merge() {
        var first = Flux.just("A", "B", "C")
                .delayElements(Duration.ofMillis(100));

        var second = Flux.just("D", "E", "F")
                .delayElements(Duration.ofMillis(125));

        return Flux.merge(first, second).log();
    }

    public Flux<String> explore_mergeWith() {
        var abcFlux = Flux.just("A", "B", "C")
                .delayElements(Duration.ofMillis(100));

        var defFlux = Flux.just("D", "E", "F")
                .delayElements(Duration.ofMillis(125));

        return abcFlux.mergeWith(defFlux).log();
    }

    public Flux<String> explore_mergeWith_mono() {
        var aMono = Mono.just("A");

        var dMono = Mono.just("D");

        return aMono.mergeWith(dMono);
    }

    public Flux<String> explore_merge_sequential() {
        var first = Flux.just("A", "B", "C")
                .delayElements(Duration.ofMillis(100));

        var second = Flux.just("D", "E", "F")
                .delayElements(Duration.ofMillis(125));

        return Flux.mergeSequential(first, second).log();
    }

    public Flux<String> explore_zip() {
        var firstFlux = Flux.just("A", "B", "C");
        var secondFlux = Flux.just("D", "E", "F");

        return Flux.zip(firstFlux, secondFlux, (first, second) -> first + second)
                .log();
    }

    public Flux<String> explore_zip_4Flux() {
        var abcFlux = Flux.just("A", "B", "C");
        var defFlux = Flux.just("D", "E", "F");
        var _123Flux = Flux.just("1", "2", "3");
        var _456Flux = Flux.just("4", "5", "6");

        return Flux.zip(abcFlux, defFlux, _123Flux, _456Flux)
                .map(t4 -> t4.getT1() + t4.getT2() + t4.getT3() + t4.getT4())
                .log();
    }

    public static void main(String[] args) {
        FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

        fluxAndMonoGeneratorService.namesFlux()
                .subscribe(name -> {
                    System.out.println("Flux Name is: " + name);
                });

        fluxAndMonoGeneratorService.nameMono()
                .subscribe(name -> {
                    System.out.println("Mono name is: " + name);
                });

        fluxAndMonoGeneratorService.namesFluxMapping()
                .subscribe(name -> {
                    System.out.println("mapped name: " + name);
                });

        fluxAndMonoGeneratorService.namesFluxFilter()
                .subscribe(n -> {
                    System.out.println("filtered flux value is: " + n);
                });

        fluxAndMonoGeneratorService.namesFluxFliterLength(4).subscribe(name -> {
            System.out.println("filtered length name: " + name);
        });

        fluxAndMonoGeneratorService.namesFluxFlatMap().subscribe(n -> {
            System.out.println("flatans: " + n);
        });

        fluxAndMonoGeneratorService.explore_concat().subscribe(val -> {
            System.out.println(val);
        });

        fluxAndMonoGeneratorService.explore_concatWith_mono().subscribe(val -> {
            System.out.println(val);
        });

    }
}
