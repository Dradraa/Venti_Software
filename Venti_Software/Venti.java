import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.prefs.Preferences;

public class Venti extends JFrame {

    private static final String WINDOW_X_KEY = "window_x";
    private static final String WINDOW_Y_KEY = "window_y";
    private int initialX, initialY;

    public Venti() {
        super("Venti");
        initUI();
    }

    private void initUI() {
        // Configuration de la fenêtre
        setSize(650, 450);
        setUndecorated(true); // Supprimer la décoration de la fenêtre
        setBackground(new Color(0, 0, 0, 0)); // Rendre la fenêtre transparente
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Chargement de l'icône de la fenêtre
        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/venti.png"));
        Image image = imageIcon.getImage();
        setIconImage(image);

        // Initialisation des composants de l'interface utilisateur
        initComponents();

        // Récupération de la position enregistrée de la fenêtre depuis les préférences utilisateur
        Preferences prefs = Preferences.userNodeForPackage(Venti.class);
        int savedX = prefs.getInt(WINDOW_X_KEY, 0);
        int savedY = prefs.getInt(WINDOW_Y_KEY, 0);

        // Positionnement enregistré ou par défaut
        setLocation(savedX, savedY);

        // Ajout du MouseListener pour permettre le déplacement de la fenêtre
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                initialX = e.getX();
                initialY = e.getY();
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                int newX = getLocation().x + e.getX() - initialX;
                int newY = getLocation().y + e.getY() - initialY;
                setLocation(newX, newY);
                saveWindowPosition(); // Sauvegarde la position lors du déplacement
            }
        });

        // Création d'un InputMap pour le JFrame
        InputMap inputMap = this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        KeyStroke escapeKey = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0); // Touche Échap

        // Mapper la touche Échap à l'action "closeWindow"
        inputMap.put(escapeKey, "closeWindow");

        // Création d'une ActionMap pour le JFrame
        ActionMap actionMap = this.getRootPane().getActionMap();

        // Associer l'action "closeWindow" à la fermeture de la fenêtre
        actionMap.put("closeWindow", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Fermer la fenêtre
            }
        });

        // Rendre la fenêtre visible
        setVisible(true);
    }

    private void initComponents() {
        // Création d'un JLabel pour afficher une image
        JLabel label = new JLabel();
        try {
            // Chargement de l'image depuis un fichier
            Image image = ImageIO.read(getClass().getResource("/venti.png"));
            label.setIcon(new ImageIcon(image));
        } catch (IOException e) {
            e.printStackTrace();
        }
        label.setBounds(0, 0, getWidth(), getHeight());
        // Ajout du JLabel à la fenêtre
        add(label);
    }

    // Méthode pour sauvegarder la position de la fenêtre dans les préférences utilisateur
    private void saveWindowPosition() {
        Preferences prefs = Preferences.userNodeForPackage(Venti.class);
        prefs.putInt(WINDOW_X_KEY, getX());
        prefs.putInt(WINDOW_Y_KEY, getY());
    }

    public static void main(String[] args) {
        // Utiliser SwingUtilities.invokeLater pour démarrer l'interface graphique Swing sur le thread de l'EDT
        SwingUtilities.invokeLater(() -> {
            Venti venti = new Venti();
            // Ajout d'un WindowListener pour sauvegarder la position lors de la fermeture de la fenêtre
            venti.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    venti.saveWindowPosition();
                }
            });
        });
    }
}
