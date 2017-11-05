package AFRS.Requests;

import AFRS.FileHandler;

import java.util.ArrayList;

public class ServiceRequest implements Request {
    FileHandler fileHandler;

    public ServiceRequest(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    @Override
    public ArrayList<String> doRequest(String[] params) {
        ArrayList<String> result = new ArrayList<>();
        if (params[0] != "local" && params[0] != "faa") {
            result.add("error, unknown information server");
        } else {
            result.add("server,successful");
        }
        return result;
    }
}
