package service;

import dao.BandaDAO;
import model.Banda;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;

public class BandaService {
    public BandaDAO bandaDAO = new BandaDAO();
    public BandaService(){

    }
    public String insert(Request request,Response response){
        String str = request.body();
        Gson gson = new Gson();
        Banda banda = gson.fromJson(str, Banda.class);
        if(bandaDAO.insert(banda) == true){
            return "Banda registrada!";
        }else{
            return "Erro no cadastro!";
        }
    }

    public String delete(Request request, Response response){
        String str = request.body();
        Gson gson = new Gson();
        Banda banda = gson.fromJson(str, Banda.class);
        if(bandaDAO.delete(banda.getID()) == true){
            return "Banda deletada!";
        }else{
            return "Erro no delete!";
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
}
