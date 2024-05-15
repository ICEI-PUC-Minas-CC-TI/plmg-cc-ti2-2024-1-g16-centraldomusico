package app;
import static spark.Spark.*;
import service.MusicoService;
import service.BandaService;
import service.CasaDeShowsService;

public class Aplicacao {
    public static MusicoService musicoService = new MusicoService();
    public static BandaService bandaService = new BandaService();
    public static CasaDeShowsService casaService = new CasaDeShowsService();


    public static void main(String[] args) {
        port(6789);
        staticFiles.location("/public");
        post("/usuario/insert", (request, response) -> musicoService.insert(request, response));    
        get("/usuario/get", (request, response) -> musicoService.get(request, response));
        post("/usuario/update", (request, response) -> musicoService.update(request, response));
        delete("/usuario/delete", (request, response) -> musicoService.delete(request, response));
        post("/banda/insert", (request, response) -> bandaService.insert(request, response));    
        get("/banda/get", (request, response) -> bandaService.get(request, response));
        post("/banda/update", (request, response) -> bandaService.update(request, response));
        delete("/banda/delete", (request, response) -> bandaService.delete(request, response));
        post("/casa/insert", (request, response) -> casaService.insert(request, response));    
        get("/casa/get", (request, response) -> casaService.get(request, response));
        post("/casa/update", (request, response) -> casaService.update(request, response));
        delete("/casa/delete", (request, response) -> casaService.delete(request, response));
    }
}

