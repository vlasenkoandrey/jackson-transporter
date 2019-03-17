package json.transporter.jackson.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FinalA {
    private final String s;

    public FinalA(@JsonProperty("s") final String s) {
        this.s = s;
    }

    public String getS() {
        return s;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final FinalA a = (FinalA) o;

        return s != null ? s.equals(a.s) : a.s == null;
    }

    @Override
    public int hashCode() {
        return s != null ? s.hashCode() : 0;
    }
}
