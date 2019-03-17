package json.transporter.jackson.model;

public class ComplexA {
    private String s;

    private A a;

    private FinalA finalA;

    public String getS() {
        return s;
    }

    public A getA() {
        return a;
    }

    public FinalA getFinalA() {
        return finalA;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final ComplexA complexA = (ComplexA) o;

        if (s != null ? !s.equals(complexA.s) : complexA.s != null) {
            return false;
        }
        if (a != null ? !a.equals(complexA.a) : complexA.a != null) {
            return false;
        }
        return finalA != null ? finalA.equals(complexA.finalA) : complexA.finalA == null;
    }

    @Override
    public int hashCode() {
        int result = s != null ? s.hashCode() : 0;
        result = 31 * result + (a != null ? a.hashCode() : 0);
        result = 31 * result + (finalA != null ? finalA.hashCode() : 0);
        return result;
    }
}
