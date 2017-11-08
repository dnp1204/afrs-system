package AFRS.Requests;

import AFRS.AirportInfo.FAAAirportInfo;
import AFRS.AirportInfo.LocalAirportInfo;
import AFRS.FileHandler;

import java.util.ArrayList;

public class ServerRequest implements Request {
    private FileHandler fileHandler;

    public ServerRequest(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    @Override
    public ArrayList<String> doRequest(String clientID, String[] params) {
        ArrayList<String> result = new ArrayList<>();
        if (!params[0].equals("local") && !params[0].equals("faa")) {
            result.add("error, unknown information server");
        } else {
            result.add("server,successful");
            switch (params[0]) {
                case "local":
                    fileHandler.setAirportInfo(clientID, new LocalAirportInfo(fileHandler.getAirportMap()));
                    break;
                case "faa":
                    fileHandler.setAirportInfo(clientID, new FAAAirportInfo(new LocalAirportInfo(fileHandler.getAirportMap())));
                    break;
            }
        }
        return result;
    }
}
