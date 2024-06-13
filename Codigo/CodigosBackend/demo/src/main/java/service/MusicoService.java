package service;

import dao.MusicoDAO;
import model.Musico;
import spark.Request;
import spark.Response;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import app.Aplicacao;
import java.util.Base64;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

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
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            List<FileItem> items = upload.parseRequest(req.raw());
            //printar cada item
            for (FileItem item : items) {
                System.out.println("Item: " + item);
            }
            String nome = null;
            String descricao = null;
            String senha = null;
            float cache = 0;
            String instrumento1 = null;
            String instrumento2 = null;
            String instrumento3 = null;
            String objetivo = null;
            String estilo = null;
            byte[] profileImage = null;
    
            for (FileItem item : items) {
                if (item.isFormField()) {
                    String fieldName = item.getFieldName();
                    String fieldValue = item.getString("UTF-8");
                    switch (fieldName) {
                        case "nome":
                            nome = fieldValue;
                            break;
                        case "descricao":
                            descricao = fieldValue;
                            break;
                        case "senha":
                            senha = fieldValue;
                            break;
                        case "cache":
                            cache = Float.parseFloat(fieldValue);
                            break;
                        case "instrumento1":
                            instrumento1 = fieldValue;
                            break;
                        case "instrumento2":
                            instrumento2 = fieldValue;
                            break;
                        case "instrumento3":
                            instrumento3 = fieldValue;
                            break;
                        case "objetivo":
                            objetivo = fieldValue;
                            break;
                        case "estilo":
                            estilo = fieldValue;
                            break;
                    }
                } else if ("fotoPerfil".equals(item.getFieldName())) { // Ajustado para "fotoPerfil"
                    //printar o item
                    System.out.println("Item--: " + item);
                    //printar a foto
                    System.out.println("Foto: " + item.getName());
                    //printar bytes
                    System.out.println("Bytes: " + item.get());
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    InputStream inputStream = item.getInputStream();
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        System.out.println("BytesRead: " + bytesRead);
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    profileImage = outputStream.toByteArray();
                    System.out.println("Profile Image Length: " + profileImage.length);
                }
            }
    
            Musico musico = new Musico(0, nome, descricao, senha, cache, instrumento1, instrumento2, instrumento3, objetivo, estilo, profileImage);
            System.out.println("Profile Image Length: " + (profileImage != null ? profileImage.length : "null"));

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
                    //adicionar a imagem do usuario ao JSON
                    if (musico.getProfileImage() != null) {
                        String base64Image = Base64.getEncoder().encodeToString(musico.getProfileImage());
                        responseJson.addProperty("profileImage", base64Image);
                    }
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
                // Adicionar o id do usuário no JSON
                responseJson.addProperty("id", musico.getId());
                // Adicionar todas as informações do usuário ao JSON
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
            //pegar a imagem
            byte[] fotoPerfil = null;
            if (json.has("fotoPerfil")) {
                String base64Image = json.get("fotoPerfil").getAsString();
                fotoPerfil = Base64.getDecoder().decode(base64Image);
            }
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
            System.out.println("FotoPerfil: " + (fotoPerfil != null ? fotoPerfil.length : "null"));
            
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
