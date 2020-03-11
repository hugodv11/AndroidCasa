package com.example.surfaceview;

public class Trabajadores {
    //eficienciatbj es la variable que nos marca cada cuantos minutos nuestros trabajadores
    //ganan dinero(Completan un ciclo), empezara siendo cada hora, y cuanto mas lo mejores mas ira disminuyendo
    //dicha variable
    //Tiempo es cuando tiempo están trabajando, logicamente no van a estar 24h, es un valor que se podrá administrar
    //CosteEnergia es la energia que gastan por cada ciclo, que viene marcado por la eficiencia, si eficiencia es 30
    //y costeEnergia es 5, si pasa una hora los trabajadores habran completado dos ciclos y gastado 10 de energia
    int numero, energia, salud, salario, eficiencia, ganancias, dineroBase, tiempo, costeEnergia;
    int ciclosDisponibles, ciclosCompletados, dineroCiclo;
    boolean mensajeBeneficios;


    public Trabajadores() {
        ciclosCompletados = 0;
        ciclosDisponibles = 0;
        //Dinero que genera cada ciclo, se calcula multiplicando las ganancias
        //de cada trabajador por el numero de ellos.
        dineroCiclo = 0;
        mensajeBeneficios = false;
    }//end constructor



    //Metodo que calcula, dependiendo de un valor base y de la salud de los trabajadores,
    //cuando dinero genera un empleado en cada ciclo
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



    //Metodo que calcula cuantos ciclos han completado los trabajadores y cuantos ciclos
    //pueden hacer
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
