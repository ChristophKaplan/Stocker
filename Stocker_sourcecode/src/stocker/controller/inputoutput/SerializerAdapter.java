package stocker.controller.inputoutput;

import java.lang.reflect.Type;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;


/**
 * Helps to serialize subtyped classes as frame profiles or indicators
 * @author Christoph Kaplan
 *
 * @param <T> the supertype class of the classes to serialze 
 */
public class SerializerAdapter<T> implements JsonSerializer<T>, JsonDeserializer<T>{

    private static final String typeAnnotation = "type";
    private static final String objectAnnotation  = "object";

    @Override
    public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {

        JsonObject retValue = new JsonObject();
        String className = src.getClass().getName();
        
        //System.out.println("-> "+ className);
        retValue.addProperty(typeAnnotation, className);
        JsonElement elem = context.serialize(src); 
        retValue.add(objectAnnotation, elem);
        return retValue;
    }

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException  {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonPrimitive prim = (JsonPrimitive) jsonObject.get(typeAnnotation);
        String className = prim.getAsString();

        Class<?> klass = null;
        try {
            klass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new JsonParseException(e.getMessage());
        }
        return context.deserialize(jsonObject.get(objectAnnotation), klass);
    }

}