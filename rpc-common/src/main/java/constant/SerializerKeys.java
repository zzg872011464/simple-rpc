package constant;

import lombok.Getter;

@Getter
public enum SerializerKeys {
    JDK("JDK"),
    JSON("JSON");

    private final String value;
    SerializerKeys(String value){
        this.value = value;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
