package com.java.webCrawler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.java.webCrawler.service.WebCrawlerService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WebCrawlerApplicationTests {
	
	@Autowired
	WebCrawlerService webCrawlerService;
	@Test
	public void contextLoads() {
		webCrawlerService.crawlPages(webCrawlerService);
	}

}
