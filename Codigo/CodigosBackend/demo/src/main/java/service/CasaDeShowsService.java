package service;

import dao.CasaDeShowsDAO;
import model.CasaDeShows;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;

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

    public String update(Request request, Response response) {
        String str = request.body();
        CasaDeShows casa = gson.fromJson(str, CasaDeShows.class);
        if (casaDAO.update(casa)) {
            response.status(200); // HTTP 200 OK
            return "Casa de Shows atualizada!";
        } else {
            response.status(500); // HTTP 500 Internal Server Error
            return "Erro na atualização!";
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

    public String getAll(Request request, Response response) {
        response.type("application/json");
        return gson.toJson(casaDAO.getAll());
    }
}
