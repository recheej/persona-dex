package com.example.rechee.persona5calculator.adapters;

import com.example.rechee.persona5calculator.models.PersonaStore;
import com.example.rechee.persona5calculator.models.RawPersonaEdge;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rechee on 10/3/2017.
 */

public class PersonaStoreGsonAdapter extends TypeAdapter<PersonaStore> {
    @Override
    public void write(JsonWriter out, PersonaStore value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        out.beginObject();

        out.name("edgesFrom");
        writeEdgesArray(out, value.edgesFrom());

        out.name("edgesTo");
        writeEdgesArray(out, value.edgesTo());

        out.endObject();
    }

    private void writeEdgesArray(JsonWriter out, RawPersonaEdge[] edges) throws IOException {
        out.beginArray();

        for (RawPersonaEdge edge : edges) {
            String output = edge.start + ";" + edge.pairPersona + ";" + edge.end;
            out.value(output);
        }

        out.endArray();
    }

    @Override
    public PersonaStore read(JsonReader in) throws IOException {
        PersonaStore store = new PersonaStore();

        in.beginObject();

        while(in.hasNext()){
            String name = in.nextName();

            if(name.equals("edgesFrom")){
                store.setEdgesFrom(this.getPersonaArray(in));
            }
            else if(name.equals("edgesTo")) {
                store.setEdgesTo(this.getPersonaArray(in));
            }
            else{
                in.skipValue();
            }
        }

        in.endObject();

        return store;
    }

    private RawPersonaEdge[] getPersonaArray(JsonReader reader) throws IOException {
        List<RawPersonaEdge> edges = new ArrayList<>();

        reader.beginArray();

        while(reader.hasNext()){
            RawPersonaEdge personaEdge = new RawPersonaEdge();

            String[] parts = reader.nextString().split(";");

            personaEdge.start = Integer.parseInt(parts[0]);
            personaEdge.pairPersona = Integer.parseInt(parts[1]);
            personaEdge.end = Integer.parseInt(parts[2]);

            edges.add(personaEdge);
        }
        reader.endArray();

        return edges.toArray(new RawPersonaEdge[edges.size()]);
    }
}
