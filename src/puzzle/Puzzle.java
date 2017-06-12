package puzzle;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import java.io.File;

/**
 * Created by Toni on 01/6/17.
 */
public class Puzzle extends JFrame implements KeyListener {

    private Tablero tablero;

    public Puzzle() {
        super("Practica Final - Puzzle");
        tablero = new Tablero();
        tablero.setFocusable(true);
        tablero.addKeyListener(this);
        initComponents();

        this.getContentPane().add(tablero);
        this.setSize(tablero.getPreferredSize());
        this.setResizable(false);
        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        Puzzle puzzle = new Puzzle();
        puzzle.setVisible(true);
    }

    //Configurar menú
    private void initComponents() {

        JMenuBar menuBar = new JMenuBar();

        JMenu menuMenu = new JMenu();

        JMenuItem menuItemCambiarImagen = new JMenuItem();
        JMenuItem menuItemMezclar = new JMenuItem();
        JMenuItem menuItemSalir = new JMenuItem();

        getContentPane().add(menuBar);

        menuItemCambiarImagen.setText("Cambiar imagen");
        menuItemCambiarImagen.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                showSelectFileDialog();
            }
        });

        menuItemMezclar.setText("Mezclar");
        menuItemMezclar.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                tablero.desordenar();
                tablero.trozearImagen();
            }
        });

        menuItemSalir.setText("Salir");
        menuItemSalir.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                System.exit(0);
            }
        });

        menuMenu.setText("Menu");
        menuMenu.add(menuItemCambiarImagen);
        menuMenu.add(menuItemMezclar);
        menuMenu.add(menuItemSalir);

        menuBar.add(menuMenu);

        setJMenuBar(menuBar);
    }

    //Mostrar dialogo de selección de ruta, para guardar el fichero
    private void showSelectFileDialog() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar imagen");

        //Solo se pueden seleccionar imágenes JPG
        fileChooser.setFileFilter(new FileNameExtensionFilter("Imagen", "jpg"));

        int userSelection = fileChooser.showOpenDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File imagenSeleccionada = fileChooser.getSelectedFile();
            tablero.setImagen(imagenSeleccionada.getAbsolutePath());
            tablero.iniciar();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        try {
            tablero.moverPieza(e);
        } catch (MovimientoIncorrecto mi) {
            System.err.println(mi.getMessage());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}

