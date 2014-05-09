/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package project.card;
import project.card.ActualCards;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import project.card.Card.ability;

/**
 *
 * @author benjaminstrick
 */
public class CardTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
         BufferedImage img = null;
    {
        try {
            img = ImageIO.read(new File("/Users/benjaminstrick/NetBeansProjects/Project-Lima/src/project/214IMAGES/Lakefill Goose.jpeg"));
            // not sure how to reference the files in respect to the folder in the project folder, have to use your full extension
        } catch (IOException ex) {
            Logger.getLogger(ActualCards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
        RegCard LakefillGoose = new RegCard("Lakefill Goose", 1, img, 2, 1, "", ability.none);
        System.out.println(LakefillGoose);
        
        
        
        
        
    }
    
}
