package service;

import dao.MusicoDAO;
import model.Musico;
import spark.Request;
import spark.Response;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.List;

public class MusicoService {
    private MusicoDAO musicoDAO;

    public MusicoService() {
        musicoDAO = new MusicoDAO();
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
            String instrumento1 = json.get("instrumento1").getAsString();
            String instrumento2 = json.get("instrumento2").getAsString();
            String instrumento3 = json.get("instrumento3").getAsString();
            String objetivo = json.get("objetivo").getAsString();
            String estilo = json.get("estilo").getAsString();

            System.out.println("Nome: " + nome);
            System.out.println("Descricao: " + descricao);
            System.out.println("Senha: " + senha);
            System.out.println("Cache: " + cache);
            System.out.println("Instrumento1: " + instrumento1);
            System.out.println("Instrumento2: " + instrumento2);
            System.out.println("Instrumento3: " + instrumento3);
            System.out.println("Objetivo: " + objetivo);
            System.out.println("Estilo: " + estilo);

            Musico musico = new Musico(0, nome, descricao, senha, cache, instrumento1, instrumento2, instrumento3, objetivo, estilo);
            System.out.println("Musico Object: " + musico); // Log the Musico object for debugging

            if (musicoDAO.insert(musico)) {
                res.status(201); // HTTP 201 Created
                return "Músico inserido com sucesso!";
            } else {
                res.status(500); // HTTP 500 Internal Server Error
                return "Erro ao inserir músico.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.status(500); // HTTP 500 Internal Server Error
            return "Erro ao inserir músico: " + e.getMessage();
        }
    }

    public Object get(Request req, Response res) {
        String idParam = req.queryParams("id");
        if (idParam != null) {
            int id = Integer.parseInt(idParam);
            Musico musico = musicoDAO.get(id);
            if (musico != null) {
                res.status(200); // HTTP 200 OK
                return musico;
            } else {
                res.status(404); // HTTP 404 Not Found
                return "Músico não encontrado.";
            }
        } else {
            List<Musico> musicos = musicoDAO.get();
            res.status(200); // HTTP 200 OK
            return musicos;
        }
    }

    public Object update(Request req, Response res) {
        int id = Integer.parseInt(req.queryParams("id"));
        String nome = req.queryParams("nome");
        String descricao = req.queryParams("descricao");
        String senha = req.queryParams("senha");
        float cache = Float.parseFloat(req.queryParams("cache"));
        String instrumento1 = req.queryParams("instrumento1");
        String instrumento2 = req.queryParams("instrumento2");
        String instrumento3 = req.queryParams("instrumento3");
        String objetivo = req.queryParams("objetivo");
        String estilo = req.queryParams("estilo");

        Musico musico = new Musico(id, nome, descricao, senha, cache, instrumento1, instrumento2, instrumento3, objetivo, estilo);

        if (musicoDAO.update(musico)) {
            res.status(200); // HTTP 200 OK
            return "Músico atualizado com sucesso!";
        } else {
            res.status(500); // HTTP 500 Internal Server Error
            return "Erro ao atualizar músico.";
        }
    }

    public Object delete(Request req, Response res) {
        int id = Integer.parseInt(req.queryParams("id"));

        if (musicoDAO.delete(id)) {
            res.status(200); // HTTP 200 OK
            return "Músico deletado com sucesso!";
        } else {
            res.status(500); // HTTP 500 Internal Server Error
            return "Erro ao deletar músico.";
        }
    }
}
