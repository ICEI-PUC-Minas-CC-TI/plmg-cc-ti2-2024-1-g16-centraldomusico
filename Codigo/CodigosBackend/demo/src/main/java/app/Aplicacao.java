package app;

import static spark.Spark.*;

import dao.SessionDAO;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Spark;
import service.MusicoService;
import service.BandaService;
import service.CasaDeShowsService;

public class Aplicacao {
    public static MusicoService musicoService = new MusicoService();
    public static BandaService bandaService = new BandaService();
    public static CasaDeShowsService casaService = new CasaDeShowsService();
    public static SessionDAO sessionDAO = new SessionDAO();

    public static void main(String[] args) {
        port(6789);
        staticFiles.location("/public");
        // Apply CORS filter before defining routes
        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, OPTIONS");
            response.header("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With, Content-Length, Accept, Origin");
            response.header("Access-Control-Allow-Credentials", "true");
        });

        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            response.status(200); // Ensure HTTP 200 OK status for preflight requests
            return "OK";
        });


        
        // Rotas para Musico
        get("/usuario/getNomeInstrumentos", (request, response) -> musicoService.getInstrumentos(request, response));
        get("/instrumento/get", (request, response) -> musicoService.getImagensInstrumentos(request, response));
        post("/usuario/instrumento",(request,response) -> musicoService.cadastrarInstrumento(request,response));
        post("/usuario/insert", (request, response) -> musicoService.insert(request, response));
        get("/usuario/get/perfil", (request, response) -> musicoService.getById(request, response));
        get("/usuario/get", (request, response) -> musicoService.get(request, response));
        put("/usuario/update", (request, response) -> musicoService.update(request, response));
        post("/usuario/update", (request, response) -> musicoService.update(request, response));
        delete("/usuario/delete", (request, response) -> musicoService.delete(request, response));
        get("/usuario/checkBanda", (request, response) -> musicoService.checkBanda(request, response));

        // Rotas para Banda (exemplo, você deve implementar os métodos no BandaService)
        post("/banda/insert", (request, response) -> bandaService.insert(request, response));
        get("/banda/getAll", (request, response) -> bandaService.getAll(request, response));
        get("/banda/get", (request, response) -> bandaService.get(request, response));
        get("/banda/getByName", (req, res) -> bandaService.getByName(req, res));
        post("/banda/join", (req, res) -> bandaService.joinBand(req, res));
        post("/banda/update", (request, response) -> bandaService.update(request, response));
        delete("/banda/delete", (request, response) -> bandaService.delete(request, response));
        get("/banda/members", (req, res) -> bandaService.getBandaMembers(req, res));
        post("/banda/leave", (req, res) -> bandaService.leaveBand(req, res));

        // Rotas para CasaDeShows (exemplo, você deve implementar os métodos no CasaDeShowsService)
        post("/casa/postarAnuncio", (request, response) -> casaService.postarAnuncio(request, response));
        get("/casa/getAnunciosBanda", (request, response) -> casaService.getAnunciosBanda  (request, response));
        post("/casa/inscreverBanda", (request, response) -> casaService.inscreverBanda(request, response)); // Nova rota para inscrever a banda
        post("/casa/insert", (request, response) -> casaService.insert(request, response));
        post("/casa/desinscreverBanda", (request, response) -> casaService.desinscreverBanda(request, response));
        get("/casa/getInscritos", (request, response) -> casaService.getAllBandas(request, response));
        get("/casa/getAll", (request, response) -> casaService.getAll(request, response));
        get("/casa/getCasa", (request, response) -> casaService.getCasaById(request, response)); // Aqui é onde adicionamos a rota
        delete("/casa/delete", (request, response) -> casaService.delete(request, response));
    }
}
