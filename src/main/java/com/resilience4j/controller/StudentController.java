package com.resilience4j.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
public class StudentController
{
	@GetMapping(value = "/student/{id}")
	@RateLimiter(name = "rateLimitingAPI", fallbackMethod = "rateLimitingFallback")
	public ResponseEntity<String> getStudentById(@PathVariable int id)
	{
		return ResponseEntity.ok("Details requested for " + id);
	}

	@GetMapping(value = "/course/{id}")
	@Bulkhead(name = "courseBulkheadApi", fallbackMethod = "bulkheadFallback")
	public ResponseEntity<String> getCourse(@PathVariable int id)
	{
        log.info("In course details for: {}", id);
		try
		{
			Thread.sleep(1000);
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
        log.info("Returning course details for: {}", id);
		return ResponseEntity.ok("Course" + id);
	}

	public ResponseEntity<Object> bulkheadFallback(int id, BulkheadFullException ex)
	{
		log.info("Bulkhead applied no further calls are accepted");

		return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
				.body("Too many request - No further calls are accepted");
	}

	public ResponseEntity<Object> rateLimitingFallback(int id, RequestNotPermitted ex)
	{
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Retry-After", "60s"); // retry after one minute

		return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
				.headers(responseHeaders) // send retry header
				.body("Too Many Requests - Retry After 1 Minute");
	}
}