package requests.pages;

import core.Request;
import requests.annotations.JSP;
import requests.annotations.RequestName;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestName("hello")
@JSP("hello.jsp")
public class Hello extends Request {

    public Hello() {
        super();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
//        request.setAttribute("test", "This is from the class");
    }
}
