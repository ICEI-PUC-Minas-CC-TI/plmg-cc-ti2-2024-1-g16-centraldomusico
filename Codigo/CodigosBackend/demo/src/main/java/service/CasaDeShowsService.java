package service;

import dao.CasaDeShowsDAO;
import model.CasaDeShows;
import spark.Request;
import spark.Response;
import model.Banda;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CasaDeShowsService {
    private CasaDeShowsDAO casaDAO;
    private Gson gson;

    public CasaDeShowsService() {
        this.casaDAO = new CasaDeShowsDAO();
        this.gson = new Gson();
    }

    public CasaDeShowsService(CasaDeShowsDAO casaDAO) {
        this.casaDAO = casaDAO;
        this.gson = new Gson();
    }

    public String insert(Request request, Response response) {
        String str = request.body();
        CasaDeShows casa = gson.fromJson(str, CasaDeShows.class);
        if (casaDAO.insert(casa)) {
            response.status(201); // HTTP 201 Created
            return "Casa de Shows registrada!";
        } else {
            response.status(500); // HTTP 500 Internal Server Error
            return "Erro no cadastro!";
        }
    }

    public String delete(Request request, Response response) {
        String str = request.body();
        CasaDeShows casa = gson.fromJson(str, CasaDeShows.class);
        if (casaDAO.delete(casa.getID())) {
            response.status(200); // HTTP 200 OK
            return "Casa de Shows deletada!";
        } else {
            response.status(500); // HTTP 500 Internal Server Error
            return "Erro no delete!";
        }
    }


    //criar metodo getAnunciosBanda que retorna todos os eventos que a banda está inscrita
    public String getAnunciosBanda(Request request, Response response) {
        //printar parametro
        System.out.println("PARAM: "+request.queryParams("id"));
        //printar TODOS os parametros
        System.out.println("PARAMS: "+request.queryParams());
        String body = request.body();
        System.out.println("BODY: "+body);
        int bandaId = request.queryParams("id") != null ? Integer.parseInt(request.queryParams("id")) : -1;
        List<CasaDeShows> casas = casaDAO.getAnunciosBanda(bandaId);
        System.out.println("CASAS: "+casas);
        response.type("application/json");
        return gson.toJson(casas);
    }

    //criar metodo postarAnuncio que insere um evento na casa de shows
    public String postarAnuncio(Request request, Response response) {
        String body = request.body();
        System.out.println("BODY: "+body);
        CasaDeShows casa = new CasaDeShows();
        JsonObject json = JsonParser.parseString(body).getAsJsonObject();
        casa.setNome(json.get("nomeCasa").getAsString());
        casa.setEndereco(json.get("endereco").getAsString());
        casa.setValor(json.get("valor").getAsFloat());
        //pegar horario
        LocalTime horario = LocalTime.parse(json.get("horario").getAsString());
        System.out.println("HORARIO: "+horario);
        casa.setHorario(horario);
        casa.setNomeDono(json.get("donoCasa").getAsString());
        casa.setTelefone(json.get("telefone").getAsString());
        System.out.println("CASA: "+casa);
        if (casaDAO.postarAnuncio(casa)) {
            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("message", "Evento postado com sucesso!");
            response.status(201); // HTTP 201 Created
            return responseJson.toString();
        } else {
            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("message", "Erro ao postar evento!");
            response.status(500); // HTTP 500 Internal Server Error
            return responseJson.toString();
        }
    }

    public String get(Request request, Response response) {
        int id = Integer.parseInt(request.queryParams("id"));
        CasaDeShows casa = casaDAO.get(id);

        if (casa != null) {
            response.status(200); // HTTP 200 OK
            return gson.toJson(casa);
        } else {
            response.status(404); // HTTP 404 Not Found
            return "Casa de Shows não encontrada";
        }
    }
    //criar metodo desinscreverBanda
    public String desinscreverBanda(Request request, Response response) {
        String body = request.body();
        System.out.println("BODY: "+body);
        JsonObject json = JsonParser.parseString(body).getAsJsonObject();
        int casaId = json.get("casaId").getAsInt();
        int bandaId = json.get("bandaId").getAsInt();
        if (casaDAO.desinscreverBanda(casaId, bandaId)) {
            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("message", "Banda desinscrita com sucesso!");
            response.status(200); // HTTP 200 OK
            return responseJson.toString();
        } else {
            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("message", "Erro ao desinscrever a banda!");
            response.status(500); // HTTP 500 Internal Server Error
            return responseJson.toString();
        }
    }
    //criar metodo isInscrito para checar se a banda está inscrita nesse evento, retornar true ou false
    public String isInscrito(Request request, Response response) {
        String body = request.body();
        System.out.println("BODY: "+body);
        JsonObject json = JsonParser.parseString(body).getAsJsonObject();
        int casaId = json.get("casaId").getAsInt();
        int bandaId = json.get("bandaId").getAsInt();
        if (casaDAO.isInscrito(casaId, bandaId)) {
            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("message", "Banda inscrita!");
            response.status(200); // HTTP 200 OK
            return responseJson.toString();
        } else {
            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("message", "Banda não inscrita!");
            response.status(404); // HTTP 404 Not Found
            return responseJson.toString();
        }
    }
    public Object getCasaById(Request req, Response res) {
        int id = Integer.parseInt(req.queryParams("id"));
        System.out.println("ID: " + id);
        System.out.println("Request: " + req.body());
        CasaDeShows casa = casaDAO.getCasaDeShows(id);

        if (casa != null) {
            res.type("application/json");
            return gson.toJson(casa);
        } else {
            res.status(404); // HTTP 404 Not Found
            return "Casa de Shows não encontrada";
        }
    }

    //criar metodo getAllBandas para pegar todas as bandas inscritas nesse evento
    public String getAllBandas(Request request, Response response) {
        int id = Integer.parseInt(request.queryParams("id"));
        List<Banda> bandas = casaDAO.getBandas(id);
        response.type("application/json");
        System.out.println("BANDAS: "+bandas);
        return gson.toJson(bandas);     
    }

        public String inscreverBanda(Request request, Response response) {
        String body = request.body();
        System.out.println("BODY: "+body);
        JsonObject json = JsonParser.parseString(body).getAsJsonObject();
        int casaId = json.get("casaId").getAsInt();
        int bandaId = json.get("bandaId").getAsInt();
        System.out.println("Casa ID: " + casaId);
        System.out.println("Banda ID: " + bandaId);
        System.out.println("BODY: "+request.body());
        if (casaDAO.inscreverBanda(casaId, bandaId)) {
            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("message", "Banda inscrita com sucesso!");
            response.status(200); // HTTP 200 OK
            return responseJson.toString();
        } else {
            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("message", "Erro ao inscrever a banda!");
            response.status(500); // HTTP 500 Internal Server Error
            return responseJson.toString();
        }
    }

    public String getAll(Request request, Response response) {
        response.type("application/json");
        return gson.toJson(casaDAO.getAll());
    }
}
