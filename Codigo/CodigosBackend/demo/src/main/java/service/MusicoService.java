package service;

import dao.MusicoDAO;
import model.Musico;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;

public class MusicoService {
    public MusicoDAO musicoDAO = new MusicoDAO();
    public MusicoService(){

    }
    public String insert(Request request,Response response){
        String str = request.body();
        Gson gson = new Gson();
        Musico musico = gson.fromJson(str, Musico.class);
        if(musicoDAO.insert(musico) == true){
            return "Músico registrado!";
        }else{
            return "Erro no cadastro!";
        }
    }

    public String delete(Request request, Response response){
        String str = request.body();
        Gson gson = new Gson();
        Musico musico = gson.fromJson(str, Musico.class);
        if(musicoDAO.delete(musico.getID()) == true){
            return "Músico deletado!";
        }else{
            return "Erro no delete!";
        }
    }
    public String update(Request request, Response response){
        String str = request.body();
        Gson gson = new Gson();
        Musico musico = gson.fromJson(str, Musico.class);
        if(musicoDAO.update(musico) == true){
            return "Músico atualizado!";
        }else{
            return "Erro na atualização!";
        }
    }
	public String get(Request request, Response response){
		String str = request.body();
		System.out.println(str);
		Gson gson = new Gson();
		Musico musico = gson.fromJson(str, Musico.class);

		if(musico.getID() == 0){
			return "Erro ao buscar usuário!";
		}
		else{
			musico = musicoDAO.get(musico.getID());
			return gson.toJson(musico);
		}
	}
}
