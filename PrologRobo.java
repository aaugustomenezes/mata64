package mata64;

import robocode.Robot;
import robocode.ScannedRobotEvent;
import org.jpl7.Query;
import org.jpl7.Term;

//public class PrologRobo extends Robot {

   /*
    public void run() {

        if (!Query.hasSolution("consult('test.pl').")) {
            System.out.println("Consult failed");
        }

        while (true) {
            ahead(80);
            fire(1.0);
        }
    }

            public void onScannedRobot (ScannedRobotEvent event) {
                super.onScannedRobot(event);

                Query q = new Query("atacar", new Term[]{
                        new org.jpl7.Float(event.getDistance())
                });
                if(q.hasSolution()){
                    ahead(100);
                    fire(3);
                }

                Query b = new Query("acelerar", new Term[]{
                        new org.jpl7.Float(event.getDistance()),
                        new org.jpl7.Float(event.getVelocity())
                });
                if (b.hasSolution()) {
                    ahead(event.getVelocity() + 3);
                    fire(1);
                }

                Query z = new Query("fugir", new Term[]{
                        new org.jpl7.Float(event.getEnergy()),
                });
                if (b.hasSolution()) {
                    turnLeft(-90);
                    ahead(30);
                    fire(1.0);
                }

            }

    */
    import robocode.*;


    public class PrologRobo extends Robot {

        public void run() {
            while (true) {

                if (!Query.hasSolution("consult('test.pl').")) {
                    System.out.println("Consult failed");
                }

                // Lógica do seu robô aqui
                ahead(100); // Avança 100 pixels
                turnGunRight(360); // Gira a torreta completa
            }
        }

        public void onScannedRobot(ScannedRobotEvent e) {
            // Consulta o arquivo Prolog para calcular a velocidade com base na posição do alvo
            double bearing = e.getBearing();
            double velocidade = consultarProlog(bearing);


            Query z = new Query("direcao", new Term[]{
                    new org.jpl7.Float(e.getVelocity()),
            });
            if (z.hasSolution()) {
                ahead(velocidade); // Avança com a velocidade calculada
            }
            else {
                back(-velocidade); // Retrocede com a velocidade calculada
            }

            // Outras ações do seu robô quando um alvo é detectado...
        }

        // Método para consultar o arquivo Prolog e obter a velocidade desejada
        private double consultarProlog(double bearing) {
            Query query = new Query("direcao", new Term[]{("test.pl")});
            if (query.hasSolution()) {
                Query queryVelocidade = new Query("velocidade"), new Float("velocidade"),
                if (queryVelocidade.hasSolution()) {
                    return queryVelocidade.oneSolution().get("Velocidade").floatValue(); // Retorna a velocidade calculada
                }
            }
            return 0; // Retorna 0 se não houver solução ou ocorrer algum erro
        }
    }