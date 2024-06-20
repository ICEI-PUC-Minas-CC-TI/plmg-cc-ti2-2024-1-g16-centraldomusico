package service;

import dao.MusicoDAO;
import model.Musico;
import spark.Request;
import spark.Response;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import app.Aplicacao;

import java.util.ArrayList;
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
            String telefone = null;
            byte[] profileImage = null;
    
            for (FileItem item : items) {
                if (item.isFormField()) {
                    String fieldName = item.getFieldName();
                    String fieldValue = item.getString("UTF-8");
                    switch (fieldName) {
                        case "nome":
                            nome = fieldValue;
                            if (musicoDAO.checkNomeMusico(nome)) {
                                res.status(400); // HTTP 400 Bad Request
                                return "{\"message\":\"Já existe um músico com o nome " + nome + ".\"}";
                            }
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
                        case "telefone":
                            telefone = fieldValue;
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
    
            Musico musico = new Musico(0, nome, descricao, senha, cache, instrumento1, instrumento2, instrumento3, objetivo, estilo, profileImage, telefone);
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

    //cadastrar instrumento e jogar na tabela 'verifica', a funcao vai receber via requisicao o id do musico, o nome do instrumento e a foto do instrumento
    public Object cadastrarInstrumento(Request req, Response res) {
        try {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            List<FileItem> items = upload.parseRequest(req.raw());
            //printar cada item
            for (FileItem item : items) {
                System.out.println("Item: " + item);
            }
            int id = 0;
            String instrumento = null;
            byte[] fotoInstrumento = null;
    
            for (FileItem item : items) {
                if (item.isFormField()) {
                    String fieldName = item.getFieldName();
                    String fieldValue = item.getString("UTF-8");
                    switch (fieldName) {
                        case "id":
                            id = Integer.parseInt(fieldValue);
                            break;
                        case "instrumento":
                            instrumento = fieldValue;
                            break;
                    }
                } else if ("foto".equals(item.getFieldName())) { // Ajustado para "fotoInstrumento"
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    InputStream inputStream = item.getInputStream();
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    fotoInstrumento = outputStream.toByteArray();
                    System.out.println("Foto Instrumento Length: " + fotoInstrumento.length);
                }
            }
    
            if (musicoDAO.cadastrarInstrumento(instrumento, fotoInstrumento,id )) {
                res.status(201); // HTTP 201 Created
                JsonObject responseJson = new JsonObject();
                responseJson.addProperty("message", "Instrumento inserido com sucesso!");
                return responseJson;
            } else {
                res.status(500); // HTTP 500 Internal Server Error
                JsonObject responseJson = new JsonObject();
                responseJson.addProperty("message", "Erro ao inserir instrumento.");
                return responseJson;
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.status(500); // HTTP 500 Internal Server Error
            return "Erro ao inserir instrumento: " + e.getMessage();
        }
    }
    //funcao para pegar os instrumentos do musico, vai mandar o id do musico e retornar uma lista de strings com os instrumentos
    public Object getInstrumentos(Request req, Response res) {
        int id = Integer.parseInt(req.queryParams("id"));
        List<String> instrumentos = musicoDAO.buscarInstrumentos(id);
        if (instrumentos != null) {
            System.out.println("Instrumentos encontrados: " + instrumentos);
            res.status(200); // HTTP 200 OK
            
            Gson gson = new Gson();
            String instrumentosJson = gson.toJson(instrumentos);
            
            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("instrumentos", instrumentosJson);
            
            return responseJson.toString();
        } else {
            System.out.println("Instrumentos não encontrados");
            res.status(404); // HTTP 404 Not Found
            return "Instrumentos não encontrados";
        }
    }
    //funcao para pegar as imagens dos instrumentos do musico, vai mandar o id do musico e retornar uma lista de imagens
    public Object getImagensInstrumentos(Request req, Response res) {
        int id = Integer.parseInt(req.queryParams("id"));
        List<byte[]> imagens = musicoDAO.buscarImagensInstrumentos(id);
        if (imagens != null) {
            System.out.println("Imagens encontradas: " + imagens);
            res.status(200); // HTTP 200 OK
            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("imagens", imagens.toString());
            return responseJson.toString();
        } else {
            System.out.println("Imagens não encontradas");
            res.status(404); // HTTP 404 Not Found
            return "Imagens não encontradas";
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
                    res.status(200);
    
                    // Carregar os detalhes da banda uma única vez
                    String bandaNome = musicoDAO.getBandaNomeByMusicoId(id);
                    int bandaId = musicoDAO.getBandaIdByMusicoId(id);
                    
                    responseJson.addProperty("id", musico.getId());
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
                    responseJson.addProperty("telefone", musico.getTelefone());
                    if (musico.getProfileImage() != null) {
                        String base64Image = Base64.getEncoder().encodeToString(musico.getProfileImage());
                        responseJson.addProperty("profileImage", base64Image);
                    }
                    return responseJson;
                } else {
                    System.out.println("Músico não encontrado.");
                    res.status(404);
                    return "Músico não encontrado.";
                }
            } catch (NumberFormatException e) {
                System.out.println("ID inválido.");
                res.status(400);
                return "ID inválido.";
            }
        } else {
            System.out.println("Parâmetro de ID ausente.");
            res.status(400);
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
                System.out.println(responseJson.toString());

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
            System.out.println("Update");
            // Verificar se a requisição é multipart (contém upload de arquivo)
            if (ServletFileUpload.isMultipartContent(req.raw())) {
                System.out.println("Multipart");
                DiskFileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(factory);
                List<FileItem> items = upload.parseRequest(req.raw());

                String nome = null;
                String descricao = null;
                String senha = null;
                float cache = 0;
                String instrumento1 = null;
                String instrumento2 = null;
                String instrumento3 = null;
                String objetivo = null;
                String estilo = null;
                byte[] fotoPerfil = null;
                int id = 0;

                for (FileItem item : items) {
                    if (item.isFormField()) {
                        switch (item.getFieldName()) {
                            case "id":
                                id = Integer.parseInt(item.getString());
                                break;
                            case "nome":
                                nome = item.getString("UTF-8");
                                break;
                            case "descricao":
                                descricao = item.getString("UTF-8");
                                break;
                            case "senha":
                                senha = item.getString("UTF-8");
                                break;
                            case "cache":
                                cache = Float.parseFloat(item.getString("UTF-8"));
                                break;
                            case "instrumento1":
                                instrumento1 = item.getString("UTF-8");
                                break;
                            case "instrumento2":
                                instrumento2 = item.getString("UTF-8");
                                break;
                            case "instrumento3":
                                instrumento3 = item.getString("UTF-8");
                                break;
                            case "objetivo":
                                objetivo = item.getString("UTF-8");
                                break;
                            case "estilo":
                                estilo = item.getString("UTF-8");
                                break;
                        }
                    } else if ("fotoPerfil".equals(item.getFieldName())) {
                        fotoPerfil = IOUtils.toByteArray(item.getInputStream());
                    }
                    System.out.println("Item: " + item.getFieldName() + " = " + item.getString());
                }
                System.out.println("ID: " + id);
                //pegar length da foto
                System.out.println("FotoPerfil Length: " + (fotoPerfil != null ? fotoPerfil.length : "null"));
                Musico musico = new Musico(id, nome, descricao, senha, cache, instrumento1, instrumento2, instrumento3, objetivo, estilo, fotoPerfil);

                if (fotoPerfil != null) {
                    musico.setProfileImage(fotoPerfil);
                }

                if (musicoDAO.update(musico)) {
                    res.status(200); // HTTP 200 OK
                    return "{\"message\":\"Músico atualizado com sucesso!\"}";
                } else {
                    res.status(500); // HTTP 500 Internal Server Error
                    return "{\"message\":\"Erro na atualização.\"}";
                }
            } else {
                System.out.println("Requisição não contém dados multipart.");
                //printar a requisição
                System.out.println("Requisição: " + req);
                //printar o corpo da requisição
                //printar o corpo da requisição em JSON
                //printar parametros da requisição
                System.out.println("Parametros: " + req.params());
                //printar o corpo da requisição
                //printar cada parametro da requisição
                System.out.println("Parametros: " + req.queryParams());


                res.status(400); // HTTP 400 Bad Request
                return "{\"message\":\"Requisição não contém dados multipart.\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro na atualização: " + e.getMessage());
            res.status(400); // HTTP 400 Bad Request
            return "{\"message\":\"Erro na atualização: " + e.getMessage() + "\"}";
        }
    }

    //funcao para deletar o musico, vai receber o id do musico, a senha e deletar o musico caso a senha esteja correta
    public Object delete(Request req, Response res) {
        String idParam = req.queryParams("id");
        String senhaParam = req.queryParams("senha");
        System.out.println("ID: " + idParam + ", Senha: " + senhaParam);

        if (idParam != null && senhaParam != null) {
            try {
                int id = Integer.parseInt(idParam);
                if (musicoDAO.delete(id, senhaParam)) {
                    res.status(200); // HTTP 200 OK
                    return "{\"message\":\"Músico deletado com sucesso!\"}";
                } else {
                    res.status(401); // HTTP 401 Unauthorized
                    return "{\"message\":\"Senha incorreta.\"}";
                }
            } catch (NumberFormatException e) {
                System.out.println("ID inválido.");
                res.status(400); // HTTP 400 Bad Request
                return "{\"message\":\"ID inválido.\"}";
            }
        } else {
            System.out.println("Parâmetros de ID e senha ausentes.");
            res.status(400); // HTTP 400 Bad Request
            return "{\"message\":\"Parâmetros de ID e senha ausentes.\"}";
        }
    }
}
