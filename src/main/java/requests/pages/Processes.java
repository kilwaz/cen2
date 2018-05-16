package requests.pages;

import core.Request;
import core.process.ManagedThread;
import requests.annotations.JSP;
import requests.annotations.RequestName;
import utils.managers.ProcessManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequestName("processes")
@JSP("processes.jsp")
public class Processes extends Request {

    public Processes() {
        super();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        ProcessManager processManager = ProcessManager.getInstance();
        List<ManagedThread> managedThreadList = processManager.getRunningThreads();



        request.setAttribute("activeThreads", processManager.getActiveThreads());
    }
}
