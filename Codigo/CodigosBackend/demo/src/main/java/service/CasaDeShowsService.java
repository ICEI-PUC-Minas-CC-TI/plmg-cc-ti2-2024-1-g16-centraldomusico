package service;

import dao.CasaDeShowsDAO;
import model.CasaDeShows;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;

public class CasaDeShowsService {
    public CasaDeShowsDAO casaDAO = new CasaDeShowsDAO();
    public CasaDeShowsService(){

    }
    public String insert(Request request,Response response){
        String str = request.body();
        Gson gson = new Gson();
        CasaDeShows casa = gson.fromJson(str, CasaDeShows.class);
        if(casaDAO.insert(casa) == true){
            return "Casa de Shows registrada!";
        }else{
            return "Erro no cadastro!";
        }
    }

    public String delete(Request request, Response response){
        String str = request.body();
        Gson gson = new Gson();
        CasaDeShows casa = gson.fromJson(str, CasaDeShows.class);
        if(casaDAO.delete(casa.getID()) == true){
            return "Casa de Shows deletada!";
        }else{
            return "Erro no delete!";
        }
    }
    public String update(Request request, Response response){
        String str = request.body();
        Gson gson = new Gson();
        CasaDeShows casa = gson.fromJson(str, CasaDeShows.class);
        if(casaDAO.update(casa) == true){
            return "Casa de Shows atualizada!";
        }else{
            return "Erro na atualização!";
        }
    }
	public String get(Request request, Response response){
		String str = request.body();
		System.out.println(str);
		Gson gson = new Gson();
		CasaDeShows casa = gson.fromJson(str, CasaDeShows.class);

		if(casa.getID() == 0){
			return "Erro ao buscar Casa de Shows!";
		}
		else{
			casa = casaDAO.get(casa.getID());
			return gson.toJson(casa);
		}
	}
}
