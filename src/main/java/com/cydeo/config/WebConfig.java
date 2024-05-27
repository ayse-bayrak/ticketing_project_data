package com.cydeo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
// for Static end points
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/welcome").setViewName("welcome");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/").setViewName("login");
    }

}

//those methods just return view
// the view is just a static view. And it doesn't have any dynamic data inside.
// So it's not using any live data. It's not using any objects from your database.
// It's just a login page that just says login.
// And then there's like email password and a and a button
//or same with Welcome page right? It was just saying like welcome.
// So if if this is a situation, and if you just need the endpoint,
// and just a static Hdml file without actually adding or sending any data to the HTML.
// Then we can use this one. But do we have to do this? No, this is just like a shortcut,
// so that you see, like different ways of implementing it.
//But we could also just create a very regular controller method with like a get mapping,
// and we could say welcome, but there is no data in the HTML. That is being used.