package com.mitocode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;


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

    public void m1doOnNext(){
        Flux<String> fx1 = Flux.fromIterable(dishes);
        //fx1.doOnNext(e->log.info(e));
        //nada sucede hasta que te subscribas
        fx1.doOnNext(log::info)

                .subscribe();

    }

    public void m2map(){
        Flux<String> fx1 = Flux.fromIterable(dishes);
        //un proceso de transf. y cada elemento lo vaos a pasar a mayuscula
        //transformar igual al stream
        Flux<String> fx2 = fx1.map(String::toUpperCase);
                fx2.subscribe(log::info);
    }

    public void m3flatMap(){
        Mono.just("jaime").map(x->31).subscribe(e->log.info("Data: " +e));
        Mono.just("jaime").map(x->Mono.just(31)).subscribe(e->log.info("Data: " + e));
        Mono.just("jaime").flatMap(x->Mono.just(31)).subscribe(e->log.info("Data: " + e));
    }

    public void m4range(){
        Flux<Integer> fx1 = Flux.range(0,10);
        fx1.map(x->x+1).subscribe(x->log.info(x.toString()));
    }

    public void m5delayElements() throws InterruptedException {
        Flux.range(0,10)
                .delayElements(Duration.ofSeconds(2))
                .doOnNext(x->log.info("Data: " + x))
                .subscribe();
        Thread.sleep(22000);
    }

    public void m6zipwith(){
        List<String> clients = new ArrayList<>();
        clients.add("client 1");
        clients.add("client 2");
        clients.add("client 3");

        Flux<String> fx1 = Flux.fromIterable(dishes);
        Flux<String> fx2 = Flux.fromIterable(clients);

        fx1.zipWith(fx2, (d,c) -> d+ "-" + c)
                .subscribe(log::info);
    }

    public void m7merge(){
        List<String> clients = new ArrayList<>();
        clients.add("client 1");
        clients.add("client 2");
        clients.add("client 3");

        Flux<String> fx1 = Flux.fromIterable(dishes);
        Flux<String> fx2 = Flux.fromIterable(clients);

        Flux.merge(fx1,fx2).subscribe(log::info);

    }

    public void m8filter(){
        Predicate<String> predicate = p->p.endsWith("la");
        Flux<String> fx1 = Flux.fromIterable(dishes);
        fx1.filter(predicate)
                .subscribe(log::info);
    }

    public void m9takelast(){
        Flux<String> fx1 = Flux.fromIterable(dishes);
        fx1.takeLast(2).subscribe(log::info);
    }

    public void m10take(){
        Flux<String> fx1 = Flux.fromIterable(dishes);
        fx1.take(2)
                .subscribe(log::info);
    }

    public void m11DefaultifEmpty(){
        dishes = new ArrayList<>();
        Flux<String> fx1 = Flux.fromIterable(dishes);

        fx1.map(e ->"P: " +e)
                .defaultIfEmpty("EMPTY FLUX")
                .subscribe(log::info);
    }

    public void m12Error(){
        Flux<String> fx1 = Flux.fromIterable(dishes);

        fx1.doOnNext(d->{
            throw new ArithmeticException("BAD NUMBER");
        })
                .onErrorReturn("Error, Please Reboot your system")
                //.onErrorMap(ex ->new Exception(ex.getMessage()))
                .subscribe(x->log.info("Data: " + x));

    }


    public static void main(String[] args) {
        dishes.add("Ceviche");
        dishes.add("cazuela");
        dishes.add("Lomo Saltado");
        SpringApplication.run(SpringReactorDemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //m1doOnNext(); //depuracion
        //createFlux();
        //m2map();
        //m3flatMap();
        //m4range();
        //m5delayElements();
        //m6zipwith();
        //m7merge();
        //m8filter();
        //m9takelast();
        //m10take();
       // m11DefaultifEmpty();
        m12Error();
    }
}
