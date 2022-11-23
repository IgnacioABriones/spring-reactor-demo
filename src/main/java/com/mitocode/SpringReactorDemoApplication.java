package com.mitocode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;


@SpringBootApplication
public class SpringReactorDemoApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SpringReactorDemoApplication.class);
    private static List<String> dishes = new ArrayList<>();

    private void createMono(){
        Mono<String> m1 = Mono.just("Hello coders");
        m1.subscribe(x->log.info("Data: " + x));

        Mono.just(5).subscribe(x->log.info("Data" + x));
    }

    public void createFlux(){
        Flux<String> fx1 = Flux.fromIterable(dishes); //tuberia secuencia de datos
        //fx1.subscribe(x->log.info("Data: " +x));
        fx1.collectList().subscribe(list ->log.info("Data: " +list));   //flux a mono
    }

    public static void main(String[] args) {
        dishes.add("Ceviche");
        dishes.add("cazuela");
        SpringApplication.run(SpringReactorDemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        createFlux();
    }
}
