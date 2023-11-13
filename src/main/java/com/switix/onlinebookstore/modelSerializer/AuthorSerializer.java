package com.switix.onlinebookstore.modelSerializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.switix.onlinebookstore.model.Author;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;


public class AuthorSerializer extends JsonSerializer<Author> {

    @Override
    public void serialize(Author author, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String requestURL = request.getRequestURI();
        if (requestURL.contains("api/books") && request.getParameter("author") != null) {
            // Conditionally serialize the full Author object if the URL condition is met
            //serializers.defaultSerializeValue(author, gen);
            gen.writeStartObject();
            gen.writeStringField("name", author.getName());

            // Write the 'authorBooks' array field
            gen.writeFieldName("authorBooks");
            gen.writeStartArray();

            // Serialize each book in the array
            author.getAuthorBooks().forEach(book -> {
                try {
                    gen.writeStartObject();
                    gen.writeStringField("title", book.getTitle());
                    gen.writeStringField("description", book.getDescription());
                    gen.writeObjectField("price", book.getPrice());
                    gen.writeObjectField("category", book.getCategory());
                    gen.writeObjectField("inventory", book.getInventory());
                    gen.writeEndObject();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            gen.writeEndArray(); // End the 'authorBooks' array
            gen.writeEndObject(); // End the main object
        } else {
            // Serialize only the "name" property otherwise
            gen.writeStartObject();
            gen.writeFieldName("id");
            gen.writeNumber(author.getId());
            gen.writeStringField("name", author.getName());
            gen.writeEndObject();
        }
    }
}
