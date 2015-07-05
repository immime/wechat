package com.immine.wmall.web;

import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.immine.wmall.entity.Greeting;

@RestController
@RequestMapping(value="/greeting")
public class GreetingCtrl {
	
	private static final Logger logger = LoggerFactory.getLogger(GreetingCtrl.class);

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@RequestMapping
	public @ResponseBody Greeting sayHello(
			@RequestParam(value = "name", required = false, defaultValue = "Stranger") String name) {
		logger.debug("excute count %s", counter.incrementAndGet());
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}

}
