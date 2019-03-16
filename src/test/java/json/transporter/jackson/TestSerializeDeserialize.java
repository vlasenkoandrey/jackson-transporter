package json.transporter.jackson;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import json.transporter.jackson.builder.JsonTransporterMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestSerializeDeserialize {
    @Test
    public void testSerializeDeserializeCheckEquals() throws IOException {
        ObjectMapper mapper = JsonTransporterMapper.builder().build();
        A r1 = mapper.readValue(TestSerializeDeserialize.class.getClassLoader().getResource("test.json"), A.class);
        String w1 = mapper.writeValueAsString(r1);
        A r2 = mapper.readValue(w1, A.class);
        String w2 = mapper.writeValueAsString(r2);
        assertEquals(r1, r2);
        assertEquals(w1, w2);
    }

    public static class A {
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
}
