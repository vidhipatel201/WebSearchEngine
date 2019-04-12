package service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import loader.ServletContextClass;

@Path("test")
public class Service1 {
	
	
	@POST
	@Path("/{query}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public String display(@PathParam("query") String query ) {
		long start = System.currentTimeMillis();
		List<String> res = ServletContextClass.finalCall(query);
		long end = System.currentTimeMillis();
		System.out.println(end-start);
//		System.out.println("TST size - "+res.size());
//		for(String x:res){
//			System.out.println(x);
//		}
//		System.out.println(ServletContextClass.words.size());
		return new Gson().toJson(res );
		
	}
}
