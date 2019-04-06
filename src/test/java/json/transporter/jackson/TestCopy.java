package json.transporter.jackson;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import json.transporter.builder.CopyMethodProvider;
import org.junit.Test;

import static json.transporter.jackson.OthersFieldInstrumentation.getOthersMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestCopy {

    @Test
    public void test() throws IOException {
        ObjectMapper mapper = JsonTransporterMapper.builder().build();
        final M deserializedM = mapper.readValue(TestBuilder.class.getClassLoader().getResource("m.json"), M.class);
        final M newM = deserializedM.copy();
        assertEquals("s1", newM.getS1());
        assertEquals("s2", newM.getS2());
        assertTrue(getOthersMap(newM).isPresent() && !getOthersMap(newM).get().isEmpty());

    }

    public static class M implements CopyMethodProvider<M> {
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

        public M(M m) {
            this.s1 = m.s1;
            this.s2 = m.s2;
        }

        @Override
        public M copy() {
            return new M(s1, s2);
        }
    }
}
