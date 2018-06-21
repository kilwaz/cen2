package requests.pages;

import core.Request;
import requests.annotations.JSP;
import requests.annotations.RequestName;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestName("people")
@JSP("people.jsp")
public class People extends Request {

    public People() {
        super();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) {

    }
}
