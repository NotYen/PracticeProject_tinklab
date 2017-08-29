package notyen.parkproject.api;

public class SmartApiData extends ApiData {
    final public static String SUCCESS = "0000";
    final public static String UPDATE = "0022";
    final public static String SUCCESS_CODE = "0";

    String offset;
    String limit;
    String count;
    String sort;
    String result;
    String data;

    public String getData() {
        return data;
    }

    public String getResults() {
        return result;
    }

    public String getOffset() {
        return offset;
    }

    public String getLimit() {
        return limit;
    }

    public String getCount() {
        return count;
    }

    public String getSort() {
        return sort;
    }
}
