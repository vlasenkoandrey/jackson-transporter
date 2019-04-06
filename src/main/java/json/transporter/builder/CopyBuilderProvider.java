package json.transporter.builder;

public interface CopyBuilderProvider<T extends Builder> {
    T copyBuilder();
}
