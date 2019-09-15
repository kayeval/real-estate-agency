package main.model.Property;

public class Capacity {

    private int carSpaces;
    private int bedrooms;
    private int baths;

    public Capacity(int carSpaces, int bedrooms, int baths) {
        super();
        this.carSpaces = carSpaces;
        this.bedrooms = bedrooms;
        this.baths = baths;
    }

    public int getCarSpaces() {
        return carSpaces;
    }

    public void setCarSpaces(int carSpaces) {
        this.carSpaces = carSpaces;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(int bedrooms) {
        this.bedrooms = bedrooms;
    }

    public int getBaths() {
        return baths;
    }

    public void setBaths(int baths) {
        this.baths = baths;
    }

}
