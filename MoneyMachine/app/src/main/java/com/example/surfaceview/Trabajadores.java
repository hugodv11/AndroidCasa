package com.example.surfaceview;

/**
 * Clase que engloba todos los atributos de los trabajadores
 * Contiene métodos con operaciones lógicas para calcular varios datos
 */
public class Trabajadores {
    /**
     * Numero de trabajadores
     */
    int numero;
    /**
     * Energia de los trabajadores
     */
    int energia;
    /**
     * Salud de los trabajadores
     */
    int salud;
    /**
     * Salario de los trabajadores
     */
    int salario;
    /**
     * Cada cuanto tiempo los trabajadores completan un ciclo
     */
    int eficiencia;
    /**
     * Cuanto dinero gana un trabajador por ciclo
     */
    int ganancias;
    /**
     * Cuanto ganan los traajadores sin tener en cuenta ninguna bonificación
     */
    int dineroBase;
    /**
     * Tiempo maximo en el que los trabajadores están activos
     */
    int tiempo;
    /**
     * Cuanta energia gastan los trabajadores por cada ciclo completado
     */
    int costeEnergia;
    /**
     * Ciclos que los empleados pueden completar
     */
    int ciclosDisponibles;
    /**
     * Ciclos que los empleados han completados
     */
    int ciclosCompletados;
    /**
     * Dinero total que han generado los trabajores en un ciclo
     */
    int dineroCiclo;
    /**
     * Indica si se tiene que mostrar un mensaje con los beneficios o no
     */
    boolean mensajeBeneficios;

    /**
     * Constructor de la clase
     */
    public Trabajadores() {
        ciclosCompletados = 0;
        ciclosDisponibles = 0;
        //Dinero que genera cada ciclo, se calcula multiplicando las ganancias
        //de cada trabajador por el numero de ellos.
        dineroCiclo = 0;
        mensajeBeneficios = false;
    }//end constructor

    /**
     * Calcula, dependiendo del valor base y de la salud de los trabajadores, cuanto dinero genera un empleado cada ciclo
     */
    public void gananciasTrabajador(){
        if(salud == 100){
            ganancias = dineroBase * 2;
        } else{
            //Si la salud está por debajo del 40% esto repercutirá de forma negativa
            if(salud <= 40){
                double calculo = salud / 100.0;
                ganancias = (int)(dineroBase * calculo);
            }//end if
            else{
                double calculo = (salud / 100.0) + 1;
                ganancias = (int)(dineroBase * calculo);
            }//end else
        }//end else
        dineroCiclo = ganancias * numero;
    }//end method gananciasTrabajador

    /**
     * Calcula cuantos ciclos han completado los trabajaodres y cuantos ciclos
     * @param diffTiempo diferencia de tiempo entre la ultima hora de conexión y la anterior
     */
    public void ciclosDisponibles(int diffTiempo){
        ciclosDisponibles = energia / costeEnergia;
        //Si tenemos como minimo un ciclo disponible procedemos con los calculos
        if(ciclosDisponibles > 0) {
            //Ahora comparamos el tiempo offline transcurrido y el tiempo maximo que los trabajadores
            //generan dinero, si el tiempo offline es mayor, solo tendremos en cuenta las horas
            //en la que los trabajadores aun estaban activos.
            if (diffTiempo > tiempo) {
                //numero de ciclos que los trabajadores han completado
                ciclosCompletados = tiempo / eficiencia;
            }//end if
            else {
                ciclosCompletados = diffTiempo / eficiencia;
            }//end else
            if(ciclosCompletados > ciclosDisponibles){
                ciclosCompletados = ciclosDisponibles;
            }//end if
        }//end if
    }//end method ciclosDisponibles

}//end class trabajadores
