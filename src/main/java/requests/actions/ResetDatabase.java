package requests.actions;

import core.Request;
import data.DBConnectionManager;
import org.apache.log4j.Logger;
import requests.annotations.Action;
import requests.annotations.RequestName;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestName("rdb")
@Action
public class ResetDatabase extends Request {
    private static Logger log = Logger.getLogger(ResetDatabase.class);

    public ResetDatabase() {
        super();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        DBConnectionManager.getInstance().getApplicationConnection().rebuildDatabase();

        log.info("Done.");
    }
}
