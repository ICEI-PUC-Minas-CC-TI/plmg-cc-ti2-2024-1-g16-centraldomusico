package app;

import static spark.Spark.*;
import service.MusicoService;


public class Aplicacao {
	
	private static MusicoService musicoService = new MusicoService();
	
    public static void main(String[] args) {
        port(6789);
        
        staticFiles.location("/public");
        
        post("/musico/insert", (request, response) -> musicoService.insert(request, response));

        get("/musico/:id", (request, response) -> musicoService.get(request, response));
        
        get("/musico/list/:orderby", (request, response) -> musicoService.getAll(request, response));

        get("/musico/update/:id", (request, response) -> musicoService.getToUpdate(request, response));
        
        post("/musico/update/:id", (request, response) -> musicoService.update(request, response));
           
        get("/musico/delete/:id", (request, response) -> musicoService.delete(request, response));

             
    }
}