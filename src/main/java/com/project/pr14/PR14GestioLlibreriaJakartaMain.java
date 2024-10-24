package com.project.pr14;

import com.google.gson.JsonParser;
import jakarta.json.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.project.objectes.Llibre;

/**
 * Classe principal que gestiona la lectura i el processament de fitxers JSON per obtenir dades de llibres.
 */
public class PR14GestioLlibreriaJakartaMain {

    private final File dataFile;

    /**
     * Constructor de la classe PR14GestioLlibreriaJSONPMain.
     *
     * @param dataFile Fitxer on es troben els llibres.
     */
    public PR14GestioLlibreriaJakartaMain(File dataFile) {
        this.dataFile = dataFile;
    }

    public static void main(String[] args) {
        File dataFile = new File(System.getProperty("user.dir"), "data/pr14" + File.separator + "llibres_input.json");
        PR14GestioLlibreriaJakartaMain app = new PR14GestioLlibreriaJakartaMain(dataFile);
        app.processarFitxer();
    }

    /**
     * Processa el fitxer JSON per carregar, modificar, afegir, esborrar i guardar les dades dels llibres.
     */
    public void processarFitxer() {
        List<Llibre> llibres = carregarLlibres();
        if (llibres != null) {
            modificarAnyPublicacio(llibres, 1, 1995);
            afegirNouLlibre(llibres, new Llibre(4, "Històries de la ciutat", "Miquel Soler", 2022));
            esborrarLlibre(llibres, 2);
            guardarLlibres(llibres);
        }
    }

    /**
     * Carrega els llibres des del fitxer JSON.
     *
     * @return Llista de llibres o null si hi ha hagut un error en la lectura.
     */
    public List<Llibre> carregarLlibres() {
        List<Llibre> llistaLlibres = new ArrayList<>();
        try {
            if (dataFile.exists()){

                JsonReader jr = Json.createReader(new FileReader(dataFile));
                JsonArray ja = jr.readArray();
                for (JsonValue valor: ja){
                    JsonObject jo = (JsonObject) valor;
                    int id = jo.getInt("id");
                    String titol = jo.getString("titol");
                    String autor = jo.getString("autor");
                    int any = jo.getInt("any");
                    llistaLlibres.add(new Llibre(id, titol, autor, any));

                }

            }


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return llistaLlibres; // Substitueix pel teu
    }

    /**
     * Modifica l'any de publicació d'un llibre amb un id específic.
     *
     * @param llibres Llista de llibres.
     * @param id Identificador del llibre a modificar.
     * @param nouAny Nou any de publicació.
     */
    public void modificarAnyPublicacio(List<Llibre> llibres, int id, int nouAny) {
        boolean canvi=false;
        for (int i=0;i<llibres.size();i++){
            if (llibres.get(i).getId()==id){
                llibres.get(i).setAny(nouAny);
                canvi = true;
                break;
            }
        }
        if (canvi){
            System.out.println("S'ha modificat correctament");
        }else {
            System.out.println("No s'ha pogut trobar la id");
        }
    }

    /**
     * Afegeix un nou llibre a la llista de llibres.
     *
     * @param llibres Llista de llibres.
     * @param nouLlibre Nou llibre a afegir.
     */
    public void afegirNouLlibre(List<Llibre> llibres, Llibre nouLlibre) {

        llibres.add(nouLlibre);
        guardarLlibres(llibres);
    }

    /**
     * Esborra un llibre amb un id específic de la llista de llibres.
     *
     * @param llibres Llista de llibres.
     * @param id Identificador del llibre a esborrar.
     */
    public void esborrarLlibre(List<Llibre> llibres, int id) {
        boolean esborrat= false;
        for (int i =0;i<llibres.size();i++){
            if (llibres.get(i).getId()==id){
                llibres.remove(i);
                esborrat = true;
                break;
            }
        }
        if (esborrat){
            guardarLlibres(llibres);
            System.out.println("S'ha esborrat correctament");
        }else {
            System.out.println("No s'ha pogut esborrar");
        }

    }

    /**
     * Guarda la llista de llibres en un fitxer nou.
     *
     * @param llibres Llista de llibres a guardar.
     */
    public void guardarLlibres(List<Llibre> llibres) {
        JsonArrayBuilder jab = Json.createArrayBuilder();

        for (int i =0;i<llibres.size();i++){
            JsonObject llibre= Json.createObjectBuilder()
                    .add("id",llibres.get(i).getId())
                    .add("titol",llibres.get(i).getTitol())
                    .add("autor",llibres.get(i).getAutor())
                    .add("any",llibres.get(i).getAny())
                    .build();
            System.out.println(llibres.get(i).getId());
            System.out.println(llibres.get(i).getTitol());
            System.out.println(llibres.get(i).getAutor());
            System.out.println(llibres.get(i).getAny());

            jab.add(llibre);
        }

        JsonArray ja = jab.build();
        System.out.println(ja.get(0));
        try (JsonWriter jw = Json.createWriter(Files.newBufferedWriter(Paths.get(dataFile.getParent(), "llibres_output_jakarta.json")))) {
            jw.writeArray(ja);
            System.out.println("S'ha guardat el Json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}