package requests.pages;

import core.Request;
import data.model.dao.SourceDAO;
import data.model.objects.Source;
import requests.annotations.JSP;
import requests.annotations.RequestName;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequestName("dba")
@JSP("databaseAdmin.jsp")
public class DatabaseAdmin extends Request {
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        SourceDAO sourceDAO = new SourceDAO();
        List<Source> sources = sourceDAO.getSources();

        request.setAttribute("sources", sources);
    }
}
