
package org.emp.gl.core.launcher;

import org.emp.gl.clients.Horloge;
import org.emp.gl.clients.HorlogeGUI;

import org.emp.gl.time.service.impl.DummyTimeServiceImpl;
import org.emp.gl.timer.service.TimerService;

import javax.swing.SwingUtilities;

/**
 * Classe principale (Amorce) de l'application.
 * Son rôle est d'initialiser le service de temps (le Sujet)
 * et de "connecter" les différents observateurs (orloge console, Horloge GUI)
 * à ce service.
 *
 * @author DJEMMANE Youcef
 */
public class App {

    /**
     * Point d'entrée de l'application.
     *
     * @param args Arguments de la ligne de commande (non utilisés).
     */
    public static void main(String[] args) {
        
        //  1. Le Sujet (Le "Sujet") ---
        // Instancier le service UNE SEULE FOIS. C'est le "Sujet"
        // que tous les autres vont observer.
        TimerService timerService = new DummyTimeServiceImpl();

        //  2. Le Premier Observateur (Console) ---
        // Créer une horloge console et l'abonner au service
        Horloge consoleHorloge = new Horloge("Horloge Console", timerService);
        consoleHorloge.afficherHeure();
        

        // 3. Le Deuxième Observateur (Graphique) ---
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Créer l'horloge GUI et l'abonner au MÊME service
                HorlogeGUI guiHorloge = new HorlogeGUI("Horloge GUI", timerService);
                
                // Rendre la fenêtre visible
                guiHorloge.showWindow();
            }
        });

        
    }

    
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}