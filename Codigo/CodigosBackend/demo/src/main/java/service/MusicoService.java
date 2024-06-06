package service;

import dao.MusicoDAO;
import model.Musico;
import spark.Request;
import spark.Response;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import app.Aplicacao;

import java.util.List;
import java.util.UUID;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
                JsonObject responseJson = new JsonObject();
                responseJson.addProperty("message", "Músico inserido com sucesso!");
                return responseJson;
            } else {
                res.status(500); // HTTP 500 Internal Server Error
                JsonObject responseJson = new JsonObject();
                responseJson.addProperty("message", "Erro ao inserir músico.");
                return responseJson;
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.status(500); // HTTP 500 Internal Server Error
            return "Erro ao inserir músico: " + e.getMessage();
        }
    }

    //funcao para checar se o musico tem uma banda interagindo com o DAO
    public Object checkBanda(Request req, Response res) {
        int id = Integer.parseInt(req.queryParams("id"));
        String bandaNome = musicoDAO.checkBanda(id);
        if (bandaNome != null) {
            System.out.println("Musico tem banda: " + bandaNome);
            res.status(200); // HTTP 200 OK
            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("nomeBanda", bandaNome);
            return responseJson.toString();
        } else {
            System.out.println("Musico não tem banda");
            res.status(404); // HTTP 404 Not Found
            return "Musico não tem banda";
        }
    }

    public Object getById(Request req, Response res) {
        String idParam = req.queryParams("id");
        System.out.println("ID: " + idParam);
        if (idParam != null) {
            try {
                int id = Integer.parseInt(idParam);
                Musico musico = musicoDAO.get(id);
                if (musico != null) {
                    System.out.println("Músico encontrado: " + musico.getNome());
                    JsonObject responseJson = new JsonObject();
                    responseJson.addProperty("message", "Músico encontrado: " + musico.getNome());
                    res.status(200); // HTTP 200 OK
                    //adicionar o id do usuario no JSON
                    String bandaNome = musicoDAO.getBandaNomeByMusicoId(id);
                    int bandaId = musicoDAO.getBandaIdByMusicoId(id);
                    responseJson.addProperty("id", musico.getId());
                    //adicionar tudo do usuario ao JSON
                    responseJson.addProperty("nome", musico.getNome());
                    responseJson.addProperty("descricao", musico.getDescricao());
                    responseJson.addProperty("cache", musico.getCache());
                    responseJson.addProperty("instrumento1", musico.getInstrumento1());
                    responseJson.addProperty("instrumento2", musico.getInstrumento2());
                    responseJson.addProperty("instrumento3", musico.getInstrumento3());
                    responseJson.addProperty("objetivo", musico.getObjetivo());
                    responseJson.addProperty("estilo", musico.getEstilo());
                    responseJson.addProperty("bandaNome", bandaNome);
                    responseJson.addProperty("bandaId", bandaId);
                    return responseJson;
                } else {
                    System.out.println("Músico não encontrado.");
                    res.status(404); // HTTP 404 Not Found
                    return "Músico não encontrado.";
                }
            } catch (NumberFormatException e) {
                System.out.println("ID inválido.");
                res.status(400); // HTTP 400 Bad Request
                return "ID inválido.";
            }
        } else {
            System.out.println("Parâmetro de ID ausente.");
            res.status(400); // HTTP 400 Bad Request
            return "Parâmetro de ID ausente.";
        }
    }
    

    public Object get(Request req, Response res) {
        String usuarioParam = req.queryParams("usuario");
        String senhaParam = req.queryParams("senha");
        System.out.println("Usuário: " + usuarioParam + ", Senha: " + senhaParam);
        if (usuarioParam != null && senhaParam != null) {
            Musico musico = musicoDAO.getByUsuarioSenha(usuarioParam, senhaParam);
            if (musico != null) {
                String token = generateSessionToken();
                Aplicacao.sessionDAO.saveSession(token, musico.getId());

                JsonObject responseJson = new JsonObject();
                responseJson.addProperty("message", "Usuário encontrado: " + musico.getNome());
                System.out.println("Usuário encontrado: " + musico.getNome());
                //adicionar o id do usuario no JSON
                responseJson.addProperty("id", musico.getId());
                //adicionar tudo do usuario ao JSON
                responseJson.addProperty("nome", musico.getNome());
                responseJson.addProperty("descricao", musico.getDescricao());
                responseJson.addProperty("cache", musico.getCache());
                responseJson.addProperty("instrumento1", musico.getInstrumento1());
                responseJson.addProperty("instrumento2", musico.getInstrumento2());
                responseJson.addProperty("instrumento3", musico.getInstrumento3());
                responseJson.addProperty("objetivo", musico.getObjetivo());
                responseJson.addProperty("estilo", musico.getEstilo());
                responseJson.addProperty("token", token);
                responseJson.addProperty("secret", musico.getSenha());

                res.status(200); // HTTP 200 OK
                return responseJson;
            } else {
                JsonObject responseJson = new JsonObject();
                responseJson.addProperty("message", "Usuário ou senha incorretos.");
                System.out.println("Usuário ou senha incorretos.");
                res.status(404); // HTTP 404 Not Found
                return responseJson;
            }
        } else {
            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("message", "Parâmetros de usuário e senha ausentes.");
            System.out.println("Parâmetros de usuário e senha ausentes.");
            res.status(400); // HTTP 400 Bad Request
            return responseJson;
        }
    }
    
    private String generateSessionToken() {
    // Implementar a geração de token de sessão
    return UUID.randomUUID().toString();
    }   

    public Object update(Request req, Response res) {
        try {
            System.out.println("Request Body: " + req.body());
            int id = Integer.parseInt(req.queryParams("id"));
            // Parse the JSON body to get the parameters
            JsonObject json = JsonParser.parseString(req.body()).getAsJsonObject();
            //printar o json
            System.out.println("Json: " + json);
            String nome = json.get("nome").getAsString();
            String descricao = json.get("descricao").getAsString();
            String senha = json.get("senha").getAsString();
            String cacheStr = json.get("cache").getAsString();
            float cache = json.get("cache").getAsFloat();
            String instrumento1 = json.get("instrumento1").getAsString();
            String instrumento2 = json.get("instrumento2").getAsString();
            String instrumento3 = json.get("instrumento3").getAsString();
            String objetivo = json.get("objetivo").getAsString();
            String estilo = json.get("estilo").getAsString();
            System.out.println("ID: " + id);
            System.out.println("Nome: " + nome);
            System.out.println("Descricao: " + descricao);
            System.out.println("Senha: " + senha);
            System.out.println("Cache: " + cache);
            System.out.println("Instrumento1: " + instrumento1);
            System.out.println("Instrumento2: " + instrumento2);
            System.out.println("Instrumento3: " + instrumento3);
            System.out.println("Objetivo: " + objetivo);
            System.out.println("Estilo: " + estilo);
            
            Musico musico = new Musico(id, nome, descricao, senha, cache, instrumento1, instrumento2, instrumento3, objetivo, estilo);

            if (musicoDAO.update(musico)) {
                JsonObject responseJson = new JsonObject();
                responseJson.addProperty("message", "Músico atualizado com sucesso!");
                res.status(200); // HTTP 200 OK
                return responseJson;
            } else {
                JsonObject responseJson = new JsonObject();
                responseJson.addProperty("message", "Erro na atualização.");
                res.status(500); // HTTP 500 Internal Server Error
                return responseJson;
            }
        } catch (Exception e) {
            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("message", "Erro na atualização: " + e.getMessage());
            res.status(400); // HTTP 400 Bad Request
            System.out.println("Erro na atualização: " + e.getMessage());
            return responseJson;
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
