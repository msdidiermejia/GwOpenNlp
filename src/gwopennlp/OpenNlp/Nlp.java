
package gwopennlp.OpenNlp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class Nlp  {
    
    public Nlp() {
    }
    
    /**
     *
     * @param rx
     * @return Retorna un String con la posibilidad de ser voz activa o pasiva
     * @throws FileNotFoundException
     * @throws IOException
     */
    public String validar(String rx) throws FileNotFoundException, IOException{
        
        InputStream tokenModelIn = null;
        InputStream posModelIn = null;
        boolean bandera1 = false;
        boolean bandera2 = false;
        boolean bandera3 = false;
        boolean bandera4 = false;
        /**
         * tokenizar la frase de entrada.
         * Tokenizar significa separar la frase por cada palabra para el pocesamiento de la misma.
         * */
        tokenModelIn = new FileInputStream("C:\\Users\\DidierPC\\Documents\\NetBeansProjects\\GwOpenNlp\\src\\en-token.bin");
        TokenizerModel tokenModel = new TokenizerModel(tokenModelIn);
        Tokenizer tokenizer = new TokenizerME(tokenModel);
        String tokens[] = tokenizer.tokenize(rx);
        
        for (String token : tokens) {
            System.out.println(token);
        }
        
        // Parts-Of-Speech Tagging
        // Lee el modelo de las partes de la frase.
        posModelIn = new FileInputStream("C:\\Users\\DidierPC\\Documents\\NetBeansProjects\\GwOpenNlp\\src\\en-pos-maxent.bin");
        // loading the parts-of-speech model from stream
        POSModel posModel = new POSModel(posModelIn);
        // initializing the parts-of-speech tagger with model
        POSTaggerME posTagger = new POSTaggerME(posModel);
        // Tagger tagging the tokens
        String tags[] = posTagger.tag(tokens);
        // Getting the probabilities of the tags given to the tokens
        double probs[] = posTagger.probs();
        
        System.out.println("Token\t:\tTag\t:\tProbability\n---------------------------------------------");
        for(int i=0;i<tokens.length;i++){
            System.out.println(tokens[i]+"\t:\t"+tags[i]+"\t:\t"+probs[i]);
        }
        for (int i = 0; i < tags.length; i++) {
            // Si lo primero que se encuentra es un sustantivo o un pronombre, hay probabilidades de ser voz activa.
            bandera1 = (tags[i].equals("PRP")  || tags[i].equals("NN")|| tags[i].equals("NNS") || (tags[i].equals("NNP") && tags[i+1].equals("VBD"))&& (probs[i]>=0.84) );
            // Si lo primero que encuentra es un verbo, puede que se trate de una voz pasiva.
            bandera2 = ((tags[i].equals("VBP") || tags[i].equals("VBZ") || tags[i].equals("VBD") || tags[i].equals("VBN")
                    ||tags[i].equals("VB") || tags[i].equals("PRP$") )&& (probs[i]>=0.84)
                    );
            if (bandera1 ) {
                // System.out.println("Probablemente activa");
                break;
            }
            if (bandera2 ) {
                // System.out.println("Probablemente pasiva");
                break;
            }
        }
        // Si es un pronombre propio o singular y le sigue un verbo en 3ra persona. pasivo
        for (int i = 0; i < tags.length; i++) {
            bandera3 =  (tags[i].equals("NNP") && tags[i+1].equals("VBZ") &&(probs[i]>=0.84)  && (probs[i+1]>=0.84) );
            if(bandera3){
                //System.out.println("Probablemente pasiva");
                break;
            }
        }
        // Si empieza con un determinante y luego un sustantivo... Pasivo.
        bandera4 = (tags[0].equals("DT") &&tags[1].equals("NN"));
        
        //Puede que sea voz activa...
        boolean rAux1 = bandera1 && !bandera4 && !bandera3;
        // Puede que sea pasiva...
        boolean rAux2 = bandera4|| bandera3 || bandera2;
        
        if(rAux1 && !rAux2){
            return "Voz activa";
        }
        else if( !rAux1 && rAux2){
            return "Voz pasiva";
        }
        else{
            return "No es posible determinar el tipo de voz ";
        }
        
    }
    /**
     * @return Retorna lista de String con frases tanto de voz activa como de voz pasiva.
     * @throws FileNotFoundException
     * @throws IOException
     */
    public List<String> leerCsv(){
        String rutaCsv = "C:\\Users\\DidierPC\\Documents\\NetBeansProjects\\GwOpenNlp\\src\\Libro1.csv";
        BufferedReader br = null;
        String line = "";
        //String cvsSplitBy = ",";
        //String[] prueba = {"","",""};
        // use comma as separator
        //String[] country = line.split(cvsSplitBy);
        List<String> sentenceList = new ArrayList<>();
        try {
            
            br = new BufferedReader(new FileReader(rutaCsv));
            while ((line = br.readLine()) != null) {
                sentenceList.add(line);
                System.out.println(line);
                
            }
        } catch (FileNotFoundException e) {
            System.out.println("No se  encontro el archivo "+ e.toString());
        } catch (IOException e) {
            System.out.println("Error tipo IO "+ e.toString());
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.out.println("Error al cerrar el archivo "+ e.toString());
                }
            }
        }
        return sentenceList;
    }
    
    /**
     *
     */
    public String contarTiposVoz(List<String> listaSentence) throws IOException{
        int contPasive = 0;
        int contActivo = 0;
        int conX = 0;
        for (String string : listaSentence) {
            String resultadoValidacion = validar(string);
            
            if (resultadoValidacion.equals("Voz activa"))
                contActivo++;
            else if(resultadoValidacion.equals("Voz pasiva"))
                contPasive++;
            else{
                conX++;
            }
        }
           System.out.println("Activos "+contActivo+" Pasivos "+contPasive+ " otros "+conX);
           return "Voice active"+","+contActivo+","+"Voice pasive"+","+contActivo+"," +"N.I"+","+conX; 
    }
   
}
