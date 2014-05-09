/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package project.card;
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
public class ActualCards {
    
    
    BufferedImage img = null;
    {
        try {
            img = ImageIO.read(new File("/Users/benjaminstrick/Desktop/214 IMAGES/Allison Hall.jpeg"));
        } catch (IOException ex) {
            Logger.getLogger(ActualCards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    
    Card LakefillGoose = new Card("Lakefill Goose", 1, 
            img, "none", ability.none);
    
    
}