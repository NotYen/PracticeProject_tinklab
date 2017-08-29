package notyen.parkproject.apidata;

import java.util.List;

import notyen.parkproject.api.SmartApiData;

public class OpenDataList extends SmartApiData {
    List<OpenData> results;

    public List<OpenData> getReturnData() {
        return results;
    }

}
