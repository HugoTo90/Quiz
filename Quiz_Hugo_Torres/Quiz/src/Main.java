import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        int numeroDePreguntas, random, vueltaError, vueltaAcierto;
        int aciertos = 0, vuelta = 0, aciertosConsecutivos = 0, errores = 0;
        boolean respuestaUsuario, respuestaPrograma, acierto;
        int[] numerosAnteriores;
        int preguntas;
        int contadorErrores = 0;
        String nombre;
        boolean salir = false;
        ArrayList<String> preguntasBB = new ArrayList<>();
        ArrayList<Boolean> respuestasBB = new ArrayList<>();
        ArrayList<String> preguntasSW = new ArrayList<>();
        ArrayList<Boolean> respuestasSW = new ArrayList<>();
        ArrayList<String> arrayPreguntas = new ArrayList<>();
        ArrayList<Boolean> arrayRespuestas;

        nombre = pedirNombre();
        iniciarPreguntasRespuestasBB(preguntasBB, respuestasBB);
        iniciarPreguntasRespuestasSW(preguntasSW, respuestasSW);
        preguntas = seleccionarTipoDePreguntas();
        arrayPreguntas = seleccionarPreguntas(preguntas, preguntasBB, preguntasSW, arrayPreguntas);
        arrayRespuestas = seleccionarRespuestas(preguntas, respuestasBB, respuestasSW);
        numeroDePreguntas = preguntarNumeroDePreguntas();
        numerosAnteriores = new int[numeroDePreguntas];

            do {
                vueltaError = vuelta;
                vueltaAcierto = vuelta;
                random = numeroAleatorio(numerosAnteriores, vuelta);
                respuestaUsuario = seleccionDePregunta(arrayPreguntas, random);
                respuestaPrograma = seleccionDeRespuesta(arrayRespuestas, random);
                acierto = compararRespuesta(respuestaUsuario, respuestaPrograma);
                aciertos = sumarAciertos(acierto, aciertos);
                errores = comprobarErroresConsecutivos(acierto, errores, vuelta, vueltaError, nombre, aciertos, contadorErrores, preguntas);
                salir = comprobarSalirPorErrores(errores);
                if (!salir){
                    contadorErrores = contarErrores(acierto, contadorErrores);
                    aciertosConsecutivos = comprobarAciertosConsecutivos(acierto, vuelta, vueltaAcierto, aciertosConsecutivos);
                    aciertos = comprobarBonus(aciertos, aciertosConsecutivos);
                    vuelta = vuelta + 1;
                    salir = comprobarFinal(vuelta, numeroDePreguntas);
                }
            }while (!salir);
        comprobarPorcentajeAciertos(aciertos, numeroDePreguntas);
        guardarDatos(nombre, aciertos, contadorErrores, preguntas);
    }

    private static boolean comprobarFinal(int vuelta, int numeroDePreguntas) {
        if (vuelta == numeroDePreguntas){
            return true;
        }
        return false;
    }

    private static boolean comprobarSalirPorErrores(int errores) {
        if (errores == -1){
            return true;
        }
        return false;
    }

    private static int contarErrores(boolean acierto, int contadorErrores) {
        if (!acierto){
            contadorErrores++;
        }
        return contadorErrores;
    }

    private static void guardarDatos(String nombre, int aciertos, int contadorErrores, int preguntas ) {
        String newfile1 = "Quiz/src/resources/resultadosBB.txt";
        String newfile2 = "Quiz/src/resources/resultadosSW.txt";
        BufferedWriter bw;

        if (preguntas == 1){
            try {
                bw = new BufferedWriter(new FileWriter(newfile1));
                bw.write("Nombre: " + nombre);
                bw.write(", Aciertos: " + aciertos);
                bw.write(", Errores: " + contadorErrores);
                bw.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                bw = new BufferedWriter(new FileWriter(newfile2));
                bw.write("Nombre: " + nombre);
                bw.write(", Aciertos: " + aciertos);
                bw.write(", Errores: " + contadorErrores);
                bw.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static String pedirNombre() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Escribe tu nombre:");
        return sc.nextLine();
    }


    private static void iniciarPreguntasRespuestasSW(ArrayList<String> preguntasSW, ArrayList<Boolean> respuestasSW) {
        BufferedReader br;
        String line;
        String fileName = "Quiz/src/resources/breakingBad.txt";
        String[] datos;

        try{
            br = new BufferedReader(new FileReader(fileName));

            while ((line = br.readLine()) != null) {
                datos = line.split(";");
                preguntasSW.add(datos[0]);
                respuestasSW.add(Boolean.valueOf(datos[1]));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void iniciarPreguntasRespuestasBB(ArrayList<String> preguntasBB, ArrayList<Boolean> respuestasBB) {
        BufferedReader br;
        String line;
        String fileName = "Quiz/src/resources/breakingBad.txt";
        String[] datos;

        try{
            br = new BufferedReader(new FileReader(fileName));

            while ((line = br.readLine()) != null) {
                datos = line.split(";");
                preguntasBB.add(datos[0]);
                respuestasBB.add(Boolean.valueOf(datos[1]));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static ArrayList<Boolean> seleccionarRespuestas(int Preguntas, ArrayList<Boolean> BreakingBad, ArrayList<Boolean> StarWars) {
        ArrayList<Boolean> ArrayRespuestas = new ArrayList<>();
        if (Preguntas == 1) {
            ArrayRespuestas = BreakingBad;
        } else if (Preguntas == 2) {
            ArrayRespuestas = StarWars;
        }

        return ArrayRespuestas;

    }


    private static ArrayList<String> seleccionarPreguntas(int preguntas, ArrayList<String> BreakingBad, ArrayList<String> StarWars, ArrayList<String> ArrayPreguntas) {
        if (preguntas == 1) {
            ArrayPreguntas = BreakingBad;
        } else if (preguntas == 2) {
            ArrayPreguntas = StarWars;
        }
        return ArrayPreguntas;
    }

    private static int seleccionarTipoDePreguntas() {
        Scanner sc = new Scanner(System.in);
        String respuesta;
        int resultado = 0;
        boolean salir = false;
        System.out.println("Escoge el tema de juego:\n1-Breaking Bad\n2-Star Wars");
        do {
            respuesta = sc.nextLine();
            try {
                resultado = Integer.parseInt(respuesta);
                if (resultado >= 1 && resultado <= 2){
                    salir = true;
                }
            } catch (Exception e) {
            }
            System.out.println("Escribe un numero entre 1 y 2");
        }while(!salir);
        return resultado;
    }

    private static int comprobarBonus(int Aciertos, int AciertosConsecutivos) {
        if (AciertosConsecutivos == 5) {
            Aciertos = Aciertos + 1;
            System.out.println("Bonus: +1");

        }
        return Aciertos;
    }

    private static int comprobarAciertosConsecutivos(boolean Acierto, int Vuelta, int VueltaAcierto, int AciertosConsecutivos) {
        if (Acierto) {
            if (Vuelta == VueltaAcierto) {
                AciertosConsecutivos++;
            } else {
                AciertosConsecutivos = 0;
            }
        } else {
            AciertosConsecutivos = 0;
        }
        return AciertosConsecutivos;
    }

    private static int comprobarErroresConsecutivos(boolean Acierto, int Errores, int Vuelta, int VueltaError, String nombre, int aciertos, int errores, int preguntas) {
        if (!Acierto) {
            if (Vuelta == VueltaError) {
                Errores++;
            } else {
                Errores = 1;
            }
        } else {
            Errores = 0;
        }

        if (Errores == 3) {
            guardarDatos(nombre, aciertos, errores, preguntas);
            System.out.println("GAME OVER");
            Errores = -1;
        }

        return Errores;
    }

    private static void comprobarPorcentajeAciertos(int aciertos, int numeroDePreguntas) {
        double porcentaje = ((double) aciertos / numeroDePreguntas) * 100;

        if (0 <= porcentaje && porcentaje <= 33) {
            System.out.println("El usuario respondió correctamente del 0% al 33% de las preguntas.");
        } else if (34 <= porcentaje && porcentaje <= 66) {
            System.out.println("El usuario respondió correctamente entre un 34% y un 66% de las preguntas.");
        } else if (67 <= porcentaje && porcentaje <= 99) {
            System.out.println("El usuario respondió correctamente del 67% al 99% de las preguntas.");
        } else if (porcentaje >= 100) {
            System.out.println("El usuario respondió correctamente el 100% de las preguntas.");
        }
    }

    private static int sumarAciertos(boolean acierto, int aciertos) {
        if (acierto) {
            aciertos = aciertos + 1;
        }
        return aciertos;
    }

    private static boolean compararRespuesta(boolean respuestausuario, boolean respuestaprograma) {
        boolean acierto = false;
        if (respuestaprograma == respuestausuario) {
            acierto = true;
        }
        return acierto;
    }

    private static boolean seleccionDeRespuesta(ArrayList<Boolean> ArrayRespuestas, int Random) {
        return ArrayRespuestas.get(Random);
    }

    private static int numeroAleatorio(int[] numerosanteriores, int vuelta) {

        int numero;

        do {
            numero = (int) (Math.random() * 20);
        } while (compararNumerosAnteriores(numero, numerosanteriores, vuelta));

        numerosanteriores[vuelta] = numero;

        return numero;
    }

    private static boolean compararNumerosAnteriores(int numero, int[] numerosanteriores, int vuelta) {
        for (int i = 0; i < vuelta; i++) {
            if (numerosanteriores[i] == numero) {
                return true;
            }
        }
        return false;
    }

    private static boolean seleccionDePregunta(ArrayList<String> ArrayPreguntas, int Random) {
        Scanner sc = new Scanner(System.in);
        boolean Resultado = false;
        boolean Salir;
        String Respuesta;
        do {
            System.out.println(ArrayPreguntas.get(Random));
            Respuesta = sc.nextLine();
            if (Respuesta.equalsIgnoreCase("True") || Respuesta.equalsIgnoreCase("Cert") ||
                    Respuesta.equalsIgnoreCase("Cierto") || Respuesta.equalsIgnoreCase("Yes") ||
                    Respuesta.equalsIgnoreCase("Si") || Respuesta.equalsIgnoreCase("Verdadero")) {
                Resultado = true;
                Salir = true;
            } else if (Respuesta.equalsIgnoreCase("False") || Respuesta.equalsIgnoreCase("Fals") ||
                    Respuesta.equalsIgnoreCase("Falso") || Respuesta.equalsIgnoreCase("No")) {
                Salir = true;
            } else {
                System.out.println("Por favor, escribe si es verdadero o falso.");
                Salir = false;
            }
        } while (!Salir);
        return Resultado;
    }

    private static int preguntarNumeroDePreguntas() {
        Scanner sc = new Scanner(System.in);
        String numeroDePreguntas;
        boolean salir = false;

        do {
            System.out.println("Cuantas preguntas quieres responder?\n(5-20):");
            numeroDePreguntas = sc.nextLine();

            try {
                int preguntas = Integer.parseInt(numeroDePreguntas);
                if (preguntas >= 5 && preguntas <= 20) {
                    salir = true;
                } else {
                    System.out.println("Escribe un número entre 5 y 20.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingresa un número válido.");
            }
        } while (!salir);

        return Integer.parseInt(numeroDePreguntas);
    }


}