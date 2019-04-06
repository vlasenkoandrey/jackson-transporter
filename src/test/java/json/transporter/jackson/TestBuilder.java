package json.transporter.jackson;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import json.transporter.builder.Builder;
import json.transporter.builder.CopyBuilderProvider;
import org.junit.Test;

import static json.transporter.jackson.OthersFieldInstrumentation.getOthersMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestBuilder {

    @Test
    public void test() throws IOException {
        ObjectMapper mapper = JsonTransporterMapper.builder().build();
        final M deserializedM = mapper.readValue(TestBuilder.class.getClassLoader().getResource("m.json"), M.class);
        final M newM = deserializedM.copyBuilder().withS2("newS2").build();
        assertEquals("s1", newM.getS1());
        assertEquals("newS2", newM.getS2());
        assertTrue(getOthersMap(newM).isPresent() && !getOthersMap(newM).get().isEmpty());
    }


    public static class M implements CopyBuilderProvider<MBuilder> {
        private final String s1;
        private final String s2;
        public String getS1() {
            return s1;
        }
        public String getS2() {
            return s2;
        }

        public M(@JsonProperty("s1") final String s1, @JsonProperty("s2") final String s2) {
            this.s1 = s1;
            this.s2 = s2;
        }

        @Override
        public MBuilder copyBuilder() {
            return MBuilder.newCopyBuilder(this);
        }
    }

    public static class M1 extends M {

        public M1(final String s1, final String s2) {
            super(s1, s2);
        }
    }

    public static class MBuilder implements Builder<M> {
        private String s1;
        private String s2;

        public static MBuilder newCopyBuilder(final M m) {
            return new MBuilder().withS1(m.getS1()).withS2(m.getS2());
        }

        public MBuilder withS1(final String s1) {
            this.s1 = s1;
            return this;
        }

        public MBuilder withS2(final String s2) {
            this.s2 = s2;
            return this;
        }

        @Override
        public M build() {
            return new M(s1, s2);
        }
    }
}
