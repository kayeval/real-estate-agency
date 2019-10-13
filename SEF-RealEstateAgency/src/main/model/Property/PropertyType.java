package main.model.Property;

public enum PropertyType {
    HOUSE("House"),
    UNIT("Unit"),
    FLAT("Flat"),
    TOWNHOUSE("Townhouse"),
    STUDIO("Studio");

    private String label;

    PropertyType(String label) {
        this.label = label;
    }

    public String toString() {
        return label;
    }
}
