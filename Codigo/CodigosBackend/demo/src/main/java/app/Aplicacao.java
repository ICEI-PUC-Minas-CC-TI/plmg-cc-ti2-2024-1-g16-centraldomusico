package app;
import static spark.Spark.*;
import service.MusicoService;

public class Aplicacao {
    public static MusicoService musicoService = new MusicoService();

    public static void main(String[] args) {
        port(6789);
        staticFiles.location("/public");
        post("/usuario/insert", (request, response) -> musicoService.insert(request, response));    
        get("/usuario/get", (request, response) -> musicoService.get(request, response));
        post("/usuario/update", (request, response) -> musicoService.update(request, response));
        delete("/usuario/delete", (request, response) -> musicoService.delete(request, response));
    }
}

