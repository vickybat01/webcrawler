package com.java.webCrawler.service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.java.webCrawler.WebCrawlerConfig;

@Service
public class WebCrawlerService implements Runnable {
	
	Set<String> success = new HashSet<>();
	Set<String> skipped = new HashSet<>();
	Set<String> error   = new HashSet<>();
	
	@Autowired
	WebCrawlerConfig webCrawlerConfig;
	ExecutorService executor = null;
	
	public void crawlPages(WebCrawlerService service) {
		
	int cores = Runtime.getRuntime().availableProcessors();
	executor = Executors.newFixedThreadPool(cores);
	executor.execute(service);
	
	
	}

	@Override
	public void run() {

		  JSONParser jsonParser = new JSONParser();
		  
		  try (FileReader reader = new FileReader(webCrawlerConfig.getFile()))
	      {
	          //Read JSON file
			  JSONObject obj = (JSONObject) jsonParser.parse(reader);

	          JSONArray pages = (JSONArray) obj.get("pages");
	          List<String> totalPageList=new ArrayList<>();
	          Iterator itcount = pages.iterator();
	          Iterator it = pages.iterator();
	          //adding all pages to crawl
	          while(itcount.hasNext()){
	        	  JSONObject pageObjNew =  ((JSONObject) itcount.next());
	        	  String pageNew = (String) pageObjNew.get("address");
	        	  totalPageList.add(pageNew);
			  }
	          while(it.hasNext()) {
	        	  JSONObject pageObj =  ((JSONObject) it.next());
	        	  String page = (String) pageObj.get("address");
	        	  
	        	  if(!success.contains(page)&&!totalPageList.contains(page)) {
	        		  success.add(page);
	        	  }else if(success.contains(page)&&!totalPageList.contains(page)) {
	        		  skipped.add(page);
	        	  }
	        	  JSONArray links =  (JSONArray) pageObj.get("links");
	        	  Iterator pageItr = links.iterator();
	        	  while(pageItr.hasNext()) {
	        		  String linkPage = (String) pageItr.next();
	        		  if(!success.contains(linkPage)) {
	            		  success.add(linkPage);
	            	  }else {
	            		  skipped.add(linkPage);
	            	  }
	        		  if(!totalPageList.contains(linkPage)){
	        			  error.add(linkPage);
	        		  }
	        		  
	        	  }
	        	 
	          }
	          success.removeAll(error);
	          System.out.println("Success---->"+" "+success);
	    	  System.out.println("Skipped---->"+" "+skipped);
	    	  System.out.println("Error---->"+" "+error);
	    	  System.out.println(success.size()+"----"+skipped.size()+"----"+error.size());
	           
	          //Iterate over employee array
	       

	      } catch (FileNotFoundException e1) {
	          e1.printStackTrace();
	      } catch (IOException e2) {
	          e2.printStackTrace();
	      } catch (ParseException e3) {
	          e3.printStackTrace();
	      }
		  finally {
			  executor.shutdown();
		  }
		
	}
  }

