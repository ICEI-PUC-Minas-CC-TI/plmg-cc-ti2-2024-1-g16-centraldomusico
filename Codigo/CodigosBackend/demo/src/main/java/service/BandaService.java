package service;

import dao.BandaDAO;
import javafx.scene.chart.PieChart.Data;
import model.Banda;
import model.Musico;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.sql.Date;
import java.time.LocalDateTime;
//importar data
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BandaService {
    public BandaDAO bandaDAO = new BandaDAO();
    private Gson gson;
    public BandaService(){
        this.gson = new Gson();
    }
    public BandaService(BandaDAO bandaDAO) {
        this.bandaDAO = bandaDAO;
        this.gson = new Gson();
    }

    public String getBandaMembers(Request request, Response response) {
        int bandaId = Integer.parseInt(request.queryParams("bandaId"));
        List<Musico> membros = bandaDAO.getBandaMembers(bandaId);
        response.type("application/json");
        return gson.toJson(membros);
    }

    public String getByName(Request request, Response response) {
        String nomeBanda = request.queryParams("nomeBanda");
        Banda banda = bandaDAO.getByName(nomeBanda);
        System.out.println("Banda: " + banda);
        if (banda != null) {
            System.out.println("Banda encontrada: " + banda.getNome());
            response.type("application/json");
            return gson.toJson(banda);
        } else {
            response.status(404); // HTTP 404 Not Found
            return "Banda não encontrada";
        }
    }
    public String joinBand(Request request, Response response) {
        try {
            String body = request.body();
            JsonObject json = JsonParser.parseString(body).getAsJsonObject();
            int bandaId = json.get("bandaId").getAsInt();
            int musicoId = json.get("musicoId").getAsInt();
    
            if (bandaDAO.isUserInBand(bandaId, musicoId)) {
                response.status(400); // HTTP 400 Bad Request
                return "Você já está nesta banda.";
            }
    
            if (bandaDAO.joinBand(bandaId, musicoId)) {
                JsonObject responseJson = new JsonObject();
                responseJson.addProperty("message", "Você entrou na banda com sucesso!");
                response.status(200); // HTTP 200 OK
                return responseJson.toString();
            } else {
                response.status(500); // HTTP 500 Internal Server Error
                return "Erro ao entrar na banda.";
            }
        } catch (Exception e) {
            System.out.println("Erro ao entrar na banda: " + e.getMessage());
            e.printStackTrace();
            response.status(500); // HTTP 500 Internal Server Error
            return "Erro ao entrar na banda: " + e.getMessage();
        }
    }
    
    public String leaveBand(Request req, Response res) {
        try {
            String body = req.body();
            JsonObject json = JsonParser.parseString(body).getAsJsonObject();
            int bandaId = json.get("bandaId").getAsInt();
            int musicoId = json.get("musicoId").getAsInt();
    
            if (bandaDAO.leaveBand(bandaId, musicoId)) {
                JsonObject responseJson = new JsonObject();
                responseJson.addProperty("message", "Você saiu da banda com sucesso!");
                res.status(200); // HTTP 200 OK
                return responseJson.toString();
            } else {
                res.status(500); // HTTP 500 Internal Server Error
                return "Erro ao sair da banda.";
            }
        } catch (Exception e) {
            System.out.println("Erro ao sair da banda: " + e.getMessage());
            e.printStackTrace();
            res.status(500); // HTTP 500 Internal Server Error
            return "Erro ao sair da banda: " + e.getMessage();
        }
    }
    
    public Object insert(Request req, Response res) {
        try {
            String body = req.body();
            System.out.println("Request Body: " + body); // Log the request body for debugging

            // Parse the JSON body to get the parameters
            JsonObject json = JsonParser.parseString(body).getAsJsonObject();
            String nome = json.get("nome").getAsString();
            String descricao = json.get("descricao").getAsString();
            String senha = json.get("senha").getAsString();
            float cache = json.get("cache").getAsFloat();
            // data criacao = hoje ( pegar no formado DD - MM - YYYY)
            LocalDateTime dataCriacao = LocalDateTime.now();
            Date data = Date.valueOf(dataCriacao.toLocalDate());
            System.out.println("Data: " + data);
            //formatar para dd-mm-aaaa
            String objetivo = json.get("objetivo").getAsString();
            String estilo = json.get("estilo").getAsString();

            System.out.println("Nome: " + nome);
            System.out.println("Descricao: " + descricao);
            System.out.println("Senha: " + senha);
            System.out.println("Cache: " + cache);

            System.out.println("Objetivo: " + objetivo);
            System.out.println("Estilo: " + estilo);
            Banda banda = new Banda(0, nome, descricao, senha, cache, estilo, objetivo);
            System.out.println("Banda Object: " + banda); // Log the Musico object for debugging

            if (bandaDAO.insert(banda)) {
                System.out.println("Banda inserida com sucesso!");
                res.status(201); // HTTP 201 Created
                JsonObject responseJson = new JsonObject();
                responseJson.addProperty("message", "banda inserido com sucesso!");
                return responseJson;
            } else {
                System.out.println("Erro ao inserir banda.");
                res.status(500); // HTTP 500 Internal Server Error
                JsonObject responseJson = new JsonObject();
                responseJson.addProperty("message", "Erro ao inserir banda.");
                return responseJson;
            }
        } catch (Exception e) {
            System.out.println("Erro ao inserir banda: " + e.getMessage());
            e.printStackTrace();
            res.status(500); // HTTP 500 Internal Server Error
            return "Erro ao inserir banda: " + e.getMessage();
        }
    }

    //metodo para deletar banda, vai receber o id da banda e a senha dela, caso a senha esteja correta a banda sera deletada
    public String delete(Request request, Response response){
        String str = request.body();
        JsonObject json = JsonParser.parseString(str).getAsJsonObject();
        int id = json.get("id").getAsInt();
        String senha = json.get("senha").getAsString();
        if(bandaDAO.delete(id, senha) == true){
            return "Banda deletada!";
        }else{
            return "Erro ao deletar banda!";
        }
    }
    public String update(Request request, Response response){
        String str = request.body();
        Gson gson = new Gson();
        Banda banda = gson.fromJson(str, Banda.class);
        if(bandaDAO.update(banda) == true){
            return "Banda atualizada!";
        }else{
            return "Erro na atualização!";
        }
    }
	public String get(Request request, Response response){
		String str = request.body();
		System.out.println(str);
		Gson gson = new Gson();
		Banda banda = gson.fromJson(str, Banda.class);

		if(banda.getID() == 0){
			return "Erro ao buscar banda!";
		}
		else{
			banda = bandaDAO.get(banda.getID());
			return gson.toJson(banda);
		}
	}
    public String getAll(Request request, Response response) {
        Gson gson = new Gson();
        System.out.println("GET ALL BANDAS");
        response.type("application/json");
        List<Banda> bandas = bandaDAO.getAllBandas();
        return gson.toJson(bandas);
    }
}
