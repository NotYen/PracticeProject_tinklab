package notyen.parkproject.apidata;

public class OpenData {
    String _id;
    String ParkName;
    String Name;
    String YearBuilt;
    String OpenTime;
    String Image;
    String Introduction;

    public String getId() {
        return _id;
    }

    public OpenData setId(String _id) {
        this._id = _id;
        return this;
    }

    public String getParkName() {
        return ParkName;
    }

    public OpenData setParkName(String parkName) {
        ParkName = parkName;
        return this;
    }

    public String getName() {
        return Name;
    }

    public OpenData setName(String name) {
        Name = name;
        return this;
    }

    public String getYearBuilt() {
        return YearBuilt;
    }

    public OpenData setYearBuilt(String yearBuilt) {
        YearBuilt = yearBuilt;
        return this;
    }

    public String getOpenTime() {
        return OpenTime;
    }

    public OpenData setOpenTime(String openTime) {
        OpenTime = openTime;
        return this;
    }

    public String getImage() {
        return Image;
    }

    public OpenData setImage(String image) {
        Image = image;
        return this;
    }

    public String getIntroduction() {
        return Introduction;
    }

    public OpenData setIntroduction(String introduction) {
        Introduction = introduction;
        return this;
    }
}
