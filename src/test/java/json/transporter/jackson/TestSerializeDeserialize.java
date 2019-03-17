package json.transporter.jackson;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import json.transporter.jackson.builder.JsonTransporterMapper;
import json.transporter.jackson.model.A;
import json.transporter.jackson.model.ComplexA;
import json.transporter.jackson.model.FinalA;
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

    @Test
    public void testSerializeDeserializeImmutableCheckEquals() throws IOException {
        ObjectMapper mapper = JsonTransporterMapper.builder().build();
        FinalA r1 = mapper.readValue(TestSerializeDeserialize.class.getClassLoader().getResource("test.json"), FinalA.class);
        String w1 = mapper.writeValueAsString(r1);
        FinalA r2 = mapper.readValue(w1, FinalA.class);
        String w2 = mapper.writeValueAsString(r2);
        assertEquals(r1, r2);
        assertEquals(w1, w2);
    }

    @Test
    public void testSerializeDeserializeComplexCheckEquals() throws IOException {
        ObjectMapper mapper = JsonTransporterMapper.builder().build();
        ComplexA r1 = mapper.readValue(TestSerializeDeserialize.class.getClassLoader().getResource("complex_test.json"), ComplexA.class);
        String w1 = mapper.writeValueAsString(r1);
        ComplexA r2 = mapper.readValue(w1, ComplexA.class);
        String w2 = mapper.writeValueAsString(r2);
        assertEquals(r1, r2);
        assertEquals(w1, w2);
    }
}
