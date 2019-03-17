package json.transporter.jackson.model;

public class A {
    private String s;

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

        final A a = (A) o;

        return s != null ? s.equals(a.s) : a.s == null;
    }

    @Override
    public int hashCode() {
        return s != null ? s.hashCode() : 0;
    }
}