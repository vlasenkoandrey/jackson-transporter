package json.transporter.jackson.deserialize;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;

import static json.transporter.jackson.OthersFieldUtils.getOthersMap;

public class CustomDeserializationProblemsHandler extends DeserializationProblemHandler {
    @Override
    public boolean handleUnknownProperty(DeserializationContext ctxt, JsonParser p, JsonDeserializer<?> deserializer,
            Object beanOrClass, String propertyName) throws IOException {
        String text = p.getText();
        TreeNode n = p.readValueAsTree();
        getOthersMap(beanOrClass).ifPresent(others -> {
            try {
                others.put(p.currentName(), n.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return true;
    }

}
