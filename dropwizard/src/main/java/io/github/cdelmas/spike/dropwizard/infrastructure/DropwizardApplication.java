package io.github.cdelmas.spike.dropwizard.infrastructure;

import com.hubspot.dropwizard.guice.GuiceBundle;
import io.dropwizard.Application;
import io.dropwizard.java8.Java8Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.github.cdelmas.spike.dropwizard.car.CarModule;
import io.github.cdelmas.spike.dropwizard.car.CarResource;
import io.github.cdelmas.spike.dropwizard.car.CarsResource;
import io.github.cdelmas.spike.dropwizard.hello.HelloModule;
import io.github.cdelmas.spike.dropwizard.hello.HelloWorldResource;

public class DropwizardApplication extends Application<DropwizardServerConfiguration> {

    private GuiceBundle<DropwizardServerConfiguration> guiceBundle;

    @Override
    public void run(DropwizardServerConfiguration configuration, Environment environment) throws Exception {
        environment.healthChecks().register("template", guiceBundle.getInjector().getInstance(TemplateHealthCheck.class));
        environment.jersey().register(guiceBundle.getInjector().getInstance(HelloWorldResource.class));
        environment.jersey().register(guiceBundle.getInjector().getInstance(CarsResource.class));
        environment.jersey().register(guiceBundle.getInjector().getInstance(CarResource.class));
    }

    @Override
    public String getName() {
        return "Dropwizard Spike Server";
    }

    @Override
    public void initialize(Bootstrap<DropwizardServerConfiguration> bootstrap) {
        guiceBundle = GuiceBundle.<DropwizardServerConfiguration>newBuilder()
                .addModule(new HelloModule())
                .addModule(new CarModule())
                .setConfigClass(DropwizardServerConfiguration.class)
                .build();
        bootstrap.addBundle(guiceBundle);
        bootstrap.addBundle(new Java8Bundle());
    }
}
